package com.grupo5.carwashapp.models;

import com.google.firebase.database.Exclude;
import com.grupo5.carwashapp.models.enums.Estados;

import java.io.Serializable;

public class CatalogoServicio implements Serializable {
    @Exclude
    private String uid;
    private String nombre;
    private String descripcion;
    private double precio;
    private Estados estado;

    public CatalogoServicio() {
    }

    public CatalogoServicio(String nombre, String descripcion, double precio, Estados estado) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.estado = estado;
    }

    @Exclude
    public String getUid() {
        return uid;
    }

    @Exclude
    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Estados getEstado() {
        return estado;
    }

    public void setEstado(Estados estado) {
        this.estado = estado;
    }
}
