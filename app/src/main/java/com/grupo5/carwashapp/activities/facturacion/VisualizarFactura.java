package com.grupo5.carwashapp.activities.facturacion;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.grupo5.carwashapp.R;
import com.grupo5.carwashapp.interfaces.RepositoryCallBack;
import com.grupo5.carwashapp.models.DetalleFactura;
import com.grupo5.carwashapp.models.Factura;
import com.grupo5.carwashapp.models.enums.EstadoFacturas;
import com.grupo5.carwashapp.models.enums.FormaPago;
import com.grupo5.carwashapp.repository.FacturacionRepository;

public class VisualizarFactura extends AppCompatActivity {
    TextView txtFecha, txtCedula, txtCliente, txtVehiculo, txtIva, txtSubtotal, txtTotal, txtEstado;
    private LinearLayout containerDetalles;
    private Button btnAnular, btnPagar;
    private Factura facturaActual;

    private LinearLayout containerInfoPago;
    private TextView txtFormaPago, txtObservaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ver_factura);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        inicializarVistas();
        facturaActual = (Factura) getIntent().getSerializableExtra("datosFactura");

        if (facturaActual != null) {
            cargarDatosGenerales(facturaActual);
            cargarListaDeServicios(facturaActual);
            configurarBotones();
        } else {
            mostrarErrores();
        }
    }

    private void finalizarConResultado() {
        Intent data = new Intent();
        data.putExtra("facturaRetorno", facturaActual);
        setResult(RESULT_OK, data);
        finish();
    }

    private void inicializarVistas() {
        txtFecha = findViewById(R.id.ver_fact_lbl_fecha);
        txtCedula = findViewById(R.id.ver_fact_lbl_cedula);
        txtCliente = findViewById(R.id.ver_fact_lbl_cliente);
        txtVehiculo = findViewById(R.id.ver_fact_lbl_vehiculo);
        txtIva = findViewById(R.id.ver_fact_lbl_iva);
        txtSubtotal = findViewById(R.id.ver_fact_lbl_subtotal);
        txtTotal = findViewById(R.id.ver_fact_lbl_total_grande);
        txtEstado = findViewById(R.id.ver_fact_lbl_estado);
        containerDetalles = findViewById(R.id.ver_fact_container_detalles);
        btnAnular = findViewById(R.id.btnAnularFactura);
        btnPagar = findViewById(R.id.btnPagarFactura);
        containerInfoPago = findViewById(R.id.containerInfoPago);
        txtFormaPago = findViewById(R.id.ver_fact_lbl_forma_pago);
        txtObservaciones = findViewById(R.id.ver_fact_lbl_observaciones);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void cargarDatosGenerales(Factura factura) {
        txtFecha.setText("Fecha: " + factura.getFechaEmision());
        txtCedula.setText("Cédula: " + factura.getClienteCedula());
        txtCliente.setText("Cliente: " + factura.getClienteNombre());

        String infoVehiculo = String.format("Vehículo: %s (%s)", factura.getVehiculoPlaca(), factura.getVehiculoModelo());
        txtVehiculo.setText(infoVehiculo);

        txtIva.setText(String.format("$%.2f", factura.getIva()));
        txtSubtotal.setText(String.format("$%.2f", factura.getSubtotal()));
        txtTotal.setText(String.format("$%.2f", factura.getTotal()));

        txtEstado.setText(factura.getEstado().toString());

        if ("PAGADA".equals(factura.getEstado().toString())) {
            int colorVerde = ContextCompat.getColor(this, R.color.green);
            txtEstado.setTextColor(colorVerde);
            mostrarDetallesPago(factura.getFormaPago(), factura.getObservaciones());
        } else if ("PENDIENTE".equals(factura.getEstado().toString())) {
            int colorNaranja = ContextCompat.getColor(this, R.color.orange);
            txtEstado.setTextColor(colorNaranja);
        } else if ("ANULADA".equals(factura.getEstado().toString())) {
            int colorRojo = ContextCompat.getColor(this, R.color.btnEliminar);
            txtEstado.setTextColor(colorRojo);
        }
    }

    @SuppressLint("DefaultLocale")
    private void cargarListaDeServicios(Factura factura) {
        containerDetalles.removeAllViews();

        if (factura.getDetalles() != null) {
            for (DetalleFactura det : factura.getDetalles()) {
                View fila = getLayoutInflater().inflate(R.layout.item_detalle, null);

                TextView lblServicio = fila.findViewById(R.id.row_lbl_servicio);
                TextView lblPrecio = fila.findViewById(R.id.row_lbl_precio);

                lblServicio.setText(det.getNombreServicio());
                lblPrecio.setText(String.format("$%.2f", det.getSubtotal()));

                containerDetalles.addView(fila);
            }
        }
    }

    private void configurarBotones() {
        if (facturaActual.getEstado() == EstadoFacturas.ANULADA) {
            bloquearBotones("ANULADA");
            return;
        }
        if (facturaActual.getEstado() == EstadoFacturas.PAGADA) {
            bloquearBotones("PAGADA");
            return;
        }

        btnAnular.setOnClickListener(v -> mostrarDialogoConfirmacion());
        btnPagar.setOnClickListener(v -> mostrarDialogoCobro());
    }

    private void mostrarDialogoCobro() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Registrar Cobro");
        builder.setMessage("Seleccione el método de pago:");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(60, 20, 60, 20);

        final Spinner spinnerPago = new Spinner(this);

        ArrayAdapter<FormaPago> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                FormaPago.values()
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPago.setAdapter(adapter);

        layout.addView(spinnerPago);

        final EditText inputObs = new EditText(this);
        inputObs.setHint("Observaciones");
        layout.addView(inputObs);

        builder.setView(layout);

        builder.setPositiveButton("Confirmar Pago", (dialog, which) -> {
            FormaPago metodoSeleccionado = (FormaPago) spinnerPago.getSelectedItem();
            String obs = inputObs.getText().toString().trim();

            realizarCobro(metodoSeleccionado, obs);
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void realizarCobro(FormaPago formaPago, String observaciones) {
        FacturacionRepository repo = new FacturacionRepository();
        btnPagar.setEnabled(false);

        repo.actualizarEstadoPago(facturaActual.getUid(), formaPago, observaciones, new RepositoryCallBack<Void>() {
            @Override
            public void onSuccess(Void result) {
                Toast.makeText(VisualizarFactura.this, "Pago registrado", Toast.LENGTH_LONG).show();

                txtEstado.setText("PAGADA");
                txtEstado.setTextColor(ContextCompat.getColor(VisualizarFactura.this, R.color.green));

                facturaActual.setEstado(EstadoFacturas.PAGADA);
                facturaActual.setFormaPago(formaPago);
                facturaActual.setObservaciones(observaciones);
                bloquearBotones("PAGADA");
                finalizarConResultado();
            }

            @Override
            public void onFailure(Exception e) {
                btnPagar.setEnabled(true);
                Toast.makeText(VisualizarFactura.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDialogoConfirmacion() {
        new AlertDialog.Builder(this)
                .setTitle("Anular Factura")
                .setMessage("¿Está seguro que desea anular esta factura?")
                .setPositiveButton("Sí, Anular", (dialog, which) -> {
                    cambiarEstado(EstadoFacturas.ANULADA);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void cambiarEstado(EstadoFacturas nuevoEstado) {
        FacturacionRepository repo = new FacturacionRepository();
        btnAnular.setEnabled(false);
        repo.anularFactura(facturaActual.getUid(), new RepositoryCallBack<Void>() {
            @Override
            public void onSuccess(Void resultado) {
                Toast.makeText(VisualizarFactura.this, "Factura anulada correctamente", Toast.LENGTH_SHORT).show();
                txtEstado.setText(nuevoEstado.toString());
                facturaActual.setEstado(nuevoEstado);

                if (nuevoEstado == EstadoFacturas.ANULADA) {
                    btnAnular.setEnabled(false);
                    btnPagar.setEnabled(false);
                    int colorRojo = ContextCompat.getColor(VisualizarFactura.this, R.color.btnEliminar);
                    txtEstado.setTextColor(colorRojo);
                }
                finalizarConResultado();
            }

            @Override
            public void onFailure(Exception e) {
                btnAnular.setEnabled(true);
                Toast.makeText(VisualizarFactura.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDetallesPago(FormaPago formaPago, String observaciones) {
        containerInfoPago.setVisibility(View.VISIBLE);

        if (formaPago != null) {
            txtFormaPago.setText(String.format("Forma de Pago: %s", formaPago));
        }

        if (observaciones != null && !observaciones.isEmpty()) {
            txtObservaciones.setText(String.format("Nota: %s", observaciones));
            txtObservaciones.setVisibility(View.VISIBLE);
        } else {
            txtObservaciones.setVisibility(View.GONE);
        }
    }

    private void mostrarErrores() {
        Toast.makeText(this, "Error al cargar los datos de la factura", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void bloquearBotones(String textoBoton) {
        btnAnular.setEnabled(false);
        btnPagar.setEnabled(false);
        btnPagar.setText(textoBoton);
    }

    public void regresar(View v) {
        finish();
    }
}
