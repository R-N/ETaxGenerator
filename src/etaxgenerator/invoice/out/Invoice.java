/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etaxgenerator.invoice.out;
import etaxgenerator.util.Config;
import java.util.ArrayList;
import etaxgenerator.counterparty.Counterparty;
import etaxgenerator.util.Util;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author MojoMacW7
 */
public class Invoice {
    public static final String header0 = "\"FK\",\"KD_JENIS_TRANSAKSI\",\"FG_PENGGANTI\",\"NOMOR_FAKTUR\",\"MASA_PAJAK\",\"TAHUN_PAJAK\",\"TANGGAL_FAKTUR\",\"NPWP\",\"NAMA\",\"ALAMAT_LENGKAP\",\"JUMLAH_DPP\",\"JUMLAH_PPN\",\"JUMLAH_PPNBM\",\"ID_KETERANGAN_TAMBAHAN\",\"FG_UANG_MUKA\",\"UANG_MUKA_DPP\",\"UANG_MUKA_PPN\",\"UANG_MUKA_PPNBM\",\"REFERENSI\",\"KODE_DOKUMEN_PENDUKUNG\"\n"
                + "\"LT\",\"NPWP\",\"NAMA\",\"JALAN\",\"BLOK\",\"NOMOR\",\"RT\",\"RW\",\"KECAMATAN\",\"KELURAHAN\",\"KABUPATEN\",\"PROPINSI\",\"KODE_POS\",\"NOMOR_TELEPON\"\n"
                + "\"OF\",\"KODE_OBJEK\",\"NAMA\",\"HARGA_SATUAN\",\"JUMLAH_BARANG\",\"HARGA_TOTAL\",\"DISKON\",\"DPP\",\"PPN\",\"TARIF_PPNBM\",\"PPNBM\"";
    
    public static final String header = header0 + "\n";
    
    public Counterparty counterparty;
    public ArrayList<Item> items = new ArrayList<Item>();
    public String no = "0000000000000";
    public int rev = 0;
    public int day;
    public int month;
    public int year;
    public double dp = 0;
    public double ppnPercent = 0.11;
    public double totalPPn = 0;
    public double ppnDP = 0;
    public double retensiPercent = 0;
    public double totalRetensi = 0;
    
    public Invoice(Counterparty counterparty){
        this.counterparty = counterparty;
    }
    
    public boolean AddItem(Item item){
        return items.add(item);
    }
    
    public Item GetItem(int index){
        return items.get(index);
    }
    
    public Item RemoveItem(int index){
        return items.remove(index);
    }
    public boolean RemoveItem(Item item){
        return items.remove(item);
    }
    public void clear(){
        items.clear();
    }
    public static DecimalFormat nf = (DecimalFormat)NumberFormat.getNumberInstance(Util.idLocale);
    public static String getFormattedNo(long no){
        nf.applyPattern("000,00000000");
        long head = (no/100000000000L);
        long tail = no-head;
        String ret = String.format(Util.usLocale, "%d.%s", head, nf.format(tail));
        return ret;
    }
    public static String getFormattedNo(String no){
        return null;
    }
    public String getNumberNo(){
        return Util.numericOnly(no);
    }
    public String getDate(){
        return String.format("%02d/%02d/%d", day, month, year);
    }
    public double getTotalTotal(){
        double sum = 0;
        for(Item i : items){
            sum+=i.getTotal();
        }
        return sum;
    }
    public double getTotalPPn(){
        return getTotalPPn(this.ppnPercent);
    }
    public double getTotalPPn(double ppnPercent){
        return (int)(ppnPercent * getTotalTotal());
    }
    public double getPPnDP(){
        return getPPnDP(this.ppnPercent);
    }
    public double getPPnDP(double ppnPercent){
        return (int)(ppnPercent * this.dp);
    }
    
    public void setPPnPercent(double ppnPercent){
        this.ppnPercent = ppnPercent;
    }
    
    public void setTotalPPn(double ppn){
        this.totalPPn = ppn;
    }
    public void setPPnDP(double ppn){
        this.ppnDP = ppn;
    }
    public void setRetensiPercent(double retensiPercent){
        this.retensiPercent = retensiPercent;
    }
    public double getTotalRetensi(){
        return getTotalRetensi(this.retensiPercent);
    }
    public double getTotalRetensi(double retensiPercent){
        return getTotalTotal()*retensiPercent;
    }
    public void applyRetensi(double retensiPercent){
        for (Item i : items){
            i.applyRetensi(retensiPercent);
        }
    }
    
    public Invoice copy(){
        Invoice in = new Invoice(this.counterparty);
        
        in.no = this.no;
        in.rev = this.rev;
        in.day = this.day;
        in.month = this.month;
        in.year = this.year;
        in.dp = this.dp;
        in.ppnPercent = this.ppnPercent;
        in.totalPPn = this.totalPPn;
        in.ppnDP = this.ppnDP;
        in.retensiPercent = this.retensiPercent;
        in.totalRetensi = this.totalRetensi;
    
        in.items = new ArrayList<Item>();
        for (Item i : this.items){
            in.items.add(i.copy());
        }
        return in;
    }
    
    public void applyMul(double mul){
        for (Item i : items){
            i.applyMul(mul);
        }
        this.dp *= mul;
        this.totalPPn *= mul;
        this.ppnDP *= mul;
        this.totalRetensi *= mul;
        return;
    }
    
    public String build(){
        if (this.totalPPn == 0){
            this.setTotalPPn(this.getTotalPPn());
        }
        if (this.ppnDP == 0){
            this.setPPnDP(this.getPPnDP());
        }
        String ret = String.format(Util.usLocale, "\"FK\",\"%s\",\"%d\",\"%s\",\"%d\",\"%d\",\"%s\",\"%s\",\"%s\",\"%s\",\"%.0f\",\"%.0f\",\"%.0f\",\"\",\"%s\",\"%.0f\",\"%.0f\",\"%.0f\",\"%s\",\n",
                "01",
                rev,
                getNumberNo(),
                month,
                year,
                getDate(),
                counterparty.getNumberNPWP(),
                Util.escape(counterparty.getCapsName()),
                Util.escape(counterparty.getFullAddress()),
                getTotalTotal(),
                this.totalPPn,
                0.0,
                "0",
                dp,
                ppnDP,
                0.0,
                ""
        );
        ret = ret + Config.buildInvoiceInfo();
        for (Item i : items){
            ret = ret + i.build(ppnPercent);
        }
        return ret;
    }
}
