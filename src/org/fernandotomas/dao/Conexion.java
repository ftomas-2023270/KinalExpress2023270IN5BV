package org.fernandotomas.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author informatica
 */
public class Conexion {
    private Connection conexion; 
    private static Conexion instancia;
    
    public Conexion(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
<<<<<<< HEAD
          //   conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/DBKinalExpress?useSSL=false", "2023270_IN5BV", "Fer14112006");
            conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/DBKinalExpress?useSSL=false", "root", "toor");
=======
            // conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/DBKinalExpress?useSSL=false", "2023270_IN5BV", "Fer14112006");
          //  conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/DBKinalExpress?useSSL=false", "root", "toor");
           conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/DBKinalExpress?useSSL=false", "root", "RootKinal2024$");
>>>>>>> 4d48aa36b93d98f1cae4c3c95b1012be1fd1607c
        }catch(ClassNotFoundException e){
            e.getStackTrace();
        }catch(InstantiationException e){
            e.printStackTrace();
        }catch(IllegalAccessException e){
            e.printStackTrace();
        }catch(SQLException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static Conexion getInstance(){
        if(instancia == null){
            instancia = new Conexion();
        }
        return instancia;
    }

    public Connection getConexion() {
        return conexion;
    }

    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }
    
 }
