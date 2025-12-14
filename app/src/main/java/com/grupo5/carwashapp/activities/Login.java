package com.grupo5.carwashapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.grupo5.carwashapp.R;
import com.grupo5.carwashapp.activities.usuario.RegistrarUsuario;
import com.grupo5.carwashapp.models.Usuario;
import com.grupo5.carwashapp.models.enums.Estados;
import com.grupo5.carwashapp.repository.UsuarioRepository;

public class Login extends AppCompatActivity {
    TextInputEditText emailUsuario, claveUsuario;
    UsuarioRepository repoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        repoUsuario = new UsuarioRepository();
        emailUsuario = findViewById(R.id.login_txt_correo);
        claveUsuario = findViewById(R.id.login_txt_clave);
    }

    public void validarInicioSesion(View v) {
        v.setEnabled(false);

        String email = emailUsuario.getText().toString().trim();
        String clave = claveUsuario.getText().toString().trim();

        if (email.isEmpty()) {
            emailUsuario.setError("El correo es obligatorio");
            emailUsuario.requestFocus();
            v.setEnabled(true);
            return;
        }

        if (clave.isEmpty()) {
            claveUsuario.setError("La contraseña es obligatoria");
            claveUsuario.requestFocus();
            v.setEnabled(true);
            return;
        }

        repoUsuario.login(email, clave, task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Iniciando Sesión...", Toast.LENGTH_SHORT).show();

                if (task.getResult().getUser() == null) {
                    v.setEnabled(true);
                    return;
                }

                String uid = task.getResult().getUser().getUid();
                repoUsuario.obtenerUsuario(uid, new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (isFinishing() || isDestroyed()) return;

                        if (snapshot.exists()) {
                            Usuario usuario = snapshot.getValue(Usuario.class);

                            if (usuario != null) {
                                if (usuario.getEstado() == Estados.INACTIVO) {
                                    Toast.makeText(Login.this, "Su usuario está inactivo", Toast.LENGTH_LONG).show();
                                    FirebaseAuth.getInstance().signOut();
                                    v.setEnabled(true);
                                    return;
                                }

                                SharedPreferences shpUsuario = getSharedPreferences("CarWashSession", MODE_PRIVATE);
                                SharedPreferences.Editor editor = shpUsuario.edit();
                                editor.putString("uidUsuario", usuario.getUid());
                                editor.putString("nombreUsuario", usuario.getNombres());
                                editor.putString("apellidoUsuario", usuario.getApellidos());
                                editor.putString("rolUsuario", usuario.getRol().toString());
                                editor.apply();

                                Intent ventanaPrincipal = new Intent(Login.this, Home.class);
                                startActivity(ventanaPrincipal);
                                finish();
                            }
                        } else {
                            v.setEnabled(true);
                            Toast.makeText(Login.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        v.setEnabled(true);
                        Toast.makeText(Login.this, "Error de base de datos", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                v.setEnabled(true);
                String errorEx = "";
                if (task.getException() != null) {
                    errorEx = task.getException().getMessage();
                }
                Toast.makeText(Login.this, "Credenciales incorrectas", Toast.LENGTH_LONG).show();
                claveUsuario.setText("");
                claveUsuario.requestFocus();
            }
        });
    }


    public void registrarse(View v) { // no usages
        Intent ventanaRegistrarUsuario = new Intent(v.getContext(), RegistrarUsuario.class);
        startActivity(ventanaRegistrarUsuario);
    }
}