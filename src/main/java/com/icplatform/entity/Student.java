package com.icplatform.entity;


import jakarta.persistence.*;

import java.util.List;

@Entity
public class Student {

    @Id//主键
    private String sno;
    private String sname;
    private String password;
    private String semail;
    private String gender;
    private String major;


/*    @ManyToMany
    @JoinTable(
            name = "sc",
            joinColumns = @JoinColumn(name = "sno"),
            inverseJoinColumns = @JoinColumn(name = "cid")
    )
    public List<Teaching> Students;*/

    public Student(){}

    public Student(String sno,String sname,String password) {
        this.sno = sno;
        this.sname = sname;
        this.password = password;
    }

    public String getSno(){return sno;}
    public String getSname(){return sname;}
    public String getPassword(){return password;}
    public void setPassword(String password) { this.password = password;}
    public String getSemail(){return semail;}
    public String getGender(){return gender;}
    public String getMajor(){return major;}
}
