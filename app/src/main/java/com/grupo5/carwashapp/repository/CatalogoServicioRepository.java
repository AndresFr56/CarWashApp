package com.grupo5.carwashapp.repository;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.grupo5.carwashapp.interfaces.RepositoryCallBack;
import com.grupo5.carwashapp.models.CatalogoServicio;
import com.grupo5.carwashapp.models.dtos.catalogoServicio.CatalogoServicioCreateDto;
import com.grupo5.carwashapp.models.dtos.catalogoServicio.CatalogoServicioUpdateDto;

import java.util.ArrayList;
import java.util.List;

public class CatalogoServicioRepository {
    private final DatabaseReference dbRef;

    public CatalogoServicioRepository() {
        dbRef = FirebaseDatabase.getInstance().getReference("CatalogoServicios");
    }

    public void registrarServicio(CatalogoServicioCreateDto servicioDto, RepositoryCallBack<String> callback) {
        DatabaseReference nuevoServicioRef = dbRef.push();
        String idNuevoServicio = nuevoServicioRef.getKey();
        nuevoServicioRef.setValue(servicioDto)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(idNuevoServicio);
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }

    public void obtenerServicios(RepositoryCallBack<List<CatalogoServicio>> callback) {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<CatalogoServicio> listaServicios = new ArrayList<>();

                for (DataSnapshot data : snapshot.getChildren()) {
                    CatalogoServicio servicio = data.getValue(CatalogoServicio.class);

                    if (servicio != null) {
                        servicio.setUid(data.getKey());
                        listaServicios.add(servicio);
                    }
                }
                callback.onSuccess(listaServicios);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onFailure(error.toException());
            }
        });
    }

    public void buscarServiciosPorNombre(String nombreServicio, RepositoryCallBack<List<CatalogoServicio>> callback) {

        if (nombreServicio == null || nombreServicio.isEmpty()) {
            callback.onSuccess(new ArrayList<>());
            return;
        }

        Query query = dbRef.orderByChild("nombre")
                .startAt(nombreServicio)
                .endAt(nombreServicio + "\uf8ff");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<CatalogoServicio> listaFiltrada = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    CatalogoServicio servicio = ds.getValue(CatalogoServicio.class);
                    if (servicio != null) {
                        servicio.setUid(ds.getKey());
                        listaFiltrada.add(servicio);
                    }
                }
                callback.onSuccess(listaFiltrada);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onFailure(error.toException());
            }
        });
    }

    public void actualizarServicio(CatalogoServicioUpdateDto dtoService, RepositoryCallBack<Void> callback) {
        if (dtoService.getUid() == null || dtoService.getUid().isEmpty()) {
            callback.onFailure(new Exception("El UID del servicio es nulo"));
            return;
        }

        DatabaseReference servicioRef = dbRef.child(dtoService.getUid());
        servicioRef.setValue(dtoService)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(null);
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }

    public void darDeBajaServicio(String uid, RepositoryCallBack<Void> callback) {

        if (uid == null) {
            callback.onFailure(new Exception("UID nulo"));
            return;
        }

        dbRef.child(uid).child("estado").setValue("INACTIVO")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(null);
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }
}
