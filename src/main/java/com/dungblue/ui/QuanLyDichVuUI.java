package com.dungblue.ui;

import com.dungblue.bus.DichVuBUS;
import com.dungblue.entity.DichVu;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class QuanLyDichVuUI extends JPanel {
    private JTable tblDichVu;
    private DefaultTableModel model;
    private DichVuBUS dichVuBUS = new DichVuBUS();
    private JTextField txtTimKiem;

    public QuanLyDichVuUI() {
        setLayout(new BorderLayout());
        initUI();
        loadData();
    }

    private void initUI() {
        // Bảng danh sách dịch vụ
    	model = new DefaultTableModel(new String[]{"Mã DV", "Tên dịch vụ", "Giá"}, 0) {
    	    @Override
    	    public boolean isCellEditable(int row, int column) {
    	        return false; // Không cho phép chỉnh sửa bất kỳ ô nào
    	    }
    	};
        tblDichVu = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tblDichVu);

        // Add double-click listener to the table
        tblDichVu.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) { // Check for double-click
                    xuatQrDichVu(); // Call the method
                }
            }
        });

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtTimKiem = new JTextField(20);
        JButton btnTimKiem = new JButton("Tìm kiếm");
        topPanel.add(new JLabel("Tìm kiếm:"));
        topPanel.add(txtTimKiem);
        topPanel.add(btnTimKiem);

        JPanel btnPanel = new JPanel();
        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");
        JButton btnLamMoi = new JButton("Làm mới");

        btnPanel.add(btnThem);
        btnPanel.add(btnSua);
        btnPanel.add(btnXoa);
        btnPanel.add(btnLamMoi);

        btnThem.addActionListener(e -> showThemDichVuDialog());
        btnSua.addActionListener(e -> showSuaDichVuDialog());
        btnXoa.addActionListener(e -> xoaDichVu());
        btnTimKiem.addActionListener(e -> timKiemDichVu());
        btnLamMoi.addActionListener(e -> loadData());

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }



    private void loadData() {
        model.setRowCount(0);
        for (DichVu dv : dichVuBUS.layDanhSachDichVu()) {
            model.addRow(new Object[]{
                dv.getMadichvu(),
                dv.getTendichvu(),
                String.format("%,.0f VNĐ", dv.getGia())
            });
        }
    }

    private void timKiemDichVu() {
        String keyword = txtTimKiem.getText().trim();
        model.setRowCount(0);
        for (DichVu dv : dichVuBUS.timKiemDichVu(keyword)) {
            model.addRow(new Object[]{
                dv.getMadichvu(),
                dv.getTendichvu(),
                String.format("%,.0f VNĐ", dv.getGia())
            });
        }
    }

    private void showThemDichVuDialog() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Thêm dịch vụ");

        JPanel panel = new JPanel(new GridLayout(3, 2));
        JTextField txtTen = new JTextField();
        JTextField txtGia = new JTextField();

        panel.add(new JLabel("Tên dịch vụ:"));
        panel.add(txtTen);
        panel.add(new JLabel("Giá:"));
        panel.add(txtGia);

        JButton btnLuu = new JButton("Lưu");
        btnLuu.addActionListener(e -> {
            try {
                DichVu dv = new DichVu();
                dv.setTendichvu(txtTen.getText());
                dv.setGia(Double.parseDouble(txtGia.getText()));
                if (dichVuBUS.themDichVu(dv)) {
                    JOptionPane.showMessageDialog(dialog, "Thêm thành công!");
                    loadData();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Thêm thất bại!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Giá phải là số!");
            }
        });

        panel.add(btnLuu);
        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showSuaDichVuDialog() {
        int selectedRow = tblDichVu.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dịch vụ để sửa!");
            return;
        }

        int madichvu = (int) model.getValueAt(selectedRow, 0);
        DichVu dv = dichVuBUS.layDanhSachDichVu().stream()
                .filter(d -> d.getMadichvu() == madichvu)
                .findFirst()
                .orElse(null);

        if (dv == null) return;

        JDialog dialog = new JDialog();
        dialog.setTitle("Sửa dịch vụ");

        JPanel panel = new JPanel(new GridLayout(3, 2));
        JTextField txtTen = new JTextField(dv.getTendichvu());
        JTextField txtGia = new JTextField(String.valueOf(dv.getGia()));

        panel.add(new JLabel("Tên dịch vụ:"));
        panel.add(txtTen);
        panel.add(new JLabel("Giá:"));
        panel.add(txtGia);

        JButton btnLuu = new JButton("Lưu");
        btnLuu.addActionListener(e -> {
            try {
                dv.setTendichvu(txtTen.getText());
                dv.setGia(Double.parseDouble(txtGia.getText()));
                if (dichVuBUS.suaDichVu(dv)) {
                    JOptionPane.showMessageDialog(dialog, "Sửa thành công!");
                    loadData();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Sửa thất bại!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Giá phải là số!");
            }
        });

        panel.add(btnLuu);
        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    private void xuatQrDichVu() {
        int selectedRow = tblDichVu.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dịch vụ để xuất QR!");
            return;
        }

        int madichvu = (int) model.getValueAt(selectedRow, 0);
        String data = "dv" + String.format("%02d", madichvu); // Format as dvXX

        try {
            QRCodeWriter qrWriter = new QRCodeWriter();
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            BitMatrix bitMatrix = qrWriter.encode(data, BarcodeFormat.QR_CODE, 300, 300, hints);
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            // Create dialog to display QR
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                                         "QR Dịch Vụ " + madichvu, true);
            dialog.setLayout(new BorderLayout(10, 10));

            JLabel lblImage = new JLabel(new ImageIcon(qrImage));
            lblImage.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
            dialog.add(lblImage, BorderLayout.CENTER);

            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton btnSave = new JButton("Lưu QR");
            JButton btnClose = new JButton("Đóng");

            btnPanel.add(btnSave);
            btnPanel.add(btnClose);
            dialog.add(btnPanel, BorderLayout.SOUTH);

            btnSave.addActionListener(e -> {
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Lưu QR Dịch Vụ");
                chooser.setSelectedFile(new File("qr_dv_" + madichvu + ".png"));
                if (chooser.showSaveDialog(dialog) == JFileChooser.APPROVE_OPTION) {
                    try {
                        ImageIO.write(qrImage, "PNG", chooser.getSelectedFile());
                        JOptionPane.showMessageDialog(dialog, "Đã lưu QR thành công!");
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(dialog, "Lỗi lưu QR: " + ex.getMessage(),
                                "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            btnClose.addActionListener(e -> dialog.dispose());

            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);

        } catch (WriterException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tạo QR: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }



    private void xoaDichVu() {
        int selectedRow = tblDichVu.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dịch vụ để xóa!");
            return;
        }

        int madichvu = (int) model.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc chắn muốn xóa dịch vụ này?",
            "Xác nhận xóa",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            if (dichVuBUS.xoaDichVu(madichvu)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!");
            }
        }
    }
}