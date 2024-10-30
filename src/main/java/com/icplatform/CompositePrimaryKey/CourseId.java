package com.icplatform.CompositePrimaryKey;

import java.io.Serializable;
import java.util.Objects;

public class CourseId implements Serializable {
    private String cno;
    private String cid;

    public CourseId() {}

    public CourseId(String cno, String cid) {
        this.cno = cno;
        this.cid = cid;
    }

    // 重写 equals 和 hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseId courseId = (CourseId) o;
        return Objects.equals(cno, courseId.cno) && Objects.equals(cid, courseId.cid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cno, cid);
    }

    // Getter 和 Setter
    public String getCno() {
        return cno;
    }

    public void setCno(String cno) {
        this.cno = cno;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
}
