package com.example.zeiterfassung;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Diese Activity zeigt zusätzliche Informationen für den Supervisor an.
 * Der Zurück-Button (System-Button) wird deaktiviert, um gezielte Navigation zu erzwingen.
 */
public class SupervisorMoreInformationActivity extends AppCompatActivity {

    /**
     * Überschreibt das Verhalten des Android-System-Zurück-Buttons.
     * Statt die Activity zu verlassen, wird eine Info angezeigt.
     */
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Bitte den zurück Button benutzen.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Verknüpft das Layout mit dieser Activity
        setContentView(R.layout.activity_supervisor_more_information);

        // Button zum Zurücknavigieren zur "Neuen Auftrag erstellen"-Seite
        Button backToSupervisorAddNewJob_S = findViewById(R.id.backToAddNewJobPage_supervisor_Button);

        // Click-Listener für den Zurück-Button
        backToSupervisorAddNewJob_S.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Wechsel zurück zur SupervisorNewJobActivity
                Intent intent = new Intent(SupervisorMoreInformationActivity.this, SupervisorNewJobActivity.class);
                startActivity(intent);
                finish(); // Schließt die aktuelle Activity
            }
        });
    }
}
