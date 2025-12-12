package com.grupo5.carwashapp.models;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class Vehiculo implements Serializable {

    @Exclude
    private String uid;
    private String clienteId;
    private String placa;
    private String marca;
    private String modelo;
    private String color;
    private String tipo;

    public Vehiculo() {
    }

    public Vehiculo(String clienteId, String placa, String marca, String modelo, String color, String tipo) {
        this.clienteId = clienteId;
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.color = color;
        this.tipo = tipo;
    }

    // Getters y Setters
    @Exclude
    public String getUid() {
        return uid;
    }

    @Exclude
    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return modelo + "-" + placa;
    }
}
