package com.grupo5.carwashapp.models.dtos.servicio;

import com.google.firebase.database.Exclude;
import com.grupo5.carwashapp.models.enums.EstadoServicio;
import com.grupo5.carwashapp.models.enums.TipoLavado;

public class ServicioUpdateDTO {
    @Exclude
    private String id_servicio;

    private TipoLavado tipoLavado; // Usando el enum directamente
    private String fecha;
    private String horaInicio;
    private String horaFin;
    private String indicaciones;
    //private String descripcionServicio;
    private EstadoServicio estadoServicio  ;// es  "PENDIENTE", "EN_PROCESO", "COMPLETADO", "CANCELADO"
    private int estado;
    private int idVehiculo;
    private String cedula_empleado;

    public ServicioUpdateDTO(){
    }

    public ServicioUpdateDTO(String id_servicio,TipoLavado tipoLavado, String fecha, String horaInicio, String horaFin,
                             String indicaciones/*., String descripcionServicio*/,int estado, EstadoServicio estadoServicio,
                             int idVehiculo, String cedula_empleado){
        this.id_servicio=id_servicio;
        this.tipoLavado = tipoLavado;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.indicaciones = indicaciones;
        // this.descripcionServicio = descripcionServicio;
        this.estadoServicio = estadoServicio;
        this.estado =estado;
        this.idVehiculo = idVehiculo;
       this.cedula_empleado=cedula_empleado;
    }

    public String getId_servicio() {
        return id_servicio;
    }

    public void setId_servicio(String id_servicio) {
        this.id_servicio = id_servicio;
    }

    public TipoLavado getTipoLavado() {
        return tipoLavado;
    }

    public void setTipoLavado(TipoLavado tipoLavado) {
        this.tipoLavado = tipoLavado;
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

    public EstadoServicio getEstadoServicio() {
        return estadoServicio;
    }

    public void setEstadoServicio(EstadoServicio estadoServicio) {
        this.estadoServicio = estadoServicio;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(int idVehiculo) {
        this.idVehiculo = idVehiculo;
    }



    public String getCedula_empleado() {
        return cedula_empleado;
    }

    public void setCedula_empleado(String cedula_empleado) {
        this.cedula_empleado = cedula_empleado;
    }
}
