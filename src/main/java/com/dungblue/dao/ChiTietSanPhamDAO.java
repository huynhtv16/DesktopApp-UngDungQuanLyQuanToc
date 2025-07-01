package com.dungblue.dao;

import com.dungblue.entity.ChiTietSanPham;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChiTietSanPhamDAO {
	public List<ChiTietSanPham> layChiTietTheoDonHang(int madonhang) {
	    List<ChiTietSanPham> danhSach = new ArrayList<>();
	    String sql = "SELECT * FROM v_chitiet_sanpham_donhang WHERE madonhang = ?";
	    
	    try (Connection conn = DBConnect.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	        
	        ps.setInt(1, madonhang);
	        ResultSet rs = ps.executeQuery();
	        
	        while (rs.next()) {
	            ChiTietSanPham ct = new ChiTietSanPham();
	            ct.setMaChiTiet(rs.getInt("machitiet"));
	            ct.setMaDonHang(rs.getInt("madonhang"));
	            ct.setMaSanPham(rs.getInt("masanpham"));
	            ct.setSoLuong(rs.getInt("soluong"));
	            ct.setGia(rs.getDouble("gia"));
	            ct.setTensp(rs.getString("tensp"));
	            danhSach.add(ct);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return danhSach;
	}

	public int themChiTietSanPham(ChiTietSanPham ct) {
	    String sql = "INSERT INTO ChiTietSanPham (maDonHang, maSanPham, soLuong, gia) VALUES (?, ?, ?, ?)";
	    
	    try (Connection conn = DBConnect.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql, new String[]{"maChiTiet"})) {
	        
	        pstmt.setInt(1, ct.getMaDonHang());
	        pstmt.setInt(2, ct.getMaSanPham());
	        pstmt.setInt(3, ct.getSoLuong());
	        pstmt.setDouble(4, ct.getGia());
	        
	        int affectedRows = pstmt.executeUpdate();
	        
	        if (affectedRows == 0) {
	            throw new SQLException("Thêm chi tiết sản phẩm thất bại, không có dòng nào bị ảnh hưởng");
	        }
	        
	        try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
	            if (generatedKeys.next()) {
	                return generatedKeys.getInt(1); // Trả về mã chi tiết mới
	            } else {
	                throw new SQLException("Thêm chi tiết sản phẩm thất bại, không lấy được ID");
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return -1;
	    }
	}
	
}