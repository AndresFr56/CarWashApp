package com.grupo5.carwashapp.activities.facturacion;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.grupo5.carwashapp.R;
import com.grupo5.carwashapp.interfaces.FacturaCallBack;
import com.grupo5.carwashapp.models.DetalleFactura;
import com.grupo5.carwashapp.models.Factura;
import com.grupo5.carwashapp.models.Usuario;
import com.grupo5.carwashapp.models.Vehiculo;
import com.grupo5.carwashapp.models.enums.CatalogoServicios;
import com.grupo5.carwashapp.models.enums.EstadoFacturas;
import com.grupo5.carwashapp.repository.FacturacionRepository;
import com.grupo5.carwashapp.repository.UsuarioRepository;
import com.grupo5.carwashapp.repository.VehiculoRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RegistrarFactura extends AppCompatActivity {
    private LinearLayout contenedorServicios;
    private Spinner spClientes;
    private Spinner spVehiculos;
    private TextView txtSubtotal, txtIva, txtTotal;
    private double valorSubtotal = 0;
    private double valorIva = 0;
    private double valorTotal = 0;
    private TextView txtFecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.facturacion_activity_registrar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        spClientes = findViewById(R.id.fact_spn_cliente);
        spVehiculos = findViewById(R.id.fact_spn_vehiculo);
        contenedorServicios = findViewById(R.id.reg_fact_contenedorServicios);
        txtSubtotal = findViewById(R.id.reg_fact_txt_subtotal);
        txtIva = findViewById(R.id.reg_fact_txt_iva);
        txtTotal = findViewById(R.id.reg_fact_txt_total);
        txtFecha = findViewById(R.id.reg_fact_txt_fecha);
        txtFecha.setText(obtenerFechaActual());

        cargarChecksServicios();
        cargarClientesEnSpinner();
    }

    private String obtenerFechaActual() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }

    private void cargarClientesEnSpinner() {
        UsuarioRepository repoUsuario = new UsuarioRepository();
        repoUsuario.obtenerUsuariosPorRol("Cliente", new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Usuario> listaClientes = new ArrayList<>();

                for (DataSnapshot data : snapshot.getChildren()) {
                    Usuario usuario = data.getValue(Usuario.class);
                    if (usuario != null) {
                        usuario.setUid(data.getKey());
                        listaClientes.add(usuario);
                    }
                }
                cargarAdapterClientes(listaClientes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error al cargar clientes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarVehiculosDelCliente(String clienteId) {
        VehiculoRepository repoVehiculo = new VehiculoRepository();
        repoVehiculo.obtenerVehiculosPorCliente(clienteId, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Vehiculo> listaVehiculos = new ArrayList<>();

                for (DataSnapshot data : snapshot.getChildren()) {
                    Vehiculo vehiculo = data.getValue(Vehiculo.class);
                    if (vehiculo != null) {
                        vehiculo.setUid(data.getKey());
                        listaVehiculos.add(vehiculo);
                    }
                }
                cargarAdapterVehiculos(listaVehiculos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RegistrarFactura.this, "Error cargando vehículos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarAdapterVehiculos(List<Vehiculo> listaVehiculos) {
        if (spVehiculos != null) {
            ArrayAdapter<Vehiculo> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    listaVehiculos
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spVehiculos.setAdapter(adapter);
        }
    }

    private void cargarAdapterClientes(List<Usuario> listaClientes) {
        if (spClientes != null) {
            ArrayAdapter<Usuario> adapterClientes = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    listaClientes
            );
            adapterClientes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spClientes.setAdapter(adapterClientes);
            spClientes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Usuario clienteSeleccionado = (Usuario) parent.getItemAtPosition(position);
                    cargarVehiculosDelCliente(clienteSeleccionado.getUid());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
    }

    private void cargarChecksServicios() {
        contenedorServicios.removeAllViews();

        for (CatalogoServicios servicioEnum : CatalogoServicios.values()) {
            CheckBox cb = new CheckBox(this);
            cb.setText(servicioEnum.getEtiqueta());
            cb.setTag(servicioEnum);
            cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                calcularTotales();
            });
            contenedorServicios.addView(cb);
        }
    }

    private void calcularTotales() {
        double sumaTemporal = 0;

        for (int i = 0; i < contenedorServicios.getChildCount(); i++) {
            View v = contenedorServicios.getChildAt(i);

            if (v instanceof CheckBox) {
                CheckBox cb = (CheckBox) v;

                if (cb.isChecked()) {
                    CatalogoServicios servicio = (CatalogoServicios) cb.getTag();
                    sumaTemporal += servicio.getPrecio();
                }
            }
        }

        this.valorSubtotal = sumaTemporal;
        this.valorIva = sumaTemporal * 0.15;
        this.valorTotal = valorSubtotal + valorIva;

        txtSubtotal.setText(String.format("$%.2f", valorSubtotal));
        txtIva.setText(String.format("$%.2f", valorIva));
        txtTotal.setText(String.format("$%.2f", valorTotal));
    }

    public void guardarFactura(View v) {
        if (spClientes.getSelectedItem() == null) {
            Toast.makeText(this, "Seleccione un cliente", Toast.LENGTH_SHORT).show();
            return;
        }
        if (spVehiculos.getSelectedItem() == null) {
            Toast.makeText(this, "Seleccione un vehículo", Toast.LENGTH_SHORT).show();
            return;
        }

        List<DetalleFactura> detalles = new ArrayList<>();

        for (int i = 0; i < contenedorServicios.getChildCount(); i++) {
            View viewChild = contenedorServicios.getChildAt(i);
            if (viewChild instanceof CheckBox) {
                CheckBox cb = (CheckBox) viewChild;
                if (cb.isChecked()) {
                    CatalogoServicios enumServicio = (CatalogoServicios) cb.getTag();

                    DetalleFactura detalle = new DetalleFactura(
                            enumServicio.name(),
                            enumServicio.getNombre(),
                            enumServicio.getPrecio(),
                            1
                    );
                    detalles.add(detalle);
                }
            }
        }

        if (detalles.isEmpty()) {
            Toast.makeText(this, "Debe seleccionar al menos un servicio", Toast.LENGTH_SHORT).show();
            return;
        }

        Usuario clienteSelect = (Usuario) spClientes.getSelectedItem();
        Vehiculo vehiculoSelect = (Vehiculo) spVehiculos.getSelectedItem();

        Factura nuevaFactura = new Factura();

        nuevaFactura.setFechaEmision(obtenerFechaActual());
        nuevaFactura.setEstado(EstadoFacturas.PENDIENTE);

        nuevaFactura.setSubtotal(this.valorSubtotal);
        nuevaFactura.setIva(this.valorIva);
        nuevaFactura.setTotal(this.valorTotal);

        // Datos Cliente
        nuevaFactura.setClienteId(clienteSelect.getUid());
        nuevaFactura.setClienteNombre(clienteSelect.toString());
        nuevaFactura.setClienteCedula(clienteSelect.getCedula());

        // Datos Vehículo
        nuevaFactura.setVehiculoId(vehiculoSelect.getUid());
        nuevaFactura.setVehiculoPlaca(vehiculoSelect.getPlaca());
        nuevaFactura.setVehiculoModelo(vehiculoSelect.getModelo());

        nuevaFactura.setDetalles(detalles);

        FacturacionRepository repoFactura = new FacturacionRepository();

        repoFactura.crearFactura(nuevaFactura, new FacturaCallBack() {
            @Override
            public void onSuccess() {
                Toast.makeText(v.getContext(), "Factura registrada correctamente", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(v.getContext(), "No se pudo registrar la factura " + error, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFacturasLoaded(List<Factura> facturas) {
            }
        });
    }

    public void cancelar(View v) {
        finish();
    }
}