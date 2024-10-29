/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etaxgenerator.invoice.out.export;

import etaxgenerator.util.Util;
import etaxgenerator.counterparty.Counterparty;
import etaxgenerator.invoice.out.Invoice;
import etaxgenerator.invoice.out.Item;
import etaxgenerator.util.Config;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author MojoMacW7
 */
public class FormReader {
    GUI form;
    public FormReader(GUI form){
        this.form = form;
    }
    public Counterparty readCounterparty(){
        Counterparty cp = new Counterparty();
        cp.name = form.customerField.getText().trim();
        cp.npwp = form.npwpField.getText().trim();
        cp.address = form.addressField.getText().trim();
        return cp;
    }
    public Invoice readInvoice(){
        boolean indoComma = Config.indoComma;
        Invoice inv = new Invoice(readCounterparty());
        inv.no = form.invoiceNoField.getText().trim();
        inv.rev = Util.parseInt(form.revField.getText().trim());
        String date = form.dateField.getText().trim();
        String[] date1 = date.replace("-", "/").split("/");
        int dl = date1.length;
        if(dl > 0) inv.day = Util.parseInt(date1[0]);
        if(dl > 1) inv.month = Util.parseInt(date1[1]);
        if(dl > 2) inv.year = Util.parseInt(date1[2]);
        inv.dp = Util.parseDouble(form.dpField.getText().trim(), indoComma);
        inv.setPPnPercent(Util.parseDouble(form.ppnPercentField.getText().trim()));
        inv.setTotalPPn(Util.parseDouble(form.ppnField.getText().trim()));
        inv.setPPnDP(Util.parseDouble(form.ppnDPField.getText().trim()));
        inv.setRetensiPercent(Util.parseDouble(form.retensiPercentField.getText().trim()));
        
        int rowCount = form.itemTable.getRowCount();
        Item prev = null;
        for (int i = 0; i < rowCount; ++i){
            Item it = readItem(i, prev);
            if (it != null){
                inv.AddItem(it);
                prev = it;
            }
        }
        return inv;
    }
    public Item readItem(int index, Item prev){
        boolean indoComma = Config.indoComma;
        DefaultTableModel dtm = (DefaultTableModel)form.itemTable.getModel();
        String name = Util.trim((String)dtm.getValueAt(index, 0));
        int qty = Util.parseInt(Util.trim((String)dtm.getValueAt(index, 1)));
        if(qty <= 0){
            if(name == null || name.isEmpty()){
                
            }else if (prev != null){
                prev.name = prev.name + "; " + name;
            }
            return null;
        }
        Item it = new Item();
        it.name = name;
        it.qty = qty;
        it.price = Util.parseDouble(Util.trim((String)dtm.getValueAt(index, 2)), indoComma);
        it.percentageDiscount = Util.parseDouble(Util.replace(Util.trim((String)dtm.getValueAt(index, 3)), ",", "."), false);
        it.valueDiscount = Util.parseDouble(Util.trim((String)dtm.getValueAt(index, 4)), indoComma);
        
        return it;
    }
    
    public Item[] readItems(){
        int rows = form.itemTable.getRowCount();
        Item it = null;
        ArrayList<Item> items = new ArrayList<Item>(rows);
        for (int i = 0; i < rows; ++i){
            it = this.readItem(i, it);
            if(it != null){
                items.add(it);
            }
        }
        Item[] ret = new Item[items.size()];
        return items.toArray(ret);
    }
}
