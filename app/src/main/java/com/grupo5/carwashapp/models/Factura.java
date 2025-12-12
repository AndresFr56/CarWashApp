package com.grupo5.carwashapp.models;

import com.google.firebase.database.Exclude;
import com.grupo5.carwashapp.models.enums.EstadoFacturas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Factura implements Serializable {
    @Exclude
    private String uid;
    private String fechaEmision;
    private double subtotal;
    private double iva;
    private double total;
    private EstadoFacturas estado;

    // Datos del cliente
    private String clienteId;
    private String clienteNombre;
    private String clienteCedula;

    // Datos del vehículo
    private String vehiculoId;
    private String vehiculoPlaca;
    private String vehiculoModelo;

    private List<DetalleFactura> detalles;

    public Factura() {
        this.detalles = new ArrayList<>();
    }

    public Factura(String fechaEmision, double subtotal,double iva, double total, EstadoFacturas estado,
                   String clienteId, String clienteNombre, String clienteCedula,
                   String vehiculoId, String vehiculoPlaca, List<DetalleFactura> detalles) {
        this.fechaEmision = fechaEmision;
        this.subtotal = subtotal;
        this.iva = iva;
        this.total = total;
        this.estado = estado;

        this.clienteId = clienteId;
        this.clienteNombre = clienteNombre;
        this.clienteCedula = clienteCedula;

        this.vehiculoId = vehiculoId;
        this.vehiculoPlaca = vehiculoPlaca;

        this.detalles = detalles;
    }

    @Exclude
    public String getUid() {
        return uid;
    }

    @Exclude
    public void setUid(String uid) {
        this.uid = uid;
    }

    // Getters y Setters
    public String getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public EstadoFacturas getEstado() {
        return estado;
    }

    public void setEstado(EstadoFacturas estado) {
        this.estado = estado;
    }

    // Getters del Cliente
    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public String getClienteCedula() {
        return clienteCedula;
    }

    public void setClienteCedula(String clienteCedula) {
        this.clienteCedula = clienteCedula;
    }

    // Getters del Vehiculo
    public String getVehiculoId() {
        return vehiculoId;
    }

    public void setVehiculoId(String vehiculoId) {
        this.vehiculoId = vehiculoId;
    }

    public String getVehiculoPlaca() {
        return vehiculoPlaca;
    }

    public void setVehiculoPlaca(String vehiculoPlaca) {
        this.vehiculoPlaca = vehiculoPlaca;
    }

    public String getVehiculoModelo() {
        return vehiculoModelo;
    }

    public void setVehiculoModelo(String vehiculoModelo) {
        this.vehiculoModelo = vehiculoModelo;
    }

    // Getter y Setter de la lista
    public List<DetalleFactura> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleFactura> detalles) {
        this.detalles = detalles;
    }
}

