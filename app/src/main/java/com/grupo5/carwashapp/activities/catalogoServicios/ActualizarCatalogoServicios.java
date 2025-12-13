package com.grupo5.carwashapp.activities.catalogoServicios;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.grupo5.carwashapp.R;
import com.grupo5.carwashapp.interfaces.RepositoryCallBack;
import com.grupo5.carwashapp.models.CatalogoServicio;
import com.grupo5.carwashapp.models.dtos.catalogoServicio.CatalogoServicioUpdateDto;
import com.grupo5.carwashapp.models.enums.Estados;
import com.grupo5.carwashapp.repository.CatalogoServicioRepository;

public class ActualizarCatalogoServicios extends AppCompatActivity {
    TextInputEditText txtNombre, txtDescripcion, txtPrecio;
    private Spinner spEstado;
    private String idServicioActual;
    private CatalogoServicioRepository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.catalogo_servicios_activity_actualizar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        spEstado = findViewById(R.id.catalogo_act_spn_estado);
        txtNombre = findViewById(R.id.catalogo_act_txt_nombre);
        txtDescripcion = findViewById(R.id.catalogo_act_txt_descripcion);
        txtPrecio = findViewById(R.id.catalogo_act_txt_precio);

        ArrayAdapter<Estados> adapterEstados = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                Estados.values()
        );

        adapterEstados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEstado.setAdapter(adapterEstados);
        cargarDatosActuales(adapterEstados);
        repo = new CatalogoServicioRepository();
    }

    private void cargarDatosActuales(ArrayAdapter<Estados> adapter) {
        CatalogoServicio servicioRecibido = (CatalogoServicio) getIntent().getSerializableExtra("servicio");

        if (servicioRecibido != null) {
            this.idServicioActual = servicioRecibido.getUid();

            txtNombre.setText(servicioRecibido.getNombre());
            txtDescripcion.setText(servicioRecibido.getDescripcion());
            txtPrecio.setText(String.valueOf(servicioRecibido.getPrecio()));

            try {
                Estados estadoEnum = Estados.valueOf(servicioRecibido.getEstado().toString());
                int position = adapter.getPosition(estadoEnum);
                spEstado.setSelection(position);
            } catch (IllegalArgumentException e) {
                spEstado.setSelection(0);
            }
        }
    }

    private boolean validarCampos() {
        String nombre = txtNombre.getText().toString().trim();
        String descripcion = txtDescripcion.getText().toString().trim();
        String precioCadena = txtPrecio.getText().toString().trim();

        if (nombre.isEmpty()) {
            txtNombre.setError("El nombre es obligatorio");
            txtNombre.requestFocus();
            return false;
        } else if (nombre.length() < 5) {
            txtNombre.setError("El nombre debe tener 5 o más caracteres");
            txtNombre.requestFocus();
            return false;
        }

        if (descripcion.isEmpty()) {
            txtDescripcion.setError("La descripción es obligatoria");
            txtDescripcion.requestFocus();
            return false;
        } else if (descripcion.length() < 10) {
            txtDescripcion.setError("La descripción debe tener 10 o más caracteres");
            txtDescripcion.requestFocus();
            return false;
        }

        if (precioCadena.isEmpty()) {
            txtPrecio.setError("El precio es obligatorio");
            txtPrecio.requestFocus();
            return false;
        }

        try {
            double precio = Double.parseDouble(precioCadena);
            if (precio <= 0) {
                txtPrecio.setError("El precio debe ser mayor a 0");
                txtPrecio.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            txtPrecio.setError("Ingrese un precio válido");
            return false;
        }
        return true;
    }

    private CatalogoServicioUpdateDto buildServiceDto() {
        if (!validarCampos()) {
            return null;
        }

        String nombre = txtNombre.getText().toString().trim();
        String descripcion = txtDescripcion.getText().toString().trim();
        double precio = Double.parseDouble(txtPrecio.getText().toString().trim());
        Estados estadoSeleccionado = (Estados) spEstado.getSelectedItem();

        CatalogoServicioUpdateDto servicioAct = new CatalogoServicioUpdateDto(
                nombre,
                descripcion,
                precio,
                estadoSeleccionado
        );
        servicioAct.setUid(this.idServicioActual);
        return servicioAct;
    }

    public void actualizarServicio(View v) {
        CatalogoServicioUpdateDto dtoUpdate = buildServiceDto();
        if (dtoUpdate == null) return;

        if (dtoUpdate.getUid() == null) {
            Toast.makeText(this, "Error: No se cargó el ID del servicio", Toast.LENGTH_SHORT).show();
            return;
        }
        v.setEnabled(false);
        repo.actualizarServicio(dtoUpdate, new RepositoryCallBack<Void>() {
            @Override
            public void onSuccess(Void result) {
                Toast.makeText(ActualizarCatalogoServicios.this, "Servicio actualizado correctamente", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Exception e) {
                v.setEnabled(true);
                Toast.makeText(ActualizarCatalogoServicios.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void limpiarCampos(View v) {
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        spEstado.setSelection(0);

        txtNombre.setError(null);
        txtDescripcion.setError(null);
        txtPrecio.setError(null);

        txtNombre.requestFocus();
    }

    public void regresar(View v) {
        finish();
    }
}