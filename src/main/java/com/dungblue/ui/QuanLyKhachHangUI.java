package com.dungblue.ui;

import com.dungblue.bus.KhachHangBUS;
import com.dungblue.bus.ThongKeBUS;
import com.dungblue.entity.KhachHang;
import com.dungblue.entity.ThongKeItem;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class QuanLyKhachHangUI extends JPanel {
    private JTabbedPane tabbedPane = new JTabbedPane();

    private KhachHangBUS khBus = new KhachHangBUS();
    private ThongKeBUS tkBus = new ThongKeBUS();

    // Table models
    private DefaultTableModel modelKhach = new DefaultTableModel(new String[]{"Mã KH","Tên KH","Giới tính","SĐT","Địa chỉ"}, 0);
    private JTable tblKhach = new JTable(modelKhach);

    private DefaultTableModel modelKhachTK = new DefaultTableModel(new String[]{"Mã KH","Tên KH","Giới tính","SĐT","Địa chỉ"}, 0);   
    private JTable tblKhachTK = new JTable(modelKhachTK);

    // Chart datasets
    private DefaultPieDataset dsSP = new DefaultPieDataset();
    private DefaultPieDataset dsDV = new DefaultPieDataset();

    // Footer labels
    private JLabel lblTongNgay = new JLabel("Tổng ngày đến: 0");
    private JLabel lblDau      = new JLabel("Ngày đầu: -");
    private JLabel lblCuoi     = new JLabel("Ngày cuối: -");

    public QuanLyKhachHangUI() {
        setLayout(new BorderLayout(5,5));
        initKhachTab();
        initThongKeTab();
        add(tabbedPane, BorderLayout.CENTER);
        loadKhach();

        // khi đổi tab Thống Kê, load dữ liệu lên bảng TK
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (tabbedPane.getSelectedIndex() == 1) { // tab 1 = Thống Kê
                    loadKhachTK();
                }
            }
        });
    }

    private void initKhachTab() {
        JPanel p = new JPanel(new BorderLayout(5,5));
        p.add(new JScrollPane(tblKhach), BorderLayout.CENTER);

        JPanel btns = new JPanel();
        JButton bAdd = new JButton("Thêm");
        JButton bUpd = new JButton("Sửa");
        JButton bDel = new JButton("Xóa");
        btns.add(bAdd); btns.add(bUpd); btns.add(bDel);
        p.add(btns, BorderLayout.SOUTH);

        tabbedPane.addTab("Khách Hàng", p);
        
        // Thêm sự kiện cho các nút
        bAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                themKhachHang();
            }
        });
        
        bUpd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                suaKhachHang();
            }
        });
        
        bDel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                xoaKhachHang();
            }
        });
    }

    private void themKhachHang() {
        JTextField txtTen = new JTextField();
        JComboBox<String> cboGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ", "Khác"});
        JTextField txtSdt = new JTextField();
        JTextField txtDiaChi = new JTextField();
        
        Object[] fields = {
            "Tên khách hàng:", txtTen,
            "Giới tính:", cboGioiTinh, // Thêm combobox giới tính
            "Số điện thoại:", txtSdt,
            "Địa chỉ:", txtDiaChi
            
        };
        
        int result = JOptionPane.showConfirmDialog(
            this, 
            fields, 
            "Thêm khách hàng", 
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );
        
        if (result == JOptionPane.OK_OPTION) {
            String ten = txtTen.getText().trim();
            String gioiTinh = (String) cboGioiTinh.getSelectedItem();
            String sdt = txtSdt.getText().trim();
            String diaChi = txtDiaChi.getText().trim();
            
            if (ten.isEmpty() || sdt.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên và số điện thoại không được để trống", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            KhachHang kh = new KhachHang();
            kh.setTenKhachHang(ten);
            kh.setGioiTinh(gioiTinh); // Lưu giới tính
            kh.setSdt(sdt);
            kh.setDiaChi(diaChi);
            
            boolean success = khBus.themKhachHang(kh);
            if (success) {
                JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                loadKhach();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm khách hàng thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void suaKhachHang() {
        int selectedRow = tblKhach.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một khách hàng để sửa", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int maKH = (int) modelKhach.getValueAt(selectedRow, 0);
        KhachHang kh = khBus.timKiemKhachHangTheoMa(maKH).stream().findFirst().orElse(null);
        if (kh == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JTextField txtTen = new JTextField(kh.getTenKhachHang());
        JComboBox<String> cboGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ", "Khác"});
        JTextField txtSdt = new JTextField(kh.getSdt());
        JTextField txtDiaChi = new JTextField(kh.getDiaChi());
        
        Object[] fields = {
            "Tên khách hàng:", txtTen,
            "Giới tính:", cboGioiTinh, // Thêm combobox giới tính
            "Số điện thoại:", txtSdt,
            "Địa chỉ:", txtDiaChi
        };
        
        int result = JOptionPane.showConfirmDialog(
            this, 
            fields, 
            "Sửa thông tin khách hàng", 
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );
        
        if (result == JOptionPane.OK_OPTION) {
            String ten = txtTen.getText().trim();
            String gioiTinh = (String) cboGioiTinh.getSelectedItem();
            String sdt = txtSdt.getText().trim();
            String diaChi = txtDiaChi.getText().trim();
            
            if (ten.isEmpty() || sdt.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên và số điện thoại không được để trống", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            kh.setTenKhachHang(ten);
            kh.setGioiTinh(gioiTinh); // Cập nhật giới tính
            kh.setSdt(sdt);
            kh.setDiaChi(diaChi);
            
            boolean success = khBus.suaKhachHang(kh);
            if (success) {
                JOptionPane.showMessageDialog(this, "Cập nhật thông tin thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                loadKhach();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thông tin thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void xoaKhachHang() {
        int selectedRow = tblKhach.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một khách hàng để xóa", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int maKH = (int) modelKhach.getValueAt(selectedRow, 0);
        String tenKH = (String) modelKhach.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "Bạn có chắc chắn muốn xóa khách hàng: " + tenKH + "?",
            "Xác nhận xóa",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = khBus.xoaKhachHang(maKH);
            if (success) {
                JOptionPane.showMessageDialog(this, "Xóa khách hàng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                loadKhach();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa khách hàng thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void initThongKeTab() {
        // split pane: trên là table, dưới là charts+footer
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        split.setResizeWeight(0.3); // table chiếm 30%, chart chiếm 70%

        // Top: tìm kiếm + bảng TK
        JPanel top = new JPanel(new BorderLayout(5,5));
        JPanel search = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField txtSearch = new JTextField(20);
        JButton bSearch = new JButton("Tìm kiếm");
        search.add(new JLabel("Tên KH:")); search.add(txtSearch); search.add(bSearch);
        top.add(search, BorderLayout.NORTH);
        top.add(new JScrollPane(tblKhachTK), BorderLayout.CENTER);
        split.setTopComponent(top);

        // Bottom: charts + footer
        JPanel bottom = new JPanel(new BorderLayout(5,5));
        JTabbedPane charts = new JTabbedPane();
        
        // Tạo pie-chart cho SP và DV
        JFreeChart cSP = ChartFactory.createPieChart(
            "Sản phẩm đã mua",   // tiêu đề
            dsSP,                // dataset
            true,                // legend
            true,                // tooltips
            false                // URLs
        );
        // Đặt label cho từng phần của pie-chart
        PiePlot plotSP = (PiePlot) cSP.getPlot();
        plotSP.setLabelGenerator(new StandardPieSectionLabelGenerator(
            "{0}: {1}"      // {0}=key, {1}=value; bạn có thể dùng {2} để hiển thị %  
        ));
        
        JFreeChart cDV = ChartFactory.createPieChart(
            "Dịch vụ đã sử dụng",
            dsDV,
            true,
            true,
            false
        );
        PiePlot plotDV = (PiePlot) cDV.getPlot();
        plotDV.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {1}"));

        charts.addTab("SP đã mua", new ChartPanel(cSP));
        charts.addTab("DV đã dùng", new ChartPanel(cDV));
        bottom.add(charts, BorderLayout.CENTER);

        // … footer …
        split.setBottomComponent(bottom);
        tabbedPane.addTab("Thống Kê", split);

        JPanel foot = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        foot.add(lblTongNgay); foot.add(lblDau); foot.add(lblCuoi);
        bottom.add(foot, BorderLayout.SOUTH);
        split.setBottomComponent(bottom);

        tabbedPane.addTab("Thống Kê", split);

        // Search event
        bSearch.addActionListener(e -> {
            modelKhachTK.setRowCount(0);
            String kw = txtSearch.getText().trim();
            for (KhachHang kh : khBus.timKiemKhachHangTheoTen(kw)) {
                modelKhachTK.addRow(new Object[]{
                    kh.getMaKhachHang(), kh.getTenKhachHang(), kh.getSdt(), kh.getDiaChi()
                });
            }
        });

        // Selection event
        tblKhachTK.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting() && tblKhachTK.getSelectedRow() != -1) {
                int maKH = (int) modelKhachTK.getValueAt(tblKhachTK.getSelectedRow(), 0);
                capNhatChart(maKH);
            }
        });
    }

    private void capNhatChart(int maKH) {
        dsSP.clear();
        for (ThongKeItem it : tkBus.getSanPhamByKhach(maKH)) {
            dsSP.setValue(it.getName(), it.getCount());
        }

        dsDV.clear();
        for (ThongKeItem it : tkBus.getDichVuByKhach(maKH)) {
            dsDV.setValue(it.getName(), it.getCount());
        }

        List<java.sql.Date> dates = tkBus.getInvoiceDates(maKH);
        if (!dates.isEmpty()) {
            dates.sort(null);
            long diff = dates.get(dates.size()-1).getTime() - dates.get(0).getTime();
            long days = diff / (1000*60*60*24);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            lblTongNgay.setText("Tổng ngày đến: " + days);
            lblDau.setText("Ngày đầu: " + sdf.format(dates.get(0)));
            lblCuoi.setText("Ngày cuối: " + sdf.format(dates.get(dates.size()-1)));
        }
    }

    private void loadKhach() {
        modelKhach.setRowCount(0);
        for (KhachHang kh : khBus.layDanhSachKhachHang()) {
            modelKhach.addRow(new Object[]{
                kh.getMaKhachHang(), 
                kh.getTenKhachHang(), 
                kh.getGioiTinh(), // Thêm giới tính
                kh.getSdt(), 
                kh.getDiaChi()
            });
        }
    }

    private void loadKhachTK() {
        modelKhachTK.setRowCount(0);
        for (KhachHang kh : khBus.layDanhSachKhachHang()) {
            modelKhachTK.addRow(new Object[]{
                kh.getMaKhachHang(), 
                kh.getTenKhachHang(), 
                kh.getGioiTinh(), // Thêm giới tính
                kh.getSdt(), 
                kh.getDiaChi()
            });
        }
    }
}