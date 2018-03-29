/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etaxgenerator.counterparty;

import etaxgenerator.util.Util;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


/**
 *
 * @author MojoMacW7
 */
public class Database {
    public static Database instance = null;
    public static Database getInstance(){
        if (instance == null){
            instance = new Database();
        }
        return instance;
    }
    etaxgenerator.util.Database db;
    public Database(){
        this.db =etaxgenerator.util.Database.getInstance();
        instance = this;
        init();
    }
    public Database(etaxgenerator.util.Database db){
        this.db = db;
        instance = this;
        init();
    }
    
    public static void init(){
        etaxgenerator.util.Database db = etaxgenerator.util.Database.getInstance();
        System.out.println("Initing counterparty database...");
        db.execute("CREATE TABLE IF NOT EXISTS Counterparty(id INTEGER PRIMARY KEY, npwp TEXT UNIQUE, name TEXT UNIQUE, address TEXT, block TEXT, no TEXT, rt TEXT, rw TEXT, kelurahan TEXT, kecamatan TEXT, city TEXT, province TEXT, zipCode TEXT, telephone TEXT)");
        
        db.commit();
        System.out.println("Inited counterparty database");

    }
    
    private static boolean fillPreparedStatement(PreparedStatement pstmt, Counterparty cp){
        try{
            int i = 0;
            pstmt.setString(++i, cp.getNumberNPWP());
            pstmt.setString(++i, cp.getCapsName());
            pstmt.setString(++i, cp.address);
            pstmt.setString(++i, cp.block);
            pstmt.setString(++i, cp.no);
            pstmt.setString(++i, cp.rt);
            pstmt.setString(++i, cp.rw);
            pstmt.setString(++i, cp.kelurahan);
            pstmt.setString(++i, cp.kecamatan);
            pstmt.setString(++i, cp.city);
            pstmt.setString(++i, cp.province);
            pstmt.setString(++i, cp.zipCode);
            pstmt.setString(++i, cp.telephone);
            return true;
        }catch(SQLException ex){
            Util.handleException(ex);
        }
        return false;
    }
    public boolean insertOrUpdate(Counterparty cp){
        System.out.println(String.format("Upserting \"%s\"...", cp.name));
        if(insertIfNotExists(cp)){
            return true;
        }else{
            try{
                PreparedStatement pstmt = db.prepareStatement("UPDATE Counterparty SET npwp=?, name=?, address=?, block=?, no=?, rt=?, rw=?, kelurahan=?, kecamatan=?, city=?, province=?, zipCode=?, telephone=? WHERE npwp=? OR name=?");
                fillPreparedStatement(pstmt, cp);
                pstmt.setString(14, cp.getNumberNPWP());
                pstmt.setString(15, cp.getCapsName());
                int ret = pstmt.executeUpdate();
                if(ret>0){
                    db.commit();
                    pstmt = db.prepareStatement("SELECT id FROM Counterparty WHERE npwp=? OR name=?");
                    pstmt.setString(1, cp.getNumberNPWP());
                    pstmt.setString(2, cp.getCapsName());
                    ResultSet rs = pstmt.executeQuery();
                    rs.next();
                    cp.id = rs.getInt(1);
                    System.out.println(String.format("Inserted \"%s\"", cp.name));
                    return true;
                }
            }catch(SQLException ex){
                Util.handleException(ex);
            }
            return false;
        }
    }
    public boolean insertIfNotExists(Counterparty cp){
        if(Util.parseLong(cp.getNumberNPWP()) == 0){
            return false;
        }
        System.out.println(String.format("Inserting \"%s\" if not exists...", cp.name));
        try{
            PreparedStatement pstmt = db.prepareStatement("INSERT OR IGNORE INTO Counterparty(npwp, name, address, block, no, rt, rw, kelurahan, kecamatan, city, province, zipCode, telephone) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            fillPreparedStatement(pstmt, cp);
            int rows = pstmt.executeUpdate();
            db.commit();
            ResultSet keys = pstmt.getGeneratedKeys();
            keys.next();
            int key = keys.getInt(1);
            cp.id = key;
            boolean ret = rows>0 || key > 0;
            if(ret){
                System.out.println(String.format("Inserted \"%s\"", cp.name));
            }else{
                System.out.println(String.format("Counterparty \"%s\" or \"%s\" already exists", cp.npwp, cp.name));
            }
            return ret;
        }catch(SQLException ex){
            Util.handleException(ex);
        }
        return false;
    }
    
    public int insertOrUpdate(List<Counterparty> cps){
        int i = 0;
        System.out.println("Inserting counterparties...");
        for (Counterparty cp : cps){
            if(insertOrUpdate(cp)){
                ++i;
            }
        }
        System.out.println("Inserted " + i + " counterparties");
        return i;
    }
    public int insertIfNotExists(List<Counterparty> cps){
        int i = 0;
        System.out.println("Inserting counterparties...");
        for (Counterparty cp : cps){
            if(insertIfNotExists(cp)){
                ++i;
            }
        }
        System.out.println("Inserted " + i + " counterparties");
        return i;
    }
    
    
    public boolean remove(Counterparty cp){
        System.out.println(String.format("Removing counterparty \"%s\" and \"%s\"", cp.npwp, cp.name));
        try{
            PreparedStatement pstmt = db.prepareStatement("DELETE FROM Counterparty WHERE npwp=? AND name=?");
           // pstmt.setInt(1, cp.id);
            pstmt.setString(1, cp.getNumberNPWP());
            pstmt.setString(2, cp.getCapsName());
            int ret = pstmt.executeUpdate();
            if(ret>0){
                System.out.println(String.format("Removed \"%s\" and \"%s\"", cp.npwp, cp.name));
                db.commit();
                return true;
            }else{
                System.out.println(String.format("Counterparty \"%s\" and\"%s\" doesn't exist", cp.npwp, cp.name));
            }
        }catch(SQLException ex){
            Util.handleException(ex);
        }
        return false;
    }
    
    public boolean removeByName(String name){
        System.out.println(String.format("Removing counterparty \"%s\"", name));
        try{
            PreparedStatement pstmt = db.prepareStatement("DELETE FROM Counterparty WHERE name=?");
            pstmt.setString(1, name);
            int ret = pstmt.executeUpdate();
            if(ret>0){
                System.out.println(String.format("Removed \"%s\"", name));
                db.commit();
                return true;
            }else{
                System.out.println(String.format("Counterparty \"%s\" doesn't exist", name));
            }
        }catch(SQLException ex){
            Util.handleException(ex);
        }
        return false;
    }
    public boolean removeByNPWP(String npwp){
        npwp = Util.numericOnly(npwp);
        System.out.println(String.format("Removing counterparty \"%s\"", npwp));
        try{
            PreparedStatement pstmt = db.prepareStatement("DELETE FROM Counterparty WHERE npwp=?");
            pstmt.setString(1, npwp);
            int ret = pstmt.executeUpdate();
            if(ret>0){
                System.out.println(String.format("Removed \"%s\"", npwp));
                db.commit();
                return true;
            }else{
                System.out.println(String.format("Counterparty \"%s\" doesn't exist", npwp));
            }
        }catch(SQLException ex){
            Util.handleException(ex);
        }
        return false;
    }
    public Counterparty findByName(String name, String prefix){
        if (prefix == null || prefix.trim().isEmpty()){
            return findByName(name);
        }
        Counterparty ret = null;
        if (ret == null){
            ret = findByName0(prefix + ". " + name);
        }
        if (ret == null){
            ret = findByName0(prefix + "." + name);
        }
        if (ret == null){
            ret = findByName0(prefix + " " + name);
        }
        if (ret == null){
            ret = findByName0(name);
        }
        return ret;
    }
    
    public static String[] prefixes = new String[]{"PT", "CV", "UD"};
    public static String[] checkPrefix(String name){
        for (String prefix : prefixes){
            if (name.startsWith(prefix)){
                String name1 = name.substring(prefix.length());
                if (name1.startsWith(".")){
                    name1 = name1.substring(1);
                }else if (!name1.startsWith(" ")){
                    continue;
                }
                name1 = name1.trim();
                return new String[]{name1, prefix};
            }
        }
        return null;
    }
    public static String[] checkPrefixSuffix(String name){
        for (String prefix : prefixes){
            if (name.endsWith(prefix)){
                int pl = prefix.length();
                String name1 = name.substring(0, name.length()-pl);
                name1 = name1.trim();
                return new String[]{name1, prefix};
            }
        }
        return null;
    }
    
    
    public Counterparty findByName(String name){
        String[] np = checkPrefix(name);
        if (np == null || np[1] == null || np[1].trim().isEmpty()){
            return findByName0(name);
        }else{
            return findByName(np[0], np[1]);
        }
    }
    
    public Counterparty findByName0(String name){
        try{
            PreparedStatement pstmt = db.prepareStatement("SELECT * FROM Counterparty WHERE name=?");
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            Counterparty ret = Parser.parse(rs);
            if (ret == null){
                pstmt.setString(1, name.toUpperCase());
                rs = pstmt.executeQuery();
                ret = Parser.parse(rs);
            }
            return ret;
        }catch(SQLException ex){
            Util.handleException(ex);
        }
        return null;
    }
    
    public Counterparty findByNPWP(String npwp){
        try{
            npwp = Util.numericOnly(npwp);
            PreparedStatement pstmt = db.prepareStatement("SELECT * FROM Counterparty WHERE npwp=?");
            pstmt.setString(1, npwp);
            ResultSet ret = pstmt.executeQuery();
            return Parser.parse(ret);
        }catch(SQLException ex){
            Util.handleException(ex);
        }
        return null;
    }
}
