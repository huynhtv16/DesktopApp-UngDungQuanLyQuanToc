package com.dungblue.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.dungblue.entity.ThongKeItem;

import oracle.jdbc.OracleTypes;

public class ThongKeDAO {

    // Lấy top 5 sản phẩm bán chạy trong ngày (Oracle)
    public List<Object[]> laySanPhamBanChay() {
        List<Object[]> data = new ArrayList<>();
        String sql = """
            SELECT * FROM (
                SELECT sp.tensp, SUM(ct.soluong) AS tongsl
                FROM chitietsanpham ct
                JOIN donhang dh ON ct.madonhang = dh.madonhang
                JOIN sanpham sp ON ct.masanpham = sp.masanpham
                WHERE TRUNC(dh.ngaydathang) = TRUNC(SYSDATE)
                GROUP BY sp.tensp
                ORDER BY tongsl DESC
            ) WHERE ROWNUM <= 5
            """;
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                data.add(new Object[]{ rs.getString("tensp"), rs.getInt("tongsl") });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    // Lấy top 5 dịch vụ sử dụng nhiều nhất trong ngày (Oracle)
    public List<Object[]> layDichVuNhieuNhat() {
        List<Object[]> data = new ArrayList<>();
        String sql = """
            SELECT * FROM (
                SELECT dv.tendichvu, SUM(ct.soluong) AS tongsl
                FROM chitietdichvu ct
                JOIN donhang dh ON ct.madonhang = dh.madonhang
                JOIN dichvu dv ON ct.madichvu = dv.madichvu
                WHERE TRUNC(dh.ngaydathang) = TRUNC(SYSDATE)
                GROUP BY dv.tendichvu
                ORDER BY tongsl DESC
            ) WHERE ROWNUM <= 5
            """;
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                data.add(new Object[]{ rs.getString("tendichvu"), rs.getInt("tongsl") });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    // Thống kê sản phẩm đã sử dụng của 1 khách hàng
    public List<ThongKeItem> sanPhamByKhach(int maKH) throws SQLException {
        String sql = "{CALL sp_sanPhamByKhach(?, ?)}";  // 2 tham số: IN và OUT
        try (Connection c = DBConnect.getConnection();
             CallableStatement cs = c.prepareCall(sql)) {
            
            cs.setInt(1, maKH);
            cs.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR); // đăng ký OUT param kiểu CURSOR
            
            cs.execute();  // gọi procedure
            
            try (ResultSet rs = (ResultSet) cs.getObject(2)) {  // lấy cursor ra dưới dạng ResultSet
                List<ThongKeItem> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(new ThongKeItem(
                        rs.getString("name"),
                        rs.getInt("cnt")
                    ));
                }
                return list;
            }
        }
    }


    // Thống kê dịch vụ đã sử dụng của 1 khách hàng
    public List<ThongKeItem> dichVuByKhach(int maKH) throws SQLException {
        String sql = "{CALL sp_dichVuByKhach(?, ?)}";  // Gọi procedure 2 tham số
        try (Connection c = DBConnect.getConnection();
             CallableStatement cs = c.prepareCall(sql)) {
            cs.setInt(1, maKH);                        // Tham số IN
            cs.registerOutParameter(2, OracleTypes.CURSOR);  // Tham số OUT là cursor            
            cs.execute();  // Chạy procedure            
            try (ResultSet rs = (ResultSet) cs.getObject(2)) {  // Lấy ResultSet từ OUT parameter
                List<ThongKeItem> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(new ThongKeItem(
                        rs.getString("name"),
                        rs.getInt("cnt")
                    ));
                }
                return list;
            }
        }
    }


    // Lấy danh sách ngày đặt hàng của khách
    public List<Date> getInvoiceDates(int maKH) throws SQLException {
        String sql = "SELECT ngaydathang FROM donhang WHERE makhachhang = ?";
        try (Connection c = DBConnect.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, maKH);
            try (ResultSet rs = p.executeQuery()) {
                List<Date> dates = new ArrayList<>();
                while (rs.next()) {
                    dates.add(rs.getDate("ngaydathang"));
                }
                return dates;
            }
        }
    }

    // Đếm số lần đến của khách hàng
    public int countVisitsByCustomer(int maKH) throws SQLException {
        String sql = "SELECT COUNT(*) AS soLan FROM donhang WHERE makhachhang = ?";
        try (Connection c = DBConnect.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, maKH);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("soLan");
                }
                return 0;
            }
        }
    }

    // Đếm số đơn hàng trong ngày hôm nay
    public Number tinhTongSoDonHangTrongNgay() {
        String sql = "SELECT COUNT(*) FROM donhang WHERE TRUNC(ngaydathang) = TRUNC(SYSDATE)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Tính tổng doanh thu trong ngày hôm nay
    public Number tinhTongDoanhThuTrongNgay() {
        String sql = "SELECT SUM(tongtien) FROM donhang WHERE TRUNC(ngaydathang) = TRUNC(SYSDATE)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
