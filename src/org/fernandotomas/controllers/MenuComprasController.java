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
import org.fernandotomas.bean.Compras;

/**
 * FXML Controller class
 *
 * @author informatica
 */
public class MenuComprasController implements Initializable {
    private ObservableList<Compras> listaCompras;
    private Principal escenarioPrincipalCompras;
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones  tipoDeOperaciones = operaciones.NINGUNO;
    @FXML Button btnRegresar;
    @FXML private TextField txtNumeroD;
    @FXML private TextField txtFechaD;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtTotalD;
    @FXML private TableView tblCompras;
    @FXML private TableColumn colNumeroD;
    @FXML private TableColumn colFechaD;
    @FXML private TableColumn colDescripcion;
    @FXML private TableColumn colTotalD;
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
        tblCompras.setItems(getCompras());
        colNumeroD.setCellValueFactory(new PropertyValueFactory<Compras, Integer>("numeroDocumento"));
        colFechaD.setCellValueFactory(new PropertyValueFactory<Compras, String>("fechaDocumento"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Compras, String>("descripcion"));
        colTotalD.setCellValueFactory(new PropertyValueFactory<Compras, Double>("totalDocumento"));
    }
    
    public void seleccionarElemento(){
        txtNumeroD.setText(String.valueOf(((Compras)tblCompras.getSelectionModel().getSelectedItem()).getNumeroDocumento()));
        txtFechaD.setText(((Compras)tblCompras.getSelectionModel().getSelectedItem()).getFechaDocumento());
        txtDescripcion.setText(((Compras)tblCompras.getSelectionModel().getSelectedItem()).getDescripcion());
        txtTotalD.setText(((Compras)tblCompras.getSelectionModel().getSelectedItem()).getTotalDocumento());
    }
    
    public ObservableList <Compras> getCompras (){
        ArrayList <Compras> lista = new ArrayList();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarCompras()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Compras (resultado.getInt("numeroDocumento"),
                        resultado.getString("fechaDocumento"),
                        resultado.getString("descripcion"),
                        resultado.getString("totalDocumento")
                ));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    return listaCompras = FXCollections.observableArrayList(lista);    
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
        Compras registro = new Compras();
        registro.setNumeroDocumento(Integer.parseInt(txtNumeroD.getText()));
        registro.setFechaDocumento(txtFechaD.getText());
        registro.setDescripcion(txtDescripcion.getText());
        registro.setTotalDocumento(txtTotalD.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarCompras(?, ?, ?, ?)}");          
            procedimiento.setInt(1, registro.getNumeroDocumento());
            procedimiento.setString(2, registro.getFechaDocumento());
            procedimiento.setString(3, registro.getDescripcion());
            procedimiento.setString(4, registro.getTotalDocumento());
            procedimiento.execute();
            listaCompras.add(registro);
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
                if(tblCompras.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"Confirmar si elimina el Registro",
                            "Eliminar Clientes",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarCompras(?)}");
                            procedimiento.setInt(1, ((Compras)tblCompras.getSelectionModel().getSelectedItem()).getNumeroDocumento());
                            procedimiento.execute();
                            listaCompras.remove(tblCompras.getSelectionModel().getSelectedItem());
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
                if(tblCompras.getSelectionModel().getSelectedItem()!= null){
                    btnEditar.setText("Actualizar");
                    btnReportes.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/fernandotomas/images/ActualizarUsuario.png"));
                    imgReporte.setImage(new Image("/org/fernandotomas/images/Cancelar.png"));
                    activarControles();
                    txtNumeroD.setEditable(false);
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarCompras(?, ?, ?, ?)}");
            Compras registro = (Compras)tblCompras.getSelectionModel().getSelectedItem();
            registro.setFechaDocumento(txtFechaD.getText());
            registro.setDescripcion(txtDescripcion.getText()); 
            registro.setTotalDocumento(txtTotalD.getText()); 
            procedimiento.setInt(1, registro.getNumeroDocumento());
            procedimiento.setString(2, registro.getFechaDocumento());
            procedimiento.setString(3, registro.getDescripcion());
            procedimiento.setDouble(4, registro.getNumeroDocumento());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
            
    
    public void desactivarControles(){
        txtNumeroD.setEditable(false);
        txtFechaD.setEditable(false);
        txtDescripcion.setEditable(false);
        txtTotalD.setEditable(false);
 
        
    }
     public void activarControles(){
        txtNumeroD.setEditable(true);
        txtFechaD.setEditable(true);
        txtDescripcion.setEditable(true);
        txtTotalD.setEditable(true);
     }
     public void limpiarControles(){
        txtNumeroD.clear();
        txtFechaD.clear();
        txtDescripcion.clear();
        txtTotalD.clear();
     }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipalCompras;
    }
    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipalCompras = escenarioPrincipal;
    }
    @FXML
    public void regresar(ActionEvent event){
         if (event.getSource() == btnRegresar){
        escenarioPrincipalCompras.menuPrincipalView();
        }
    }  
}
