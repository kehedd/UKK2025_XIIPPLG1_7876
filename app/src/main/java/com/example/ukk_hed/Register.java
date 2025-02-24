package com.example.ukk_hed;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private EditText etNama, etEmail, etPassword;
    private Button registerasi;
    private RequestQueue requestQueue;

    private  String Url = "http://172.16.0.234/ukk_hed/register.php";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etNama = findViewById(R.id.etNamaLengkap);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        registerasi = findViewById(R.id.btnRegister);


        registerasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }
    private void registerUser() {
        String password = etPassword.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String name = etNama.getText().toString().trim();

        if (name.isEmpty()) {
            etNama.setError("Nama tidak boleh kosong!");
            etNama.requestFocus();
            return;
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Masukkan email yang valid!");
            etEmail.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Password minimal 6 karakter!");
            etPassword.requestFocus();
            return;
        }

        // Simpan URL di variabel agar mudah dikelola
        String url = "http://172.16.0.234/ukk_hed/register.php";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("nama_lengkap", name);
            jsonObject.put("email", email);
            jsonObject.put("password", password); // Password tetap dikirim dalam teks biasa (server harus hash)
        } catch (JSONException e) {
            Toast.makeText(this, "Terjadi kesalahan saat memproses data!", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                response -> {
                    try {
                        String status = response.getString("status");
                        String message = response.getString("message");

                        Toast.makeText(Register.this, message, Toast.LENGTH_SHORT).show();

                        if (status.equals("success")) {
                            startActivity(new Intent(Register.this, MainActivity.class));
                            finish();
                        }
                    } catch (JSONException e) {
                        Log.e("VolleyError", "Error parsing response: " + e.getMessage());
                        Toast.makeText(Register.this, "Format respons tidak sesuai!", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    if (error instanceof TimeoutError) {
                        Log.e("Volley", "Timeout terjadi, coba periksa koneksi atau perpanjang waktu request.");
                    } else {
                        Log.e("Volley", "Error lain terjadi: " + error.toString());
                    }
                    Toast.makeText(Register.this, "Terjadi kesalahan jaringan!", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        requestQueue = Volley.newRequestQueue(this);

        // Atur timeout ke 20 detik
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000, // 20 detik
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObjectRequest);
    }

}