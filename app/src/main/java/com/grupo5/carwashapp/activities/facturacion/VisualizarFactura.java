package com.grupo5.carwashapp.activities.facturacion;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.grupo5.carwashapp.interfaces.FacturaCallBack;
import com.grupo5.carwashapp.models.DetalleFactura;
import com.grupo5.carwashapp.models.Factura;
import com.grupo5.carwashapp.models.enums.EstadoFacturas;
import com.grupo5.carwashapp.repository.FacturacionRepository;

import java.util.List;

public class VisualizarFactura extends AppCompatActivity {
    TextView txtFecha, txtCedula, txtCliente, txtVehiculo, txtIva, txtSubtotal, txtTotal, txtEstado;
    private LinearLayout containerDetalles;
    private Button btnAnular, btnPagar;
    private Factura facturaActual;

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
            darDeBajaFactura();
        } else {
            mostrarErrores();
        }
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
    }

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
        } else if ("PENDIENTE".equals(factura.getEstado().toString())) {
            int colorNaranja = ContextCompat.getColor(this, R.color.orange);
            txtEstado.setTextColor(colorNaranja);
        } else if ("ANULADA".equals(factura.getEstado().toString())) {
            int colorRojo = ContextCompat.getColor(this, R.color.btnEliminar);
            txtEstado.setTextColor(colorRojo);
        }
    }

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

    private void darDeBajaFactura() {
        if (facturaActual.getEstado() == EstadoFacturas.ANULADA) {
            btnAnular.setEnabled(false);
            btnPagar.setEnabled(false);
            return;
        }
        btnAnular.setOnClickListener(v -> mostrarDialogoConfirmacion());
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
        repo.anularFactura(facturaActual.getUid(), new FacturaCallBack() {
            @Override
            public void onSuccess() {
                Toast.makeText(VisualizarFactura.this, "Factura anulada correctamente", Toast.LENGTH_SHORT).show();
                txtEstado.setText(nuevoEstado.toString());

                if (nuevoEstado == EstadoFacturas.ANULADA) {
                    btnAnular.setEnabled(false);
                    btnPagar.setEnabled(false);
                    int colorRojo = ContextCompat.getColor(VisualizarFactura.this, R.color.btnEliminar);
                    txtEstado.setTextColor(colorRojo);
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(VisualizarFactura.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFacturasLoaded(List<Factura> facturas) {
            }
        });
    }

    private void mostrarErrores() {
        Toast.makeText(this, "Error al cargar los datos de la factura", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void regresar(View v) {
        finish();
    }
}
