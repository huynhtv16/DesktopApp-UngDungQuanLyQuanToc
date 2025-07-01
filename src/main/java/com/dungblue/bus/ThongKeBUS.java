package com.dungblue.bus;

import com.dungblue.dao.ThongKeDAO;
import com.dungblue.entity.ThongKeItem;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ThongKeBUS {
    private ThongKeDAO thongKeDAO = new ThongKeDAO();

    public List<Object[]> laySanPhamBanChay() {
        return thongKeDAO.laySanPhamBanChay();
    }

    public List<Object[]> layDichVuNhieuNhat() {
        return thongKeDAO.layDichVuNhieuNhat();
    }
    public List<ThongKeItem> getSanPhamByKhach(int maKH) {
        try { return thongKeDAO.sanPhamByKhach(maKH); }
        catch (SQLException e) { throw new RuntimeException(e); }
    }
    public List<ThongKeItem> getDichVuByKhach(int maKH) {
        try { return thongKeDAO.dichVuByKhach(maKH); }
        catch (SQLException e) { throw new RuntimeException(e); }
    }
    public List<Date> getInvoiceDates(int maKH) {
        try { return thongKeDAO.getInvoiceDates(maKH); }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

	public Number tinhTongSoDonHangTrongNgay() {
		return thongKeDAO.tinhTongSoDonHangTrongNgay();
	}

	public Number tinhTongDoanhThuTrongNgay() {
		return thongKeDAO.tinhTongDoanhThuTrongNgay();
	}

	public Map<String, Object> thongKeSanPhamBanChay(int thang, int nam) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, Object> thongKeTheoThang(int thang, int nam) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, Object> thongKeTheoNgay(java.util.Date ngay) {
		// TODO Auto-generated method stub
		return null;
	}
}