package com.grupo5.carwashapp.activities.catalogoServicios;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.grupo5.carwashapp.R;
import com.grupo5.carwashapp.interfaces.RepositoryCallBack;
import com.grupo5.carwashapp.models.dtos.catalogoServicio.CatalogoServicioCreateDto;
import com.grupo5.carwashapp.repository.CatalogoServicioRepository;

public class RegistrarCatalogoServicios extends AppCompatActivity {
    private TextInputEditText txtNombre, txtDescripcion, txtPrecio;
    private CatalogoServicioRepository repoCatSer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.catalogo_servicios_activity_registrar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        txtNombre = findViewById(R.id.catalogo_reg_txt_nombre);
        txtDescripcion = findViewById(R.id.catalogo_reg_txt_descripcion);
        txtPrecio = findViewById(R.id.catalogo_reg_txt_precio);
        repoCatSer = new CatalogoServicioRepository();
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

    private CatalogoServicioCreateDto buildServiceDto() {
        if (!validarCampos()) {
            return null;
        }

        String nombre = txtNombre.getText().toString().trim();
        String descripcion = txtDescripcion.getText().toString().trim();
        double precio = Double.parseDouble(txtPrecio.getText().toString().trim());

        CatalogoServicioCreateDto nuevoServicio = new CatalogoServicioCreateDto(
                nombre,
                descripcion,
                precio
        );
        return nuevoServicio;
    }

    public void registrarServicio(View v) {
        CatalogoServicioCreateDto dtoService = buildServiceDto();

        if (dtoService == null) {
            return;
        }

        v.setEnabled(false);

        repoCatSer.registrarServicio(dtoService, new RepositoryCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(RegistrarCatalogoServicios.this, "Servicio registrado correctamente", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Exception e) {
                v.setEnabled(true);
                Toast.makeText(RegistrarCatalogoServicios.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void limpiarCampos(View v) {
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");

        txtNombre.setError(null);
        txtDescripcion.setError(null);
        txtPrecio.setError(null);

        txtNombre.requestFocus();
    }

    public void regresar(View v) {
        finish();
    }
}