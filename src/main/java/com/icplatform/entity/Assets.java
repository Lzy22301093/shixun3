package com.icplatform.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.sql.Date;

@Entity
public class Assets {

    @Id
    private int id;

    private String fname;
    private String type;
    private byte[] size;
    private String cid;
    private String tpath;
    private Date time;
    private int aid;

    public Assets() {}

    public String getTpath(){return this.tpath;}
    public String getFname(){return this.fname;}
    public String getType(){return this.type;}
    public Date getTime(){return this.time;}
    public void setTpath(String tpath){this.tpath = tpath;}
    public void setFname(String fname){this.fname = fname;}
    public void setType(String type){this.type = type;}
    public void setSize(byte[] size){this.size = size;}
    public void setTime(Date time){this.time = time;}
    public String getCid(){return this.cid;}
    public void setCid(String cid){this.cid = cid;}
    public byte[] getSize(){return this.size;}
    public void setAid(int aid){this.aid = aid;}
    public int getAid(){return this.aid;}

}
