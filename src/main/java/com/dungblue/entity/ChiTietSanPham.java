package com.dungblue.entity;

public class ChiTietSanPham {
	private int maChiTiet;
	private int maSanPham;
	private String tensp;
	private int maDonHang;
	private int soLuong;
	private double gia;
	
	public ChiTietSanPham(int maChiTiet, int maSanPham, int maDonHang, int soLuong, double gia, String tensp) {
		this.maChiTiet = maChiTiet;
		this.maSanPham = maSanPham;
		this.maDonHang = maDonHang;
		this.soLuong = soLuong;
		this.gia = gia;
		this.tensp = tensp;
	}
	public ChiTietSanPham() {
	}
	public String getTensp() {
		return tensp;
	}
	public void setTensp(String tensp) {
		this.tensp = tensp;
	}
	public int getMaChiTiet() {
		return maChiTiet;
	}
	public void setMaChiTiet(int maChiTiet) {
		this.maChiTiet = maChiTiet;
	}
	public int getMaSanPham() {
		return maSanPham;
	}
	public void setMaSanPham(int maSanPham) {
		this.maSanPham = maSanPham;
	}
	public int getMaDonHang() {
		return maDonHang;
	}
	public void setMaDonHang(int maDonHang) {
		this.maDonHang = maDonHang;
	}
	public int getSoLuong() {
		return soLuong;
	}
	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}
	public double getGia() {
		return gia;
	}
	public void setGia(double gia) {
		this.gia = gia;
	}
	

}
