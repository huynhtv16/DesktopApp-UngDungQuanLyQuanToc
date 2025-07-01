package com.dungblue.entity;

import java.sql.Date;
import java.sql.Timestamp;

public class DonHang {
    private int madonhang;
    private Date ngaydathang;
    private double tongtien;
    private int makhachhang;
    private int manguoidung;

    // Constructor, getter, setter (đặt tên theo camelCase)
    public DonHang() {
	}
    public DonHang(int madonhang, Date ngaydathang, double tongtien, int makhachhang, int manguoidung) {
		this.madonhang = madonhang;
		this.ngaydathang = ngaydathang;
		this.tongtien = tongtien;
		this.makhachhang = makhachhang;
		this.manguoidung = manguoidung;
	}
    // Ví dụ: getMadonhang(), setMadonhang(...)
    public int getMaDonHang() {
		return madonhang;
	}
    	public void setMaDonHang(int madonhang) {
		this.madonhang = madonhang;
	}

	public double getTongTien() {
		return tongtien;
	}
		public void setTongTien(double tongtien) {
		this.tongtien = tongtien;
	}
	public int getMaKhachHang() {
		return makhachhang;
	}
		public void setMaKhachHang(int makhachhang) {
		this.makhachhang = makhachhang;
	}
	public int getMaNguoiDung() {
		return manguoidung;
	}
		public void setMaNguoiDung(int manguoidung) {
		this.manguoidung = manguoidung;
	}
		public Date getNgayDatHang() {
		return ngaydathang;
	}
		public void setNgayDatHang(Date ngaydathang) {
		this.ngaydathang = ngaydathang;
	}
		
}