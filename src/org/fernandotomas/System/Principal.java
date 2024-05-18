package org.fernandotomas.System;
 
import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.fernandotomas.controllers.MenuCargoEmpController;
import org.fernandotomas.controllers.MenuClientesController;
import org.fernandotomas.controllers.MenuComprasController;
import org.fernandotomas.controllers.MenuPrincipalController;
import org.fernandotomas.controllers.MenuProductosController;
import org.fernandotomas.controllers.ProgramadorController;
import org.fernandotomas.controllers.MenuProveedoresController;
import org.fernandotomas.controllers.MenuTipoProductosController;
 
/*
Nombre: Fernando Javier Tomas Velasquez
Fecha de creacion: 11/04
Fecha de actualizacion: 11/04
 
*/
 
public class Principal extends Application{
    private Stage escenarioPrincipal;
    private Scene escena;
    private final String URLVIEW = "/org/fernandotomas/views/";
        /*
        FXMLLoades, Parent
        */
        @Override
        public void start(Stage escenarioPrincipal) throws IOException {
            this.escenarioPrincipal = escenarioPrincipal;
            this.escenarioPrincipal.setTitle("Mega Market");
            menuPrincipalView();
            //Parent root = FXMLLoader.load(getClass().getResource("/org/adrianmorataya/views/MenuPrincipalView.fxml"));
            //Scene escena = new Scene(root);
            //escenarioPrincipal.setScene(escena);
            escenarioPrincipal.show();
        }
        public Initializable cambiarEscena(String fxmlName, int width, int heigth) throws IOException{
            Initializable resultado = null;
            FXMLLoader loader = new FXMLLoader();
            InputStream file = Principal.class.getResourceAsStream(URLVIEW + fxmlName);
            loader.setBuilderFactory(new JavaFXBuilderFactory());
            loader.setLocation(Principal.class.getResource(URLVIEW + fxmlName));
            escena = new Scene((AnchorPane)loader.load(file), width, heigth);
            escenarioPrincipal.setScene(escena);
            escenarioPrincipal.sizeToScene();
            resultado = (Initializable)loader.getController();
            return resultado;
        }
        public void menuPrincipalView(){
            try{
                MenuPrincipalController menuPrincipalView = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml", 1036, 582);
                menuPrincipalView.setEscenarioPrincipal( this);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        public void menuClientesView(){
            try{
                MenuClientesController menuClientesView = (MenuClientesController)cambiarEscena("MenuClientesView.fxml", 1036, 583);
                menuClientesView.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        public void programadorView(){
            try{
                ProgramadorController programadorView = (ProgramadorController)cambiarEscena("ProgramadorView.fxml", 759, 427);
                programadorView.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        public void menuProveedoresView(){
            try{
                MenuProveedoresController menuProveedoresView = (MenuProveedoresController)cambiarEscena("MenuProveedoresView.fxml", 1036, 583);
                menuProveedoresView.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        public void menuCargoEmpView(){
            try{
                MenuCargoEmpController menuCargoEmpView = (MenuCargoEmpController)cambiarEscena("MenuCargoEmpView.fxml", 1036, 583);
                menuCargoEmpView.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        public void menuTipoProductoView(){
            try{
                MenuTipoProductosController menuTipoProductoView = (MenuTipoProductosController)cambiarEscena("MenuTipoProductosView.fxml", 1036, 583);
                menuTipoProductoView.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        public void menuComprasView(){
            try{
                MenuComprasController menuComprasView = (MenuComprasController)cambiarEscena("MenuComprasView.fxml", 1036, 583);
                menuComprasView.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        public void menuProductosView(){
            try{
                MenuProductosController menuProductosView = (MenuProductosController)cambiarEscena("MenuProductosView.fxml", 1036, 583);
                menuProductosView.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        public static void main(String args[]){
            launch(args);
        }
}