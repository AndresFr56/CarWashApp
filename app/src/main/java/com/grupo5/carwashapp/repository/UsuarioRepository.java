package com.grupo5.carwashapp.repository;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.grupo5.carwashapp.models.dtos.usuario.UsuarioCreateDto;
import com.grupo5.carwashapp.models.dtos.usuario.UsuarioUpdateDto;

import java.util.HashMap;
import java.util.Map;

public class UsuarioRepository {
    private final FirebaseAuth auth;
    private final DatabaseReference dbRefer;

    public UsuarioRepository() {
        auth = FirebaseAuth.getInstance();
        dbRefer = FirebaseDatabase.getInstance().getReference("Usuarios");
    }

    public void registrarUsuario(UsuarioCreateDto usuario, String contrasenia, OnCompleteListener<AuthResult> listener) {
        auth.createUserWithEmailAndPassword(usuario.getCorreo(), contrasenia)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String uid = auth.getCurrentUser().getUid();

                        // Guardamos los datos del usuario en la BD
                        dbRefer.child(uid).setValue(usuario);
                        listener.onComplete(task);
                    } else {
                        listener.onComplete(task);
                    }
                });
    }

    public void obtenerUsuario(String uid, ValueEventListener listener) {
        dbRefer.child(uid).addListenerForSingleValueEvent(listener);
    }

    public void obtenerUsuarioPorCedula(String cedula, ValueEventListener listener) {
        dbRefer.orderByChild("cedula")
                .equalTo(cedula)
                .addListenerForSingleValueEvent(listener);
    }

    public void actualizarUsuario(String uid, UsuarioUpdateDto usuario, OnCompleteListener<Void> listener) {
        Map<String, Object> actualizaciones = new HashMap<>();

        actualizaciones.put("cedula", usuario.getCedula());
        actualizaciones.put("nombres", usuario.getNombres());
        actualizaciones.put("apellidos", usuario.getApellidos());
        actualizaciones.put("telefono", usuario.getTelefono());
        actualizaciones.put("direccion", usuario.getDireccion());
        actualizaciones.put("rol", usuario.getRol());
        actualizaciones.put("estado", usuario.getEstado());

        dbRefer.child(uid).updateChildren(actualizaciones)
                .addOnCompleteListener(listener);
    }

    public void eliminarUsuarioFisico(String uid, OnCompleteListener<Void> listener) {
        dbRefer.child(uid).removeValue()
                .addOnCompleteListener(listener);
    }

    public void obtenerTodosLosUsuarios(ValueEventListener listener) {
        dbRefer.addListenerForSingleValueEvent(listener);
    }

    public void login(String correo, String contrasenia, OnCompleteListener<AuthResult> listener) {
        auth.signInWithEmailAndPassword(correo, contrasenia)
                .addOnCompleteListener(listener);
    }

    public void logout() {
        auth.signOut();
    }
}
