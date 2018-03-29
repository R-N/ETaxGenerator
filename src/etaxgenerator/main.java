/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etaxgenerator;

/**
 *
 * @author MojoMacW7
 */
public class main {

    /**
     * @param args the command line arguments
     */
    
    public static void init(){
        etaxgenerator.util.Util.init();
        etaxgenerator.util.Database.getInstance();
        etaxgenerator.counterparty.Database.getInstance();
    }
    public static void main(String[] args) {
        // TODO code application logic here
        init();
        GUI.main(args);
    }
    
}
