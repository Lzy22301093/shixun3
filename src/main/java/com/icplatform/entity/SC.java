package com.icplatform.entity;

import com.icplatform.CompositePrimaryKey.SCId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;


@Entity
@IdClass(SCId.class)
public class SC {


    @Id
    private String sno;
    @Id
    private String cno;

    private String cid;
    private String score;

    public SC(){}

    public String getSno(){return sno;}
    public void setSno(String sno){this.sno = sno;}
    public String getCid(){return cid;}
    public void setCid(String cid){this.cid = cid;}
    public String getCno(){return cno;}
    public void setCno(String cno){this.cno = cno;}
    public String getScore(){return score;}
    public void setScore(String score){this.score = score;}
}
