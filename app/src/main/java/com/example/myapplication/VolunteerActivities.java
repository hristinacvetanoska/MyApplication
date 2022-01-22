package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VolunteerActivities extends AppCompatActivity {
    private ArrayList<String> lines = new ArrayList<>();
    private static final int PERMISSIONS_FINE_LOCATION = 99;
    private ListView lista;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private String emailKorisnik, telefonKorisnik, tipNaUsluga, password;
    private double volonterLat, volonterLon, korisnikLat, korisnikLon, distance;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_activities);


        lista = findViewById(R.id.listaAktivni);


        FirebaseDatabase.getInstance("https://myapplication-6ee26-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Aktivnosti").orderByChild("Status").equalTo("Zakazana zadaca").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {

                    lines.add(postSnapshot.child("Title").getValue().toString());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        VolunteerActivities.this, android.R.layout.simple_list_item_1, lines);
                lista.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String[] niza = lines.get(i).split(" - ");
                String tipUsluga = niza[0];
                Intent intent = new Intent(VolunteerActivities.this, UserDescription.class);
                FirebaseDatabase.getInstance("https://myapplication-6ee26-default-rtdb.europe-west1.firebasedatabase.app/")
                        .getReference("Aktivnosti").orderByChild("Title").equalTo(tipUsluga).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                            emailKorisnik = postSnapshot.child("email").getValue().toString();
                            telefonKorisnik = postSnapshot.child("PhoneNumber").getValue().toString();
                            tipNaUsluga = postSnapshot.child("Title").getValue().toString();
                            password = postSnapshot.child("Password").getValue().toString();
                        }
                        intent.putExtra("title",tipNaUsluga);
                        intent.putExtra("email",emailKorisnik);
                        intent.putExtra("phoneNumber",telefonKorisnik);
                        intent.putExtra("password", password);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}