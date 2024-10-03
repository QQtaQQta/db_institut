package com.example.schedule;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        showGroups();
        showRooms();
    }

    public void showGroups() {
        TextView view = findViewById(R.id.content);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("http://217.71.129.139:4858/groups").build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String json = response.body().string();
                    JSONArray array = new JSONArray(json);
                    ArrayList<String> groups = new ArrayList<String>();
                    Spinner spinner = findViewById(R.id.spinner);
                    groups.add("Группы");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String text = object.getString("name");
                        groups.add(text);

                    }

                    // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемента spinner
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, groups);
                    // Определяем разметку для использования при выборе элемента
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Применяем адаптер к элементу spinner
                    MainActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            spinner.setAdapter(adapter);
                            spinner.setSelection(0);
                        }
                    });
                } catch (JSONException e) {
                    view.post(new Runnable() {
                        public void run() {
                            view.append(e.getMessage());
                        }
                    });
                }
            }

            public void onFailure(Call call, IOException e) {
                view.post(new Runnable() {
                    public void run() {
                        view.append(e.getMessage());
                    }
                });
            }
        });
    }
    public void showRooms() {
        TextView view = findViewById(R.id.content);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("http://217.71.129.139:4858/rooms").build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String json = response.body().string();
                    JSONArray array = new JSONArray(json);
                    ArrayList<String> groups = new ArrayList<String>();
                    Spinner spinner = findViewById(R.id.spinner2);
                    groups.add("Кабинет");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String text = object.getString("name");
                        groups.add(text);
                    }

                    // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемента spinner
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, groups);
                    // Определяем разметку для использования при выборе элемента
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Применяем адаптер к элементу spinner
                    MainActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            spinner.setAdapter(adapter);
                            spinner.setSelection(0);
                        }
                    });
                } catch (JSONException e) {
                    view.post(new Runnable() {
                        public void run() {
                            view.append(e.getMessage());
                        }
                    });
                }
            }

            public void onFailure(Call call, IOException e) {
                view.post(new Runnable() {
                    public void run() {
                        view.append(e.getMessage());
                    }
                });
            }
        });
    }
    private boolean isScheduleVisible = false; // Для отслеживания состояния расписания

    public void showSchedule(View view) {
        TextView scheduleView = findViewById(R.id.text_schedule);
        TextView contentView = findViewById(R.id.content);

        // Меняем состояние
        isScheduleVisible = !isScheduleVisible;

        if (isScheduleVisible) {
            contentView.setVisibility(View.GONE); // Скрываем другой текст
            scheduleView.setVisibility(View.VISIBLE); // Показываем расписание

            // Вызов метода для загрузки расписания




        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("http://217.71.129.139:4858/schedule").build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String json = response.body().string();

                // Передаем данные в UI-поток для отображения
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Парсим JSON и отображаем расписание
                            JSONArray array = new JSONArray(json);
                            StringBuilder scheduleText = new StringBuilder();

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject scheduleItem = array.getJSONObject(i);
                                String date = scheduleItem.getString("discipline");
                                String event = scheduleItem.getString("time");
                                String day = scheduleItem.getString("day");

                                // Формируем текст для отображения
                                scheduleText.append("день: ").append(day).append("\n");
                                scheduleText.append("дисциплина: ").append(date).append("\n");
                                scheduleText.append("время: ").append(event).append("\n\n");
                            }

                            // Отображаем расписание в TextView
                            scheduleView.setText(scheduleText.toString());

                        } catch (JSONException e) {
                            scheduleView.setText("Ошибка при парсинге данных");
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        scheduleView.setText("Ошибка при загрузке данных");
                    }
                });
            }
        });
        } else {
            scheduleView.setVisibility(View.GONE); // Скрываем расписание
            contentView.setVisibility(View.VISIBLE); // Показываем другой текст
        }
    }


    }
