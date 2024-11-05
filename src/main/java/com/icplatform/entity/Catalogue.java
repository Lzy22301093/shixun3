package com.icplatform.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Catalogue {

    @Id
    private int id;

    private String path;

    public Catalogue() {}

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
}
