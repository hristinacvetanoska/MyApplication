package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class Volunteer extends AppCompatActivity{
    private ArrayList<String> lines = new ArrayList<>();
    private ListView lista;
    private static final int PERMISSIONS_FINE_LOCATION = 99;
    private Button logout, prijaveniZadaci;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private String celoIme, datum, title, rastojanie, rejting, lat, lon, email;
    private double volonterLat, volonterLon, korisnikLat, korisnikLon, distance;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);

        lista = findViewById(R.id.listaVolonter);
        logout = findViewById(R.id.logout);
        prijaveniZadaci = findViewById(R.id.prijaveniZadaci);

        prijaveniZadaci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Volunteer.this, VolunteerActivities.class));
            }
        });

        String email = getIntent().getStringExtra("email");
        FirebaseDatabase.getInstance("https://myapplication-6ee26-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Aktivnosti")
                .orderByChild("Status").equalTo("Aktivna").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                   lines.add(postSnapshot.child("Title").getValue().toString());
               }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        Volunteer.this, android.R.layout.simple_list_item_1, lines);
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
                Intent intent = new Intent(Volunteer.this,VolunteerDetails.class);
                FirebaseDatabase.getInstance("https://myapplication-6ee26-default-rtdb.europe-west1.firebasedatabase.app/")
                        .getReference("Aktivnosti").orderByChild("Title").equalTo(tipUsluga).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot postSnapshot: snapshot.getChildren()) {


                            celoIme = postSnapshot.child("fullName").getValue().toString();
                            datum = postSnapshot.child("Datum").getValue().toString();
                            title = postSnapshot.child("Title").getValue().toString();
                            rejting = postSnapshot.child("RatingNaKorisnik").getValue().toString();
                        }
                        intent.putExtra("Title",title);
                        intent.putExtra("ImeKorisnik",celoIme);
                        intent.putExtra("Datum",datum);
                        intent.putExtra("email", email);
                        intent.putExtra("RejtingVolonter",rejting);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(Volunteer.this,MainActivity.class));
            }
        });

    }


    }
