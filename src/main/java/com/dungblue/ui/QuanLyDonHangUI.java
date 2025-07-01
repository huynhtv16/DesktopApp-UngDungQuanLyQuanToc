package com.dungblue.ui;

import com.dungblue.bus.ChiTietDichVuBUS;
import com.dungblue.bus.ChiTietSanPhamBUS;
import com.dungblue.bus.DichVuBUS;
import com.dungblue.bus.DonHangBUS;
import com.dungblue.bus.SanPhamBUS;
import com.dungblue.bus.KhachHangBUS; // Thêm import
import com.dungblue.entity.ChiTietDichVu;
import com.dungblue.entity.ChiTietSanPham;
import com.dungblue.entity.DichVu;
import com.dungblue.entity.DonHang;
import com.dungblue.entity.SanPham;
import com.dungblue.entity.KhachHang; // Thêm import
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.element.Cell;

import javax.print.PrintService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class QuanLyDonHangUI extends JPanel {
    private DonHangBUS dhBus = new DonHangBUS();
    private ChiTietSanPhamBUS ctSanPhamBus = new ChiTietSanPhamBUS();
    private ChiTietDichVuBUS ctDichVuBus = new ChiTietDichVuBUS();
    private SanPhamBUS spBus = new SanPhamBUS();
    private DichVuBUS dvBus = new DichVuBUS();
    private KhachHangBUS khBus = new KhachHangBUS(); // Thêm BUS khách hàng
    private JTabbedPane tabbedPane = new JTabbedPane();
    private int lastCreatedOrderId = -1;
    
    // Các model cho bảng
    private DefaultTableModel modelDH = new DefaultTableModel(
        new String[]{"Mã ĐH", "Ngày đặt", "Tổng tiền", "Khách hàng", "Nhân viên"}, 0);
    
    private DefaultTableModel modelCT = new DefaultTableModel(
        new String[]{"Loại", "Tên", "Số lượng", "Đơn giá", "Thành tiền"}, 0);
    
    private DefaultTableModel modelSelected = new DefaultTableModel(
        new String[]{"Mã", "Tên", "Loại", "Số lượng", "Đơn giá", "Thành tiền"}, 0);
    
    private JLabel lblTotal = new JLabel("Tổng tiền: 0 VNĐ");

    private JTable tblDH = new JTable(modelDH);
    private JTable tblCT = new JTable(modelCT);

    public QuanLyDonHangUI() {
        setLayout(new BorderLayout());
        initUI();
        loadDonHang();
        
        // Reset dữ liệu khi chuyển tab
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 1) { // Tab Tạo Đơn Hàng
                modelSelected.setRowCount(0);
                lblTotal.setText("Tổng tiền: 0 VNĐ");
            }
        });
    }

    private void initUI() {
        tabbedPane.addTab("Danh Sách Đơn Hàng", createDanhSachDonHangPanel());
        tabbedPane.addTab("Tạo Đơn Hàng", createTaoDonHangPanel());
        add(tabbedPane, BorderLayout.CENTER);
    }
    

    // Panel danh sách đơn hàng
    private JPanel createDanhSachDonHangPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        
        // Bảng danh sách đơn hàng
        JPanel topPanel = new JPanel(new BorderLayout());
        JScrollPane scrollDH = new JScrollPane(tblDH);
        scrollDH.setPreferredSize(new Dimension(0, 150));
        topPanel.add(scrollDH, BorderLayout.CENTER);
        
        // Thanh tìm kiếm
        JTextField txtTimKiem = new JTextField(20);
        JButton btnTimKiem = new JButton("Tìm kiếm");
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Tìm kiếm:"));
        searchPanel.add(txtTimKiem);
        searchPanel.add(btnTimKiem);
        
        // Panel nút chức năng
        JPanel btnPanel = new JPanel();
        btnPanel.add(createButton("Sửa", this::suaDonHang));
        btnPanel.add(createButton("Xóa", this::xoaDonHang));
        // Làm mới
        btnPanel.add(createButton("Làm mới", this::loadDonHang));
        btnPanel.add(createButton("In", () -> {
            int row = tblDH.getSelectedRow();
            if (row >= 0) {
                int maDH = (int) modelDH.getValueAt(row, 0);
                printInvoice(maDH);
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn đơn hàng để in");
            }
        }));

        // Gắn các thành phần
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(btnPanel, BorderLayout.SOUTH);
        
        // Panel chi tiết
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Chi tiết đơn hàng"));
        bottomPanel.add(new JScrollPane(tblCT), BorderLayout.CENTER);

        // Sự kiện tìm kiếm
        btnTimKiem.addActionListener(e -> searchDonHang(txtTimKiem.getText()));
        
        // Sự kiện chọn đơn hàng
        tblDH.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) loadChiTiet();
        });
        
        // Sự kiện double click để xuất hóa đơn
        tblDH.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tblDH.rowAtPoint(e.getPoint());
                    if (row >= 0) {
                        int maDH = (int) modelDH.getValueAt(row, 0);
                        xuatHoaDon(maDH);
                    }
                }
            }
        });
        

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(bottomPanel, BorderLayout.CENTER);
        
        return panel;
    }

    // Xuất hóa đơn
    private void xuatHoaDon(int maDH) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn nơi lưu hóa đơn");
        fileChooser.setSelectedFile(new File("invoice_" + maDH + ".pdf"));
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try {
                generateInvoicePDF(maDH, fileToSave.getAbsolutePath());
                JOptionPane.showMessageDialog(this,
                    "Xuất hóa đơn thành công:\n" + fileToSave.getAbsolutePath());
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                    "Lỗi khi tạo hóa đơn: " + ex.getMessage());
            }
        }
    }
    

    // Panel tạo đơn hàng 
    private JPanel createTaoDonHangPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Phần tìm kiếm và chọn loại
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField txtSearch = new JTextField(20);
        JButton btnSearch = new JButton("Tìm kiếm");
        JComboBox<String> cboLoai = new JComboBox<>(new String[]{"Sản phẩm", "Dịch vụ"});
        JButton btnQuetQR = new JButton("Quét QR");
        searchPanel.add(new JLabel("Tìm kiếm:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(new JLabel("Loại:"));
        searchPanel.add(cboLoai);
        searchPanel.add(btnQuetQR);

        // Bảng sản phẩm/dịch vụ
        DefaultTableModel modelSPDV = new DefaultTableModel(
            new String[]{"Mã", "Tên", "Loại", "Đơn giá"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tblSPDV = new JTable(modelSPDV);
       
        // Thêm sự kiện double-click để thêm vào đơn hàng
        tblSPDV.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = tblSPDV.rowAtPoint(e.getPoint());
                    if (selectedRow >= 0) {
                        int ma = (int) modelSPDV.getValueAt(selectedRow, 0);
                        String ten = (String) modelSPDV.getValueAt(selectedRow, 1);
                        String loai = (String) modelSPDV.getValueAt(selectedRow, 2);
                        double gia = (double) modelSPDV.getValueAt(selectedRow, 3);
                        
                        // Kiểm tra xem đã có trong bảng chi tiết chưa
                        boolean found = false;
                        for (int i = 0; i < modelSelected.getRowCount(); i++) {
                            if ((int) modelSelected.getValueAt(i, 0) == ma) {
                                // Nếu đã có thì tăng số lượng
                                int currentQty = (int) modelSelected.getValueAt(i, 3);
                                modelSelected.setValueAt(currentQty + 1, i, 3);
                                modelSelected.setValueAt((currentQty + 1) * gia, i, 5);
                                found = true;
                                break;
                            }
                        }
                        
                        // Nếu chưa có thì thêm mới
                        if (!found) {
                            modelSelected.addRow(new Object[]{
                                ma,
                                ten,
                                loai,
                                1,
                                gia,
                                gia
                            });
                        }
                        
                        // Cập nhật tổng tiền
                        updateTotal(QuanLyDonHangUI.this.lblTotal, modelSelected);
                    }
                }
            }
        });
        

        // Bảng chi tiết đã chọn
        JTable tblSelected = new JTable(modelSelected) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Thêm sự kiện double-click để xóa
        tblSelected.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tblSelected.rowAtPoint(e.getPoint());
                    if (row >= 0) {
                        modelSelected.removeRow(row);
                        updateTotal(QuanLyDonHangUI.this.lblTotal, modelSelected);
                    }
                }
            }
        });

        // Nút và tổng tiền
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JButton btnTaoDon = new JButton("Tạo Đơn");
        JButton btnTaoHoaDon = new JButton("Tạo Hóa Đơn"); 
        this.lblTotal.setText("Tổng tiền: 0 VNĐ"); // Sử dụng biến instance
        bottomPanel.add(btnTaoDon, BorderLayout.WEST);
        bottomPanel.add(btnTaoHoaDon, BorderLayout.CENTER);
        bottomPanel.add(this.lblTotal, BorderLayout.EAST); // Sử dụng biến instance

        // Layout
        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(tblSPDV), BorderLayout.WEST);
        panel.add(new JScrollPane(tblSelected), BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        // Set default selection to "Dịch vụ"
        cboLoai.setSelectedItem("Dịch vụ");
        loadSanPhamDichVu(modelSPDV, "Dịch vụ");

        // Sự kiện chọn loại
        cboLoai.addActionListener(e -> {
            String selectedType = (String) cboLoai.getSelectedItem();
            loadSanPhamDichVu(modelSPDV, selectedType);
        });

        // Sự kiện tìm kiếm
        btnSearch.addActionListener(e -> {
            String keyword = txtSearch.getText();
            String selectedType = (String) cboLoai.getSelectedItem();
            modelSPDV.setRowCount(0);
            
            if ("Sản phẩm".equals(selectedType)) {
                for (SanPham sp : spBus.layDanhSachSanPham()) {
                    if (sp.getTenSanPham().toLowerCase().contains(keyword.toLowerCase())) {
                        modelSPDV.addRow(new Object[]{
                            sp.getMaSanPham(),
                            sp.getTenSanPham(),
                            "Sản phẩm",
                            sp.getGiaBan()
                        });
                    }
                }
            } else if ("Dịch vụ".equals(selectedType)) {
                for (DichVu dv : dvBus.layDanhSachDichVu()) {
                    if (dv.getTendichvu().toLowerCase().contains(keyword.toLowerCase())) {
                        modelSPDV.addRow(new Object[]{
                            dv.getMadichvu(),
                            dv.getTendichvu(),
                            "Dịch vụ",
                            dv.getGia()
                        });
                    }
                }
            }
        });

        // Sự kiện quét QR
        btnQuetQR.addActionListener(e -> {    	
            String qrResult = scanQrCode();
            if (qrResult != null) {
                try {
                    String[] parts;
                    // Xử lý cả 2 định dạng QR
                    if (qrResult.contains(":")) {
                        parts = qrResult.split(":");
                    } else {
                        // Xử lý định dạng cũ: sp123, dv456
                        String prefix = qrResult.substring(0, 2).toLowerCase();
                        String id = qrResult.substring(2);
                        
                        if ("sp".equals(prefix)) {
                            parts = new String[]{"Sản phẩm", id};
                        } else if ("dv".equals(prefix)) {
                            parts = new String[]{"Dịch vụ", id};
                        } else {
                            JOptionPane.showMessageDialog(this, "QR không hợp lệ!");
                            return;
                        }
                    }
                    
                    if (parts.length != 2) {
                        JOptionPane.showMessageDialog(this, "QR không hợp lệ!");
                        return;
                    }

                    String loai = parts[0];
                    int id = Integer.parseInt(parts[1]);
                    boolean found = false;

                    if ("Sản phẩm".equals(loai)) {
                        SanPham sp = spBus.laySanPhamTheoMa(id);
                        if (sp != null) {
                            // Tìm trong bảng đã chọn
                            for (int i = 0; i < modelSelected.getRowCount(); i++) {
                                if ((int) modelSelected.getValueAt(i, 0) == id) {
                                    int currentQty = (int) modelSelected.getValueAt(i, 3);
                                    modelSelected.setValueAt(currentQty + 1, i, 3);
                                    modelSelected.setValueAt((currentQty + 1) * sp.getGiaBan(), i, 5);
                                    found = true;
                                    break;
                                }
                            }
                            
                            // Thêm mới nếu chưa có
                            if (!found) {
                                modelSelected.addRow(new Object[]{
                                    sp.getMaSanPham(),
                                    sp.getTenSanPham(),
                                    "Sản phẩm",
                                    1,
                                    sp.getGiaBan(),
                                    sp.getGiaBan()
                                });
                            }
                            updateTotal(QuanLyDonHangUI.this.lblTotal, modelSelected);
                        } else {
                            JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm với mã: " + id);
                        }
                    } 
                    else if ("Dịch vụ".equals(loai)) {
                        DichVu dv = dvBus.layDichVuTheoMa(id);
                        if (dv != null) {
                            // Tìm trong bảng đã chọn
                            for (int i = 0; i < modelSelected.getRowCount(); i++) {
                                if ((int) modelSelected.getValueAt(i, 0) == id) {
                                    int currentQty = (int) modelSelected.getValueAt(i, 3);
                                    modelSelected.setValueAt(currentQty + 1, i, 3);
                                    modelSelected.setValueAt((currentQty + 1) * dv.getGia(), i, 5);
                                    found = true;
                                    break;
                                }
                            }
                            
                            // Thêm mới nếu chưa có
                            if (!found) {
                                modelSelected.addRow(new Object[]{
                                    dv.getMadichvu(),
                                    dv.getTendichvu(),
                                    "Dịch vụ",
                                    1,
                                    dv.getGia(),
                                    dv.getGia()
                                });
                            }
                            updateTotal(QuanLyDonHangUI.this.lblTotal, modelSelected);
                        } else {
                            JOptionPane.showMessageDialog(this, "Không tìm thấy dịch vụ với mã: " + id);
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "QR không hợp lệ: " + ex.getMessage());
                }
            }
        });

        // Sự kiện tạo hóa đơn
        btnTaoHoaDon.addActionListener(e -> {
            if (lastCreatedOrderId <= 0) {
                JOptionPane.showMessageDialog(this, "Chưa có đơn nào vừa tạo để xuất hóa đơn!");
                return;
            }

            // Mở JFileChooser để chọn nơi lưu
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn nơi lưu hóa đơn");
            fileChooser.setSelectedFile(new File("invoice_" + lastCreatedOrderId + ".pdf"));
            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                try {
                    generateInvoicePDF(lastCreatedOrderId, fileToSave.getAbsolutePath());
                    JOptionPane.showMessageDialog(this,
                        "Xuất hóa đơn thành công:\n" + fileToSave.getAbsolutePath());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this,
                        "Lỗi khi tạo hóa đơn: " + ex.getMessage());
                }
            }
        });
        

        // Sự kiện tạo đơn
        btnTaoDon.addActionListener(e -> {
            if (modelSelected.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm/dịch vụ!");
                return;
            }

            // Tạo panel chứa form
            JPanel popupPanel = new JPanel(new BorderLayout(5, 5));
            
            // Panel thông tin khách hàng
            JPanel khPanel = new JPanel(new GridLayout(2, 2, 5, 5));
            JTextField txtSDT = new JTextField(15);
            JButton btnTimKH = new JButton("Tìm");
            JLabel lblLichSu = new JLabel(" ");
            lblLichSu.setForeground(Color.RED);
            
            JPanel sdtPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            sdtPanel.add(txtSDT);
            sdtPanel.add(btnTimKH);
            
            khPanel.add(new JLabel("Số điện thoại:"));
            khPanel.add(sdtPanel);
            khPanel.add(new JLabel("Lịch sử mua hàng:"));
            khPanel.add(lblLichSu);
            
            // Panel thông tin nhân viên
            JPanel nvPanel = new JPanel(new GridLayout(1, 2, 5, 5));
            JTextField txtMaNV = new JTextField();
            nvPanel.add(new JLabel("Mã nhân viên:"));
            nvPanel.add(txtMaNV);
            
            // Gộp các panel
            popupPanel.add(khPanel, BorderLayout.NORTH);
            popupPanel.add(nvPanel, BorderLayout.CENTER);
            
            // Sự kiện tìm kiếm khách hàng
            btnTimKH.addActionListener(ev -> {
                String sdt = txtSDT.getText().trim();
                if (sdt.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập số điện thoại");
                    return;
                }
                
                // Tìm khách hàng theo số điện thoại
                KhachHang kh = khBus.timKhachHangTheoSDT(sdt);
                if (kh != null) {
                    // Đếm số đơn hàng của khách hàng
                    int soDon = dhBus.demSoDonHangCuaKhachHang(kh.getMaKhachHang());
                    lblLichSu.setText("Đã mua " + soDon + " đơn hàng");
                } else {
                    lblLichSu.setText("Khách hàng mới");
                }
            });
            
            int result = JOptionPane.showConfirmDialog(
                this, popupPanel, "Thông tin đơn hàng", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    String sdt = txtSDT.getText().trim();
                    if (sdt.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Vui lòng nhập số điện thoại");
                        return;
                    }
                    
                    // Tìm hoặc tạo khách hàng
                    KhachHang kh = khBus.timKhachHangTheoSDT(sdt);
                    int maKH;
                    
                    if (kh == null) {
                        maKH = khBus.themKhachHangChiCoSDT(sdt);
                        // Đảm bảo khách hàng được tạo thành công
                        if (maKH <= 0) {
                            throw new Exception("Không thể tạo khách hàng mới");
                        }
                    } else {
                        maKH = kh.getMaKhachHang();
                    }
                    
                    // Tạo đơn hàng
                    DonHang dh = new DonHang();
                    dh.setMaKhachHang(maKH);
                    dh.setMaNguoiDung(Integer.parseInt(txtMaNV.getText().trim()));
                    dh.setNgayDatHang(new java.sql.Date(System.currentTimeMillis()));
                    dh.setTongTien(calculateTotal(modelSelected));
                    
                    int maDH = dhBus.themDonHang(dh);
                    lastCreatedOrderId = maDH;
                    
                    // Kiểm tra đơn hàng được tạo thành công
                    if (maDH <= 0) {
                        throw new Exception("Không thể tạo đơn hàng");
                    }

                    // Thêm chi tiết
                    for (int i = 0; i < modelSelected.getRowCount(); i++) {
                        int ma = (int) modelSelected.getValueAt(i, 0);
                        String loai = (String) modelSelected.getValueAt(i, 2);
                        int sl = (int) modelSelected.getValueAt(i, 3);
                        double gia = (double) modelSelected.getValueAt(i, 4);

                        if (loai.equals("Sản phẩm")) {
                            ChiTietSanPham ctsp = new ChiTietSanPham();
                            ctsp.setMaDonHang(maDH);
                            ctsp.setMaSanPham(ma);
                            ctsp.setSoLuong(sl);
                            ctsp.setGia(gia);
                            
                            try {
                                ctSanPhamBus.themChiTietSanPham(ctsp);
                            } catch (Exception ex) {
                                // Xử lý lỗi nếu cần
                                ex.printStackTrace();
                            }
                        
                        } else if (loai.equals("Dịch vụ")) {
                            ChiTietDichVu ctdv = new ChiTietDichVu();
                            ctdv.setMaDonHang(maDH);
                            ctdv.setMaDichVu(ma);
                            ctdv.setSoLuong(sl);
                            ctdv.setGia(gia);
                            ctDichVuBus.themChiTietDichVu(ctdv);
                        }
                    }

                    // Reset và thông báo
                    modelSelected.setRowCount(0);
                    lblTotal.setText("Tổng tiền: 0 VNĐ");
                    loadDonHang();
                    JOptionPane.showMessageDialog(this, "Tạo đơn thành công!");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
                }
            }
        });
        return panel;
    }
    private void printInvoice(int maDH) {
        // Lấy danh sách máy in
        PrintService[] printServices = PrinterJob.lookupPrintServices();
        String[] printerNames = Arrays.stream(printServices)
                                     .map(PrintService::getName)
                                     .toArray(String[]::new);
        
        if (printerNames.length == 0) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy máy in nào!");
            return;
        }

        // Hiển thị dialog chọn máy in
        String selectedPrinter = (String) JOptionPane.showInputDialog(
            this,
            "Chọn máy in:",
            "Chọn máy in",
            JOptionPane.PLAIN_MESSAGE,
            null,
            printerNames,
            printerNames[0]
        );

        if (selectedPrinter != null) {
            try {
                InHoaDon.inHoaDon(maDH, selectedPrinter);
                JOptionPane.showMessageDialog(this, "Đã gửi lệnh in thành công!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi in: " + ex.getMessage());
            }
        }
    }

    private void generateInvoicePDF(int maDH, String dest) throws Exception {
        // Lấy thông tin đơn hàng
        DonHang dh = dhBus.layDonHangTheoMa(maDH);
        List<ChiTietSanPham> listSp = ctSanPhamBus.layChiTietTheoDonHang(maDH);
        List<ChiTietDichVu> listDv = ctDichVuBus.layChiTietTheoDonHang(maDH);

        // Tạo PDF
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Header
        document.add(new Paragraph("HÓA ĐƠN BÁN HÀNG")
            .setBold().setFontSize(16));
        document.add(new Paragraph("Mã đơn hàng: " + maDH));
        document.add(new Paragraph("Ngày: " +
            new SimpleDateFormat("dd/MM/yyyy").format(dh.getNgayDatHang())));
        document.add(new Paragraph("Khách hàng: " + dh.getMaKhachHang() +
                                   "    Nhân viên: " + dh.getMaNguoiDung()));
        document.add(new Paragraph("\n"));

        // Table chi tiết
        Table table = new Table(UnitValue.createPercentArray(new float[]{2, 6, 2, 2, 2}))
                             .useAllAvailableWidth();
        table.addHeaderCell("Loại");
        table.addHeaderCell("Tên");
        table.addHeaderCell("SL");
        table.addHeaderCell("Đơn giá");
        table.addHeaderCell("Thành tiền");

        for (ChiTietSanPham ct : listSp) {
            SanPham sp = spBus.laySanPhamTheoMa(ct.getMaSanPham());
            table.addCell("Sản phẩm");
            table.addCell(sp.getTenSanPham());
            table.addCell(String.valueOf(ct.getSoLuong()));
            table.addCell(String.format("%,.0f", ct.getGia()));
            table.addCell(String.format("%,.0f", ct.getSoLuong() * ct.getGia()));
        }
        for (ChiTietDichVu ct : listDv) {
            DichVu dv = dvBus.layDichVuTheoMa(ct.getMaDichVu());
            table.addCell("Dịch vụ");
            table.addCell(dv.getTendichvu());
            table.addCell(String.valueOf(ct.getSoLuong()));
            table.addCell(String.format("%,.0f", ct.getGia()));
            table.addCell(String.format("%,.0f", ct.getSoLuong() * ct.getGia()));
        }

        document.add(table);
        document.add(new Paragraph("\nTổng tiền: "
            + String.format("%,.0f VNĐ", dh.getTongTien())).setBold());

        document.close();
    }

    private String scanQrCode() {
        Webcam webcam = null;
        try {
            webcam = Webcam.getDefault();
            if (webcam == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy camera!");
                return null;
            }

            webcam.setViewSize(WebcamResolution.QVGA.getSize());
            webcam.open();

            long timeout = 30_000; 
            long start = System.currentTimeMillis();

            while (System.currentTimeMillis() - start < timeout) {
                BufferedImage image = webcam.getImage();
                if (image == null) continue;

                LuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                try {
                    Result result = new MultiFormatReader().decode(bitmap);
                    String qrData = result.getText();
                    System.out.println("QR Data: " + qrData);

                    // Kiểm tra định dạng QR code
                    if (qrData.matches("^(sp|dv)\\d+$")) {
                        String prefix = qrData.substring(0, 2).toLowerCase();
                        String idStr = qrData.substring(2);
                        
                        if ("sp".equals(prefix)) {
                            return "Sản phẩm:" + idStr;
                        } else if ("dv".equals(prefix)) {
                            return "Dịch vụ:" + idStr;
                        }
                    } else if (qrData.startsWith("Sản phẩm:") || qrData.startsWith("Dịch vụ:")) {
                        return qrData;
                    } else {
                        JOptionPane.showMessageDialog(this, "QR không hợp lệ! Định dạng đúng: 'sp123' hoặc 'dv123'");
                    }
                } catch (NotFoundException e) {
                    // Bỏ qua nếu không tìm thấy QR code
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }

            JOptionPane.showMessageDialog(this, "Hết thời gian quét!");
            return null;

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi quét QR: " + ex.getMessage());
            return null;
        } finally {
            if (webcam != null && webcam.isOpen()) {
                webcam.close();
            }
        }
    }

    private void loadSanPhamDichVu(DefaultTableModel model, String type) {
        model.setRowCount(0);
        if ("Sản phẩm".equals(type)) {
            for (SanPham sp : spBus.layDanhSachSanPham()) {
                model.addRow(new Object[]{
                    sp.getMaSanPham(),
                    sp.getTenSanPham(),
                    "Sản phẩm",
                    sp.getGiaBan()
                });
            }
        } else if ("Dịch vụ".equals(type)) {
            for (DichVu dv : dvBus.layDanhSachDichVu()) {
                model.addRow(new Object[]{
                    dv.getMadichvu(),
                    dv.getTendichvu(),
                    "Dịch vụ",
                    dv.getGia()
                });
            }
        }
    }
    
    private void updateTotal(JLabel lbl, DefaultTableModel model) {
        double total = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            total += (double) model.getValueAt(i, 5);
        }
        String formatted = String.format("Tổng tiền: %,.0f VNĐ", total);
        lbl.setText(formatted);
        
        // ĐẢM BẢO CẬP NHẬT GIAO DIỆN
        lbl.revalidate();
        lbl.repaint();
    }

    private double calculateTotal(DefaultTableModel model) {
        double total = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            total += (double) model.getValueAt(i, 5);
        }
        return total;
    }

    private void searchDonHang(String keyword) {
        modelDH.setRowCount(0);
        for (DonHang dh : dhBus.layDanhSachDonHang()) {
            if (String.valueOf(dh.getMaDonHang()).contains(keyword) || 
                String.valueOf(dh.getMaKhachHang()).contains(keyword) ||
                String.valueOf(dh.getMaNguoiDung()).contains(keyword)) {
                modelDH.addRow(new Object[]{
                    dh.getMaDonHang(),
                    new SimpleDateFormat("dd/MM/yyyy").format(dh.getNgayDatHang()),
                    dh.getTongTien(),
                    dh.getMaKhachHang(),
                    dh.getMaNguoiDung()
                });
            }
        }
    }

    private JButton createButton(String text, Runnable action) {
        JButton btn = new JButton(text);
        btn.addActionListener(e -> action.run());
        return btn;
    }

    private void loadChiTiet() {
        modelCT.setRowCount(0);
        int selectedRow = tblDH.getSelectedRow();
        if (selectedRow == -1) return;
        
        int maDH = (int) modelDH.getValueAt(selectedRow, 0);
        
        // Load chi tiết sản phẩm
        for (ChiTietSanPham ct : ctSanPhamBus.layChiTietTheoDonHang(maDH)) {
            SanPham sp = spBus.laySanPhamTheoMa(ct.getMaSanPham());
            modelCT.addRow(new Object[]{
                "Sản phẩm",
                sp != null ? sp.getTenSanPham() : "Không xác định",
                ct.getSoLuong(),
                String.format("%,.0f VNĐ", ct.getGia()),
                String.format("%,.0f VNĐ", ct.getSoLuong() * ct.getGia())
            });
        }
        
        // Load chi tiết dịch vụ
        for (ChiTietDichVu ct : ctDichVuBus.layChiTietTheoDonHang(maDH)) {
            DichVu dv = dvBus.layDichVuTheoMa(ct.getMaDichVu());
            modelCT.addRow(new Object[]{
                "Dịch vụ", 
                dv != null ? dv.getTendichvu() : "Không xác định",
                ct.getSoLuong(),
                String.format("%,.0f VNĐ", ct.getGia()),
                String.format("%,.0f VNĐ", ct.getSoLuong() * ct.getGia())
            });
        }
    }

    private void suaDonHang() {
        int row = tblDH.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Chọn đơn hàng để sửa.");
            return;
        }

        int maDH = (int) modelDH.getValueAt(row, 0);
        DonHang dh = dhBus.layDonHangTheoMa(maDH);

        JTextField txtMaKH = new JTextField(String.valueOf(dh.getMaKhachHang()));
        JTextField txtMaNV = new JTextField(String.valueOf(dh.getMaNguoiDung()));
        JTextField txtNgayDat = new JTextField(new SimpleDateFormat("dd/MM/yyyy").format(dh.getNgayDatHang()));
        JTextField txtTongTien = new JTextField(String.valueOf(dh.getTongTien()));

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Mã KH:")); panel.add(txtMaKH);
        panel.add(new JLabel("Mã NV:")); panel.add(txtMaNV);
        panel.add(new JLabel("Ngày đặt:")); panel.add(txtNgayDat);
        panel.add(new JLabel("Tổng tiền:")); panel.add(txtTongTien);

        int result = JOptionPane.showConfirmDialog(this, panel, "Sửa Đơn Hàng", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                dh.setMaKhachHang(Integer.parseInt(txtMaKH.getText()));
                dh.setMaNguoiDung(Integer.parseInt(txtMaNV.getText()));
                dh.setNgayDatHang(new java.sql.Date(new SimpleDateFormat("dd/MM/yyyy").parse(txtNgayDat.getText()).getTime()));
                dh.setTongTien(Double.parseDouble(txtTongTien.getText()));
                dhBus.suaDonHang(dh);
                loadDonHang();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        }
    }

    private void xoaDonHang() {
        int row = tblDH.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Chọn đơn hàng để xóa.");
            return;
        }

        int maDH = (int) modelDH.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dhBus.xoaDonHang(maDH);
            loadDonHang();
        }
    }

    private void loadDonHang() {
        modelDH.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for (DonHang d : dhBus.layDanhSachDonHang()) {
            modelDH.addRow(new Object[]{
                d.getMaDonHang(),
                sdf.format(d.getNgayDatHang()),
                d.getTongTien(),
                d.getMaKhachHang(),
                d.getMaNguoiDung()
            });
        }
    }
}