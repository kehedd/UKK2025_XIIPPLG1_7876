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
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryAdapter extends ArrayAdapter<task> {// Ganti dengan URL API

    public HistoryAdapter(Context context, List<task> taskList) {
        super(context, 0, taskList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.history_item, parent, false);
        }

        // Ambil View dari layout
        TextView desc = convertView.findViewById(R.id.description);
        TextView kategori = convertView.findViewById(R.id.kategori);
        TextView tanggal = convertView.findViewById(R.id.tanggal);
        TextView waktu = convertView.findViewById(R.id.waktu);
        TextView deskripsi = convertView.findViewById(R.id.deskripsi);
        TextView status = convertView.findViewById(R.id.status);


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

            // Set onClickListener pada editIcon untuk pindah ke EditTaskActivity

            // Set onClickListener pada completeIcon untuk menyelesaikan tugas
        }

        return convertView;
    }

    // Function untuk menandai tugas sebagai 'Selesai'
}
