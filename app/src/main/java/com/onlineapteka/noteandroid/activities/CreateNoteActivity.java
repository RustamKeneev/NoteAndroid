package com.onlineapteka.noteandroid.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.onlineapteka.noteandroid.R;
import com.onlineapteka.noteandroid.database.NotesDatabase;
import com.onlineapteka.noteandroid.entities.Note;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateNoteActivity extends AppCompatActivity {
    private ImageView imageBack;
    private EditText editTextNoteTitle;
    private EditText editTextNoteSubTitle;
    private EditText editTextNote;
    private TextView textDateTime;
    private ImageView imageSave;
    private ImageView imageArrow;
    private String selectedNoteColor;
    private View viewSubtitleIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        initViews();
    }

    private void initViews() {
        imageBack = findViewById(R.id.image_back);
        editTextNoteTitle = findViewById(R.id.edit_text_note_title);
        editTextNoteSubTitle = findViewById(R.id.edit_text_note_sub_title);
        editTextNote = findViewById(R.id.edit_text_note);
        textDateTime = findViewById(R.id.text_date_time);
        imageSave = findViewById(R.id.image_save);
        viewSubtitleIndicator = findViewById(R.id.view_sub_title_indicator);
//        imageArrow = findViewById(R.id.image_arrow);

        imageSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        textDateTime.setText(new SimpleDateFormat("EEEE,dd MMMM yyyy HH:mm a",
                Locale.getDefault()).format(new Date()));

        selectedNoteColor = "#333333";

        initMiscellaneous();
        setSubtitleIndicatorColor();
    }

    private void saveNote(){
        if (editTextNoteTitle.getText().toString().trim().isEmpty()){
            Toast.makeText(this,"Note title can't be empty!",Toast.LENGTH_LONG).show();
            return;
        }else if (editTextNoteSubTitle.getText().toString().trim().isEmpty() &&
                editTextNote.getText().toString().trim().isEmpty()){
            Toast.makeText(this,"Note can't be empty!",Toast.LENGTH_LONG).show();
            return;
        }
        final Note note = new Note();
        note.setTitle(editTextNoteTitle.getText().toString());
        note.setSubTitle(editTextNoteSubTitle.getText().toString());
        note.setNoteText(editTextNote.getText().toString());
        note.setDateTime(textDateTime.getText().toString());
        note.setColor(selectedNoteColor);


        @SuppressLint("StaticFieldLeak")
        class SaveNoteTask extends AsyncTask<Void,Void,Void>{
            @Override
            protected Void doInBackground(Void... voids) {
                NotesDatabase.getNotesDatabase(getApplicationContext()).noteDao().insertNote(note);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Intent intent = new Intent();
                setResult(RESULT_OK,intent);
                finish();
            }
        }
        new SaveNoteTask().execute();
    }

    private void initMiscellaneous(){
        final LinearLayout layoutMiscellaneous = findViewById(R.id.layout_miscellaneous);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(layoutMiscellaneous);
        layoutMiscellaneous.findViewById(R.id.text_miscellaneous).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED){
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                            imageArrow.setImageResource(R.drawable.ic_down_arrows);
//                            imageArrow.setVisibility(View.VISIBLE);
                        }else {
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                            imageArrow.setImageResource(R.drawable.ic_up_arrow);
//                            imageArrow.setVisibility(View.GONE);
                        }
                    }
                }
        );
        final ImageView imageView1 = layoutMiscellaneous.findViewById(R.id.image_color_one);
        final ImageView imageView2 = layoutMiscellaneous.findViewById(R.id.image_color_two);
        final ImageView imageView3 = layoutMiscellaneous.findViewById(R.id.image_color_three);
        final ImageView imageView4 = layoutMiscellaneous.findViewById(R.id.image_color_four);
        final ImageView imageView5 = layoutMiscellaneous.findViewById(R.id.image_color_five);

        layoutMiscellaneous.findViewById(R.id.view_color_one).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedNoteColor = "#333333";
                        imageView1.setImageResource(R.drawable.ic_done);
                        imageView2.setImageResource(0);
                        imageView3.setImageResource(0);
                        imageView4.setImageResource(0);
                        imageView5.setImageResource(0);
                        setSubtitleIndicatorColor();
                    }
                }
        );

        layoutMiscellaneous.findViewById(R.id.view_color_two).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedNoteColor = "#FDBE3B";
                        imageView1.setImageResource(0);
                        imageView2.setImageResource(R.drawable.ic_done);
                        imageView3.setImageResource(0);
                        imageView4.setImageResource(0);
                        imageView5.setImageResource(0);
                        setSubtitleIndicatorColor();
                    }
                }
        );

        layoutMiscellaneous.findViewById(R.id.view_color_three).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedNoteColor = "#FF4842";
                        imageView1.setImageResource(0);
                        imageView2.setImageResource(0);
                        imageView3.setImageResource(R.drawable.ic_done);
                        imageView4.setImageResource(0);
                        imageView5.setImageResource(0);
                        setSubtitleIndicatorColor();
                    }
                }
        );

        layoutMiscellaneous.findViewById(R.id.view_color_four).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedNoteColor = "#3A52FC";
                        imageView1.setImageResource(0);
                        imageView2.setImageResource(0);
                        imageView3.setImageResource(0);
                        imageView4.setImageResource(R.drawable.ic_done);
                        imageView5.setImageResource(0);
                        setSubtitleIndicatorColor();
                    }
                }
        );

        layoutMiscellaneous.findViewById(R.id.view_color_five).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedNoteColor = "#000000";
                        imageView1.setImageResource(0);
                        imageView2.setImageResource(0);
                        imageView3.setImageResource(0);
                        imageView4.setImageResource(0);
                        imageView5.setImageResource(R.drawable.ic_done);
                        setSubtitleIndicatorColor();
                    }
                }
        );
    }
    private void setSubtitleIndicatorColor(){
        GradientDrawable gradientDrawable = (GradientDrawable) viewSubtitleIndicator.getBackground();
        gradientDrawable.setColor(Color.parseColor(selectedNoteColor));
    }
}