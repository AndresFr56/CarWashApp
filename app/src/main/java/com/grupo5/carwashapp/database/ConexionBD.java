package com.grupo5.carwashapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ConexionBD extends SQLiteOpenHelper {
    private static final String nameBD = "CarWashBD";
    private static final int version = 1;

    //Tabla Usuario
    private static final String tableUsuarioCreate =
            "CREATE TABLE usuario(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "cedula TEXT NOT NULL," +
                    "nombre TEXT NOT NULL," +
                    "apellido TEXT NOT NULL," +
                    "telefono TEXT NOT NULL," +
                    "rol TEXT NOT NULL," +
                    "correo TEXT NOT NULL," +
                    "direccion TEXT," +
                    "estado TEXT NOT NULL," +
                    "contrasenia TEXT NOT NULL" +
                    ")";

    // Tabla Vehiculo
    private static final String tableVehiculoCreate =
            "CREATE TABLE vehiculo (" +
                    "id_vehiculo INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "placa TEXT UNIQUE NOT NULL," +
                    "marca TEXT NOT NULL," +
                    "modelo TEXT NOT NULL," +
                    "observaciones TEXT NOT NULL," +
                    "id_cliente INTEGER" +
                    ")";

    // Tabla Servicio
    private static final String tableServicioCreate =
            "CREATE TABLE servicio (" +
                    "id_servicio INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "tipo_lavado TEXT NOT NULL," +
                    "fecha_hora TEXT NOT NULL," +
                    "costo REAL NOT NULL," +
                    " indicaciones TEXT NOT NULL,"+
                    "descripcion_servicio TEXT NOT NULL," +
                    "estado_servicio TEXT NOT NULL," +
                    "id_vehiculo INTEGER," +
                    "id_empleado INTEGER" +
                    ")";
    // Tabla TipoServicio (Catálogo de servicios)
    private static final String tableTipoServicioCreate =
            "CREATE TABLE tipo_servicio (" +
                    "id_tipo_servicio INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nombre TEXT NOT NULL," +
                    "descripcion TEXT," +
                    "precio REAL NOT NULL" +
                    ")";

    // Tabla Factura
    private static final String tableFacturaCreate =
            "CREATE TABLE factura (" +
                    "id_factura INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "fecha_emision TEXT NOT NULL," +
                    "IVA REAL NOT NULL," +
                    "total_factura REAL NOT NULL," +
                    "estado_factura TEXT NOT NULL," +
                    "id_cliente INTEGER" +
                    ")";

    // Tabla DetalleFactura
    private static final String tableDetalleFacturaCreate =
            "CREATE TABLE detalle_factura (" +
                    "id_detalle INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "id_factura INTEGER NOT NULL," +
                    "id_servicio INTEGER NOT NULL," +
                    "subtotal REAL NOT NULL" +
                    ")";

    public ConexionBD(Context context) {
        super(context, nameBD, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tableUsuarioCreate);
        db.execSQL(tableVehiculoCreate);
        db.execSQL(tableServicioCreate);
        db.execSQL(tableTipoServicioCreate);
        db.execSQL(tableFacturaCreate);
        db.execSQL(tableDetalleFacturaCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
