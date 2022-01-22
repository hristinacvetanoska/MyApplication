package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    TextView titleRegister, registerUser;
    //private FirebaseApp firebaseApp;
    private FirebaseAuth mAuth;
    private EditText editTextFullName, editTextPhoneNumber, editTextEmail, editTextPassword;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        titleRegister =  findViewById(R.id.titleRegister);
        titleRegister.setOnClickListener(this);

        registerUser =  findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);

        editTextFullName =  findViewById(R.id.fullName);
        editTextPhoneNumber =  findViewById(R.id.phoneNumber);
        editTextEmail =  findViewById(R.id.emailReg);
        editTextPassword =  findViewById(R.id.passwordReg);

        progressBar =  findViewById(R.id.progressBar);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titleRegister:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.registerUser:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String fullName = editTextFullName.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();
        Spinner spin =  findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        String role = spin.getSelectedItem().toString();
        if (fullName.isEmpty()) {
            editTextFullName.setError("Мора да внесите име и презиме");
            editTextFullName.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            editTextEmail.setError("Мора да внесете email");
            editTextEmail.requestFocus();
            return;
        }

        if (phoneNumber.isEmpty()) {
            editTextPhoneNumber.setError("Мора да внесете телефонски број");
            editTextPhoneNumber.requestFocus();
            return;
        }
        if (password.length() < 6) {
            editTextPassword.setError("Пасвордот мора да има минимум 6 карактери");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if(task.isSuccessful()){
                        User user = new User(fullName, email, phoneNumber, role, password, 0, 0, 0);

                        FirebaseDatabase.getInstance("https://myapplication-6ee26-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users")
                                .//child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                child(password)
                                .setValue(user).addOnCompleteListener(task1 -> {

                            if(task1.isSuccessful()){
                                Toast.makeText(RegisterActivity.this,"Корисникот успешно се регистрира!", Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(RegisterActivity.this, "Корисникот не се регистрираше! Пробајте повторно!", Toast.LENGTH_LONG).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        });
                    }
                    else {
                        Toast.makeText(RegisterActivity.this, "Корисникот не се регистрираше!", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
}