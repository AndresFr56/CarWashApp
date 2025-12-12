package com.grupo5.carwashapp.interfaces;

import com.grupo5.carwashapp.models.Factura;

import java.util.List;

public interface FacturaCallBack {
    void onSuccess();

    void onFacturasLoaded(List<Factura> facturas);

    void onError(String error);
}
