package com.grupo5.carwashapp.activities.servicio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.grupo5.carwashapp.R;
import com.grupo5.carwashapp.activities.Home; // Asegúrate de importar la Activity Home

// Importa las activities de destino
import com.grupo5.carwashapp.activities.catalogoServicios.ConsultarCatalogoServicios;
import com.grupo5.carwashapp.activities.catalogoServicios.RegistrarCatalogoServicios;
import com.grupo5.carwashapp.activities.servicio.RegistrarServicio;
import com.grupo5.carwashapp.activities.servicio.ConsultarServicio;

public class servicio_menu extends AppCompatActivity {

    CardView cardregistrar, cardconsultar, cardRegistrarCatalogo, cardConsultarCatalogo;
    ImageButton btnSalir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_servicio_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cardregistrar = findViewById(R.id.home_serv_registrar);
        cardconsultar = findViewById(R.id.home_consultar_serv);
        cardRegistrarCatalogo = findViewById(R.id.card_registrar_catalogo);
        cardConsultarCatalogo = findViewById(R.id.card_consultar_catalogo);
        btnSalir = findViewById(R.id.btnSalir_serv);



        cardregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la Activity para Registrar un Servicio
                Intent i = new Intent(servicio_menu.this, RegistrarServicio.class);
                startActivity(i);
            }
        });


        cardconsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la Activity para Consultar un Servicio
                Intent i = new Intent(servicio_menu.this, BuscarServicio.class);
                startActivity(i);
            }
        });
        cardRegistrarCatalogo.setOnClickListener(v -> {
            Intent i = new Intent(servicio_menu.this, RegistrarCatalogoServicios.class);
            startActivity(i);
        });
        cardConsultarCatalogo.setOnClickListener(v -> {
            Intent i = new Intent(servicio_menu.this, ConsultarCatalogoServicios.class);
            startActivity(i);
        });


        btnSalir.setOnClickListener(v -> {
            Intent i = new Intent(servicio_menu.this, Home.class);
            startActivity(i);
            finish();
        });

    }
}