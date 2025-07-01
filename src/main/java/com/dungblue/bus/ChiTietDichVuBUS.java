package com.dungblue.bus;

import com.dungblue.dao.ChiTietDichVuDAO;
import com.dungblue.entity.ChiTietDichVu;
import java.util.List;

public class ChiTietDichVuBUS {
    private ChiTietDichVuDAO dao = new ChiTietDichVuDAO();
    
    public List<ChiTietDichVu> layChiTietTheoDonHang(int madonhang) {
        return dao.layChiTietTheoDonHang(madonhang);
    }
    public int themChiTietDichVu(ChiTietDichVu ct) {
		return dao.themChiTietDichVu(ct);
	}
}