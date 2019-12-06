package com.example.expathelper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

    class APITask extends AsyncTask<Integer[], Integer, Void> {

        public double getTemperatureByCity(int cityID) {
            String API_KEY = ""; // укажите свой ключ для API
            // указать правильный адрес, формат в документации https://openweathermap.org/current#cityid
            String sampleURL = "https://api.openweathermap.org/data/2.5/weather?q=London,uk&appid=b6907d289e10d714a6e88b30761fae22";
            try {
                URL url = new URL(sampleURL); // заменить на правильный адрес, как в API https://openweathermap.org/current
                InputStream stream = (InputStream) url.getContent();
                Gson gson = new Gson();
                Weather weather = gson.fromJson(new InputStreamReader(stream), Weather.class);
                // создать класс Weather, соответствующий структуре данных в JSON
                // в Weather должен быть внутренний класс
                return weather.main.temp; // указать нужное поле класса
            } catch (IOException e) {
                Log.d("mytag", e.getLocalizedMessage()); // выводим ошибку в журнал
                return Integer.MIN_VALUE; // если не получилось узнать погоду в городе
            }

        }
        @Override
        protected Void doInBackground(Integer[]... cities) {
            // для каждого
            for (Integer cityID: cities[0]){
                Log.d("mytag", "temperature for " + cityID + " is "+ getTemperatureByCity(cityID));
                publishProgress(cityID); // вызвать метод onProgressUpdate
            }
            return null;
        }
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // выполняется в основном потоке
            // сообщить, что вся информация получена

            // основная задача: собрать данные о температуре в массив строк и отобразить
            // его на ListView через ArrayAdapter
            // города упорядочить по убыванию температуры
            // температуру указать в градусах Цельсия
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            // отобразить прогресс выполнения задания
            // например, какое число городов уже обработано
            Log.d("mytag", "Got temp in cityID " + values[0]); // сообщаем в журнал, какой город обработан
        }
    }

    public void onClick(View v) {
        Integer[] cities = {123, 234}; // номера городов нужно найти на сайте OpenWeatherMap
        // внести их в файл strings.xml (как массив строк) и считать их
        APITask task = new APITask();
        // запустить получение погоды в списке городов из массива
        task.execute(cities);



    }
}
