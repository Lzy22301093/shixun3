package com.icplatform.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Teacher {

    @Id
    private String tno;

    private String tname;
    private String password;
    private String temail;
    private String gender;
    private String major;
    private String title;

    public Teacher(){}

    public Teacher(String tno, String tname, String password) {
        this.tno = tno;
        this.tname = tname;
        this.password = password;
    }

    public String getTno() {return tno;}
    public void setTno(String tno) {this.tno = tno;}
    public String getTname() {return tname;}
    public void setTname(String tname) {this.tname = tname;}
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
    public String getTemail() {return temail;}
    public String getGender() {return gender;}
    public String getMajor() {return major;}
    public String getTitle() {return title;}
}
