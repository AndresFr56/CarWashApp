package com.grupo5.carwashapp.models.enums;

public enum CatalogoServicios {
    LAVADO_EXPRESS("Lavado Express", 5.00),
    LAVADO_FULL("Lavado Full + Aspirado", 10.00),
    ENCERADO("Encerado Orbital", 15.00),
    LIMPIEZA_MOTOR("Limpieza de Motor", 8.50),
    LAVADO_TAPICERIA("Lavado de Tapicería", 25.00);

    private final String nombre;
    private final double precio;

    CatalogoServicios(String nombre, double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public String getEtiqueta() {
        return nombre;
    }
}
