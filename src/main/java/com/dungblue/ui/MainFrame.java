package com.dungblue.ui;

import javax.swing.*;
import com.dungblue.entity.NguoiDung;
import com.formdev.flatlaf.FlatLightLaf;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {
    private final Color SELECTED_COLOR = new Color(255, 140, 0);
    private final Color HOVER_COLOR = new Color(245, 245, 245);
    private final Color DEFAULT_COLOR = Color.BLACK;
    private final Color MENU_BG_COLOR = new Color(250, 250, 250);
    private NguoiDung nguoiDung;

    private List<JButton> menuButtons = new ArrayList<>();
    private JButton btnNhanVien, btnSaoLuu;
    private JButton currentSelectedButton;

    public MainFrame(NguoiDung nguoiDung) {
        this.nguoiDung = nguoiDung;
        setTitle("Quản lý salon - " + nguoiDung.getHoTen());
        setSize(1400, 800); // Tăng chiều rộng
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        initUI();
    }

    private void initUI() {
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
        headerPanel.setPreferredSize(new Dimension(100, 50));

        // Bản quyền bên phải
        JLabel lblCopyright = new JLabel("© 2025 - Developed by Huỳnh TV");
        lblCopyright.setFont(new Font("Segoe UI", Font.BOLD, 12)); // In đậm
        lblCopyright.setForeground(new Color(100, 100, 100));
        lblCopyright.setHorizontalAlignment(SwingConstants.RIGHT);
        lblCopyright.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        headerPanel.add(lblCopyright, BorderLayout.EAST);

        // Sửa đường dẫn icon: thêm package path
        Icon iconHome = resizeIcon("/com/dungblue/ui/icons/home.png", 30, 30);
        Icon iconKhachHang = resizeIcon("/com/dungblue/ui/icons/customer.png", 30, 30);
        Icon iconSanPham = resizeIcon("/com/dungblue/ui/icons/products.png", 30, 30);
        Icon iconDichVu = resizeIcon("/com/dungblue/ui/icons/service.png", 30, 30);
        Icon iconDonHang = resizeIcon("/com/dungblue/ui/icons/order.png", 30, 30);
        Icon iconNhanVien = resizeIcon("/com/dungblue/ui/icons/employee.png", 30, 30);
        Icon iconStats = resizeIcon("/com/dungblue/ui/icons/statistics.png", 30, 30);
        Icon iconLogout = resizeIcon("/com/dungblue/ui/icons/logout.png", 30, 30);
        Icon iconBackup = resizeIcon("/com/dungblue/ui/icons/backup.png", 30, 30);

        JPanel leftMenu = new JPanel();
        leftMenu.setLayout(new BoxLayout(leftMenu, BoxLayout.Y_AXIS));
        leftMenu.setPreferredSize(new Dimension(240, 800)); // Thu gọn menu
        leftMenu.setBackground(MENU_BG_COLOR);
        leftMenu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Dimension buttonSize = new Dimension(220, 60);

        JButton btnTrangChu = createMenuButton("Trang chủ", iconHome, buttonSize);
        JButton btnKhachHang = createMenuButton("Quản lý khách hàng", iconKhachHang, buttonSize);
        JButton btnSanPham = createMenuButton("Quản lý sản phẩm", iconSanPham, buttonSize);
        JButton btnDichVu = createMenuButton("Quản lý dịch vụ", iconDichVu, buttonSize);
        JButton btnDonHang = createMenuButton("Quản lý đơn hàng", iconDonHang, buttonSize);
        btnNhanVien = createMenuButton("Quản lý nhân viên", iconNhanVien, buttonSize);
        JButton btnThongKe = createMenuButton("Báo cáo tổng quan", iconStats, buttonSize);
        btnSaoLuu = createMenuButton("Sao lưu & Phục hồi", iconBackup, buttonSize);
        JButton btnDangXuat = createMenuButton("Thoát tài khoản", iconLogout, buttonSize);

        // Thêm nút vào menu
        leftMenu.add(Box.createVerticalStrut(10));
        leftMenu.add(btnTrangChu);
        leftMenu.add(Box.createVerticalStrut(8));
        leftMenu.add(btnKhachHang);
        leftMenu.add(Box.createVerticalStrut(8));
        leftMenu.add(btnSanPham);
        leftMenu.add(Box.createVerticalStrut(8));
        leftMenu.add(btnDichVu);
        leftMenu.add(Box.createVerticalStrut(8));
        leftMenu.add(btnDonHang);
        leftMenu.add(Box.createVerticalStrut(8));
        leftMenu.add(btnNhanVien);
        leftMenu.add(Box.createVerticalStrut(8));
        leftMenu.add(btnThongKe);
        leftMenu.add(Box.createVerticalStrut(8));
        leftMenu.add(btnSaoLuu);
        leftMenu.add(Box.createVerticalGlue());
        leftMenu.add(btnDangXuat);
        leftMenu.add(Box.createVerticalStrut(20));

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25)); // Tăng padding
        contentPanel.setBackground(Color.WHITE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(leftMenu, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);

        // Giao diện mặc định
        contentPanel.add(new TrangChuUI(), BorderLayout.CENTER);
        highlightSelected(btnTrangChu);

        // Xử lý sự kiện
        btnTrangChu.addActionListener(e -> setContentPanel(contentPanel, new TrangChuUI(), btnTrangChu));
        btnKhachHang.addActionListener(e -> setContentPanel(contentPanel, new QuanLyKhachHangUI(), btnKhachHang));
        btnSanPham.addActionListener(e -> setContentPanel(contentPanel, new QuanLySanPhamUI(), btnSanPham));
        btnDichVu.addActionListener(e -> setContentPanel(contentPanel, new QuanLyDichVuUI(), btnDichVu));
        btnDonHang.addActionListener(e -> setContentPanel(contentPanel, new QuanLyDonHangUI(), btnDonHang));
        btnNhanVien.addActionListener(e -> setContentPanel(contentPanel, new QuanLyNhanVienUI(), btnNhanVien));
        btnThongKe.addActionListener(e -> setContentPanel(contentPanel, new BaoCaoTongQuanUI(), btnThongKe));
        btnSaoLuu.addActionListener(e -> {
            // Tùy chỉnh sau nếu muốn mở SaoLuuUI
        });
        btnDangXuat.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc muốn đăng xuất không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose();
                new DangNhapUI().setVisible(true);
            }
        });
    }

    private JButton createMenuButton(String text, Icon icon, Dimension size) {
        JButton button = new JButton(text, icon);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setIconTextGap(15);
        button.setPreferredSize(size);
        button.setMaximumSize(size);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        button.setForeground(DEFAULT_COLOR);
        button.setBackground(MENU_BG_COLOR);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 5));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (button != currentSelectedButton) {
                    button.setBackground(HOVER_COLOR);
                }
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (button != currentSelectedButton) {
                    button.setBackground(MENU_BG_COLOR);
                }
            }
        });

        menuButtons.add(button);
        return button;
    }

    private void setContentPanel(JPanel contentPanel, JPanel panelToShow, JButton selectedButton) {
        contentPanel.removeAll();
        contentPanel.add(panelToShow, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
        highlightSelected(selectedButton);
    }

    private void highlightSelected(JButton selectedButton) {
        for (JButton btn : menuButtons) {
            if (btn == selectedButton) {
                btn.setForeground(SELECTED_COLOR);
                btn.setBackground(new Color(255, 240, 220));
                btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
            } else {
                btn.setForeground(DEFAULT_COLOR);
                btn.setBackground(MENU_BG_COLOR);
                btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            }
        }
        currentSelectedButton = selectedButton;
    }

    private Icon resizeIcon(String resourcePath, int width, int height) {
        try {
            // Sử dụng getResourceAsStream với đường dẫn đầy đủ
            java.io.InputStream input = getClass().getResourceAsStream(resourcePath);
            if (input == null) {
                System.err.println("Không tìm thấy icon: " + resourcePath);
                return null;
            }
            
            // Đọc dữ liệu từ InputStream
            byte[] bytes = input.readAllBytes();
            input.close();
            
            // Tạo ImageIcon từ byte array
            ImageIcon icon = new ImageIcon(bytes);
            Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
