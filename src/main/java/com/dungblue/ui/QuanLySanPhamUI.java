package com.dungblue.ui;

import com.dungblue.bus.DanhMucBUS;
import com.dungblue.bus.SanPhamBUS;
import com.dungblue.entity.DanhMuc;
import com.dungblue.entity.SanPham;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuanLySanPhamUI extends JPanel {
    // BUS
    private SanPhamBUS sanPhamBUS = new SanPhamBUS();
    private DanhMucBUS danhMucBUS = new DanhMucBUS();

    // Tabbed pane
    private JTabbedPane tabbedPane = new JTabbedPane();

    // === Tab Sản Phẩm (card view) ===
    private JTextField txtTimSP = new JTextField(20);
    private JPanel cardsPanel;
    private JScrollPane scrollPane;

    // === Tab Danh Mục ===
    private DefaultTableModel modelDanhMuc;
    private JTable tableDanhMuc;
    private JTextField txtTimDM = new JTextField(20);

    public QuanLySanPhamUI() {
        setLayout(new BorderLayout(5,5));
        initSanPhamTab();
        initDanhMucTab();
        add(tabbedPane, BorderLayout.CENTER);
    }

    private void initSanPhamTab() {
        JPanel pSP = new JPanel(new BorderLayout(5, 5));
        JPanel topSP = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topSP.add(new JLabel("Tìm kiếm:"));
        JButton btnTimSP = new JButton("Tìm");
        JButton btnScan = new JButton("📷 Quét QR");
        topSP.add(txtTimSP);
        topSP.add(btnTimSP);
        topSP.add(btnScan);
        pSP.add(topSP, BorderLayout.NORTH);

        cardsPanel = new JPanel();
        cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
        scrollPane = new JScrollPane(cardsPanel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        pSP.add(scrollPane, BorderLayout.CENTER);

        JPanel botSP = new JPanel();
        JButton btnThemSP = new JButton("Thêm");
        JButton btnSuaSP = new JButton("Sửa");
        JButton btnXoaSP = new JButton("Xóa");
        JButton btnReSP = new JButton("Làm mới"); // New "Làm mới" button
        botSP.add(btnThemSP);
        botSP.add(btnSuaSP);
        botSP.add(btnXoaSP);
        botSP.add(btnReSP); // Add the button to the panel
        pSP.add(botSP, BorderLayout.SOUTH);

        btnTimSP.addActionListener(e -> loadSPCards(txtTimSP.getText()));
        txtTimSP.addActionListener(e -> loadSPCards(txtTimSP.getText()));
        btnScan.addActionListener(e -> scanQrAndAddProduct());
        btnThemSP.addActionListener(e -> showThemSanPhamDialog());
        btnSuaSP.addActionListener(e -> {
            Integer id = (Integer) cardsPanel.getClientProperty("sel");
            if (id != null) showSuaSanPhamDialog(id);
        });
        btnReSP.addActionListener(e -> { // Add action listener for "Làm mới"
            txtTimSP.setText("");
            loadSPCards("");
        });
        btnXoaSP.addActionListener(e -> {
            Integer id = (Integer) cardsPanel.getClientProperty("sel");
            if (id != null && JOptionPane.showConfirmDialog(this, "Xóa SP?", "", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                sanPhamBUS.xoaSanPham(id);
                loadSPCards(txtTimSP.getText());
            }
        });

        tabbedPane.addTab("Sản Phẩm", pSP);
        loadSPCards("");
    }


    private void loadSPCards(String kw) {
        cardsPanel.removeAll();
        cardsPanel.putClientProperty("sel", null);
        List<SanPham> ds = sanPhamBUS.timKiemSanPham(kw);
        for (SanPham sp : ds) {
            JPanel card = makeCard(sp);
            card.setPreferredSize(new Dimension(200, 200));
            cardsPanel.add(card);
        }
        cardsPanel.revalidate();
        cardsPanel.repaint();
    }
    private JPanel makeCard(SanPham sp) {
        JPanel c = new JPanel(new BorderLayout(5, 5));
        c.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        c.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel img = new JLabel();
        try {
            Image i = ImageIO.read(new File(sp.getDuongdan()))
                       .getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            img.setIcon(new ImageIcon(i));
        } catch (Exception e) {
            img.setText("No Image");
            img.setHorizontalAlignment(JLabel.CENTER);
        }
        c.add(img, BorderLayout.WEST);

        JPanel info = new JPanel(new GridLayout(0, 1));
        info.add(new JLabel("Mã SP: " + sp.getMaSanPham()));
        info.add(new JLabel("Tên SP: " + sp.getTenSanPham()));
        info.add(new JLabel("Giá nhập: " + sp.getGiaNhap()));
        info.add(new JLabel("Giá bán: " + sp.getGiaBan()));
        info.add(new JLabel("SL: " + sp.getSoLuong()));
        info.add(new JLabel("DM: " + sp.getMaDanhMuc()));
        c.add(info, BorderLayout.CENTER);

        c.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                cardsPanel.putClientProperty("sel", sp.getMaSanPham());
                for (Component comp : cardsPanel.getComponents()) {
                    comp.setBackground(null);
                }
                c.setBackground(Color.LIGHT_GRAY);
                if (e.getClickCount() == 2) {
                    showProductQrDialog(sp);
                }
            }
        });

        return c;
    }
    private void showProductQrDialog(SanPham sp) {
        // 1. Chuẩn bị dữ liệu cho QR (chỉ mã sản phẩm)
        String data = "sp" + sp.getMaSanPham();

        // 2. Tạo BitMatrix (có thể ném WriterException)
        BitMatrix bitMatrix;
        try {
            QRCodeWriter qrWriter = new QRCodeWriter();
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            bitMatrix = qrWriter.encode(data, BarcodeFormat.QR_CODE, 300, 300, hints);
        } catch (WriterException we) {
            JOptionPane.showMessageDialog(
                this,
                "Lỗi khi tạo QR Code:\n" + we.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // 3. Chuyển sang BufferedImage
        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        // 4. Tạo dialog
        JDialog dialog = new JDialog(
            (Frame) SwingUtilities.getWindowAncestor(this),
            "QR Code SP " + sp.getMaSanPham(),
            true
        );
        dialog.setLayout(new BorderLayout(10, 10));

        JLabel lblImage = new JLabel(new ImageIcon(qrImage));
        lblImage.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        dialog.add(lblImage, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Lưu QR");
        JButton btnClose = new JButton("Đóng");
        btnPanel.add(btnSave);
        btnPanel.add(btnClose);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        // 5. Xử lý lưu ảnh
        btnSave.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Lưu QR sản phẩm");
            chooser.setSelectedFile(new File("qr_sp_" + sp.getMaSanPham() + ".png"));
            if (chooser.showSaveDialog(dialog) == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                try {
                    ImageIO.write(qrImage, "PNG", f);
                    JOptionPane.showMessageDialog(dialog, "Đã lưu: " + f.getAbsolutePath());
                } catch (IOException io) {
                    JOptionPane.showMessageDialog(
                        dialog,
                        "Lỗi lưu file:\n" + io.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        btnClose.addActionListener(e -> dialog.dispose());

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }


    private void scanQrAndAddProduct() {
        JFrame frame = new JFrame("Quét QR sản phẩm");
        Webcam webcam = Webcam.getDefault();
        webcam.setViewSize(WebcamResolution.VGA.getSize());
        WebcamPanel panel = new WebcamPanel(webcam);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);

        new Thread(() -> {
            MultiFormatReader reader = new MultiFormatReader();
            Result qrResult = null;

            // Chạy liên tục cho đến khi đọc được QR
            while (qrResult == null) {
                BufferedImage image = webcam.getImage();
                if (image == null) {
                    continue;
                }
                try {
                    qrResult = reader.decodeWithState(
                        new com.google.zxing.BinaryBitmap(
                            new HybridBinarizer(new BufferedImageLuminanceSource(image))
                        )
                    );
                } catch (NotFoundException e) {
                    // Chưa tìm thấy QR trên khung hình này, bỏ qua và tiếp tục loop
                }
            }

            // Khi đã có kết quả
            String data = qrResult.getText();
            webcam.close();
            SwingUtilities.invokeLater(frame::dispose);

            // Parse data và thêm sản phẩm
            Map<String, String> map = new HashMap<>();
            for (String line : data.split("\n")) {
                String[] kv = line.split(":", 2);
                if (kv.length == 2) map.put(kv[0].trim(), kv[1].trim());
            }
            try {
                SanPham sp = new SanPham();
                sp.setTenSanPham(map.getOrDefault("Tên", ""));
                sp.setGiaNhap(Double.parseDouble(map.getOrDefault("Giá Nhập", "0")));
                sp.setGiaBan(Double.parseDouble(map.getOrDefault("Giá Bán", "0")));
                sp.setSoLuong(Integer.parseInt(map.getOrDefault("SL", "0")));
                sp.setMaDanhMuc(Integer.parseInt(map.getOrDefault("DM", "0")));
                sp.setDuongdan(map.getOrDefault("Đường Dẫn", ""));
                sanPhamBUS.themSanPham(sp);

                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "Đã thêm sản phẩm từ QR");
                    loadSPCards("");
                });
            } catch (NumberFormatException ex) {
                SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(this, "Dữ liệu QR không hợp lệ!")
                );
            }
        }).start();
    }



    private void initDanhMucTab() {
        JPanel pDM = new JPanel(new BorderLayout(5,5));

        // Search
        JPanel topDM = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topDM.add(new JLabel("Tìm kiếm:"));
        JButton btnTimDM = new JButton("Tìm");
        topDM.add(txtTimDM);
        topDM.add(btnTimDM);
        pDM.add(topDM, BorderLayout.NORTH);

        // Table
        modelDanhMuc = new DefaultTableModel(new String[]{"Mã DM","Tên danh mục"}, 0);
        tableDanhMuc = new JTable(modelDanhMuc);
        pDM.add(new JScrollPane(tableDanhMuc), BorderLayout.CENTER);

        // Buttons
        JPanel botDM = new JPanel();
        JButton btnThemDM = new JButton("Thêm");
        JButton btnSuaDM  = new JButton("Sửa");
        JButton btnXoaDM  = new JButton("Xóa");
        JButton btnReDM   = new JButton("Làm mới");
        botDM.add(btnThemDM);
        botDM.add(btnSuaDM);
        botDM.add(btnXoaDM);
        botDM.add(btnReDM);
        pDM.add(botDM, BorderLayout.SOUTH);

        // Events
        btnTimDM.addActionListener(e -> loadDanhMuc(txtTimDM.getText()));
        btnReDM.addActionListener(e -> { txtTimDM.setText(""); loadDanhMuc(""); });
        btnThemDM.addActionListener(e -> showThemDanhMucDialog());
        btnSuaDM.addActionListener(e -> {
            int r = tableDanhMuc.getSelectedRow();
            if (r < 0) return;
            showSuaDanhMucDialog((int)modelDanhMuc.getValueAt(r,0));
        });
        btnXoaDM.addActionListener(e -> {
            int r = tableDanhMuc.getSelectedRow();
            if (r<0) return;
            if (JOptionPane.showConfirmDialog(this,"Xóa?","",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
                danhMucBUS.xoaDanhMuc((int)modelDanhMuc.getValueAt(r,0));
                loadDanhMuc("");
            }
        });

        tabbedPane.addTab("Danh Mục", pDM);
        loadDanhMuc("");
    }

    // === Danh Mục Helpers ===
    private void loadDanhMuc(String kw) {
        modelDanhMuc.setRowCount(0);
        for (DanhMuc dm : danhMucBUS.timKiemDanhMuc(kw)) {
            modelDanhMuc.addRow(new Object[]{dm.getMadanhmuc(), dm.getTendanhmuc()});
        }
    }
    private void showThemDanhMucDialog() {
        String ten = JOptionPane.showInputDialog(this,"Tên danh mục:");
        if (ten!=null && !ten.isBlank()) {
            DanhMuc dm = new DanhMuc(); dm.setTendanhmuc(ten);
            danhMucBUS.themDanhMuc(dm);
            loadDanhMuc("");
        }
    }
    private void showSuaDanhMucDialog(int id) {
        DanhMuc dm = danhMucBUS.layDanhMucTheoMa(id);
        String ten = JOptionPane.showInputDialog(this,"Tên mới:", dm.getTendanhmuc());
        if (ten!=null && !ten.isBlank()) {
            dm.setTendanhmuc(ten);
            danhMucBUS.suaDanhMuc(dm);
            loadDanhMuc("");
        }
    }

    private void showThemSanPhamDialog() {
        JDialog d = new JDialog((Frame) null, "Thêm Sản Phẩm", true);
        d.setLayout(new BorderLayout(10, 10));
        d.setSize(600, 300);

        // Panel form trái
        JPanel form = new JPanel(new GridLayout(5, 2, 5, 5)); // Updated to 5 rows
        JTextField txtTen = new JTextField();
        JTextField txtGian = new JTextField();
        JTextField txtGiaban = new JTextField();
        JTextField txtSoLuong = new JTextField(); // New field for "Số lượng"
        JComboBox<String> cbDM = new JComboBox<>();
        Map<String, Integer> mapDM = new HashMap<>();
        for (DanhMuc dm : danhMucBUS.layDanhSachDanhMuc()) {
            cbDM.addItem(dm.getTendanhmuc());
            mapDM.put(dm.getTendanhmuc(), dm.getMadanhmuc());
        }
        form.add(new JLabel("Tên sản phẩm:"));
        form.add(txtTen);
        form.add(new JLabel("Giá Nhập:"));
        form.add(txtGian);
        form.add(new JLabel("Giá Bán:"));
        form.add(txtGiaban);
        form.add(new JLabel("Số lượng:")); // Label for "Số lượng"
        form.add(txtSoLuong); // Input for "Số lượng"
        form.add(new JLabel("Mã Danh Mục:"));
        form.add(cbDM);

        // Panel hình ảnh phải
        JPanel picPanel = new JPanel(new BorderLayout());
        picPanel.setBorder(BorderFactory.createTitledBorder("Hình Ảnh SP"));
        JLabel lblPic = new JLabel("Chưa có ảnh", JLabel.CENTER);
        JButton btnChon = new JButton("Chọn Ảnh...");
        picPanel.add(lblPic, BorderLayout.CENTER);
        picPanel.add(btnChon, BorderLayout.SOUTH);

        final String[] selectedPath = {null};
        btnChon.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(d) == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                selectedPath[0] = f.getAbsolutePath();
                ImageIcon icon = new ImageIcon(
                    new ImageIcon(selectedPath[0]).getImage()
                        .getScaledInstance(200, 200, Image.SCALE_SMOOTH)
                );
                lblPic.setText("");
                lblPic.setIcon(icon);
            }
        });

        // Nút Lưu
        JPanel bottom = new JPanel();
        JButton btnLuu = new JButton("Lưu");
        bottom.add(btnLuu);
        btnLuu.addActionListener(e -> {
            try {
                SanPham sp = new SanPham();
                sp.setTenSanPham(txtTen.getText());
                sp.setGiaNhap(Double.parseDouble(txtGian.getText()));
                sp.setGiaBan(Double.parseDouble(txtGiaban.getText()));
                sp.setSoLuong(Integer.parseInt(txtSoLuong.getText())); // Set "Số lượng"
                sp.setMaDanhMuc(mapDM.get(cbDM.getSelectedItem()));
                sp.setDuongdan(selectedPath[0]);
                if (sanPhamBUS.themSanPham(sp)) {
                    JOptionPane.showMessageDialog(d, "Thêm thành công!");
                    loadSPCards(txtTimSP.getText());
                    d.dispose();
                } else {
                    JOptionPane.showMessageDialog(d, "Thêm thất bại!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(d, "Dữ liệu không hợp lệ!");
            }
        });

        d.add(form, BorderLayout.WEST);
        d.add(picPanel, BorderLayout.EAST);
        d.add(bottom, BorderLayout.SOUTH);
        d.setLocationRelativeTo(this);
        d.setVisible(true);
    }


    private void showSuaSanPhamDialog(int maSP) {
        SanPham sp = sanPhamBUS.laySanPhamTheoMa(maSP);
        if (sp == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm");
            return;
        }

        JDialog d = new JDialog((Frame)null, "Sửa Sản Phẩm", true);
        d.setLayout(new BorderLayout(10,10));
        d.setSize(600, 300);

        // Form trái
        JPanel form = new JPanel(new GridLayout(4,2,5,5));
        JTextField txtTen = new JTextField(sp.getTenSanPham());
        JTextField txtGian = new JTextField(String.valueOf(sp.getGiaNhap()));
        JTextField txtGiaban = new JTextField(String.valueOf(sp.getGiaBan()));
        JComboBox<String> cbDM = new JComboBox<>();
        Map<String,Integer> mapDM = new HashMap<>();
        for (DanhMuc dm : danhMucBUS.layDanhSachDanhMuc()) {
			cbDM.addItem(dm.getTendanhmuc());
			mapDM.put(dm.getTendanhmuc(), dm.getMadanhmuc());
		}
        form.add(new JLabel("Tên sản phẩm:")); form.add(txtTen);
		form.add(new JLabel("Giá Nhập:"));      form.add(txtGian);
		form.add(new JLabel("Giá Bán:"));       form.add(txtGiaban);
		form.add(new JLabel("Mã Danh Mục:"));   form.add(cbDM);

		// Hình ảnh phải
		JPanel picPanel = new JPanel(new BorderLayout());
		picPanel.setBorder(BorderFactory.createTitledBorder("Hình Ảnh SP"));
		JLabel lblPic = new JLabel("Chưa có ảnh", JLabel.CENTER);
		JButton btnChon = new JButton("Chọn Ảnh...");
		picPanel.add(lblPic, BorderLayout.CENTER);
		picPanel.add(btnChon, BorderLayout.SOUTH);

		final String[] selectedPath = {sp.getDuongdan()};
		try {
			Image i = ImageIO.read(new File(sp.getDuongdan()))
					   .getScaledInstance(200,200,Image.SCALE_SMOOTH);
			lblPic.setIcon(new ImageIcon(i));
		} catch (Exception e) {
			lblPic.setText("No Image");
			lblPic.setHorizontalAlignment(JLabel.CENTER);
		}
		
		btnChon.addActionListener(e -> {
			JFileChooser chooser = new JFileChooser();
			if (chooser.showOpenDialog(d)==JFileChooser.APPROVE_OPTION) {
				File f = chooser.getSelectedFile();
				selectedPath[0] = f.getAbsolutePath();
				ImageIcon icon = new ImageIcon(
					new ImageIcon(selectedPath[0]).getImage()
						.getScaledInstance(200,200,Image.SCALE_SMOOTH)
				);
				lblPic.setText("");
				lblPic.setIcon(icon);
			}
		});

		// Nút Lưu
		JPanel bottom = new JPanel();
		JButton btnLuu = new JButton("Lưu"); bottom.add(btnLuu);
		btnLuu.addActionListener(e -> {
			try {
				sp.setTenSanPham(txtTen.getText());
				sp.setGiaNhap(Double.parseDouble(txtGian.getText()));
				sp.setGiaBan(Double.parseDouble(txtGiaban.getText()));
				sp.setMaDanhMuc(mapDM.get(cbDM.getSelectedItem()));
				sp.setDuongdan(selectedPath[0]);
				if (sanPhamBUS.suaSanPham(sp)) {
					JOptionPane.showMessageDialog(d, "Sửa thành công!");
					loadSPCards(txtTimSP.getText());
					d.dispose();
					} else {
						JOptionPane.showMessageDialog(d, "Sửa thất bại!");
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(d, "Dữ liệu không hợp lệ!");
					
				}
			}
		);
		d.add(form, BorderLayout.WEST);
		d.add(picPanel, BorderLayout.EAST);
		d.add(bottom, BorderLayout.SOUTH);
		d.setLocationRelativeTo(this);
		d.setVisible(true);
		}
    }
    
    		
