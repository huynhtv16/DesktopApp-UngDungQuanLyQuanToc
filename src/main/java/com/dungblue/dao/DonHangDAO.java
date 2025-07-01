package com.dungblue.dao;

import com.dungblue.entity.DonHang;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DonHangDAO {
    public List<DonHang> layDanhSachDonHang() {
        List<DonHang> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM donhang";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DonHang dh = new DonHang();
                dh.setMaDonHang(rs.getInt("madonhang"));
                dh.setNgayDatHang(rs.getDate("ngaydathang"));
                dh.setTongTien(rs.getDouble("tongtien"));
                dh.setMaKhachHang(rs.getInt("makhachhang"));
                dh.setMaNguoiDung(rs.getInt("manguoidung"));
                danhSach.add(dh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    // Thêm đơn hàng
    public int themDonHang(DonHang dh) {
        String call = "{ call THEM_DON_HANG(?, ?, ?, ?, ?) }";

        try (Connection conn = DBConnect.getConnection();
             CallableStatement cs = conn.prepareCall(call)) {

            cs.setDate(1, dh.getNgayDatHang());
            cs.setDouble(2, dh.getTongTien());
            cs.setInt(3, dh.getMaKhachHang());
            cs.setInt(4, dh.getMaNguoiDung());

            cs.registerOutParameter(5, java.sql.Types.INTEGER); // OUT parameter

            cs.execute();

            return cs.getInt(5); // Mã đơn hàng được trả về

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean suaDonHang(DonHang dh) {
        String sql = "UPDATE donhang SET ngaydathang = ?, tongtien = ?, makhachhang = ?, manguoidung = ? WHERE madonhang = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, dh.getNgayDatHang());
            ps.setDouble(2, dh.getTongTien());
            ps.setInt(3, dh.getMaKhachHang()); 
            ps.setInt(4, dh.getMaNguoiDung());
            ps.setInt(5, dh.getMaDonHang());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


	// Ví dụ: xóa đơn hàng
	public boolean xoaDonHang(int maDonHang) {
		String sql = "DELETE FROM donhang WHERE madonhang = ?";
		try (Connection conn = DBConnect.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, maDonHang);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	// Ví dụ: lấy đơn hàng theo mã
	public DonHang layDonHangTheoMa(int maDonHang) {
		String sql = "SELECT * FROM donhang WHERE madonhang = ?";
		try (Connection conn = DBConnect.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, maDonHang);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				DonHang dh = new DonHang();
				dh.setMaDonHang(rs.getInt("madonhang"));
				dh.setNgayDatHang(rs.getDate("ngaydathang"));
				dh.setTongTien(rs.getDouble("tongtien"));
				dh.setMaKhachHang(rs.getInt("makhachhang"));
				dh.setMaNguoiDung(rs.getInt("manguoidung"));
				return dh;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int demSoDonHangCuaKhachHang(int maKhachHang) {
		String sql = "SELECT COUNT(*) FROM donhang WHERE makhachhang = ?";
		try (Connection conn = DBConnect.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, maKhachHang);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
}