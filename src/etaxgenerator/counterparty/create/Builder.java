/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etaxgenerator.counterparty.create;

import etaxgenerator.util.Util;
import java.util.ArrayList;
import java.io.PrintWriter;
import java.io.File;
import etaxgenerator.counterparty.Counterparty;

/**
 *
 * @author MojoMacW7
 */
public class Builder {
    ArrayList<Counterparty> counterparties = new ArrayList<Counterparty>();
    public Builder(){}
    
    public void add(Counterparty counterparty){
        counterparties.add(counterparty);
    }
    
    public void clear(){
        counterparties.clear();
    }
    
    public void deleteLast(){
        int c = size();
        if(c > 0){
            counterparties.remove(c-1);
        }
    }
    
    public String build(){
        String ret = new String(Counterparty.header);
        for(Counterparty cp : counterparties){
            ret = ret + cp.build();
        }
        return ret;
    }
    
    public int size(){
        return counterparties.size();
    }
    
    public int[] export(){
        File f = null;
        PrintWriter writer = null;
        int[] o = new int[]{size(), 0};
        try{
            f = Util.getExportCSVPath();
            if(f == null){
                if(o[0]== 1){
                    clear();
                }
                o[0] = 0;
                return o;
            }
            writer = new PrintWriter(f, "UTF-8");
            writer.print(build());
            o[1] = etaxgenerator.counterparty.Database.getInstance().insertIfNotExists(counterparties);
            
            if(o[0]== 1){
                clear();
            }
            return o;
        }catch(Exception ex){
            Util.handleException(ex);
            if(o[0]== 1){
                clear();
            }
            o[0] = 0;
            return o;
        }finally{
            if (writer != null){
                writer.close();
            }
        }
    }
}
