package com.grupo5.carwashapp.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.grupo5.carwashapp.R;
import com.grupo5.carwashapp.activities.facturacion.MenuFacturacion;
import com.grupo5.carwashapp.activities.facturacion.RegistrarFactura;
import com.grupo5.carwashapp.activities.servicio.ConsultarServicio;
import com.grupo5.carwashapp.activities.servicio.RegistrarServicio;
import com.grupo5.carwashapp.activities.usuario.ConsultarUsuario;
import com.grupo5.carwashapp.activities.usuario.MenuUsuarios;
import com.grupo5.carwashapp.activities.usuario.RegistrarUsuario;
import com.grupo5.carwashapp.activities.vehiculo.ConsultarVehiculo;
import com.grupo5.carwashapp.activities.vehiculo.RegistrarVehiculo;
import com.grupo5.carwashapp.repository.UsuarioRepository;


public class Home extends AppCompatActivity {
    CardView cardUsuarios, cardServicios, cardVehiculos, cardFacturacion;

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
        cardUsuarios = findViewById(R.id.home_card_usuarios);
        cardServicios = findViewById(R.id.home_card_servicios);
        cardVehiculos = findViewById(R.id.home_card_vehiculos);
        cardFacturacion = findViewById(R.id.home_card_facturacion);

        cardUsuarios.setOnClickListener(v -> {
            Intent i = new Intent(Home.this, MenuUsuarios.class);
            startActivity(i);
        });

        cardServicios.setOnClickListener(v -> {
            Intent i = new Intent(Home.this, RegistrarFactura.class);
            startActivity(i);
        });

        cardVehiculos.setOnClickListener(v -> {
            Intent i = new Intent(Home.this, ConsultarUsuario.class);
            startActivity(i);
        });

        cardFacturacion.setOnClickListener(v -> {
            Intent i = new Intent(Home.this, MenuFacturacion.class);
            startActivity(i);
        });

        TextView mensajeBienvenida = findViewById(R.id.home_lbl_bienvenida);
        SharedPreferences prefs = getSharedPreferences("CarWashSession", MODE_PRIVATE);
        String nombreUser = prefs.getString("nombreUsuario", "Usuario");

        mensajeBienvenida.setText(String.format("Bienvenido, %s", nombreUser));
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
        if (item.getItemId() == R.id.mp_servicio_nuevo){
            Intent ventanaRegistroServicio = new Intent( this, RegistrarServicio.class);
            startActivity(ventanaRegistroServicio);
        }

        if (item.getItemId() == R.id.mp_vehiculo_consultar){
            Intent ventanaConsultaServicio = new Intent( this, ConsultarServicio.class);
            startActivity(ventanaConsultaServicio);
        }

        if (item.getItemId() == R.id.mp_acercaDe){
            Dialog acercaDe = new Dialog(this);
            acercaDe.setContentView(R.layout.dialog_acerca_de);
            acercaDe.setCancelable(false);

            Button btnRegresar = acercaDe.findViewById(R.id.dlg_btn_regresar);
            btnRegresar.setOnClickListener(v -> acercaDe.dismiss());

            acercaDe.show();

            return true;
        }

        if (item.getItemId() == R.id.mp_cerrarSesion) {
            SharedPreferences prefs = getSharedPreferences("CarWashSession", MODE_PRIVATE);
            prefs.edit().clear().apply();

            UsuarioRepository repoUsuario = new UsuarioRepository();
            repoUsuario.logout();

            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}