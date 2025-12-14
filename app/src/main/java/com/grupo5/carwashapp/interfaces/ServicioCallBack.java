package com.grupo5.carwashapp.interfaces;

import com.grupo5.carwashapp.models.Servicio;
import java.util.List;

public interface ServicioCallBack {
    void onSuccess();
    void onServiciosLoaded(List<Servicio> servicios);
    void onError(String error);
}
