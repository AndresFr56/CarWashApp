package com.grupo5.carwashapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.grupo5.carwashapp.R;
import com.grupo5.carwashapp.activities.facturacion.RegistrarFactura;
import com.grupo5.carwashapp.activities.facturacion.ConsultarFactura;
import com.grupo5.carwashapp.activities.servicio.ConsultarServicio;
import com.grupo5.carwashapp.activities.servicio.RegistrarServicio;
import com.grupo5.carwashapp.activities.usuario.ConsultarUsuario;
import com.grupo5.carwashapp.activities.usuario.RegistrarUsuario;
import com.grupo5.carwashapp.activities.vehiculo.ConsultarVehiculo;
import com.grupo5.carwashapp.activities.vehiculo.RegistrarVehiculo;


public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent infoLogin = getIntent();
        String usuarioLogin = infoLogin.getStringExtra( "user");

        TextView mensajeBienvenida = findViewById(R.id.home_lbl_bienvenida);
        mensajeBienvenida.setText("Bienvenido " + usuarioLogin +"!");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Cargo el menu principal en el Activity Home
        MenuInflater menuPrincipal = getMenuInflater();
        menuPrincipal.inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Submenu de Usuarios
        if (item.getItemId() == R.id.mp_usuario_nuevo) {
            Intent ventanaRegistroUsuario = new Intent(this, RegistrarUsuario.class);
            startActivity(ventanaRegistroUsuario);
        }

        if (item.getItemId() == R.id.mp_usuario_consultar){
            Intent ventanaConsultaUsuario = new Intent( this, ConsultarUsuario.class);
            startActivity(ventanaConsultaUsuario);
        }

        //Submenu de Vehiculos
        if (item.getItemId() == R.id.mp_vehiculo_nuevo){
            Intent ventanaRegistroVehiculo = new Intent( this, RegistrarVehiculo.class);
            startActivity(ventanaRegistroVehiculo);
        }

        if (item.getItemId() == R.id.mp_vehiculo_consultar){
            Intent ventanaConsultaVehiculo = new Intent( this, ConsultarVehiculo.class);
            startActivity(ventanaConsultaVehiculo);
        }

        //Submenu de Servicios
        if (item.getItemId() == R.id.mp_vehiculo_nuevo){
            Intent ventanaRegistroServicio = new Intent( this, RegistrarServicio.class);
            startActivity(ventanaRegistroServicio);
        }

        if (item.getItemId() == R.id.mp_vehiculo_consultar){
            Intent ventanaConsultaServicio = new Intent( this, ConsultarServicio.class);
            startActivity(ventanaConsultaServicio);
        }

        //Submenu de Facturación
        if (item.getItemId() == R.id.mp_vehiculo_nuevo){
            Intent ventanaRegistroFacturacion = new Intent( this, RegistrarFactura.class);
            startActivity(ventanaRegistroFacturacion);
        }

        if (item.getItemId() == R.id.mp_vehiculo_consultar){
            Intent ventanaConsultaFactura = new Intent( this, ConsultarFactura.class);
            startActivity(ventanaConsultaFactura);
        }

        if (item.getItemId() == R.id.mp_acercaDe){
        }

        if (item.getItemId() == R.id.mp_cerrarSesion) {
            Intent ventanaLogin = new Intent(this, Login.class);
            startActivity(ventanaLogin);
        }
        return super.onOptionsItemSelected(item);
    }


}