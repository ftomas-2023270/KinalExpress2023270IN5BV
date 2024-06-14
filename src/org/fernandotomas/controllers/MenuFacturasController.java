/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fernandotomas.controllers;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.fernandotomas.dao.Conexion;
import org.fernandotomas.System.Principal;
import org.fernandotomas.bean.Factura;
import org.fernandotomas.bean.Empleados;
import org.fernandotomas.bean.Clientes;
import org.fernandotomas.reports.GenerarReportes;


/**
 * FXML Controller class
 *
 * @author informatica
 */
public class MenuFacturasController implements Initializable {
    private ObservableList<Factura> listaFactura;
    private ObservableList<Empleados> listaProveedores;
    private ObservableList<Clientes> listaClientes;
    private Principal escenarioPrincipalCargoEmp;
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones  tipoDeOperaciones = operaciones.NINGUNO;
    @FXML Button btnRegresar;
    @FXML private TextField txtNumeroF;
    @FXML private TextField txtEstado;
    @FXML private TextField txtTotalF;
    @FXML private TextField txtFechaF;
    @FXML private ComboBox cmbCodigoE;
    @FXML private ComboBox cmbCodigoC;
    @FXML private TableView tblFactura;
    @FXML private TableColumn colNumeroF;
    @FXML private TableColumn colEstado;
    @FXML private TableColumn colTotalF;
    @FXML private TableColumn colFechaF;
    @FXML private TableColumn colCodigoC;
    @FXML private TableColumn colCodigoE;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReportes;
    @FXML private ImageView imgAgregar;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
    }
    public void cargarDatos(){
        tblFactura.setItems(getFactura());
        colNumeroF.setCellValueFactory(new PropertyValueFactory<Factura, Integer>("numeroFactura"));
        colEstado.setCellValueFactory(new PropertyValueFactory<Factura, String>("estado"));
        colTotalF.setCellValueFactory(new PropertyValueFactory<Factura, Double>("totalFactura"));
        colFechaF.setCellValueFactory(new PropertyValueFactory<Factura, String>("fechaFactura"));
        colCodigoC.setCellValueFactory(new PropertyValueFactory<Factura, Integer>("codigoCliente"));
        colCodigoE.setCellValueFactory(new PropertyValueFactory<Factura, Integer>("codigoEmpleado   "));
    }
    
    public void seleccionarElemento(){
        txtNumeroF.setText(String.valueOf(((Factura)tblFactura.getSelectionModel().getSelectedItem()).getNumeroFactura()));
        txtEstado.setText(((Factura)tblFactura.getSelectionModel().getSelectedItem()).getEstado());
        txtTotalF.setText(String.valueOf(((Factura)tblFactura.getSelectionModel().getSelectedItem()).getTotalFactura()));
        txtFechaF.setText(((Factura)tblFactura.getSelectionModel().getSelectedItem()).getFechaFactura());
        cmbCodigoC.getSelectionModel().select(buscarEmpleado(((Factura)tblFactura.getSelectionModel().getSelectedItem()).getCodigoCliente()));
        cmbCodigoE.getSelectionModel().select(buscarEmpleado(((Factura)tblFactura.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
    }
    
    public Empleados buscarEmpleado (int codigoEmpleado ){
        Empleados resultado = null;
        try{
         PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_Empleados(?)}");
         procedimiento.setInt(1, codigoEmpleado);
         ResultSet registro = procedimiento.executeQuery();
         while (registro.next()){
             resultado = new Empleados (registro.getInt("codigoProveedor"),
                        registro.getString("NITProveedor"),
                        registro.getString("nombreProveedor"),
                        registro.getDouble("apellidoProveedor"),
                        registro.getString("direccionProveedor"),
                        registro.getString("raszonSocial"),
                        registro.getInt("contactoPrincipal")
             );
         }
        }catch (Exception e)
        {
            e.printStackTrace();
        }    
    
        return resultado;
    }
    public ObservableList <Factura> getFactura (){
        ArrayList <Factura> lista = new ArrayList();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarFacturas()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Factura (resultado.getInt("codigoEmailProveedor"),
                        resultado.getString("emailProveedor"),
                        resultado.getDouble("Proveedores_codigoProveedores"),
                        resultado.getString("emailProveedor"),
                        resultado.getInt("descripcion"),
                        resultado.getInt("descripcion")
                ));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    return listaFactura = FXCollections.observableArrayList(lista);    
    }
    public void agregar(){
        switch (tipoDeOperaciones){
            case NINGUNO:
                activarControles();
                btnAgregar.setText("Guardar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(true);
                btnReportes.setDisable(true);
                imgAgregar.setImage(new Image ("/org/fernandotomas/images/Guardar.png"));
                imgEliminar.setImage(new Image ("/org/fernandotomas/images/Cancelar.png"));
                tipoDeOperaciones = operaciones.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReportes.setDisable(false);
                imgAgregar.setImage(new Image("/org/fernandotomas/images/AgregarUsuario.png"));
                imgEliminar.setImage(new Image("/org/fernandotomas/images/EliminarUsuario.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
        }    
    }
    public void guardar(){
        Factura registro = new Factura();
        registro.setNumeroFactura(Integer.parseInt(txtNumeroF.getText()));
        registro.setEstado(txtEstado.getText());
        registro.setTotalFactura(Double.parseDouble(txtTotalF.getText()));
        registro.setFechaFactura(txtTotalF.getText());
        registro.setTotalFactura(Integer.parseInt(txtTotalF.getText()));
        registro.setTotalFactura(Integer.parseInt(txtTotalF.getText()));
        registro.setCodigoCliente(((Clientes)cmbCodigoC.getSelectionModel().getSelectedItem()
                 ).getCodigoCliente());
        registro.setCodigoEmpleado(((Empleados)cmbCodigoE.getSelectionModel().getSelectedItem()
                 ).getCodigoEmpleado());

        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarFactura( ?, ?, ?, ?)}");          
            procedimiento.setInt(1, registro.getNumeroFactura());
            procedimiento.setString(2, registro.getEstado());
            procedimiento.setString(3, registro.getFechaFactura());
            procedimiento.setInt(4, registro.getCodigoEmpleado());
            procedimiento.setInt(4, registro.getCodigoCliente());
            procedimiento.execute();
            listaFactura.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public void reporte (){
        switch(tipoDeOperaciones){
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnEditar.setText("Actualizar");
                btnReportes.setText("Cancelar");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setDisable(false);
                imgEditar.setImage(new Image("/org/fernandotomas/images/Cancelar.png"));
                imgReporte.setImage(new Image(("/org/fernandotomas/images/Registros.png")));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
        }
    }
    
    public void ImprimirReporte(){
        Map parametros = new HashMap();
        int factId = ((Factura)tblFactura.getSelectionModel().getSelectedItem()).getNumeroFactura();
        parametros.put("factId", factId);
        GenerarReportes.mostrarReportes("ReporteFacturaVespertina.jasper", "Factura de la tarde", parametros);
    }
    
    public void editar(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                if(tblFactura.getSelectionModel().getSelectedItem()!= null){
                    btnEditar.setText("Actualizar");
                    btnReportes.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/fernandotomas/images/ActualizarUsuario.png"));
                    imgReporte.setImage(new Image("/org/fernandotomas/images/Cancelar.png"));
                    activarControles();
                    txtNumeroF.setEditable(false);
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                }else
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar algun elemento");
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReportes.setText("Reportes");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/fernandotomas/images/ActualizarUsuario.png"));
                imgEditar.setImage(new Image("/org/fernandotomas/images/Registros.png"));
                desactivarControles();
                limpiarControles();
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void actualizar(){
        Factura registro = new Factura();
        registro.setNumeroFactura(Integer.parseInt(txtNumeroF.getText()));
        registro.setEstado(txtEstado.getText());
        registro.setTotalFactura(Double.parseDouble(txtTotalF.getText()));
        registro.setFechaFactura(txtTotalF.getText());
        registro.setTotalFactura(Integer.parseInt(txtTotalF.getText()));
        registro.setTotalFactura(Integer.parseInt(txtTotalF.getText()));
        registro.setCodigoCliente(((Clientes)cmbCodigoC.getSelectionModel().getSelectedItem()
                 ).getCodigoCliente());
        registro.setCodigoEmpleado(((Empleados)cmbCodigoE.getSelectionModel().getSelectedItem()
                 ).getCodigoEmpleado());
        try{
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarFactura( ?, ?, ?, ?)}");          
            procedimiento.setInt(1, registro.getNumeroFactura());
            procedimiento.setString(2, registro.getEstado());
            procedimiento.setString(3, registro.getFechaFactura());
            procedimiento.setInt(4, registro.getCodigoEmpleado());
            procedimiento.setInt(4, registro.getCodigoCliente());
            procedimiento.execute();
            listaFactura.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
            
        public void desactivarControles(){
        txtNumeroF.setEditable(false);
        txtEstado.setEditable(false);
        txtTotalF.setEditable(false);
        txtFechaF.setEditable(false);
        cmbCodigoC.setDisable(true);
        cmbCodigoE.setDisable(true);
    }
      public void activarControles(){
        txtNumeroF.setEditable(true);
        txtEstado.setEditable(true);
        txtTotalF.setEditable(true);
        txtFechaF.setEditable(true);
        cmbCodigoC.setDisable(false);
        cmbCodigoE.setDisable(false);
    }
      public void limpiarControles(){
        txtNumeroF.clear();
        txtEstado.clear();
        txtTotalF.clear();
        txtFechaF.clear();
        cmbCodigoC.getSelectionModel().getSelectedItem();
        cmbCodigoE.getSelectionModel().getSelectedItem();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipalCargoEmp;
    }
    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipalCargoEmp = escenarioPrincipal;
    }
    @FXML
    public void regresar(ActionEvent event){
         if (event.getSource() == btnRegresar){
        escenarioPrincipalCargoEmp.menuPrincipalView();
        }
    }  
}