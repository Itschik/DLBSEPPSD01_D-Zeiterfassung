package com.example.zeiterfassung;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
 * Adapter für RecyclerView, um eine Liste von Aufträgen anzuzeigen.
 * Unterstützt Klick- und LongClick-Events für jede Kartenansicht.
 */
public class AuftragAdapter extends RecyclerView.Adapter<AuftragAdapter.AuftragViewHolder> {

    // Liste der Aufträge, die angezeigt werden sollen
    private final List<Auftrag> auftragList;

    /**
     * Konstruktor des Adapters.
     * @param auftragList Liste von Aufträgen, die angezeigt werden.
     */
    public AuftragAdapter(List<Auftrag> auftragList) {
        this.auftragList = auftragList;
    }

    /**
     * ViewHolder definiert die einzelnen UI-Elemente in der Kartenansicht.
     */
    public static class AuftragViewHolder extends RecyclerView.ViewHolder {
        TextView orderNumberTextView, customerNameTextView, einsatzgrundTextView, addressTextView, mitarbeiterTextView;
        View cardBackground;

        public AuftragViewHolder(@NonNull View itemView) {
            super(itemView);
            // Views aus dem Layout finden
            orderNumberTextView = itemView.findViewById(R.id.orderNumberText);
            einsatzgrundTextView = itemView.findViewById(R.id.reasonForOrderText);
            customerNameTextView = itemView.findViewById(R.id.customerText);
            addressTextView = itemView.findViewById(R.id.addressText);
            mitarbeiterTextView = itemView.findViewById(R.id.cardItem_mitarbeiter);
            cardBackground = itemView.findViewById(R.id.cardViewBackground);
        }
    }

    /**
     * Erzeugt neue ViewHolder-Instanzen und inflatet das Kartenlayout.
     */
    @Override
    public AuftragViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false);
        return new AuftragViewHolder(view);
    }

    /**
     * Bindet die Auftragsdaten an die Views im ViewHolder.
     * Setzt auch die Hintergrundfarbe je nach Priorität.
     * Definiert Klick- und Long-Click-Verhalten der Karten.
     */
    @Override
    public void onBindViewHolder(@NonNull AuftragViewHolder holder, int position) {
        Auftrag auftrag = auftragList.get(position);

        // Auftragsinformationen in die TextViews eintragen
        holder.orderNumberTextView.setText("Auftragsnummer: " + auftrag.getOrderNumber());
        holder.einsatzgrundTextView.setText("Einsatzgrund: " + auftrag.getEinsatzgrund());
        holder.customerNameTextView.setText("Kundenname: " + auftrag.getCustomerName());
        holder.addressTextView.setText("Einsatzort: " + auftrag.getCustomerAddress());
        holder.mitarbeiterTextView.setText("Mitarbeiter: " + auftrag.getMitarbeiter());

        // LongClick: Zeigt einen Dialog zum Löschen des Auftrags an
        holder.itemView.setOnLongClickListener(v -> {
            Context context = v.getContext();

            new AlertDialog.Builder(context)
                    .setTitle("Auftrag löschen")
                    .setMessage("Möchten Sie diesen Auftrag wirklich löschen?")
                    .setPositiveButton("Löschen", (dialog, which) -> {
                        // Auftrag aus Firebase-Datenbank entfernen
                        String auftragId = auftrag.getId();

                        FirebaseDatabase.getInstance()
                                .getReference("auftraege")
                                .child(auftragId)
                                .removeValue()
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(context, "Auftrag gelöscht", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(context, "Fehler beim Löschen", Toast.LENGTH_SHORT).show();
                                });
                    })
                    .setNegativeButton("Abbrechen", null)
                    .show();

            return true; // LongClick-Ereignis wurde behandelt
        });

        // Klick auf Karte: Öffnet Detailansicht (SupervisorCurrentJobActivity) mit Auftragsdaten
        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, SupervisorCurrentJobActivity.class);
            intent.putExtra("id", auftrag.getId());
            intent.putExtra("orderNumber", auftrag.getOrderNumber());
            intent.putExtra("customerName", auftrag.getCustomerName());
            intent.putExtra("customerAddress", auftrag.getCustomerAddress());
            intent.putExtra("einsatzgrund", auftrag.getEinsatzgrund());
            intent.putExtra("mitarbeiter", auftrag.getMitarbeiter());
            intent.putExtra("prioritaet", auftrag.getPrioritaet());
            intent.putExtra("weitereInformationen", auftrag.getMoreInformation());
            context.startActivity(intent);

            // Falls Kontext eine Activity ist, diese beenden, damit keine unnötige History entsteht
            if (context instanceof Activity) {
                ((Activity) context).finish();
            }
        });

        // Setze Hintergrundfarbe der Karte abhängig von der Priorität
        String prioritaet = auftrag.getPrioritaet();
        if (prioritaet != null) {
            switch (prioritaet.trim().toLowerCase()) {
                case "hoch":
                    holder.cardBackground.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.prioritaet_Hoch));
                    break;
                case "mittel":
                    holder.cardBackground.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.prioritaet_Mittel));
                    break;
                case "niedrig":
                    holder.cardBackground.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.prioritaet_Niedrig));
                    break;
                default:
                    // Weiß als Standardfarbe, falls Priorität unbekannt ist
                    holder.cardBackground.setBackgroundColor(Color.WHITE);
                    Log.d("PrioritaetDebug", "Unbekannte Priorität: " + prioritaet);
            }
        }
    }

    /**
     * Gibt die Anzahl der Aufträge zurück.
     */
    @Override
    public int getItemCount() {
        return auftragList.size();
    }
}
