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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.fernandotomas.dao.Conexion;
import org.fernandotomas.System.Principal;
import org.fernandotomas.bean.Proveedores;

/**
 * FXML Controller class
 *
 * @author informatica
 */
public class MenuProveedoresController implements Initializable {
    private ObservableList<Proveedores> listaProveedores;
    private Principal escenarioPrincipalProveedores;
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones  tipoDeOperaciones = operaciones.NINGUNO;
    @FXML Button btnRegresar;
    @FXML private TextField txtCodigoP;
    @FXML private TextField txtNombreP;
    @FXML private TextField txtApellidoP;
    @FXML private TextField txtNit;
    @FXML private TextField txtRazonSocialP;
    @FXML private TextField txtDireccionP;
    @FXML private TextField txtPaginaWP;
    @FXML private TextField txtContactoP;
    @FXML private TableView tblProveedores;
    @FXML private TableColumn colCodigoP;
    @FXML private TableColumn colNombreP;
    @FXML private TableColumn colApellidoP;
    @FXML private TableColumn colNit;
    @FXML private TableColumn colDireccionP;
    @FXML private TableColumn colRazonSocialP;
    @FXML private TableColumn colContactoP;
    @FXML private TableColumn colPaginaWP;
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
        tblProveedores.setItems(getProveedores());
        colCodigoP.setCellValueFactory(new PropertyValueFactory<Proveedores, Integer>("codigoProveedor"));
        colNit.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("NITProveedor"));
        colNombreP.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("nombreProveedor"));
        colApellidoP.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("apellidoProveedor"));
        colDireccionP.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("direccionProveedor"));
        colRazonSocialP.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("razonSocial"));
        colContactoP.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("contactoPrincipal"));
        colPaginaWP.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("paginaWebProveedor"));
    }
    
    public void seleccionarElemento(){
        txtCodigoP.setText(String.valueOf(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
        txtNombreP.setText(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getNombreProveedor());
        txtApellidoP.setText(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getApellidoProveedor());
        txtNit.setText(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getNITProveedor());
        txtContactoP.setText(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getContactoPrincipal());
        txtDireccionP.setText(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getDireccionProveedor());
        txtPaginaWP.setText(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getPaginaWebProveedor());
        txtRazonSocialP.setText(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getRazonSocial());
    }
    
    public ObservableList <Proveedores> getProveedores (){
        ArrayList <Proveedores> lista = new ArrayList();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProveedores()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Proveedores (resultado.getInt("codigoProveedor"),
                        resultado.getString("NITProveedor"),
                        resultado.getString("nombreProveedor"),
                        resultado.getString("apellidoProveedor"),
                        resultado.getString("direccionProveedor"),
                        resultado.getString("razonSocial"),
                        resultado.getString("contactoPrincipal"),
                        resultado.getString("paginaWebProveedor")
                ));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    return listaProveedores = FXCollections.observableArrayList(lista);    
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
        Proveedores registro = new Proveedores();
        registro.setCodigoProveedor(Integer.parseInt(txtCodigoP.getText()));
        registro.setNombreProveedor(txtNombreP.getText());
        registro.setApellidoProveedor(txtApellidoP.getText());
        registro.setNITProveedor(txtNit.getText());
        registro.setContactoPrincipal(txtContactoP.getText());
        registro.setDireccionProveedor(txtDireccionP.getText());
        registro.setRazonSocial(txtRazonSocialP.getText());
        registro.setPaginaWebProveedor(txtPaginaWP.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarProveedores(?, ?, ?, ?, ?, ?, ?, ?)}");          
            procedimiento.setInt(1, registro.getCodigoProveedor());
            procedimiento.setString(2, registro.getNITProveedor());
            procedimiento.setString(3, registro.getNombreProveedor());
            procedimiento.setString(4, registro.getApellidoProveedor());
            procedimiento.setString(5, registro.getDireccionProveedor());
            procedimiento.setString(6, registro.getRazonSocial());
            procedimiento.setString(7, registro.getContactoPrincipal());
            procedimiento.setString(8, registro.getPaginaWebProveedor());
            procedimiento.execute();
            listaProveedores.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void eliminar(){
        switch (tipoDeOperaciones){
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReportes.setDisable(false);
                imgAgregar.setImage(new Image("/org/fernandotomas/images/AgregarUsuario.png"));
                imgEliminar.setImage(new Image("/org/fernandotomas/images/EliminarUsuario.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
            default :
                if(tblProveedores.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"Confirmar si elimina el Registro",
                            "Eliminar Clientes",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarProveedores(?)}");
                            procedimiento.setInt(1, ((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getCodigoProveedor());
                            procedimiento.execute();
                            listaProveedores.remove(tblProveedores.getSelectionModel().getSelectedItem());
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }else
                    JOptionPane.showMessageDialog(null, "Seleccione un elemento.");
                break;
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
                if(tblProveedores.getSelectionModel().getSelectedItem()!= null){
                    btnEditar.setText("Actualizar");
                    btnReportes.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/fernandotomas/images/ActualizarUsuario.png"));
                    imgReporte.setImage(new Image("/org/fernandotomas/images/Cancelar.png"));
                    activarControles();
                    txtCodigoP.setEditable(false);
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
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarProveedores(?, ?, ?, ?, ?, ?, ?, ?)}");
            Proveedores registro = new Proveedores();
            registro.setCodigoProveedor(Integer.parseInt(txtCodigoP.getText()));
            registro.setNombreProveedor(txtNombreP.getText());
            registro.setApellidoProveedor(txtApellidoP.getText());
            registro.setNITProveedor(txtNit.getText());
            registro.setContactoPrincipal(txtContactoP.getText());
            registro.setDireccionProveedor(txtDireccionP.getText());
            registro.setRazonSocial(txtRazonSocialP.getText());
            registro.setPaginaWebProveedor(txtPaginaWP.getText());
            procedimiento.setInt(1, registro.getCodigoProveedor());
            procedimiento.setString(2, registro.getNITProveedor());
            procedimiento.setString(3, registro.getNombreProveedor());
            procedimiento.setString(4, registro.getApellidoProveedor());
            procedimiento.setString(5, registro.getDireccionProveedor());
            procedimiento.setString(6, registro.getRazonSocial());
            procedimiento.setString(7, registro.getContactoPrincipal());
            procedimiento.setString(8, registro.getPaginaWebProveedor());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
            
    
    public void desactivarControles(){
        txtCodigoP.setEditable(false);
        txtNombreP.setEditable(false);
        txtApellidoP.setEditable(false);
        txtDireccionP.setEditable(false);
        txtContactoP.setEditable(false);
        txtNit.setEditable(false);
        txtPaginaWP.setEditable(false);
        txtRazonSocialP.setEditable(false);
    }
     public void activarControles(){
        txtCodigoP.setEditable(true);
        txtNombreP.setEditable(true);
        txtApellidoP.setEditable(true);
        txtDireccionP.setEditable(true);
        txtContactoP.setEditable(true);
        txtNit.setEditable(true);
        txtPaginaWP.setEditable(true);
        txtRazonSocialP.setEditable(true);
     }
     public void limpiarControles(){
        txtCodigoP.clear();
        txtNombreP.clear();
        txtApellidoP.clear();
        txtContactoP.clear();
        txtPaginaWP.clear();
        txtNit.clear();
        txtDireccionP.clear();
        txtRazonSocialP.clear();
     }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipalProveedores;
    }
    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipalProveedores = escenarioPrincipal;
    }
    @FXML
    public void regresar(ActionEvent event){
         if (event.getSource() == btnRegresar){
        escenarioPrincipalProveedores.menuPrincipalView();
        }
    }  
}
