package com.example.weatherapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    EditText etCity,etZipcode;
    TextView tvResult;
    private final String url = "http://api.openweathermap.org/data/2.5/weather";
    private final String appid = "cf18db6089239cdad00a1e4b9a83f946";
    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etCity = findViewById(R.id.etCity);
        etZipcode = findViewById(R.id.etZipcode);
        tvResult = findViewById(R.id.tvResult);
    }

    public void getWeatherDetails(View view) {
        String tempurl = "";
        String city = etCity.getText().toString().trim();
        String zipcode = etZipcode.getText().toString().trim();
        if(city.equals("")) {
            //tvResult.setText("City field can not be empty!");
            Toast.makeText(getApplicationContext(), "City field cannot be empty!", Toast.LENGTH_SHORT).show();
        }
        else {
            if(!zipcode.equals("")) {
                tempurl = url + "?q=" + city + "," + zipcode + "&appid=" + appid;
            }
            else {
                tempurl = url + "?q=" + city + "&appid=" + appid;
            }
            StringRequest request = new StringRequest(Request.Method.POST, tempurl, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                   // Log.d("response",response);
                    String output = "";
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                        String description = jsonObjectWeather.getString("description");
                        JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                        double temp = jsonObjectMain.getDouble("temp") - 273.15;
                        double feels_like = jsonObjectMain.getDouble("feels_like") - 273.15;
                        float pressure = jsonObjectMain.getInt("pressure");
                        int humidity = jsonObjectMain.getInt("humidity");
                        JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                        double windSpeed = jsonObjectWind.getDouble("speed");
                        JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                        String country = jsonObjectSys.getString("country");
                        output += "Current weather of " + city + "(" +country+ ")" + "\n" + description +
                                  "\nTemperature: " + df.format(temp) + "°C" + "\nFeels like: " + df.format(feels_like) + "°C" + "\nHumidity: " + humidity + "%" +
                                  "\nWind Speed: " + windSpeed + "m/s" + "\nPressure: " + pressure + "hPa";
                        tvResult.setText(output);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue requestqueue = Volley.newRequestQueue(getApplicationContext());
            requestqueue.add(request);
        }
    }
}