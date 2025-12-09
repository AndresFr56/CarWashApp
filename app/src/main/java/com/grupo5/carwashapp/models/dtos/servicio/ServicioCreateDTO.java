package com.grupo5.carwashapp.models.dtos.servicio;


import com.grupo5.carwashapp.models.enums.EstadoServicio;
import com.grupo5.carwashapp.models.enums.TipoLavado;

public class ServicioCreateDTO {

    private TipoLavado tipoLavado; // Usando el enum directamente
    private int nro_servicio;
    private String fecha;
    private String horaInicio;
    private String horaFin;
    private String indicaciones;
    //private String descripcionServicio;
    private EstadoServicio estadoServicio =EstadoServicio.PENDIENTE; // Podría ser "PENDIENTE", "EN_PROCESO", "COMPLETADO", "CANCELADO"
    private int estado;
    private int idVehiculo;
    private String idEmpleado;
    private String cedula_empleado;

    // NOTA: El campo 'costo' no va aquí porque debe calcularse automáticamente
    // basándose en el TipoLavado seleccionado lo mismo que con descripcionServicio

    // Constructor vacío
    public ServicioCreateDTO() {
    }

    // Constructor con parámetros
    public ServicioCreateDTO(TipoLavado tipoLavado ,int nro_servicio, String fecha, String horaInicio, String horaFin,
                             String indicaciones/*., String descripcionServicio*/,int estado, EstadoServicio estadoServicio,
                             int idVehiculo, String cedula_empleado) {
        this.tipoLavado = tipoLavado;
        this.nro_servicio=nro_servicio;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.indicaciones = indicaciones;
        this.estado =estado;
       // this.descripcionServicio = descripcionServicio;
        this.estadoServicio = estadoServicio;
        this.idVehiculo = idVehiculo;
      //  this.idEmpleado = idEmpleado;
        this.cedula_empleado=cedula_empleado;
    }

    // Getters y Setters
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

   /* public String getDescripcionServicio() {
        return descripcionServicio;
    }

    public void setDescripcionServicio(String descripcionServicio) {
        this.descripcionServicio = descripcionServicio;
    }*/

    public EstadoServicio getEstadoServicio() {
        return estadoServicio;
    }

    public void setEstadoServicio(EstadoServicio estadoServicio) {
        this.estadoServicio = estadoServicio;
    }

    public int getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(int idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public String getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(String idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
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
}
