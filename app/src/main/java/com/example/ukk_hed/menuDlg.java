package com.example.ukk_hed;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class menuDlg extends BottomSheetDialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.menu, null);
        dialog.setContentView(view);

        // Menyesuaikan ukuran dialog agar setengah layar dari kiri ke tengah
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(getScreenWidth() / 2, WindowManager.LayoutParams.MATCH_PARENT);
            window.setGravity(Gravity.START); // Muncul dari kiri
            window.setBackgroundDrawableResource(android.R.color.transparent); // Hindari background gelap
        }

        Button btnKat = view.findViewById(R.id.kategori);
        btnKat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), kategori.class);
                startActivity(intent);
            }
        });

        Button btnTgs = view.findViewById(R.id.tugas);
        btnTgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), TambahTugas.class);
                startActivity(intent);
            }
        });


        return dialog;
    }

    // Metode untuk mendapatkan lebar layar
    private int getScreenWidth() {
        return requireContext().getResources().getDisplayMetrics().widthPixels;
    }
}

