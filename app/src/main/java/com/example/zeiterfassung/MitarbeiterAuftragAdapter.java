package com.example.zeiterfassung;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Adapter-Klasse für RecyclerView in der Mitarbeiter-Ansicht.
 * Diese Klasse stellt Aufträge als Karten dar und ermöglicht das Öffnen eines ausgewählten Auftrags.
 */
public class MitarbeiterAuftragAdapter extends RecyclerView.Adapter<MitarbeiterAuftragAdapter.AuftragViewHolder> {

    // Liste der darzustellenden Aufträge
    private final List<Auftrag> auftragList;

    // Konstruktor: erhält eine Liste von Aufträgen
    public MitarbeiterAuftragAdapter(List<Auftrag> auftragList) {
        this.auftragList = auftragList;
    }

    /**
     * ViewHolder: Verwaltet die UI-Komponenten einer einzelnen Karte.
     */
    public static class AuftragViewHolder extends RecyclerView.ViewHolder {
        TextView orderNumberTextView, customerNameTextView, einsatzgrundTextView, addressTextView, mitarbeiterTextView;
        View cardBackground; // Hintergrund der Karte (für farbliche Priorisierung)

        public AuftragViewHolder(@NonNull View itemView) {
            super(itemView);
            orderNumberTextView = itemView.findViewById(R.id.orderNumberText);
            einsatzgrundTextView = itemView.findViewById(R.id.reasonForOrderText);
            customerNameTextView = itemView.findViewById(R.id.customerText);
            addressTextView = itemView.findViewById(R.id.addressText);
            mitarbeiterTextView = itemView.findViewById(R.id.cardItem_mitarbeiter);
            cardBackground = itemView.findViewById(R.id.cardViewBackground);
        }
    }

    /**
     * Erstellt die Ansicht (Card) für ein Element in der Liste.
     */
    @Override
    public AuftragViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false); // Layout einer Karte laden
        return new AuftragViewHolder(view);
    }

    /**
     * Verbindet die Daten eines Auftrags mit dem ViewHolder.
     */
    @Override
    public void onBindViewHolder(@NonNull AuftragViewHolder holder, int position) {
        Auftrag auftrag = auftragList.get(position);

        // Textfelder mit Daten füllen
        holder.orderNumberTextView.setText("Auftragsnummer: " + auftrag.getOrderNumber());
        holder.einsatzgrundTextView.setText("Einsatzgrund: " + auftrag.getEinsatzgrund());
        holder.customerNameTextView.setText("Kundenname: " + auftrag.getCustomerName());
        holder.addressTextView.setText("Einsatzort: " + auftrag.getCustomerAddress());
        holder.mitarbeiterTextView.setText("Mitarbeiter: " + auftrag.getMitarbeiter());

        // Wenn eine Karte geklickt wird, öffnet sich die Detail-Ansicht
        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, EmployeeCurrentJobActivity.class);

            // Auftragsdaten an die neue Activity übergeben
            intent.putExtra("id", auftrag.getId());
            intent.putExtra("orderNumber", auftrag.getOrderNumber());
            intent.putExtra("customerName", auftrag.getCustomerName());
            intent.putExtra("customerAddress", auftrag.getCustomerAddress());
            intent.putExtra("einsatzgrund", auftrag.getEinsatzgrund());
            intent.putExtra("mitarbeiter", auftrag.getMitarbeiter());
            intent.putExtra("prioritaet", auftrag.getPrioritaet());
            intent.putExtra("weitereInformationen", auftrag.getMoreInformation());

            context.startActivity(intent);

            // Optional: Aktuelle Activity beenden, um Rücknavigation zu vermeiden
            if (context instanceof Activity) {
                ((Activity) context).finish();
            }
        });

        // Kartenhintergrundfarbe abhängig von Priorität setzen
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
                    // Standardfarbe bei unbekannter Priorität
                    holder.cardBackground.setBackgroundColor(Color.WHITE);
                    Log.d("PrioritaetDebug", "Unbekannte Priorität: " + prioritaet);
            }
        }
    }

    /**
     * Gibt die Anzahl der Elemente in der Liste zurück.
     */
    @Override
    public int getItemCount() {
        return auftragList.size();
    }

    /**
     * Erlaubt das Ersetzen der Liste durch eine neue.
     */
    @SuppressLint("NotifyDataSetChanged")
    public void setAuftraege(List<Auftrag> neueListe) {
        auftragList.clear();
        auftragList.addAll(neueListe);
        notifyDataSetChanged(); // RecyclerView aktualisieren
    }
}
