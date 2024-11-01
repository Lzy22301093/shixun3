package com.icplatform.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Teaching {

    @Id
    private String cid;

    private String cno;
    private String tno;

    public Teaching(String cid, String cno, String tno) {

        this.cid = cid;
        this.cno = cno;
        this.tno = tno;
    }

    public Teaching() {}

    public String getCid() {return this.cid;}
    public String getCno() {return this.cno;}
    public String getTno() {return this.tno;}
    public void setCid(String cid) {this.cid = cid;}
    public void setCno(String cno) {this.cno = cno;}
    public void setTno(String tno) {this.tno = tno;}

}
