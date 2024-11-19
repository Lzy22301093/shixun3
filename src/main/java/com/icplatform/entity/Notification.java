package com.icplatform.entity;

import cn.hutool.core.date.DateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Notification {

    @Id
    private int id;

    private String name;
    private String content;
    private String cid;
    private LocalDateTime time;
    private String tno;
    private int nid;

    public Notification() {}

    public void setName(String name) { this.name = name; }
    public String getName() { return name; }

    public void setContent(String content) { this.content = content; }
    public String getContent() { return content; }

    public void setCid(String cid) { this.cid = cid; }
    public String getCid() { return cid; }

    public void setTime(LocalDateTime time) { this.time = time; }
    public LocalDateTime getTime() { return time; }

    public void setTno(String tno) { this.tno = tno; }
    public String getTno() { return tno; }

    public void setNid(int nid) { this.nid = nid; }
    public int getNid() { return nid; }
}
