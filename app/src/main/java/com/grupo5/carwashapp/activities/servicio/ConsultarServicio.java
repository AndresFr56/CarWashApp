package com.grupo5.carwashapp.activities.servicio;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.grupo5.carwashapp.R;
import com.grupo5.carwashapp.models.Servicio;
import com.grupo5.carwashapp.models.dtos.servicio.ServicioUpdateDTO;
import com.grupo5.carwashapp.models.enums.EstadoServicio;
import com.grupo5.carwashapp.models.enums.TipoLavado;
import com.grupo5.carwashapp.repository.ServicioRepository;
import java.util.Calendar;


public class ConsultarServicio extends AppCompatActivity {
    private TextInputEditText id_serv,vehiculoserv, empleadoserv, indicacionesserv ;
    private Spinner spestadoserv,spTipoLavado;
    private EditText horaInicio, horaFin;
    private Button btnConsultar,btnModificar,btnEliminar;
    private TextView descripcionTxt,fechacalensrv,precioserv;

    private ServicioRepository serviciorepo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.servicio_activity_consultar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;


        });
        serviciorepo = new ServicioRepository();

        id_serv = findViewById(R.id.txt_id_serv);
        vehiculoserv = findViewById(R.id.consul_vehiculoserv);
        empleadoserv = findViewById(R.id.consul_empleadoserv);
        fechacalensrv =findViewById(R.id.con_fechaserv);
        precioserv=findViewById(R.id.cons_precioserv);
        horaInicio = findViewById(R.id.cons_horainix_serv);
        horaFin = findViewById(R.id.con_horafin_serv);
        indicacionesserv = findViewById(R.id.txt_con_indicaciones);
        descripcionTxt = findViewById(R.id.cons_descripserv);

        spTipoLavado = findViewById(R.id.sp_tipolavado);
        spestadoserv = findViewById(R.id.sp_estadoserv);

        btnConsultar = findViewById(R.id.btn_buscar_serv);
        btnModificar = findViewById(R.id.btn_serv_actualizar);
        btnEliminar = findViewById(R.id.btn_serv_eliminar);
        cargarSpinnerEstadoServicio();
        cargarSpinnerTipoLavado();


        btnConsultar.setOnClickListener(v -> {

            String textoBusqueda = id_serv.getText().toString().trim();

            if (textoBusqueda.isEmpty()) {
                Toast.makeText(this, "Ingrese ID o número de servicio", Toast.LENGTH_SHORT).show();
                return;
            }

            // Si es número → buscar por nro_servicio
            if (textoBusqueda.matches("\\d+")) {
                int nro = Integer.parseInt(textoBusqueda);

                serviciorepo.buscarPorNumero(nro, new ServicioRepository.OnBuscarServicio() {
                    @Override
                    public void onFound(Servicio s) {
                        cargarEnPantalla(s);
                    }

                    @Override
                    public void onNotFound() {
                        Toast.makeText(ConsultarServicio.this, "No existe servicio con ese número", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(ConsultarServicio.this, error, Toast.LENGTH_LONG).show();
                    }
                });

            } else { // Caso contrario, buscar por ID
                serviciorepo.buscarPorId(textoBusqueda, new ServicioRepository.OnBuscarServicio() {
                    @Override
                    public void onFound(Servicio s) {
                        cargarEnPantalla(s);
                    }

                    @Override
                    public void onNotFound() {
                        Toast.makeText(ConsultarServicio.this, "No existe servicio con ese ID", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(ConsultarServicio.this, error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        btnModificar.setOnClickListener(v -> {

            String id = id_serv.getText().toString();

            if (id.isEmpty()) {
                Toast.makeText(this, "Primero busque un servicio", Toast.LENGTH_SHORT).show();
                return;
            }

            TipoLavado tipo = (TipoLavado) spTipoLavado.getSelectedItem();
            String cedulaEmp = empleadoserv.getText().toString();
            EstadoServicio estadoEnum = EstadoServicio.values()[spestadoserv.getSelectedItemPosition()];

            int idVeh = Integer.parseInt(vehiculoserv.getText().toString());

            ServicioUpdateDTO dto = new ServicioUpdateDTO(
                    id,
                    tipo,
                    fechacalensrv.getText().toString(),
                    horaInicio.getText().toString(),
                    horaFin.getText().toString(),
                    indicacionesserv.getText().toString(),
                    0,                        // estado lógico (0= activo normalmente)
                    estadoEnum,               // estadoServicio
                    idVeh,                    // idVehiculo
                    cedulaEmp                 // cedulaEmpleado
            );


            serviciorepo.actualizarServicio(dto, new ServicioRepository.OnServiceResult() {
                @Override
                public void onSuccess(String msg) {
                    Toast.makeText(ConsultarServicio.this, msg, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(ConsultarServicio.this, error, Toast.LENGTH_LONG).show();
                }
            });

        });
        btnEliminar.setOnClickListener(v -> {

            String id = id_serv.getText().toString();

            if (id.isEmpty()) {
                Toast.makeText(this, "Primero busque un servicio", Toast.LENGTH_SHORT).show();
                return;
            }

            serviciorepo.eliminarLogico(id, new ServicioRepository.OnServiceResult() {
                @Override
                public void onSuccess(String msg) {
                    Toast.makeText(ConsultarServicio.this, msg, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(ConsultarServicio.this, error, Toast.LENGTH_LONG).show();
                }
            });

        });
    }
    private void cargarSpinnerTipoLavado() {
        ArrayAdapter<TipoLavado> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                TipoLavado.values()
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipoLavado.setAdapter(adapter);
    }

    private void cargarSpinnerEstadoServicio() {
        ArrayAdapter<EstadoServicio> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                EstadoServicio.values()
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spestadoserv.setAdapter(adapter);
    }
    private void configurarAutoPrecioDescripcion() {
        spTipoLavado.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {

                // Obtener el tipo seleccionado
                TipoLavado tipo = (TipoLavado) spTipoLavado.getSelectedItem();

                // Mostrar automáticamente
                precioserv.setText(String.valueOf(tipo.getCosto()));
                descripcionTxt.setText(tipo.getDescripcion());
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });
    }
    private void configurarTimePickers() {
        horaInicio.setKeyListener(null);
        horaFin.setKeyListener(null);

        horaInicio.setOnClickListener(v -> mostrarTimePicker(horaInicio));
        horaFin.setOnClickListener(v -> mostrarTimePicker(horaFin));
    }

    private void mostrarTimePicker(EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        android.app.TimePickerDialog timePicker = new android.app.TimePickerDialog(
                ConsultarServicio.this,
                (view, hourOfDay, minute1) -> {
                    String hora = String.format("%02d:%02d", hourOfDay, minute1);
                    editText.setText(hora);
                },
                hour,
                minute,
                true
        );
        timePicker.show();
    }



    private void cargarEnPantalla(Servicio s) {
        empleadoserv.setText(s.getCedula_empleado());
        vehiculoserv.setText(String.valueOf(s.getId_vehiculo()));

        // Spinner tipo lavado
        TipoLavado tipo = TipoLavado.valueOf(s.getTipo_lavado());
        spTipoLavado.setSelection(tipo.ordinal());

        descripcionTxt.setText(s.getDescripcion_servicio());

        // Precios (si está en otro TextView)
        precioserv.setText(String.valueOf(s.getCosto()));

        horaInicio.setText(s.getHora_inicio());
        horaFin.setText(s.getHora_fin());

        indicacionesserv.setText(s.getIndicaciones());

        // Fecha
        fechacalensrv.setText(s.getFecha());

        // Estado del servicio
        spestadoserv.setSelection(s.getEstadoServicio().ordinal());

        // Guarda el ID real para actualización y eliminación
        id_serv.setText(s.getId_servicio());
    }

}