package com.dungblue.bus;

import com.dungblue.dao.DonHangDAO;
import com.dungblue.entity.DonHang;
import java.util.List;

public class DonHangBUS {
    private DonHangDAO donHangDAO = new DonHangDAO();

    public List<DonHang> layDanhSachDonHang() {
        return donHangDAO.layDanhSachDonHang();
    }

    public int themDonHang(DonHang dh) {
        return donHangDAO.themDonHang(dh);
    }

    public boolean suaDonHang(DonHang dh) {
		return donHangDAO.suaDonHang(dh);
	}
    
	public boolean xoaDonHang(int madonhang) {
		return donHangDAO.xoaDonHang(madonhang);
	}
	// layDonHangTheoMa
	public DonHang layDonHangTheoMa(int madonhang) {
		return donHangDAO.layDonHangTheoMa(madonhang);
	}

	public int demSoDonHangCuaKhachHang(int maKhachHang) {
		return donHangDAO.demSoDonHangCuaKhachHang(maKhachHang);
	}

}