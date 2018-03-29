/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etaxgenerator.invoice.in.export;

import etaxgenerator.util.Util;
import etaxgenerator.util.Config;
import etaxgenerator.counterparty.Counterparty;
import etaxgenerator.invoice.in.Invoice;

/**
 *
 * @author MojoMacW7
 */
public class GUI extends javax.swing.JFrame {

    public Builder builder = new Builder();
    PasteParser parser = null;
    /**
     * Creates new form GUI
     */
    public void load(Counterparty cp){
        nameField.setText(cp.name);
        npwpField.setText(cp.getFormattedNPWP());
        addressField.setText(cp.getFullAddress());
    }
    public void refreshQueueCount(){
        queueCountLabel.setText(String.valueOf(builder.size()));
    }
    public Invoice read(){
        Counterparty cp = new Counterparty();
        cp.name = nameField.getText();
        cp.address = addressField.getText();
        cp.npwp= npwpField.getText();
        Invoice in = new Invoice(cp);
        String no = invoiceNoField.getText();
        in.setNo(no);
        in.dpp = Util.parseInt(dppField.getText());
        in.ppn = Util.parseInt(ppnField.getText());
        in.setDate(dateField.getText());
        in.period = Util.parseInt(periodField.getText());
        in.year = Util.parseInt(yearField.getText());
        int[] date = Util.parseDate(in.date);
        if(in.period < date[1] && in.year <= date[2]) in.period = date[1];
        if (in.year < date[2]) in.year = date[2];
        return in;
    }
    
    public boolean check(){
        if(Util.isNullOrEmpty(nameField.getText())){
            Util.showError("Nama harus diisi", "Error");
            return false;
        }
        if(Util.isNullEmptyOrZero(invoiceNoField.getText())){
            Util.showError("Nomor faktur harus diisi dan tidak boleh nol", "Error");
            return false;
        }
        if(Util.isNullEmptyOrZero(periodField.getText())){
            Util.showError("Masa pajak tidak boleh kosong dan tidak boleh nol", "Error");
            return false;
        }
        if(Util.isNullEmptyOrZero(yearField.getText())){
            Util.showError("Tahun pajak tidak boleh kosong dan tidak boleh nol", "Error");
            return false;
        }
        if(Util.isNullEmptyOrZero(dppField.getText())){
            Util.showError("DPP tidak boleh kosong dan tidak boleh nol", "Error");
            return false;
        }
        if(Util.isNullOrEmpty(ppnField.getText())){
            Util.showError("PPn tidak boleh kosong", "Error");
            return false;
        }
        int dpp = Util.parseInt(dppField.getText());
        int ppn = Util.parseInt(ppnField.getText());
        System.out.println("getppn " + Invoice.getPPn(dpp));
        System.out.println("ppn " + ppn);
        int diff = Util.abs(Invoice.getPPn(dpp)-ppn);
        if (diff > 1){
            Util.showError("PPn atau DPP salah. Diff=" + diff, "Error");
            return false;
        }
        int[] date = Util.parseDate(dateField.getText());
        if(date.length != 3 || date[0] < 1 || date[1] < 1 || date[2] < 1){
            Util.showError("Format tanggal salah. Seharusnya DD/MM/YYYY dan tidak boleh nol", "Error");
            return false;
        }else{
            int period = Util.parseInt(periodField.getText());
            int year = Util.parseInt(yearField.getText());
            diff = (year-date[2])*12 + period-date[1];
            if (diff < 0 || diff > 3){
                Util.showError("Faktur hanya bisa masuk SPT bulan pembuatannya atau maksimal bulan ketiga setelahnya", "Error");
                return false;
            }
        }
        return true;
    }
    public boolean add(){
        if(!check()){
            return false;
        }
        builder.add(read());
        refreshQueueCount();
        return true;
    }
    public void generate(){
        int x = builder.size();
        if(x == 0){
            if(!add()){
                return;
            }
            x=1;
        }
        int y = builder.export();
        refreshQueueCount();
        Util.showMessage(String.format("%d/%d faktur pajak masukan berhasil diexport", y,x), "Success");
    }
    public void clearForm(){
        
        nameField.setText("");
        npwpField.setText(Util.nullNPWP);
        addressField.setText("");
        dateField.setText("");
        invoiceNoField.setText("");
        pasteField.setText("");
        dppField.setText("");
        ppnField.setText("");
    }
    public void calculate(){
        ppnField.setText(Util.formatNumber(Invoice.getPPn(Util.parseInt(dppField.getText()))));
    }
    public GUI() {
        initComponents();
        clearForm();
        indoCommaCheckBox.setSelected(Config.indoComma);
        parser = new PasteParser(this, pasteField);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        findNameButton = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        npwpField = new javax.swing.JTextField();
        findNPWPButton = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        addressField = new javax.swing.JTextArea();
        jPanel9 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        invoiceNoField = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        dateField = new javax.swing.JTextField();
        jPanel15 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        periodField = new javax.swing.JTextField();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));
        jLabel17 = new javax.swing.JLabel();
        yearField = new javax.swing.JTextField();
        jPanel14 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        dppField = new javax.swing.JTextField();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));
        jLabel14 = new javax.swing.JLabel();
        ppnField = new javax.swing.JTextField();
        calculateButton = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        indoCommaCheckBox = new javax.swing.JCheckBox();
        parseButton = new javax.swing.JButton();
        generateExcelButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        pasteField = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        queueCountLabel = new java.awt.Label();
        clearQueueButton = new javax.swing.JButton();
        delPrevButton = new javax.swing.JButton();
        clearFormButton = new javax.swing.JButton();
        addButton = new javax.swing.JButton();
        generateButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.PAGE_AXIS));

        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("Faktur Masukan");
        jPanel4.add(jLabel1);

        getContentPane().add(jPanel4);

        jPanel7.setLayout(new javax.swing.BoxLayout(jPanel7, javax.swing.BoxLayout.LINE_AXIS));

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setText("Nama");
        jLabel4.setPreferredSize(new java.awt.Dimension(80, 15));
        jPanel7.add(jLabel4);
        jPanel7.add(nameField);

        findNameButton.setText("Find");
        findNameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findNameButtonActionPerformed(evt);
            }
        });
        jPanel7.add(findNameButton);

        getContentPane().add(jPanel7);

        jPanel6.setLayout(new javax.swing.BoxLayout(jPanel6, javax.swing.BoxLayout.LINE_AXIS));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText("NPWP");
        jLabel3.setPreferredSize(new java.awt.Dimension(80, 15));
        jPanel6.add(jLabel3);
        jPanel6.add(npwpField);

        findNPWPButton.setText("Find");
        findNPWPButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findNPWPButtonActionPerformed(evt);
            }
        });
        jPanel6.add(findNPWPButton);

        getContentPane().add(jPanel6);

        jPanel8.setLayout(new javax.swing.BoxLayout(jPanel8, javax.swing.BoxLayout.LINE_AXIS));

        jLabel5.setText("Alamat Lengkap");
        jLabel5.setPreferredSize(new java.awt.Dimension(80, 14));
        jPanel8.add(jLabel5);

        addressField.setColumns(20);
        addressField.setRows(5);
        jScrollPane4.setViewportView(addressField);

        jPanel8.add(jScrollPane4);

        getContentPane().add(jPanel8);

        jPanel9.setLayout(new javax.swing.BoxLayout(jPanel9, javax.swing.BoxLayout.LINE_AXIS));

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setText("No. Faktur");
        jLabel6.setPreferredSize(new java.awt.Dimension(80, 15));
        jPanel9.add(jLabel6);
        jPanel9.add(invoiceNoField);

        getContentPane().add(jPanel9);

        jPanel11.setLayout(new javax.swing.BoxLayout(jPanel11, javax.swing.BoxLayout.LINE_AXIS));

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel11.setText("Tanggal");
        jLabel11.setPreferredSize(new java.awt.Dimension(80, 15));
        jPanel11.add(jLabel11);

        dateField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dateFieldActionPerformed(evt);
            }
        });
        jPanel11.add(dateField);

        getContentPane().add(jPanel11);

        jPanel15.setLayout(new javax.swing.BoxLayout(jPanel15, javax.swing.BoxLayout.LINE_AXIS));

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel16.setText("Masa Pajak:");
        jLabel16.setPreferredSize(new java.awt.Dimension(80, 15));
        jPanel15.add(jLabel16);
        jPanel15.add(periodField);
        jPanel15.add(filler4);

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel17.setText("Tahun Pajak:");
        jLabel17.setPreferredSize(new java.awt.Dimension(80, 15));
        jPanel15.add(jLabel17);
        jPanel15.add(yearField);

        getContentPane().add(jPanel15);

        jPanel14.setLayout(new javax.swing.BoxLayout(jPanel14, javax.swing.BoxLayout.LINE_AXIS));

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel15.setText("DPP :");
        jLabel15.setPreferredSize(new java.awt.Dimension(80, 15));
        jPanel14.add(jLabel15);

        dppField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dppFieldActionPerformed(evt);
            }
        });
        jPanel14.add(dppField);
        jPanel14.add(filler3);

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel14.setText("PPN : ");
        jLabel14.setPreferredSize(new java.awt.Dimension(80, 15));
        jPanel14.add(jLabel14);
        jPanel14.add(ppnField);

        calculateButton.setText("Calculate");
        calculateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calculateButtonActionPerformed(evt);
            }
        });
        jPanel14.add(calculateButton);

        getContentPane().add(jPanel14);

        jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5, javax.swing.BoxLayout.LINE_AXIS));

        jLabel2.setText("Paste Excel");
        jLabel2.setPreferredSize(new java.awt.Dimension(80, 14));
        jPanel5.add(jLabel2);

        indoCommaCheckBox.setText("Indo Comma");
        indoCommaCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                indoCommaCheckBoxActionPerformed(evt);
            }
        });
        jPanel5.add(indoCommaCheckBox);

        parseButton.setText("Parse");
        parseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                parseButtonActionPerformed(evt);
            }
        });
        jPanel5.add(parseButton);

        generateExcelButton.setText("Generate");
        generateExcelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateExcelButtonActionPerformed(evt);
            }
        });
        jPanel5.add(generateExcelButton);

        pasteField.setColumns(20);
        pasteField.setRows(5);
        jScrollPane2.setViewportView(pasteField);

        jPanel5.add(jScrollPane2);

        getContentPane().add(jPanel5);

        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));

        queueCountLabel.setText("Count: 0");
        jPanel2.add(queueCountLabel);

        clearQueueButton.setText("Clear Queue");
        clearQueueButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearQueueButtonActionPerformed(evt);
            }
        });
        jPanel2.add(clearQueueButton);

        delPrevButton.setText("Del Prev");
        delPrevButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delPrevButtonActionPerformed(evt);
            }
        });
        jPanel2.add(delPrevButton);

        clearFormButton.setText("Clear Form");
        clearFormButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearFormButtonActionPerformed(evt);
            }
        });
        jPanel2.add(clearFormButton);

        addButton.setText("Add");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });
        jPanel2.add(addButton);

        generateButton.setText("Generate");
        generateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateButtonActionPerformed(evt);
            }
        });
        jPanel2.add(generateButton);

        getContentPane().add(jPanel2);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void findNameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findNameButtonActionPerformed
        // TODO add your handling code here:
        String name = nameField.getText().trim();
        Counterparty cp = etaxgenerator.counterparty.Database.getInstance().findByName(Counterparty.getCapsName(name));
        if (cp == null){
            Util.showError(String.format("Lawan transaksi dengan nama \"%s\" tidak ditemukan.", name), "Failed");
        }else{
            load(cp);
        }
    }//GEN-LAST:event_findNameButtonActionPerformed

    private void findNPWPButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findNPWPButtonActionPerformed
        // TODO add your handling code here:
        String npwp = npwpField.getText().trim();
        Counterparty cp = etaxgenerator.counterparty.Database.getInstance().findByNPWP(Util.numericOnly(npwp));
        if (cp == null){
            Util.showError(String.format("Lawan transaksi dengan NPWP \"%s\" tidak ditemukan.", npwp), "Failed");
        }else{
            load(cp);
        }
    }//GEN-LAST:event_findNPWPButtonActionPerformed

    private void indoCommaCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_indoCommaCheckBoxActionPerformed
        // TODO add your handling code here:
        int dpp = Util.parseInt(dppField.getText());
        int ppn = Util.parseInt(ppnField.getText());
        Config.setIndoComma(indoCommaCheckBox.isSelected());
        if(dpp > 0) dppField.setText(Util.formatNumber(dpp));
        if(ppn > 0) ppnField.setText(Util.formatNumber(ppn));
    }//GEN-LAST:event_indoCommaCheckBoxActionPerformed

    private void parseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_parseButtonActionPerformed
        // TODO add your handling code here:
        parser.parse(pasteField.getText());
    }//GEN-LAST:event_parseButtonActionPerformed

    private void calculateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calculateButtonActionPerformed
        // TODO add your handling code here:
        calculate();
    }//GEN-LAST:event_calculateButtonActionPerformed

    private void clearQueueButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearQueueButtonActionPerformed
        // TODO add your handling code here:
        if(Util.askConfirmation("Are you sure you want to clear the export queue?", "Confirmation")){
            builder.clear();
            refreshQueueCount();
        }
    }//GEN-LAST:event_clearQueueButtonActionPerformed

    private void delPrevButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delPrevButtonActionPerformed
        // TODO add your handling code here:
        if(Util.askConfirmation("Are you sure you want to delete the previous export queue entry?", "Confirmation")){
            builder.deleteLast();
            refreshQueueCount();
        }
    }//GEN-LAST:event_delPrevButtonActionPerformed

    private void clearFormButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearFormButtonActionPerformed
        // TODO add your handling code here:

        if(Util.askConfirmation("Are you sure you want to clear the form?", "Confirmation")){
            clearForm();
        }
    }//GEN-LAST:event_clearFormButtonActionPerformed

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        // TODO add your handling code here:
        if(add()){
        refreshQueueCount();
        Util.showMessage("Invoice berhasil dimasukkan ke export queue", "Success");
        }
    }//GEN-LAST:event_addButtonActionPerformed

    private void generateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateButtonActionPerformed
        // TODO add your handling code here:
        generate();
        refreshQueueCount();
    }//GEN-LAST:event_generateButtonActionPerformed

    private void dppFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dppFieldActionPerformed
        // TODO add your handling code here:
        calculate();
    }//GEN-LAST:event_dppFieldActionPerformed

    private void dateFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dateFieldActionPerformed
        // TODO add your handling code here:
        parseDate();
    }//GEN-LAST:event_dateFieldActionPerformed

    private void generateExcelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateExcelButtonActionPerformed
        // TODO add your handling code here:
        pasteField.setText(parser.generate());
    }//GEN-LAST:event_generateExcelButtonActionPerformed

    
    public void parseDate(){
        parseDate(dateField.getText());
    }
    public void parseDate(String date){
        int[] date1 = Util.parseDate(date);
        if(date1[1] > 0) periodField.setText(String.valueOf(date1[1]));
        if(date1[2] > 0) yearField.setText(String.valueOf(date1[2]));
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    public javax.swing.JTextArea addressField;
    private javax.swing.JButton calculateButton;
    private javax.swing.JButton clearFormButton;
    private javax.swing.JButton clearQueueButton;
    public javax.swing.JTextField dateField;
    private javax.swing.JButton delPrevButton;
    public javax.swing.JTextField dppField;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.JButton findNPWPButton;
    private javax.swing.JButton findNameButton;
    private javax.swing.JButton generateButton;
    private javax.swing.JButton generateExcelButton;
    public javax.swing.JCheckBox indoCommaCheckBox;
    public javax.swing.JTextField invoiceNoField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    public javax.swing.JTextField nameField;
    public javax.swing.JTextField npwpField;
    private javax.swing.JButton parseButton;
    public javax.swing.JTextArea pasteField;
    public javax.swing.JTextField periodField;
    public javax.swing.JTextField ppnField;
    private java.awt.Label queueCountLabel;
    public javax.swing.JTextField yearField;
    // End of variables declaration//GEN-END:variables
}
