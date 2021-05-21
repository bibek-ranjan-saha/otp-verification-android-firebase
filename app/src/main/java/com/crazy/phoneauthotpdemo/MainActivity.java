package com.crazy.phoneauthotpdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    Button b1;
    Spinner lang;
    String[] languages;
    FirebaseAuth auth;
    String langu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getSharedPreferences("my_data",MODE_PRIVATE);

         langu = preferences.getString("language","english");

//        AppD details = new AppD();
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null)
        {
            startActivity(new Intent(MainActivity.this,profile.class));
            finish();
        }


        b1 = findViewById(R.id.button);
        b1.setEnabled(false);
        lang = findViewById(R.id.spinner);
        languages= getResources().getStringArray(R.array.Language);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lang.setAdapter(adapter);

        lang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                details.setLanguage(languages[position]);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("language",languages[position]);
                editor.apply();

                b1.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                b1.setEnabled(false);
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MainActivity.this,PhoneNum.class);
                        startActivity(i);
                    }
                }
        );
    }
}
