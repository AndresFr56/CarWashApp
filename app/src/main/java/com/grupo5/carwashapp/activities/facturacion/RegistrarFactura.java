package com.grupo5.carwashapp.activities.facturacion;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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
import com.grupo5.carwashapp.interfaces.RepositoryCallBack;
import com.grupo5.carwashapp.models.CatalogoServicio;
import com.grupo5.carwashapp.models.DetalleFactura;
import com.grupo5.carwashapp.models.Factura;
import com.grupo5.carwashapp.models.Usuario;
import com.grupo5.carwashapp.models.Vehiculo;
import com.grupo5.carwashapp.models.enums.EstadoFacturas;
import com.grupo5.carwashapp.models.enums.Roles;
import com.grupo5.carwashapp.repository.CatalogoServicioRepository;
import com.grupo5.carwashapp.repository.FacturacionRepository;
import com.grupo5.carwashapp.repository.UsuarioRepository;
import com.grupo5.carwashapp.repository.VehiculoRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    private final Calendar calendario = Calendar.getInstance();
    private CatalogoServicioRepository repoServicios;

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
        repoServicios = new CatalogoServicioRepository();

        actualizarInputFecha();
        txtFecha.setOnClickListener(v -> mostrarSelectorFecha());

        cargarChecksServicios();
        cargarClientesEnSpinner();
    }

    private void mostrarSelectorFecha() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendario.set(Calendar.YEAR, year);
                    calendario.set(Calendar.MONTH, month);
                    calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    actualizarInputFecha();
                },
                calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH),
                calendario.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void actualizarInputFecha() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        txtFecha.setText(sdf.format(calendario.getTime()));
    }

    private void cargarClientesEnSpinner() {
        UsuarioRepository repoUsuario = new UsuarioRepository();
        repoUsuario.obtenerUsuariosPorRol(Roles.CLIENTE.name(), new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (isFinishing() || isDestroyed()) return;
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
        repoServicios.obtenerServicios(new RepositoryCallBack<List<CatalogoServicio>>() {
            @Override
            public void onSuccess(List<CatalogoServicio> listaServicios) {
                contenedorServicios.removeAllViews();
                for (CatalogoServicio servicio : listaServicios) {
                    CheckBox cb = new CheckBox(RegistrarFactura.this);

                    String etiqueta = servicio.getNombre() + " ($" + servicio.getPrecio() + ")";
                    cb.setText(etiqueta);

                    cb.setTag(servicio);

                    cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        calcularTotales();
                    });
                    contenedorServicios.addView(cb);
                }
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(RegistrarFactura.this, "Error al cargar servicios: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @SuppressLint("DefaultLocale")
    private void calcularTotales() {
        double sumaTemporal = 0;

        for (int i = 0; i < contenedorServicios.getChildCount(); i++) {
            View v = contenedorServicios.getChildAt(i);

            if (v instanceof CheckBox) {
                CheckBox cb = (CheckBox) v;

                if (cb.isChecked()) {
                    CatalogoServicio servicio = (CatalogoServicio) cb.getTag();
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
                    CatalogoServicio servicioObj = (CatalogoServicio) cb.getTag();

                    if (servicioObj != null) {
                        DetalleFactura detalle = new DetalleFactura(
                                servicioObj.getUid(),
                                servicioObj.getNombre(),
                                servicioObj.getPrecio(),
                                1
                        );
                        detalles.add(detalle);
                    }
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

        nuevaFactura.setFechaEmision(txtFecha.getText().toString());
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
        v.setEnabled(false);

        repoFactura.crearFactura(nuevaFactura, new RepositoryCallBack<String>() {
            @Override
            public void onSuccess(String resultado) {
                Toast.makeText(v.getContext(), "Factura registrada correctamente", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onFailure(Exception e) {
                v.setEnabled(true);
                Toast.makeText(RegistrarFactura.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void limpiarFormulario(View v) {
        if (spClientes.getAdapter() != null && spClientes.getCount() > 0) {
            spClientes.setSelection(0);
        }
        for (int i = 0; i < contenedorServicios.getChildCount(); i++) {
            View viewChild = contenedorServicios.getChildAt(i);
            if (viewChild instanceof CheckBox) {
                ((CheckBox) viewChild).setChecked(false);
            }
        }
        this.valorSubtotal = 0;
        this.valorIva = 0;
        this.valorTotal = 0;
    }

    public void regresar(View v) {
        finish();
    }
}