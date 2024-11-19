package com.icplatform.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Video {

    @Id
    private int id;

    private String vname;
    private String path;
    private String cid;
    private int vid;
    private LocalDateTime start;
    private LocalDateTime end;

    public Video(){}

    public void setVid(int vid) { this.vid = vid; }
    public int getVid() { return vid; }

    public void setVname(String vname) { this.vname = vname; }
    public String getVname() { return vname; }

    public void setPath(String path) { this.path = path; }
    public String getPath() { return path; }

    public void setCid(String cid) { this.cid = cid; }
    public String getCid() { return cid; }

    public void setStart(LocalDateTime start) { this.start = start; }
    public LocalDateTime getStart() { return start; }

    public void setEnd(LocalDateTime end) { this.end = end; }
    public LocalDateTime getEnd() { return end; }
}
