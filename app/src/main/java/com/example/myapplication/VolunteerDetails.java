package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class VolunteerDetails extends AppCompatActivity {
     private TextView imeKorisnikDesno, datumDesno, tipNaUslugaDesno, rastojanieDesno, rejtingDesno;
     private Button prifatiVolonter;
     private String imeVolonter, rejtingVolonter, telefonVolonter, emailVolonter;
     private FirebaseAuth auth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_details);


        prifatiVolonter = findViewById(R.id.prifatiVolonter);
        imeKorisnikDesno = findViewById(R.id.imeKorisnikDesno);
        datumDesno = findViewById(R.id.datumVolonterDesno);
        tipNaUslugaDesno = findViewById(R.id.tipUslugaVolonterDesno);
        rastojanieDesno = findViewById(R.id.rastojanieDesno);
        rejtingDesno = findViewById(R.id.rejtingVolonterDesno);

        Intent intent = getIntent();
        String celoIme = intent.getStringExtra("ImeKorisnik");
        String datum = intent.getStringExtra("Datum");
        String tipNaUsluga = intent.getStringExtra("Title");
        String rejting = intent.getStringExtra("RejtingVolonter");

        imeKorisnikDesno.setText(celoIme);
        datumDesno.setText(datum);
        tipNaUslugaDesno.setText(tipNaUsluga);
        rejtingDesno.setText(rejting);

        HashMap<String, Object> map = new HashMap<>();
        prifatiVolonter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prifatiVolonter.setVisibility(View.GONE);
                emailVolonter = getIntent().getStringExtra("email");
                FirebaseDatabase.getInstance("https://myapplication-6ee26-default-rtdb.europe-west1.firebasedatabase.app/")
                        .getReference("Users").orderByChild("email").equalTo(emailVolonter).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot datas : snapshot.getChildren()) {
                            imeVolonter = datas.child("fullName").getValue().toString();
                            rejtingVolonter = datas.child("rating").getValue().toString();
                            telefonVolonter = datas.child("phoneNumber").getValue().toString();
                        }
                        map.put("EmailVolonter", emailVolonter);
                        map.put("ImeVolonter", imeVolonter);
                        map.put("PhoneNumberVolonter:", telefonVolonter);
                        map.put("RejtingNaVolonter", rejtingVolonter);
                        map.put("Status", "Prijaven volonter");
                        FirebaseDatabase.getInstance("https://myapplication-6ee26-default-rtdb.europe-west1.firebasedatabase.app/")
                                .getReference("Aktivnosti").orderByChild("Title").equalTo(tipNaUsluga).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                    FirebaseDatabase.getInstance("https://myapplication-6ee26-default-rtdb.europe-west1.firebasedatabase.app/").getReference()
                                            .child("Aktivnosti").child(postSnapshot.getKey()).updateChildren(map);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                }
            });
        }
}