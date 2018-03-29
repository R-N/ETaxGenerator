/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etaxgenerator.counterparty;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author MojoMacW7
 */
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Parser {
    
    public static Counterparty parse(String line){
        
      // String to be scanned to find the pattern.
        String pattern = "\"LT\",\"(.*)\",\"(.*)\",\"(.*)\",\"(.*)\",\"(.*)\",\"(.*)\",\"(.*)\",\"(.*)\",\"(.*)\",\"(.*)\",\"(.*)\",\"(.*)\",\"(.*)\"";

        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);

        // Now create matcher object.
        Matcher m = r.matcher(line);
        if (m.find()) {
            Counterparty cp = new Counterparty();
            int i = 0;
            cp.npwp = m.group(++i);
            cp.name = m.group(++i);
            cp.address = m.group(++i);
            cp.block = m.group(++i);
            cp.no = m.group(++i);
            cp.rt = m.group(++i);
            cp.rw = m.group(++i);
            cp.kecamatan = m.group(++i);
            cp.kelurahan = m.group(++i);
            cp.city = m.group(++i);
            cp.province = m.group(++i);
            cp.zipCode = m.group(++i);
            cp.telephone = m.group(++i);
            return cp;
        }
        return null;
    }
    
    public static Counterparty parse(ResultSet result){
        try{
            if(!result.next()){
                return null;
            }
            Counterparty cp = new Counterparty();
            cp.id = result.getInt("id");
            cp.npwp = result.getString("npwp");
            cp.name = result.getString("name");
            cp.address = result.getString("address");
            cp.block = result.getString("block");
            cp.no = result.getString("no");
            cp.rt = result.getString("rt");
            cp.rw = result.getString("rw");
            cp.kelurahan = result.getString("kelurahan");
            cp.kecamatan = result.getString("kecamatan");
            cp.city = result.getString("city");
            cp.province = result.getString("province");
            cp.zipCode = result.getString("zipCode");
            cp.telephone = result.getString("telephone");
            return cp;
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return null;
    }
}
