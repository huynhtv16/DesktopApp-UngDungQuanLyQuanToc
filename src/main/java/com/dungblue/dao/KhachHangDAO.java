package com.dungblue.dao;

import com.dungblue.entity.KhachHang;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KhachHangDAO {
    // Lấy danh sách khách hàng
    public List<KhachHang> layDanhSachKhachHang() {
        List<KhachHang> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM khachhang";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setMaKhachHang(rs.getInt("makhachhang"));
                kh.setTenKhachHang(rs.getString("tenkhachhang"));
                kh.setGioiTinh(rs.getString("gioitinh"));
                kh.setSdt(rs.getString("sdt"));
                kh.setDiaChi(rs.getString("diachi"));
                danhSach.add(kh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    // Thêm khách hàng
    public boolean themKhachHang(KhachHang kh) {
		String sql = "INSERT INTO khachhang (tenkhachhang, sdt, diachi) VALUES (?, ?, ?)";
		try (Connection conn = DBConnect.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, kh.getTenKhachHang());
			ps.setString(2, kh.getGioiTinh());
			ps.setString(2, kh.getSdt());
			ps.setString(3, kh.getDiaChi());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
    public int themKhachHangChiCoSDT(String sdt) throws SQLException {
        // Kiểm tra trước nếu sdt đã tồn tại
        String checkSql = "SELECT maKhachHang FROM khachhang WHERE sdt = ?";
		try (Connection conn = DBConnect.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            
            checkStmt.setString(1, sdt);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("maKhachHang");
                }
            }
        }

        // Nếu chưa tồn tại, thêm mới
        String insertSql = "INSERT INTO khachhang (maKhachHang, sdt) VALUES (SEQ_KHACHHANG.NEXTVAL, ?)";
		try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSql, new String[]{"maKhachHang"})) {
            
            pstmt.setString(1, sdt);
            pstmt.executeUpdate();
            
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Không thể lấy mã khách hàng");
                }
            }
        }
    }


    
    
	// Sửa khách hàng
	public boolean suaKhachHang(KhachHang kh) {
		String sql = "UPDATE khachhang SET tenkhachhang = ?, sdt = ?, diachi = ? WHERE makhachhang = ?";
		try (Connection conn = DBConnect.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, kh.getTenKhachHang());
			ps.setString(2, kh.getGioiTinh());
			ps.setString(2, kh.getSdt());
			ps.setString(3, kh.getDiaChi());
			ps.setInt(4, kh.getMaKhachHang());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// Xóa khách hàng
	public boolean xoaKhachHang(int maKhachHang) {
		String sql = "DELETE FROM khachhang WHERE makhachhang = ?";
		try (Connection conn = DBConnect.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, maKhachHang);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	// Tìm kiếm khách hàng theo tên
	public List<KhachHang> timKiemKhachHang(String tenKhachHang) {
		List<KhachHang> danhSach = new ArrayList<>();
		String sql = "SELECT * FROM khachhang WHERE tenkhachhang LIKE ?";
		try (Connection conn = DBConnect.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, "%" + tenKhachHang + "%");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				KhachHang kh = new KhachHang();
				kh.setMaKhachHang(rs.getInt("makhachhang"));
				kh.setTenKhachHang(rs.getString("tenkhachhang"));
				kh.setGioiTinh(rs.getString("gioitinh"));
				kh.setSdt(rs.getString("sdt"));
				kh.setDiaChi(rs.getString("diachi"));
				danhSach.add(kh);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return danhSach;
	}
	public List<KhachHang> searchById(int id) throws SQLException {
        String sql = "SELECT * FROM KhachHang WHERE maKhachHang = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                List<KhachHang> list = new ArrayList<>();
                while (rs.next()) {
                    KhachHang kh = new KhachHang();
                    kh.setMaKhachHang(rs.getInt("maKhachHang"));
                    kh.setTenKhachHang(rs.getString("tenKhachHang"));
                    kh.setGioiTinh(rs.getString("gioiTinh"));
                    kh.setSdt(rs.getString("sdt"));
                    kh.setDiaChi(rs.getString("diaChi"));
                    kh.setGioiTinh(rs.getString("gioiTinh"));
                    list.add(kh);
                }
                return list;
            }
        }
    }

	public KhachHang timKhachHangTheoSDT(String sdt) {
		String sql = "SELECT * FROM khachhang WHERE sdt = ?";
		try (Connection conn = DBConnect.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, sdt);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				KhachHang kh = new KhachHang();
				kh.setMaKhachHang(rs.getInt("makhachhang"));
				kh.setTenKhachHang(rs.getString("tenkhachhang"));
				kh.setGioiTinh(rs.getString("gioitinh"));
				kh.setSdt(rs.getString("sdt"));
				kh.setDiaChi(rs.getString("diachi"));
				return kh;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}


}