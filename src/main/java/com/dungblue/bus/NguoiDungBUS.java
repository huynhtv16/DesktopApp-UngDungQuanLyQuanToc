package com.dungblue.bus;

import java.util.List;

import com.dungblue.dao.NguoiDungDAO;
import com.dungblue.entity.NguoiDung;

public class NguoiDungBUS {
    private final NguoiDungDAO nguoiDungDAO = new NguoiDungDAO();
    
    public NguoiDung dangNhap(String taiKhoan, String matKhau) {
        return nguoiDungDAO.kiemTraDangNhap(taiKhoan, matKhau);
    }
    
    public List<NguoiDung> layDanhSachNguoiDung() {
        return nguoiDungDAO.layDanhSachNguoiDung();
    }

	public boolean themNguoiDung(NguoiDung nd) {
		if (nd != null && nd.getTaiKhoan() != null && !nd.getTaiKhoan().isEmpty()) {
			return nguoiDungDAO.themNguoiDung(nd);
		}
		return false; // Thêm người dùng thất bại nếu thông tin không hợp lệ
	}

	public boolean suaNguoiDung(NguoiDung nd) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean xoaNguoiDung(int maNV) {
		if (maNV > 0) {
			return nguoiDungDAO.xoaNguoiDung(maNV);
		}
		return false; // Xóa người dùng thất bại nếu mã không hợp lệ
	}

    public List<NguoiDung> timKiemNguoiDung(String tuKhoa) {
        return nguoiDungDAO.timKiemNguoiDung(tuKhoa);
    }
    public int demTongSoNguoiDung() {
        return nguoiDungDAO.demTongSoNguoiDung();
    }
    // Phương thức phân trang
    public List<NguoiDung> layDanhSachPhanTrang(int page, int pageSize) {
        return nguoiDungDAO.layDanhSachPhanTrang(page, pageSize);
    }
}