// bus/SaoLuuBUS.java
package com.dungblue.bus;

import com.dungblue.dao.SaoLuuDAO;
import com.dungblue.entity.SaoLuu;
import java.util.List;

public class SaoLuuBUS {
    private SaoLuuDAO saoLuuDAO = new SaoLuuDAO();
    
    public boolean saoLuuDuLieu(String duongDan) {
        return saoLuuDAO.saoLuuDuLieu(duongDan);
    }
    
    public boolean khoiPhucDuLieu(String duongDan) {
        return saoLuuDAO.khoiPhucDuLieu(duongDan);
    }
    
    public List<SaoLuu> layDanhSachSaoLuu() {
        return saoLuuDAO.layDanhSachSaoLuu();
    }
}