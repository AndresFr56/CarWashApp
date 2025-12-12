package com.grupo5.carwashapp.models.dtos.usuario;

import com.grupo5.carwashapp.models.enums.EstadoUsuarios;
import com.grupo5.carwashapp.models.enums.Roles;

public class UsuarioCreateDto {

    private final EstadoUsuarios estado = EstadoUsuarios.ACTIVO;
    private String cedula;
    private String nombres;
    private String apellidos;
    private String telefono;
    private String correo;
    private String direccion;
    private Roles rol;

    // Constructor vacio
    public UsuarioCreateDto() {
    }

    // Constructor para el registro del usuario
    public UsuarioCreateDto(String cedula, String nombres, String apellidos, String telefono,
                            String correo, String direccion, Roles rol) {
        this.cedula = cedula;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
        this.rol = rol;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Roles getRol() {
        return rol;
    }

    public void setRol(Roles rol) {
        this.rol = rol;
    }

    public EstadoUsuarios getEstado() {
        return estado;
    }
}


