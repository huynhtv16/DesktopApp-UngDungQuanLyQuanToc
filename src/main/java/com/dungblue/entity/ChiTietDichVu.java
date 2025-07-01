package com.dungblue.entity;

public class ChiTietDichVu {
		private int maChiTietDichVu;
	private int maDichVu;
	private String tenDichVu;
	private int maDonHang;
	private int soLuong;
	private double gia;

	public ChiTietDichVu(int maChiTietDichVu, int maDichVu, int maDonHang, int soLuong, double gia, String tenDichVu) {
		this.maChiTietDichVu = maChiTietDichVu;
		this.maDichVu = maDichVu;
		this.maDonHang = maDonHang;
		this.soLuong = soLuong;
		this.gia = gia;
		this.tenDichVu = tenDichVu;
	}

	public ChiTietDichVu() {
	}
	public String getTenDichVu() {
		return tenDichVu;
	}
	public void setTenDichVu(String tenDichVu) {
		this.tenDichVu = tenDichVu;
	}

	public int getMaChiTietDichVu() {
		return maChiTietDichVu;
	}

	public void setMaChiTietDichVu(int maChiTietDichVu) {
		this.maChiTietDichVu = maChiTietDichVu;
	}

	public int getMaDichVu() {
		return maDichVu;
	}

	public void setMaDichVu(int maDichVu) {
		this.maDichVu = maDichVu;
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
