package com.dungblue.bus;

import com.dungblue.dao.KhachHangDAO;
import com.dungblue.entity.KhachHang;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class KhachHangBUS {
    private KhachHangDAO khachHangDAO = new KhachHangDAO();

    public List<KhachHang> layDanhSachKhachHang() {
        return khachHangDAO.layDanhSachKhachHang();
    }
 
    public boolean themKhachHang(KhachHang kh) {
		return khachHangDAO.themKhachHang(kh);
	}
    	public boolean suaKhachHang(KhachHang kh) {
		return khachHangDAO.suaKhachHang(kh);
	}
    	public boolean xoaKhachHang(int maKhachHang) {
		return khachHangDAO.xoaKhachHang(maKhachHang);
	}
	public List<KhachHang> timKiemKhachHangTheoTen(String tenKhachHang) {
		return khachHangDAO.timKiemKhachHang(tenKhachHang);
	}
	public List<KhachHang> timKiemKhachHangTheoMa(int id){
		try{return khachHangDAO.searchById(id);}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public KhachHang timKhachHangTheoSDT(String sdt) {
		return khachHangDAO.timKhachHangTheoSDT(sdt);
	}

	public KhachHang layKhachHangTheoMa(int maKhachHang) {
		// TODO Auto-generated method stub
		return null;
	}

	public int themKhachHangChiCoSDT(String sdt) {
	    try {
	        return khachHangDAO.themKhachHangChiCoSDT(sdt);
	    } catch (SQLException ex) {
	        // Nếu lỗi do trùng sdt, thử tìm lại
	        if (ex.getErrorCode() == 1) { // Mã lỗi ORA-00001
	            KhachHang kh = timKhachHangTheoSDT(sdt);
	            if (kh != null) {
	                return kh.getMaKhachHang();
	            }
	        }
	        throw new RuntimeException("Lỗi khi thêm khách hàng: " + ex.getMessage(), ex);
	    }
	}

	
}