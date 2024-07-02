package com.zybooks.vacationapp.UI;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zybooks.vacationapp.R;
import com.zybooks.vacationapp.dao.VacationDAO;
import com.zybooks.vacationapp.database.Repository;
import com.zybooks.vacationapp.entitites.Excursion;
import com.zybooks.vacationapp.entitites.Vacation;

import java.util.List;

public class ExcursionAdapter extends RecyclerView.Adapter<ExcursionAdapter.ExcursionViewHolder>{
    private List<Excursion> mExcursions;
    private final Context context;
    private final LayoutInflater mInflater;



    class ExcursionViewHolder extends RecyclerView.ViewHolder {

        private final TextView excursionItemView;

        private final TextView excursionItemView2;

        private final TextView excursionItemView3;

        private final  TextView excursionItemView4;

        public ExcursionViewHolder(@NonNull View itemView) {
            super(itemView);
            excursionItemView = itemView.findViewById(R.id.textView3);
            excursionItemView2 = itemView.findViewById(R.id.textView4);
            excursionItemView3 = itemView.findViewById(R.id.textView5);
            excursionItemView4 = itemView.findViewById(R.id.textView6);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Vacation vacation;

                    int position = getAdapterPosition();
                    final Excursion current = mExcursions.get(position);
                    Intent intent = new Intent(context, ExcursionDetails.class);
                    intent.putExtra("excursionId", current.getExcursionId());
                    intent.putExtra("excursionName", current.getExcursionTitle());
                    intent.putExtra("excursionDate", current.getExcursionDate());
                    intent.putExtra("ExcursionVacationId", current.getVacationId());
                    context.startActivity(intent);
                }
            });
        }
    }
    public ExcursionAdapter(Context context){

        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public ExcursionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.excursion_list_item, parent, false);
        return new ExcursionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExcursionViewHolder holder, int position) {
        if (mExcursions != null) {
            Excursion current = mExcursions.get(position);
            holder.excursionItemView.setText(current.getExcursionTitle() + " ");
            holder.excursionItemView2.setText(current.getExcursionDate() + " ");
            holder.excursionItemView3.setText("Vacation ID: " + current.getVacationId()+ " ");
            holder.excursionItemView4.setText("Excursion ID: " + current.getExcursionId()+ " ");
            if (position % 2 == 0) {
                holder.itemView.setBackgroundColor(Color.BLUE);
            } else {
                holder.itemView.setBackgroundColor(Color.BLACK);
            }
            holder.itemView.setPadding(0, 0, 0, 50);

        } else {
            holder.excursionItemView.setText("No Excursion");
            holder.excursionItemView2.setText("No Date");
        }
    }
    public void setExcursions(List<Excursion> excursions){
        mExcursions = excursions;
        notifyDataSetChanged();
    }
    public int getItemCount() {
        if(mExcursions != null){
            return mExcursions.size();
        }
        else{
            return 0;
        }
    }
}

