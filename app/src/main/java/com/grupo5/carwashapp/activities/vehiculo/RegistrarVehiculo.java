package com.grupo5.carwashapp.activities.vehiculo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.grupo5.carwashapp.R;
import com.grupo5.carwashapp.models.Vehiculo;
import com.grupo5.carwashapp.repository.VehiculoRepository;

public class RegistrarVehiculo extends AppCompatActivity {

    private TextInputEditText txtPlaca, txtMarca, txtModelo, txtTipo, txtColor;
    private VehiculoRepository repoVehiculo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vehiculo_activity_registrar);

        repoVehiculo = new VehiculoRepository();

        txtPlaca = findViewById(R.id.veh_txt_placa);
        txtMarca = findViewById(R.id.veh_txt_marca);
        txtModelo = findViewById(R.id.veh_txt_modelo);
        txtTipo = findViewById(R.id.veh_txt_tipo);
        txtColor = findViewById(R.id.veh_txt_color);


        InputFilter soloLetras = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    char c = source.charAt(i);
                    if (!(Character.isLetter(c) || c == ' ')) {
                        return "";
                    }
                }
                return null;
            }
        };

        txtMarca.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15), soloLetras});
        txtTipo.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15), soloLetras});
        txtColor.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(10),
                soloLetras
        });
    }


    public void guardarVehiculo(View v) {
        String placa = getText(txtPlaca).toUpperCase().trim();
        String marca = getText(txtMarca).trim();
        String modelo = getText(txtModelo).trim();
        String tipo = getText(txtTipo).trim();
        String color = getText(txtColor).trim();

        if (!validar(placa, marca, modelo, tipo, color)) return;

        String clienteIdTemp;
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            clienteIdTemp = FirebaseAuth.getInstance().getCurrentUser().getUid();
        } else {
            SharedPreferences prefs = getSharedPreferences("CarWashSession", MODE_PRIVATE);
            clienteIdTemp = prefs.getString("uidUsuario", null);
        }

        if (clienteIdTemp == null) {
            Toast.makeText(this, "No se detectó sesión. Vuelve a iniciar sesión.", Toast.LENGTH_LONG).show();
            return;
        }

        final String clienteId = clienteIdTemp;
        repoVehiculo.existePlaca(placa, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(RegistrarVehiculo.this, "Ya existe un vehículo con esa placa", Toast.LENGTH_SHORT).show();
                    return;
                }

                Vehiculo vehiculo = new Vehiculo(clienteId, placa, marca, modelo, color, tipo);

                repoVehiculo.crearVehiculo(vehiculo, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegistrarVehiculo.this, "Vehículo registrado", Toast.LENGTH_SHORT).show();
                        limpiarFormularioVehiculo(null);
                        finish();
                    } else {
                        Toast.makeText(RegistrarVehiculo.this,
                                "Error al registrar: " + (task.getException() != null ? task.getException().getMessage() : ""),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(RegistrarVehiculo.this, "Error BD: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void limpiarFormularioVehiculo(View v) {
        txtPlaca.setText("");
        txtMarca.setText("");
        txtModelo.setText("");
        txtTipo.setText("");
        txtColor.setText("");
        txtPlaca.requestFocus();
    }

    public void cancelarRegistroVehiculo(View v) {
        finish();
    }

    private boolean validar(String placa, String marca, String modelo, String tipo, String color) {

        if (placa.isEmpty()) {
            txtPlaca.setError("La placa es obligatoria");
            txtPlaca.requestFocus();
            return false;
        } else if (placa.length() > 10) {
            txtPlaca.setError("La placa no debe superar 10 caracteres");
            txtPlaca.requestFocus();
            return false;
        }

        if (marca.isEmpty()) {
            txtMarca.setError("La marca es obligatoria");
            txtMarca.requestFocus();
            return false;
        } else if (!marca.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            txtMarca.setError("La marca solo debe contener letras");
            txtMarca.requestFocus();
            return false;
        } else if (marca.length() > 15) {
            txtMarca.setError("La marca no debe superar 15 caracteres");
            txtMarca.requestFocus();
            return false;
        }

        if (modelo.isEmpty()) {
            txtModelo.setError("El modelo es obligatorio");
            txtModelo.requestFocus();
            return false;
        } else if (!modelo.matches("[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ ]+")) {
            txtModelo.setError("El modelo solo puede contener letras y números");
            txtModelo.requestFocus();
            return false;
        } else if (modelo.length() > 20) {
            txtModelo.setError("El modelo no debe superar 20 caracteres");
            txtModelo.requestFocus();
            return false;
        }

        if (tipo.isEmpty()) {
            txtTipo.setError("El tipo es obligatorio");
            txtTipo.requestFocus();
            return false;
        } else if (!tipo.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            txtTipo.setError("El tipo solo debe contener letras");
            txtTipo.requestFocus();
            return false;
        } else if (tipo.length() > 15) {
            txtTipo.setError("El tipo no debe superar 15 caracteres");
            txtTipo.requestFocus();
            return false;
        }

        if (color.isEmpty()) {
            txtColor.setError("El color es obligatorio");
            txtColor.requestFocus();
            return false;
        } else if (color.length() > 10) {
            txtColor.setError("El color no debe superar 10 caracteres");
            txtColor.requestFocus();
            return false;
        }

        return true;
    }


    private String getText(TextInputEditText et) {
        return et.getText() == null ? "" : et.getText().toString();
    }

    public void regresar(View v) {
        finish();
    }
}
