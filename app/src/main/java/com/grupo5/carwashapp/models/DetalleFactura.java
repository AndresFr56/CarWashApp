package com.grupo5.carwashapp.models;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class DetalleFactura implements Serializable {
    @Exclude
    private String servicioId;
    private String nombreServicio;
    private double precioUnitario;
    private int cantidad;
    private double subtotal;

    public DetalleFactura() {
    }

    public DetalleFactura(String servicioId, String nombreServicio, double precioUnitario, int cantidad) {
        this.servicioId = servicioId;
        this.nombreServicio = nombreServicio;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
        this.subtotal = precioUnitario * cantidad;
    }

    // Getters y Setters
    public String getServicioId() {
        return servicioId;
    }

    public void setServicioId(String servicioId) {
        this.servicioId = servicioId;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}
