/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.fernandotomas.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import org.fernandotomas.System.Principal;
 
/**
*
* @author informatica
*/
public class MenuPrincipalController implements Initializable {
    private Principal escenarioPrincipal;
    @FXML MenuItem btnMenuClientes;
    @FXML MenuItem btnProgramador;
    @FXML MenuItem btnMenuProveedores;
    @FXML MenuItem btnMenuCargoEmp;
    @FXML MenuItem btnMenuTipoProducto;
    @FXML MenuItem btnMenuCompras;
    
    
    @Override
    public void initialize (URL location, ResourceBundle resources)  {
 
    }
 
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }
 
    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
 
    @FXML
    public void handleButtonAction(ActionEvent event){
        if(event.getSource()== btnMenuClientes){
            escenarioPrincipal.menuClientesView();
        }else if(event.getSource()== btnProgramador){
            escenarioPrincipal.programadorView();
        }else if(event.getSource()== btnMenuProveedores){
            escenarioPrincipal.menuProveedoresView();
        }else if(event.getSource()== btnMenuCargoEmp){
            escenarioPrincipal.menuCargoEmpView();
        }else if(event.getSource()== btnMenuTipoProducto){
            escenarioPrincipal.menuTipoProductoView();
        }else if(event.getSource()== btnMenuCompras){
            escenarioPrincipal.menuComprasView();
        }
    }
}