package com.grupo5.carwashapp.models;

public class TipoServicio {

    private int idTipoServicio;
    private String nombre;
    private String descripcion;
    private double precio;

    public TipoServicio() {}

    public TipoServicio(int idTipoServicio, String nombre, String descripcion, double precio) {
        this.idTipoServicio = idTipoServicio;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    public int getIdTipoServicio() {
        return idTipoServicio;
    }

    public void setIdTipoServicio(int idTipoServicio) {
        this.idTipoServicio = idTipoServicio;
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

    @Override
    public String toString() {
        return nombre;  // útil para mostrar en Spinner
    }
}

