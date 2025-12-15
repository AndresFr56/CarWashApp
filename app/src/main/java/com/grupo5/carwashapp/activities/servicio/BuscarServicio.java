package com.grupo5.carwashapp.activities.servicio;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.grupo5.carwashapp.R;
import com.grupo5.carwashapp.adapters.ServicioAdapter;
import com.grupo5.carwashapp.interfaces.ServicioCallBack;
import com.grupo5.carwashapp.models.Servicio;
import com.grupo5.carwashapp.repository.ServicioRepository;

import java.util.List;
import java.util.ArrayList;


public class BuscarServicio extends AppCompatActivity {

    private TextInputEditText placaBuscarT;
    private RecyclerView recycler;
    private ServicioAdapter adapter;
    private ServicioRepository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.servicio_activity_buscar);
        placaBuscarT = findViewById(R.id.consul_txt_placaserv_buscar);
        recycler = findViewById(R.id.consul_serv_recycle);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        repo = new ServicioRepository();
        placaBuscarT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String placa = s.toString().trim().toUpperCase();

                if (placa.isEmpty()) {
                    recycler.setAdapter(null);
                    return;
                }
                if (placa.length() == 7) {
                    buscarServiciosAutomatico(placa);
                } else {
                    recycler.setAdapter(null);
                }
            }@Override
            public void afterTextChanged(Editable s) {}
        });
    }

    // Método de búsqueda automática
    private void buscarServiciosAutomatico(String placa) {
        repo.buscarServiciosPorPlaca(placa, new ServicioCallBack() {
            @Override
            public void onSuccess() {}
            @Override
            public void onServiciosLoaded(List<Servicio> servicios) {
                if (servicios == null || servicios.isEmpty()) {
                    recycler.setAdapter(null);
                    return;
                }
                List<Servicio> serviciosFiltrados = new ArrayList<>();
                for (Servicio s : servicios) {
                    if (s.getEstado() != 1) {
                        serviciosFiltrados.add(s);
                    }
                }
                if (serviciosFiltrados.isEmpty()) {
                    recycler.setAdapter(null);
                } else {
                    cargarRecycler(serviciosFiltrados);
                }
            }
            @Override
            public void onError(String error) {
                Toast.makeText(BuscarServicio.this,
                        "Error: " + error,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Cargar RecyclerView
    private void cargarRecycler(List<Servicio> servicios) {

        adapter = new ServicioAdapter(servicios, servicio -> {
            Intent intent = new Intent(BuscarServicio.this, ConsultarServicio.class);
             intent.putExtra("servicio", servicio);
            startActivity(intent);
        });

        recycler.setAdapter(adapter);
    }
}
