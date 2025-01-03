/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etaxgenerator.util;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JOptionPane;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.time.LocalDateTime;
/**
 *
 * @author MojoMacW7
 */
public class Util {
    public static final Locale idLocale = new Locale("in");
    public static final Locale usLocale = Locale.US;
    public static final String nullNPWP = "00.000.000.0-000.000";
    public static void main(String[] args){
     DecimalFormat nf = (DecimalFormat)NumberFormat.getNumberInstance(Util.idLocale);
     long x = 1001;
        nf.applyPattern("000,00000000");
        long head = (x/100000000000L);
        long tail = x-head;
        System.out.println(String.format("%d.%s", head, nf.format(tail)));
    }
    public static int[] parseDate(String date){
        date = formatDate(date);
        String[] date1 = date.split("/");
        int[] ret = new int[3];
        int l = date1.length;
        if (l > 0) ret[0] = Util.parseInt(date1[0]);
        if (l > 1) ret[1] = Util.parseInt(date1[1]);
        if (l > 2) ret[2] = Util.parseInt(date1[2]);
        return ret;
    }
    public static String replace(String s, String what, String replacement){
        
        if(s == null){
            return s;
        }
        return s.replace(what, replacement);
    }
    
    public static String getToday(){
       LocalDateTime now = LocalDateTime.now();
       return String.format("%02d/%02d/%d", now.getDayOfMonth(), now.getMonthValue(), now.getYear());
    }
    
    public static String trim(String s){
        if(s == null){
            return s;
        }
        return s.trim();
    }
    
    public static int[] sort(int[] indexes){
        int l = indexes.length;
        Integer[] arr = new Integer[l];
        for(int i = 0; i < l; ++i){
            arr[i] = indexes[i];
        }
        Arrays.sort(arr);
        int[] ret = new int[l];
        for(int i = 0; i < l; ++i){
            ret[i] = arr[i];
        }
        return ret;
    }
 
    public static Number parseNumber(String s, boolean indoComma) throws ParseException{
        if(s == null){
            return 0;
        }
        s = s.replace(" ", "");
        if(s.isEmpty() || s.equals("-")){
            return 0;
        }
        DecimalFormat df = dfInt;
        if(indoComma){
            df = dfIndoInt;
        }
        df.applyPattern("###,##0.0#");
        return df.parse(s);
    }
    
    public static long parseLong(String s){
        return parseLong(s, Config.indoComma);
    }
    
    public static long parseLong(String s, boolean indoComma){
        if(s == null){
            return 0;
        }
        s = s.replace(" ", "");
        
        try{
            Number n = parseNumber(s, indoComma);
            return n.longValue();
        }catch(ParseException ex){
            log(stackTraceString(ex));
        }
        
        s = s.replace(",", "").replace(".", "");
        if(s.isEmpty() || s.equals("-")){
            return 0;
        }
        try{
            return Long.valueOf(s);
        }catch(Exception ex){
            ex.printStackTrace();
            return 0;
        }
    }
    
    public static int parseInt(String s){
        return parseInt(s, Config.indoComma);
    }
    public static int parseInt(String s, boolean indoComma){
        return (int)parseLong(s, indoComma);
    }
    public static double parseDouble(String s){
        return parseDouble(s, Config.indoComma);
    }
    public static double parseDouble(String s, boolean indoComma){
        if(s == null){
            return 0;
        }
        
        s = s.replace(" ", "");
        
        try{
            Number n = parseNumber(s, indoComma);
            return n.doubleValue();
        }catch(ParseException ex){
            log(stackTraceString(ex));
        }
        
        if(indoComma){
            s = s.replace(".", "").replace(",", ".");
        }else{
            s = s.replace(",", "");
        }
        if(s.isEmpty() || s.equals("-")){
            return 0;
        }
        try{
            return Double.valueOf(s);
        }catch(Exception ex){
            ex.printStackTrace();
            return 0;
        }
    }
    
    
    public static String formatDate(String date){
        return date.replace("-", "/").trim();
    }
    public static String formatDateExcel(String date){
        return date.replace("/", "-").trim();
    }
    
    public static String numericOnly(String s){
        return s.replaceAll("[^0-9]", "");
    }
    
    public static boolean isNullOrEmpty(String s){
        return s == null || s.trim().isEmpty();
    }
    
    public static File chooseCSV(){
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("E-Faktur csv", "csv");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }
        return null;
    }
    public static String capitalize(String s){
        if(isNullOrEmpty(s)){
            return s;
        }
        char[] chars = s.trim().toLowerCase().toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        int l = chars.length;
        boolean prevSpace = false;
        for(int i = 1; i < l; ++i){
            if(prevSpace){
                chars[i] = Character.toUpperCase(chars[i]);
                prevSpace = false;
            }
            if (chars[i] == ' '){
                prevSpace = true;
            }
        }
        return new String(chars);
    }
    public static int abs(int x){
        if (x < 0){
            return -x;
        }
        return x;
    }
    public static boolean isNullEmptyOrZero(String s){
        if(isNullOrEmpty(s)){
            return true;
        }
        long x = parseLong(numericOnly(s));
        if(x == 0){
            return true;
        }
        return false;
    }
    public static DecimalFormat dfDec = (DecimalFormat)NumberFormat.getNumberInstance(Util.usLocale);
    public static DecimalFormat dfIndoDec = (DecimalFormat)NumberFormat.getNumberInstance(Util.idLocale);
    public static DecimalFormat dfInt = (DecimalFormat)NumberFormat.getNumberInstance(Util.usLocale);
    public static DecimalFormat dfIndoInt = (DecimalFormat)NumberFormat.getNumberInstance(Util.idLocale);
    public static String formatNumber(int num){
        return formatNumber(num, Config.indoComma);
    }
    public static String formatNumber(double num){
        return formatNumber(num, Config.indoComma);
    }
    public static String formatNumber(int num, boolean indoComma){
        DecimalFormat df = dfInt;
        if(indoComma){
            df = dfIndoInt;
        }
        df.applyPattern("###,##0.0#");
        return df.format(num);
    }
    static File logFile = null;
    static PrintWriter logWriter = null;
    public static PrintWriter getLogWriter() throws FileNotFoundException, IOException{
        if (logWriter == null){
            if (logFile == null){
                logFile = new File(Config.workDir + "/log.txt");
                if(logFile.exists()){
                    logFile.delete();
                }
                logFile.createNewFile();
            }
            logWriter = new PrintWriter(new FileOutputStream(logFile, true));
        }
        return logWriter;
    }
    
    public static void closeLogWriter(){
        if(logWriter != null){
            logWriter.close();
            logWriter = null;
        }
    }
    
    public static String stackTraceString(Exception ex){
        StringWriter errors = new StringWriter();
        ex.printStackTrace(new PrintWriter(errors));
        return errors.toString();
    }
    
    public static void writeLog(String s){
        try{
            getLogWriter().println(s);
            getLogWriter().flush();
        }catch(Exception ex){
            ex.printStackTrace();
            showError(stackTraceString(ex), ex.getMessage());
        }finally{
            try{
                closeLogWriter();
            }catch(Exception ex){
                ex.printStackTrace();
                showError(stackTraceString(ex), ex.getMessage());
            }
        }
    }
    
    public static void log(String s){
        System.out.println(s);
        writeLog(s);
    }
    
    public static void init(){
        log("ETAXGENERATOR");
        log(LocalDateTime.now().toString());
    }
    public static String formatNumber(double num, boolean indoComma){
        DecimalFormat df = dfDec;
        if(indoComma){
            df = dfIndoDec;
        }
        df.applyPattern("###,##0.0#");
        return df.format(num);
    }
    public static String formatDouble(double num){
        DecimalFormat df = dfDec;
        df.applyPattern("##0.0####");
        return df.format(num);
    }
    public static String formatInt(double num){
        return formatInt((int)num);
    }
    public static String formatInt(int num){
        return String.valueOf(num);
    }
    public static void showMessage(String content, String title){
        JOptionPane.showMessageDialog(null, content, title, JOptionPane.INFORMATION_MESSAGE);
    }
    public static void showError(String content, String title){
        JOptionPane.showMessageDialog(null, content, title, JOptionPane.ERROR_MESSAGE);
    }
    public static String getStackTrace(Exception ex){
        StringWriter errors = new StringWriter();
        ex.printStackTrace(new PrintWriter(errors));
        return errors.toString();
    }
    public static String escape(String s){
        return s.replace("\"", "\\\"");
    }
    public static void handleException(Exception ex){
        ex.printStackTrace();
        String err = getStackTrace(ex);
        writeLog("[EXCEPTION] " + err);
        showError(err, ex.getMessage());
    }
    public static boolean askConfirmation(String content, String title){
        int conf = JOptionPane.showConfirmDialog(null, content, title, JOptionPane.YES_NO_OPTION);
        return conf == JOptionPane.YES_OPTION;
    }
    public static File getExportCSVPath(){
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("E-Faktur csv", "csv");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showSaveDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            try{
                if (f.exists()){
                    if (askConfirmation("Do you want to replace?", "File exists")){
                        f.delete();
                        f.createNewFile();
                    }else{
                        return getExportCSVPath();
                    }
                }else{
                    f.createNewFile();
                }
                return f;
            }catch(IOException ex){
                handleException(ex);
            }
        }
        return null;
    }
    public static void centerWindow(Window frame) {
        frame.setLocationRelativeTo(null);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }
    
}
