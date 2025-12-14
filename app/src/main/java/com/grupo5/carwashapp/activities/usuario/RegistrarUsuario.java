package com.grupo5.carwashapp.activities.usuario;

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
import com.grupo5.carwashapp.models.dtos.usuario.UsuarioCreateDto;
import com.grupo5.carwashapp.models.enums.Roles;
import com.grupo5.carwashapp.repository.UsuarioRepository;

public class RegistrarUsuario extends AppCompatActivity {
    private TextInputEditText cedulaT, nombresT, apellidosT, telefonoT, correoT, direccionT, contraseniaT;
    private Spinner spRol;
    private UsuarioRepository repoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.usuario_activity_registrar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        repoUsuario = new UsuarioRepository();

        cedulaT = findViewById(R.id.reg_txt_cedula);
        nombresT = findViewById(R.id.reg_txt_nombres);
        apellidosT = findViewById(R.id.reg_txt_apellidos);
        telefonoT = findViewById(R.id.reg_txt_telefono);
        correoT = findViewById(R.id.reg_txt_correo);
        direccionT = findViewById(R.id.reg_txt_direccion);
        contraseniaT = findViewById(R.id.reg_txt_contrasenia);

        // Llenamos los roles con los valores del enum
        spRol = findViewById(R.id.reg_sp_rol);

        ArrayAdapter<Roles> adapterRoles = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                Roles.values()
        );

        adapterRoles.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRol.setAdapter(adapterRoles);
    }

    private boolean validarFormulario() {
        String cedula = cedulaT.getText().toString().trim();
        String nombre = nombresT.getText().toString();
        String apellido = apellidosT.getText().toString();
        String telefono = telefonoT.getText().toString().trim();
        String correo = correoT.getText().toString().trim();
        String direccion = direccionT.getText().toString();
        String contrasenia = contraseniaT.getText().toString().trim();

        if (cedula.isEmpty()) {
            cedulaT.setError("La cédula es obligatoria");
            cedulaT.requestFocus();
            return false;
        } else if (cedula.length() != 10) {
            cedulaT.setError("La cédula debe tener 10 dígitos");
            cedulaT.requestFocus();
            return false;
        }

        if (nombre.isEmpty()) {
            nombresT.setError("El nombre es obligatorio");
            nombresT.requestFocus();
            return false;
        }

        if (apellido.isEmpty()) {
            apellidosT.setError("El apellido es obligatorio");
            apellidosT.requestFocus();
            return false;
        }

        if (telefono.isEmpty()) {
            telefonoT.setError("El teléfono es obligatorio");
            telefonoT.requestFocus();
            return false;
        } else if (telefono.length() != 10) {
            telefonoT.setError("El teléfono debe tener 10 dígitos");
            telefonoT.requestFocus();
            return false;
        }

        if (correo.isEmpty()) {
            correoT.setError("El correo es obligatorio");
            correoT.requestFocus();
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            correoT.setError("El formato del correo es inválido");
            correoT.requestFocus();
            return false;
        }

        if (direccion.isEmpty()) {
            direccionT.setError("La dirección es obligatoria");
            direccionT.requestFocus();
            return false;
        } else if (direccion.length() < 10) {
            direccionT.setError("La dirección debe tener 10 o más caracteres");
            direccionT.requestFocus();
            return false;
        } else if (direccion.length() > 50) {
            direccionT.setError("La dirección debe tener menos de 50 caracteres");
            direccionT.requestFocus();
            return false;
        }

        if (contrasenia.isEmpty()) {
            contraseniaT.setError("La contraseña es obligatoria");
            contraseniaT.requestFocus();
            return false;
        } else if (contrasenia.length() < 6) {
            contraseniaT.setError("La contraseña debe tener al menos 6 caracteres");
            contraseniaT.requestFocus();
            return false;
        }
        return true;
    }

    public void guardarUsuario(View v) {
        if (!validarFormulario()) {
            return;
        }
        String cedula = cedulaT.getText().toString().trim();
        String nombres = nombresT.getText().toString().trim();
        String apellidos = apellidosT.getText().toString().trim();
        String telefono = telefonoT.getText().toString().trim();
        String correo = correoT.getText().toString().trim();
        String direccion = direccionT.getText().toString().trim();
        String contrasenia = contraseniaT.getText().toString().trim();

        UsuarioCreateDto usuario = new UsuarioCreateDto(
                cedula,
                nombres,
                apellidos,
                telefono,
                correo,
                direccion,
                Roles.valueOf(spRol.getSelectedItem().toString())
        );
        repoUsuario.registrarUsuario(usuario, contrasenia, task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "No se pudo realizar el registro", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void limpiarFormulario(View v) {
        cedulaT.setText("");
        nombresT.setText("");
        apellidosT.setText("");
        telefonoT.setText("");
        correoT.setText("");
        direccionT.setText("");
        contraseniaT.setText("");
    }

    public void regresar(View v) {
        finish();
    }
}