package com.example.zeiterfassung;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SupervisorMoreInformationActivity extends AppCompatActivity {

    // Hier wird der Zurückbutton für diese Activity blockiert
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Bitte den zurück Button benutzen.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_more_information);  // <--- Name der Layout-XML

        Button backToSupervisorAddNewJob_S = findViewById(R.id.backToAddNewJobPage_supervisor_Button);


// Zurück Button zur Add New Job Activity
        backToSupervisorAddNewJob_S.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Zurück zur Add New JobActivity
                Intent intent = new Intent(SupervisorMoreInformationActivity.this, SupervisorNewJobActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();  // Beendet die aktuelle Activity
            }
        });


    }
}
