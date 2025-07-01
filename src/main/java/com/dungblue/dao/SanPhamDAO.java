package com.dungblue.dao;

import com.dungblue.entity.SanPham;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SanPhamDAO {
    private Connection connection;

    public SanPhamDAO(){
        this.connection = DBConnect.getConnection();
    }

    // Thêm sản phẩm
    public boolean themSanPham(SanPham sp) {
        String sql = "{CALL sp_them_sanpham(?, ?, ?, ?, ?, ?)}";
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setString(1, sp.getTenSanPham());
            cs.setDouble(2, sp.getGiaNhap());
            cs.setDouble(3, sp.getGiaBan());
            cs.setInt(4, sp.getSoLuong());
            cs.setInt(5, sp.getMaDanhMuc());
            cs.setString(6, sp.getDuongdan());

            cs.execute();
            return true; // nếu không có lỗi
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Sửa sản phẩm
    public boolean suaSanPham(SanPham sp) {
        String sql = "UPDATE sanpham SET tensp=?, gianhap=?, giaban=?, soluong=?, madanhmuc=?, duongdan=? WHERE masanpham=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, sp.getTenSanPham());
            ps.setDouble(2, sp.getGiaNhap());
            ps.setDouble(3, sp.getGiaBan());
            ps.setInt(4, sp.getSoLuong());
            ps.setInt(5, sp.getMaDanhMuc());
            ps.setString(6, sp.getDuongdan());
            ps.setInt(7, sp.getMaSanPham());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa sản phẩm
    public boolean xoaSanPham(int maSanPham) {
        String sql = "DELETE FROM sanpham WHERE masanpham=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maSanPham);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Tìm kiếm sản phẩm
    public List<SanPham> timKiemSanPham(String keyword) {
        List<SanPham> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM sanpham WHERE tensp LIKE ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SanPham sp = new SanPham();
                sp.setMaSanPham(rs.getInt("masanpham"));
                sp.setTenSanPham(rs.getString("tensp"));
                sp.setGiaNhap(rs.getDouble("gianhap"));
                sp.setGiaBan(rs.getDouble("giaban"));
                sp.setSoLuong(rs.getInt("soluong"));
                sp.setMaDanhMuc(rs.getInt("madanhmuc"));
                sp.setDuongdan(rs.getString("duongdan"));
                danhSach.add(sp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }
    public SanPham laySanPhamTheoMa(int maSP) {
        String sql = "SELECT * FROM sanpham WHERE masanpham = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maSP);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                SanPham sp = new SanPham();
                sp.setMaSanPham(rs.getInt("masanpham"));
                sp.setTenSanPham(rs.getString("tensp"));
                sp.setGiaNhap(rs.getDouble("gianhap"));
                sp.setGiaBan(rs.getDouble("giaban"));
                sp.setSoLuong(rs.getInt("soluong"));
                sp.setMaDanhMuc(rs.getInt("madanhmuc"));
                sp.setDuongdan(rs.getString("duongdan"));
                return sp;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<SanPham> layDanhSachSanPham() {
        List<SanPham> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM sanpham";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SanPham sp = new SanPham();
                sp.setMaSanPham(rs.getInt("masanpham"));
                sp.setTenSanPham(rs.getString("tensp"));
                sp.setGiaNhap(rs.getDouble("gianhap"));
                sp.setGiaBan(rs.getDouble("giaban"));
                sp.setSoLuong(rs.getInt("soluong"));
                sp.setMaDanhMuc(rs.getInt("madanhmuc"));
                sp.setDuongdan(rs.getString("duongdan"));
                danhSach.add(sp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }
    public Map<String, Integer> thongKeSanPhamDaBan() {
        Map<String, Integer> ketQua = new HashMap<>();

        String sql = "SELECT sp.tensp, SUM(ctsp.soLuong) AS tongSoLuong " +
                     "FROM chiTietSanPham ctsp " +
                     "JOIN sanPham sp ON ctsp.maSanPham = sp.maSanPham " +
                     "GROUP BY sp.tensp";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String tenSanPham = rs.getString("tensp");
                int tongSoLuong = rs.getInt("tongSoLuong");
                ketQua.put(tenSanPham, tongSoLuong);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ketQua;
    }


}