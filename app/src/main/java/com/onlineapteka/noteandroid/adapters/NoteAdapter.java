package com.onlineapteka.noteandroid.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.onlineapteka.noteandroid.R;
import com.onlineapteka.noteandroid.entities.Note;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder>{

     List<Note> noteList;

    public NoteAdapter(List<Note> noteList) {
        this.noteList = noteList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_note,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.onBind(noteList.get(position));
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return  position;
    }

   public static class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView itemTextTitle;
        TextView itemTextSubtitle;
        TextView itemTextDateTime;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTextTitle = itemView.findViewById(R.id.item_text_title);
            itemTextSubtitle = itemView.findViewById(R.id.item_text_subtitle);
            itemTextDateTime = itemView.findViewById(R.id.item_text_date_time);
        }

       public void onBind(Note note){
            itemTextTitle.setText(note.getTitle());
            if (note.getSubTitle().trim().isEmpty()){
                itemTextSubtitle.setVisibility(View.GONE);
            }else {
                itemTextSubtitle.setText(note.getSubTitle());
            }
            itemTextDateTime.setText(note.getDateTime());
        }
    }
}
