package com.dungblue.dao;

import com.dungblue.entity.ChucVu;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChucVuDAO {
    public List<ChucVu> layDanhSachChucVu() {
        List<ChucVu> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM chucvu";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ChucVu cv = new ChucVu();
                cv.setMaChucVu(rs.getInt("machucvu"));
                cv.setTenChucVu(rs.getString("tenchucvu"));
                danhSach.add(cv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    // Thêm các phương thức thêm, sửa, xóa...
 // Thêm chức vụ
    public boolean themChucVu(ChucVu chucVu) {
        String sql = "INSERT INTO chucvu (tenchucvu) VALUES (?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, chucVu.getTenChucVu());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Sửa chức vụ
    public boolean suaChucVu(ChucVu chucVu) {
        String sql = "UPDATE chucvu SET tenchucvu = ? WHERE machucvu = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, chucVu.getTenChucVu());
            ps.setInt(2, chucVu.getMaChucVu());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa chức vụ
    public boolean xoaChucVu(int maChucVu) {
        String sql = "DELETE FROM chucvu WHERE machucvu = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maChucVu);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Tìm kiếm chức vụ
    public List<ChucVu> timKiemChucVu(String tuKhoa) {
        List<ChucVu> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM chucvu WHERE tenchucvu LIKE ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + tuKhoa + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ChucVu cv = new ChucVu();
                cv.setMaChucVu(rs.getInt("machucvu"));
                cv.setTenChucVu(rs.getString("tenchucvu"));
                danhSach.add(cv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    
    // Lấy tên chức vụ theo mã
    public String layTenChucVu(int maChucVu) {
		String tenChucVu = null;
		String sql = "SELECT tenchucvu FROM chucvu WHERE machucvu = ?";
		try (Connection conn = DBConnect.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, maChucVu);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				tenChucVu = rs.getString("tenchucvu");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tenChucVu;
	}
    
}