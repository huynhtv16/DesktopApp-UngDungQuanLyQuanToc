package com.dungblue.bus;
import com.dungblue.dao.DanhMucDAO;
import com.dungblue.entity.DanhMuc;
import java.util.List;


public class DanhMucBUS {
		// Phương thức để lấy danh sách danh mục từ DAO
	public List<DanhMuc> layDanhSachDanhMuc() {
		DanhMucDAO danhMucDAO = new DanhMucDAO();
		return danhMucDAO.layDanhSachDanhMuc();
	}

	// Thêm logic nghiệp vụ (validate tên danh mục...)
	public boolean themDanhMuc(DanhMuc danhMuc) {
		DanhMucDAO danhMucDAO = new DanhMucDAO();
		return danhMucDAO.themDanhMuc(danhMuc);
	}

	public boolean suaDanhMuc(DanhMuc danhMuc) {
		DanhMucDAO danhMucDAO = new DanhMucDAO();
		return danhMucDAO.suaDanhMuc(danhMuc);
	}

	public boolean xoaDanhMuc(int maDM) {
		DanhMucDAO danhMucDAO = new DanhMucDAO();
		return danhMucDAO.xoaDanhMuc(maDM);
	}
	public List<DanhMuc> timKiemDanhMuc(String tuKhoa) {
		DanhMucDAO danhMucDAO = new DanhMucDAO();
		return danhMucDAO.timKiemDanhMuc(tuKhoa);
	}
	// Phương thức lấy danh mục theo mã
	public DanhMuc layDanhMucTheoMa(int maDM) {
		DanhMucDAO danhMucDAO = new DanhMucDAO();
		return danhMucDAO.layDanhMucTheoMa(maDM);
	}
	

}
