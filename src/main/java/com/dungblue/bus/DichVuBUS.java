package com.dungblue.bus;

import com.dungblue.dao.DichVuDAO;
import com.dungblue.entity.DichVu;
import java.util.List;
import java.util.Map;

public class DichVuBUS {
    private DichVuDAO dichVuDAO = new DichVuDAO();

    public List<DichVu> layDanhSachDichVu() {
        return dichVuDAO.layDanhSachDichVu();
    }

    public boolean themDichVu(DichVu dv) {
        if (dv.getTendichvu().isEmpty() || dv.getGia() <= 0) {
            return false;
        }
        return dichVuDAO.themDichVu(dv);
    }

    public boolean suaDichVu(DichVu dv) {
        if (dv.getTendichvu().isEmpty() || dv.getGia() <= 0) {
            return false;
        }
        return dichVuDAO.suaDichVu(dv);
    }

    public boolean xoaDichVu(int madichvu) {
        return dichVuDAO.xoaDichVu(madichvu);
    }

    public List<DichVu> timKiemDichVu(String keyword) {
        return dichVuDAO.timKiemDichVu(keyword);
    }
    public DichVu layDichVuTheoMa(int madichvu) {
		return dichVuDAO.layDichVuTheoMa(madichvu);
	}
    // thongKeDichVu
    public Map<String, Integer> thongKeDichVu() {
		return dichVuDAO.thongKeDichVuDaSuDung();
	}
}