package com.grupo5.carwashapp.models;

public class DetalleFactura {
    private int id_detalle;
    private int id_factura;
    private int id_servicio;
    private double subtotal;

    public DetalleFactura() {
    }

    public DetalleFactura(int id_factura, int id_servicio, double subtotal) {
        this.id_factura = id_factura;
        this.id_servicio = id_servicio;
        this.subtotal = subtotal;
    }

    // Getters y Setters
    public int getId_detalle() {
        return id_detalle;
    }

    public void setId_detalle(int id_detalle) {
        this.id_detalle = id_detalle;
    }

    public int getId_factura() {
        return id_factura;
    }

    public void setId_factura(int id_factura) {
        this.id_factura = id_factura;
    }

    public int getId_servicio() {
        return id_servicio;
    }

    public void setId_servicio(int id_servicio) {
        this.id_servicio = id_servicio;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}
