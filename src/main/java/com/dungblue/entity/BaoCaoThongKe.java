// BaoCaoThongKe.java
package com.dungblue.entity;

import java.util.Date;

public class BaoCaoThongKe {
    private Date ngay;
    private double tongDoanhThu;
    private int soDonHang;
    private int soSanPhamBan;

    // Constructors
    public BaoCaoThongKe() {}
    
    public BaoCaoThongKe(Date ngay, double tongDoanhThu, int soDonHang, int soSanPhamBan) {
        this.ngay = ngay;
        this.tongDoanhThu = tongDoanhThu;
        this.soDonHang = soDonHang;
        this.soSanPhamBan = soSanPhamBan;
    }

    // Getters and Setters
    public Date getNgay() { return ngay; }
    public void setNgay(Date ngay) { this.ngay = ngay; }
    public double getTongDoanhThu() { return tongDoanhThu; }
    public void setTongDoanhThu(double tongDoanhThu) { this.tongDoanhThu = tongDoanhThu; }
    public int getSoDonHang() { return soDonHang; }
    public void setSoDonHang(int soDonHang) { this.soDonHang = soDonHang; }
    public int getSoSanPhamBan() { return soSanPhamBan; }
    public void setSoSanPhamBan(int soSanPhamBan) { this.soSanPhamBan = soSanPhamBan; }
}