package com.grupo5.carwashapp.repository;

import androidx.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.grupo5.carwashapp.models.Servicio;
import com.grupo5.carwashapp.models.dtos.servicio.ServicioCreateDTO;
import com.grupo5.carwashapp.models.dtos.servicio.ServicioUpdateDTO;
import com.grupo5.carwashapp.models.enums.TipoLavado;

import java.util.HashMap;

public class ServicioRepository {

    private final DatabaseReference dbRef;

    public ServicioRepository() {
        dbRef = FirebaseDatabase.getInstance().getReference("Servicios");
    }

    // ---------------------------------------------------------
    // 1. Crear servicio
    // ---------------------------------------------------------
    public void crearServicio(ServicioCreateDTO dto, OnServiceResult listener) {

        // Genera ID automático
        String id = dbRef.push().getKey();
        if (id == null) {
            listener.onError("Error generando ID de servicio");
            return;
        }

        // Construimos el objeto final Servicio
        Servicio servicio = new Servicio();
        servicio.setId_servicio(id);

        // Tipo de lavado
        servicio.setTipo_lavado(dto.getTipoLavado().name());
        servicio.setCosto(dto.getTipoLavado().getCosto());
        servicio.setDescripcion_servicio(dto.getTipoLavado().getDescripcion());

        // Fecha y horas
        servicio.setFecha(dto.getFecha());
        servicio.setHora_inicio(dto.getHoraInicio());
        servicio.setHora_fin(dto.getHoraFin());

        // Otros campos
        servicio.setIndicaciones(dto.getIndicaciones());
        servicio.setEstadoServicio(dto.getEstadoServicio());
        servicio.setEstado(dto.getEstado()); // eliminación lógica
        servicio.setId_vehiculo(dto.getIdVehiculo());
        servicio.setId_empleado(dto.getIdEmpleado());

        // Guardar en Firebase
        dbRef.child(id).setValue(servicio)
                .addOnSuccessListener(aVoid -> listener.onSuccess("Servicio creado correctamente"))
                .addOnFailureListener(e -> listener.onError(e.getMessage()));
    }

    // ---------------------------------------------------------
    // 2. Actualizar servicio
    // ---------------------------------------------------------
    public void actualizarServicio(ServicioUpdateDTO dto, OnServiceResult listener) {

        HashMap<String, Object> updates = new HashMap<>();

        updates.put("tipo_lavado", dto.getTipoLavado().name());
        updates.put("costo", dto.getTipoLavado().getCosto());
        updates.put("descripcion_servicio", dto.getTipoLavado().getDescripcion());

        updates.put("fecha", dto.getFecha());
        updates.put("hora_inicio", dto.getHoraInicio());
        updates.put("hora_fin", dto.getHoraFin());
        updates.put("indicaciones", dto.getIndicaciones());

        updates.put("estadoServicio", dto.getEstadoServicio());
        updates.put("estado", dto.getEstado());
        updates.put("id_vehiculo", dto.getIdVehiculo());
        updates.put("id_empleado", dto.getIdEmpleado());

        dbRef.child(dto.getId_servicio())
                .updateChildren(updates)
                .addOnSuccessListener(aVoid -> listener.onSuccess("Servicio actualizado"))
                .addOnFailureListener(e -> listener.onError(e.getMessage()));
    }

    // ---------------------------------------------------------
    // 3. Eliminar lógico (estado = 1)
    // ---------------------------------------------------------
    public void eliminarLogico(String id, OnServiceResult listener) {

        dbRef.child(id).child("estado").setValue(1)
                .addOnSuccessListener(aVoid -> listener.onSuccess("Servicio eliminado lógicamente"))
                .addOnFailureListener(e -> listener.onError(e.getMessage()));
    }


    // ---------------------------------------------------------
    // 4. Callback interface
    // ---------------------------------------------------------
    public interface OnServiceResult {
        void onSuccess(String msg);
        void onError(String error);
    }
}
