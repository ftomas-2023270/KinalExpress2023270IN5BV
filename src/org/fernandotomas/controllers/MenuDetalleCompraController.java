/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.fernandotomas.controllers;

import java.io.IOException;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.fernandotomas.System.Principal;
import org.fernandotomas.bean.Compras;
import org.fernandotomas.bean.DetalleCompra;
import org.fernandotomas.bean.Productos;
import org.fernandotomas.dao.Conexion;

/**
 *
 * @author alfre
 */
public class MenuDetalleCompraController implements Initializable {

    private Principal escenarioPrincipal;

    private enum operaciones {
        AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO
    }
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<Productos> listaProducto;
    private ObservableList<DetalleCompra> listaDCompra;
    private ObservableList<Compras> listaCompras;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnReporte;
    @FXML
    private ImageView imgAgregar;
    @FXML
    private ImageView imgEditar;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgReporte;
    @FXML
    private Button btnBack;
    @FXML
    private ImageView imgBack;
    @FXML
    private TableColumn colDetalleCompra;
    @FXML
    private TableColumn colCantidad;
    @FXML
    private TableColumn colProducto;
    @FXML
    private TableColumn colCompra;
    @FXML
    private TextField txtDetalleCompraID;
    @FXML
    private TextField txtCantidad;
    @FXML
    private ComboBox cmbProducto;
    @FXML
    private ComboBox cmbCompra;
    @FXML
    private TableView tblDetalleCompra;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
        cmbProducto.setItems(getProductos());
        cmbCompra.setItems(getCompras());
    }

    public void cargarDatos() {
        tblDetalleCompra.setItems(getDetalleCompra());
        colDetalleCompra.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Integer>("detalleCompraId"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Integer>("cantidadCompra"));
        colProducto.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Integer>("productoId"));
        colCompra.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Integer>("compraId"));
    }

    public void seleccionarElemento() {
        txtDetalleCompraID.setText(String.valueOf(((DetalleCompra) tblDetalleCompra.getSelectionModel().getSelectedItem()).getCodigoDetalleCompra()));
        txtCantidad.setText(String.valueOf(((DetalleCompra) tblDetalleCompra.getSelectionModel().getSelectedItem()).getPrecioUnitario()));
        
    }
    
    /*
    public Productos buscarProducto(int codigoProducto) {
    Productos resultado = null;
    try {
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_buscarProductos(?)}");
        procedimiento.setInt(1, codigoProducto);
        ResultSet registro = procedimiento.executeQuery();
        while (registro.next()) {
            resultado = new Productos(
                    registro.getInt("productoId"),
                    registro.getString("nombreProducto"),
                    registro.getString("descripcionProducto"),
                    registro.getInt("cantidadStock"),
                    registro.getDouble("precioVentaUnitario"),
                    registro.getDouble("precioVentaMayor"),
                    registro.getDouble("precioCompra"),
                    registro.getInt("codigoProveedor"),
                    registro.getInt("categoriaProductoId")
            );
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return resultado;
}

*/
    public ObservableList<DetalleCompra> getDetalleCompra() {
        ArrayList<DetalleCompra> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_listarDetalleCompra()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new DetalleCompra(resultado.getInt("detalleCompraId"),
                        resultado.getInt("cantidadCompra"),
                        resultado.getInt("productoId"),
                        resultado.getInt("compraId")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaDCompra = FXCollections.observableArrayList(lista);

    }

    public ObservableList<Productos> getProductos() {
        ArrayList<Productos> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_listarProductos()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Productos(resultado.getInt("productoId"),
                        resultado.getString("nombreProducto"),
                        resultado.getString("descripcionProducto"),
                        resultado.getInt("cantidadStock"),
                        resultado.getDouble("precioVentaUnitario"),
                        resultado.getDouble("precioVentaMayor"),
                        resultado.getDouble("precioCompra"),
                        resultado.getInt("codigoProveedor"),
                        resultado.getInt("categoriaProductoId")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaProducto = FXCollections.observableArrayList(lista);

    }

    public ObservableList<Compras> getCompras() {
        ArrayList<Compras> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarCompras()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                java.sql.Date fecha = resultado.getDate("fechaCompra");
                LocalDate fechaDP = fecha.toLocalDate();
                lista.add(new Compras(resultado.getInt("compraId"),
                        fechaDP,
                        resultado.getString("totalCompra")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaCompras = FXCollections.observableArrayList(lista);

    }

    public void Agregar() {
        switch (tipoDeOperaciones) {
            case NINGUNO:
                activarControles();
                btnAgregar.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgAgregar.setImage(new Image("/org/harolrodriguez/images/Guardar.png"));
                imgEliminar.setImage(new Image("/org/harolrodriguez/images/Cancelar.png"));
                tipoDeOperaciones = operaciones.ACTUALIZAR;
                cargarDatos();
                break;
            case ACTUALIZAR:
                guardar();
                activarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgAgregar.setImage(new Image("/org/harolrodriguez/images/CarritoC.png"));
                imgEliminar.setImage(new Image("/org/harolrodriguez/images/EliminarCarrito.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                break;
        }

    }

    public void guardar() {
        DetalleCompra registro = new DetalleCompra();
        registro.setDetalleCompraId(Integer.parseInt(txtDetalleCompraID.getText()));
        registro.setCantidadCompra(Integer.parseInt(txtCantidad.getText()));
        registro.setProductoId(((Productos) cmbProducto.getSelectionModel().getSelectedItem()).getProductoId());
        registro.setCompraId(((Compras) cmbCompra.getSelectionModel().getSelectedItem()).getCompraId());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_agregarDetalleCompra(?, ?, ?, ?)}");
            procedimiento.setInt(1, registro.getDetalleCompraId());
            procedimiento.setInt(2, registro.getCantidadCompra());
            procedimiento.setInt(3, registro.getProductoId());
            procedimiento.setInt(4, registro.getCompraId());
            procedimiento.execute();
            listaDCompra.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminar() {
        switch (tipoDeOperaciones) {
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgAgregar.setImage(new Image("/org/harolrodriguez/images/CarritoC.png"));
                imgEliminar.setImage(new Image("/org/harolrodriguez/images/EliminarCarrito.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
            default:
                if (tblDetalleCompra.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmar si elimina el registro", "Eliminar Detalle Compra", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_NO_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_eliminarDetalleCompra(?) }");
                            procedimiento.setInt(1, ((DetalleCompra) tblDetalleCompra.getSelectionModel().getSelectedItem()).getDetalleCompraId());
                            procedimiento.execute();
                            listaProducto.remove(tblDetalleCompra.getSelectionModel().getSelectedItem());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, " Debe de Seleccionar un Elemento ");
                }
        }
    }

    public void editar() {
        switch (tipoDeOperaciones) {
            case NINGUNO:

                if (tblDetalleCompra.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/harolrodriguez/images/Guardar.png"));
                    imgReporte.setImage(new Image("/org/harolrodriguez/images/Reportes.png"));
                    activarControles();
                    txtDetalleCompraID.setEditable(false);
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un Elemento");
                }
                break;
            case ACTUALIZAR:

                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reportes");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/harolrodriguez/images/EditarC.png"));
                imgReporte.setImage(new Image("/org/harolrodriguez/images/Reportes.png"));
                desactivarControles();
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
        }
    }

    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_editarDetalleCompra(?, ?, ?, ?) }");
            DetalleCompra registro = (DetalleCompra) tblDetalleCompra.getSelectionModel().getSelectedItem();
            registro.setDetalleCompraId(Integer.parseInt(txtDetalleCompraID.getText()));
            registro.setCantidadCompra(Integer.parseInt(txtCantidad.getText()));
            registro.setProductoId(((Productos) cmbProducto.getSelectionModel().getSelectedItem()).getProductoId());
            registro.setCompraId(((Compras) cmbCompra.getSelectionModel().getSelectedItem()).getCompraId());
            procedimiento.setInt(1, registro.getDetalleCompraId());
            procedimiento.setInt(2, registro.getCantidadCompra());
            procedimiento.setInt(3, registro.getProductoId());
            procedimiento.setInt(4, registro.getCompraId());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reporte() {
        switch (tipoDeOperaciones) {
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/harolrodriguez/images/EditarC.png"));
                imgReporte.setImage(new Image("/org/harolrodriguez/images/Reportes.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
        }
    }

    public void desactivarControles() {
        txtDetalleCompraID.setEditable(false);
        txtCantidad.setEditable(false);
    }

    public void activarControles() {
        txtDetalleCompraID.setEditable(true);
        txtCantidad.setEditable(true);

    }

    public void limpiarControles() {
        txtDetalleCompraID.clear();
        txtCantidad.clear();

    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @FXML
    public void clickAtras(ActionEvent event) throws IOException {
        if (event.getSource() == btnBack) {
            escenarioPrincipal.menuPrincipalView();
        } else if (event.getSource() == imgBack) {
            escenarioPrincipal.menuPrincipalView();
        }
    }
}
