package com.example.ukk_hed;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity {

    private ListView listView;
    private ImageView menu;
    private taskAdapter adapter;
    private List<task> taskList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static final String URL = "http://172.16.0.234/ukk_hed/listview_history.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        swipeRefreshLayout = findViewById(R.id.swiperefreshh);
        listView = findViewById(R.id.list);
        taskList = new ArrayList<>();

        adapter = new taskAdapter(this, taskList);
        listView.setAdapter(adapter);

        // Fetch data saat pertama kali dibuka
        fetchData();

        // Set listener saat di-swipe
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
            }
        });
    }

    private void fetchData() {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("API Response", response.toString()); // Debugging API response

                            if (response.getString("status").equals("success")) {
                                JSONArray dataArray = response.getJSONArray("data");
                                taskList.clear();

                                for (int i = 0; i < dataArray.length(); i++) {
                                    try {
                                        JSONObject taskObject = dataArray.getJSONObject(i);

                                        // Ambil data dari JSON
                                        String id = taskObject.getString("id");  // ID sebagai String
                                        String namaTugas = taskObject.getString("nama_tugas");
                                        String kategori = taskObject.getString("kategori");
                                        String tanggal = taskObject.getString("tanggal");
                                        String waktu = taskObject.getString("waktu");
                                        String deskripsi = taskObject.getString("deskripsi");
                                        String status = taskObject.getString("status");

                                        // Buat objek Task dan tambahkan ke list
                                        taskList.add(new task(id, namaTugas, kategori, tanggal, waktu, deskripsi, status));

                                    }catch (JSONException e) {
                                        e.printStackTrace();  // Log error jika ada masalah parsing JSON
                                    }
                                }

                                adapter.notifyDataSetChanged();
                            } else {
                                Log.e("API Response", "Gagal mendapatkan data tugas.");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("JSON Error", "Error parsing JSON: " + e.getMessage());
                        } finally {
                            swipeRefreshLayout.setRefreshing(false); // Hentikan animasi refresh
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Error fetching data: " + error.toString());
                        swipeRefreshLayout.setRefreshing(false); // Hentikan animasi refresh saat error
                    }
                });

        queue.add(request);
    }
}