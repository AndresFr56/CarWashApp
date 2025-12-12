package com.grupo5.carwashapp.activities.usuario;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.grupo5.carwashapp.R;

public class MenuUsuarios extends AppCompatActivity {
    CardView cardNuevoUsuario, cardConsultarUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_usuarios);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        cardNuevoUsuario = findViewById(R.id.card_nuevo_usuario);
        cardConsultarUsuario = findViewById(R.id.card_consultar_usuario);

        cardNuevoUsuario.setOnClickListener(v -> {
            Intent i = new Intent(v.getContext(), RegistrarUsuario.class);
            startActivity(i);
        });

        cardConsultarUsuario.setOnClickListener(v -> {
            Intent i = new Intent(v.getContext(), ConsultarUsuario.class);
            startActivity(i);
        });
    }

    public void regresar(View v) {
        finish();
    }
}