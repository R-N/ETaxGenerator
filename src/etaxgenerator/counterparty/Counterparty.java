/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etaxgenerator.counterparty;

import etaxgenerator.util.Util;

/**
 *
 * @author MojoMacW7
 */
public class Counterparty {
    public static final String header0 = "\"LT\",\"NPWP\",\"NAMA\",\"JALAN\",\"BLOK\",\"NOMOR\",\"RT\",\"RW\",\"KECAMATAN\",\"KELURAHAN\",\"KABUPATEN\",\"PROPINSI\",\"KODE_POS\",\"NOMOR_TELEPON\"";
    public static final String header = header0 + "\n";
    public int id = -1;
    public String npwp = "00.000.000.0-000.000";
    public String name = "";
    public String address = "";
    public String block = "";
    public String no = "";
    public String rt = "";
    public String rw = "";
    public String kelurahan = "";
    public String kecamatan = "";
    public String city = "";
    public String province = "";
    public String zipCode = "";
    public String telephone = "";
    
    public Counterparty(){}
    
    
    public static String getFormattedNPWP(String npwp){
        npwp = Util.numericOnly(npwp);
        char[] chars = npwp.toCharArray();
        if (chars.length < 15){
            Util.showError("NPWP too short: " + npwp, "NPWP too short");
        }
        int i = -1;
        String s = String.format(Util.usLocale, "%c%c.%c%c%c.%c%c%c.%c-%c%c%c.%c%c%c", 
                chars[++i], chars[++i], chars[++i], chars[++i], chars[++i], chars[++i], chars[++i], chars[++i], chars[++i], chars[++i], chars[++i], chars[++i], chars[++i], chars[++i], chars[++i]);
        return s;
    }
    
    public static String getCapsName(String name){
        return name.trim().toUpperCase();
    }
    public String getFormattedNPWP(){
        return getFormattedNPWP(npwp);
    }
    public String getNumberNPWP(){
        return Util.numericOnly(npwp);
    }
    public String getCapsName(){
        return getCapsName(name);
    }
    public String getFullAddress(){
        return getFullAddress(false);
    }
    public String getFullAddress(boolean withoutCity){
        String ret = address;
        if (!block.isEmpty()){
            ret = ret + " Blok " + block;
        }
        if (!no.isEmpty()){
            ret = ret + " No." + no;
        }
        if (!rt.isEmpty()){
            try{
                ret = ret + String.format(Util.usLocale, " RT:%03d", Util.parseInt(rt));
            }catch(Exception ex){
                ret = ret + " RT:" + rt;
            }
        }
        if (!rw.isEmpty()){
            try{
                ret = ret + String.format(Util.usLocale, " RW:%03d", Util.parseInt(rw));
            }catch(Exception ex){
                ret = ret + " RW:" + rw;
            }
        }
        if(!kelurahan.isEmpty()){
            ret = ret + " Kel." + kelurahan;
        }
        if(!kecamatan.isEmpty()){
            ret = ret + " Kec." + kecamatan;
        }
        if(withoutCity){
            return ret;
        }
        if(!city.isEmpty()){
            ret = ret + " Kota/Kab." + city;
        }
        if(!province.isEmpty()){
            ret = ret + " " + province;
        }
        if(!zipCode.isEmpty()){
            ret = ret + " " + zipCode;
        }
        return ret;
    }
    
    public String build(){
        return String.format(Util.usLocale, "\"LT\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"\n", 
                Util.escape(getNumberNPWP()),
                Util.escape(getCapsName()),
                Util.escape(address),
                Util.escape(block),
                Util.escape(no),
                Util.escape(rt),
                Util.escape(rw),
                Util.escape(kecamatan),
                Util.escape(kelurahan),
                Util.escape(city),
                Util.escape(province),
                Util.escape(zipCode),
                Util.escape(telephone)
        );
        /*return "\"LT\",\"" + getNumberNPWP() 
                + "\",\"" + getCapsName()
                + "\",\"" + address
                + "\",\"" + block
                + "\",\"" + no
                + "\",\"" + rt
                + "\",\"" + rw
                + "\",\"" + kecamatan
                + "\",\"" + kelurahan
                + "\",\"" + city
                + "\",\"" + province
                + "\",\"" + zipCode
                + "\",\"" + telephone
                + "\"\n";*/
    }
}
