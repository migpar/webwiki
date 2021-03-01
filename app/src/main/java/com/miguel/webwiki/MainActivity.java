package com.miguel.webwiki;

import androidx.core.os.HandlerCompat;
import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, LecturaWeb.DevuelveDatos {

    private TextInputLayout ciudad;
    private Button boton;
    public static LatLng coordenadas;
    private static GoogleMap mMap;
    private static String nombre;
    private LecturaWeb.DevuelveDatos contexto = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ciudad = findViewById(R.id.text_ciudad);
        boton = findViewById(R.id.button);

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombre = ciudad.getEditText().getText().toString();
                Handler mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());

                LecturaWeb hilo = new LecturaWeb(nombre, contexto, mainThreadHandler);
                Thread t = new Thread(hilo);
                t.start();
            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("onmap", "llegamos");
    }


    @Override
    public void cargarMarcador(LatLng coodenadas) {
        // Add a marker in Sydney and move the camera
        mMap.addMarker(new MarkerOptions().position(coodenadas).title("Marker in " + nombre));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(coodenadas));
    }
}