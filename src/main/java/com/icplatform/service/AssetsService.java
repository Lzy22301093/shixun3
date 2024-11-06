package com.icplatform.service;

import com.icplatform.entity.Assets;
import com.icplatform.repositories.AssetsRepositories;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class AssetsService {
    private final AssetsRepositories assetsRepositories;

    public AssetsService(AssetsRepositories assetsRepositories) {
        this.assetsRepositories = assetsRepositories;
    }

    public String searchTpathByFname(String fname) {
        return assetsRepositories.findTpathByFname(fname);
    }

    // 更新资源信息
    public void updateAssetByFname(String fname, String type, byte[] size, String tpath, Date time,String cid) {
        Assets assets = assetsRepositories.findByFname(fname);
        if (assets == null) {
            throw new IllegalArgumentException("资源不存在");
        }
        assets.setTpath(tpath);
        assets.setType(type);  // 更新文件类型
        assets.setSize(size);  // 更新文件大小
        assets.setTime(time);  // 更新时间戳
        assets.setCid(cid);//更新cid
        assetsRepositories.save(assets);  // 保存更新后的资产
    }

    // 插入资源信息
    public void insertNewAsset(String fname, String type, byte[] size, String tpath, Date time,String cid) {
        Assets assets = new Assets();
        assets.setFname(fname);
        assets.setType(type);
        assets.setSize(size);
        assets.setTpath(tpath);
        assets.setTime(time);
        assets.setCid(cid);
        assetsRepositories.save(assets);  // 保存新资产
    }

    public Assets getAssetByFname(String fname) {
        return assetsRepositories.findByFname(fname);
    }

    public List<String> getTpathByStarting(String path) {

        String normalizedPath = path.replace("\\", "/");
        return assetsRepositories.findByTpathStartingWith(normalizedPath + "%"); // 添加 % 以匹配所有后续文件
    }

}
