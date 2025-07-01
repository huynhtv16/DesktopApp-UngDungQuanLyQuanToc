package com.dungblue.entity;

public class ChucVu {
	private int maChucVu;
	private String tenChucVu;

// constructor
public ChucVu(int maChucVu, String tenChucVu) {
		this.maChucVu = maChucVu;
		this.tenChucVu = tenChucVu;
	}
	public ChucVu() {
		// Constructor rỗng
	}
	
	// Getter and Setter methods
	public int getMaChucVu() {
		return maChucVu;
	}

	public void setMaChucVu(int maChucVu) {
		this.maChucVu = maChucVu;
	}

	public String getTenChucVu() {
		return tenChucVu;
	}

	public void setTenChucVu(String tenChucVu) {
		this.tenChucVu = tenChucVu;
	}

}