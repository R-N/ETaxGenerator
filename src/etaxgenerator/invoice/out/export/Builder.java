/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etaxgenerator.invoice.out.export;

import etaxgenerator.counterparty.Counterparty;
import etaxgenerator.invoice.out.Invoice;
import etaxgenerator.util.Util;
import java.io.PrintWriter;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author MojoMacW7
 */
public class Builder {
    ArrayList<Invoice> invoices = new ArrayList<Invoice>();
    public Builder(){}
    
    public void add(Invoice invoice){
        invoices.add(invoice);
    }
    
    public void clear(){
        invoices.clear();
    }
    
    public void deleteLast(){
        int c = size();
        if (c > 0){
            invoices.remove(c-1);
        }
    }
    
    public String build(){
        String ret = new String(Invoice.header);
        for(Invoice in : invoices){
            ret = ret + in.build();
        }
        return ret;
    }
    
    public int size(){
        return invoices.size();
    }
    
    public int export(){
        PrintWriter writer = null;
        int count = size();
        try{
            File f = etaxgenerator.util.Util.getExportCSVPath();
            if(f == null){
                if(count == 1){
                    clear();
                }
                return 0;
            }
            writer = new PrintWriter(f, "UTF-8");
            writer.print(build());
            if(count == 1){
                clear();
            }
            return count;
        }catch(Exception ex){
            Util.handleException(ex);
            if(count == 1){
                clear();
            }
            return 0;
        }finally{
            if(writer != null){
                writer.close();
            }
        }
    }
}

