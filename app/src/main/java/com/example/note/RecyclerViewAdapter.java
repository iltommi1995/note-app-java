package com.example.note;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>
{
    List<Nota> note;
    private Context context;

    public RecyclerViewAdapter(List<Nota> note, Context context) {
        this.note = note;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_row, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        Nota nota = note.get(position);
        holder.title.setText(nota.getTitle());
        holder.text.setText(nota.getNotaRecap());
        holder.date.setText(nota.getDate());
        holder.touch_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.sendObject(context, position);
                notifyItemChanged(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return note.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView title;
        private TextView text;
        private TextView date;
        private RelativeLayout touch_layout;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            text = itemView.findViewById(R.id.text);
            touch_layout = itemView.findViewById(R.id.touch_layout);
        }
    }
}
