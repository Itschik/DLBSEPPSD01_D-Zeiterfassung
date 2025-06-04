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

/**
 * LoginActivity – Ermöglicht das Einloggen von Benutzern (Mitarbeiter & Vorgesetzte)
 * Authentifiziert über Firebase Authentication und bestimmt die Rolle über Firebase Realtime Database.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Blockiert den Android-Zurück-Button.
     * Benutzer sollen zum Beenden den Schließen Button verwenden.
     */
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Icon oben rechts tippen um die App zu schließen", Toast.LENGTH_SHORT).show();
    }

    // UI-Komponenten
    private EditText emailEditText, passwordEditText;

    // Firebase
    private FirebaseAuth mAuth;                   // Für Authentifizierung
    private DatabaseReference userRef;            // Für Datenbankzugriff auf Nutzerrollen


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);  // Verknüpft XML-Layout mit Activity

        // Initialisiere Firebase Authentication und verweise auf den "users"-Zweig in der Realtime Database
        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("users");

        // Zuweisung der UI-Elemente
        emailEditText = findViewById(R.id.mitarbeiterEmailInput);
        passwordEditText = findViewById(R.id.passwortInput);
        Button loginButton = findViewById(R.id.loginButton);
        Button exitButton = findViewById(R.id.ExitAppButton);

        // Eingabefelder beim Start leeren
        emailEditText.setText("");
        passwordEditText.setText("");

        // Login-Button mit Login-Funktion verknüpfen
        loginButton.setOnClickListener(v -> loginUser());

        // Exit-Button mit Exit-Funktion verknüpfen
        exitButton.setOnClickListener(v -> exitApp());
    }

    /**
     * Diese Methode führt den Login-Vorgang mit Firebase durch.
     * Nach erfolgreicher Anmeldung wird die Rolle des Benutzers geladen und zur passenden Activity weitergeleitet.
     */
    private void loginUser() {
        // Eingaben holen und trimmen
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Eingabevalidierung
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Bitte Emailadresse und Passwort eingeben.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Anmeldung bei Firebase Authentication
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    // UID des aktuellen Benutzers
                    String uid = mAuth.getCurrentUser().getUid();

                    // Benutzerrolle aus Realtime Database abrufen
                    userRef.child(uid).child("role").get().addOnSuccessListener(snapshot -> {
                        String role = snapshot.getValue(String.class);

                        // Activity basierend auf Rolle starten
                        if ("mitarbeiter".equals(role)) {
                            startActivity(new Intent(this, EmployeeActivity.class));
                        } else if ("vorgesetzter".equals(role)) {
                            startActivity(new Intent(this, SupervisorActivity.class));
                        }

                        // LoginActivity beenden
                        finish();
                    });

                }).addOnFailureListener(e ->
                        // Bei Fehlern: Fehlermeldung anzeigen
                        Toast.makeText(this, "Login fehlgeschlagen!\nE-Mail oder Passwort falsch.", Toast.LENGTH_SHORT).show()
                );
    }

    /**
     * Beendet die App vollständig (inkl. aller offenen Activities).
     */
    private void exitApp() {
        finishAndRemoveTask();  // Beendet die Task und entfernt sie aus dem App-Switcher
    }
}
