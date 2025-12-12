package com.grupo5.carwashapp.activities.facturacion;

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

public class MenuFacturacion extends AppCompatActivity {
    CardView cardNuevaFactura, cardConsultarFactura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_facturacion);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        cardNuevaFactura = findViewById(R.id.card_nueva_factura);
        cardConsultarFactura = findViewById(R.id.card_consultar_factura);

        cardNuevaFactura.setOnClickListener(v -> {
            Intent i = new Intent(v.getContext(), RegistrarFactura.class);
            startActivity(i);
        });

        cardConsultarFactura.setOnClickListener(v -> {
            Intent i = new Intent(v.getContext(), ConsultarFactura.class);
            startActivity(i);
        });
    }

    public void regresar(View v) {
        finish();
    }
}