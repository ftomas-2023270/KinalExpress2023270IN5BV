/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.fernandotomas.bean;

/**
 *
 * @author alfre
 */
public class EmailProveedores {
    private int codigoEmailProveedor;
    private String emailProveedor;
    private String descripcion;
    private int proveedores_codigoProveedores;

    public EmailProveedores() {
    }

    public EmailProveedores(int codigoEmailProveedor, String emailProveedor, String descripcion, int proveedores_codigoProveedores) {
        this.codigoEmailProveedor = codigoEmailProveedor;
        this.emailProveedor = emailProveedor;
        this.descripcion = descripcion;
        this.proveedores_codigoProveedores = proveedores_codigoProveedores;
    }

    public int getCodigoEmailProveedor() {
        return codigoEmailProveedor;
    }

    public void setCodigoEmailProveedor(int codigoEmailProveedor) {
        this.codigoEmailProveedor = codigoEmailProveedor;
    }

    public String getEmailProveedor() {
        return emailProveedor;
    }

    public void setEmailProveedor(String emailProveedor) {
        this.emailProveedor = emailProveedor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getProveedores_codigoProveedores() {
        return proveedores_codigoProveedores;
    }

    public void setProveedores_codigoProveedores(int proveedores_codigoProveedores) {
        this.proveedores_codigoProveedores = proveedores_codigoProveedores;
    }
    
    @Override
    public String toString() {
        return getCodigoEmailProveedor() +  " -  " + getEmailProveedor() ;
    }
}
