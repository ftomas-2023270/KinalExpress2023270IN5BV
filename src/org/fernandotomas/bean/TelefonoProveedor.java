/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.fernandotomas.bean;

/**
 *
 * @author alfre
 */
public class TelefonoProveedor {
    private int codigoTelefonoProveedor;
    private String telefonoPrincipal;
    private String telefonoSecundario;
    private String observaciones;
    private int codigoProveedores;

    public TelefonoProveedor() {
    }

    public TelefonoProveedor(int codigoTelefonoProveedor, String telefonoPrincipal, String telefonoSecundario, String observaciones, int codigoProveedores) {
        this.codigoTelefonoProveedor = codigoTelefonoProveedor;
        this.telefonoPrincipal = telefonoPrincipal;
        this.telefonoSecundario = telefonoSecundario;
        this.observaciones = observaciones;
        this.codigoProveedores = codigoProveedores;
    }

    public int getCodigoTelefonoProveedor() {
        return codigoTelefonoProveedor;
    }

    public void setCodigoTelefonoProveedor(int codigoTelefonoProveedor) {
        this.codigoTelefonoProveedor = codigoTelefonoProveedor;
    }

    public String getTelefonoPrincipal() {
        return telefonoPrincipal;
    }

    public void setTelefonoPrincipal(String telefonoPrincipal) {
        this.telefonoPrincipal = telefonoPrincipal;
    }

    public String getTelefonoSecundario() {
        return telefonoSecundario;
    }

    public void setTelefonoSecundario(String telefonoSecundario) {
        this.telefonoSecundario = telefonoSecundario;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public int getCodigoProveedores() {
        return codigoProveedores;
    }

    public void setCodigoProveedores(int codigoProveedores) {
        this.codigoProveedores = codigoProveedores;
    }
    
    @Override
    public String toString() {
        return getCodigoTelefonoProveedor() +  " -  " + getTelefonoPrincipal() ;
    }
}
