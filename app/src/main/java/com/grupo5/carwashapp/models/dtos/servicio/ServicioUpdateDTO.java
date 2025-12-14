package com.grupo5.carwashapp.models.dtos.servicio;

import com.google.firebase.database.Exclude;
import com.grupo5.carwashapp.models.CatalogoServicio;
import com.grupo5.carwashapp.models.enums.EstadoServicio;
import com.grupo5.carwashapp.models.enums.TipoLavado;

public class ServicioUpdateDTO {
    @Exclude
    private String id_servicio;


    private CatalogoServicio catalogoServicio; // Cambiado de TipoLavado
    private String fecha;
    private String horaInicio;
    private String horaFin;
    private String indicaciones;
    private int estado;
    private EstadoServicio estadoServicio;
    private String placa; // Cambiado de idVehiculo (int) a placa (String)
    private String cedula_empleado;

    public ServicioUpdateDTO() {
    }

    // MODIFICADO: Cambiar TipoLavado por CatalogoServicio
    public ServicioUpdateDTO(String id_servicio, CatalogoServicio catalogoServicio,
                             String fecha, String horaInicio, String horaFin,
                             String indicaciones, int estado, EstadoServicio estadoServicio,
                             String placa, String cedula_empleado) {
        this.id_servicio = id_servicio;
        this.catalogoServicio = catalogoServicio;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.indicaciones = indicaciones;
        this.estado = estado;
        this.estadoServicio = estadoServicio;
        this.placa = placa;
        this.cedula_empleado = cedula_empleado;
    }

    // Getters y Setters
    public CatalogoServicio getCatalogoServicio() {
        return catalogoServicio;
    }

    public void setCatalogoServicio(CatalogoServicio catalogoServicio) {
        this.catalogoServicio = catalogoServicio;
    }

    public String getId_servicio() {
        return id_servicio;
    }

    public void setId_servicio(String id_servicio) {
        this.id_servicio = id_servicio;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public String getIndicaciones() {
        return indicaciones;
    }

    public void setIndicaciones(String indicaciones) {
        this.indicaciones = indicaciones;
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

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getCedula_empleado() {
        return cedula_empleado;
    }

    public void setCedula_empleado(String cedula_empleado) {
        this.cedula_empleado = cedula_empleado;
    }
}
