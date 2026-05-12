package org.project.autoserviceapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public Connection databaseLink;

    public Connection getConnection(){
        try{
            Class.forName("org.sqlite.JDBC");
            databaseLink = DriverManager.getConnection("jdbc:sqlite:autoserviceApp.db");

            DriverManager.registerDriver(new org.sqlite.JDBC());
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return databaseLink;
    }
}
