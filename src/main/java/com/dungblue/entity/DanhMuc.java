package com.dungblue.entity;

public class DanhMuc {
		private int madanhmuc;
	private String tendanhmuc;
	// Constructor
	public DanhMuc(int madanhmuc, String tendanhmuc) {
		this.madanhmuc = madanhmuc;
		this.tendanhmuc = tendanhmuc;
	}
	// Default constructor
	public DanhMuc() {
		
	}
	// Getter and Setter
	public int getMadanhmuc() {
		return madanhmuc;
	}
	public void setMadanhmuc(int madanhmuc) {
		this.madanhmuc = madanhmuc;
	}
	public String getTendanhmuc() {
		return tendanhmuc;
	}
	public void setTendanhmuc(String tendanhmuc) {
		this.tendanhmuc = tendanhmuc;
	}
	

}
