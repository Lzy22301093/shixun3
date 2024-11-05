package com.icplatform.service;

import com.icplatform.entity.Catalogue;
import com.icplatform.repositories.CatalogueRepositories;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogueService {

    private CatalogueRepositories catalogueRepositories;

    public CatalogueService(CatalogueRepositories catalogueRepositories) {
        this.catalogueRepositories = catalogueRepositories;
    }

    public List<String> getEmptyFoldersByRootPath(String rootPath) {
        // 查询数据库中以 rootPath 开头的所有文件夹路径
        return catalogueRepositories.findEmptyFoldersByRootPath(rootPath);
    }

    public void saveNewFolder(String folderPath) {
        Catalogue catalogue = new Catalogue();
        catalogue.setPath(folderPath);
        catalogueRepositories.save(catalogue);
    }
}
