package com.onlineapteka.noteandroid.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        note.setTitle(editTextNoteSubTitle.getText().toString());
        note.setTitle(editTextNote.getText().toString());
        note.setDateTime(textDateTime.getText().toString());


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
}