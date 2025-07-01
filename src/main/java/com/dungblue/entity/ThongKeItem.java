package com.dungblue.entity;

public class ThongKeItem {
	private String name;
	private int count;
	
	public ThongKeItem(String name, int count) {
		this.name = name;
		this.count = count;
	}
	public ThongKeItem() {
		// Constructor rỗng
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	

}
