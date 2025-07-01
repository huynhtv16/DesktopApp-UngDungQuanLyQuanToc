package com.dungblue.ui;

import com.dungblue.bus.NguoiDungBUS;
import com.dungblue.bus.ChucVuBUS;
import com.dungblue.entity.NguoiDung;
import com.dungblue.entity.ChucVu;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class QuanLyNhanVienUI extends JPanel {
    private JTabbedPane tabbedPane = new JTabbedPane();
    private DefaultTableModel modelNhanVien, modelChucVu;
    private JTable tblNhanVien, tblChucVu;
    private NguoiDungBUS nguoiDungBUS = new NguoiDungBUS();
    private ChucVuBUS chucVuBUS = new ChucVuBUS();

    public QuanLyNhanVienUI() {
        setLayout(new BorderLayout());
        
        initUI();
        loadDataNhanVien();
        loadDataChucVu();
    }

    private void initUI() {
        // Tab Nhân viên
        JPanel panelNhanVien = new JPanel(new BorderLayout());
        modelNhanVien = new DefaultTableModel(new String[]{"Mã NV", "Tài khoản", "Họ tên", "SĐT", "Chức vụ"}, 0);
        tblNhanVien = new JTable(modelNhanVien);
        panelNhanVien.add(new JScrollPane(tblNhanVien), BorderLayout.CENTER);

        // Thanh tìm kiếm nhân viên
        JPanel searchPanelNhanVien = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField txtTimKiemNV = new JTextField(15); // Giảm độ rộng
        JButton btnTimKiemNV = new JButton("Tìm kiếm");
        JButton btnXoaTimKiemNV = new JButton("Xóa tìm");
        searchPanelNhanVien.add(new JLabel("Tìm:"));
        searchPanelNhanVien.add(txtTimKiemNV);
        searchPanelNhanVien.add(btnTimKiemNV);
        searchPanelNhanVien.add(btnXoaTimKiemNV);
        panelNhanVien.add(searchPanelNhanVien, BorderLayout.NORTH);

        // Nút thao tác nhân viên
        JPanel btnPanelNhanVien = new JPanel();
        JButton btnThemNV = new JButton("Thêm");
        JButton btnSuaNV = new JButton("Sửa");
        JButton btnXoaNV = new JButton("Xóa");
        btnPanelNhanVien.add(btnThemNV);
        btnPanelNhanVien.add(btnSuaNV);
        btnPanelNhanVien.add(btnXoaNV);
        panelNhanVien.add(btnPanelNhanVien, BorderLayout.SOUTH);

        // Tab Chức vụ
        JPanel panelChucVu = new JPanel(new BorderLayout());
        modelChucVu = new DefaultTableModel(new String[]{"Mã CV", "Tên chức vụ"}, 0);
        tblChucVu = new JTable(modelChucVu);
        panelChucVu.add(new JScrollPane(tblChucVu), BorderLayout.CENTER);

        // Thanh tìm kiếm chức vụ
        JPanel searchPanelChucVu = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField txtTimKiemCV = new JTextField(15); // Giảm độ rộng
        JButton btnTimKiemCV = new JButton("Tìm kiếm");
        JButton btnXoaTimKiemCV = new JButton("Xóa tìm");
        searchPanelChucVu.add(new JLabel("Tìm:"));
        searchPanelChucVu.add(txtTimKiemCV);
        searchPanelChucVu.add(btnTimKiemCV);
        searchPanelChucVu.add(btnXoaTimKiemCV);
        panelChucVu.add(searchPanelChucVu, BorderLayout.NORTH);

        // Nút thao tác chức vụ
        JPanel btnPanelChucVu = new JPanel();
        JButton btnThemCV = new JButton("Add");
        JButton btnSuaCV = new JButton("Update");
        JButton btnXoaCV = new JButton("Delete");
        btnPanelChucVu.add(btnThemCV);
        btnPanelChucVu.add(btnSuaCV);
        btnPanelChucVu.add(btnXoaCV);
        panelChucVu.add(btnPanelChucVu, BorderLayout.SOUTH);

        // Thêm các tab vào JTabbedPane
        tabbedPane.addTab("Nhân viên", panelNhanVien);
        tabbedPane.addTab("Chức vụ", panelChucVu);
        add(tabbedPane, BorderLayout.CENTER);

        // Sự kiện tìm kiếm
        btnTimKiemNV.addActionListener(e -> timKiemNhanVien(txtTimKiemNV.getText()));
        btnXoaTimKiemNV.addActionListener(e -> {
			txtTimKiemNV.setText("");
			loadDataNhanVien(); // Hiển thị lại toàn bộ danh sách
		});
        btnTimKiemCV.addActionListener(e -> timKiemChucVu(txtTimKiemCV.getText()));
        btnXoaTimKiemCV.addActionListener(e -> {
   			txtTimKiemCV.setText("");
   			loadDataChucVu(); // Hiển thị lại toàn bộ danh sách
   		});

        // Sự kiện nút thêm/sửa/xóa nhân viên
        btnThemNV.addActionListener(e -> showThemNhanVienDialog());
        btnSuaNV.addActionListener(e -> showSuaNhanVienDialog());
        btnXoaNV.addActionListener(e -> xoaNhanVien());

        // Sự kiện nút thêm/sửa/xóa chức vụ
        btnThemCV.addActionListener(e -> showThemChucVuDialog());
        btnSuaCV.addActionListener(e -> showSuaChucVuDialog());
        btnXoaCV.addActionListener(e -> xoaChucVu());
    }


    private void loadDataNhanVien() {
        modelNhanVien.setRowCount(0);
        for (NguoiDung nd : nguoiDungBUS.layDanhSachNguoiDung()) {
            modelNhanVien.addRow(new Object[]{
                nd.getMaNguoiDung(),
                nd.getTaiKhoan(),
                nd.getHoTen(),
                nd.getSdt(),
                nd.getMaChucVu()
            });
        }
    }

    private void loadDataChucVu() {
        modelChucVu.setRowCount(0);
        for (ChucVu cv : chucVuBUS.layDanhSachChucVu()) {
            modelChucVu.addRow(new Object[]{
                cv.getMaChucVu(),
                cv.getTenChucVu()
            });
        }
    }

    private void showThemNhanVienDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm nhân viên", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtTaiKhoan = new JTextField();
        JTextField txtMatKhau = new JTextField();
        JTextField txtHoTen = new JTextField();
        JTextField txtSdt = new JTextField();
        JComboBox<String> cbChucVu = new JComboBox<>();
        for (ChucVu cv : chucVuBUS.layDanhSachChucVu()) {
            cbChucVu.addItem(cv.getTenChucVu());
        }

        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Tài khoản:"), gbc);
        gbc.gridx = 1;
        dialog.add(txtTaiKhoan, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Mật khẩu:"), gbc);
        gbc.gridx = 1;
        dialog.add(txtMatKhau, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("Họ tên:"), gbc);
        gbc.gridx = 1;
        dialog.add(txtHoTen, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        dialog.add(new JLabel("SĐT:"), gbc);
        gbc.gridx = 1;
        dialog.add(txtSdt, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        dialog.add(new JLabel("Chức vụ:"), gbc);
        gbc.gridx = 1;
        dialog.add(cbChucVu, gbc);

        JButton btnLuu = new JButton("Lưu");
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        dialog.add(btnLuu, gbc);

        btnLuu.addActionListener(e -> {
            NguoiDung nd = new NguoiDung();
            nd.setTaiKhoan(txtTaiKhoan.getText());
            nd.setMatKhau(txtMatKhau.getText());
            nd.setHoTen(txtHoTen.getText());
            nd.setSdt(txtSdt.getText());
            nd.setMaChucVu(cbChucVu.getSelectedIndex() + 1);

            if (nguoiDungBUS.themNguoiDung(nd)) {
                JOptionPane.showMessageDialog(dialog, "Thêm nhân viên thành công!");
                loadDataNhanVien();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Thêm nhân viên thất bại!");
            }
        });

        dialog.setVisible(true);
    }


    private void showThemChucVuDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm chức vụ", true);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtTenChucVu = new JTextField();

        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Tên chức vụ:"), gbc);
        gbc.gridx = 1;
        dialog.add(txtTenChucVu, gbc);

        JButton btnLuu = new JButton("Lưu");
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        dialog.add(btnLuu, gbc);

        btnLuu.addActionListener(e -> {
            ChucVu cv = new ChucVu();
            cv.setTenChucVu(txtTenChucVu.getText());

            if (chucVuBUS.themChucVu(cv)) {
                JOptionPane.showMessageDialog(dialog, "Thêm chức vụ thành công!");
                loadDataChucVu();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Thêm chức vụ thất bại!");
            }
        });

        dialog.setVisible(true);
    }


    private void showSuaNhanVienDialog() {
        int selectedRow = tblNhanVien.getSelectedRow();
        if (selectedRow != -1) {
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Sửa nhân viên", true);
            dialog.setSize(400, 300);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

			NguoiDung nd = nguoiDungBUS.layDanhSachNguoiDung().get(selectedRow);			
            JTextField txtTaiKhoan = new JTextField(nd.getTaiKhoan());
            JTextField txtMatKhau = new JTextField(nd.getMatKhau());
            JTextField txtHoTen = new JTextField(nd.getHoTen());
            JTextField txtSdt = new JTextField(nd.getSdt());
            JComboBox<String> cbChucVu = new JComboBox<>();
            for (ChucVu cv : chucVuBUS.layDanhSachChucVu()) {
                cbChucVu.addItem(cv.getTenChucVu());
            }
            cbChucVu.setSelectedIndex(nd.getMaChucVu() - 1);

            gbc.gridx = 0; gbc.gridy = 0;
            dialog.add(new JLabel("Tài khoản:"), gbc);
            gbc.gridx = 1;
            dialog.add(txtTaiKhoan, gbc);

            gbc.gridx = 0; gbc.gridy = 1;
            dialog.add(new JLabel("Mật khẩu:"), gbc);
            gbc.gridx = 1;
            dialog.add(txtMatKhau, gbc);

            gbc.gridx = 0; gbc.gridy = 2;
            dialog.add(new JLabel("Họ tên:"), gbc);
            gbc.gridx = 1;
            dialog.add(txtHoTen, gbc);

            gbc.gridx = 0; gbc.gridy = 3;
            dialog.add(new JLabel("SĐT:"), gbc);
            gbc.gridx = 1;
            dialog.add(txtSdt, gbc);

            gbc.gridx = 0; gbc.gridy = 4;
            dialog.add(new JLabel("Chức vụ:"), gbc);
            gbc.gridx = 1;
            dialog.add(cbChucVu, gbc);

            JButton btnLuu = new JButton("Lưu");
            gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
            dialog.add(btnLuu, gbc);

            btnLuu.addActionListener(e -> {
                nd.setTaiKhoan(txtTaiKhoan.getText());
                nd.setMatKhau(txtMatKhau.getText());
                nd.setHoTen(txtHoTen.getText());
                nd.setSdt(txtSdt.getText());
                nd.setMaChucVu(cbChucVu.getSelectedIndex() + 1);

                if (nguoiDungBUS.suaNguoiDung(nd)) {
                    JOptionPane.showMessageDialog(dialog, "Sửa nhân viên thành công!");
                    loadDataNhanVien();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Sửa nhân viên thất bại!");
                }
            });

            dialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên để sửa!");
        }
    }
    // Sửa chức vụ
    private void showSuaChucVuDialog() {
		int selectedRow = tblChucVu.getSelectedRow();
		if (selectedRow != -1) {
			JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Sửa chức vụ", true);
			dialog.setSize(300, 200);
			dialog.setLocationRelativeTo(this);
			dialog.setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(5, 5, 5, 5);
			gbc.fill = GridBagConstraints.HORIZONTAL;

			ChucVu cv = chucVuBUS.layDanhSachChucVu().get(selectedRow);

			JTextField txtTenChucVu = new JTextField(cv.getTenChucVu());

			gbc.gridx = 0; gbc.gridy = 0;
			dialog.add(new JLabel("Tên chức vụ:"), gbc);
			gbc.gridx = 1;
			dialog.add(txtTenChucVu, gbc);

			JButton btnLuu = new JButton("Lưu");
			gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
			dialog.add(btnLuu, gbc);

			btnLuu.addActionListener(e -> {
				cv.setTenChucVu(txtTenChucVu.getText());

				if (chucVuBUS.suaChucVu(cv)) {
					JOptionPane.showMessageDialog(dialog, "Sửa chức vụ thành công!");
					loadDataChucVu();
					dialog.dispose();
				} else {
					JOptionPane.showMessageDialog(dialog, "Sửa chức vụ thất bại!");
				}
			});

			dialog.setVisible(true);
		} else {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn chức vụ để sửa!");
		}
	}


	
	private void xoaNhanVien() {
		int selectedRow = tblNhanVien.getSelectedRow();
		if (selectedRow != -1) {
			int maNV = (int) modelNhanVien.getValueAt(selectedRow, 0);
			if (nguoiDungBUS.xoaNguoiDung(maNV)) {
				JOptionPane.showMessageDialog(this, "Xóa nhân viên thành công!");
				loadDataNhanVien();
			} else {
				JOptionPane.showMessageDialog(this, "Xóa nhân viên thất bại!");
			}
		} else {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên để xóa!");
		}
	}
	private void xoaChucVu() {
		int selectedRow = tblChucVu.getSelectedRow();
		if (selectedRow != -1) {
			int maCV = (int) modelChucVu.getValueAt(selectedRow, 0);
			if (chucVuBUS.xoaChucVu(maCV)) {
				JOptionPane.showMessageDialog(this, "Xóa chức vụ thành công!");
				loadDataChucVu();
			} else {
				JOptionPane.showMessageDialog(this, "Xóa chức vụ thất bại!");
			}
		} else {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn chức vụ để xóa!");
		}
		}

	private void timKiemNhanVien(String tuKhoa) {
		modelNhanVien.setRowCount(0);
		for (NguoiDung nd : nguoiDungBUS.timKiemNguoiDung(tuKhoa)) {
			modelNhanVien.addRow(new Object[]{
				nd.getMaNguoiDung(),
				nd.getTaiKhoan(),
				nd.getHoTen(),
				nd.getSdt(),
				nd.getMaChucVu()
			});
		}
	}

	private void timKiemChucVu(String tuKhoa) {
		modelChucVu.setRowCount(0);
		for (ChucVu cv : chucVuBUS.timKiemChucVu(tuKhoa)) {
			modelChucVu.addRow(new Object[]{
				cv.getMaChucVu(),
				cv.getTenChucVu()
			});
		}
	}

}