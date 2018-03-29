/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etaxgenerator.counterparty.create;

import etaxgenerator.counterparty.Counterparty;
import etaxgenerator.counterparty.Database;
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
        String name = "";
        String address = "";
        String address2 = "";
        String city = "";
        String telephone = "";
        String npwp = "";
        int cl = cols.length;
        if (cl > 0) name = cols[0].trim();
        if (cl > 1) address = cols[1].trim();
        if (cl > 2) address2 = cols[2].trim();
        if(cl > 3) city = cols[3].trim();
        if(cl > 4) telephone = cols[4].trim();
        if(cl > 6) npwp = cols[6].trim();
        if(!Util.isNullOrEmpty(address2)){
            address = String.format("%s %s", address, address2);
        }
        String[] ps = Database.checkPrefixSuffix(name);
        Counterparty cp = null;
        if(ps != null){
            name = String.format("%s. %s", ps[1], ps[0]).toUpperCase();
            cp = etaxgenerator.counterparty.Database.getInstance().findByName(ps[0], ps[1]);
        }else{
            cp = etaxgenerator.counterparty.Database.getInstance().findByName(name);
        }
        
        if(cp == null){
            gui.nameField.setText(name);
        }else{
            if(Util.isNullOrEmpty(address)){
                gui.load(cp);
            }else{
                gui.nameField.setText(cp.name);
                gui.addressField.setText(address);
                gui.telephoneField.setText(cp.telephone);
                gui.npwpField.setText(cp.getFormattedNPWP());
                gui.cityField.setText(cp.city);
                
                if(!Util.isNullOrEmpty(cp.zipCode)){
                    if (!city.toUpperCase().contains(cp.zipCode.toUpperCase())){
                        gui.zipCodeField.setText(cp.zipCode);
                    }
                }
                if(!Util.isNullOrEmpty(cp.province)){
                    if (!city.toUpperCase().contains(cp.province.toUpperCase())){
                        gui.provinceField.setText(cp.province);
                    }
                }
            }
        }
        
        if(!Util.isNullOrEmpty(city)) gui.cityField.setText(city);
        if(!Util.isNullOrEmpty(telephone)) gui.telephoneField.setText(telephone);
        if(!Util.isNullOrEmpty(npwp)) gui.npwpField.setText(npwp);
        
    }
    
    public String generate(){
        Counterparty cp = gui.read();
        String name = cp.name;
        String[] np = Database.checkPrefix(name);
        if(np != null){
            name = Util.capitalize(np[0])+np[1];
        }else{
            name = Util.capitalize(name);
        }
        String address = cp.getFullAddress(true);
        String city = cp.city;
        if(!Util.isNullOrEmpty(cp.province)) city = String.format("%s, %s", city, cp.province);
        if (!Util.isNullEmptyOrZero(cp.zipCode)) city = String.format("%s - %s", city, cp.zipCode);
        
        String ret = String.format("%s\t%s\t\t%s\t%s\t\t%s\n",
                name,
                address,
                city,
                cp.telephone,
                cp.getFormattedNPWP()
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

