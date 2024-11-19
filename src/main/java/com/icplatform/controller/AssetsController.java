package com.icplatform.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.reflect.TypeToken;
import com.icplatform.dto.DownloadLinkResponse;
import com.icplatform.dto.PreviewLinkResponse;
import com.icplatform.service.AssetsService;
import com.icplatform.service.CatalogueService;
import com.icplatform.utils.GsonUtil;
import com.icplatform.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import com.icplatform.dto.FileUploadResponse;


@RestController
@RequestMapping("/api/assets")
@CrossOrigin(origins = "*")
public class AssetsController {

    @Value("${files.upload.path}")
    private String uploadPath;

    @Autowired
    private AssetsService assetsService;

    @Autowired
    private CatalogueService catalogueService;

    //上传资源并更新数据库
    @PostMapping("/upload")
    public FileUploadResponse uploadFile(@RequestHeader Map<String, String> header, @RequestParam("file") MultipartFile file, @RequestParam("cid") String cid, @RequestParam("tpath") String tpath, @RequestParam("aid") int aid) throws IOException {

        String token = header.get("token");
        DecodedJWT decodedJWT;
        try {
            decodedJWT = JWTUtil.verifyToken(token);
        } catch (Exception e) {
            return new FileUploadResponse("error", "token已被清除或已过期");
        }

        String username = decodedJWT.getClaim("username").asString();
        int userType = decodedJWT.getClaim("usertype").asInt();

        if (userType == 1) {
            String originalFilename = file.getOriginalFilename();
            byte[] fileSize = file.getBytes();
            String fileType = file.getContentType();
            Date currentTime = new Date(System.currentTimeMillis());

            // 设置文件夹路径
            String courseResourcePath = tpath + '/' + originalFilename;

            System.out.println(tpath);
            System.out.println(courseResourcePath);

            File courseDir = new File(courseResourcePath);
           // File courseDir = new File(tpath).getParentFile();
            System.out.println(courseDir.getAbsolutePath());

            // 如果文件夹不存在则创建
            if (!courseDir.exists()) {
                courseDir.mkdirs();
            }
            // 设置上传文件路径
            File uploadFile = new File(courseDir.getAbsolutePath());

            // 如果文件已存在，删除旧文件
            if (uploadFile.exists()) {
                uploadFile.delete();
            }

            // 保存新文件
            file.transferTo(uploadFile);

            // 检查数据库中是否存在相同的 fname
            try {
                // 存在相同的 fname 则更新记录
                assetsService.updateAssetByFname(originalFilename, fileType, fileSize, uploadFile.getAbsolutePath().replace("\\", "/"), currentTime, cid, aid); // 将路径中的反斜杠替换为正斜杠
            } catch (IllegalArgumentException e) {
                // 不存在相同的 fname 则插入新记录
                assetsService.insertNewAsset(originalFilename, fileType, fileSize, uploadFile.getAbsolutePath().replace("\\", "/"), currentTime, cid, aid); // 将路径中的反斜杠替换为正斜杠
            }

            String newToken = JWTUtil.generateToken(userType, username);
            return new FileUploadResponse("success", "上传成功",newToken);

        } else {
            return new FileUploadResponse("error", "用户权限不足");
        }
    }

    //返回文件目录结构
    @GetMapping("/catalogue")
    public Map<String, Object> getCatalogue(@RequestHeader Map<String, String> header, @RequestParam String path) {
        String token = header.get("token");
        DecodedJWT decodedJWT;
        try {
            decodedJWT = JWTUtil.verifyToken(token);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "token已被清除或已过期");
            return response;
        }

        String username = decodedJWT.getClaim("username").asString();
        int userType = decodedJWT.getClaim("usertype").asInt();

        if (userType == 0 || userType == 1) {
            // 标准化路径
            String normalizedPath = normalizePath(path);

            System.out.println(normalizedPath);

            List<String> filePaths = assetsService.getTpathByStarting(normalizedPath);

            System.out.println(filePaths);

            List<FileNode> folderStructure = buildFolderStructure(filePaths, normalizedPath);

            String newToken = JWTUtil.generateToken(userType, username);
            Map<String, Object> response = new HashMap<>();
            response.put("folderStructure", folderStructure);
            response.put("status", "success");
            response.put("newToken", newToken);
            return response;
        } else {
            return null; // 可以考虑返回一个明确的错误信息
        }
    }

    // 定义文件和文件夹的结构类
    public static class FileNode {
        private String label; // 文件/文件夹名称
        private String type;  // "directory" or "file"
        private List<FileNode> children; // 子节点
        private String path;  // 文件的完整路径
        private int aid; //文件aid

        public FileNode() {}

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<FileNode> getChildren() {
            return children;
        }

        public void setChildren(List<FileNode> children) {
            this.children = children;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public int getAid() { return aid; }

        public void setAid(int aid) { this.aid = aid; }
    }

    // 创建目录结构
    private List<FileNode> buildFolderStructure(List<String> filePaths, String rootPath) {
        Map<String, FileNode> directoryMap = new HashMap<>();

        // 初始化根节点
        FileNode rootNode = new FileNode();
        rootNode.setLabel(new File(rootPath).getName());
        rootNode.setType("directory");
        rootNode.setChildren(new ArrayList<>());
        rootNode.setPath(rootPath);
        directoryMap.put(rootPath, rootNode);


        // 从数据库中的文件路径构建文件树
        for (String filePath : filePaths) {
            String normalizedFilePath = filePath.replace("\\", "/");
            String relativePath = normalizedFilePath.replaceFirst(rootPath.replace("\\", "/") + "/", "");
            String[] parts = relativePath.split("/");

            FileNode currentNode = rootNode;

            for (int i = 0; i < parts.length; i++) {
                String part = parts[i];
                boolean isFile = (i == parts.length - 1);
                String currentPath = currentNode.getPath() + "/" + part;

                FileNode nextNode = directoryMap.computeIfAbsent(currentPath, k -> {

                    FileNode node = new FileNode();
                    node.setLabel(part);
                    node.setType(isFile ? "file" : "directory");
                    node.setPath(currentPath);



                    if (!isFile) {
                        node.setChildren(new ArrayList<>());
                    }
                    return node;
                });
                if(isFile){
                    System.out.println("路径： "+nextNode.getPath());
                    int aid = assetsService.searchAidByTpath(nextNode.getPath());

                    nextNode.setAid(aid);
                }

                if (!currentNode.getChildren().contains(nextNode)) {
                    currentNode.getChildren().add(nextNode);
                }
                currentNode = nextNode;
            }
        }

        // 从数据库中获取空文件夹并添加到文件树
        addEmptyFoldersFromDB(rootPath, directoryMap);

        return new ArrayList<>(rootNode.getChildren());
    }

    // 从数据库中获取空文件夹并添加到文件结构
    private void addEmptyFoldersFromDB(String rootPath, Map<String, FileNode> directoryMap) {
        List<String> emptyFolderPaths = catalogueService.getEmptyFoldersByRootPath(rootPath); // 获取数据库中以 rootPath 开头的空文件夹路径

        for (String folderPath : emptyFolderPaths) {
            String normalizedFolderPath = folderPath.replace("\\", "/");

            if (!directoryMap.containsKey(normalizedFolderPath)) {
                String relativePath = normalizedFolderPath.replaceFirst(rootPath.replace("\\", "/") + "/", "");
                String[] parts = relativePath.split("/");

                FileNode currentNode = directoryMap.get(rootPath);
                for (String part : parts) {
                    String currentPath = currentNode.getPath() + "/" + part;

                    FileNode nextNode = directoryMap.computeIfAbsent(currentPath, k -> {

                        FileNode node = new FileNode();
                        node.setLabel(part);
                        node.setType("directory");
                        node.setPath(currentPath);
                        node.setChildren(new ArrayList<>());
                        return node;
                    });

                    if (!currentNode.getChildren().contains(nextNode)) {
                        currentNode.getChildren().add(nextNode);
                    }
                    currentNode = nextNode;
                }
            }
        }
    }

    // 标准化路径
    private String normalizePath(String path) {
        // 替换所有的 / 和 \ 为 /
        String normalized = path.replaceAll("[/\\\\]+", "/");
        // 去除路径末尾的多余斜杠（如果有）
        if (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        return normalized;
    }



    //下载资源
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String filePath) {
        File file = new File(filePath);
        Resource resource = new FileSystemResource(file);

        if (!resource.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("status","error")
                    .build();
        }

        // 设置 Content-Disposition 响应头
        String filename = URLEncoder.encode(file.getName(), StandardCharsets.UTF_8);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

        // 返回文件资源及新的 token
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("status","success")
                .body(resource);
    }

    //生成资源下载链接
    @GetMapping("/generateDownloadLink")
    public ResponseEntity<DownloadLinkResponse> generateDownloadLink(@RequestHeader Map<String, String> header, @RequestParam String cid, @RequestParam int aid) {
        String token = header.get("token");
        DecodedJWT decodedJWT;

        try {
            decodedJWT = JWTUtil.verifyToken(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DownloadLinkResponse("", "error"));
        }

        String username = decodedJWT.getClaim("username").asString();
        int userType = decodedJWT.getClaim("usertype").asInt();

        if (userType == 0 || userType == 1) {
            // 根据 cid, workid 和 sno 查找文件路径
            String filePath = assetsService.searchTpathByCidAndAid(cid, aid);
            System.out.println("文件路径: " + filePath);

            if (filePath == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DownloadLinkResponse("", "error"));
            }

            // 替换路径中的反斜杠为正斜杠
            filePath = filePath.replace("\\", "/");

            // 获取当前 IP 地址
            String ipAddress;
            try {
                ipAddress = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DownloadLinkResponse("", "error"));
            }

            // 构建下载链接
            String downloadUrl = "http://" + ipAddress + ":8080/api/homework/download?filePath=" + URLEncoder.encode(filePath, StandardCharsets.UTF_8);
            System.out.println(downloadUrl);

            // 生成新的 token
            String newToken = JWTUtil.generateToken(userType, username);

            return ResponseEntity.ok(new DownloadLinkResponse(downloadUrl, "success", newToken));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new DownloadLinkResponse("", "error"));
    }

    //新建资源文件夹
    @PostMapping("/folder/create")
    public Map<String, String> creatingNewFolder(@RequestHeader Map<String, String> header, @RequestBody Map<String, String> folderData) {//foldData中应当含有 folderPath
        String token = header.get("token");
        DecodedJWT decodedJWT;

        try {
            decodedJWT = JWTUtil.verifyToken(token);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status","error");
            response.put("message","token已被清除或已过期");
            return response;
        }

        String username = decodedJWT.getClaim("username").asString();
        int userType = decodedJWT.getClaim("usertype").asInt();

        if(userType == 1) {

            String folderPath = folderData.get("folderPath");

            String courseResourcePath = folderPath;  // 使用正斜杠

            System.out.println(courseResourcePath);

            File courseDir = new File(courseResourcePath);

            if (!courseDir.exists()) {
                courseDir.mkdirs();

                catalogueService.saveNewFolder(folderPath);
            }

            String newToken = JWTUtil.generateToken(userType, username);

            Map<String, String> response = new HashMap<>();
            response.put("status","success");
            response.put("message","创建文件夹成功");
            response.put("newToken", newToken);
            return response;
        }else{
            Map<String, String> response = new HashMap<>();
            response.put("status","error");
            response.put("message","新建文件夹失败");
            return response;
        }
    }

    //生成预览链接
    @GetMapping("/preview")
    public ResponseEntity<PreviewLinkResponse> generatePreviewLink(@RequestHeader Map<String, String> header, @RequestParam String cid, @RequestParam int aid) {
        System.out.println("预览");
        String token = header.get("token");
        DecodedJWT decodedJWT;

        try {
            decodedJWT = JWTUtil.verifyToken(token);
        } catch (Exception e) {
            System.out.println("预览");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new PreviewLinkResponse("", "error"));
        }

        String username = decodedJWT.getClaim("username").asString();
        int userType = decodedJWT.getClaim("usertype").asInt();

        if (userType == 0 || userType == 1) {
            System.out.println("预览1");
            // 根据 cid 和 aid 查找文件路径
            String filePath = assetsService.searchTpathByCidAndAid(cid, aid);
            if (filePath == null) {
                System.out.println("路径是空的");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PreviewLinkResponse("", "error"));
            }

            // 替换路径中的反斜杠为正斜杠
            filePath = filePath.replace("\\", "/");
            System.out.println("预览2");
            // 获取当前 IP 地址
            String ipAddress;
            try {
                ipAddress = InetAddress.getLocalHost().getHostAddress();
                System.out.println("预览3");
            } catch (UnknownHostException e) {
                System.out.println("无法获取ip");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new PreviewLinkResponse("", "error"));
            }

            // 生成预览链接，编码路径确保格式正确
            String previewUrl = "http://" + ipAddress + ":8080/api/assets/preview/pdf?filePath=" + URLEncoder.encode(filePath, StandardCharsets.UTF_8);
            String newToken = JWTUtil.generateToken(userType, username);
            System.out.println(previewUrl);

            return ResponseEntity.ok(new PreviewLinkResponse(previewUrl, "success", newToken));
        }
        System.out.println("权限错误");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new PreviewLinkResponse("", "error"));
    }





    //批量删除资源
    @PostMapping("/delete")
    public Map<String, Object> AssetsDelete(@RequestHeader Map<String, String> header, @RequestBody Map<String, Object> fileData) {
        String token = header.get("token");
        DecodedJWT decodedJWT;

        try {
            decodedJWT = JWTUtil.verifyToken(token);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "token已被清除或已过期");
            return response;
        }

        String username = decodedJWT.getClaim("username").asString();
        int userType = decodedJWT.getClaim("usertype").asInt();

        if (userType == 1) {

            Type listOfStringType = new TypeToken<List<String>>() {}.getType();
            Type listOfIntegerType = new TypeToken<List<Integer>>() {}.getType();

            // 转换 JSON 数据
            List<String> cids = GsonUtil.fromJson(GsonUtil.toJson(fileData.get("cid")), listOfStringType);
            List<Double> aidDoubles = GsonUtil.fromJson(GsonUtil.toJson(fileData.get("aid")), new TypeToken<List<Double>>() {}.getType());

            // 转换 List<Double> 为 List<Integer>
            List<Integer> aids = aidDoubles.stream()
                    .map(Double::intValue)  // 将 Double 转为 Integer
                    .collect(Collectors.toList());

            if (cids == null || aids == null || cids.size() != aids.size()) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "传入的cid和aid数量不匹配");
                return response;
            }

            boolean allDeleted = true;
            for (int i = 0; i < cids.size(); i++) {
                String cid = cids.get(i);
                int aid = aids.get(i);

                // 查询路径
                String tpath = assetsService.searchTpathByCidAndAid(cid, aid);
                if (tpath != null) {
                    // 删除数据库记录和文件
                    assetsService.deleteAssetByCidAndAid(cid, aid);
                    File file = new File(tpath);

                    if (file.exists() && !file.delete()) {
                        allDeleted = false;
                    }
                } else {
                    allDeleted = false;
                }
            }

            String newToken = JWTUtil.generateToken(userType, username);
            Map<String, Object> response = new HashMap<>();
            response.put("status", allDeleted ? "success" : "partial_success");
            response.put("message", allDeleted ? "文件删除成功" : "文件删除失败");
            response.put("newToken", newToken);
            return response;
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", "权限不足,删除失败");
        return response;
    }

}
