package com.grupo5.carwashapp.models;

import com.grupo5.carwashapp.models.enums.EstadoServicio;

public class Servicio {

    private String id_servicio;

    private String tipo_lavado;
    private String fecha;
    private String hora_inicio;
    private String hora_fin;
    private double costo;
    private String indicaciones;
    private String descripcion_servicio;
    private EstadoServicio estadoServicio;

   // private  String Estado;
    // Claves foráneas
    private int estado;
    private int id_vehiculo;
    private int id_empleado;

    // 2. Constructor Vacío (Requerido para muchas librerías como Firebase o JSON parsing)
    public Servicio() {
    }

    // 3. Constructor Completo (Opcional, pero útil para crear objetos fácilmente)
    public Servicio(String id_servicio, String tipo_lavado, String fecha, String hora_inicio, String hora_fin,
                    double costo, String indicaciones, String descripcion_servicio, EstadoServicio estado_servicio,int estado,
                    int id_vehiculo, int id_empleado) {
        this.id_servicio = id_servicio;
        this.tipo_lavado = tipo_lavado;
        this.fecha = fecha;
        this.hora_inicio = hora_inicio;
        this.hora_fin = hora_fin;
        this.costo = costo;
        this.indicaciones = indicaciones;
        this.descripcion_servicio = descripcion_servicio;
       // this.estado_servicio = estado_servicio;
        this.estadoServicio=estado_servicio;
        this.id_vehiculo = id_vehiculo;
        this.id_empleado = id_empleado;
    }

    // 4. Getters y Setters (Métodos para acceder y modificar las variables)

    public String getId_servicio() {
        return id_servicio;
    }

    public void setId_servicio(String id_servicio) {
        this.id_servicio = id_servicio;
    }



    public String getTipo_lavado() {
        return tipo_lavado;
    }

    public void setTipo_lavado(String tipo_lavado) {
        this.tipo_lavado = tipo_lavado;
    }



    public int getId_vehiculo() {
        return id_vehiculo;
    }

    public void setId_vehiculo(int id_vehiculo) {
        this.id_vehiculo = id_vehiculo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora_inicio() {
        return hora_inicio;
    }

    public void setHora_inicio(String hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public String getHora_fin() {
        return hora_fin;
    }

    public void setHora_fin(String hora_fin) {
        this.hora_fin = hora_fin;
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

    public String getDescripcion_servicio() {
        return descripcion_servicio;
    }

    public void setDescripcion_servicio(String descripcion_servicio) {
        this.descripcion_servicio = descripcion_servicio;
    }



    public int getId_empleado() {
        return id_empleado;
    }

    public void setId_empleado(int id_empleado) {
        this.id_empleado = id_empleado;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public EstadoServicio getEstadoServicio() {
        return estadoServicio;
    }

    public void setEstadoServicio(EstadoServicio estadoServicio) {
        this.estadoServicio = estadoServicio;
    }
}
