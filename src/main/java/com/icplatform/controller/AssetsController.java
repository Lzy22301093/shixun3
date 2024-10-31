package com.icplatform.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.icplatform.dto.DownloadLinkResponse;
import com.icplatform.dto.DownloadResponse;
import com.icplatform.entity.Assets;
import com.icplatform.service.AssetsService;
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
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.*;

import com.icplatform.dto.FileUploadResponse; // 导入你的响应类
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/assets")
@CrossOrigin(origins = "*")
public class AssetsController {

    @Value("${files.upload.path}")
    private String uploadPath;

    @Autowired
    private AssetsService assetsService;

    //上传资源并更新数据库
    @PostMapping("/upload")
    public FileUploadResponse uploadFile(@RequestHeader Map<String, String> header, @RequestParam MultipartFile file, @RequestParam String cid) throws IOException {

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
            long fileSize = file.getSize();
            String fileType = file.getContentType();
            Date currentTime = new Date(System.currentTimeMillis());

            // 设置文件夹路径：E:/ICPlatformStorage/CourseResources/cid
            String courseResourcePath = "E:/ICPlatformStorage/CourseResources/" + cid;  // 使用正斜杠
            File courseDir = new File(courseResourcePath);

            // 如果文件夹不存在则创建
            if (!courseDir.exists()) {
                courseDir.mkdirs();
            }
            // 设置上传文件路径
            File uploadFile = new File(courseDir, originalFilename);

            // 如果文件已存在，删除旧文件
            if (uploadFile.exists()) {
                uploadFile.delete();
            }

            // 保存新文件
            file.transferTo(uploadFile);

            // 检查数据库中是否存在相同的 fname
            try {
                // 存在相同的 fname 则更新记录
                assetsService.updateAssetByFname(originalFilename, fileType, fileSize, uploadFile.getAbsolutePath().replace("\\", "/"), currentTime, cid); // 将路径中的反斜杠替换为正斜杠
            } catch (IllegalArgumentException e) {
                // 不存在相同的 fname 则插入新记录
                assetsService.insertNewAsset(originalFilename, fileType, fileSize, uploadFile.getAbsolutePath().replace("\\", "/"), currentTime, cid); // 将路径中的反斜杠替换为正斜杠
            }

            String newToken = JWTUtil.generateToken(userType, username);
            return new FileUploadResponse("success", "上传成功",newToken);

        } else {
            return new FileUploadResponse("error", "用户权限不足");
        }
    }


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

        if (userType == 0) {
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

        // Constructors, getters and setters
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

        for (String filePath : filePaths) {
            String normalizedFilePath = filePath.replace("\\", "/");
            String relativePath = normalizedFilePath.replaceFirst(rootPath.replace("\\", "/") + "/", "");
            String[] parts = relativePath.split("/");

            // 从根节点开始构建路径
            FileNode currentNode = rootNode;

            for (int i = 0; i < parts.length; i++) {
                String part = parts[i];
                boolean isFile = (i == parts.length - 1);
                String currentPath = currentNode.getPath() + "/" + part;

                // 如果当前路径不存在，则创建新节点
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

                // 将新节点添加到当前节点的子节点中
                if (!currentNode.getChildren().contains(nextNode)) {
                    currentNode.getChildren().add(nextNode);
                }
                currentNode = nextNode;
            }
        }

        return new ArrayList<>(rootNode.getChildren());
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
    public ResponseEntity<Resource> downloadFile(@RequestHeader Map<String, String> header, @RequestParam String filePath) {
        System.out.println(filePath);

        // 从请求头中获取 token
        String token = header.get("token");
        DecodedJWT decodedJWT;

        try {
            decodedJWT = JWTUtil.verifyToken(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .header("status","error")
                    .build();
        }

        // 获取用户信息
        String username = decodedJWT.getClaim("username").asString();
        int userType = decodedJWT.getClaim("usertype").asInt();

        // 检查用户权限
        if (userType == 0 || userType == 1) {
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

            // 生成新的 token
            String newToken = JWTUtil.generateToken(userType, username); // 请确保这个方法存在并适合你的业务逻辑

            // 返回文件资源及新的 token
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header("newToken", newToken)
                    .header("status","success")
                    .body(resource);
        }

        // 用户权限不足
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .header("newToken", null)
                .header("status","error")
                .build();
    }

    //生成资源下载链接
    @GetMapping("/generateDownloadLink")
    public ResponseEntity<DownloadLinkResponse> generateDownloadLink(@RequestHeader Map<String, String> header, @RequestParam String fileName) {
        String token = header.get("token");
        DecodedJWT decodedJWT;

        try {
            decodedJWT = JWTUtil.verifyToken(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DownloadLinkResponse("error", "无效的Token"));
        }

        String username = decodedJWT.getClaim("username").asString();
        int userType = decodedJWT.getClaim("usertype").asInt();

        if (userType == 0 || userType == 1) {
            String correctedFileName = null;

            // 解码文件名
            String decodedFname = null;
            try {
                decodedFname = URLDecoder.decode(fileName, StandardCharsets.UTF_8.name());
                // 将空格替换回加号
                correctedFileName = decodedFname.replace(" ", "+");
                System.out.println(correctedFileName);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DownloadLinkResponse("error", "文件名解码失败"));
            }

            System.out.println("解码后的文件名: " + correctedFileName);
            String filePath = assetsService.searchTpathByFname(correctedFileName);
            System.out.println("文件路径: " + filePath);

            if (filePath == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DownloadLinkResponse("error", "文件未找到"));
            }

            filePath = filePath.replace("\\", "/");

            String ipAddress;
            try {
                ipAddress = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DownloadLinkResponse("error", "无法获取IP地址"));
            }

            String downloadUrl = "http://" + ipAddress + ":8080/api/assets/download?filePath=" + URLEncoder.encode(filePath, StandardCharsets.UTF_8);
            String newToken = JWTUtil.generateToken(userType,username);

            return ResponseEntity.ok(new DownloadLinkResponse("success", downloadUrl,newToken));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new DownloadLinkResponse("error", "用户权限不足"));
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

}
