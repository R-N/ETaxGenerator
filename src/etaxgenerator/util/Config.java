/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etaxgenerator.util;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

/**
 *
 * @author MojoMacW7
 */
public class Config {
    public static String workDir = ".";
    public static String companyName = "PT CONCRETINDO NUSA CEMERLANG";
    public static String companyAddress = "JL GUBENG KERTAJAYA 5-C/17 , KOTA SURABAYA";
    public static String lastInvoiceNoPrefix = "";
    public static long lastInvoiceNo = 0;
    public static boolean indoComma = false;
    
    public static void init(Class main) throws URISyntaxException{
        Path jarDir = Paths.get(main.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
        Config.workDir = jarDir.toString();

        Database db = Database.getInstance();
        db.execute("CREATE TABLE IF NOT EXISTS Config(name TEXT PRIMARY KEY, value TEXT)");
        db.commit();
        saveIfNotExists("companyName", companyName);
        saveIfNotExists("companyAddress", companyAddress);
        companyName = get("companyName");
        companyAddress = get("companyAddress");
        lastInvoiceNoPrefix = get("lastInvoiceNoPrefix");
        if(lastInvoiceNoPrefix==null){
            lastInvoiceNoPrefix = "";
        }
        try{
            lastInvoiceNo = Util.parseLong(get("lastInvoiceNo"));
        }catch(Exception ex){
            
        }
        defaultIndoComma();
        setIndoComma();
    }
    
    public static void setLastInvoiceNo(String sLastInvoiceNo){
        if(sLastInvoiceNo == null || sLastInvoiceNo.trim().isEmpty()){
            return;
        }
        String[] s2 = sLastInvoiceNo.replace("\\-",".").split("\\.");
        if(s2.length != 3){
            try{
                throw new Exception(String.format("Invalid invoice no: '%s', dots=%d, should be 2", sLastInvoiceNo, s2.length-1));
            }catch(Exception ex){
                Util.handleException(ex);
            }
        }
        String s22 = Util.numericOnly(s2[2]);
        String prefix = sLastInvoiceNo.substring(0, sLastInvoiceNo.length()-s2[2].length());
        Config.lastInvoiceNoPrefix = prefix;
        Config.lastInvoiceNo = Util.parseLong(s2[2]);
        Config.save("lastInvoiceNo", s22);
        Config.save("lastInvoiceNoPrefix", prefix);
    }
    public static void setIndoComma(){
        setIndoComma(Locale.getDefault().equals(Util.idLocale));
    }
    public static void defaultIndoComma(){
        String sIndoComma = get("indoComma");
        if(sIndoComma == null || sIndoComma.trim().isEmpty()){
            setIndoComma(Locale.getDefault().equals(Util.idLocale));
        }else{
            setIndoComma(sIndoComma);
        }
    }
    public static void setIndoComma(String sIndoComma){
        if(sIndoComma == null || sIndoComma.trim().isEmpty()){
            return;
        }
        setIndoComma(Boolean.valueOf(sIndoComma));
    }
    public static void setIndoComma(boolean indoComma){
        Config.indoComma = indoComma;
        Config.save("indoComma", String.valueOf(indoComma));
    }
    
    public static boolean saveIfNotExists(String name, String value){
        Database db = Database.getInstance();
        try{
            PreparedStatement pstmt = db.prepareStatement("INSERT OR IGNORE INTO Config(name, value) VALUES(?, ?)");
            pstmt.setString(1, name);
            pstmt.setString(2, value);
            int rows = pstmt.executeUpdate();
            db.commit();
            return rows>0;
        }catch(SQLException ex){
            Util.handleException(ex);
        }
        return false;
    }
    public static boolean save(String name, String value){
        Database db = Database.getInstance();
        if(saveIfNotExists(name, value)){
            return true;
        }else{
            try{
                PreparedStatement pstmt = db.prepareStatement("UPDATE Config SET value=? WHERE name=?");
                pstmt.setString(2, name);
                pstmt.setString(1, value);
                int ret = pstmt.executeUpdate();
                if(ret>0){
                    db.commit();
                    return true;
                }

            }catch(SQLException ex){
                Util.handleException(ex);
            }
            return false;
        }
    }
    public static String get(String name){
        
        Database db = Database.getInstance();
        try{
            PreparedStatement pstmt = db.prepareStatement("SELECT value FROM Config WHERE name=?");
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()){
                return rs.getString(1);
            }
        }catch(SQLException ex){
            Util.handleException(ex);
        }
        return null;
    }
    public static String buildInvoiceInfo(){
        return String.format("\"FAPR\",\"%s\",\"%s\",,,,\n",
                companyName,
                companyAddress
        );
    }
}
