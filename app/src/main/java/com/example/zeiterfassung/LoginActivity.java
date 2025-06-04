package com.example.zeiterfassung;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class LoginActivity extends AppCompatActivity {

    // Hier wird der Zurückbutton für diese Activity blockiert
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Icon oben rechts tippen um die App zu schließen", Toast.LENGTH_SHORT).show();
    }



    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private Button exitButton;

    private FirebaseAuth mAuth;
    private DatabaseReference userRef;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);  // <--- Name der Layout-XML

        // Goolge Firebase Realtime Database Verbindung zur NoSQL herstellen, Pfad "users"
        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("users");

        //Variablen zuweisen
        emailEditText = findViewById(R.id.mitarbeiterEmailInput);
        passwordEditText = findViewById(R.id.passwortInput);
        loginButton = findViewById(R.id.loginButton);
        exitButton = findViewById(R.id.ExitAppButton);

        // Nullwerte den Eingabefelder zuweisen
        emailEditText.setText("");
        passwordEditText.setText("");

        // Methode für login aufrufen
        loginButton.setOnClickListener(v -> loginUser());

        // Methode für das Beenden der App aufrufen
        exitButton.setOnClickListener(v -> exitApp());
    }

    // Methode für den Login
    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Bitte Emailadresse und Passwort eingeben.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    String uid = mAuth.getCurrentUser().getUid();

                    userRef.child(uid).child("role").get().addOnSuccessListener(snapshot -> {
                        String role = snapshot.getValue(String.class);

                        if ("mitarbeiter".equals(role)) {
                            Intent intent = new Intent(this, EmployeeActivity.class);
                            startActivity(intent);
                        } else if ("vorgesetzter".equals(role)) {
                            Intent intent = new Intent(this, SupervisorActivity.class);
                            startActivity(intent);
                        }
                        finish();  // Beendet die aktuelle Activity
                    });

                }).addOnFailureListener(e ->
                        Toast.makeText(this, "Login fehlgeschlagen!\nE-Mail oder Passwort falsch.", Toast.LENGTH_SHORT).show()
                );
    }

    // Methode für Beenden der App
    private void exitApp (){
        finishAndRemoveTask();
        //Diese Methode schließt alle Activities in der aktuellen Task und beendet die App und den Prozess.
    }
}
