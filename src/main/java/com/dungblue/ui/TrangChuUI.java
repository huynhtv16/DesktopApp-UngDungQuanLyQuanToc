package com.dungblue.ui;

import com.dungblue.bus.ThongKeBUS;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class TrangChuUI extends JPanel {
    private ThongKeBUS thongKeBUS = new ThongKeBUS();

    public TrangChuUI() {
        setLayout(new BorderLayout(10, 10)); // Thêm khoảng cách giữa các thành phần
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Thêm padding
        
        // Panel chứa các chỉ số thống kê (số đơn + doanh thu)
        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 10, 0)); // 1 hàng, 2 cột, khoảng cách ngang 10px
        statsPanel.add(taoTheThongKe("SỐ ĐƠN BÁN RA TRONG NGÀY", 
                                    thongKeBUS.tinhTongSoDonHangTrongNgay()));
        statsPanel.add(taoTheThongKe("DOANH THU TRONG NGÀY", 
                                    thongKeBUS.tinhTongDoanhThuTrongNgay()));

        // Panel chứa các biểu đồ
        JPanel chartPanel = new JPanel(new GridLayout(2, 1, 0, 10)); // 2 hàng, 1 cột, khoảng cách dọc 10px
        chartPanel.add(taoBieuDo("TOP 5 SẢN PHẨM BÁN CHẠY TRONG NGÀY", thongKeBUS.laySanPhamBanChay()));
        chartPanel.add(taoBieuDo("TOP 5 DỊCH VỤ SỬ DỤNG NHIỀU TRONG NGÀY", thongKeBUS.layDichVuNhieuNhat()));

        // Thêm các panel chính vào giao diện
        add(statsPanel, BorderLayout.NORTH);
        add(chartPanel, BorderLayout.CENTER);
    }

    // Tạo thẻ thống kê (số đơn/doanh thu)
    private JPanel taoTheThongKe(String title, Number value) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 70, 130), 1), // Viền xanh đậm với độ dày 1px
            BorderFactory.createEmptyBorder(10, 15, 10, 15) // Padding
        ));
        
        // Tiêu đề
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(new Color(0, 70, 130)); // Màu xanh đậm
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Giá trị
        String valueText;
        if (value instanceof Integer) {
            valueText = String.format("%,d", value); // Định dạng số nguyên
        } else {
            valueText = String.format("%,.0f VND", value); // Định dạng tiền tệ
        }
        
        JLabel valueLabel = new JLabel(valueText, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(Color.RED); // Màu đỏ cho giá trị
        panel.add(valueLabel, BorderLayout.CENTER);
        
        return panel;
    }

    // Tạo biểu đồ từ dữ liệu
    private ChartPanel taoBieuDo(String title, List<Object[]> data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Object[] row : data) {
            dataset.addValue((Integer) row[1], "Số lượng", (String) row[0]);
        }
        JFreeChart chart = ChartFactory.createBarChart(title, "Tên", "Số lượng", dataset);
        return new ChartPanel(chart);
    }
}