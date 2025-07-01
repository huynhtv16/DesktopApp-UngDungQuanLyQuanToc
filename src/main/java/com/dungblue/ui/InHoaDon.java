package com.dungblue.ui;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPosConst;
import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.escpos.image.*;
import com.github.anastaciocintra.output.PrinterOutputStream;
import com.dungblue.entity.*;
import com.dungblue.bus.*;

import javax.print.PrintService;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class InHoaDon {
    public static void inHoaDon(int maDH, String printerName) throws IOException {
        DonHangBUS dhBus = new DonHangBUS();
        ChiTietSanPhamBUS ctSanPhamBus = new ChiTietSanPhamBUS();
        ChiTietDichVuBUS ctDichVuBus = new ChiTietDichVuBUS();
        SanPhamBUS spBus = new SanPhamBUS();
        DichVuBUS dvBus = new DichVuBUS();

        DonHang dh = dhBus.layDonHangTheoMa(maDH);
        List<ChiTietSanPham> listSp = ctSanPhamBus.layChiTietTheoDonHang(maDH);
        List<ChiTietDichVu> listDv = ctDichVuBus.layChiTietTheoDonHang(maDH);

        // Tìm máy in theo tên
        PrintService printService = PrinterOutputStream.getPrintServiceByName(printerName);
        if (printService == null) {
            throw new IOException("Không tìm thấy máy in: " + printerName);
        }

        try (PrinterOutputStream printerOutputStream = new PrinterOutputStream(printService);
             EscPos escpos = new EscPos(printerOutputStream)) {

            // Thiết lập kiểu chữ
            Style title = new Style()
                    .setFontSize(Style.FontSize._2, Style.FontSize._2)
                    .setJustification(EscPosConst.Justification.Center);
            
            Style header = new Style()
                    .setBold(true)
                    .setJustification(EscPosConst.Justification.Center);
            
            Style normal = new Style();
            
            Style rightAlign = new Style()
                    .setJustification(EscPosConst.Justification.Right);
            
            Style leftAlign = new Style()
                    .setJustification(EscPosConst.Justification.Left_Default);

            // Header hóa đơn
            escpos.writeLF(title, "HTV COFFEE");
            escpos.writeLF(header, "HÓA ĐƠN BÁN HÀNG");
            escpos.writeLF(normal, "-------------------------------");
            
            // Thông tin đơn hàng
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            escpos.writeLF(leftAlign, "Mã ĐH: " + maDH);
            escpos.writeLF(leftAlign, "Ngày: " + sdf.format(new Date()));
            escpos.writeLF(leftAlign, "Khách hàng: " + dh.getMaKhachHang());
            escpos.writeLF(leftAlign, "Nhân viên: " + dh.getMaNguoiDung());
            escpos.writeLF(normal, "-------------------------------");
            
            // Tiêu đề cột
            escpos.write(leftAlign, "Tên");
            escpos.write(rightAlign, "SL   Đ.Giá   T.Tiền");
            escpos.writeLF(normal, "");
            escpos.writeLF(normal, "-------------------------------");

            // Chi tiết sản phẩm
            for (ChiTietSanPham ct : listSp) {
                SanPham sp = spBus.laySanPhamTheoMa(ct.getMaSanPham());
                if (sp != null) {
                    String ten = sp.getTenSanPham();
                    if (ten.length() > 16) ten = ten.substring(0, 13) + "...";
                    
                    escpos.write(leftAlign, String.format("%-16s", ten));
                    escpos.write(rightAlign, String.format("%2d %7.0f %8.0f", 
                            ct.getSoLuong(), ct.getGia(), ct.getSoLuong() * ct.getGia()));
                    escpos.feed(1);
                }
            }

            // Chi tiết dịch vụ
            for (ChiTietDichVu ct : listDv) {
                DichVu dv = dvBus.layDichVuTheoMa(ct.getMaDichVu());
                if (dv != null) {
                    String ten = dv.getTendichvu();
                    if (ten.length() > 16) ten = ten.substring(0, 13) + "...";
                    
                    escpos.write(leftAlign, String.format("%-16s", ten));
                    escpos.write(rightAlign, String.format("%2d %7.0f %8.0f", 
                            ct.getSoLuong(), ct.getGia(), ct.getSoLuong() * ct.getGia()));
                    escpos.feed(1);
                }
            }

            // Tổng cộng
            escpos.writeLF(normal, "-------------------------------");
            escpos.write(leftAlign, "TỔNG CỘNG:");
            escpos.writeLF(rightAlign, String.format("%,.0f VNĐ", dh.getTongTien()));
            escpos.writeLF(normal, "-------------------------------");
            escpos.writeLF(header, "CẢM ƠN QUÝ KHÁCH!");
            escpos.writeLF(header, "HẸN GẶP LẠI!");
            
            // Cắt giấy
            escpos.feed(5);
            escpos.cut(EscPos.CutMode.FULL);
            
        } catch (Exception e) {
            throw new IOException("Lỗi khi in: " + e.getMessage());
        }
    }
}