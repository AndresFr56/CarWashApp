package com.grupo5.carwashapp.models.dtos.catalogoServicio;

import com.grupo5.carwashapp.models.enums.Estados;

public class CatalogoServicioCreateDto {
    private final Estados estado = Estados.ACTIVO;
    private String nombre;
    private String descripcion;
    private double precio;

    public CatalogoServicioCreateDto() {
    }

    public CatalogoServicioCreateDto(String nombre, String descripcion, double precio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
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
}
