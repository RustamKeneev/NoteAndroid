package com.onlineapteka.noteandroid.adapters;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.onlineapteka.noteandroid.R;
import com.onlineapteka.noteandroid.entities.Note;
import com.onlineapteka.noteandroid.listeners.NoteListeners;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder>{

     private List<Note> noteList;
     private NoteListeners noteListeners;

    public NoteAdapter(List<Note> noteList, NoteListeners noteListeners) {
        this.noteList = noteList;
        this.noteListeners = noteListeners;
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
        holder.layoutNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteListeners.onNoteClicked(noteList.get(position),position);
            }
        });
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
        LinearLayout layoutNote;
        RoundedImageView roundedImageView;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTextTitle = itemView.findViewById(R.id.item_text_title);
            itemTextSubtitle = itemView.findViewById(R.id.item_text_subtitle);
            itemTextDateTime = itemView.findViewById(R.id.item_text_date_time);
            layoutNote = itemView.findViewById(R.id.layout_note);
            roundedImageView = itemView.findViewById(R.id.image_note);

        }

       public void onBind(Note note){
            itemTextTitle.setText(note.getTitle());
            if (note.getSubTitle().trim().isEmpty()){
                itemTextSubtitle.setVisibility(View.GONE);
            }else {
                itemTextSubtitle.setText(note.getSubTitle());
            }
            itemTextDateTime.setText(note.getDateTime());
           GradientDrawable gradientDrawable = (GradientDrawable) layoutNote.getBackground();
           if (note.getColor() !=null){
               gradientDrawable.setColor(Color.parseColor(note.getColor()));
           }else {
               gradientDrawable.setColor(Color.parseColor("#333333"));
           }
           if (note.getImagePath() !=null){
               roundedImageView.setImageBitmap(BitmapFactory.decodeFile(note.getImagePath()));
               roundedImageView.setVisibility(View.VISIBLE);
           }else {
               roundedImageView.setVisibility(View.GONE);
           }
        }
    }
}
