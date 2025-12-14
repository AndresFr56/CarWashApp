package com.grupo5.carwashapp.activities.facturacion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.grupo5.carwashapp.R;
import com.grupo5.carwashapp.adapters.FacturaAdapter;
import com.grupo5.carwashapp.interfaces.RepositoryCallBack;
import com.grupo5.carwashapp.models.Factura;
import com.grupo5.carwashapp.repository.FacturacionRepository;

import java.util.List;

public class ConsultarFactura extends AppCompatActivity {
    private TextInputEditText cedulaBuscarT;
    private RecyclerView recycler;
    private FacturaAdapter adapter;

    private final ActivityResultLauncher<Intent> launcherDetalle = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Factura facturaActualizada = (Factura) result.getData().getSerializableExtra("facturaRetorno");

                    if (facturaActualizada != null && adapter != null) {
                        adapter.actualizarItem(facturaActualizada);
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.facturacion_activity_consultar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        cedulaBuscarT = findViewById(R.id.consul_fact_txt_cedula);
        recycler = findViewById(R.id.consul_fact_recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
    }

    public void consultarFacturas(View v) {
        String cedula = cedulaBuscarT.getText().toString().trim();

        if (cedula.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese una cédula", Toast.LENGTH_SHORT).show();
            return;
        }

        FacturacionRepository repo = new FacturacionRepository();
        v.setEnabled(false);
        repo.buscarFacturasPorCedula(cedula, new RepositoryCallBack<List<Factura>>() {
            @Override
            public void onSuccess(List<Factura> listaFacturas) {
                v.setEnabled(true);
                if (listaFacturas.isEmpty()) {
                    Toast.makeText(ConsultarFactura.this, "No se encontraron coincidencias", Toast.LENGTH_SHORT).show();
                    recycler.setAdapter(null);
                } else {
                    cargarRecycler(listaFacturas);
                }
            }

            @Override
            public void onFailure(Exception e) {
                v.setEnabled(true);
                Toast.makeText(ConsultarFactura.this, "Error al cargar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarRecycler(List<Factura> listaFacturas) {
        adapter = new FacturaAdapter(listaFacturas, factura -> {
            Intent intent = new Intent(ConsultarFactura.this, VisualizarFactura.class);
            intent.putExtra("datosFactura", factura);
            launcherDetalle.launch(intent);
        });
        recycler.setAdapter(adapter);
    }

    public void regresar(View v) {
        finish();
    }
}