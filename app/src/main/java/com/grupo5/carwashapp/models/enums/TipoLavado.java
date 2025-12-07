package com.grupo5.carwashapp.models.enums;

public enum TipoLavado {
    RAPIDO("Lavado Rápido", 3.00, "Lavado básico exterior, 10 minutos"),
    COMPLETO("Lavado Completo", 5.00, "Incluye interior y exterior"),
    ENCERADO("Lavado con Encerado", 8.00, "Lavado + encerado premium");

    private final String nombre;
    private final double costo;
    private final String descripcion;

    TipoLavado(String nombre, double costo, String descripcion) {
        this.nombre = nombre;
        this.costo = costo;
        this.descripcion = descripcion;
    }

    public String getNombre() { return nombre; }
    public double getCosto() { return costo; }
    public String getDescripcion() { return descripcion; }
}
