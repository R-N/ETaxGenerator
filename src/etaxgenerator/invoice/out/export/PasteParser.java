/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etaxgenerator.invoice.out.export;

import etaxgenerator.invoice.out.Item;
import etaxgenerator.counterparty.Counterparty;
import etaxgenerator.counterparty.Database;
import etaxgenerator.invoice.out.Invoice;
import etaxgenerator.util.Util;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 *
 * @author MojoMacW7
 */
public class PasteParser{// implements ActionListener {
   GUI gui = null;
   private String rowstring,value;
   //private Clipboard system;
   private JComponent jComp ;
   /**
    * The Excel Adapter is constructed with a
    * JTable on which it enables Copy-Paste and acts
    * as a Clipboard listener.
    */
    public PasteParser(GUI gui, JComponent myComp)
   {
        this.gui = gui;
        jComp = myComp;
        KeyStroke paste = KeyStroke.getKeyStroke(KeyEvent.VK_V,ActionEvent.CTRL_MASK,false);
        // Identifying the Paste KeyStroke user can modify this
        //to copy on some other Key combination.
        //jComp.registerKeyboardAction(this,"Paste",paste,JComponent.WHEN_FOCUSED);
        //system = Toolkit.getDefaultToolkit().getSystemClipboard();
   }
    public void parse(String s){
        if(Util.isNullOrEmpty(s)){
            Util.showMessage("Text to parse is empty", "Failed");
            return;
        }
        gui.clearForm();
        String[] rows = s.split("\n");
        int cbRow = rows.length;
        String discountPercent = "";
        String date = "";
        String customer = "";
        String prefix = "";
        String invoiceNo = "";
        String[][] items = new String[cbRow][];
        int lastRow = cbRow-1;
        for(int i=0;i<cbRow;i++)
        {
            rowstring = rows[i];
            String[] cols = rowstring.split("\t");
            int cbCol = cols.length;
            int limit = 12;
            int diff = 0;
            if (cbCol < limit){
                diff = limit-cbCol;
                limit = cbCol;
            }
            items[i] = new String[4];
            if(i==0){
                if (cbCol > 0) date = cols[0].trim();
                if (cbCol > 4) customer = cols[4].trim();
                if (cbCol > 5) prefix = cols[5].trim();
                if (cbCol > 14) discountPercent = cols[14].trim();
                if (cbCol > 17) invoiceNo = cols[17].trim();
            }
            for(int j=9;j<limit;j++)
            {
               value = cols[j].trim();
               items[i][j-9] = value;
            }
            items[i][3] = discountPercent;
        }
        String fullCustomer;
        Counterparty cp = etaxgenerator.counterparty.Database.getInstance().findByName(customer, prefix);
        if (cp == null){
            fullCustomer = prefix + ". " + customer; 
        gui.customerField.setText(fullCustomer.toUpperCase());
        }else{
            gui.load(cp);
        }
        gui.dateField.setText(Util.formatDate(date));
        if(invoiceNo != null && !invoiceNo.isEmpty()){
            gui.invoiceNoField.setText(invoiceNo);
        }
        gui.itemTableAdapter.paste(items);
        gui.refreshItemCount();
        gui.calculate();
    }
    public String generate(){
        Invoice in = gui.reader.readInvoice();
        String date = Util.formatDateExcel(in.getDate());
        String name = in.counterparty.name;
        String[] np = Database.checkPrefix(name);
        String prefix = "";
        if(np != null){
            prefix = np [1];
            name = np[0];
        }
        name = Util.capitalize(name);
        double discountPercent = 0;
        int ic = in.items.size();
        String firstItemName = "";
        String firstItemQty = "";
        String firstItemPrice = "";
        if(ic > 0){
            Item firstItem = in.items.get(0);
            discountPercent = firstItem.percentageDiscount;
            firstItemName = firstItem.name;
            firstItemQty = String.valueOf(firstItem.qty);
            firstItemPrice = Util.formatNumber(firstItem.price);
        }
        String header = String.format("%s\t\t\t\t%s\t%s\t\t\t\t%s\t%s\t%s\n",
                date,
                name,
                prefix,
                firstItemName,
                firstItemQty,
                firstItemPrice
        );
        String ret = header;
        for(int i = 1; i < ic; ++i){
            Item it = in.items.get(i);
            String line = String.format("\t\t\t\t\t\t\t\t\t%s\t%s\t%s\n",
                    it.name,
                    String.valueOf(it.qty),
                    Util.formatNumber(it.price)
            );
            ret = ret + line;
        }
        
        
        return ret;
    }
    /*public void actionPerformed(ActionEvent e){
        if (e.getActionCommand().compareTo("Paste")==0)
        {
            try
            {
              String trstring= (String)(system.getContents(this).getTransferData(DataFlavor.stringFlavor));
               parse(trstring);
            } catch(Exception ex){ex.printStackTrace();}
         
        }
    }*/
}
