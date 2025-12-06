package com.grupo5.carwashapp.models;

public class Factura {
    private int id_factura;
    private String fecha_emision;
    private double IVA;
    private double total_factura;
    private String estado_factura;
    private int id_cliente;

    public Factura() {
    }

    public Factura(String fecha_emision, double IVA, double total_factura,
                   String estado_factura, int id_cliente) {
        this.fecha_emision = fecha_emision;
        this.IVA = IVA;
        this.total_factura = total_factura;
        this.estado_factura = estado_factura;
        this.id_cliente = id_cliente;
    }

    // Getters y Setters
    public int getId_factura() {
        return id_factura;
    }

    public void setId_factura(int id_factura) {
        this.id_factura = id_factura;
    }

    public String getFecha_emision() {
        return fecha_emision;
    }

    public void setFecha_emision(String fecha_emision) {
        this.fecha_emision = fecha_emision;
    }

    public double getIVA() {
        return IVA;
    }

    public void setIVA(double IVA) {
        this.IVA = IVA;
    }

    public double getTotal_factura() {
        return total_factura;
    }

    public void setTotal_factura(double total_factura) {
        this.total_factura = total_factura;
    }

    public String getEstado_factura() {
        return estado_factura;
    }

    public void setEstado_factura(String estado_factura) {
        this.estado_factura = estado_factura;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }
}
