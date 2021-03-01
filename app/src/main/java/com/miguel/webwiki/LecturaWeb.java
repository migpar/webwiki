package com.miguel.webwiki;

import android.os.Handler;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class LecturaWeb implements Runnable {

    private String ciudad;
    private DevuelveDatos callback;
    private Handler handler;

    public LecturaWeb(String ciudad, DevuelveDatos callback, Handler handler) {
        this.ciudad = ciudad;
        this.callback = callback;
        this.handler = handler;
    }

    public LatLng obternerCoordenadas() {
        Document doc;
        LatLng coordenadas;
        try {
            Log.d("prueba", "empezamos el metodo");
            String url = "https://es.wikipedia.org/wiki/" + ciudad;
            Log.d("coordenadas", url);
            doc = Jsoup.connect(url).get();
            Elements latitud = doc.getElementsByClass("latitude");
            Elements longitud = doc.getElementsByClass("longitude");
            String[] lat = latitud.get(0).text().replace('°', ' ')
                    .replace('′', ' ').replace('″', ' ')
                    .split(" ");
            String[] longi = longitud.get(0).text().replace('°', ' ')
                    .replace('′', ' ').replace('″', ' ')
                    .split(" ");
            double lati = Double.parseDouble(lat[0]) + (Double.parseDouble(lat[1]) / 60)
                    + (Double.parseDouble(lat[2]) / 3600);
            double lon = Double.parseDouble(longi[0]) + (Double.parseDouble(longi[1]) / 60)
                    + (Double.parseDouble(longi[2]) / 3600);
            if (lat[3].equals("S")) {
                lati = -lati;
            }
            if (longi[3].equals("O")) {
                lon = -lon;
            }
            coordenadas = new LatLng(lati, lon);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            coordenadas = null;
        }

        return coordenadas;
    }

    @Override
    public void run() {
        LatLng coodenadas = obternerCoordenadas();
        Log.d("coordenadas", coodenadas.toString());
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.cargarMarcador(coodenadas);
            }
        });
    }


    public interface DevuelveDatos {
        void cargarMarcador(LatLng coodenadas);
    }

}


