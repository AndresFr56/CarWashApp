package com.grupo5.carwashapp.activities.servicio;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.grupo5.carwashapp.R;
import com.grupo5.carwashapp.models.dtos.servicio.ServicioCreateDTO;
import com.grupo5.carwashapp.models.enums.EstadoServicio;
import com.grupo5.carwashapp.models.enums.TipoLavado;
import com.grupo5.carwashapp.repository.ServicioRepository;

import java.util.Calendar;

public class RegistrarServicio extends AppCompatActivity {
    private TextInputEditText vehiculoserv, empleadoserv, precioserv, fechacalensrv, indicacionesserv;
    private EditText horaInicio, horaFin;
    private Spinner spTipoLavado;
    private Button btnCalendario, btnRegistrar;
    private TextView descripcionTxt;

    private ServicioRepository serviciorepo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.servicio_activity_registrar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        serviciorepo = new ServicioRepository();
        vehiculoserv = findViewById(R.id.reg_txt_vehiserv);
        empleadoserv = findViewById(R.id.reg_text_empleserv);
        precioserv = findViewById(R.id.reg_txt_precioserv);
        fechacalensrv = findViewById(R.id.txt_fechacalensrv);
        indicacionesserv = findViewById(R.id.reg_text_indicaserv);

        horaInicio = findViewById(R.id.HoraIncioTime);
        horaFin = findViewById(R.id.HoraFinalTime);

        spTipoLavado = findViewById(R.id.rg_sp_tipo_lavado);
        btnCalendario = findViewById(R.id.btn_calen);
        btnRegistrar = findViewById(R.id.reg_btn_registrarserv);

        descripcionTxt = findViewById(R.id.textview_descripcion);

        cargarSpinnerTipoLavado();
        configurarAutoPrecioDescripcion();
        configurarCalendario();
        configurarBotonRegistrar();




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
    private void configurarCalendario() {
        btnCalendario.setOnClickListener(v -> {

            Calendar calendario = Calendar.getInstance();
            int año = calendario.get(Calendar.YEAR);
            int mes = calendario.get(Calendar.MONTH);
            int dia = calendario.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    RegistrarServicio.this,
                    (DatePicker view, int year, int month, int dayOfMonth) -> {
                        String fecha = dayOfMonth + "/" + (month + 1) + "/" + year;
                        fechacalensrv.setText(fecha);
                    },
                    año, mes, dia
            );

            dialog.show();
        });
    }
    private void configurarAutoPrecioDescripcion() {

        spTipoLavado.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                TipoLavado tipo = (TipoLavado) spTipoLavado.getSelectedItem();

                precioserv.setText(String.valueOf(tipo.getCosto()));
                descripcionTxt.setText(tipo.getDescripcion());
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) { }
        });
    }
    private void configurarBotonRegistrar() {

        btnRegistrar.setOnClickListener(v -> {

            // VALIDACIÓN
            if (vehiculoserv.getText().toString().isEmpty() ||
                    empleadoserv.getText().toString().isEmpty() ||
                    precioserv.getText().toString().isEmpty() ||
                    fechacalensrv.getText().toString().isEmpty() ||
                    horaInicio.getText().toString().isEmpty() ||
                    horaFin.getText().toString().isEmpty()) {

                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // OBTENER TIPO DE LAVADO
            TipoLavado tipoSeleccionado = (TipoLavado) spTipoLavado.getSelectedItem();

            // CREAR DTO
            int idVehiculo = Integer.parseInt(vehiculoserv.getText().toString());
            int idEmpleado = Integer.parseInt(empleadoserv.getText().toString());

            ServicioCreateDTO dto = new ServicioCreateDTO(
                    tipoSeleccionado,
                    fechaString(fechacalensrv),
                    horaInicio.getText().toString(),
                    horaFin.getText().toString(),
                    indicacionesserv.getText().toString(),
                    0, // estado activo
                    EstadoServicio.PENDIENTE,
                    idVehiculo,
                    idEmpleado
            );

            // GUARDAR
            serviciorepo.crearServicio(dto, new ServicioRepository.OnServiceResult() {
                @Override
                public void onSuccess(String msg) {
                    Toast.makeText(RegistrarServicio.this, msg, Toast.LENGTH_LONG).show();
                    finish();
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(RegistrarServicio.this, "ERROR: " + error, Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    private String fechaString(TextInputEditText txt) {
        return txt.getText() != null ? txt.getText().toString() : "";
    }
}
