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
import org.fernandotomas.bean.Productos;
import org.fernandotomas.bean.Proveedores;
import org.fernandotomas.bean.TipoProducto;
import org.fernandotomas.dao.Conexion;
import org.fernandotomas.System.Principal;

/**
 *
 * @author lphg3
 */
public class MenuProductosController implements Initializable{

    
    private Principal escenarioPrincipal;
    private enum operaciones{AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoOperaciones = operaciones.NINGUNO;
    private ObservableList <Productos> listaProductos;
    private ObservableList <Proveedores> listaProveedores;
    private ObservableList <TipoProducto> listaTipoDeProducto;
    
    @FXML private Button btnRegresar;
    @FXML private TextField txtCodigoProd;
    @FXML private TextField txtDescPro;
    @FXML private TextField txtPrecioU;
    @FXML private TextField txtPrecioD;
    @FXML private TextField txtPrecioM;
    @FXML private TextField txtExistencia;
    @FXML private ComboBox cmbCodigoTipoP;
    @FXML private ComboBox cmbCodProv;
    @FXML private TableView tblProductos;
    @FXML private TableColumn colCodProd;
    @FXML private TableColumn colDescProd;
    @FXML private TableColumn colPrecioU;
    @FXML private TableColumn colPrecioD;
    @FXML private TableColumn colPrecioM;
    @FXML private TableColumn colExistencia;
    @FXML private TableColumn colCodTipoProd;
    @FXML private TableColumn colCodProv;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReportes;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoTipoP.setItems(getTipoP());
        cmbCodProv.setItems(getProveedores());
    }


    
    public void cargarDatos(){
    tblProductos.setItems(getProductos());
    colCodProd.setCellValueFactory(new PropertyValueFactory<Productos, String>("codigoProducto"));
    colDescProd.setCellValueFactory(new PropertyValueFactory<Productos, String>("descripcionProducto"));
    colPrecioU.setCellValueFactory(new PropertyValueFactory<Productos, Double>("precioUnitario"));
    colPrecioD.setCellValueFactory(new PropertyValueFactory<Productos, Double>("precioDocena"));
    colPrecioM.setCellValueFactory(new PropertyValueFactory<Productos, Double>("precioMayor"));
    colExistencia.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("existencia"));
    colCodTipoProd.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("codigoTipoProducto"));
    colCodProv.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("codigoProveedor"));
    
    
    }
    public void seleccionarElementos(){
       txtCodigoProd.setText(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto());
       txtDescPro.setText(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getDescripcionProducto());
       txtPrecioU.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getPrecioUnitario()));
       txtPrecioD.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getPrecioDocena()));
       txtPrecioM.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getPrecioMayor()));
       txtExistencia.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getExistencia()));
       cmbCodigoTipoP.getSelectionModel().select(buscarTipoProducto(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getCodigoTipoProducto()));
    }
    
    public TipoProducto buscarTipoProducto (int codigoTipoProducto ){
        TipoProducto resultado = null;
        try{
         PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarTipoProducto(?)}");
         procedimiento.setInt(1, codigoTipoProducto);
         ResultSet registro = procedimiento.executeQuery();
         while (registro.next()){
             resultado = new TipoProducto(registro.getInt("codigoTipoProducto"),
                                            registro.getString("descripcionProducto")
             
             );
         }
        }catch (Exception e)
        {
            e.printStackTrace();
        }    
    
        return resultado;
    }
    
    
    public ObservableList<Productos> getProductos(){
    ArrayList<Productos> lista = new ArrayList<Productos>();
    try{
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProductos()}");
        ResultSet resultado = procedimiento.executeQuery();
        while(resultado.next()){
            lista.add(new Productos (resultado.getString("codigoProducto"),
                                    resultado.getString("descripcionProducto"),
                                    resultado.getDouble("precioUnitario"),
                                    resultado.getDouble("precioDocena"),
                                    resultado.getDouble("precioMayor"),
                                    resultado.getInt("existencia"),
                                    resultado.getInt("codigoTipoProducto"),
                                    resultado.getInt("codigoProveedor")            
            ));
        }
    }catch (Exception e){
        e.printStackTrace();
    }
    
    
    return listaProductos = FXCollections.observableArrayList(lista);
        
    }
    public ObservableList<Proveedores> getProveedores() {
        ArrayList<Proveedores> listaPro = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProveedores()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                listaPro.add(new Proveedores(resultado.getInt("codigoProveedor"),
                        resultado.getString("NITProveedor"),
                        resultado.getString("nombreProveedor"),
                        resultado.getString("apellidoProveedor"),
                        resultado.getString("direccionProveedor"),
                        resultado.getString("razonSocial"),
                        resultado.getString("contactoPrincipal"),
                        resultado.getString("paginaWeb")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProveedores = FXCollections.observableList(listaPro);
    }
     public ObservableList<TipoProducto> getTipoP() {
        ArrayList<TipoProducto> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoProducto()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new TipoProducto(resultado.getInt("CodigoTipoProducto"),
                        resultado.getString("descripcion")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaTipoDeProducto = FXCollections.observableList(lista);
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
         Productos registro = new Productos();
         registro.setCodigoProducto(txtCodigoProd.getText());
         registro.setCodigoProveedor(((Proveedores)cmbCodProv.getSelectionModel().getSelectedItem()
                 ).getCodigoProveedor());
         registro.setCodigoTipoProducto(((TipoProducto)cmbCodigoTipoP.getSelectionModel().getSelectedItem())
                 .getCodigoTipoProducto());
         registro.setDescripcionProducto(txtDescPro.getText());
         registro.setPrecioDocena(Double.parseDouble(txtPrecioD.getText()));
         registro.setPrecioUnitario(Double.parseDouble(txtPrecioU.getText()));
         registro.setPrecioMayor(Double.parseDouble(txtPrecioM.getText()));
         registro.setExistencia(Integer.parseInt(txtExistencia.getText()));
         try {
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall
        ("{CALL sp_agregarProducto(?, ?, ?, ?, ?, ?, ?, ?)}");
        procedimiento.setString(1, registro.getCodigoProducto());
        procedimiento.setString(2, registro.getDescripcionProducto());
        procedimiento.setDouble(3, registro.getPrecioUnitario());
        procedimiento.setDouble(4, registro.getPrecioDocena());
        procedimiento.setDouble(5, registro.getPrecioMayor());
        procedimiento.setInt(6, registro.getExistencia());
        procedimiento.setInt(7, registro.getCodigoProveedor());
        procedimiento.setInt(8, registro.getCodigoTipoProducto());
        procedimiento.execute();
        
        listaProductos.add(registro);

         }catch (Exception e){
             e.printStackTrace();
         }
     
     }
    
     public void eliminar(){
        switch (tipoOperaciones){
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReportes.setDisable(false);
                tipoOperaciones = operaciones.NINGUNO;
                break;
            default :
                if(tblProductos.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"Confirmar si elimina el Registro",
                            "Eliminar Clientes",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarClientes(?)}");
                            procedimiento.setInt(1, ((Productos)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProveedor());
                            procedimiento.execute();
                            listaProductos.remove(tblProductos.getSelectionModel().getSelectedItem());
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }else
                    JOptionPane.showMessageDialog(null, "Seleccione un elemento.");
                break;
        } 
    }
     
     public void editar(){
        switch(tipoOperaciones){
            case NINGUNO:
                if(tblProductos.getSelectionModel().getSelectedItem()!= null){
                    btnEditar.setText("Actualizar");
                    btnReportes.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    txtCodigoProd.setEditable(false);
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
        Productos registro = new Productos();
         registro.setCodigoProducto(txtCodigoProd.getText());
         registro.setCodigoProveedor(((Proveedores)cmbCodProv.getSelectionModel().getSelectedItem()
                 ).getCodigoProveedor());
         registro.setCodigoTipoProducto(((TipoProducto)cmbCodigoTipoP.getSelectionModel().getSelectedItem())
                 .getCodigoTipoProducto());
         registro.setDescripcionProducto(txtDescPro.getText());
         registro.setPrecioDocena(Double.parseDouble(txtPrecioD.getText()));
         registro.setPrecioUnitario(Double.parseDouble(txtPrecioU.getText()));
         registro.setPrecioMayor(Double.parseDouble(txtPrecioM.getText()));
         registro.setExistencia(Integer.parseInt(txtExistencia.getText()));
         try {
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall
        ("{CALL sp_editarProducto(?, ?, ?, ?, ?, ?, ?, ?)}");
        procedimiento.setString(1, registro.getCodigoProducto());
        procedimiento.setString(2, registro.getDescripcionProducto());
        procedimiento.setDouble(3, registro.getPrecioUnitario());
        procedimiento.setDouble(4, registro.getPrecioDocena());
        procedimiento.setDouble(5, registro.getPrecioMayor());
        procedimiento.setInt(6, registro.getExistencia());
        procedimiento.setInt(7, registro.getCodigoProveedor());
        procedimiento.setInt(8, registro.getCodigoTipoProducto());
        procedimiento.execute();
        
        listaProductos.add(registro);

         }catch (Exception e){
             e.printStackTrace();
         }
     
    }
    public void desactivarControles(){
        txtCodigoProd.setEditable(false);
        txtDescPro.setEditable(false);
        txtPrecioU.setEditable(false);
        txtPrecioD.setEditable(false);
        txtPrecioM.setEditable(false);
        txtExistencia.setEditable(false);
        cmbCodigoTipoP.setDisable(true);
        cmbCodigoTipoP.setDisable(true);
    
    }
      public void activarControles(){
        txtCodigoProd.setEditable(true);
        txtDescPro.setEditable(true);
        txtPrecioU.setEditable(true);
        txtPrecioD.setEditable(true);
        txtPrecioM.setEditable(true);
        txtExistencia.setEditable(true);
        cmbCodigoTipoP.setDisable(false);
        cmbCodigoTipoP.setDisable(false);
    
    }
      public void limpiarControles(){
        txtCodigoProd.clear();
        txtDescPro.clear();
        txtPrecioU.clear();
        txtPrecioD.clear();
        txtPrecioM.clear();
        txtExistencia.clear();
        tblProductos.getSelectionModel().getSelectedItem();
        cmbCodigoTipoP.getSelectionModel().getSelectedItem();
        cmbCodigoTipoP.getSelectionModel().getSelectedItem();
    
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