/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etaxgenerator.util;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.datatransfer.*;
import java.util.*;
import javax.swing.table.DefaultTableModel;
/**
 * ExcelAdapter enables Copy-Paste Clipboard functionality on JTables.
 * The clipboard data format used by the adapter is compatible with
 * the clipboard format used by Excel. This provides for clipboard
 * interoperability between enabled JTables and Excel.
 */
public class ExcelAdapter implements ActionListener
   {
   private String rowstring,value;
   private Clipboard system;
   private StringSelection stsel;
   private JTable jTable1 ;
   /**
    * The Excel Adapter is constructed with a
    * JTable on which it enables Copy-Paste and acts
    * as a Clipboard listener.
    */
public ExcelAdapter(JTable myJTable)
   {
        jTable1 = myJTable;
        KeyStroke copy = KeyStroke.getKeyStroke(KeyEvent.VK_C,ActionEvent.CTRL_MASK,false);
        // Identifying the copy KeyStroke user can modify this
        // to copy on some other Key combination.
        KeyStroke paste = KeyStroke.getKeyStroke(KeyEvent.VK_V,ActionEvent.CTRL_MASK,false);
        // Identifying the Paste KeyStroke user can modify this
        //to copy on some other Key combination.
        jTable1.registerKeyboardAction(this,"Copy",copy,JComponent.WHEN_FOCUSED);
        jTable1.registerKeyboardAction(this,"Paste",paste,JComponent.WHEN_FOCUSED);
        system = Toolkit.getDefaultToolkit().getSystemClipboard();
   }
   /**
    * Public Accessor methods for the Table on which this adapter acts.
    */
public JTable getJTable() {return jTable1;}
public void setJTable(JTable jTable1) {this.jTable1=jTable1;}
   /**
    * This method is activated on the Keystrokes we are listening to
    * in this implementation. Here it listens for Copy and Paste ActionCommands.
    * Selections comprising non-adjacent cells result in invalid selection and
    * then copy action cannot be performed.
    * Paste is done by aligning the upper left corner of the selection with the
    * 1st element in the current selection of the JTable.
    */
public void paste(String[][] sTable){
    paste(sTable, 0, 0);
}
public void paste(String[][] sTable, int startRow, int startCol){
    
    int rowCount = jTable1.getRowCount();
    int colCount = jTable1.getColumnCount();
    int cbRow = sTable.length;
    int maxRow = startRow+cbRow;
    DefaultTableModel dtm = (DefaultTableModel)jTable1.getModel();
    while (maxRow > rowCount){
        ++rowCount;
        dtm.addRow(new Object[colCount]);
    }
    for(int i=0;i<cbRow;i++)
    {
        
        String[] cols = sTable[i];
        int cbCol = cols.length;
        int maxCol = startCol+cbCol;
        while (maxCol > colCount){
            ++colCount;
            dtm.addColumn("");
            //dtm.addColumn("", new Object[rowCount]);
        }
        for(int j=0;j<cbCol;j++)
        {
           //value=(String)st2.nextToken();
           value = cols[j];
           if (startRow+i< rowCount && startCol+j< colCount){
                jTable1.setValueAt(value,startRow+i,startCol+j);
           }else{
               try{
                   throw new Exception(String.format("Out of range: row=%d, col=%d", i, j));
               }catch(Exception ex){
                    Util.handleException(ex);
               }
           }
       }
    }
}
public void paste(String clipboard){
    paste(clipboard, 0, 0);
}

public void paste(String clipboard, int startRow, int startCol){
    
    String[] rows = clipboard.split("\n");
    int cbRow = rows.length;
    String[][] sTable = new String[cbRow][];
    //int cbCol = 0;
    for (int i = 0; i < cbRow; ++i){
        sTable[i] = rows[i].split("\t");
        int len = sTable[i].length;
        /*if (cbCol < len){
            cbCol = len;
        }*/
    }
    paste(sTable, 0, 0);
}
public void insertRows(int index){
    ((DefaultTableModel)jTable1.getModel()).insertRow(index, new Object[jTable1.getColumnCount()]);
}
public void insertRows(int index, int count){
    for (int i = 0; i < count; ++i){
        insertRows(index);
    }
}
public void insertSelectedRows(){
    int[] rows = jTable1.getSelectedRows();
    int l = rows.length;
    int r = 0;
    if(l == 0){
        addRows();
        l=1;
    }else{
        r = rows[0];
        rows=Util.sort(rows);
        insertRows(r, l);
    }
    jTable1.setRowSelectionInterval(r, r+l-1);
}
public void addRows(){
    ((DefaultTableModel)jTable1.getModel()).addRow(new Object[jTable1.getColumnCount()]);
}
public void addRows(int count){
    for (int i = 0; i < count; ++i){
        addRows();
    }
}
public void moveRows(int index, int to){
    moveRows(index, index, to);
}
public void moveRows(int[] indexes, int to){
    int l = indexes.length;
    if(l == 0){
        return;
    }
    int[] rows = Util.sort(indexes);
    int start = rows[0];
    int end = start;
    int diff = 0;
    for(int i = 1; i < l; ++i){
        int r = rows[i] + diff;
        if (r-end == 1){
            end = r;
        }else{
            moveRows(start, end, to);
            diff += start-end-1;
            if(to > end){
                to=to+diff;
            }
            to = to+end-start+1;
            
            r = rows[i] + diff;
            start=r;
            end=r;
        }
    }
    moveRows(start, end, to);
}
public void moveRows(int start, int end, int to){
    if(to < 0){
        to = 0;
    }else if (to >= jTable1.getRowCount()){
        to = jTable1.getRowCount()-1;
    }
    ((DefaultTableModel)jTable1.getModel()).moveRow(start, end, to);
}
public void moveRowsUp(int index){
    moveRowsUp(index, index);
}
public void moveRowsUp(int start, int end){
    if(start==0){
        return;
    }
    moveRows(start, end, start-1);
}
public void moveSelectedRowsUp(){
    int[] rows = jTable1.getSelectedRows();
    if(rows.length == 0){
        return;
    }
    rows=Util.sort(rows);
    int i = rows[0]-1;
    moveRows(rows, i);
    jTable1.setRowSelectionInterval(i, i+rows.length-1);
}
public void moveRowsDown(int index){
    moveRowsDown(index, index);
}
public void moveRowsDown(int start, int end){
    if (end == jTable1.getRowCount()-1){
        return;
    }
    moveRows(start, end, end+1);
}
public void moveSelectedRowsDown(){
    int[] rows = jTable1.getSelectedRows();
    if(rows.length == 0){
        return;
    }
    rows=Util.sort(rows);
    int i = rows[0]+1;
    moveRows(rows, i);
    jTable1.setRowSelectionInterval(i, i-1+rows.length);
}
public void reduceRows(){
    int c = jTable1.getRowCount();
    if (c < 0){
        return;
    }
    ((DefaultTableModel)jTable1.getModel()).removeRow(c-1);
}
public void reduceRows(int count){
    for(int i = 0; i < count; ++i){
        reduceRows();
    }
}
public void deleteRows(int index){
    if(index < 0 || index >= jTable1.getRowCount()){
        return;
    }
    ((DefaultTableModel)jTable1.getModel()).removeRow(index);
}

public void deleteRows(int[] indexes){
    indexes = Util.sort(indexes);
    for (int i = indexes.length-1; i >= 0; --i){
        deleteRows(indexes[i]);
    }
}
public void deleteSelectedRows(){
    int[] rows = jTable1.getSelectedRows();
    if(rows.length == 0){
        deleteRows(0);
        return;
    }
    deleteRows(rows);
}
public void setRowCount(int count){
    int c = jTable1.getRowCount();
    if (c == count){
        return;
    }else if (c < count){
        addRows(count-c);
    }else{
        reduceRows(c-count);
    }
}
public void actionPerformed(ActionEvent e)
   {
       
      if (e.getActionCommand().compareTo("Copy")==0)
      {
         StringBuffer sbf=new StringBuffer();
         // Check to ensure we have selected only a contiguous block of
         // cells
         int numcols=jTable1.getSelectedColumnCount();
         int numrows=jTable1.getSelectedRowCount();
         int[] rowsselected=jTable1.getSelectedRows();
         int[] colsselected=jTable1.getSelectedColumns();
         if (!((numrows-1==rowsselected[rowsselected.length-1]-rowsselected[0] &&
                numrows==rowsselected.length) &&
(numcols-1==colsselected[colsselected.length-1]-colsselected[0] &&
                numcols==colsselected.length)))
         {
            Util.showMessage("Invalid Copy Selection",
                                          "Invalid Copy Selection");
            return;
         }
         for (int i=0;i<numrows;i++)
         {
            for (int j=0;j<numcols;j++)
            {
sbf.append(jTable1.getValueAt(rowsselected[i],colsselected[j]));
               if (j<numcols-1) sbf.append("\t");
            }
            sbf.append("\n");
         }
         stsel  = new StringSelection(sbf.toString());
         system = Toolkit.getDefaultToolkit().getSystemClipboard();
         system.setContents(stsel,stsel);
      }
      if (e.getActionCommand().compareTo("Paste")==0)
      {
          int startRow=(jTable1.getSelectedRows())[0];
          int startCol=(jTable1.getSelectedColumns())[0];
          try
          {
            String trstring= (String)(system.getContents(this).getTransferData(DataFlavor.stringFlavor));
            paste(trstring, startRow, startCol);
         }
         catch(Exception ex){
            Util.handleException(ex);
         }
      }
   }
}