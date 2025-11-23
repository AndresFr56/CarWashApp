package com.grupo5.carwashapp.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.grupo5.carwashapp.R;
import com.grupo5.carwashapp.activities.usuario.RegistrarUsuario;
import com.grupo5.carwashapp.database.ConexionBD;

public class Login extends AppCompatActivity {
    TextInputEditText emailUsuario, claveUsuario;

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
        emailUsuario = findViewById(R.id.login_txt_correo);
        claveUsuario = findViewById(R.id.login_txt_clave);
    }

    public void validarDeInicioSesion(View v){
        String email = emailUsuario.getText().toString();
        String clave = claveUsuario.getText().toString();

        ConexionBD conexion = new ConexionBD(this);
        SQLiteDatabase db = conexion.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT contrasenia,nombre " +
                        "FROM usuario WHERE correo=?",
                new String[]{email}
        );

        if (c.moveToFirst()) {
            String claveBD = c.getString(0);
            String nombresBD = c.getString(1).toString();

            if (claveBD.equals(clave)){
                Intent ventanaHome = new Intent(v.getContext(), Home.class);
                ventanaHome.putExtra("user", nombresBD);
                startActivity(ventanaHome);
                Toast.makeText(this, "Credenciales correctas", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Correo no registrado", Toast.LENGTH_LONG).show();
        }
        c.close();
        db.close();
    }

    public void registrarse(View v) { // no usages
        Intent ventanaRegistrarUsuario = new Intent(v.getContext(), RegistrarUsuario.class);
        startActivity(ventanaRegistrarUsuario);
    }


}