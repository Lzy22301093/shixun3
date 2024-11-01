package com.icplatform.entity;


import com.icplatform.CompositePrimaryKey.CourseId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

@Entity
@IdClass(CourseId.class)
public class Course {

    @Id
    private String cno;
    @Id
    private String cid;

    private String cname;
    private String tno;
    private String major;
    private String description;

    public Course(){}

    public String getCno() {return cno;}
    public void setCno(String cno) {this.cno = cno;}
    public String getCid() {return cid;}
    public void setCid(String cid) {this.cid = cid;}
    public String getCname() {return cname;}
    public void setCname(String cname) {this.cname = cname;}
    public String getTno() {return tno;}
    public void setTno(String tno) {this.tno = tno;}
    public String getMajor() {return major;}
    public void setMajor(String major) {this.major = major;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

}
