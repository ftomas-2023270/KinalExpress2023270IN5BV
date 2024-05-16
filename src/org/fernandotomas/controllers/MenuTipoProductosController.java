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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.fernandotomas.db.Conexion;
import org.fernandotomas.System.Principal;
import org.fernandotomas.bean.TipoProducto;

/**
 * FXML Controller class
 *
 * @author informatica
 */
public class MenuTipoProductosController implements Initializable {
    private ObservableList<TipoProducto> listaTipoProducto;
    private Principal escenarioPrincipalTipoProducto;
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones  tipoDeOperaciones = operaciones.NINGUNO;
    @FXML Button btnRegresar;
    @FXML private TextField txtCodigoTP;
    @FXML private TextField txtDescripcion;
    @FXML private TableView tblTipoProducto;
    @FXML private TableColumn colCodigoTP;
    @FXML private TableColumn colDescripcion;
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
        tblTipoProducto.setItems(getTipoProducto());
        colCodigoTP.setCellValueFactory(new PropertyValueFactory<TipoProducto, Integer>("codigoTipoProducto"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<TipoProducto, String>("descripcion"));
    }
    
    public void seleccionarElemento(){
        txtCodigoTP.setText(String.valueOf(((TipoProducto)tblTipoProducto.getSelectionModel().getSelectedItem()).getCodigoTipoProducto()));
        txtDescripcion.setText(((TipoProducto)tblTipoProducto.getSelectionModel().getSelectedItem()).getDescripcion());
    }
    
    public ObservableList <TipoProducto> getTipoProducto (){
        ArrayList <TipoProducto> lista = new ArrayList();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoProducto()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new TipoProducto (resultado.getInt("codigoTipoProducto"),
                        resultado.getString("descripcion")
                ));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    return listaTipoProducto = FXCollections.observableArrayList(lista);    
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
        TipoProducto registro = new TipoProducto();
        registro.setCodigoTipoProducto(Integer.parseInt(txtCodigoTP.getText()));
        registro.setDescripcion(txtDescripcion.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarTipoProducto(?, ?)}");          
            procedimiento.setInt(1, registro.getCodigoTipoProducto());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.execute();
            listaTipoProducto.add(registro);
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
                if(tblTipoProducto.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"Confirmar si elimina el Registro",
                            "Eliminar Clientes",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarTipoProducto(?)}");
                            procedimiento.setInt(1, ((TipoProducto)tblTipoProducto.getSelectionModel().getSelectedItem()).getCodigoTipoProducto());
                            procedimiento.execute();
                            listaTipoProducto.remove(tblTipoProducto.getSelectionModel().getSelectedItem());
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
                imgEliminar.setImage(new Image("/org/fernandotomas/images/Cancelar.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
            case NINGUNO:
                desactivarControles();
                limpiarControles();
                btnEditar.setText("Editar");
                btnReportes.setText("Reportes");
                btnAgregar.setDisable(true);
                btnEliminar.setDisable(true);
                imgAgregar.setImage(new Image("/org/fernandotomas/images/ActualizarUsuario.png"));
                imgEliminar.setImage(new Image("/org/fernandotomas/images/Registros.png"));
                tipoDeOperaciones = operaciones.ACTUALIZAR;
                break;
        }
    }
    
    public void editar(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                if(tblTipoProducto.getSelectionModel().getSelectedItem()!= null){
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
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarTipoProducto(?, ?)}");
            TipoProducto registro = (TipoProducto)tblTipoProducto.getSelectionModel().getSelectedItem();
            registro.setDescripcion(txtDescripcion.getText()); 
            procedimiento.setInt(1, registro.getCodigoTipoProducto());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
            
    
    public void desactivarControles(){
        txtCodigoTP.setEditable(false);
        txtDescripcion.setEditable(false);
 
        
    }
     public void activarControles(){
        txtCodigoTP.setEditable(true);
        txtDescripcion.setEditable(true);
     }
     public void limpiarControles(){
        txtCodigoTP.clear();
        txtDescripcion.clear();
     }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipalTipoProducto;
    }
    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipalTipoProducto = escenarioPrincipal;
    }
    @FXML
    public void regresar(ActionEvent event){
         if (event.getSource() == btnRegresar){
        escenarioPrincipalTipoProducto.menuPrincipalView();
        }
    }  
}
