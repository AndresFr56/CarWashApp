package com.grupo5.carwashapp.activities.catalogoServicios;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.grupo5.carwashapp.R;
import com.grupo5.carwashapp.adapters.CatalogoServiciosAdapter;
import com.grupo5.carwashapp.interfaces.RepositoryCallBack;
import com.grupo5.carwashapp.models.CatalogoServicio;
import com.grupo5.carwashapp.repository.CatalogoServicioRepository;

import java.util.ArrayList;
import java.util.List;

public class ConsultarCatalogoServicios extends AppCompatActivity {
    private TextInputEditText txtBuscador;
    private RecyclerView recycler;
    private CatalogoServiciosAdapter adapter;
    private CatalogoServicioRepository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.catalogo_servicios_activity_consultar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        txtBuscador = findViewById(R.id.consul_txt_serv_buscar);
        recycler = findViewById(R.id.consul_recycler_catalogo_serv);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        repo = new CatalogoServicioRepository();

        adapter = new CatalogoServiciosAdapter(new ArrayList<>(), new CatalogoServiciosAdapter.OnItemClickListener() {
            @Override
            public void onEditarClick(CatalogoServicio servicio) {
                irAEditar(servicio);
            }

            @Override
            public void onEliminarClick(CatalogoServicio servicio) {
                mostrarDialogoEliminar(servicio);
            }
        });
        recycler.setAdapter(adapter);
        setupBuscador();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarDatosDeFirebase();
    }

    private void cargarDatosDeFirebase() {
        repo.obtenerServicios(new RepositoryCallBack<List<CatalogoServicio>>() {
            @Override
            public void onSuccess(List<CatalogoServicio> lista) {
                if (lista.isEmpty()) {
                    Toast.makeText(ConsultarCatalogoServicios.this, "No hay servicios registrados", Toast.LENGTH_SHORT).show();
                }
                adapter.setLista(lista);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(ConsultarCatalogoServicios.this, "Error al cargar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupBuscador() {
        txtBuscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter != null) {
                    adapter.filtrar(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void mostrarDialogoEliminar(CatalogoServicio servicio) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar Servicio")
                .setMessage("¿Estás seguro de dar de baja el servicio '" + servicio.getNombre() + "'?")
                .setPositiveButton("Sí, dar de baja", (dialog, which) -> {
                    eliminarServicio(servicio.getUid());
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminarServicio(String uid) {
        repo.darDeBajaServicio(uid, new RepositoryCallBack<Void>() {
            @Override
            public void onSuccess(Void result) {
                Toast.makeText(ConsultarCatalogoServicios.this, "Servicio dado de baja correctamente", Toast.LENGTH_SHORT).show();
                cargarDatosDeFirebase();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(ConsultarCatalogoServicios.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void irAEditar(CatalogoServicio servicio) {
        Intent ventanaActualizar = new Intent(ConsultarCatalogoServicios.this, ActualizarCatalogoServicios.class);
        ventanaActualizar.putExtra("servicio", servicio);
        startActivity(ventanaActualizar);
    }

    public void regresar(View v) {
        finish();
    }
}