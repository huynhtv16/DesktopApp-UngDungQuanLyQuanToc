// BaoCaoTongQuanUI.java
package com.dungblue.ui;

import com.dungblue.bus.BaoCaoBUS;
import com.dungblue.bus.DichVuBUS;
import com.dungblue.bus.SanPhamBUS;
import com.dungblue.entity.BaoCaoThongKe;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class BaoCaoTongQuanUI extends JPanel {
    private final BaoCaoBUS bus = new BaoCaoBUS();
    private final SanPhamBUS sanPhamBUS = new SanPhamBUS();
    private final DichVuBUS dichVuBUS = new DichVuBUS();
    private JTabbedPane tabbedPane;

    public BaoCaoTongQuanUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Tạo tabbed pane
        tabbedPane = new JTabbedPane();
        
        // Tab 1: Doanh thu
        tabbedPane.addTab("Doanh thu", createDoanhThuTab());
        
        // Tab 2: Sản phẩm
        tabbedPane.addTab("Sản phẩm", createSanPhamTab());
        
        // Tab 3: Dịch vụ
        tabbedPane.addTab("Dịch vụ", createDichVuTab());
        
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createDoanhThuTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        // Panel chọn loại thống kê
        JPanel pnlControl = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlControl.add(new JLabel("Loại thống kê:"));
        
        JComboBox<String> cboLoaiThongKe = new JComboBox<>(new String[]{"Theo ngày", "Theo tháng"});
        pnlControl.add(cboLoaiThongKe);
        
        pnlControl.add(new JLabel("Ngày:"));
        JDatePicker txtNgay = new JDatePicker();
        pnlControl.add(txtNgay);
        
        JButton btnThongKe = new JButton("Thống kê");
        btnThongKe.addActionListener(e -> onThongKeDoanhThuClick(cboLoaiThongKe, txtNgay));
        pnlControl.add(btnThongKe);
        
        panel.add(pnlControl, BorderLayout.NORTH);

        // Tạo biểu đồ mặc định
        JFreeChart chart = ChartFactory.createBarChart(
            "Doanh thu", 
            "Thời gian", 
            "Doanh thu (VND)", 
            new DefaultCategoryDataset(),
            PlotOrientation.VERTICAL,
            true, true, false);
        
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 300));
        panel.add(chartPanel, BorderLayout.CENTER);
        
        // Mặc định chọn ngày hiện tại
        txtNgay.setDate(new Date());
        
        return panel;
    }

    private JPanel createSanPhamTab() {
        JPanel panel = new JPanel(new BorderLayout());

        // Lấy dữ liệu thống kê sản phẩm bán ra từ lớp BUS
        Map<String, Integer> sanPhamData = sanPhamBUS.thongKeSanPham();

        // Tạo dataset cho biểu đồ tròn
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Map.Entry<String, Integer> entry : sanPhamData.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        // Tạo biểu đồ tròn
        JFreeChart chart = ChartFactory.createPieChart(
            "Tỷ lệ sản phẩm bán ra",
            dataset,
            true, true, false
        );

        // Tùy chỉnh biểu đồ nếu cần
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        plot.setCircular(true);
        plot.setLabelGap(0.02);
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {1}"));


        // Thêm biểu đồ vào panel
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 400));
        panel.add(chartPanel, BorderLayout.CENTER);

        return panel;
    }


    private JPanel createDichVuTab() {
    	  JPanel panel = new JPanel(new BorderLayout());

          // Lấy dữ liệu thống kê sản phẩm bán ra từ lớp BUS
          Map<String, Integer> dichVuData = dichVuBUS.thongKeDichVu();

          // Tạo dataset cho biểu đồ tròn
          DefaultPieDataset dataset = new DefaultPieDataset();
          for (Map.Entry<String, Integer> entry : dichVuData.entrySet()) {
              dataset.setValue(entry.getKey(), entry.getValue());
          }

          // Tạo biểu đồ tròn
          JFreeChart chart = ChartFactory.createPieChart(
              "Tỷ lệ dịch vụ đã sử dụng",
              dataset,
              true, true, false
          );

          // Tùy chỉnh biểu đồ nếu cần
          PiePlot plot = (PiePlot) chart.getPlot();
          plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
          plot.setCircular(true);
          plot.setLabelGap(0.02);
          plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {1}"));


          // Thêm biểu đồ vào panel
          ChartPanel chartPanel = new ChartPanel(chart);
          chartPanel.setPreferredSize(new Dimension(500, 400));
          panel.add(chartPanel, BorderLayout.CENTER);

          return panel;
    }

    private void onThongKeDoanhThuClick(JComboBox<String> cboLoaiThongKe, JDatePicker txtNgay) {
        Date ngay = txtNgay.getDate();
        if (ngay == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày");
            return;
        }
        
        List<BaoCaoThongKe> duLieu;
        if (cboLoaiThongKe.getSelectedIndex() == 0) {
            duLieu = bus.thongKeTheoNgay(ngay);
        } else {
            duLieu = bus.thongKeTheoThang(ngay);
        }
        
        hienThiDoanhThu(duLieu, cboLoaiThongKe);
    }

    private void hienThiDoanhThu(List<BaoCaoThongKe> duLieu, JComboBox<String> cboLoaiThongKe) {
        SimpleDateFormat sdf = cboLoaiThongKe.getSelectedIndex() == 0 
                ? new SimpleDateFormat("dd/MM/yyyy") 
                : new SimpleDateFormat("MM/yyyy");
        
        // Tạo dataset cho biểu đồ doanh thu
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        for (BaoCaoThongKe item : duLieu) {
            String ngayStr = sdf.format(item.getNgay());
            dataset.addValue(item.getTongDoanhThu(), "Doanh thu", ngayStr);
        }
        
        // Tạo biểu đồ
        String title = "Doanh thu " + (cboLoaiThongKe.getSelectedIndex() == 0 ? "theo ngày" : "theo tháng");
        JFreeChart chart = ChartFactory.createBarChart(
            title,
            "Thời gian",
            "Doanh thu (VND)",
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
        
        // Cập nhật biểu đồ trong tab Doanh thu
        Component comp = tabbedPane.getComponentAt(0);
        if (comp instanceof JPanel) {
            Component[] components = ((JPanel) comp).getComponents();
            for (Component c : components) {
                if (c instanceof ChartPanel) {
                    ((ChartPanel) c).setChart(chart);
                    break;
                }
            }
        }
    }
}

// Helper class for date picker
class JDatePicker extends JPanel {
    private final JSpinner spinner = new JSpinner(new SpinnerDateModel());
    private final JSpinner.DateEditor editor;

    public JDatePicker() {
        setLayout(new BorderLayout());
        editor = new JSpinner.DateEditor(spinner, "dd/MM/yyyy");
        spinner.setEditor(editor);
        add(spinner, BorderLayout.CENTER);
    }

    public Date getDate() {
        return ((SpinnerDateModel) spinner.getModel()).getDate();
    }
    
    public void setDate(Date date) {
        spinner.setValue(date);
    }
}