package com.grupo5.carwashapp.repository;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
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

    // 1. Crear servicio

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
        servicio.setNro_servicio(dto.getNro_servicio());


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
        servicio.setPlaca(dto.getPlaca());
        servicio.setCedula_empleado(dto.getCedula_empleado());
        servicio.setNombre_empleado(dto.getNombreEmpleado());


        // Guardar en Firebase
        dbRef.child(id).setValue(servicio)
                .addOnSuccessListener(aVoid -> listener.onSuccess("Servicio creado correctamente"))
                .addOnFailureListener(e -> listener.onError(e.getMessage()));
    }

    // 2. Actualizar servicio
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
        updates.put("cedula_empleado",dto.getCedula_empleado());
        //updates.put("id_empleado", dto.getIdEmpleado());

        dbRef.child(dto.getId_servicio())
                .updateChildren(updates)
                .addOnSuccessListener(aVoid -> listener.onSuccess("Servicio actualizado"))
                .addOnFailureListener(e -> listener.onError(e.getMessage()));
    }

    // 3. Eliminar lógico (estado = 1)

    public void eliminarLogico(String id, OnServiceResult listener) {

        dbRef.child(id).child("estado").setValue(1)
                .addOnSuccessListener(aVoid -> listener.onSuccess("Servicio eliminado lógicamente"))
                .addOnFailureListener(e -> listener.onError(e.getMessage()));
    }
    public void buscarPorId(String id, OnBuscarServicio listener) {
        dbRef.child(id)
                .get()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        listener.onError(task.getException().getMessage());
                        return;
                    }
                    if (!task.getResult().exists()) {
                        listener.onNotFound();
                    } else {
                        Servicio servicio = task.getResult().getValue(Servicio.class);
                        listener.onFound(servicio);
                    }
                });
    }
    public void buscarPorNumero(int nro, OnBuscarServicio listener) {
        dbRef.orderByChild("nro_servicio")
                .equalTo(nro)
                .get()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        listener.onError(task.getException().getMessage());
                        return;
                    }
                    if (!task.getResult().exists()) {
                        listener.onNotFound();
                    } else {
                        for (DataSnapshot ds : task.getResult().getChildren()) {
                            Servicio servicio = ds.getValue(Servicio.class);
                            listener.onFound(servicio);
                            return;
                        }
                    }
                });
    }
    public interface OnBuscarServicio {
        void onFound(Servicio servicio);
        void onNotFound();
        void onError(String error);
    }





    public void generarNroServicio(OnNroGenerado listener) {
        DatabaseReference contadorRef = dbRef.child("contador");

        contadorRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                Integer value = currentData.getValue(Integer.class);
                if (value == null) value = 0;
                value = value + 1;
                currentData.setValue(value);
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(DatabaseError error, boolean committed, DataSnapshot currentData) {
                if (committed) {
                    listener.onSuccess(currentData.getValue(Integer.class));
                } else {
                    listener.onError("Error generando número");
                }
            }
        });
    }

    public interface OnNroGenerado {
        void onSuccess(int nro);
        void onError(String err);
    }

    // 4. Callback interface

    public interface OnServiceResult {
        void onSuccess(String msg);
        void onError(String error);
    }
}
