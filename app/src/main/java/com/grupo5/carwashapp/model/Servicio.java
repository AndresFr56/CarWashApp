package com.grupo5.carwashapp.model;

public class Servicio {

    private int idServicio;
    private int idTipoServicio;         // Relación con tipo_servicio
    private String fechaHora;
    private double costo;

    private String indicaciones;        // Lo que pide el cliente
    private String descripcionServicio; // Descripción del tipo de servicio elegido

    private String estadoServicio;
    private int idVehiculo;
    private int idEmpleado;

    public Servicio() {}

    public Servicio(int idServicio, int idTipoServicio, String fechaHora, double costo,
                    String indicaciones, String descripcionServicio,
                    String estadoServicio, int idVehiculo, int idEmpleado) {

        this.idServicio = idServicio;
        this.idTipoServicio = idTipoServicio;
        this.fechaHora = fechaHora;
        this.costo = costo;
        this.indicaciones = indicaciones;
        this.descripcionServicio = descripcionServicio;
        this.estadoServicio = estadoServicio;
        this.idVehiculo = idVehiculo;
        this.idEmpleado = idEmpleado;
    }

    public int getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(int idServicio) {
        this.idServicio = idServicio;
    }

    public int getIdTipoServicio() {
        return idTipoServicio;
    }

    public void setIdTipoServicio(int idTipoServicio) {
        this.idTipoServicio = idTipoServicio;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public String getIndicaciones() {
        return indicaciones;
    }

    public void setIndicaciones(String indicaciones) {
        this.indicaciones = indicaciones;
    }

    public String getDescripcionServicio() {
        return descripcionServicio;
    }

    public void setDescripcionServicio(String descripcionServicio) {
        this.descripcionServicio = descripcionServicio;
    }

    public String getEstadoServicio() {
        return estadoServicio;
    }

    public void setEstadoServicio(String estadoServicio) {
        this.estadoServicio = estadoServicio;
    }

    public int getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(int idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }
}
