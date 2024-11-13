package com.icplatform.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Notification {

    @Id
    private int id;

    private String name;
    private String content;
    private String cid;
    private int nid;

    public Notification() {}

    public void setName(String name) { this.name = name; }
    public String getName() { return name; }

    public void setContent(String content) { this.content = content; }
    public String getContent() { return content; }

    public void setCid(String cid) { this.cid = cid; }
    public String getCid() { return cid; }

    public void setNid(int nid) { this.nid = nid; }
    public int getNid() { return nid; }
}
