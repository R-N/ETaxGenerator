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
        int ppnCalc = getPPn();
        System.out.println("getppn " + ppnCalc);
        System.out.println("ppn " + ppn);
        int diff = Util.abs(ppnCalc-ppn);
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
    
    public int getPPn(){
        return Invoice.getPPn(
            Util.parseInt(dppField.getText()),
            Util.parseDouble(ppnPercentField.getText())
        );
    }
    
    public void setPPn(){
        ppnField.setText(Util.formatNumber(getPPn()));
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
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel1 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel7 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        nameField = new javax.swing.JTextField();
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
        jLabel11 = new javax.swing.JLabel();
        dateField = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        periodField = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        yearField = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        dppField = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        ppnPercentField = new javax.swing.JTextField();
        ppnField = new javax.swing.JTextField();
        calculateButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        pasteField = new javax.swing.JTextArea();
        jPanel6 = new javax.swing.JPanel();
        dppPPNCheckBox = new javax.swing.JCheckBox();
        dppWorkaroundCheckBox = new javax.swing.JCheckBox();
        ppnPercentField0 = new javax.swing.JTextField();
        indoCommaCheckBox = new javax.swing.JCheckBox();
        parseButton = new javax.swing.JButton();
        generateExcelButton = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        clearFormButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        queueCountLabel = new javax.swing.JLabel();
        clearQueueButton = new javax.swing.JButton();
        delPrevButton = new javax.swing.JButton();
        addButton = new javax.swing.JButton();
        generateButton = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("Faktur Masukan");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Faktur Masukan - ETaxGenerator");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jSeparator2.setMaximumSize(new java.awt.Dimension(32767, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(jSeparator2, gridBagConstraints);

        jPanel7.setLayout(new java.awt.GridBagLayout());

        jLabel4.setText("Nama");
        jLabel4.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel7.add(jLabel4, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel1.add(nameField, gridBagConstraints);

        findNameButton.setText("Find");
        findNameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findNameButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
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
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel7.add(jLabel3, gridBagConstraints);

        jPanel3.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
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
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel7.add(jLabel5, gridBagConstraints);

        jScrollPane4.setMinimumSize(new java.awt.Dimension(64, 27));

        addressField.setColumns(20);
        addressField.setLineWrap(true);
        addressField.setRows(1);
        addressField.setMinimumSize(new java.awt.Dimension(64, 27));
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
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel7.add(jLabel6, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel7.add(invoiceNoField, gridBagConstraints);

        jLabel11.setText("Tanggal");
        jLabel11.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel11.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel7.add(jLabel11, gridBagConstraints);

        dateField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dateFieldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel7.add(dateField, gridBagConstraints);

        jLabel16.setText("Masa Pajak:");
        jLabel16.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel16.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel7.add(jLabel16, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel7.add(periodField, gridBagConstraints);

        jLabel17.setText("Tahun Pajak:");
        jLabel17.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel17.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel7.add(jLabel17, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel7.add(yearField, gridBagConstraints);

        jLabel15.setText("DPP :");
        jLabel15.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel15.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel7.add(jLabel15, gridBagConstraints);

        dppField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dppFieldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel7.add(dppField, gridBagConstraints);

        jLabel14.setText("PPN : ");
        jLabel14.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel14.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel7.add(jLabel14, gridBagConstraints);

        jPanel4.setLayout(new java.awt.GridBagLayout());

        ppnPercentField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        ppnPercentField.setText("0.12");
        ppnPercentField.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel4.add(ppnPercentField, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel4.add(ppnField, gridBagConstraints);

        calculateButton.setText("Calculate");
        calculateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calculateButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel4.add(calculateButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel7.add(jPanel4, gridBagConstraints);

        jLabel2.setText("Paste Excel");
        jLabel2.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel7.add(jLabel2, gridBagConstraints);

        jScrollPane2.setMinimumSize(new java.awt.Dimension(64, 27));

        pasteField.setColumns(20);
        pasteField.setLineWrap(true);
        pasteField.setRows(1);
        pasteField.setMinimumSize(new java.awt.Dimension(64, 27));
        jScrollPane2.setViewportView(pasteField);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel7.add(jScrollPane2, gridBagConstraints);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 4, 4));

        dppPPNCheckBox.setSelected(true);
        dppPPNCheckBox.setText("DPP + PPN");
        dppPPNCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dppPPNCheckBoxActionPerformed(evt);
            }
        });
        jPanel6.add(dppPPNCheckBox);

        dppWorkaroundCheckBox.setSelected(true);
        dppWorkaroundCheckBox.setText("DPP Workaround");
        dppWorkaroundCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dppWorkaroundCheckBoxActionPerformed(evt);
            }
        });
        jPanel6.add(dppWorkaroundCheckBox);

        ppnPercentField0.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        ppnPercentField0.setText("0.11");
        ppnPercentField0.setToolTipText("");
        jPanel6.add(ppnPercentField0);

        indoCommaCheckBox.setText("Indo Comma");
        indoCommaCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                indoCommaCheckBoxActionPerformed(evt);
            }
        });
        jPanel6.add(indoCommaCheckBox);

        parseButton.setText("Parse");
        parseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                parseButtonActionPerformed(evt);
            }
        });
        jPanel6.add(parseButton);

        generateExcelButton.setText("Generate");
        generateExcelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateExcelButtonActionPerformed(evt);
            }
        });
        jPanel6.add(generateExcelButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        jPanel7.add(jPanel6, gridBagConstraints);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 4, 4));

        clearFormButton.setText("Clear Form");
        clearFormButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearFormButtonActionPerformed(evt);
            }
        });
        jPanel8.add(clearFormButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        jPanel7.add(jPanel8, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(jPanel7, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabel7.setText("Queue");
        jLabel7.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel7.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel2.add(jLabel7, gridBagConstraints);

        queueCountLabel.setText("Count: 0");
        queueCountLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        queueCountLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel2.add(queueCountLabel, gridBagConstraints);

        clearQueueButton.setText("Clear Queue");
        clearQueueButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearQueueButtonActionPerformed(evt);
            }
        });
        jPanel2.add(clearQueueButton, new java.awt.GridBagConstraints());

        delPrevButton.setText("Del Prev");
        delPrevButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delPrevButtonActionPerformed(evt);
            }
        });
        jPanel2.add(delPrevButton, new java.awt.GridBagConstraints());

        addButton.setText("Add");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });
        jPanel2.add(addButton, new java.awt.GridBagConstraints());

        generateButton.setText("Generate");
        generateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateButtonActionPerformed(evt);
            }
        });
        jPanel2.add(generateButton, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(jPanel2, gridBagConstraints);

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
        setPPn();
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
        setPPn();
    }//GEN-LAST:event_dppFieldActionPerformed

    private void dateFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dateFieldActionPerformed
        // TODO add your handling code here:
        parseDate();
    }//GEN-LAST:event_dateFieldActionPerformed

    private void generateExcelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateExcelButtonActionPerformed
        // TODO add your handling code here:
        pasteField.setText(parser.generate());
    }//GEN-LAST:event_generateExcelButtonActionPerformed

    private void dppPPNCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dppPPNCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dppPPNCheckBoxActionPerformed

    private void dppWorkaroundCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dppWorkaroundCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dppWorkaroundCheckBoxActionPerformed

    
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
    public javax.swing.JCheckBox dppPPNCheckBox;
    public javax.swing.JCheckBox dppWorkaroundCheckBox;
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
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator2;
    public javax.swing.JTextField nameField;
    public javax.swing.JTextField npwpField;
    private javax.swing.JButton parseButton;
    public javax.swing.JTextArea pasteField;
    public javax.swing.JTextField periodField;
    public javax.swing.JTextField ppnField;
    public javax.swing.JTextField ppnPercentField;
    public javax.swing.JTextField ppnPercentField0;
    private javax.swing.JLabel queueCountLabel;
    public javax.swing.JTextField yearField;
    // End of variables declaration//GEN-END:variables
}
