package com.example.practicamoviles.ui.home;

import retrofit2.Call;
import retrofit2.http.GET;

public interface servicio {
    String API_ROUTE="/fact";

    @GET(API_ROUTE)
    Call<Elemento> getElemento();
}
