/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etaxgenerator.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author MojoMacW7
 */
public class Database {
    public static final String url = "jdbc:sqlite:database.db";
    public static Database instance = null;
    
    public static Database getInstance(){
        if (instance == null){
            instance = new Database();
        }
        return instance;
    }
    public Connection conn;
    
    public Database(String url){
        connect(url);
    }
    public Database(){
        connect(url);
    }
    
    public boolean setAutoCommit(boolean autoCommit){
        try{
            conn.setAutoCommit(autoCommit);
            return true;
        }catch (SQLException ex){
            Util.handleException(ex);
        }
        return false;
    }
    
    
    public boolean commit(){
        try{
            conn.commit();
            return true;
        }catch (SQLException ex){
            Util.handleException(ex);
        }
        return false;
    }
    
    public boolean connect(String url){
        try {
            System.out.println("Connecting...");
            // db parameters
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            instance = this;
            setAutoCommit(false);
            System.out.println("Connected");
            Config.init();
            etaxgenerator.counterparty.Database.getInstance();
            return true;
        } catch (SQLException ex) {
            Util.handleException(ex);
        } 
        return false;
    }
    
    public PreparedStatement prepareStatement(String sql){
        try{
            return conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        }catch(SQLException ex){
            Util.handleException(ex);
        }
        return null;
    }
    
    public Statement createStatement(){
        try{
            return conn.createStatement();
        }catch(SQLException ex){
            Util.handleException(ex);
        }
        return null;
    }
    
    public ResultSet executeQuery(String query){
        try{
            return conn.createStatement().executeQuery(query);
        }catch(SQLException ex){
            Util.handleException(ex);
        }
        return null;
    }
    
    public boolean execute(String query){
        try{
            return conn.createStatement().execute(query);
        }catch(SQLException ex){
            Util.handleException(ex);
        }
        return false;
    }
    
    public int executeUpdate(String query){
        try{
            return conn.createStatement().executeUpdate(query);
        }catch(SQLException ex){
            Util.handleException(ex);
        }
        return -1;
    }
    
    public boolean close(){
        try {
            if (conn != null) {
                conn.close();
                return true;
            }
        } catch (SQLException ex) {
            Util.handleException(ex);
        }
        return false;
    }
}
