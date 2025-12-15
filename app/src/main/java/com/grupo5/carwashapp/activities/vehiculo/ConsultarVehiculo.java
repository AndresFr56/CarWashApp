package com.grupo5.carwashapp.activities.vehiculo;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.grupo5.carwashapp.R;
import com.grupo5.carwashapp.models.Vehiculo;
import com.grupo5.carwashapp.repository.VehiculoRepository;

import java.util.Iterator;

public class ConsultarVehiculo extends AppCompatActivity {

    private TextInputEditText txtPlacaBuscar, txtModelo, txtTipo, txtColor;
    private String vehiculoIdActual = null;
    private VehiculoRepository repoVehiculo;

    private boolean modoEdicion = false;
    private Vehiculo vehiculoCargado = null; // guardamos el objeto para mantener clienteId/placa

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.vehiculo_activity_consultar);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        repoVehiculo = new VehiculoRepository();

        // IDs según tu XML
        txtPlacaBuscar = findViewById(R.id.consul_txt_cedulaBuscar);
        txtModelo = findViewById(R.id.consul_txt_cedula);
        txtTipo = findViewById(R.id.consul_txt_nombre);
        txtColor = findViewById(R.id.consul_txt_apellido);

        // Bloquear campos al iniciar
        bloquearCampos(true);
    }

    private void bloquearCampos(boolean bloquear) {
        // bloquear = true => no editable
        txtModelo.setEnabled(!bloquear);
        txtTipo.setEnabled(!bloquear);
        txtColor.setEnabled(!bloquear);

        // opcional: para que se vea como "solo lectura"
        txtModelo.setFocusable(!bloquear);
        txtModelo.setFocusableInTouchMode(!bloquear);

        txtTipo.setFocusable(!bloquear);
        txtTipo.setFocusableInTouchMode(!bloquear);

        txtColor.setFocusable(!bloquear);
        txtColor.setFocusableInTouchMode(!bloquear);

        modoEdicion = !bloquear;
    }

    public void consultarVehiculo(View v) {
        String placa = "";
        if (txtPlacaBuscar.getText() != null) {
            placa = txtPlacaBuscar.getText().toString().toUpperCase().trim();
        }

        if (placa.isEmpty()) {
            txtPlacaBuscar.setError("Ingrese la placa");
            txtPlacaBuscar.requestFocus();
            return;
        }

        repoVehiculo.obtenerVehiculoPorPlaca(placa, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!snapshot.exists()) {
                    Toast.makeText(ConsultarVehiculo.this, "No se encontró un vehículo con esa placa", Toast.LENGTH_SHORT).show();
                    limpiarVista();
                    return;
                }

                Iterator<DataSnapshot> it = snapshot.getChildren().iterator();
                if (!it.hasNext()) {
                    Toast.makeText(ConsultarVehiculo.this, "No se encontró un vehículo con esa placa", Toast.LENGTH_SHORT).show();
                    limpiarVista();
                    return;
                }

                DataSnapshot first = it.next();
                Vehiculo vehiculo = first.getValue(Vehiculo.class);

                if (vehiculo == null) {
                    Toast.makeText(ConsultarVehiculo.this, "Error leyendo datos del vehículo", Toast.LENGTH_SHORT).show();
                    limpiarVista();
                    return;
                }

                vehiculoIdActual = first.getKey();
                vehiculo.setUid(vehiculoIdActual); // opcional
                vehiculoCargado = vehiculo;

                txtModelo.setText(vehiculo.getModelo() != null ? vehiculo.getModelo() : "");
                txtTipo.setText(vehiculo.getTipo() != null ? vehiculo.getTipo() : "");
                txtColor.setText(vehiculo.getColor() != null ? vehiculo.getColor() : "");

                // Bloquear campos luego de cargar
                bloquearCampos(true);

                Toast.makeText(ConsultarVehiculo.this, "Vehículo encontrado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ConsultarVehiculo.this, "Error BD: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // Este método está atado a tu botón btn_act_actualizar (XML: android:onClick="actualizarUsuario")
    public void actualizarUsuario(View v) {
        if (vehiculoIdActual == null || vehiculoCargado == null) {
            Toast.makeText(this, "Primero consulte un vehículo", Toast.LENGTH_SHORT).show();
            return;
        }

        // Primer click: habilitar edición
        if (!modoEdicion) {
            bloquearCampos(false);
            Toast.makeText(this, "Edición habilitada. Ahora puede modificar.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Segundo click: guardar cambios
        String nuevoModelo = txtModelo.getText() != null ? txtModelo.getText().toString().trim() : "";
        String nuevoTipo   = txtTipo.getText() != null ? txtTipo.getText().toString().trim() : "";
        String nuevoColor  = txtColor.getText() != null ? txtColor.getText().toString().trim() : "";

        if (nuevoModelo.isEmpty()) { txtModelo.setError("Modelo obligatorio"); txtModelo.requestFocus(); return; }
        if (nuevoTipo.isEmpty())   { txtTipo.setError("Tipo obligatorio"); txtTipo.requestFocus(); return; }
        if (nuevoColor.isEmpty())  { txtColor.setError("Color obligatorio"); txtColor.requestFocus(); return; }

        // Construimos objeto manteniendo clienteId y placa originales
        Vehiculo actualizado = new Vehiculo(
                vehiculoCargado.getClienteId(),
                vehiculoCargado.getPlaca(),
                vehiculoCargado.getMarca(),   // si no muestras marca aquí, se conserva
                nuevoModelo,
                nuevoColor,
                nuevoTipo
        );

        v.setEnabled(false);
        repoVehiculo.actualizarVehiculo(vehiculoIdActual, actualizado, task -> {
            v.setEnabled(true);
            if (task.isSuccessful()) {
                vehiculoCargado = actualizado;
                bloquearCampos(true);
                Toast.makeText(ConsultarVehiculo.this, "Vehículo actualizado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ConsultarVehiculo.this, "Error al actualizar", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Este método está atado a tu botón btn_act_eliminar (XML: android:onClick="eliminarUsuario")
    public void eliminarUsuario(View v) {
        if (vehiculoIdActual == null) {
            Toast.makeText(this, "Primero consulte un vehículo", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Confirmación")
                .setMessage("¿Esta seguro que desea borrar ?")
                .setCancelable(true)
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    v.setEnabled(false);
                    repoVehiculo.eliminarVehiculo(vehiculoIdActual, task -> {
                        v.setEnabled(true);
                        if (task.isSuccessful()) {
                            Toast.makeText(ConsultarVehiculo.this, "Vehículo eliminado", Toast.LENGTH_SHORT).show();
                            limpiarVista();
                        } else {
                            Toast.makeText(ConsultarVehiculo.this, "No se pudo eliminar", Toast.LENGTH_LONG).show();
                        }
                    });
                })
                .show();
    }

    public void regresar(View v) {
        finish();
    }

    private void limpiarVista() {
        vehiculoIdActual = null;
        vehiculoCargado = null;
        txtModelo.setText("");
        txtTipo.setText("");
        txtColor.setText("");
        bloquearCampos(true);
    }
}
