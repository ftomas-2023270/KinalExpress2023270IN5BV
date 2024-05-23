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
import org.fernandotomas.bean.TelefonoProveedor;
import org.fernandotomas.bean.Proveedores;


/**
 * FXML Controller class
 *
 * @author informatica
 */
public class MenuTelefonoProveedoresController implements Initializable {
    private ObservableList<TelefonoProveedor> listaTelefonoProveedor;
    private ObservableList<Proveedores> listaProveedores;
    private Principal escenarioPrincipalCargoEmp;
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones  tipoDeOperaciones = operaciones.NINGUNO;
    @FXML Button btnRegresar;
    @FXML private TextField txtCodigoTP;
    @FXML private TextField txtNumeroPrincipal;
    @FXML private TextField txtNumeroSecundario;
    @FXML private TextField txtObservaciones;
    @FXML private ComboBox cmbCodigoProv;
    @FXML private TableView tblTelefonoProveedor;
    @FXML private TableColumn colCodigoTP;
    @FXML private TableColumn colNumeroPrincipal;
    @FXML private TableColumn colNumeroSecundario;
    @FXML private TableColumn colObservaciones;
    @FXML private TableColumn colCodigoProv;
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
        tblTelefonoProveedor.setItems(getTelefonoProveedor());
        colCodigoTP.setCellValueFactory(new PropertyValueFactory<TelefonoProveedor, Integer>("codigoTelefonoProveedor"));
        colNumeroPrincipal.setCellValueFactory(new PropertyValueFactory<TelefonoProveedor, String>("numeroPrincipal"));
        colNumeroSecundario.setCellValueFactory(new PropertyValueFactory<TelefonoProveedor, String>("numeroSecundario"));
        colObservaciones.setCellValueFactory(new PropertyValueFactory<TelefonoProveedor, String>("observaciones"));
        colCodigoProv.setCellValueFactory(new PropertyValueFactory<TelefonoProveedor, Integer>("Proveedores_codigoProveedores"));
    }
    
    public void seleccionarElemento(){
        txtCodigoTP.setText(String.valueOf(((TelefonoProveedor)tblTelefonoProveedor.getSelectionModel().getSelectedItem()).getCodigoTelefonoProveedor()));
        txtNumeroPrincipal.setText(((TelefonoProveedor)tblTelefonoProveedor.getSelectionModel().getSelectedItem()).getTelefonoPrincipal());
        txtNumeroSecundario.setText(((TelefonoProveedor)tblTelefonoProveedor.getSelectionModel().getSelectedItem()).getTelefonoSecundario());
        txtObservaciones.setText(((TelefonoProveedor)tblTelefonoProveedor.getSelectionModel().getSelectedItem()).getObservaciones());
       cmbCodigoProv.getSelectionModel().select(buscarProveedor(((Proveedores)tblTelefonoProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
    }
    
    public Proveedores buscarProveedor (int codigoProveedor ){
        Proveedores resultado = null;
        try{
         PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarProveedor(?)}");
         procedimiento.setInt(1, codigoProveedor);
         ResultSet registro = procedimiento.executeQuery();
         while (registro.next()){
             resultado = new Proveedores (registro.getInt("codigoProveedor"),
                        registro.getString("NITProveedor"),
                        registro.getString("nombreProveedor"),
                        registro.getString("apellidoProveedor"),
                        registro.getString("direccionProveedor"),
                        registro.getString("raszonSocial"),
                        registro.getString("contactoPrincipal"),
                        registro.getString("paginaWebProveedor")
             );
         }
        }catch (Exception e)
        {
            e.printStackTrace();
        }    
    
        return resultado;
    }
    public ObservableList <TelefonoProveedor> getTelefonoProveedor (){
        ArrayList <TelefonoProveedor> lista = new ArrayList();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTelefonoProveedor()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new TelefonoProveedor (resultado.getInt("codigoTelefonoProveedor"),
                        resultado.getString("numeroPrincipal"),
                        resultado.getString("numeroSecundario"),
                        resultado.getString("observaciones"),
                        resultado.getInt("Proveedores_codigoProveedores")
                ));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    return listaTelefonoProveedor = FXCollections.observableArrayList(lista);    
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
        TelefonoProveedor registro = new TelefonoProveedor();
        registro.setCodigoTelefonoProveedor(Integer.parseInt(txtCodigoTP.getText()));
        registro.setTelefonoPrincipal(txtNumeroPrincipal.getText());
        registro.setTelefonoSecundario(txtNumeroSecundario.getText());
        registro.setObservaciones(txtObservaciones.getText());
        registro.setCodigoProveedores(((Proveedores)cmbCodigoProv.getSelectionModel().getSelectedItem()
                 ).getCodigoProveedor());

        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarTelefonoProveedor(?, ?, ?, ?, ?)}");          
            procedimiento.setInt(1, registro.getCodigoTelefonoProveedor());
            procedimiento.setString(2, registro.getTelefonoPrincipal());
            procedimiento.setString(3, registro.getTelefonoSecundario());
            procedimiento.setString(4, registro.getObservaciones());
            procedimiento.setInt(5, registro.getCodigoProveedores());
            procedimiento.execute();
            listaTelefonoProveedor.add(registro);
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
    
    public void editar(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                if(tblTelefonoProveedor.getSelectionModel().getSelectedItem()!= null){
                    btnEditar.setText("Actualizar");
                    btnReportes.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/fernandotomas/images/ActualizarUsuario.png"));
                    imgReporte.setImage(new Image("/org/fernandotomas/images/Cancelar.png"));
                    activarControles();
                    txtCodigoTP.setEditable(false);
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
        TelefonoProveedor registro = new TelefonoProveedor();
        registro.setTelefonoPrincipal(txtNumeroPrincipal.getText());
        registro.setTelefonoSecundario(txtNumeroSecundario.getText());
        registro.setObservaciones(txtObservaciones.getText());
        registro.setCodigoProveedores(((Proveedores)cmbCodigoProv.getSelectionModel().getSelectedItem()
                 ).getCodigoProveedor());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarTelefonoProveedor(?, ?, ?, ?, ?)}");        
            procedimiento.setInt(1, registro.getCodigoTelefonoProveedor());
            procedimiento.setString(2, registro.getTelefonoPrincipal());
            procedimiento.setString(3, registro.getTelefonoSecundario());
            procedimiento.setString(4, registro.getObservaciones());
            procedimiento.setInt(5, registro.getCodigoProveedores());
            procedimiento.execute();
            listaTelefonoProveedor.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
            
        public void desactivarControles(){
        txtCodigoTP.setEditable(false);
        txtNumeroPrincipal.setEditable(false);
        txtNumeroSecundario.setEditable(false);
        txtObservaciones.setEditable(false);
        cmbCodigoProv.setDisable(true);
    }
      public void activarControles(){
        txtCodigoTP.setEditable(true);
        txtNumeroPrincipal.setEditable(true);
        txtNumeroSecundario.setEditable(true);
        txtObservaciones.setEditable(true);
        cmbCodigoProv.setDisable(false);
    }
      public void limpiarControles(){
        txtCodigoTP.clear();
        txtNumeroPrincipal.clear();
        txtNumeroSecundario.clear();
        txtObservaciones.clear();
        cmbCodigoProv.getSelectionModel().getSelectedItem();
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
