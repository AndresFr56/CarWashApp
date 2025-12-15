package com.grupo5.carwashapp.activities.vehiculo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.grupo5.carwashapp.R;

public class MenuVehiculo extends AppCompatActivity {

    CardView cardNuevo, cardConsultar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_vehiculo);

        cardNuevo = findViewById(R.id.card_nuevo_usuario);
        cardConsultar = findViewById(R.id.card_consultar_usuario);

        cardNuevo.setOnClickListener(v -> {
            startActivity(new Intent(MenuVehiculo.this, RegistrarVehiculo.class));
        });

        cardConsultar.setOnClickListener(v -> {
            startActivity(new Intent(MenuVehiculo.this, ConsultarVehiculo.class));
        });
    }
    public void regresar(View v) {
        finish();
    }

}
