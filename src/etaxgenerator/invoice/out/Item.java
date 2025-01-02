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
    
    public Item copy(){
        Item it = new Item();
        it.code = this.code;
        it.name = this.name;
        it.price = this.price;
        it.qty = this.qty;
        it.percentageDiscount = this.percentageDiscount;
        it.valueDiscount = this.valueDiscount;
        it.totalDiscount = this.totalDiscount;
        return it;
    }
    
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
    public double getRetensi(double percent){
        return getTotal()*percent;
    }
    public void applyMul(double mul){
        this.price *= mul;
    }
    public void applyRetensi(double mul){
        applyMul(1.0 - mul);
    }
    public String build(double ppnPercent){
        return String.format(Util.usLocale, "\"OF\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"\n",
                "",
                Util.escape(name),
                Util.formatDouble(price),
                Util.formatDouble(qty),
                Util.formatDouble(getSubtotal()),
                Util.formatDouble(getTotalDiscount()),
                Util.formatDouble(getTotal()),
                Util.formatDouble(getPPn(ppnPercent)),
                "0",
                Util.formatDouble(0.0)
        );
    }
}
