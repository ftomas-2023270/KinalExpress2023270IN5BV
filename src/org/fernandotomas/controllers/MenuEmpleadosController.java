/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fernandotomas.controllers;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.fernandotomas.bean.CargoEmp;
import org.fernandotomas.dao.Conexion;
import org.fernandotomas.System.Principal;
import org.fernandotomas.bean.Empleados;

/**
 *
 * @author lphg3
 */
public class MenuEmpleadosController implements Initializable{

    
    private Principal escenarioPrincipal;
    private enum operaciones{AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoOperaciones = operaciones.NINGUNO;
    private ObservableList <CargoEmp> listaCargoEmp;
    private ObservableList <Empleados> listaEmpleados;
    
    @FXML private Button btnRegresar;
    @FXML private TextField txtCodigoEmp;
    @FXML private TextField txtNombreEmp;
    @FXML private TextField txtApellidoEmp;
    @FXML private TextField txtSueldo;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtTurno;
    @FXML private ComboBox cmbCargoEmp;
    @FXML private TableView tblEmpleados;
    @FXML private TableColumn colCodigoEmp;
    @FXML private TableColumn colNombreEmp;
    @FXML private TableColumn colApellidoEmp;
    @FXML private TableColumn colSueldo;
    @FXML private TableColumn colDireccion;
    @FXML private TableColumn colTurno;
    @FXML private TableColumn colCodigoCargoEmp;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReportes;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCargoEmp.setItems(getCargoEmp());
    }


    
    public void cargarDatos(){
    tblEmpleados.setItems(getEmpleados());
    colCodigoEmp.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("codigoEmpleado"));
    colNombreEmp.setCellValueFactory(new PropertyValueFactory<Empleados, String>("nombreEmpleado"));
    colApellidoEmp.setCellValueFactory(new PropertyValueFactory<Empleados, String>("apellidoEmpleado"));
    colSueldo.setCellValueFactory(new PropertyValueFactory<Empleados, Double>("sueldo"));
    colDireccion.setCellValueFactory(new PropertyValueFactory<Empleados, String>("direccion"));
    colTurno.setCellValueFactory(new PropertyValueFactory<Empleados, String>("turno"));
    colCodigoCargoEmp.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("codigoCargoEmp"));
    
    
    }
    public void seleccionarElementos(){
       txtCodigoEmp.setText(String.valueOf(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
       txtNombreEmp.setText(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getNombreEmpleado());
       txtApellidoEmp.setText(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getApellidoEmpleado());
       txtSueldo.setText(String.valueOf(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getSueldo()));
       txtDireccion.setText(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getDireccion());
       txtTurno.setText(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getTurno());
       cmbCargoEmp.getSelectionModel().select(buscarCargoEmp(((CargoEmp)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoCargoEmpleado()));
    }
    
    public CargoEmp buscarCargoEmp (int codigoCargoEmp ){
        CargoEmp resultado = null;
        try{
         PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarCargoEmp(?)}");
         procedimiento.setInt(1, codigoCargoEmp);
         ResultSet registro = procedimiento.executeQuery();
         while (registro.next()){
             resultado = new CargoEmp(registro.getInt("codigoCargoEmpleado"),
                                            registro.getString("nombreCargoEmp"),
                                            registro.getString("descripcion")
             
             );
         }
        }catch (Exception e)
        {
            e.printStackTrace();
        }    
    
        return resultado;
    }
    
    public ObservableList<CargoEmp> getCargoEmp(){
    ArrayList<CargoEmp> lista = new ArrayList<CargoEmp>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarCargoEmp()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new CargoEmp (resultado.getInt("codigoCargoEmpleado"),
                                              resultado.getString("nombreCargo"),
                                        resultado.getString("descripcionCargo")    
                ));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    return listaCargoEmp = FXCollections.observableArrayList(lista);
    }
    
    
    public ObservableList<Empleados> getEmpleados(){
    ArrayList<Empleados> lista = new ArrayList<Empleados>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpleados()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Empleados (resultado.getInt("codigoProducto"),
                                        resultado.getString("descripcionProducto"),
                                        resultado.getString("descripcionProducto"),
                                        resultado.getDouble("precioUnitario"),
                                        resultado.getString("existencia"),
                                        resultado.getString("codigoTipoProducto"),
                                        resultado.getInt("codigoProveedor")            
                ));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    return listaEmpleados = FXCollections.observableArrayList(lista);
        
    }
    
     public void agregar (){
         switch(tipoOperaciones){
             case NINGUNO:
              activarControles();
             btnAgregar.setText("Guardar");
             btnEliminar.setText("Cancelar");
             btnEditar.setDisable(true);
             btnReportes.setDisable(true);   
             tipoOperaciones = operaciones.ACTUALIZAR;
             break;
             case ACTUALIZAR:
             guardar ();
             desactivarControles();
             limpiarControles ();
             btnAgregar.setText("Agregar");
             btnEliminar.setText("Eliminar");
             btnEditar.setDisable(false);
             btnReportes.setDisable(false);
             tipoOperaciones = operaciones.NINGUNO;
             cargarDatos();
             break;
         }
     
     
     
     
     }
     
     
     public void guardar (){
         Empleados registro = new Empleados();
         registro.setCodigoEmpleado(Integer.parseInt(txtCodigoEmp.getText()));
         registro.setCargoEmpleado(((CargoEmp)cmbCargoEmp.getSelectionModel().getSelectedItem()
                 ).getCodigoCargoEmpleado());
         registro.setNombreEmpleado(txtNombreEmp.getText());
         registro.setApellidoEmpleado(txtApellidoEmp.getText());
         registro.setSueldo(Double.parseDouble(txtSueldo.getText()));
         registro.setDireccion(txtDireccion.getText());
         registro.setTurno(txtTurno.getText());
         try {
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall
        ("{CALL sp_AgregarEmpleados(?, ?, ?, ?, ?, ?, ?, ?)}");
        procedimiento.setInt(1, registro.getCodigoEmpleado());
        procedimiento.setString(2, registro.getNombreEmpleado());
        procedimiento.setString(3, registro.getApellidoEmpleado());
        procedimiento.setDouble(4, registro.getSueldo());
        procedimiento.setString(5,registro.getDireccion());
        procedimiento.setString(6,registro.getTurno());
        procedimiento.setInt(7, registro.getCargoEmpleado());
        procedimiento.execute();
        
        listaEmpleados.add(registro);

         }catch (Exception e){
             e.printStackTrace();
         }
     
     }
     public void editar(){
        switch(tipoOperaciones){
            case NINGUNO:
                if(tblEmpleados.getSelectionModel().getSelectedItem()!= null){
                    btnEditar.setText("Actualizar");
                    btnReportes.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    txtCodigoEmp.setEditable(false);
                    tipoOperaciones = operaciones.ACTUALIZAR;
                }else
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar algun elemento");
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReportes.setText("Reportes");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                desactivarControles();
                limpiarControles();
                tipoOperaciones = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
     public void reporte (){
        switch(tipoOperaciones){
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnEditar.setText("Actualizar");
                btnReportes.setText("Cancelar");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setDisable(false);
                tipoOperaciones = operaciones.NINGUNO;
                break;
        }
    }
     
    public void actualizar(){
        Empleados registro = new Empleados();
         registro.setCargoEmpleado(((CargoEmp)cmbCargoEmp.getSelectionModel().getSelectedItem())
            .getCodigoCargoEmpleado());
         registro.setNombreEmpleado(txtNombreEmp.getText());
         registro.setApellidoEmpleado(txtApellidoEmp.getText());
         registro.setSueldo(Double.parseDouble(txtSueldo.getText()));
         registro.setDireccion(txtDireccion.getText());
         registro.setTurno(txtTurno.getText());
         try {
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall
        ("{CALL sp_EditarEmpleados(?, ?, ?, ?, ?, ?, ?, ?)}");
        procedimiento.setInt(1, registro.getCodigoEmpleado());
        procedimiento.setString(2, registro.getNombreEmpleado());
        procedimiento.setString(3, registro.getApellidoEmpleado());
        procedimiento.setDouble(4, registro.getSueldo());
        procedimiento.setString(5,registro.getDireccion());
        procedimiento.setString(6,registro.getTurno());
        procedimiento.setInt(7, registro.getCargoEmpleado());
        procedimiento.execute();
        
        listaEmpleados.add(registro);

         }catch (Exception e){
             e.printStackTrace();
         }
     
    }
    
    public void desactivarControles(){
        txtCodigoEmp.setEditable(false);
        txtNombreEmp.setEditable(false);
        txtApellidoEmp.setEditable(false);
        txtSueldo.setEditable(false);
        txtDireccion.setEditable(false);
        txtTurno.setEditable(false);
        cmbCargoEmp.setDisable(true);
    }
      public void activarControles(){
        txtCodigoEmp.setEditable(true);
        txtNombreEmp.setEditable(true);
        txtApellidoEmp.setEditable(true);
        txtSueldo.setEditable(true);
        txtDireccion.setEditable(true);
        txtTurno.setEditable(true);
        cmbCargoEmp.setDisable(false);
    }
      public void limpiarControles(){
        txtCodigoEmp.clear();
        txtNombreEmp.clear();
        txtApellidoEmp.clear();
        txtSueldo.clear();
        txtDireccion.clear();
        txtTurno.clear();
        tblEmpleados.getSelectionModel().getSelectedItem();
        cmbCargoEmp.getSelectionModel().getSelectedItem();
    }
      
          public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    @FXML
    public void regresar(ActionEvent event){
         if (event.getSource() == btnRegresar){
        escenarioPrincipal.menuPrincipalView();
        }
    } 
}