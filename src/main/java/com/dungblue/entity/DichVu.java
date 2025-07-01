package com.dungblue.entity;

public class DichVu {
    private int madichvu;
    private String tendichvu;
    private double gia;

    // Constructor, getter, setter
    
	public DichVu(int madichvu, String tendichvu, double gia) {
		this.madichvu = madichvu;
		this.tendichvu = tendichvu;
		this.gia = gia;
	}
	public DichVu() {
	}

	public int getMadichvu() {
		return madichvu;
	}

	public void setMadichvu(int madichvu) {
		this.madichvu = madichvu;
	}

	public String getTendichvu() {
		return tendichvu;
	}

	public void setTendichvu(String tendichvu) {
		this.tendichvu = tendichvu;
	}

	public double getGia() {
		return gia;
	}

	public void setGia(double gia) {
		this.gia = gia;
	}
}