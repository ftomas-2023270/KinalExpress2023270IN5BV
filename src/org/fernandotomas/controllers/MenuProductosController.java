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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.fernandotomas.System.Principal;
import org.fernandotomas.bean.Productos;
import org.fernandotomas.bean.Proveedores;
import org.fernandotomas.bean.TipoProducto;
import org.fernandotomas.db.Conexion;

/**
 *
 * @author informatica
 */
public class MenuProductosController implements Initializable{

    private Principal escenarioPrincipal;
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones  tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList <Productos>listaProducto;
    private ObservableList <Proveedores>listaProveedores;
    private ObservableList <TipoProducto>listaTipoProducto;
    
    @FXML private Button btnRegresar ;
    @FXML private TextField txtCodigoProd ;
    @FXML private TextField txtDescProd ;
    @FXML private TextField txtPrecioU ;
    @FXML private TextField txtPrecioD ;
    @FXML private TextField txtPrecioM ;
    @FXML private TextField txtExistencia ;
    @FXML private ComboBox cmbCodigoTipoP;
    @FXML private ComboBox cmbCodigoProv;
    @FXML private TableView tblProductos ;
    @FXML private TableColumn colCdoProd;
    @FXML private TableColumn colDescProd;
    @FXML private TableColumn colPrecioU;
    @FXML private TableColumn colPrecioD;
    @FXML private TableColumn colPrecioM;
    @FXML private TableColumn colExistencia;
    @FXML private TableColumn colCodTipoProd;
    @FXML private TableColumn colCodProv;
    @FXML private Button btnAgregar ;
    @FXML private Button btnEliminar ;
    @FXML private Button btnEditar ;
    @FXML private Button btnReportes ;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoTipoP.setItems(getTipoProducto());
        cmbCodigoProv.setItems(getProveedores());
    }
    
    public void cargarDatos(){
        tblProductos.setItems(getProductos());
        colCdoProd.setCellValueFactory(new PropertyValueFactory<Productos,String>("codigoProducto"));
        colDescProd.setCellValueFactory(new PropertyValueFactory<Productos,String>("descripcionProducto"));
        colPrecioU.setCellValueFactory(new PropertyValueFactory<Productos,Double>("precioUnitario"));
        colPrecioD.setCellValueFactory(new PropertyValueFactory<Productos,Double>("precioDocena"));
        colPrecioM.setCellValueFactory(new PropertyValueFactory<Productos,Double>("precioMayor"));
        colExistencia.setCellValueFactory(new PropertyValueFactory<Productos,Integer>("existencia"));
        colCodTipoProd.setCellValueFactory(new PropertyValueFactory<Productos,Integer>("codigoTipoProducto"));
        colCodProv.setCellValueFactory(new PropertyValueFactory<Productos,Integer>("codigoProveedor"));     
    }
    
    public void seleccionarElemento(){
        txtCodigoProd.setText(value);
    }
    
    public TipoProducto buscarTipoP(int codigoTipoProducto){
        TipoProducto resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_buscarTipoProducto(?)}");
            procedimiento.setInt(1, codigoTipoProducto);
            ResultSet registro = procedimiento.executeQuery();
                while(registro.next()){
                    resultado = new TipoProducto(registro.getInt("codigoTipoProducto"),
                    registro.getString("descripcion");
                }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public ObservableList <Productos> getProductos (){
        ArrayList <Productos> lista = new ArrayList();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoProducto()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Productos (resultado.getString("codigoProducto"),
                        resultado.getString("descripcionProducto"),
                        resultado.getDouble("precioUnitario"),
                        resultado.getDouble("precioDocena"),
                        resultado.getDouble("precioMayor"),
                        resultado.getString("imagenProducto"),
                        resultado.getInt("existencia"),
                        resultado.getInt("codigoTipoProducto"),
                        resultado.getInt("codigoProveedor")
                ));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
            return listaProducto = FXCollections.observableArrayList(lista);    
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
        switch(tipoDeOperaciones){
            case NINGUNO:
                activarControler();
                btnAgregar.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReportes.setDisable(true);
                tipoDeOperaciones = operaciones.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReportes.setDisable(true);
                tipoDeOperaciones = operaciones.ACTUALIZAR;
                break;
        }
    }
    
    public void guardar(){
        Productos registro = new Productos();
        registro.setCodigoProducto(txtCodigoProd.getText());
        registro.setCodigoProveedor(((Proveedores)cmbCodigoProv.getSelectionModel().getSelectedItem()).getCodigoProveedor());
        registro.setCodigoTipoProducto(((TipoProducto)cmbCodigoTipoP.getSelectionModel().getSelectedItem()).getCodigoTipoProducto());
        registro.setDescripcionProducto(txtDescProd.getText());
        registro.setPrecioUnitario(Double.parseDouble(txtPrecioU.getText()));
        registro.setPrecioDocena(Double.parseDouble(txtPrecioD.getText()));
        registro.setPrecioMayor(Double.parseDouble(txtPrecioM.getText()));
        registro.setExistencia(Integer.parseInt(txtExistencia.getText()));
        try{
            PreparedStatement
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void activarControler(){
        txtCodigoProd.setDisable(true);
        txtDescProd.setDisable(true);
        txtPrecioU.setDisable(true);
        txtPrecioD.setDisable(true);
        txtPrecioM.setDisable(true);
        txtExistencia.setDisable(true);
        cmbCodigoTipoP.setDisable(true);
        cmbCodigoProv.setDisable(true);
    }
    
    public void desactivarControles(){
        txtCodigoProd.setDisable(false);
        txtDescProd.setDisable(false);
        txtPrecioU.setDisable(false);
        txtPrecioD.setDisable(false);
        txtPrecioM.setDisable(false);
        txtExistencia.setDisable(false);
        cmbCodigoTipoP.setDisable(false);
        cmbCodigoProv.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoProd.clear();
        txtDescProd.clear();
        txtPrecioU.clear();
        txtPrecioD.clear();
        txtPrecioM.clear();
        txtExistencia.clear();
        cmbCodigoTipoP.selectionModelProperty();
        cmbCodigoProv.selectionModelProperty();
    }
}
