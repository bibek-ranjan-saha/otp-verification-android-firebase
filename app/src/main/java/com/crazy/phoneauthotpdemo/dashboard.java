package com.crazy.phoneauthotpdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

public class dashboard extends AppCompatActivity {
    RadioButton b1,b2;
    Button button;
    FirebaseFirestore db;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        SharedPreferences preferences = getSharedPreferences("my_data",MODE_PRIVATE);

        type = preferences.getString("type","not yet selected");

        b1 = findViewById(R.id.shipbtn);
        b2 = findViewById(R.id.transbtn);
        button = findViewById(R.id.button);
        db = FirebaseFirestore.getInstance();
        AppD appD = new AppD();

        b1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (b2.isChecked())
                {
                    b2.setChecked(false);
                }
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("type","shipper");
                editor.apply();
//                appD.setType("shipper");
            }
        });
        b2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (b1.isChecked())
                {
                    b1.setChecked(false);
                }
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("type","transporter");
                editor.apply();
//                appD.setType("Transporter");
            }
        });

        button.setOnClickListener(v -> {
            if (b1.isChecked() || b2.isChecked()){
                db.collection("User")
                        .document().set(appD).addOnCompleteListener(task -> startActivity(new Intent(getApplicationContext(),profile.class)));
            }
        });

    }
}