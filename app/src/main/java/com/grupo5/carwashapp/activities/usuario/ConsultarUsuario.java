package com.grupo5.carwashapp.activities.usuario;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.grupo5.carwashapp.R;
import com.grupo5.carwashapp.models.dtos.usuario.UsuarioUpdateDto;
import com.grupo5.carwashapp.models.enums.Estados;
import com.grupo5.carwashapp.models.enums.Roles;
import com.grupo5.carwashapp.repository.UsuarioRepository;

public class ConsultarUsuario extends AppCompatActivity {
    private TextInputEditText cedulaT, nombresT, apellidosT, telefonoT, correoT, direccionT, cedulaBuscarT;
    private Spinner spRol, spEstado;
    private UsuarioUpdateDto usuarioActual;
    private UsuarioRepository repoUsuario;
    private boolean modoEdicion = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.usuario_activity_consultar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        repoUsuario = new UsuarioRepository();
        usuarioActual = new UsuarioUpdateDto();

        cedulaT = findViewById(R.id.consul_txt_cedula);
        nombresT = findViewById(R.id.consul_txt_nombre);
        apellidosT = findViewById(R.id.consul_txt_apellido);
        telefonoT = findViewById(R.id.consul_txt_telefono);
        correoT = findViewById(R.id.consul_txt_correo);
        direccionT = findViewById(R.id.consul_txt_direccion);
        cedulaBuscarT = findViewById(R.id.consul_txt_cedulaBuscar);

        // Llenamos los roles con los valores del enum
        spRol = findViewById(R.id.consul_spn_rol);

        ArrayAdapter<Roles> adapterRoles = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                Roles.values()
        );

        adapterRoles.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRol.setAdapter(adapterRoles);

        // Llenamos los estados con los valores del enum
        spEstado = findViewById(R.id.consul_spn_estado);

        ArrayAdapter<Estados> adapterEstados = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                Estados.values()
        );

        adapterEstados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEstado.setAdapter(adapterEstados);
    }

    @SuppressLint("Range")
    public void consultarUsuario(View v) {
        String cedulaConsul = cedulaBuscarT.getText().toString();
        repoUsuario.obtenerUsuarioPorCedula(cedulaConsul, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        UsuarioUpdateDto usuario = ds.getValue(UsuarioUpdateDto.class);
                        usuario.setUid(ds.getKey());
                        cedulaT.setText(usuario.getCedula());
                        nombresT.setText(usuario.getNombres());
                        apellidosT.setText(usuario.getApellidos());
                        telefonoT.setText(usuario.getTelefono());
                        correoT.setText(usuario.getCorreo());
                        direccionT.setText(usuario.getDireccion());
                        spRol.setSelection(usuario.getRol().ordinal());
                        spEstado.setSelection(usuario.getEstado().ordinal());
                        usuarioActual = usuario;
                    }
                    bloquearCampos();
                } else {
                    Toast.makeText(v.getContext(), "No se encontraron coincidencias", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private boolean validarFormulario() {
        String cedula = cedulaT.getText().toString().trim();
        String nombre = nombresT.getText().toString().trim();
        String apellido = apellidosT.getText().toString().trim();
        String telefono = telefonoT.getText().toString().trim();
        String correo = correoT.getText().toString().trim();
        String direccion = direccionT.getText().toString().trim();

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
        return true;
    }

    public void actualizarUsuario(View v) {
        if (!modoEdicion) {
            desbloquearCampos();
            modoEdicion = true;
            return;
        }

        UsuarioUpdateDto usuarioActualizado = new UsuarioUpdateDto();
        usuarioActualizado.setCedula(cedulaT.getText().toString());
        usuarioActualizado.setNombres(nombresT.getText().toString());
        usuarioActualizado.setApellidos(apellidosT.getText().toString());
        usuarioActualizado.setTelefono(telefonoT.getText().toString());
        usuarioActualizado.setCorreo(correoT.getText().toString());
        usuarioActualizado.setDireccion(direccionT.getText().toString());
        usuarioActualizado.setRol(Roles.values()[spRol.getSelectedItemPosition()]);
        usuarioActualizado.setEstado(Estados.values()[spEstado.getSelectedItemPosition()]);

        if (!validarFormulario()) {
            return;
        }

        repoUsuario.actualizarUsuario(usuarioActual.getUid(), usuarioActualizado, task -> {
            if (task.isSuccessful()) {
                Toast.makeText(v.getContext(), "Usuario actualizado correctamente", Toast.LENGTH_LONG).show();
                bloquearCampos();
                modoEdicion = false;
            } else {
                Toast.makeText(v.getContext(), "No se pudo realizar la actualización", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void eliminarUsuario(View v) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar Usuario")
                .setMessage("¿Está seguro que desea eliminar permanentemente al usuario " + usuarioActual.getNombres() + "?")
                .setPositiveButton("Sí, eliminar", (dialog, which) -> {
                    realizarEliminacion(v);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void realizarEliminacion(View v) {
        repoUsuario.eliminarUsuarioLogico(usuarioActual.getUid(), task -> {
            if (task.isSuccessful()) {
                Toast.makeText(v.getContext(), "Usuario dado de baja correctamente", Toast.LENGTH_SHORT).show();
                limpiarCampos(v);
                usuarioActual = null;
            } else {
                Toast.makeText(v.getContext(), "No se pudo dar de baja el usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bloquearCampos() {
        cedulaT.setEnabled(false);
        nombresT.setEnabled(false);
        apellidosT.setEnabled(false);
        telefonoT.setEnabled(false);
        correoT.setEnabled(false);
        direccionT.setEnabled(false);
        spRol.setEnabled(false);
        spEstado.setEnabled(false);
    }

    private void desbloquearCampos() {
        cedulaT.setEnabled(true);
        nombresT.setEnabled(true);
        apellidosT.setEnabled(true);
        telefonoT.setEnabled(true);
        correoT.setEnabled(true);
        direccionT.setEnabled(true);
        spRol.setEnabled(true);
        spEstado.setEnabled(true);
    }

    public void limpiarCampos(View v) {
        cedulaT.setText("");
        nombresT.setText("");
        apellidosT.setText("");
        telefonoT.setText("");
        correoT.setText("");
        direccionT.setText("");
    }

    public void regresar(View v) {
        finish();
    }
}