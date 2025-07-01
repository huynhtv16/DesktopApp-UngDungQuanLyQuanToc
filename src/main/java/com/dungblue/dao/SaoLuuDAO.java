// dao/SaoLuuDAO.java
package com.dungblue.dao;

import com.dungblue.entity.SaoLuu;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SaoLuuDAO {
    public boolean saoLuuDuLieu(String duongDan) {
        try {
            // Tạo file backup
            File backupFile = new File(duongDan);
            backupFile.createNewFile();
            
            // Copy database file (đơn giản hóa, thực tế cần dùng mysqldump)
            File dbFile = new File("database.db");
            Files.copy(dbFile.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean khoiPhucDuLieu(String duongDan) {
        try {
            // Khôi phục database từ file
            File backupFile = new File(duongDan);
            File dbFile = new File("database.db");
            Files.copy(backupFile.toPath(), dbFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public List<SaoLuu> layDanhSachSaoLuu() {
        List<SaoLuu> danhSach = new ArrayList<>();
        // Giả lập dữ liệu
        danhSach.add(new SaoLuu("backup_20231001.db", new Date(), "15 MB"));
        danhSach.add(new SaoLuu("backup_20231015.db", new Date(), "18 MB"));
        return danhSach;
    }
}