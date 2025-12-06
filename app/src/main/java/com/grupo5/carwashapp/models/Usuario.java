package com.grupo5.carwashapp.models;

import com.google.firebase.database.Exclude;
import com.grupo5.carwashapp.models.enums.Estado;
import com.grupo5.carwashapp.models.enums.Roles;

public class Usuario {
    @Exclude
    private String uid;
    private String cedula;
    private String nombres;
    private String apellidos;
    private String telefono;
    private String correo;
    private String direccion;
    private Roles rol;
    private Estado estado;

    // Constructor vacio
    public Usuario(){}

    public Usuario(String cedula, String nombres, String apellidos, String telefono,
                   String correo, String direccion, Roles rol, Estado estado) {
        this.cedula = cedula;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
        this.rol = rol;
        this.estado = estado;
    }

    // Getters
    @Exclude
    public String getUid() { return uid; }
    @Exclude
    public void setUid(String uid) { this.uid = uid; }
    public String getCedula() {
        return cedula;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public Roles getRol() {
        return rol;
    }

    public Estado getEstado() {
        return estado;
    }

    // Setters
    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setRol(Roles rol) {
        this.rol = rol;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}
