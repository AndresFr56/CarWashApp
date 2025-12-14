package com.grupo5.carwashapp.models;

import com.grupo5.carwashapp.models.enums.EstadoServicio;

import java.io.Serializable;

public class Servicio implements Serializable {

    private String id_servicio;
    private int nro_servicio;

    private String id_catalogo_servicio;
    private String nombre_servicio;
    private String fecha;
    private String hora_inicio;
    private String hora_fin;
    private double costo;
    private String indicaciones;
    private String descripcion_servicio;
    private EstadoServicio estadoServicio;
    private String placa;

   // private  String Estado;
    // Claves foráneas
    private int estado;
    private int id_vehiculo;
   //) private String id_empleado;
   private String cedula_empleado;
   private String nombre_empleado;


    // 2. Constructor Vacío (Requerido para  como Firebase )
    public Servicio() {
    }

    // 3. Constructor Completo
    public Servicio(String id_servicio, int nro_servicio, String fecha, String hora_inicio, String hora_fin,
                    double costo, String indicaciones, String descripcion_servicio, EstadoServicio estado_servicio,int estado,
                   /* int id_vehiculo,,*/String placa, String cedula_empleado,String nombre_empleado) {
        this.id_servicio = id_servicio;
        this.nro_servicio=nro_servicio;
        this.id_catalogo_servicio = id_catalogo_servicio;
        this.nombre_servicio =nombre_servicio;
        this.fecha = fecha;
        this.hora_inicio = hora_inicio;
        this.hora_fin = hora_fin;
        this.costo = costo;
        this.indicaciones = indicaciones;
        this.descripcion_servicio = descripcion_servicio;
       // this.estado_servicio = estado_servicio;
        this.estadoServicio=estado_servicio;
        this.placa=placa;
       // this.id_vehiculo = id_vehiculo;
        //this.id_empleado = id_empleado;
        this.cedula_empleado=cedula_empleado;
        this.nombre_empleado= nombre_empleado;
    }



    public String getId_servicio() {
        return id_servicio;
    }

    public void setId_servicio(String id_servicio) {
        this.id_servicio = id_servicio;
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

    public String getCedula_empleado() {
        return cedula_empleado;
    }

    public void setCedula_empleado(String cedula_empleado) {
        this.cedula_empleado = cedula_empleado;
    }

    public int getNro_servicio() {
        return nro_servicio;
    }

    public void setNro_servicio(int nro_servicio) {
        this.nro_servicio = nro_servicio;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getNombre_empleado() {
        return nombre_empleado;
    }

    public void setNombre_empleado(String nombre_empleado) {
        this.nombre_empleado = nombre_empleado;
    }

    public String getId_catalogo_servicio() {
        return id_catalogo_servicio;
    }

    public void setId_catalogo_servicio(String id_catalogo_servicio) {
        this.id_catalogo_servicio = id_catalogo_servicio;
    }

    public String getNombre_servicio() {
        return nombre_servicio;
    }

    public void setNombre_servicio(String nombre_servicio) {
        this.nombre_servicio = nombre_servicio;
    }
}
