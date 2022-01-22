package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import javax.annotation.Nullable;

public class OlderUser extends AppCompatActivity {

    EditText deadline, description1, titleActivity;
    int year, month, day;
    TextView username;
    Button locationPicker;
    TextView textView;
    int Place_Picker_Request = 1;
    FirebaseUser user;
    private String userID;
    private Task<Void> reference;
    private FirebaseAuth mAuth;
    private Button save, listaAktivnosti;
    private String fullname, rating, phoneNumber, dateTime;
    //DatePickerDialog.OnDateSetListener setListener;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_older_user);
        mAuth = FirebaseAuth.getInstance();

        titleActivity = findViewById(R.id.activity1);

        deadline = findViewById(R.id.deadline);
        username = findViewById(R.id.userName);

        String name = getIntent().getStringExtra("fullName");
        username.setText(name);

        String password = getIntent().getStringExtra("password");
        String email = getIntent().getStringExtra("email");

        locationPicker = findViewById(R.id.locationButton);
        textView = findViewById(R.id.location);

        description1 = findViewById(R.id.descriptionActivityOlder);

        save = findViewById(R.id.addActivity);

        listaAktivnosti = findViewById(R.id.activitiesButton);
        Spinner spin = findViewById(R.id.frequencyOfActivity);

        Spinner spin1 = findViewById(R.id.urgencyOfActivity);

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        String urgencyOfActivity = spin1.getSelectedItem().toString();
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        String frequencyofActivity = spin.getSelectedItem().toString();
        HashMap<String, Object> map = new HashMap<>();
        map.put("Password", password);
        map.put("email",email);

        if(frequencyofActivity.equals("Ендократно")){
            map.put("Повторливост", "Ендократно");
        }
        else {
            map.put("Повторливост", "Да се повторува неделно");
        }
        if(urgencyOfActivity.equals("Да")){
            map.put("Итност","Да");
        }
        else {
            map.put("Итност","Не");
        }
        final Calendar calendar = Calendar.getInstance();
        deadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(OlderUser.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        deadline.setText(SimpleDateFormat.getInstance().format(calendar.getTime()));
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        locationPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(OlderUser.this), Place_Picker_Request);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance("https://myapplication-6ee26-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users")
                        .orderByChild("password").equalTo(password).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            fullname = dataSnapshot.child("fullName").getValue().toString();
                            rating = dataSnapshot.child("rating").getValue().toString();
                            phoneNumber = dataSnapshot.child("phoneNumber").getValue().toString();
                        }
                        map.put("Title", titleActivity.getText().toString().trim());
                        map.put("Opis", description1.getText().toString().trim());
                        map.put("Datum", deadline.getText().toString().trim());
                        map.put("fullName", fullname);
                        map.put("RatingNaKorisnik",rating);
                        map.put("EmailVolonter","");
                        map.put("ImeVolonter","");
                        map.put("Status","Aktivna");
                        map.put("PhoneNumber", phoneNumber);
                        map.put("PhoneNumberVolonter","");
                        map.put("OpisZaKorisnik","");
                        map.put("OpisZaVolonter","");
                        map.put("RejtingZaKorisnik",0);
                        map.put("RejtingNaVolonter",0);
                        map.put("RejtingZaVolonter",0);

                        FirebaseDatabase.getInstance("https://myapplication-6ee26-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("Aktivnosti").push().updateChildren(map);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        listaAktivnosti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OlderUser.this, ListActivities.class);

                intent.putExtra("password", password);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Place_Picker_Request) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                StringBuilder stringBuilder = new StringBuilder();
                String latitude = String.valueOf(place.getLatLng().latitude);
                String longitude = String.valueOf(place.getLatLng().longitude);
                stringBuilder.append("LATITUDE: ");
                stringBuilder.append(latitude);
                stringBuilder.append("\n");
                stringBuilder.append("LONGITUDE");
                stringBuilder.append(longitude);
                textView.setText(stringBuilder.toString());
            }
        }
    }
}