// entity/SaoLuu.java
package com.dungblue.entity;

import java.util.Date;

public class SaoLuu {
    private String tenFile;
    private Date ngayTao;
    private String kichThuoc;

    public SaoLuu(String tenFile, Date ngayTao, String kichThuoc) {
        this.tenFile = tenFile;
        this.ngayTao = ngayTao;
        this.kichThuoc = kichThuoc;
    }

    // Getters and setters
    public String getTenFile() { return tenFile; }
    public void setTenFile(String tenFile) { this.tenFile = tenFile; }
    public Date getNgayTao() { return ngayTao; }
    public void setNgayTao(Date ngayTao) { this.ngayTao = ngayTao; }
    public String getKichThuoc() { return kichThuoc; }
    public void setKichThuoc(String kichThuoc) { this.kichThuoc = kichThuoc; }
}