package com.dungblue.bus;

import com.dungblue.dao.ChiTietSanPhamDAO;
import com.dungblue.entity.ChiTietSanPham;
import java.util.List;

public class ChiTietSanPhamBUS {
    private ChiTietSanPhamDAO dao = new ChiTietSanPhamDAO();
    
    public List<ChiTietSanPham> layChiTietTheoDonHang(int madonhang) {
        return dao.layChiTietTheoDonHang(madonhang);
    }
    public int themChiTietSanPham(ChiTietSanPham ct) {
		return dao.themChiTietSanPham(ct);
	}
}