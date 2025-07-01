// BaoCaoDAO.java
package com.dungblue.dao;

import com.dungblue.entity.BaoCaoThongKe;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BaoCaoDAO {
    public List<BaoCaoThongKe> thongKeTheoNgay(Date ngay) throws SQLException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(ngay);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date startDate = cal.getTime();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date endDate = cal.getTime();
        
        return layDuLieuTheoKhoangThoiGian(startDate, endDate, "ngay");
    }
    
    public List<BaoCaoThongKe> thongKeTheoThang(Date ngay) throws SQLException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(ngay);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, 0);
        Date startDate = cal.getTime();
        cal.set(Calendar.MONTH, 11);
        cal.set(Calendar.DAY_OF_MONTH, 31);
        Date endDate = cal.getTime();
        
        return layDuLieuTheoKhoangThoiGian(startDate, endDate, "thang");
    }
    
    private List<BaoCaoThongKe> layDuLieuTheoKhoangThoiGian(Date startDate, Date endDate, String loaiThongKe) throws SQLException {
        List<BaoCaoThongKe> ketQua = new ArrayList<>();
        String sql = "";
        
        if ("ngay".equals(loaiThongKe)) {
            sql = "SELECT TRUNC(dh.ngayDatHang) AS ngay, "
                + "SUM(dh.tongTien) AS tongDoanhThu, "
                + "COUNT(dh.maDonHang) AS soDonHang, "
                + "SUM(ctsp.soLuong) AS soSanPhamBan "
                + "FROM donHang dh "
                + "LEFT JOIN chiTietSanPham ctsp ON dh.maDonHang = ctsp.maDonHang "
                + "WHERE TRUNC(dh.ngayDatHang) BETWEEN ? AND ? "
                + "GROUP BY TRUNC(dh.ngayDatHang) "
                + "ORDER BY ngay";
        } else if ("thang".equals(loaiThongKe)) {
            sql = "SELECT TRUNC(dh.ngayDatHang, 'MM') AS ngay, "
                + "SUM(dh.tongTien) AS tongDoanhThu, "
                + "COUNT(dh.maDonHang) AS soDonHang, "
                + "SUM(ctsp.soLuong) AS soSanPhamBan "
                + "FROM donHang dh "
                + "LEFT JOIN chiTietSanPham ctsp ON dh.maDonHang = ctsp.maDonHang "
                + "WHERE TRUNC(dh.ngayDatHang) BETWEEN ? AND ? "
                + "GROUP BY TRUNC(dh.ngayDatHang, 'MM') "
                + "ORDER BY ngay";
        }

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, new java.sql.Date(startDate.getTime()));
            stmt.setDate(2, new java.sql.Date(endDate.getTime()));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Date ngayThongKe = rs.getDate("ngay");
                    double tongDoanhThu = rs.getDouble("tongDoanhThu");
                    int soDonHang = rs.getInt("soDonHang");
                    int soSanPhamBan = rs.getInt("soSanPhamBan");

                    ketQua.add(new BaoCaoThongKe(ngayThongKe, tongDoanhThu, soDonHang, soSanPhamBan));
                }
            }
        }
        return ketQua;
    }
}