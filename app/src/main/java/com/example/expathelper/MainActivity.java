package com.example.expathelper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    ListView lv; EditText etCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = findViewById(R.id.cities_list);
        etCity = findViewById(R.id.city);

    }

	//Добрый день, товарищи студенты!
    class CountTask extends AsyncTask<Integer, Integer, Void> {

        public double getTemperatureByCity(int cityID) {
            String API_KEY = ""; // укажите свой ключ для API
            String sampleURL = "https://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=b6907d289e10d714a6e88b30761fae22";
            try {
                URL url = new URL(sampleURL); // заменить на правильный адрес, как в API https://openweathermap.org/current
                InputStream stream = (InputStream) url.getContent();
                Gson gson = new Gson();
                Weather weather = gson.fromJson(new InputStreamReader(stream), Weather.class);
                // создать класс Weather, соответствующий структуре данных в JSON
                // в Weather должен быть внутренний класс
                return weather.temp; // указать нужное поле класса
            } catch (IOException e) {
                Log.d("mytag", e.getLocalizedMessage()); // выводим ошибку в журнал
                return Integer.MIN_VALUE;
            }

        }
        @Override
        protected Void doInBackground(Integer... cities) {
            for (int cityID: cities){
                Log.d("mytag", "temperature for " + cityID + " is "+ getTemperatureByCity(cityID));
                publishProgress(cityID);
            }
            return null;
        }
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // выполняется в основном потоке
            // сообщить, что вся информация получена
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //tvCount.setText("Got temp in cityID " + values[0]);
        }
    }
}
