package com.example.zeiterfassung;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Adapter-Klasse für RecyclerView zur Anzeige von bereits abgeschlossenen Aufträgen.
 * Ermöglicht langes Tippen zum Löschen einzelner Aufträge aus Firebase Realtime Database.
 */
public class FertigerAuftragAdapter extends RecyclerView.Adapter<FertigerAuftragAdapter.AuftragViewHolder> {

    // Liste der anzuzeigenden Aufträge
    private final List<Auftrag> auftragList;

    // Konstruktor
    public FertigerAuftragAdapter(List<Auftrag> auftragList) {
        this.auftragList = auftragList;
    }

    /**
     * ViewHolder-Klasse hält die Referenzen zu allen UI-Elementen in einem Card-Item
     */
    public static class AuftragViewHolder extends RecyclerView.ViewHolder {
        TextView orderNumberTextView, customerNameTextView, einsatzgrundTextView,
                addressTextView, mitarbeiterTextView, auftragsDauerTextView;
        View cardBackground;

        public AuftragViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialisieren der UI-Elemente im Layout
            orderNumberTextView     = itemView.findViewById(R.id.orderNumberText);
            einsatzgrundTextView    = itemView.findViewById(R.id.reasonForOrderText);
            customerNameTextView    = itemView.findViewById(R.id.customerText);
            addressTextView         = itemView.findViewById(R.id.addressText);
            auftragsDauerTextView   = itemView.findViewById(R.id.cardItem_arbeitszeit);
            mitarbeiterTextView     = itemView.findViewById(R.id.cardItem_mitarbeiter);
            cardBackground          = itemView.findViewById(R.id.cardViewBackground);
        }
    }

    @Override
    public AuftragViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Card-Layout für abgeschlossene Aufträge aufblähen
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fertiger_auftrag_card_item, parent, false);
        return new AuftragViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AuftragViewHolder holder, int position) {
        Auftrag auftrag = auftragList.get(position);

        // Textfelder mit Auftragsinformationen befüllen
        holder.orderNumberTextView.setText("Auftragsnummer: " + auftrag.getOrderNumber());
        holder.einsatzgrundTextView.setText("Einsatzgrund: " + auftrag.getEinsatzgrund());
        holder.customerNameTextView.setText("Kundenname: " + auftrag.getCustomerName());
        holder.addressTextView.setText("Einsatzort: " + auftrag.getCustomerAddress());
        holder.mitarbeiterTextView.setText("Mitarbeiter: " + auftrag.getMitarbeiter());
        holder.auftragsDauerTextView.setText("Auftragsdauer: " + auftrag.getAuftragsDauer());

        // Long-Click zum Löschen des Auftrags aus der Datenbank
        holder.itemView.setOnLongClickListener(v -> {
            Context context = v.getContext();

            // Bestätigungsdialog anzeigen
            new AlertDialog.Builder(context)
                    .setTitle("Auftrag löschen")
                    .setMessage("Möchten Sie diesen Auftrag wirklich löschen?")
                    .setPositiveButton("Löschen", (dialog, which) -> {
                        String auftragId = auftrag.getId();

                        // Auftrag aus Firebase entfernen
                        FirebaseDatabase.getInstance()
                                .getReference("fertigeAuftraege")
                                .child(auftragId)
                                .removeValue()
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(context, "Auftrag gelöscht", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(context, "Fehler beim Löschen", Toast.LENGTH_SHORT).show();
                                });
                    })
                    .setNegativeButton("Abbrechen", null) // Nichts tun bei Abbruch
                    .show();

            return true; // Signalisiert, dass der LongClick behandelt wurde
        });

        // Kartenhintergrundfarbe anhand der Priorität setzen
        String prioritaet = auftrag.getPrioritaet();
        if (prioritaet != null) {
            switch (prioritaet.trim().toLowerCase()) {
                case "hoch":
                    holder.cardBackground.setBackgroundColor(
                            ContextCompat.getColor(holder.itemView.getContext(), R.color.prioritaet_Hoch));
                    break;
                case "mittel":
                    holder.cardBackground.setBackgroundColor(
                            ContextCompat.getColor(holder.itemView.getContext(), R.color.prioritaet_Mittel));
                    break;
                case "niedrig":
                    holder.cardBackground.setBackgroundColor(
                            ContextCompat.getColor(holder.itemView.getContext(), R.color.prioritaet_Niedrig));
                    break;
                default:
                    // Unbekannte Priorität: weißer Hintergrund und Log-Eintrag
                    holder.cardBackground.setBackgroundColor(Color.WHITE);
                    Log.d("PrioritaetDebug", "Unbekannte Priorität: " + prioritaet);
            }
        }
    }

    @Override
    public int getItemCount() {
        return auftragList.size();  // Anzahl der angezeigten Elemente im RecyclerView
    }
}
