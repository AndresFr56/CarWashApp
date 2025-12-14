package com.grupo5.carwashapp.activities.servicio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
import androidx.appcompat.app.AlertDialog;
import com.grupo5.carwashapp.models.CatalogoServicio;
import com.grupo5.carwashapp.models.Servicio;
import com.grupo5.carwashapp.models.Usuario;
import com.grupo5.carwashapp.models.Vehiculo;
import com.grupo5.carwashapp.models.dtos.servicio.ServicioUpdateDTO;
import com.grupo5.carwashapp.models.enums.EstadoServicio;
import com.grupo5.carwashapp.models.enums.Estados;
import com.grupo5.carwashapp.models.enums.TipoLavado;
import com.grupo5.carwashapp.repository.CatalogoServicioRepository;
import com.grupo5.carwashapp.repository.ServicioRepository;
import com.grupo5.carwashapp.interfaces.RepositoryCallBack;
import com.grupo5.carwashapp.repository.UsuarioRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ConsultarServicio extends AppCompatActivity {
    private TextInputEditText   indicacionesserv;
    private Spinner spEstadoServ, spCatalogoServicio;
    private EditText horaInicio, horaFin;
    private Button  btnModificar, btnEliminar;
    private TextView descripcionTxt, fechacalensrv, precioserv;

    private ServicioRepository serviciorepo;
    private CatalogoServicioRepository catalogoServicioRepository; // Nuevo
    private String idRealServicio = null;
    private Spinner spEmpleadoConServ;
    private UsuarioRepository usuarioRepository;
    private List<Usuario> listaEmpleados;
    private String cedulaEmpleadoPendiente = null;
    private String placaVehiculoPendiente = null;

    private Spinner spVehiculoConServ;
    private List<Vehiculo> listaVehiculos = new ArrayList<>();



    private List<CatalogoServicio> listaCatalogoServicios; // Lista para almacenar servicios

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
        catalogoServicioRepository = new CatalogoServicioRepository(); // Inicializar
        listaCatalogoServicios = new ArrayList<>();
        usuarioRepository = new UsuarioRepository();
        listaEmpleados = new ArrayList<>();




        fechacalensrv = findViewById(R.id.con_fechaserv);
        precioserv = findViewById(R.id.cons_precioserv);
        horaInicio = findViewById(R.id.cons_horainix_serv);
        horaFin = findViewById(R.id.con_horafin_serv);
        indicacionesserv = findViewById(R.id.txt_con_indicaciones);
        descripcionTxt = findViewById(R.id.cons_descripserv);

        // CAMBIAR: spTipoLavado ahora es spCatalogoServicio
        spCatalogoServicio = findViewById(R.id.sp_tipolavado); // Mismo ID del layout
        spEstadoServ = findViewById(R.id.sp_estadoserv);
        spEmpleadoConServ = findViewById(R.id.sp_emrpleadocon_serv);
        spVehiculoConServ = findViewById(R.id.sp_vehiculocon_serv);



        btnModificar = findViewById(R.id.btn_serv_actualizar);
        btnEliminar = findViewById(R.id.btn_serv_eliminar);

        cargarSpinnerEstadoServicio();
        cargarCatalogoServicios(); // NUEVO: en lugar de cargarSpinnerTipoLavado()
        configurarTimePickers();
        cargarEmpleadosEnSpinner();
        cargarVehiculosEnSpinner();
        Servicio servicioRecibido =
                (Servicio) getIntent().getSerializableExtra("servicio");

        if (servicioRecibido != null) {
            cargarEnPantalla(servicioRecibido);


        }




        btnModificar.setOnClickListener(v -> {
            if (idRealServicio == null) {
                Toast.makeText(this, "Primero busque un servicio", Toast.LENGTH_SHORT).show();
                return;
            }

            // Obtener servicio seleccionado del catálogo
            int selectedPosition = spCatalogoServicio.getSelectedItemPosition();
            if (selectedPosition < 0 || selectedPosition >= listaCatalogoServicios.size()) {
                Toast.makeText(this, "Seleccione un servicio del catálogo", Toast.LENGTH_SHORT).show();
                return;
            }

            CatalogoServicio catalogoServicio = listaCatalogoServicios.get(selectedPosition);
            Usuario empleadoSeleccionado = (Usuario) spEmpleadoConServ.getSelectedItem();
            String cedulaEmp = empleadoSeleccionado.getCedula();

            EstadoServicio estadoEnum = EstadoServicio.values()[spEstadoServ.getSelectedItemPosition()];

            Vehiculo vehiculoSeleccionado =
                    (Vehiculo) spVehiculoConServ.getSelectedItem();

            if (vehiculoSeleccionado == null) {
                Toast.makeText(this, "Seleccione un vehículo", Toast.LENGTH_SHORT).show();
                return;
            }

            String placa = vehiculoSeleccionado.getPlaca();


            // Crear DTO con CatalogoServicio
            ServicioUpdateDTO dto = new ServicioUpdateDTO(
                    idRealServicio,
                    catalogoServicio, // Enviar CatalogoServicio en lugar de TipoLavado
                    fechacalensrv.getText().toString(),
                    horaInicio.getText().toString(),
                    horaFin.getText().toString(),
                    indicacionesserv.getText().toString(),
                    0,                        // estado lógico (0= activo normalmente)
                    estadoEnum,               // estadoServicio
                    placa,                    // placa del vehículo
                    cedulaEmp                 // cedulaEmpleado
            );

            serviciorepo.actualizarServicio(dto, new ServicioRepository.OnServiceResult() {
                @Override
                public void onSuccess(String msg) {
                    Toast.makeText(ConsultarServicio.this, msg, Toast.LENGTH_LONG).show();
                    volverAServicioMenu();
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(ConsultarServicio.this, error, Toast.LENGTH_LONG).show();
                }
            });
        });

        btnEliminar.setOnClickListener(v -> {
            if (idRealServicio == null) {
                Toast.makeText(this, "Primero busque un servicio", Toast.LENGTH_SHORT).show();
                return;
            }

            new AlertDialog.Builder(ConsultarServicio.this)
                    .setTitle("Confirmar eliminación")
                    .setMessage("¿Está seguro de que desea eliminar este servicio?")
                    .setCancelable(false)
                    .setPositiveButton("Sí, eliminar", (dialog, which) -> {

                        // 👉 SOLO AQUÍ se elimina
                        serviciorepo.eliminarLogico(idRealServicio, new ServicioRepository.OnServiceResult() {
                            @Override
                            public void onSuccess(String msg) {
                                Toast.makeText(ConsultarServicio.this, msg, Toast.LENGTH_LONG).show();
                                volverAServicioMenu();
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(ConsultarServicio.this, error, Toast.LENGTH_LONG).show();
                            }
                        });

                    })
                    .setNegativeButton("Cancelar", (dialog, which) -> {
                        dialog.dismiss(); // Cierra el diálogo sin hacer nada
                    })
                    .show();
        });

    }

    private void volverAServicioMenu() {
        Intent intent = new Intent(ConsultarServicio.this, servicio_menu.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
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
                        vehiculo.setUid(data.getKey());
                        listaVehiculos.add(vehiculo);
                    }
                }

                ArrayAdapter<Vehiculo> adapter = new ArrayAdapter<>(
                        ConsultarServicio.this,
                        android.R.layout.simple_spinner_item,
                        listaVehiculos
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spVehiculoConServ.setAdapter(adapter);


                seleccionarVehiculoPendiente();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ConsultarServicio.this,
                        "Error al cargar vehículos", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void seleccionarVehiculoPendiente() {
        if (placaVehiculoPendiente == null || listaVehiculos.isEmpty()) return;

        for (int i = 0; i < listaVehiculos.size(); i++) {
            if (listaVehiculos.get(i).getPlaca()
                    .equalsIgnoreCase(placaVehiculoPendiente)) {
                spVehiculoConServ.setSelection(i);
                break;
            }
        }
    }


    private void cargarEmpleadosEnSpinner() {
        usuarioRepository.obtenerUsuariosPorRol("EMPLEADO",
                new com.google.firebase.database.ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listaEmpleados.clear();

                        // 1️⃣ Llenar lista
                        for (DataSnapshot data : snapshot.getChildren()) {
                            Usuario empleado = data.getValue(Usuario.class);
                            if (empleado != null) {
                                empleado.setUid(data.getKey());
                                listaEmpleados.add(empleado);
                            }
                        }

                        // 2️⃣ Asignar adapter
                        ArrayAdapter<Usuario> adapter = new ArrayAdapter<>(
                                ConsultarServicio.this,
                                android.R.layout.simple_spinner_item,
                                listaEmpleados
                        );
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spEmpleadoConServ.setAdapter(adapter);

                        // 3️⃣ Seleccionar empleado asignado (AHORA SÍ)
                        if (cedulaEmpleadoPendiente != null) {
                            for (int i = 0; i < listaEmpleados.size(); i++) {
                                if (listaEmpleados.get(i).getCedula()
                                        .equals(cedulaEmpleadoPendiente)) {

                                    spEmpleadoConServ.setSelection(i);
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ConsultarServicio.this,
                                "Error al cargar empleados", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    // NUEVO: Método para cargar servicios del catálogo
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
                    Toast.makeText(ConsultarServicio.this,
                            "No hay servicios disponibles en el catálogo", Toast.LENGTH_SHORT).show();
                }

                cargarAdapterCatalogoServicios();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(ConsultarServicio.this,
                        "Error al cargar servicios: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // NUEVO: Configurar adapter del catálogo
    private void cargarAdapterCatalogoServicios() {
        ArrayAdapter<CatalogoServicio> adapter = new ArrayAdapter<CatalogoServicio>(
                this,
                android.R.layout.simple_spinner_item,
                listaCatalogoServicios
        ) {
            @Override
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view;
                if (position >= 0 && position < listaCatalogoServicios.size()) {
                    textView.setText(listaCatalogoServicios.get(position).getNombre());
                }
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, android.view.ViewGroup parent) {
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

        // Configurar listener para actualizar precio y descripción automáticamente
        spCatalogoServicio.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < listaCatalogoServicios.size()) {
                    CatalogoServicio servicioSeleccionado = listaCatalogoServicios.get(position);
                    precioserv.setText(String.valueOf(servicioSeleccionado.getPrecio()));
                    descripcionTxt.setText(servicioSeleccionado.getDescripcion());
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                precioserv.setText("");
                descripcionTxt.setText("");
            }
        });
    }

    private void cargarSpinnerEstadoServicio() {
        ArrayAdapter<EstadoServicio> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                EstadoServicio.values()
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEstadoServ.setAdapter(adapter);
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


        cedulaEmpleadoPendiente = s.getCedula_empleado();
        placaVehiculoPendiente = s.getPlaca();
        fechacalensrv.setText(s.getFecha());
        horaInicio.setText(s.getHora_inicio());
        horaFin.setText(s.getHora_fin());
        indicacionesserv.setText(s.getIndicaciones());
        precioserv.setText(String.valueOf(s.getCosto()));
        descripcionTxt.setText(s.getDescripcion_servicio());


        EstadoServicio estado = s.getEstadoServicio();
        if (estado != null) {
            spEstadoServ.setSelection(estado.ordinal());
        } else {
            spEstadoServ.setSelection(0); // PENDIENTE por defecto
        }
        if (!listaCatalogoServicios.isEmpty()) {
            if (s.getId_catalogo_servicio() != null) {
                for (int i = 0; i < listaCatalogoServicios.size(); i++) {
                    if (listaCatalogoServicios.get(i).getUid()
                            .equals(s.getId_catalogo_servicio())) {
                        spCatalogoServicio.setSelection(i);
                        break;
                    }
                }
            } else {
                for (int i = 0; i < listaCatalogoServicios.size(); i++) {
                    if (listaCatalogoServicios.get(i).getNombre()
                            .equals(s.getNombre_servicio())) {
                        spCatalogoServicio.setSelection(i);
                        break;
                    }
                }
            }
        }

        
        idRealServicio = s.getId_servicio();
    }

}