package com.grupo5.carwashapp.activities.servicio;

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
import com.grupo5.carwashapp.activities.Home; // Asegúrate de importar la Activity Home

// Importa las activities de destino
import com.grupo5.carwashapp.activities.servicio.RegistrarServicio;
import com.grupo5.carwashapp.activities.servicio.ConsultarServicio;

public class servicio_menu extends AppCompatActivity {

    CardView cardregistrar,cardconsultar,cardsalir;
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
        // 1. Obtener referencias de las CardViews usando los IDs del XML (activity_servicio_menu.xml)
        cardregistrar = findViewById(R.id.home_serv_registrar);
        cardconsultar = findViewById(R.id.home_consultar_serv);
        cardsalir = findViewById(R.id.home_salir_serv); // Asumo que este ID es para "Salir"

        // 2. Establecer OnClickListeners para cada CardView

        // CLIC EN REGISTRAR SERVICIO
        cardregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la Activity para Registrar un Servicio
                Intent i = new Intent(servicio_menu.this, RegistrarServicio.class);
                startActivity(i);
            }
        });

        // CLIC EN CONSULTAR SERVICIO
        cardconsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la Activity para Consultar un Servicio
                Intent i = new Intent(servicio_menu.this, ConsultarServicio.class);
                startActivity(i);
            }
        });
        cardsalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Volver al Home Activity
                // Dependiendo de tu flujo, puedes usar finish() para cerrar esta actividad
                // si Home ya está en la pila, o iniciar un Intent
                // Opción 1: Cerrar la Activity actual y volver a la anterior (si es Home)
                finish();

                // Opción 2: Iniciar la Home Activity de nuevo (solo si es necesario)
                // Intent i = new Intent(servicio_menu.this, Home.class);
                // startActivity(i);
            }
        });
    }
}