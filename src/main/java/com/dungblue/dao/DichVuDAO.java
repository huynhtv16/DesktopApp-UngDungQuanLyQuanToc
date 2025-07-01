package com.dungblue.dao;

import com.dungblue.entity.DichVu;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DichVuDAO {
    // Lấy danh sách dịch vụ
    public List<DichVu> layDanhSachDichVu() {
        List<DichVu> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM dichvu";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DichVu dv = new DichVu();
                dv.setMadichvu(rs.getInt("madichvu"));
                dv.setTendichvu(rs.getString("tendichvu"));
                dv.setGia(rs.getDouble("gia"));
                danhSach.add(dv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    // Thêm dịch vụ
    public boolean themDichVu(DichVu dv) {
        String sql = "INSERT INTO dichvu (tendichvu, gia) VALUES (?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dv.getTendichvu());
            ps.setDouble(2, dv.getGia());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Sửa dịch vụ
    public boolean suaDichVu(DichVu dv) {
        String sql = "UPDATE dichvu SET tendichvu = ?, gia = ? WHERE madichvu = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dv.getTendichvu());
            ps.setDouble(2, dv.getGia());
            ps.setInt(3, dv.getMadichvu());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa dịch vụ
    public boolean xoaDichVu(int madichvu) {
        String sql = "DELETE FROM dichvu WHERE madichvu = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, madichvu);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Tìm kiếm dịch vụ
    public List<DichVu> timKiemDichVu(String keyword) {
        List<DichVu> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM dichvu WHERE tendichvu LIKE ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DichVu dv = new DichVu();
                dv.setMadichvu(rs.getInt("madichvu"));
                dv.setTendichvu(rs.getString("tendichvu"));
                dv.setGia(rs.getDouble("gia"));
                danhSach.add(dv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }
    // Lấy dịch vụ theo mã
    public DichVu layDichVuTheoMa(int madichvu) {
		String sql = "SELECT * FROM dichvu WHERE madichvu = ?";
		try (Connection conn = DBConnect.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, madichvu);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				DichVu dv = new DichVu();
				dv.setMadichvu(rs.getInt("madichvu"));
				dv.setTendichvu(rs.getString("tendichvu"));
				dv.setGia(rs.getDouble("gia"));
				return dv;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
    public Map<String, Integer> thongKeDichVuDaSuDung() {
        Map<String, Integer> thongKeMap = new HashMap<>();
        String sql = "SELECT dv.tenDichVu, SUM(ctdv.soLuong) AS tongSoLuong "
                   + "FROM chiTietDichVu ctdv "
                   + "JOIN dichVu dv ON ctdv.maDichVu = dv.maDichVu "
                   + "GROUP BY dv.tenDichVu";

        try (
            Connection conn = DBConnect.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
        ) {
            while (rs.next()) {
                String tenDichVu = rs.getString("tenDichVu");
                int tongSoLuong = rs.getInt("tongSoLuong");
                thongKeMap.put(tenDichVu, tongSoLuong);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return thongKeMap;
    }


}