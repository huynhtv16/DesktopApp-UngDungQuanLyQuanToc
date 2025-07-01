package com.dungblue.entity;

public class SanPham {
    private int maSanPham;
    private String tenSanPham;
    private double giaNhap;
    private double giaBan;
    private int soLuong;
    private int maDanhMuc;
    private String duongdan;
    
    // Constructor, getter, setter

// Constructor
	public SanPham(int maSanPham, String tenSanPham, double giaNhap, double giaBan, int soLuong, int maDanhMuc, String duongdan) {
		this.maSanPham = maSanPham;
		this.tenSanPham = tenSanPham;
		this.giaNhap = giaNhap;
		this.giaBan = giaBan;
		this.soLuong = soLuong;
		this.maDanhMuc = maDanhMuc;
		this.duongdan = duongdan;
	}
	public SanPham() {
		// Constructor rỗng
	}
	
	// Getter and Setter methods
	public int getMaSanPham() {
		return maSanPham;
	}

	public void setMaSanPham(int maSanPham) {
		this.maSanPham = maSanPham;
	}

	public String getTenSanPham() {
		return tenSanPham;
	}

	public void setTenSanPham(String tenSanPham) {
		this.tenSanPham = tenSanPham;
	}

	public double getGiaNhap() {
		return giaNhap;
	}

	public void setGiaNhap(double giaNhap) {
		this.giaNhap = giaNhap;
	}

	public double getGiaBan() {
		return giaBan;
	}
	public void setGiaBan(double giaBan) {
		this.giaBan = giaBan;
	}
	public int getSoLuong() {
		return soLuong;
	}
	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}
	public int getMaDanhMuc() {
		return maDanhMuc;
	}
	public void setMaDanhMuc(int maDanhMuc) {
		this.maDanhMuc = maDanhMuc;
	}
	public String getDuongdan() {
		return duongdan;
	}
	public void setDuongdan(String duongdan) {
		this.duongdan = duongdan;
	}
	
}