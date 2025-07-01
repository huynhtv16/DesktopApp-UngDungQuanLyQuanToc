package com.dungblue.dao;

import com.dungblue.entity.NguoiDung;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NguoiDungDAO {
	  public NguoiDung kiemTraDangNhap(String taiKhoan, String matKhau) {
	        String sql = "SELECT * FROM nguoiDung WHERE taiKhoan = ? AND matKhau = ?";
	        
	        try (Connection conn = DBConnect.getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            
	            pstmt.setString(1, taiKhoan);
	            pstmt.setString(2, matKhau);
	            
	            try (ResultSet rs = pstmt.executeQuery()) {
	                if (rs.next()) {
	                    NguoiDung nd = new NguoiDung();
	                    nd.setMaNguoiDung(rs.getInt("maNguoiDung"));
	                    nd.setTaiKhoan(rs.getString("taiKhoan"));
	                    nd.setMatKhau(rs.getString("matKhau"));
	                    nd.setMaChucVu(rs.getInt("maChucVu"));
	                    nd.setHoTen(rs.getString("hoTen"));
	                    nd.setSdt(rs.getString("sdt"));
	                    nd.setDiaChi(rs.getString("diaChi"));
	                    nd.setTrangThai(rs.getString("trangThai"));
	                    return nd;
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
	    public List<NguoiDung> layDanhSachPhanTrang(int page, int pageSize) {
	        List<NguoiDung> ds = new ArrayList<>();
	        String sql = "SELECT * FROM nguoiDung ORDER BY maNguoiDung OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
	        
	        try (Connection conn = DBConnect.getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            
	            int offset = (page - 1) * pageSize;
	            pstmt.setInt(1, offset);
	            pstmt.setInt(2, pageSize);
	            
	            try (ResultSet rs = pstmt.executeQuery()) {
	                while (rs.next()) {
	                    NguoiDung nd = new NguoiDung();
	                    // Ánh xạ dữ liệu từ ResultSet
	                    ds.add(nd);
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return ds;
	    }
	    public int demTongSoNguoiDung() {
	        String sql = "SELECT COUNT(*) FROM nguoiDung";
	        try (Connection conn = DBConnect.getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(sql);
	             ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt(1);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return 0;
	    }
	
	    public List<NguoiDung> layDanhSachNguoiDung() {
	        String sql = "SELECT * FROM nguoiDung";
	        List<NguoiDung> dsNguoiDung = new ArrayList<>();
	        
	        try (Connection conn = DBConnect.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql);
	             ResultSet rs = stmt.executeQuery()) {
	            
	            while (rs.next()) {
	                NguoiDung nd = new NguoiDung();
	                nd.setMaNguoiDung(rs.getInt("maNguoiDung"));
	                nd.setTaiKhoan(rs.getString("taiKhoan"));
	                nd.setMatKhau(rs.getString("matKhau"));
	                nd.setMaChucVu(rs.getInt("maChucVu"));
	                nd.setHoTen(rs.getString("hoTen"));
	                nd.setSdt(rs.getString("sdt"));
	                nd.setDiaChi(rs.getString("diaChi"));
	                nd.setTrangThai(rs.getString("trangThai"));
	                
	                dsNguoiDung.add(nd);
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	        
	        return dsNguoiDung; // Trả về list, có thể rỗng nhưng không null
	    }

	public boolean themNguoiDung(NguoiDung nd) {
		String sql = "INSERT INTO nguoiDung (taiKhoan, matKhau, maChucVu, hoTen, sdt, diaChi, trangThai) VALUES (?, ?, ?, ?, ?, ?, ?)";
		try (Connection conn = DBConnect.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {
			
			stmt.setString(1, nd.getTaiKhoan());
			stmt.setString(2, nd.getMatKhau());
			stmt.setInt(3, nd.getMaChucVu());
			stmt.setString(4, nd.getHoTen());
			stmt.setString(5, nd.getSdt());
			stmt.setString(6, nd.getDiaChi());
			stmt.setString(7, nd.getTrangThai());
			
			int rowsInserted = stmt.executeUpdate();
			return rowsInserted > 0;
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return false; // Thêm người dùng thất bại
	}

	public boolean xoaNguoiDung(int maNV) {
		String sql = "DELETE FROM nguoiDung WHERE maNguoiDung = ?";
		try (Connection conn = DBConnect.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {
			
			stmt.setInt(1, maNV);
			int rowsDeleted = stmt.executeUpdate();
			return rowsDeleted > 0;
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return false; // Xóa người dùng thất bại
	}

	public List<NguoiDung> timKiemNguoiDung(String tuKhoa) {
	    String sql = "SELECT * FROM nguoiDung WHERE hoTen LIKE ? OR taiKhoan LIKE ?";
	    List<NguoiDung> dsNguoiDung = new ArrayList<>();
	    
	    try (Connection conn = DBConnect.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setString(1, "%" + tuKhoa + "%");
	        stmt.setString(2, "%" + tuKhoa + "%");

	        try (ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	                NguoiDung nd = new NguoiDung();
	                nd.setMaNguoiDung(rs.getInt("maNguoiDung"));
	                nd.setTaiKhoan(rs.getString("taiKhoan"));
	                nd.setMatKhau(rs.getString("matKhau"));
	                nd.setMaChucVu(rs.getInt("maChucVu"));
	                nd.setHoTen(rs.getString("hoTen"));
	                nd.setSdt(rs.getString("sdt"));
	                nd.setDiaChi(rs.getString("diaChi"));
	                nd.setTrangThai(rs.getString("trangThai"));

	                dsNguoiDung.add(nd);
	            }
	        }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }

	    return dsNguoiDung;
	}

}