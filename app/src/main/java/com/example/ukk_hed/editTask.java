package com.example.ukk_hed;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class editTask extends AppCompatActivity {

    private EditText edtDeskripsi, edtTask;
    private Spinner Kategori;
    private TextView date, waktu;
    private Button edit, btnDate, btnTime;
    private String namaTugasLama;
    private RequestQueue requestQueue;
    private static final String URL_EDIT = "http://172.16.0.234/ukk_hed/editTask.php";
    private static final String URL_KATEGORI = "http://172.16.0.234/ukk_hed/kategori.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        edtDeskripsi = findViewById(R.id.etTaskDescription);
        edtTask = findViewById(R.id.etTaskName);
        Kategori = findViewById(R.id.etTaskCategory);
        date = findViewById(R.id.etTaskDate);
        waktu = findViewById(R.id.etTaskTime);
        btnDate = findViewById(R.id.button_date);
        btnTime = findViewById(R.id.button_time);
        edit = findViewById(R.id.btnSaveTask);

        requestQueue = Volley.newRequestQueue(this);

        // Ambil data dari Intent
        Intent intent = getIntent();
        if (intent != null) {
            namaTugasLama = intent.getStringExtra("nama_tugas");
            edtTask.setText(intent.getStringExtra("nama_tugas"));
            date.setText(intent.getStringExtra("tanggal"));
            waktu.setText(intent.getStringExtra("waktu"));
            edtDeskripsi.setText(intent.getStringExtra("deskripsi"));
        }

        // Load kategori ke Spinner
        loadKategori();

        edit.setOnClickListener(v -> editTugas());
        btnDate.setOnClickListener(v -> showDatePicker());
        btnTime.setOnClickListener(v -> showTimePicker());
    }

    private void loadKategori() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_KATEGORI,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        String[] categories = new String[jsonArray.length()];
                        for (int i = 0; i < jsonArray.length(); i++) {
                            categories[i] = jsonArray.getJSONObject(i).getString("category");
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        Kategori.setAdapter(adapter);

                        // Pilih kategori sesuai data yang lama
                        String kategoriLama = getIntent().getStringExtra("kategori");
                        for (int i = 0; i < categories.length; i++) {
                            if (categories[i].equals(kategoriLama)) {
                                Kategori.setSelection(i);
                                break;
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Gagal memuat kategori", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Gagal mengambil data kategori", Toast.LENGTH_SHORT).show()
        );

        requestQueue.add(stringRequest);
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) ->
                date.setText(String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)),
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(this, (view, hourOfDay, minute) ->
                waktu.setText(String.format("%02d:%02d", hourOfDay, minute)),
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }

    private void editTugas() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT,
                response -> {
                    Log.d("ServerResponse", "Response: " + response);
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String status = jsonResponse.getString("status");
                        String message = jsonResponse.getString("message");

                        Toast.makeText(editTask.this, message, Toast.LENGTH_SHORT).show();
                        if (status.equals("success")) {
                            Intent intent = new Intent(editTask.this, Dashboard.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish(); // Tutup activity jika sukses
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(editTask.this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(editTask.this, "Gagal menghubungi server", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nama_tugas_lama", namaTugasLama);
                params.put("nama_tugas", edtTask.getText().toString());
                params.put("kategori", Kategori.getSelectedItem().toString());
                params.put("tanggal", date.getText().toString());
                params.put("waktu", waktu.getText().toString());
                params.put("deskripsi", edtDeskripsi.getText().toString());
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}
