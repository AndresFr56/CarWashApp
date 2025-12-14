package com.grupo5.carwashapp.activities.servicio;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.grupo5.carwashapp.R;
import com.grupo5.carwashapp.interfaces.RepositoryCallBack;
import com.grupo5.carwashapp.models.CatalogoServicio;
import com.grupo5.carwashapp.models.Vehiculo;
import com.grupo5.carwashapp.models.dtos.servicio.ServicioCreateDTO;
import com.grupo5.carwashapp.models.enums.EstadoServicio;
import com.grupo5.carwashapp.models.enums.Estados;
import com.grupo5.carwashapp.models.enums.TipoLavado;
import com.grupo5.carwashapp.repository.CatalogoServicioRepository;
import com.grupo5.carwashapp.repository.ServicioRepository;
import com.grupo5.carwashapp.repository.UsuarioRepository;
import com.grupo5.carwashapp.models.Usuario;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RegistrarServicio extends AppCompatActivity {

    private TextInputEditText  precioserv, fechacalensrv, indicacionesserv;
    private EditText horaInicio, horaFin;
    private Spinner spCatalogoServicio, spEmpleado;
    private Button btnCalendario, btnRegistrar, btncancelar ,btnBorrar;
    private TextView descripcionTxt;

    private ServicioRepository serviciorepo;
    private UsuarioRepository usuarioRepository;
    private CatalogoServicioRepository catalogoServicioRepository;
    private List<CatalogoServicio> listaCatalogoServicios;
    private Spinner spVehiculo;
    private List<Vehiculo> listaVehiculos = new ArrayList<>();


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
        usuarioRepository = new UsuarioRepository();
        catalogoServicioRepository = new CatalogoServicioRepository(); // Inicializar
        listaCatalogoServicios = new ArrayList<>();

        // Inicializar campos

        precioserv = findViewById(R.id.reg_txt_precioserv);
        fechacalensrv = findViewById(R.id.txt_fechacalensrv);
        indicacionesserv = findViewById(R.id.reg_text_indicaserv);

        horaInicio = findViewById(R.id.HoraIncioTime);
        horaFin = findViewById(R.id.HoraFinalTime);
        horaInicio.setKeyListener(null);
        horaFin.setKeyListener(null);

        horaInicio.setOnClickListener(v -> mostrarTimePicker(horaInicio));
        horaFin.setOnClickListener(v -> mostrarTimePicker(horaFin));

        spCatalogoServicio = findViewById(R.id.rg_sp_tipo_lavado);
        spEmpleado = findViewById(R.id.sp_empleado_serv); // Spinner para empleados
        spVehiculo = findViewById(R.id.sp_plvehiculo_serv);

        btnCalendario = findViewById(R.id.btn_calen);
        btnRegistrar = findViewById(R.id.reg_btn_registrarserv);
        btncancelar = findViewById(R.id.reg_btn_cancelarserv);
        btnBorrar = findViewById(R.id.reg_btn_borrarserv);

        descripcionTxt = findViewById(R.id.textview_descripcion);
        btncancelar.setOnClickListener(v -> {
            finish();
        });
        btnBorrar.setOnClickListener(v -> {


            precioserv.setText("");
            fechacalensrv.setText("");
            indicacionesserv.setText("");
            horaInicio.setText("");
            horaFin.setText("");

            // Resetear spinners
            spCatalogoServicio.setSelection(0);
            spEmpleado.setSelection(0);
            spVehiculo.setSelection(0);

            // Limpiar descripción
            descripcionTxt.setText("");

            Toast.makeText(this, "Campos limpiados", Toast.LENGTH_SHORT).show();
        });



        cargarCatalogoServicios();
        cargarEmpleadosEnSpinner(); // Cargar empleados en el spinner

        configurarCalendario();
        configurarBotonRegistrar();
        cargarVehiculosEnSpinner();
    }

    private void cargarVehiculosEnSpinner() {
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("Vehiculos");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaVehiculos.clear();

                for (DataSnapshot data : snapshot.getChildren()) {
                    Vehiculo vehiculo = data.getValue(Vehiculo.class);
                    if (vehiculo != null) {
                        listaVehiculos.add(vehiculo);
                    }
                }

                if (listaVehiculos.isEmpty()) {
                    Toast.makeText(RegistrarServicio.this,
                            "No hay vehículos registrados", Toast.LENGTH_SHORT).show();
                }

                cargarAdapterVehiculos(listaVehiculos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RegistrarServicio.this,
                        "Error al cargar vehículos: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void cargarAdapterVehiculos(List<Vehiculo> vehiculos) {
        ArrayAdapter<Vehiculo> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                vehiculos
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spVehiculo.setAdapter(adapter);
    }



    // MÉTODO PARA CARGAR EMPLEADOS EN EL SPINNER
    private void cargarEmpleadosEnSpinner() {
        usuarioRepository.obtenerUsuariosPorRol("EMPLEADO", new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Usuario> listaEmpleados = new ArrayList<>();

                for (DataSnapshot data : snapshot.getChildren()) {
                    Usuario empleado = data.getValue(Usuario.class);
                    if (empleado != null) {
                        empleado.setUid(data.getKey()); // Asignar ID de Firebase
                        listaEmpleados.add(empleado);
                    }
                }

                if (listaEmpleados.isEmpty()) {
                    Toast.makeText(RegistrarServicio.this,
                            "No hay empleados registrados", Toast.LENGTH_SHORT).show();
                }

                cargarAdapterEmpleados(listaEmpleados);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RegistrarServicio.this,
                        "Error al cargar empleados: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // MÉTODO PARA CONFIGURAR EL ADAPTER DEL SPINNER
    private void cargarAdapterEmpleados(List<Usuario> listaEmpleados) {
        if (spEmpleado != null) {
            ArrayAdapter<Usuario> adapterEmpleados = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    listaEmpleados
            );
            adapterEmpleados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spEmpleado.setAdapter(adapterEmpleados);
        }
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

    private void mostrarTimePicker(EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        android.app.TimePickerDialog timePicker = new android.app.TimePickerDialog(
                RegistrarServicio.this,
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



    private void cargarCatalogoServicios() {
        catalogoServicioRepository.obtenerServicios(new RepositoryCallBack<List<CatalogoServicio>>() {
            @Override
            public void onSuccess(List<CatalogoServicio> servicios) {
                listaCatalogoServicios.clear();

                // Filtrar solo servicios ACTIVOS
                for (CatalogoServicio servicio : servicios) {
                    if (servicio.getEstado() == Estados.ACTIVO) {
                        listaCatalogoServicios.add(servicio);
                    }
                }

                if (listaCatalogoServicios.isEmpty()) {
                    Toast.makeText(RegistrarServicio.this,
                            "No hay servicios disponibles en el catálogo", Toast.LENGTH_SHORT).show();
                }

                cargarAdapterCatalogoServicios();
                configurarAutoPrecioDescripcion(); // Configurar después de cargar
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(RegistrarServicio.this,
                        "Error al cargar servicios: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // NUEVO MÉTODO: Configurar adapter del catálogo
    private void cargarAdapterCatalogoServicios() {
        ArrayAdapter<CatalogoServicio> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                listaCatalogoServicios
        ) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view;
                if (position >= 0 && position < listaCatalogoServicios.size()) {
                    textView.setText(listaCatalogoServicios.get(position).getNombre());
                }
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                if (position >= 0 && position < listaCatalogoServicios.size()) {
                    CatalogoServicio servicio = listaCatalogoServicios.get(position);
                    textView.setText(servicio.getNombre() + " - $" + servicio.getPrecio());
                }
                return view;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCatalogoServicio.setAdapter(adapter);
    }

    // MODIFICAR: Configurar auto precio y descripción
    private void configurarAutoPrecioDescripcion() {
        spCatalogoServicio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < listaCatalogoServicios.size()) {
                    CatalogoServicio servicioSeleccionado = listaCatalogoServicios.get(position);
                    precioserv.setText(String.valueOf(servicioSeleccionado.getPrecio()));
                    descripcionTxt.setText(servicioSeleccionado.getDescripcion());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                precioserv.setText("");
                descripcionTxt.setText("");
            }
        });
    }

    private void configurarBotonRegistrar() {
        btnRegistrar.setOnClickListener(v -> {
            // Validaciones básicas
            if (precioserv.getText().toString().isEmpty() ||
                    fechacalensrv.getText().toString().isEmpty() ||
                    horaInicio.getText().toString().isEmpty() ||
                    horaFin.getText().toString().isEmpty() ||
                    spEmpleado.getSelectedItem() == null ||
                    spVehiculo.getSelectedItem() == null) {

                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }


            // Obtener empleado seleccionado del spinner
            Usuario empleadoSeleccionado = (Usuario) spEmpleado.getSelectedItem();

            // Ya no necesitamos validar la cédula porque el spinner ya contiene empleados válidos
            // Generar el número de servicio directamente
            serviciorepo.generarNroServicio(new ServicioRepository.OnNroGenerado() {
                @Override
                public void onSuccess(int nro) {
                    crearServicioFinal(nro, empleadoSeleccionado);
                }

                @Override
                public void onError(String err) {
                    Toast.makeText(RegistrarServicio.this, err, Toast.LENGTH_LONG).show();
                }
            });
        });
    }


    // MODIFICAR el método crearServicioFinal MODIFICADO: Usar directamente el empleado del spinner
    private void crearServicioFinal(int nro_servicio, Usuario empleadoSeleccionado) {

        CatalogoServicio catalogoServicio =
                listaCatalogoServicios.get(spCatalogoServicio.getSelectedItemPosition());

        Vehiculo vehiculoSeleccionado = (Vehiculo) spVehiculo.getSelectedItem();
        String placa = vehiculoSeleccionado.getPlaca();

        ServicioCreateDTO dto = new ServicioCreateDTO(
                catalogoServicio,
                nro_servicio,
                fechaString(fechacalensrv),
                horaInicio.getText().toString(),
                horaFin.getText().toString(),
                indicacionesserv.getText().toString(),
                0,
                EstadoServicio.PENDIENTE,
                empleadoSeleccionado.getCedula(),
                empleadoSeleccionado.toString(),
                placa
        );

        serviciorepo.crearServicio(dto, new ServicioRepository.OnServiceResult() {
            @Override
            public void onSuccess(String msg) {
                Toast.makeText(RegistrarServicio.this, msg, Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(RegistrarServicio.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }


    private String fechaString(TextInputEditText txt) {
        return txt.getText() != null ? txt.getText().toString() : "";
    }
}