package com.example.practicamoviles.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.practicamoviles.databinding.FragmentHomeBinding;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private static servicio API_SERVICE;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        /*Boton de consulta*/
        final Button BtnConsulta = binding.btnConsulta;
        BtnConsulta.setOnClickListener(new View.OnClickListener() {
            final  TextView txtConsulta = binding.Tvtxt;
            final  TextView txtLength = binding.TVcantidad;
            final HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

            @Override
            public void onClick(View v) {
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                // Asociamos el interceptor a las peticiones
                final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                httpClient.addInterceptor(logging);
                Retrofit retrofit=new Retrofit.Builder()
                        .baseUrl("https://catfact.ninja/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(httpClient.build())
                        .build();

                API_SERVICE=retrofit.create(servicio.class);
                Call<Elemento> call=API_SERVICE.getElemento();

                call.enqueue(new Callback<Elemento>() {
                    @Override
                    public void onResponse(Call<Elemento> call, Response<Elemento> response) {
                        try {
                            if(response.isSuccessful()){
                                Elemento elemento=response.body();
                                txtConsulta.setText(elemento.getFact());
                                txtLength.setText(elemento.getLength());
                            }

                        }catch (Exception ex){
                            Toast.makeText(getContext(), ""+ ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Elemento> call, Throwable t) {
                        Toast.makeText(getContext(), "Error de conexion", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}