package com.grupo5.carwashapp.activities.usuario;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
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
import com.grupo5.carwashapp.database.ConexionBD;
import com.grupo5.carwashapp.model.Usuario;

public class ConsultarUsuario extends AppCompatActivity {
    private Spinner spRol, spEstado;

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
        spRol = findViewById(R.id.consul_spn_rol);
        spEstado = findViewById(R.id.consul_spn_estado);

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

    @SuppressLint("Range")
    public void consultarUsuario(View v) {
        TextInputEditText txtCedulaBuscar = findViewById(R.id.consul_txt_cedulaBuscar);

        TextInputEditText txtCedula = findViewById(R.id.consul_txt_cedula);
        TextInputEditText txtNombres = findViewById(R.id.consul_txt_nombre);
        TextInputEditText txtApellidos = findViewById(R.id.consul_txt_apellido);
        TextInputEditText txtTelefono = findViewById(R.id.consul_txt_telefono);
        TextInputEditText txtCorreo = findViewById(R.id.consul_txt_correo);
        TextInputEditText txtDireccion = findViewById(R.id.consul_txt_direccion);
        TextInputEditText txtContrasenia = findViewById(R.id.consul_txt_contrasenia);

        Spinner spRol = findViewById(R.id.consul_spn_rol);
        Spinner spEstado = findViewById(R.id.consul_spn_estado);

        ConexionBD conexion = new ConexionBD(this);
        SQLiteDatabase db = conexion.getReadableDatabase();

        String cedulaIngresada = txtCedulaBuscar.getText().toString();

        Cursor c = db.rawQuery(
                "SELECT cedula,nombre,apellido,telefono,correo,direccion,rol,estado,contrasenia " +
                        "FROM usuario WHERE cedula=?",
                new String[]{cedulaIngresada}
        );

        if (c.moveToFirst()) {
            txtCedula.setText(c.getString(c.getColumnIndex("cedula")));
            txtNombres.setText(c.getString(c.getColumnIndex("nombre")));
            txtApellidos.setText(c.getString(c.getColumnIndex("apellido")));
            txtTelefono.setText(c.getString(c.getColumnIndex("telefono")));
            txtCorreo.setText(c.getString(c.getColumnIndex("correo")));
            txtDireccion.setText(c.getString(c.getColumnIndex("direccion")));

            String rolBD = c.getString(c.getColumnIndex("rol"));
            String estadoBD = c.getString(c.getColumnIndex("estado"));

            txtContrasenia.setText(c.getString(c.getColumnIndex("contrasenia")));

            spRol.setSelection(((ArrayAdapter) spRol.getAdapter()).getPosition(rolBD));
            spEstado.setSelection(((ArrayAdapter) spEstado.getAdapter()).getPosition(estadoBD));
        } else {
            Toast.makeText(this, "La cédula no existe", Toast.LENGTH_LONG).show();
        }
        c.close();
        db.close();
    }

    public void actualizarUsuario(View v){
        TextInputEditText txtCedulaBuscar = findViewById(R.id.consul_txt_cedulaBuscar);
        String cedula = txtCedulaBuscar.getText().toString();

        if (cedula.isEmpty()) {
            Toast.makeText(this, "Debe consultar un usuario", Toast.LENGTH_SHORT).show();
            return;
        }

        TextInputEditText txtCedula = findViewById(R.id.consul_txt_cedula);
        TextInputEditText txtNombres = findViewById(R.id.consul_txt_nombre);
        TextInputEditText txtApellidos = findViewById(R.id.consul_txt_apellido);
        TextInputEditText txtTelefono = findViewById(R.id.consul_txt_telefono);
        TextInputEditText txtCorreo = findViewById(R.id.consul_txt_correo);
        TextInputEditText txtDireccion = findViewById(R.id.consul_txt_direccion);
        TextInputEditText txtContrasenia = findViewById(R.id.consul_txt_contrasenia);

        Spinner spRol = findViewById(R.id.consul_spn_rol);
        Spinner spEstado = findViewById(R.id.consul_spn_estado);

        Usuario usuario = new Usuario(
                txtCedula.getText().toString(),
                txtNombres.getText().toString(),
                txtApellidos.getText().toString(),
                txtTelefono.getText().toString(),
                txtCorreo.getText().toString(),
                txtDireccion.getText().toString(),
                spRol.getSelectedItem().toString(),
                spEstado.getSelectedItem().toString(),
                txtContrasenia.getText().toString()
        );

        ConexionBD conexion = new ConexionBD(this);
        SQLiteDatabase db = conexion.getWritableDatabase();

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

        int filasAfectadas = db.update("usuario", cv, "cedula=?", new String[]{cedula});
        db.close();

        if (filasAfectadas > 0) {
            Toast.makeText(this, "Usuario actualizado correctamente", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No se pudo realizar la actualización", Toast.LENGTH_LONG).show();
        }
    }

    public void eliminarUsuario(View v){
        TextInputEditText txtCedulaBuscar = findViewById(R.id.consul_txt_cedulaBuscar);
        String cedula = txtCedulaBuscar.getText().toString();

        if (cedula.isEmpty()) {
            Toast.makeText(this, "Debe consultar un usuario", Toast.LENGTH_SHORT).show();
            return;
        }

        ConexionBD conexion = new ConexionBD(this);
        SQLiteDatabase db = conexion.getWritableDatabase();

        int filasAfectadas = db.delete("usuario","cedula=?", new String[]{cedula});
        db.close();

        if (filasAfectadas > 0) {
            Toast.makeText(this, "Usuario eliminado correctamente", Toast.LENGTH_LONG).show();
            limpiarCampos(v);
        } else {
            Toast.makeText(this, "No se pudo realizar la eliminación", Toast.LENGTH_LONG).show();
        }
    }

    public void limpiarCampos(View v) {
        ((TextInputEditText) findViewById(R.id.reg_txt_cedula)).setText("");
        ((TextInputEditText) findViewById(R.id.reg_txt_nombres)).setText("");
        ((TextInputEditText) findViewById(R.id.reg_txt_apellidos)).setText("");
        ((TextInputEditText) findViewById(R.id.reg_txt_telefono)).setText("");
        ((TextInputEditText) findViewById(R.id.reg_txt_correo)).setText("");
        ((TextInputEditText) findViewById(R.id.reg_txt_direccion)).setText("");
        ((TextInputEditText) findViewById(R.id.reg_txt_contrasenia)).setText("");
    }
}