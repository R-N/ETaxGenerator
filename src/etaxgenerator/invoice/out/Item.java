/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etaxgenerator.invoice.out;

import etaxgenerator.util.Util;

/**
 *
 * @author MojoMacW7
 */
public class Item {
    //"OF","KODE_OBJEK","NAMA","HARGA_SATUAN","JUMLAH_BARANG","HARGA_TOTAL","DISKON","DPP","PPN","TARIF_PPNBM","PPNBM"
    public String code="";
    public String name="";
    public double price=0;
    public double qty=1;
    
    public double percentageDiscount=0;
    public double valueDiscount=0;
    public double totalDiscount=0;//total = subtotal-totalDiscount
    
    public double getSubtotal(){
        return price*qty;
    }
    public double getTotalDiscount(){
        double subtotal = getSubtotal();
        return subtotal*percentageDiscount/100.0 + valueDiscount*qty + totalDiscount;
    }
    public double getTotal(){
        return getSubtotal() - getTotalDiscount();
    }
    public double getPPn(double percent){
        return getTotal()*percent;
    }
    public String build(double ppnPercent){
        return String.format(Util.usLocale, "\"OF\",\"%s\",\"%s\",\"%.1f\",\"%.1f\",\"%.1f\",\"%.1f\",\"%.1f\",\"%.1f\",\"%s\",\"%.1f\"\n",
                "",
                Util.escape(name),
                price,
                qty,
                getSubtotal(),
                getTotalDiscount(),
                getTotal(),
                getPPn(ppnPercent),
                "0",
                0.0
        );
    }
}
