package com.dungblue.bus;

import com.dungblue.dao.ChucVuDAO;
import com.dungblue.entity.ChucVu;
import java.util.List;

public class ChucVuBUS {
    private ChucVuDAO chucVuDAO = new ChucVuDAO();

    public List<ChucVu> layDanhSachChucVu() {
        return chucVuDAO.layDanhSachChucVu();
    }

    // Thêm logic nghiệp vụ (validate tên chức vụ...)
	public boolean themChucVu(ChucVu chucVu) {
		// Kiểm tra tên chức vụ đã tồn tại chưa
		for (ChucVu cv : chucVuDAO.layDanhSachChucVu()) {
			if (cv.getTenChucVu().equalsIgnoreCase(chucVu.getTenChucVu())) {
				return false; // Tên chức vụ đã tồn tại
			}
		}
		return chucVuDAO.themChucVu(chucVu);
	}

	public boolean suaChucVu(ChucVu chucVu) {
		return chucVuDAO.suaChucVu(chucVu);
	}

	public boolean xoaChucVu(int maChucVu) {
		return chucVuDAO.xoaChucVu(maChucVu);
	}
	public List<ChucVu> timKiemChucVu(String tuKhoa) {
		return chucVuDAO.timKiemChucVu(tuKhoa);
	}
}