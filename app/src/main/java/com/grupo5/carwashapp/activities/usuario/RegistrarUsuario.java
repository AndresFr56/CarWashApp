package com.grupo5.carwashapp.activities.usuario;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
import com.grupo5.carwashapp.activities.Login;
import com.grupo5.carwashapp.database.ConexionBD;
import com.grupo5.carwashapp.model.Usuario;

public class RegistrarUsuario extends AppCompatActivity {
    private Spinner spRol, spEstado;

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
        spRol = findViewById(R.id.reg_sp_rol);
        spEstado = findViewById(R.id.reg_sp_estado);

        //Llenamos los spinners usando los recursos strings.xml
        ArrayAdapter<CharSequence> adapterRol = ArrayAdapter.createFromResource(
                this,
                R.array.roles_array,
                android.R.layout.simple_spinner_item
        );
        adapterRol.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRol.setAdapter(adapterRol);

        ArrayAdapter<CharSequence> adapterEstado = ArrayAdapter.createFromResource(
                this,
                R.array.estados_array,
                android.R.layout.simple_spinner_item
        );
        adapterEstado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEstado.setAdapter(adapterEstado);
    }

    public void guardarUsuario(View v){
        TextInputEditText cedulaT, nombresT, apellidosT, telefonoT, correoT, direccionT, contraseniaT;

        cedulaT = findViewById(R.id.reg_txt_cedula);
        nombresT = findViewById(R.id.reg_txt_nombres);
        apellidosT = findViewById(R.id.reg_txt_apellidos);
        telefonoT = findViewById(R.id.reg_txt_telefono);
        correoT = findViewById(R.id.reg_txt_correo);
        direccionT = findViewById(R.id.reg_txt_direccion);
        contraseniaT = findViewById(R.id.reg_txt_contrasenia);

        Usuario usuario = new Usuario(
                cedulaT.getText().toString(),
                nombresT.getText().toString(),
                apellidosT.getText().toString(),
                telefonoT.getText().toString(),
                correoT.getText().toString(),
                direccionT.getText().toString(),
                spRol.getSelectedItem().toString(),
                spEstado.getSelectedItem().toString(),
                contraseniaT.getText().toString()
        );
        guardarDatosDB(usuario);
    }

    private void guardarDatosDB(Usuario usuario){
        ConexionBD CarWashAppBD = new ConexionBD(this);
        final SQLiteDatabase editCarWashBD = CarWashAppBD.getWritableDatabase();
        if (editCarWashBD != null) {
            ContentValues cv = new ContentValues();

            cv.put("cedula", usuario.getCedula());
            cv.put("nombre", usuario.getNombres());
            cv.put("apellido", usuario.getApellidos());
            cv.put("telefono", usuario.getTelefono());
            cv.put("rol", usuario.getRol());
            cv.put("correo", usuario.getCorreo());
            cv.put("direccion", usuario.getDireccion());
            cv.put("estado", usuario.getEstado());
            cv.put("contrasenia", usuario.getContrasenia());

            editCarWashBD.insert( "usuario", null, cv);
            Toast.makeText(this,"Usuario registrado correctamente",Toast.LENGTH_LONG).show();
            editCarWashBD.close();
        }
    }

    public void limpiarFormulario(View v) {
        ((TextInputEditText) findViewById(R.id.reg_txt_cedula)).setText("");
        ((TextInputEditText) findViewById(R.id.reg_txt_nombres)).setText("");
        ((TextInputEditText) findViewById(R.id.reg_txt_apellidos)).setText("");
        ((TextInputEditText) findViewById(R.id.reg_txt_telefono)).setText("");
        ((TextInputEditText) findViewById(R.id.reg_txt_correo)).setText("");
        ((TextInputEditText) findViewById(R.id.reg_txt_direccion)).setText("");
        ((TextInputEditText) findViewById(R.id.reg_txt_contrasenia)).setText("");
    }

    public void cancelarRegistro(View v){
        Intent ventanaLogin = new Intent(v.getContext(), Login.class);
        startActivity(ventanaLogin);
    }
}