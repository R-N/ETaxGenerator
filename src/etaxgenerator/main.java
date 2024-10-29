/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etaxgenerator;

import java.net.URISyntaxException;

/**
 *
 * @author MojoMacW7
 */
public class main {

    /**
     * @param args the command line arguments
     */
    
    public static void init() throws URISyntaxException{
        // Get the path to the JAR file
        etaxgenerator.util.Config.init(main.class);
        etaxgenerator.util.Util.init();
        etaxgenerator.util.Database.getInstance();
        etaxgenerator.counterparty.Database.getInstance();
    }
    public static void main(String[] args) throws URISyntaxException {
        // TODO code application logic here
        init();
        GUI.main(args);
    }
    
}
