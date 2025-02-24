package com.example.ukk_hed;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class kategori extends AppCompatActivity {

    private ListView listView;
    private kategoriAdpt adapter;
    private List<ktgr> ktgrlist;
    private ImageView add;
    private SwipeRefreshLayout swipeRefresh;
    private static final String URL = "http://172.16.0.234/ukk_hed/kategori.php";
    private static final String DELETE_URL = "http://172.16.0.234/ukk_hed/hapusKat.php";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori);

        add = findViewById(R.id.buttonplus);
        listView = findViewById(R.id.list_ktgr);
        ktgrlist = new ArrayList<>();

        adapter = new kategoriAdpt(this,ktgrlist);
        listView.setAdapter(adapter);

        fetchData();

        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData(); // Memuat ulang data dari SharedPreferences
                swipeRefresh.setRefreshing(false); // Hentikan animasi refresh setelah selesai
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ktgrDlg editDialog = new ktgrDlg(kategori.this);
                editDialog.show(getSupportFragmentManager(), "Add Kategori");
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteConfirmation(position);
                return true;
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent untuk pindah ke SecondActivity
                Intent intent = new Intent(kategori.this, Dashboard.class);
                startActivity(intent);
            }
        });
    }
    private void showDeleteConfirmation(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Konfirmasi Hapus");
        builder.setMessage("Apakah Anda yakin ingin menghapus kategori ini?");
        builder.setPositiveButton("Hapus", (dialog, which) -> {
            deleteBookFromServer(ktgrlist.get(position).getNama_kategori());
        });
        builder.setNegativeButton("Batal", null);
        builder.show();
    }
    private void deleteBookFromServer(String title) {
        StringRequest request = new StringRequest(Request.Method.POST, DELETE_URL,
                response -> {
                    Toast.makeText(kategori.this, "kategori berhasil dihapus!", Toast.LENGTH_SHORT).show();
                    fetchBooks();
                },
                error -> Toast.makeText(kategori.this, "Gagal menghapus kategori!", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nama_kategori", title);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
    private void fetchBooks() {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("API Response", response.toString()); // Debugging API response
                        ktgrlist.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject bookObject = response.getJSONObject(i);
                                String title = bookObject.getString("category");
                                ktgrlist.add(new ktgr(title));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                    }
                });
        queue.add(request);
    }
    private void fetchData() {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("API Response", response.toString()); // Debugging API response
                        ktgrlist.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject bookObject = response.getJSONObject(i);
                                String kategori = bookObject.getString("category");
                                ktgrlist.add(new ktgr(kategori));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                    }
                });
        queue.add(request);
    }

}