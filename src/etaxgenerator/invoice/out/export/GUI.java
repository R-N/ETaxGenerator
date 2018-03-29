/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etaxgenerator.invoice.out.export;

import etaxgenerator.util.Util;
import etaxgenerator.util.ExcelAdapter;
import etaxgenerator.counterparty.Counterparty;
import etaxgenerator.invoice.out.Invoice;
import etaxgenerator.invoice.out.Item;
import etaxgenerator.util.Config;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author MojoMacW7
 */
public class GUI extends javax.swing.JFrame {

    /**
     * Creates new form Table
     */
    FormReader reader = new FormReader(this);
    Builder builder = new Builder();
    DefaultTableModel itemTableModel;
    PasteParser parser;
    Object[] itemTableColumnNames;
    public static DecimalFormat df = (DecimalFormat)NumberFormat.getNumberInstance(Util.usLocale);
    public static DecimalFormat dfIndo = (DecimalFormat)NumberFormat.getNumberInstance(Util.idLocale);
    public void clearForm(){
        customerField.setText("");
        npwpField.setText(Util.nullNPWP);
        addressField.setText("");
        revField.setText("0");
        dateField.setText(Util.getToday());
        dpField.setText("");
        pasteField.setText("");
        itemTable.setModel(new DefaultTableModel(itemTableColumnNames, 1));
        if(Config.lastInvoiceNo==0){
            invoiceNoField.setText("");
        }else{
            invoiceNoField.setText(Config.lastInvoiceNoPrefix + String.valueOf(Config.lastInvoiceNo+1));
        }
        refreshItemCount();
        calculate();
    }
    public ExcelAdapter itemTableAdapter = null;
    public GUI() {
        initComponents();
        Config.init();
        indoCommaCheckBox.setSelected(Config.indoComma);
        df.applyPattern("###,##0.00");
        dfIndo.applyPattern("###,##0.00");
        itemTableModel = (DefaultTableModel)itemTable.getModel();
        int colCount = itemTable.getColumnCount();
        itemTableColumnNames = new Object[colCount];
        for (int i = 0; i < colCount; ++i){
            itemTableColumnNames[i] = itemTable.getColumnName(i);
        }
        jbInit(invoiceTable);
        itemTableAdapter = jbInit(itemTable);
        parser = new PasteParser(this, pasteField);
        clearForm();
    }
    
    public ExcelAdapter jbInit(javax.swing.JTable jTable){
        jTable.setCellSelectionEnabled(true);
        return new ExcelAdapter(jTable);
    }
    
    public void load(Counterparty cp){
        if(cp == null){
            return;
        }
        npwpField.setText(cp.getFormattedNPWP());
        customerField.setText(cp.getCapsName());
        addressField.setText(cp.getFullAddress());
    }
    
    public boolean check(){
        if(Util.isNullOrEmpty(customerField.getText())){
            Util.showError("Nama customer harus diisi", "Error");
            return false;
        }
        if(Util.isNullEmptyOrZero(invoiceNoField.getText())){
            Util.showError("Nomor faktur harus diisi dan tidak boleh nol", "Error");
            return false;
        }
        if(Util.isNullOrEmpty(addressField.getText())){
            Util.showError("Alamat harus diisi", "Error");
            return false;
        }
        int[] date = Util.parseDate(dateField.getText());
        if(date.length != 3 || date[0] < 1 || date[1] < 1 || date[2] < 1){
            Util.showError("Format tanggal salah. Seharusnya DD/MM/YYYY dan tidak boleh nol", "Error");
            return false;
        }
        int rc = itemTable.getRowCount();
        if(rc == 0){
            Util.showError("Item tidak boleh kosong", "Error");
            return false;
        }
        int sum = 0;
        DefaultTableModel dtm = (DefaultTableModel)itemTable.getModel();
        for (int i = 0; i < rc; ++i){
            sum+= Util.parseInt((String)dtm.getValueAt(i, 1));
        }
        if(sum == 0){
            Util.showError("Item tidak boleh kosong", "Error");
            return false;
        }
        return true;
    }
    
    public boolean add(){
        if (!check()){
            return false;
        }
        calculate();
        String sLastInvoiceNo = Util.trim(invoiceNoField.getText());
        Config.setLastInvoiceNo(sLastInvoiceNo);
        builder.add(reader.readInvoice());
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
        Util.showMessage(String.format("%d/%d faktur pajak keluaran berhasil diexport", y, x), "Success");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    
    public void refreshQueueCount(){
        queueCountLabel.setText("Count: " + String.valueOf(builder.size()));
    }
    
    public void refreshItemCount(){
        itemCountField.setText(String.valueOf(itemTable.getRowCount()));
    }
    public void calculate(){
        int rows = itemTable.getRowCount();
        double dpp = 0;
        double ppn = 0;
        for (int i = 0; i < rows; ++i){
            Item it = reader.readItem(i, null);
            if(it != null){
                dpp+=it.getTotal();
                ppn += it.getPPn();
            }
        }
        String sDPP;
        String sPPn;
        sDPP = Util.formatNumber(dpp);
        sPPn = Util.formatNumber(ppn);
        dppLabel.setText(sDPP);
        ppnLabel.setText(sPPn);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel10 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        invoiceTable = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        customerField = new javax.swing.JTextField();
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
        jLabel7 = new javax.swing.JLabel();
        revField = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        dateField = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        dpField = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        itemTable = new javax.swing.JTable();
        jPanel13 = new javax.swing.JPanel();
        deleteRowButton = new javax.swing.JButton();
        insertRowButton = new javax.swing.JButton();
        reduceItemCountButton = new javax.swing.JButton();
        itemCountField = new javax.swing.JTextField();
        increaseItemCountButton = new javax.swing.JButton();
        moveRowUpButton = new javax.swing.JButton();
        moveRowDownButton = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        dppLabel = new javax.swing.JLabel();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));
        jLabel14 = new javax.swing.JLabel();
        ppnLabel = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
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

        jPanel10.setLayout(new javax.swing.BoxLayout(jPanel10, javax.swing.BoxLayout.LINE_AXIS));

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel8.setText("Tanggal");
        jLabel8.setPreferredSize(new java.awt.Dimension(80, 15));
        jPanel10.add(jLabel8);
        jPanel10.add(jTextField5);

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel9.setText("Bulan");
        jLabel9.setPreferredSize(new java.awt.Dimension(40, 15));
        jPanel10.add(jLabel9);
        jPanel10.add(jTextField6);

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel10.setText("Tahun");
        jLabel10.setPreferredSize(new java.awt.Dimension(40, 15));
        jPanel10.add(jLabel10);
        jPanel10.add(jTextField7);

        invoiceTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Tanggal", "Tipe", "Sales", "Pengirim", "Customer", "Prefix", "No. PO", "Tgl PO", "Proyek", "Item", "Qty", "Harga", "Subtotal1", "Subtotal2", "Diskon", "PPn", "Total", "No. Faktur"
            }
        ));
        jScrollPane1.setViewportView(invoiceTable);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(452, 600));
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.PAGE_AXIS));

        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("Faktur Keluaran");
        jPanel4.add(jLabel1);

        getContentPane().add(jPanel4);

        jPanel7.setLayout(new javax.swing.BoxLayout(jPanel7, javax.swing.BoxLayout.LINE_AXIS));

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setText("Customer");
        jLabel4.setPreferredSize(new java.awt.Dimension(80, 15));
        jPanel7.add(jLabel4);
        jPanel7.add(customerField);

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

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText("Rev");
        jLabel7.setPreferredSize(new java.awt.Dimension(30, 15));
        jPanel9.add(jLabel7);

        revField.setText("0");
        revField.setMaximumSize(new java.awt.Dimension(100, 2147483647));
        revField.setPreferredSize(new java.awt.Dimension(40, 20));
        jPanel9.add(revField);

        getContentPane().add(jPanel9);

        jPanel11.setLayout(new javax.swing.BoxLayout(jPanel11, javax.swing.BoxLayout.LINE_AXIS));

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel11.setText("Tanggal");
        jLabel11.setPreferredSize(new java.awt.Dimension(80, 15));
        jPanel11.add(jLabel11);
        jPanel11.add(dateField);

        getContentPane().add(jPanel11);

        jPanel12.setLayout(new javax.swing.BoxLayout(jPanel12, javax.swing.BoxLayout.LINE_AXIS));

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel12.setText("DP");
        jLabel12.setPreferredSize(new java.awt.Dimension(80, 15));
        jPanel12.add(jLabel12);
        jPanel12.add(dpField);

        getContentPane().add(jPanel12);

        itemTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null}
            },
            new String [] {
                "Item", "Qty", "Harga", "Diskon Persen", "Diskon Value"
            }
        ));
        jScrollPane3.setViewportView(itemTable);

        getContentPane().add(jScrollPane3);

        jPanel13.setLayout(new javax.swing.BoxLayout(jPanel13, javax.swing.BoxLayout.LINE_AXIS));

        deleteRowButton.setText("Delete");
        deleteRowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteRowButtonActionPerformed(evt);
            }
        });
        jPanel13.add(deleteRowButton);

        insertRowButton.setText("Insert");
        insertRowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertRowButtonActionPerformed(evt);
            }
        });
        jPanel13.add(insertRowButton);

        reduceItemCountButton.setText("-");
        reduceItemCountButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reduceItemCountButtonActionPerformed(evt);
            }
        });
        jPanel13.add(reduceItemCountButton);

        itemCountField.setText("1");
        itemCountField.setMaximumSize(new java.awt.Dimension(40, 2147483647));
        itemCountField.setMinimumSize(new java.awt.Dimension(40, 20));
        itemCountField.setPreferredSize(new java.awt.Dimension(40, 20));
        itemCountField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemCountFieldActionPerformed(evt);
            }
        });
        jPanel13.add(itemCountField);

        increaseItemCountButton.setText("+");
        increaseItemCountButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                increaseItemCountButtonActionPerformed(evt);
            }
        });
        jPanel13.add(increaseItemCountButton);

        moveRowUpButton.setText("Move Up");
        moveRowUpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveRowUpButtonActionPerformed(evt);
            }
        });
        jPanel13.add(moveRowUpButton);

        moveRowDownButton.setText("Move Down");
        moveRowDownButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveRowDownButtonActionPerformed(evt);
            }
        });
        jPanel13.add(moveRowDownButton);

        getContentPane().add(jPanel13);

        jPanel14.setLayout(new javax.swing.BoxLayout(jPanel14, javax.swing.BoxLayout.LINE_AXIS));

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel15.setText("DPP :");
        jLabel15.setPreferredSize(new java.awt.Dimension(80, 15));
        jPanel14.add(jLabel15);

        dppLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        dppLabel.setText("0");
        dppLabel.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        dppLabel.setMaximumSize(new java.awt.Dimension(20000, 14));
        jPanel14.add(dppLabel);
        jPanel14.add(filler3);

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel14.setText("PPN : ");
        jLabel14.setPreferredSize(new java.awt.Dimension(80, 15));
        jPanel14.add(jLabel14);

        ppnLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        ppnLabel.setText("0");
        ppnLabel.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppnLabel.setMaximumSize(new java.awt.Dimension(20000, 14));
        jPanel14.add(ppnLabel);

        jButton1.setText("Calculate");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel14.add(jButton1);

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

    private void clearQueueButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearQueueButtonActionPerformed
        // TODO add your handling code here:
        if(Util.askConfirmation("Are you sure you want to clear the export queue?", "Confirmation")){
            builder.clear();
            refreshQueueCount();
        }
    }//GEN-LAST:event_clearQueueButtonActionPerformed

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
    }//GEN-LAST:event_generateButtonActionPerformed

    private void deleteRowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteRowButtonActionPerformed
        // TODO add your handling code here:
        itemTableAdapter.deleteSelectedRows();
        refreshItemCount();
    }//GEN-LAST:event_deleteRowButtonActionPerformed

    private void insertRowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertRowButtonActionPerformed
        // TODO add your handling code here:
        itemTableAdapter.insertSelectedRows();
        refreshItemCount();
    }//GEN-LAST:event_insertRowButtonActionPerformed

    private void reduceItemCountButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reduceItemCountButtonActionPerformed
        // TODO add your handling code here:
        itemTableAdapter.reduceRows();
        refreshItemCount();
    }//GEN-LAST:event_reduceItemCountButtonActionPerformed

    private void itemCountFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemCountFieldActionPerformed
        // TODO add your handling code here:
        itemTableAdapter.setRowCount(Util.parseInt(itemCountField.getText()));
    }//GEN-LAST:event_itemCountFieldActionPerformed

    private void increaseItemCountButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_increaseItemCountButtonActionPerformed
        // TODO add your handling code here:
        itemTableAdapter.addRows();
        refreshItemCount();
    }//GEN-LAST:event_increaseItemCountButtonActionPerformed

    private void moveRowUpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveRowUpButtonActionPerformed
        // TODO add your handling code here:
        itemTableAdapter.moveSelectedRowsUp();
    }//GEN-LAST:event_moveRowUpButtonActionPerformed

    private void moveRowDownButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveRowDownButtonActionPerformed
        // TODO add your handling code here:
        itemTableAdapter.moveSelectedRowsDown();
        
    }//GEN-LAST:event_moveRowDownButtonActionPerformed

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

    private void parseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_parseButtonActionPerformed
        // TODO add your handling code here:
        parser.parse(pasteField.getText());
    }//GEN-LAST:event_parseButtonActionPerformed

    private void findNameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findNameButtonActionPerformed
        // TODO add your handling code here:
        String name = customerField.getText().trim();
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
        double dp = Util.parseDouble(dpField.getText());
        int rc = itemTable.getRowCount();
        DefaultTableModel dtm = (DefaultTableModel) itemTable.getModel();
        double[][] temp = new double[rc][];
        for(int i = 0; i < rc; ++i){
            temp[i] = new double[4];
            temp[i][0] = Util.parseDouble((String)dtm.getValueAt(i, 1));
            temp[i][1] = Util.parseDouble((String)dtm.getValueAt(i, 2));
            temp[i][2] = Util.parseDouble((String)dtm.getValueAt(i, 3));
            temp[i][3] = Util.parseDouble((String)dtm.getValueAt(i, 4));
        }
        Config.setIndoComma(indoCommaCheckBox.isSelected());
        if(dp > 0) dpField.setText(Util.formatNumber(dp));
        for(int i = 0; i < rc; ++i){
            if(temp[i][0] > 0 ) dtm.setValueAt(Util.formatNumber(temp[i][0]), i, 1);
            if(temp[i][1] > 0 ) dtm.setValueAt(Util.formatNumber(temp[i][1]), i, 2);
            if(temp[i][2] > 0 ) dtm.setValueAt(Util.formatNumber(temp[i][2]), i, 3);
            if(temp[i][3] > 0 ) dtm.setValueAt(Util.formatNumber(temp[i][3]), i, 4);
        }
        calculate();
    }//GEN-LAST:event_indoCommaCheckBoxActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        calculate();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void generateExcelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateExcelButtonActionPerformed
        // TODO add your handling code here:
        pasteField.setText(parser.generate());
    }//GEN-LAST:event_generateExcelButtonActionPerformed

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
    private javax.swing.JButton clearFormButton;
    private javax.swing.JButton clearQueueButton;
    public javax.swing.JTextField customerField;
    public javax.swing.JTextField dateField;
    private javax.swing.JButton delPrevButton;
    public javax.swing.JButton deleteRowButton;
    public javax.swing.JTextField dpField;
    public javax.swing.JLabel dppLabel;
    private javax.swing.Box.Filler filler3;
    private javax.swing.JButton findNPWPButton;
    private javax.swing.JButton findNameButton;
    private javax.swing.JButton generateButton;
    private javax.swing.JButton generateExcelButton;
    private javax.swing.JButton increaseItemCountButton;
    public javax.swing.JCheckBox indoCommaCheckBox;
    public javax.swing.JButton insertRowButton;
    public javax.swing.JTextField invoiceNoField;
    private javax.swing.JTable invoiceTable;
    public javax.swing.JTextField itemCountField;
    public javax.swing.JTable itemTable;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    public javax.swing.JButton moveRowDownButton;
    public javax.swing.JButton moveRowUpButton;
    public javax.swing.JTextField npwpField;
    private javax.swing.JButton parseButton;
    public javax.swing.JTextArea pasteField;
    public javax.swing.JLabel ppnLabel;
    private java.awt.Label queueCountLabel;
    public javax.swing.JButton reduceItemCountButton;
    public javax.swing.JTextField revField;
    // End of variables declaration//GEN-END:variables
}
