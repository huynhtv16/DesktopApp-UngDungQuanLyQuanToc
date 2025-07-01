// BaoCaoBUS.java
package com.dungblue.bus;

import com.dungblue.dao.BaoCaoDAO;
import com.dungblue.entity.BaoCaoThongKe;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BaoCaoBUS {
    private final BaoCaoDAO baoCaoDAO = new BaoCaoDAO();
    
    public List<BaoCaoThongKe> thongKeTheoNgay(Date ngay) {
        try {
            return baoCaoDAO.thongKeTheoNgay(ngay);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    public List<BaoCaoThongKe> thongKeTheoThang(Date ngay) {
        try {
            return baoCaoDAO.thongKeTheoThang(ngay);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }
}