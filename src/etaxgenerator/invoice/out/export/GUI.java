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
import java.util.ArrayList;
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

    boolean ppnCalculated = false;
    double preRetensi = 0;
    
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
        preRetensi = 0;
        ppnCalculated = false;
        calculateRetensiButton.setEnabled(true);
        applyRetensiButton.setEnabled(true);
        calculateRetensi();
        calculatePPn();
    }
    public ExcelAdapter itemTableAdapter = null;
    public GUI() {
        initComponents();
        indoCommaCheckBox.setSelected(Config.indoComma);
        df.applyPattern("###,##0.0#");
        dfIndo.applyPattern("###,##0.0#");
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
        if (!ppnCalculated){
            calculatePPn();
        }
        String sLastInvoiceNo = Util.trim(invoiceNoField.getText());
        Config.setLastInvoiceNo(sLastInvoiceNo);
        Invoice in = reader.readInvoice();
        builder.add(in);
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
    
    public double getTotal(){
        return getTotal(reader.readInvoice());
    }
    
    public double getTotal(Invoice in){
        return in.getTotalTotal();
    }
    
    public void calculatePPn(){
        Invoice in = reader.readInvoice();
        double dpp = in.getTotalTotal();
        double ppn = in.getTotalPPn();
        double ppnDP = in.getPPnDP();
        dppLabel.setText(Util.formatNumber(dpp));
        ppnField.setText(Util.formatNumber(ppn));
        ppnDPField.setText(Util.formatNumber(ppnDP));
        ppnCalculated = true;
    }
    
    public void calculateRetensi(){
        if(this.preRetensi == 0){
            this.calculateRetensi1();
        }else{
            this.calculateRetensi2();
        }
    }
    
    public void calculateRetensi1(){
        Invoice in = reader.readInvoice();
        double dpr = in.getTotalTotal();
        double retensi = in.getTotalRetensi();
        dprLabel.setText(Util.formatNumber(dpr));
        retensiLabel.setText(Util.formatNumber(retensi));
    }
    
    public void calculateRetensi2(){
        Invoice in = reader.readInvoice();
        double dpr = this.preRetensi;
        double retensi = dpr - in.getTotalTotal();
        dprLabel.setText(Util.formatNumber(dpr));
        retensiLabel.setText(Util.formatNumber(retensi));
    }

    public void applyRetensi(){
        double retensiPercent = Util.parseDouble(retensiPercentField.getText());
        int rows = itemTable.getRowCount();
        double dpr = 0;
        Item it = null;
        for (int i = 0; i < rows; ++i){
            it = reader.readItem(i, it);
            if(it != null){
                dpr += it.getTotal();
                it.applyRetensi(retensiPercent);
                itemTable.setValueAt(Util.formatNumber(it.price), i, 2);
            }
        }
        this.preRetensi = dpr;
        this.calculateRetensi();
        applyRetensiButton.setEnabled(false);
        calculatePPn();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel10 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        invoiceTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel7 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        customerField = new javax.swing.JTextField();
        findNameButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        npwpField = new javax.swing.JTextField();
        findNPWPButton = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        addressField = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        invoiceNoField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        revField = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        dateField = new javax.swing.JTextField();
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
        jPanel15 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        dprLabel = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        retensiPercentField = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        retensiLabel = new javax.swing.JLabel();
        calculateRetensiButton = new javax.swing.JButton();
        applyRetensiButton = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        dppLabel = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        ppnPercentField = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        ppnField = new javax.swing.JTextField();
        calculatePPnButton = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        dpField = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));
        jLabel22 = new javax.swing.JLabel();
        ppnDPField = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        pasteField = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        indoCommaCheckBox = new javax.swing.JCheckBox();
        parseButton = new javax.swing.JButton();
        generateExcelButton = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        clearFormButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        queueCountLabel = new javax.swing.JLabel();
        clearQueueButton = new javax.swing.JButton();
        delPrevButton = new javax.swing.JButton();
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

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("Faktur Keluaran");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Faktur Keluaran - ETaxGenerator");
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jSeparator2.setMaximumSize(new java.awt.Dimension(32767, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(jSeparator2, gridBagConstraints);

        jPanel7.setLayout(new java.awt.GridBagLayout());

        jLabel4.setText("Customer");
        jLabel4.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        jLabel4.setPreferredSize(new java.awt.Dimension(80, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel7.add(jLabel4, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel1.add(customerField, gridBagConstraints);

        findNameButton.setText("Find");
        findNameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findNameButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel1.add(findNameButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel7.add(jPanel1, gridBagConstraints);

        jLabel3.setText("NPWP");
        jLabel3.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        jLabel3.setPreferredSize(new java.awt.Dimension(80, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel7.add(jLabel3, gridBagConstraints);

        jPanel3.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel3.add(npwpField, gridBagConstraints);

        findNPWPButton.setText("Find");
        findNPWPButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findNPWPButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel3.add(findNPWPButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel7.add(jPanel3, gridBagConstraints);

        jLabel5.setText("Alamat");
        jLabel5.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel7.add(jLabel5, gridBagConstraints);

        jScrollPane4.setMinimumSize(null);

        addressField.setColumns(20);
        addressField.setLineWrap(true);
        addressField.setRows(1);
        jScrollPane4.setViewportView(addressField);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel7.add(jScrollPane4, gridBagConstraints);

        jLabel6.setText("No. Faktur");
        jLabel6.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel6.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        jLabel6.setPreferredSize(new java.awt.Dimension(80, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel7.add(jLabel6, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel7.add(invoiceNoField, gridBagConstraints);

        jLabel7.setText("Rev");
        jLabel7.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel7.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        jLabel7.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel7.add(jLabel7, gridBagConstraints);

        revField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        revField.setText("0");
        revField.setMaximumSize(null);
        revField.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel7.add(revField, gridBagConstraints);

        jLabel11.setText("Tanggal");
        jLabel11.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel11.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        jLabel11.setPreferredSize(new java.awt.Dimension(80, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel7.add(jLabel11, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel7.add(dateField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.4;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(jPanel7, gridBagConstraints);

        jScrollPane3.setMinimumSize(new java.awt.Dimension(64, 64));

        itemTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null}
            },
            new String [] {
                "Item", "Qty", "Harga", "Diskon Persen", "Diskon Value"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class, java.lang.Object.class, java.lang.Double.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        itemTable.setMinimumSize(new java.awt.Dimension(64, 64));
        jScrollPane3.setViewportView(itemTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        getContentPane().add(jScrollPane3, gridBagConstraints);

        jPanel13.setLayout(new java.awt.GridBagLayout());

        deleteRowButton.setText("Delete");
        deleteRowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteRowButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel13.add(deleteRowButton, gridBagConstraints);

        insertRowButton.setText("Insert");
        insertRowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertRowButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel13.add(insertRowButton, gridBagConstraints);

        reduceItemCountButton.setText("-");
        reduceItemCountButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reduceItemCountButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel13.add(reduceItemCountButton, gridBagConstraints);

        itemCountField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        itemCountField.setText("1");
        itemCountField.setMaximumSize(new java.awt.Dimension(40, 2147483647));
        itemCountField.setMinimumSize(new java.awt.Dimension(40, 20));
        itemCountField.setPreferredSize(new java.awt.Dimension(40, 20));
        itemCountField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemCountFieldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel13.add(itemCountField, gridBagConstraints);

        increaseItemCountButton.setText("+");
        increaseItemCountButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                increaseItemCountButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel13.add(increaseItemCountButton, gridBagConstraints);

        moveRowUpButton.setText("Move Up");
        moveRowUpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveRowUpButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel13.add(moveRowUpButton, gridBagConstraints);

        moveRowDownButton.setText("Move Down");
        moveRowDownButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveRowDownButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel13.add(moveRowDownButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(jPanel13, gridBagConstraints);

        jPanel15.setLayout(new java.awt.GridBagLayout());

        jLabel17.setText("Retensi :");
        jLabel17.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel17.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel15.add(jLabel17, gridBagConstraints);

        dprLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        dprLabel.setText("0");
        dprLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        dprLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        dprLabel.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel15.add(dprLabel, gridBagConstraints);

        jLabel18.setText("x");
        jLabel18.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel18.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel15.add(jLabel18, gridBagConstraints);

        retensiPercentField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        retensiPercentField.setText("0");
        retensiPercentField.setToolTipText("");
        retensiPercentField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                retensiPercentFieldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel15.add(retensiPercentField, gridBagConstraints);

        jLabel19.setText("=");
        jLabel19.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel19.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel15.add(jLabel19, gridBagConstraints);

        retensiLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        retensiLabel.setText("0");
        retensiLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        retensiLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        retensiLabel.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel15.add(retensiLabel, gridBagConstraints);

        calculateRetensiButton.setText("Calc");
        calculateRetensiButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calculateRetensiButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel15.add(calculateRetensiButton, gridBagConstraints);

        applyRetensiButton.setText("Apply");
        applyRetensiButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyRetensiButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel15.add(applyRetensiButton, gridBagConstraints);

        jLabel15.setText("DPP :");
        jLabel15.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel15.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel15.add(jLabel15, gridBagConstraints);

        dppLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        dppLabel.setText("0");
        dppLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        dppLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        dppLabel.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel15.add(dppLabel, gridBagConstraints);

        jLabel13.setText("PPN");
        jLabel13.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel13.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel15.add(jLabel13, gridBagConstraints);

        ppnPercentField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        ppnPercentField.setText("0.11");
        ppnPercentField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppnPercentFieldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel15.add(ppnPercentField, gridBagConstraints);

        jLabel14.setText("=");
        jLabel14.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel14.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel15.add(jLabel14, gridBagConstraints);

        ppnField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        ppnField.setText("0");
        ppnField.setToolTipText("Silahkan ubah penyesuaian ke bilangan bulat");
        ppnField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppnFieldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel15.add(ppnField, gridBagConstraints);

        calculatePPnButton.setText("Calculate");
        calculatePPnButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calculatePPnButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel15.add(calculatePPnButton, gridBagConstraints);

        jLabel20.setText("DP :");
        jLabel20.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel20.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel15.add(jLabel20, gridBagConstraints);

        dpField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        dpField.setText("0");
        dpField.setToolTipText("");
        dpField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dpFieldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel15.add(dpField, gridBagConstraints);

        jLabel21.setText("PPN");
        jLabel21.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel21.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel15.add(jLabel21, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        jPanel15.add(filler1, gridBagConstraints);

        jLabel22.setText("=");
        jLabel22.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel22.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel15.add(jLabel22, gridBagConstraints);

        ppnDPField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        ppnDPField.setText("0");
        ppnDPField.setToolTipText("Silahkan ubah penyesuaian ke bilangan bulat");
        ppnDPField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppnDPFieldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel15.add(ppnDPField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(jPanel15, gridBagConstraints);

        jPanel5.setLayout(new java.awt.GridBagLayout());

        jLabel2.setText("Paste Excel");
        jLabel2.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel5.add(jLabel2, gridBagConstraints);

        jScrollPane2.setMinimumSize(new java.awt.Dimension(64, 27));

        pasteField.setColumns(20);
        pasteField.setLineWrap(true);
        pasteField.setRows(1);
        jScrollPane2.setViewportView(pasteField);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel5.add(jScrollPane2, gridBagConstraints);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 4, 4));

        indoCommaCheckBox.setText("Indo Comma");
        indoCommaCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                indoCommaCheckBoxActionPerformed(evt);
            }
        });
        jPanel4.add(indoCommaCheckBox);

        parseButton.setText("Parse");
        parseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                parseButtonActionPerformed(evt);
            }
        });
        jPanel4.add(parseButton);

        generateExcelButton.setText("Generate");
        generateExcelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateExcelButtonActionPerformed(evt);
            }
        });
        jPanel4.add(generateExcelButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel5.add(jPanel4, gridBagConstraints);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 4, 4));

        clearFormButton.setText("Clear Form");
        clearFormButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearFormButtonActionPerformed(evt);
            }
        });
        jPanel6.add(clearFormButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        jPanel5.add(jPanel6, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(jPanel5, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabel16.setText("Queue");
        jLabel16.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel16.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel2.add(jLabel16, gridBagConstraints);

        queueCountLabel.setText("Count: 0");
        queueCountLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        queueCountLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 1, 1, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel2.add(queueCountLabel, gridBagConstraints);

        clearQueueButton.setText("Clear Queue");
        clearQueueButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearQueueButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel2.add(clearQueueButton, gridBagConstraints);

        delPrevButton.setText("Del Prev");
        delPrevButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delPrevButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel2.add(delPrevButton, gridBagConstraints);

        addButton.setText("Add");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel2.add(addButton, gridBagConstraints);

        generateButton.setText("Generate");
        generateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel2.add(generateButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(jPanel2, gridBagConstraints);

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
        calculatePPn();
    }//GEN-LAST:event_indoCommaCheckBoxActionPerformed

    private void calculatePPnButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calculatePPnButtonActionPerformed
        // TODO add your handling code here:
        calculatePPn();
    }//GEN-LAST:event_calculatePPnButtonActionPerformed

    private void generateExcelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateExcelButtonActionPerformed
        // TODO add your handling code here:
        pasteField.setText(parser.generate());
    }//GEN-LAST:event_generateExcelButtonActionPerformed

    private void ppnPercentFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppnPercentFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ppnPercentFieldActionPerformed

    private void dpFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dpFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dpFieldActionPerformed

    private void ppnFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppnFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ppnFieldActionPerformed

    private void retensiPercentFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_retensiPercentFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_retensiPercentFieldActionPerformed

    private void calculateRetensiButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calculateRetensiButtonActionPerformed
        // TODO add your handling code here:
        calculateRetensi();
    }//GEN-LAST:event_calculateRetensiButtonActionPerformed

    private void applyRetensiButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyRetensiButtonActionPerformed
        // TODO add your handling code here:
        applyRetensi();
    }//GEN-LAST:event_applyRetensiButtonActionPerformed

    private void ppnDPFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppnDPFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ppnDPFieldActionPerformed

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
    private javax.swing.JButton applyRetensiButton;
    private javax.swing.JButton calculatePPnButton;
    private javax.swing.JButton calculateRetensiButton;
    private javax.swing.JButton clearFormButton;
    private javax.swing.JButton clearQueueButton;
    public javax.swing.JTextField customerField;
    public javax.swing.JTextField dateField;
    private javax.swing.JButton delPrevButton;
    public javax.swing.JButton deleteRowButton;
    public javax.swing.JTextField dpField;
    public javax.swing.JLabel dppLabel;
    public javax.swing.JLabel dprLabel;
    private javax.swing.Box.Filler filler1;
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    public javax.swing.JButton moveRowDownButton;
    public javax.swing.JButton moveRowUpButton;
    public javax.swing.JTextField npwpField;
    private javax.swing.JButton parseButton;
    public javax.swing.JTextArea pasteField;
    public javax.swing.JTextField ppnDPField;
    public javax.swing.JTextField ppnField;
    public javax.swing.JTextField ppnPercentField;
    private javax.swing.JLabel queueCountLabel;
    public javax.swing.JButton reduceItemCountButton;
    public javax.swing.JLabel retensiLabel;
    public javax.swing.JTextField retensiPercentField;
    public javax.swing.JTextField revField;
    // End of variables declaration//GEN-END:variables
}
