/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etaxgenerator.counterparty.importer;

import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import etaxgenerator.counterparty.Counterparty;
import java.io.IOException;
import java.util.ArrayList;
import etaxgenerator.util.Util;
/**
 *
 * @author MojoMacW7
 */
public class Reader {
    
    public static ArrayList<Counterparty> parse(){
        File file = etaxgenerator.util.Util.chooseCSV();
        if (file == null){
            System.out.println("No file choosen");
            return null;
        }
        ArrayList<Counterparty> ret = parse(file);
        return ret;
    }
    public static ArrayList<Counterparty> parse(File file){
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader buf = null;
        try{
            System.out.println("Reading " + file.getName());
            if (file == null){
                throw new Exception("File is null");
            }else if (!file.exists()){
                throw new Exception("File does not exist");
            }else if(!file.canRead()){
                throw new Exception("File is unreadable");
            }
            is = new FileInputStream(file);
             isr = new InputStreamReader(is);
             buf = new BufferedReader(isr);
            String line = buf.readLine();
            if (line == null || line.isEmpty()){
                throw new Exception("File is empty");
            }else if (!line.equals(Counterparty.header0) && !line.equals(Counterparty.header)){
                throw new Exception ("Invalid header");
            }
            ArrayList<Counterparty> ret = new ArrayList<Counterparty>();
            line = buf.readLine();
            while(line != null){
                Counterparty cp = etaxgenerator.counterparty.Parser.parse(line);
                if (cp != null){
                    ret.add(cp);
                }
                line = buf.readLine();
            }
            System.out.println("Read " + ret.size() + " counterparties");
            return ret;
        }catch(FileNotFoundException ex){
            Util.handleException(ex);
        }catch(IOException ex){
            Util.handleException(ex);
        }catch(Exception ex){
            Util.handleException(ex);
        }finally{
            try{
                is.close();
                isr.close();
                buf.close();
            }catch(IOException ex){
                Util.handleException(ex);
            }
        }
        return null;
    }
}
