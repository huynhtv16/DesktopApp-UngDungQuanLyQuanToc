package com.dungblue.dao;

import com.dungblue.entity.ChiTietDichVu;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChiTietDichVuDAO {
	public List<ChiTietDichVu> layChiTietTheoDonHang(int madonhang) {
	    List<ChiTietDichVu> danhSach = new ArrayList<>();
	    String sql = "SELECT * FROM v_chitiet_dichvu_donhang WHERE madonhang = ?";
	    
	    try (Connection conn = DBConnect.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	        
	        ps.setInt(1, madonhang);
	        ResultSet rs = ps.executeQuery();
	        
	        while (rs.next()) {
	            ChiTietDichVu ct = new ChiTietDichVu();
	            ct.setMaChiTietDichVu(rs.getInt("maChiTietDichVu"));
	            ct.setMaDichVu(rs.getInt("maDichVu"));
	            ct.setTenDichVu(rs.getString("tendichvu"));
	            ct.setSoLuong(rs.getInt("soluong"));
	            ct.setGia(rs.getDouble("gia"));
	            ct.setMaDonHang(rs.getInt("madonhang"));
	            danhSach.add(ct);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return danhSach;
	}

	public int themChiTietDichVu(ChiTietDichVu ct) {
	    String call = "{ call THEM_CHITIET_DICHVU(?, ?, ?, ?, ?) }";

	    try (Connection conn = DBConnect.getConnection();
	         CallableStatement cs = conn.prepareCall(call)) {

	        cs.setInt(1, ct.getMaDonHang());
	        cs.setInt(2, ct.getMaDichVu());
	        cs.setInt(3, ct.getSoLuong());
	        cs.setDouble(4, ct.getGia());
	        cs.registerOutParameter(5, java.sql.Types.INTEGER); // OUT: machitietdichvu

	        cs.execute();

	        return cs.getInt(5); 

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return -1;
	    }
	}

}