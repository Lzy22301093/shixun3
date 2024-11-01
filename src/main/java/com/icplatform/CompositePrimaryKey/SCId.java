package com.icplatform.CompositePrimaryKey;

import java.io.Serializable;
import java.util.Objects;

public class SCId implements Serializable {
    private String sno;
    private String cno;

    public SCId() {}

    public SCId(String sno, String cno) {
        this.sno = sno;
        this.cno = cno;
    }

    // 重写 equals 和 hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SCId scId = (SCId) o;
        return Objects.equals(sno, scId.sno) && Objects.equals(cno, scId.cno);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sno, cno);
    }

    // Getter 和 Setter
    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getCno() {
        return cno;
    }

    public void setCno(String cno) {
        this.cno = cno;
    }
}