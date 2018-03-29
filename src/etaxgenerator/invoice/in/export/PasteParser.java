/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etaxgenerator.invoice.in.export;

import etaxgenerator.counterparty.Counterparty;
import etaxgenerator.counterparty.Database;
import etaxgenerator.invoice.in.Invoice;
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
    public void parse(String row){
        if(Util.isNullOrEmpty(row)){
            Util.showMessage("Text to parse is empty", "Failed");
            return;
        }
        gui.clearForm();
        row = row.split("\n")[0];
        String[] cols = row.split("\t");
        String date = "";
        String invoiceNo = "";
        String dppPlusPPn = "";
        String ppn = "";
        String fullCustomer = "";
        int cl = cols.length;
        if (cl > 0) date = cols[0].trim();
        if (cl > 2) fullCustomer = cols[2].trim();
        if (cl > 3) dppPlusPPn = cols[3].trim();
        if(Util.isNullEmptyOrZero(dppPlusPPn) && cl > 5) dppPlusPPn = cols[5].trim();
        if(cl > 8) ppn = cols[8].trim();
        if(cl > 9) invoiceNo = cols[9].trim();
        int iPPn = Util.parseInt(ppn);
        int iDPPPlusPPn = Util.parseInt(dppPlusPPn);
        int iDPP = iDPPPlusPPn - iPPn;
        String dpp = Util.formatNumber(iDPP);
        Counterparty cp = etaxgenerator.counterparty.Database.getInstance().findByName(fullCustomer);
        if (cp == null){
            gui.nameField.setText(fullCustomer.toUpperCase());
        }else{
            gui.load(cp);
        }
        gui.dppField.setText(dpp);
        gui.ppnField.setText(ppn);
        gui.dateField.setText(Util.formatDate(date));
        gui.parseDate();
        gui.invoiceNoField.setText(invoiceNo);
        
        gui.calculate();
    }
    
    public String generate(){
        Invoice in = gui.read();
        String date = Util.formatDateExcel(in.getFormattedDate());
        String name = in.counterparty.name;
        String[] np = Database.checkPrefix(name);
        if(np != null){
            name = String.format("%s. %s", np[1],Util.capitalize(np[0]));
        }else{
            name = Util.capitalize(name);
        }
        String ppn = Util.formatNumber(in.ppn);
        String dppPlusPPn = Util.formatNumber(in.dpp + in.ppn);
        String invoiceNo = gui.invoiceNoField.getText();
        
        String ret = String.format("%s\t\t%s\t\t\t%s\n",
                date,
                name,
                dppPlusPPn
        );
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
