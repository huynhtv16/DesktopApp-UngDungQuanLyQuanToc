package com.dungblue.bus;

import com.dungblue.dao.SanPhamDAO;
import com.dungblue.entity.SanPham;
import java.util.List;
import java.util.Map;

public class SanPhamBUS {
	private SanPhamDAO sanPhamDAO = new SanPhamDAO();
	
    public List<SanPham> layDanhSachSanPham() {
        return sanPhamDAO.layDanhSachSanPham();
    }
    // Thêm sản phẩm (có validate)
    public boolean themSanPham(SanPham sp) {
        if (sp.getTenSanPham().isEmpty() || sp.getGiaNhap() <= 0 || sp.getGiaBan() <= 0) {
            return false;
        }
        return sanPhamDAO.themSanPham(sp);
    }

    // Sửa sản phẩm
    public boolean suaSanPham(SanPham sp) {
        return sanPhamDAO.suaSanPham(sp);
    }

    // Xóa sản phẩm
    public boolean xoaSanPham(int maSanPham) {
        return sanPhamDAO.xoaSanPham(maSanPham);
    }

    // Tìm kiếm
    public List<SanPham> timKiemSanPham(String keyword) {
        return sanPhamDAO.timKiemSanPham(keyword);
    }
    public SanPham laySanPhamTheoMa(int maSP) {
        return sanPhamDAO.laySanPhamTheoMa(maSP);
    }
    // thongKeSanPham
    public Map<String, Integer> thongKeSanPham() {
		return sanPhamDAO.thongKeSanPhamDaBan();
	}

}