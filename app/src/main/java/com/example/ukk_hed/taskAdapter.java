package com.example.ukk_hed;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable; // Tambahkan ini!

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class taskAdapter extends ArrayAdapter<task> {

    private static final String COMPLETE_TASK_URL = "http://172.16.0.234/ukk_hed/completeTask.php"; // Ganti dengan URL API

    public taskAdapter(Context context, List<task> taskList) {
        super(context, 0, taskList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_item, parent, false);
        }

        // Ambil View dari layout
        TextView desc = convertView.findViewById(R.id.description);
        TextView kategori = convertView.findViewById(R.id.kategori);
        TextView tanggal = convertView.findViewById(R.id.tanggal);
        TextView waktu = convertView.findViewById(R.id.waktu);
        TextView deskripsi = convertView.findViewById(R.id.deskripsi);
        TextView status = convertView.findViewById(R.id.status);
        ImageView edit = convertView.findViewById(R.id.editIcon);
        ImageView complete = convertView.findViewById(R.id.imgComplete); // Tambahkan ikon complete

        // Ambil objek task pada posisi tertentu
        task currentTask = getItem(position);
        if (currentTask != null) {
            desc.setText(currentTask.getNamaTugas());
            kategori.setText("Kategori: " + currentTask.getKategori());
            tanggal.setText("Tanggal: " + currentTask.getTanggal());
            waktu.setText("Waktu: " + currentTask.getWaktu());
            deskripsi.setText("Deskripsi: " + currentTask.getDeskripsi());
            status.setText("Status: " + currentTask.getStatus());

            // Jika tugas sudah selesai, disable tombol complete
            if ("Selesai".equalsIgnoreCase(currentTask.getStatus())) {
                complete.setImageResource(R.drawable.ic_checked); // Ikon selesai
                complete.setEnabled(false);
            } else {
                complete.setImageResource(R.drawable.ic_check); // Ikon belum selesai
                complete.setEnabled(true);
            }

            // Set onClickListener pada editIcon untuk pindah ke EditTaskActivity
            edit.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), editTask.class);
                intent.putExtra("nama_tugas", currentTask.getNamaTugas());
                intent.putExtra("kategori", currentTask.getKategori());
                intent.putExtra("tanggal", currentTask.getTanggal());
                intent.putExtra("waktu", currentTask.getWaktu());
                intent.putExtra("deskripsi", currentTask.getDeskripsi());
                intent.putExtra("status", currentTask.getStatus());
                intent.putExtra("id", currentTask.getId());
                getContext().startActivity(intent);
            });

            // Set onClickListener pada completeIcon untuk menyelesaikan tugas
            complete.setOnClickListener(v -> markTaskComplete(currentTask, status, complete));
        }

        return convertView;
    }

    // Function untuk menandai tugas sebagai 'Selesai'
    private void markTaskComplete(task currentTask, TextView statusTextView, ImageView completeIcon) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        StringRequest postRequest = new StringRequest(
                Request.Method.POST,
                COMPLETE_TASK_URL,
                response -> {
                    Log.d("taskAdapter", "Response: " + response);
                    if (response.trim().equals("Task successfully marked as 'Selesai'")) {
                        currentTask.setStatus("Selesai");
                        statusTextView.setText("Status: Selesai");
                        completeIcon.setImageResource(R.drawable.ic_checked); // Ganti ikon
                        completeIcon.setEnabled(false); // Disable klik setelah selesai
                    } else {
                        Toast.makeText(getContext(), "Gagal menyelesaikan tugas!", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("taskAdapter", "Volley Error: " + error.getMessage());
                    Toast.makeText(getContext(), "Terjadi kesalahan!", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nama_tugas", currentTask.getNamaTugas()); // Kirim data ke PHP
                return params;
            }
        };

        requestQueue.add(postRequest);
    }
}
