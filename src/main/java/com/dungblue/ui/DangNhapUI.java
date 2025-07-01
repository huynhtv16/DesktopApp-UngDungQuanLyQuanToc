package com.dungblue.ui;

import com.dungblue.bus.NguoiDungBUS;
import com.dungblue.entity.NguoiDung;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class DangNhapUI extends JFrame {
    private JTextField txtTaiKhoan;
    private JPasswordField txtMatKhau;
    private JButton btnDangNhap;
    private JButton btnThoat;
    private NguoiDungBUS nguoiDungBUS = new NguoiDungBUS();
    private JLabel lblSlideshow;
    private List<ImageIcon> slideImages;
    private int currentSlideIndex = 0;
    private Timer slideTimer;
    private JCheckBox chkHienMatKhau;

    public DangNhapUI() {
        initComponents();
        loadSlideImages();
        startSlideshow();
    }

    private void initComponents() {
        setTitle("Đăng nhập hệ thống");
        setSize(1000, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel với GridLayout chia làm 2 phần
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.setBackground(new Color(240, 240, 240));

        // ========== Phần bên trái cho slideshow ==========
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        lblSlideshow = new JLabel();
        lblSlideshow.setHorizontalAlignment(SwingConstants.CENTER);
        lblSlideshow.setVerticalAlignment(SwingConstants.CENTER);
        lblSlideshow.setBackground(Color.WHITE);
        lblSlideshow.setOpaque(true);
        leftPanel.add(lblSlideshow, BorderLayout.CENTER);

        // ========== Phần bên phải cho form đăng nhập ==========
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 15, 12, 15); // Tăng padding
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Tiêu đề form - Căn giữa
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel lblTitle = new JLabel("ĐĂNG NHẬP HỆ THỐNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(0, 102, 204));
        formPanel.add(lblTitle, gbc);

        // Reset gridwidth
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        // Tài khoản
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lblTaiKhoan = new JLabel("Tài khoản:");
        lblTaiKhoan.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formPanel.add(lblTaiKhoan, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        txtTaiKhoan = new JTextField(20);
        txtTaiKhoan.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtTaiKhoan.setPreferredSize(new Dimension(300, 45)); // Tăng chiều cao
        // Viền bo tròn với hiệu ứng focus
        txtTaiKhoan.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        // hiệu ứng focus
        txtTaiKhoan.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTaiKhoan.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 102, 204), 2),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTaiKhoan.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200)),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }
        });
        formPanel.add(txtTaiKhoan, gbc);

        // Mật khẩu
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblMatKhau = new JLabel("Mật khẩu:");
        lblMatKhau.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formPanel.add(lblMatKhau, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        
        // Panel chứa mật khẩu với viền và nút hiển thị tích hợp
        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.setBackground(Color.WHITE);
        passwordPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(0, 0, 0, 5)
        ));
        passwordPanel.setPreferredSize(new Dimension(300, 45));

        txtMatKhau = new JPasswordField();
        txtMatKhau.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtMatKhau.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 10));
        passwordPanel.add(txtMatKhau, BorderLayout.CENTER);
        
        // Hiệu ứng focus cho ô mật khẩu
        txtMatKhau.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                passwordPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 102, 204), 2),
                    BorderFactory.createEmptyBorder(0, 0, 0, 5)
                ));
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                passwordPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200)),
                    BorderFactory.createEmptyBorder(0, 0, 0, 5)
                ));
            }
        });
   
        // Nút hiển thị mật khẩu - thiết kế hiện đại
        chkHienMatKhau = new JCheckBox();
        chkHienMatKhau.setFocusPainted(false);
        chkHienMatKhau.setContentAreaFilled(false);
        chkHienMatKhau.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // Sử dụng icon chất lượng cao (thay bằng đường dẫn thực tế của bạn)
        try {
            ImageIcon eyeClosedIcon = new ImageIcon(getClass().getResource("/com/dungblue/ui/hide.png"));
            ImageIcon eyeOpenIcon = new ImageIcon(getClass().getResource("/com/dungblue/ui/view.png"));
            
            // Scale icon cho vừa vặn
            Image closedImg = eyeClosedIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            Image openImg = eyeOpenIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            
            chkHienMatKhau.setIcon(new ImageIcon(closedImg));
            chkHienMatKhau.setSelectedIcon(new ImageIcon(openImg));
        } catch (Exception e) {
            // Fallback nếu không có icon
            chkHienMatKhau.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        }
        
        chkHienMatKhau.addActionListener(e -> {
            if (chkHienMatKhau.isSelected()) {
                txtMatKhau.setEchoChar((char) 0); // Hiển thị mật khẩu
            } else {
                txtMatKhau.setEchoChar('•'); // Ẩn mật khẩu
            }
            txtMatKhau.requestFocus(); // Giữ focus trong ô nhập
        });
        
        passwordPanel.add(chkHienMatKhau, BorderLayout.EAST);
        formPanel.add(passwordPanel, gbc);

        // Quên mật khẩu
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        
        JButton btnQuenMatKhau = new JButton("Quên mật khẩu?");
        btnQuenMatKhau.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnQuenMatKhau.setForeground(new Color(100, 100, 100));
        btnQuenMatKhau.setContentAreaFilled(false);
        btnQuenMatKhau.setBorderPainted(false);
        btnQuenMatKhau.setFocusPainted(false);
        btnQuenMatKhau.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hiệu ứng hover tinh tế
        btnQuenMatKhau.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnQuenMatKhau.setForeground(new Color(0, 102, 204));
                btnQuenMatKhau.setFont(new Font("Segoe UI", Font.BOLD, 14));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnQuenMatKhau.setForeground(new Color(100, 100, 100));
                btnQuenMatKhau.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            }
        });
        
        formPanel.add(btnQuenMatKhau, gbc);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(formPanel, BorderLayout.CENTER);
        
        // Button panel - thiết kế hiện đại
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        buttonPanel.setBackground(new Color(240, 240, 240));

        btnDangNhap = new JButton("Đăng nhập");
        btnDangNhap.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnDangNhap.setBackground(new Color(0, 102, 204));
        btnDangNhap.setForeground(Color.WHITE);
        btnDangNhap.setPreferredSize(new Dimension(160, 50));
        btnDangNhap.setFocusPainted(false);
        btnDangNhap.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 82, 184)),
            BorderFactory.createEmptyBorder(8, 25, 8, 25)
        ));
        // Hiệu ứng hover cho nút đăng nhập
        btnDangNhap.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnDangNhap.setBackground(new Color(0, 92, 194));
                btnDangNhap.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 72, 174)),
                    BorderFactory.createEmptyBorder(8, 25, 8, 25)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnDangNhap.setBackground(new Color(0, 102, 204));
                btnDangNhap.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 82, 184)),
                    BorderFactory.createEmptyBorder(8, 25, 8, 25)
                ));
            }
        });

        btnThoat = new JButton("Thoát");
        btnThoat.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnThoat.setBackground(new Color(240, 240, 240));
        btnThoat.setForeground(new Color(100, 100, 100));
        btnThoat.setPreferredSize(new Dimension(160, 50));
        btnThoat.setFocusPainted(false);
        btnThoat.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 25, 8, 25)
        ));
        // Hiệu ứng hover cho nút thoát
        btnThoat.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnThoat.setBackground(new Color(230, 230, 230));
                btnThoat.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(180, 180, 180)),
                    BorderFactory.createEmptyBorder(8, 25, 8, 25)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnThoat.setBackground(new Color(240, 240, 240));
                btnThoat.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200)),
                    BorderFactory.createEmptyBorder(8, 25, 8, 25)
                ));
            }
        });

        buttonPanel.add(btnDangNhap);
        buttonPanel.add(btnThoat);
        
        // Thêm bản quyền vào panel chính bên phải
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(240, 240, 240));
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        

    
        rightPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Thêm 2 panel vào main
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
        add(mainPanel);

        // Xử lý sự kiện
        btnDangNhap.addActionListener(this::dangNhap);
        btnThoat.addActionListener(e -> System.exit(0));
        txtMatKhau.addActionListener(this::dangNhap);
    }

    private void loadSlideImages() {
        slideImages = new ArrayList<>();
        try {
            slideImages.add(new ImageIcon(getClass().getResource("/com/dungblue/ui/mauNu.jpg")));
            slideImages.add(new ImageIcon(getClass().getResource("/com/dungblue/ui/mauNam1.jpg")));
            slideImages.add(new ImageIcon(getClass().getResource("/com/dungblue/ui/mauNu2.jpg")));
            slideImages.add(new ImageIcon(getClass().getResource("/com/dungblue/ui/mauNam2.jpg")));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy ảnh slideshow!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            slideImages.add(createPlaceholderImage("Slide 1", new Color(70, 130, 180)));
            slideImages.add(createPlaceholderImage("Slide 2", new Color(46, 139, 87)));
            slideImages.add(createPlaceholderImage("Slide 3", new Color(220, 20, 60)));
        }
    }

    private ImageIcon createPlaceholderImage(String text, Color bgColor) {
        BufferedImage img = new BufferedImage(400, 500, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();
        
        g2d.setColor(bgColor);
        g2d.fillRect(0, 0, 400, 500);
        
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        FontMetrics fm = g2d.getFontMetrics();
        int x = (img.getWidth() - fm.stringWidth(text)) / 2;
        int y = (img.getHeight() - fm.getHeight()) / 2 + fm.getAscent();
        g2d.drawString(text, x, y);
        
        g2d.dispose();
        return new ImageIcon(img);
    }

    private void startSlideshow() {
        if (slideImages.isEmpty()) return;

        SwingUtilities.invokeLater(() -> showCurrentSlide());

        slideTimer = new Timer(2000, e -> {
            currentSlideIndex = (currentSlideIndex + 1) % slideImages.size();
            showCurrentSlide();
        });
        slideTimer.start();
    }

    private void showCurrentSlide() {
        if (lblSlideshow.getWidth() <= 0 || lblSlideshow.getHeight() <= 0) {
            return;
        }

        ImageIcon originalIcon = slideImages.get(currentSlideIndex);
        Image scaledImage = originalIcon.getImage().getScaledInstance(
            lblSlideshow.getWidth(),
            lblSlideshow.getHeight(),
            Image.SCALE_SMOOTH
        );
        lblSlideshow.setIcon(new ImageIcon(scaledImage));
    }

    private void dangNhap(ActionEvent e) {
        String taiKhoan = txtTaiKhoan.getText().trim();
        String matKhau = new String(txtMatKhau.getPassword()).trim();

        if (taiKhoan.isEmpty() || matKhau.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        NguoiDung nguoiDung = nguoiDungBUS.dangNhap(taiKhoan, matKhau);

        if (nguoiDung != null) {
            slideTimer.stop();
            JOptionPane.showMessageDialog(this, "Đăng nhập thành công!\nXin chào: " + nguoiDung.getHoTen());
            this.dispose();
            new MainFrame(nguoiDung).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Tài khoản hoặc mật khẩu không đúng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtMatKhau.setText("");
            txtMatKhau.requestFocus();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DangNhapUI ui = new DangNhapUI();
            ui.setVisible(true);
        });
    }
}