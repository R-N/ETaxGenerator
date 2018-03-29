/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etaxgenerator.invoice.in;

import etaxgenerator.counterparty.Counterparty;
import etaxgenerator.util.Util;

/**
 *
 * @author MojoMacW7
 */
public class Invoice {
    public static final String header0 = "\"FM\",\"KD_JENIS_TRANSAKSI\",\"FG_PENGGANTI\",\"NOMOR_FAKTUR\",\"MASA_PAJAK\",\"TAHUN_PAJAK\",\"TANGGAL_FAKTUR\",\"NPWP\",\"NAMA\",\"ALAMAT_LENGKAP\",\"JUMLAH_DPP\",\"JUMLAH_PPN\",\"JUMLAH_PPNBM\",\"IS_CREDITABLE\"";
    public static final String header = header0 + "\n";
    
    public Counterparty counterparty;
    public String no = "0000000000000";
    public String date = "00/00/0000";
    public int rev = 0;
    public int period = 0;
    public int year = 0;
    public boolean isCreditable = true;
    public int dpp = 0;
    public int ppn = 0;
    
    public Invoice(Counterparty cp){
        this.counterparty = cp;
    }
    
    public void setNo(String no){
        if(Util.isNullOrEmpty(no)){
            return;
        }
        no = Util.numericOnly(no);
        this.rev = Util.parseInt(String.valueOf(no.charAt(2)));
        this.no = no.substring(3);
    }
    
    public String getNumberNo(){
        return Util.numericOnly(no);
    }
    
    public String getFormattedDate(){
        return Util.formatDate(date);
    }
    
    public void setDPP(int dpp){
        this.dpp = dpp;
        this.ppn = getPPn(dpp);
    }
    
    public static int getPPn(int dpp){
        return (int)(dpp*0.1);
    }
    
    public void setDate(String date){
        this.date = Util.formatDate(date);
        int[] date1 = Util.parseDate(date);
        if(period < date1[1] && year <= date1[2]) period = date1[1];
        if (year < date1[2]) year = date1[2];
    }
    
    
    //"FM","01","0","001.18.96297256","2","2018","08/02/2018","019523919431000","PT BERSAMA BANGUN PERSADA",,"14400000","1440000","0","1"
    public String build(){
        return String.format(Util.usLocale, "\"FM\",\"%s\",\"%d\",\"%s\",\"%d\",\"%d\",\"%s\",\"%s\",\"%s\",%s,\"%d\",\"%d\",\"%d\",\"%d\"",
                "01",
                rev,
                no,
                period,
                year,
                getFormattedDate(),
                counterparty.getNumberNPWP(),
                Util.escape(counterparty.getCapsName()),
                "",//alamat, mbo kok dikosongi
                dpp,
                ppn,
                0,//ppnbm
                isCreditable ? 1 : 0
        );
    }
}
