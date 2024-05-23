/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.fernandotomas.bean;

/**
 *
 * @author alfre
 */
public class CargoEmp {
    private int codigoCargoEmpleado;
    private String nombreCargo;
    private String descripcionCargo;

    public CargoEmp() {
    }

    public CargoEmp(int codigoCargoEmpleado, String nombreCargo, String descripcionCargo) {
        this.codigoCargoEmpleado = codigoCargoEmpleado;
        this.nombreCargo = nombreCargo;
        this.descripcionCargo = descripcionCargo;
    }

    public int getCodigoCargoEmpleado() {
        return codigoCargoEmpleado;
    }

    public void setCodigoCargoEmpleado(int codigoCargoEmpleado) {
        this.codigoCargoEmpleado = codigoCargoEmpleado;
    }

    public String getNombreCargo() {
        return nombreCargo;
    }
    
    public void setNombreCargo(String nombreCargo) {
        this.nombreCargo = nombreCargo;
    }

    public String getDescripcionCargo() {
        return descripcionCargo;
    }

    public void setDescripcionCargo(String descripcionCargo) {
        this.descripcionCargo = descripcionCargo;
    }
    
    @Override
    public String toString() {
        return getCodigoCargoEmpleado() +  " -  " + getNombreCargo() ;
    }
}
