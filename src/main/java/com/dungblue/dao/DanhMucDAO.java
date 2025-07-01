package com.dungblue.dao;
import com.dungblue.entity.DanhMuc;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DanhMucDAO {
		// Phương thức lấy danh sách danh mục từ cơ sở dữ liệu
	public List<DanhMuc> layDanhSachDanhMuc() {
		List<DanhMuc> danhSach = new ArrayList<>();
		String sql = "SELECT * FROM danhmuc";
		try (Connection conn = DBConnect.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				DanhMuc dm = new DanhMuc();
				dm.setMadanhmuc(rs.getInt("madanhmuc"));
				dm.setTendanhmuc(rs.getString("tendanhmuc"));
				danhSach.add(dm);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return danhSach;
	}
	// Phương thức thêm danh mục
	public boolean themDanhMuc(DanhMuc danhMuc) {
		String sql = "INSERT INTO danhmuc (tendanhmuc) VALUES (?)";
		try (Connection conn = DBConnect.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, danhMuc.getTendanhmuc());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	// Phương thức sửa danh mục
	public boolean suaDanhMuc(DanhMuc danhMuc) {
		String sql = "UPDATE danhmuc SET tendanhmuc = ? WHERE madanhmuc = ?";
		try (Connection conn = DBConnect.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, danhMuc.getTendanhmuc());
			ps.setInt(2, danhMuc.getMadanhmuc());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	// Phương thức xóa danh mục
	public boolean xoaDanhMuc(int maDM) {
		String sql = "DELETE FROM danhmuc WHERE madanhmuc = ?";
		try (Connection conn = DBConnect.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, maDM);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	// Phương thức tìm kiếm danh mục theo tên
	public List<DanhMuc> timKiemDanhMuc(String keyword) {
		List<DanhMuc> danhSach = new ArrayList<>();
		String sql = "SELECT * FROM danhmuc WHERE tendanhmuc LIKE ?";
		try (Connection conn = DBConnect.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, "%" + keyword + "%");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				DanhMuc dm = new DanhMuc();
				dm.setMadanhmuc(rs.getInt("madanhmuc"));
				dm.setTendanhmuc(rs.getString("tendanhmuc"));
				danhSach.add(dm);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return danhSach;
	}
	// Phương thức lấy tên danh mục theo mã
	public String layTenDanhMuc(int maDM) {
		String sql = "SELECT tendm FROM danhmuc WHERE madanhmuc = ?";
		try (Connection conn = DBConnect.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, maDM);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString("tendanhmuc");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	// Phương thức lấy danh mục theo mã
	public DanhMuc layDanhMucTheoMa(int maDM) {
		String sql = "SELECT * FROM danhmuc WHERE madanhmuc = ?";
		try (Connection conn = DBConnect.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, maDM);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				DanhMuc dm = new DanhMuc();
				dm.setMadanhmuc(rs.getInt("madanhmuc"));
				dm.setTendanhmuc(rs.getString("tendanhmuc"));
				return dm;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
