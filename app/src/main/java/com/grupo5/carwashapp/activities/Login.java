package com.grupo5.carwashapp.activities;

import android.content.Intent;
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
import com.grupo5.carwashapp.models.enums.Estado;
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
        String email = emailUsuario.getText().toString();
        String clave = claveUsuario.getText().toString();
        if (email.isEmpty()) {
            emailUsuario.setError("El correo es obligatorio");
            emailUsuario.requestFocus();
            return;
        }

        if (clave.isEmpty()) {
            claveUsuario.setError("La contraseña es obligatoria");
            claveUsuario.requestFocus();
            return;
        }
        repoUsuario.login(email, clave, task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Iniciando Sesión...", Toast.LENGTH_SHORT).show();
                String uid = task.getResult().getUser().getUid();
                repoUsuario.obtenerUsuario(uid, new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Usuario usuario = snapshot.getValue(Usuario.class);

                            if (usuario != null) {
                                if (usuario.getEstado() == Estado.Inactivo) {
                                    Toast.makeText(v.getContext(), "Su usuario está inactivo", Toast.LENGTH_LONG).show();
                                    FirebaseAuth.getInstance().signOut();
                                    return;
                                }

                                Intent ventanaPrincipal = new Intent(v.getContext(), Home.class);
                                ventanaPrincipal.putExtra("user", usuario.getNombres());
                                startActivity(ventanaPrincipal);
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(v.getContext(), "Error de base de datos", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    public void registrarse(View v) { // no usages
        Intent ventanaRegistrarUsuario = new Intent(v.getContext(), RegistrarUsuario.class);
        startActivity(ventanaRegistrarUsuario);
    }


}