package com.example.ukk_hed;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.net.URL;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;

public class TambahTugas extends AppCompatActivity {

    private EditText edtDeskripsi, edtTask;
    private Spinner Kategori;
    private TextView date, waktu;
    private Button tambah, btnDate, btnTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private RequestQueue requestQueue;
    private static final String URL = "http://172.16.0.234/ukk_hed/tambah_tugas.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_tugas);

        // Inisialisasi View
        edtDeskripsi = findViewById(R.id.etTaskDescription);
        edtTask = findViewById(R.id.etTaskName);
        Kategori = findViewById(R.id.etTaskCategory);
        date = findViewById(R.id.etTaskDate);
        waktu = findViewById(R.id.etTaskTime);
        btnDate = findViewById(R.id.button_date);
        btnTime = findViewById(R.id.button_time);
        tambah = findViewById(R.id.btnSaveTask);

        requestQueue = Volley.newRequestQueue(this);

        tambah.setOnClickListener(v -> tambahTugas());
        btnDate.setOnClickListener(v -> showDatePicker());
        btnTime.setOnClickListener(v -> showTimePicker());

        new GetCategoriesTask().execute("http://172.16.0.234/ukk_hed/kategori.php");
    }

    private class GetCategoriesTask extends AsyncTask<String, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(String... urls) {
            ArrayList<String> categoryNames = new ArrayList<>();
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONArray jsonArray = new JSONArray(response.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject category = jsonArray.getJSONObject(i);
                    categoryNames.add(category.getString("category"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return categoryNames;
        }

        @Override
        protected void onPostExecute(ArrayList<String> categoryNames) {
            if (categoryNames.isEmpty()) {
                Toast.makeText(TambahTugas.this, "Tidak ada kategori", Toast.LENGTH_SHORT).show();
            } else {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(TambahTugas.this,
                        android.R.layout.simple_spinner_item, categoryNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Kategori.setAdapter(adapter);
            }
        }
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    mYear = year;
                    mMonth = month;
                    mDay = dayOfMonth;
                    updateDateText();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    mHour = hourOfDay;
                    mMinute = minute;
                    updateTimeText();
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true);
        timePickerDialog.show();
    }

    private void updateDateText() {
        if (mYear != 0 && mMonth != 0 && mDay != 0) {
            date.setText(String.format("%04d-%02d-%02d", mYear, mMonth + 1, mDay));
        }
    }

    private void updateTimeText() {
        if (mHour != 0 || mMinute != 0) {
            waktu.setText(String.format("%02d:%02d", mHour, mMinute));
        }
    }

    private void tambahTugas() {
        String nama = edtTask.getText().toString().trim();
        String kategori = Kategori.getSelectedItem().toString();
        String tanggal = date.getText().toString().trim();
        String jam = waktu.getText().toString().trim();
        String deskripsi = edtDeskripsi.getText().toString().trim();

        if (nama.isEmpty() || kategori.isEmpty() || tanggal.isEmpty() || jam.isEmpty() || deskripsi.isEmpty()) {
            Toast.makeText(this, "Semua field wajib diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Buat JSON Object
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("nama_tugas", nama);
            jsonObject.put("kategori", kategori);
            jsonObject.put("tanggal", tanggal);
            jsonObject.put("waktu", jam);
            jsonObject.put("deskripsi", deskripsi);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Kirim Request ke Server
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject,
                response -> {
                    try {
                        String status = response.getString("status");
                        String message = response.getString("message");

                        Toast.makeText(TambahTugas.this, message, Toast.LENGTH_SHORT).show();

                        if ("success".equals(status)) {
                            startActivity(new Intent(TambahTugas.this, MainActivity.class));
                            finish();
                        }
                    } catch (JSONException e) {
                        Log.e("VolleyError", "Error parsing response: " + e.getMessage());
                        Toast.makeText(TambahTugas.this, "Format respons tidak sesuai!", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    if (error.networkResponse != null) {
                        String errorResponse = new String(error.networkResponse.data);
                        Log.e("VolleyError", "Error Response: " + errorResponse);
                    } else {
                        Log.e("VolleyError", "Response kosong atau tidak dapat diurai!");
                    }
                    Toast.makeText(TambahTugas.this, "Terjadi kesalahan jaringan!", Toast.LENGTH_SHORT).show();
                });

        requestQueue.add(jsonObjectRequest);
    }
}
