package fr.iut_amiens.weatherapplication;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.iut_amiens.weatherapplication.openweathermap.WeatherManager;
import fr.iut_amiens.weatherapplication.openweathermap.WeatherResponse;

/***
 * Created by omer on 16/03/18.
 */
public class GetWeatherResponseTask extends AsyncTask <Object ,WeatherResponse, String> {
    private List<GetWeatherResponseListener> listeners;
    private String city;
    private Double latitude;
    private Double longitude;

    /***
     * Constructeur par ville
     * @param city
     */
    public GetWeatherResponseTask(String city){
        this.city = city;
        listeners = new ArrayList<>();
        this.latitude = null;
        this.longitude = null;
        Log.d("Task", "Constructeur city");
    }

    /***
     * Constructeur par coordonnée
     * @param latitude latitude
     * @param longitude longitude
     */
    public GetWeatherResponseTask(Double latitude, Double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = null;
        listeners = new ArrayList<>();
        Log.d("Task", "Constructeur lat, long");
    }

    /***
     * Ajoute un abonné
     * @param listener
     */
    public void addListener(GetWeatherResponseListener listener){
        listeners.add(listener);
    }

    /***
     * Supprimer un abonné
     * @param listener
     */
    public void removeListener(GetWeatherResponseListener listener){
        listeners.remove(listener);
    }

    /***
     * Envoie la notification au abonnés
     * @param weather
     */
    public void listenerNotify(WeatherResponse weather){
        for (GetWeatherResponseListener listener : listeners){
            listener.getWeather(weather);
        }
    }

    @Override
    protected String doInBackground(Object[] objects) {
        WeatherManager weatherManager = new WeatherManager();
        WeatherResponse weather = null;
        try {
            if(city != null) {
                weather = weatherManager.findWeatherByCityName(city);
            }else{
                weather = weatherManager.findWeatherByGeographicCoordinates(latitude, longitude);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        publishProgress(weather);


        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("Task", "onPreExecute");
    }

    @Override
    protected void onPostExecute(String str) {
        super.onPostExecute(str);
        Log.d("Task", "onPostExecute");
        onCancelled();
    }

    @Override
    protected void onProgressUpdate(WeatherResponse... values) {
        super.onProgressUpdate(values);
        Log.d("Task", "Task: Progress --> " + values[0]);
        listenerNotify(values[0]);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        Log.d("Task", "Task: Cancelled");
    }
}