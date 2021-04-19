package com.onlineapteka.noteandroid.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.onlineapteka.noteandroid.R;
import com.onlineapteka.noteandroid.adapters.NoteAdapter;
import com.onlineapteka.noteandroid.database.NotesDatabase;
import com.onlineapteka.noteandroid.entities.Note;
import com.onlineapteka.noteandroid.listeners.NoteListeners;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NoteListeners {

    public static final int REQUEST_CODE_ADD_NOTE = 1;
    public static final int REQUEST_CODE_UPDATE_NOTE = 2;
    public static final int REQUEST_CODE_SHOW_NOTES = 3;
    public static final int REQUEST_CODE_SELECT_IMAGE = 4;
    public static final int REQUEST_CODE_STORAGE_PERMISSION = 5;

    private int noteClickedPosition = -1;

    private ImageView imageAddMainNote;
    private RecyclerView notesRecyclerView;
    private List<Note> noteList;
    private NoteAdapter noteAdapter;
    private EditText editTextSearch;
    private AlertDialog alertDialogAddUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initviews();
    }

    private void initviews() {
        imageAddMainNote = findViewById(R.id.image_add_main_note);
        editTextSearch = findViewById(R.id.edit_text_search);

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                noteAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (noteList.size() != 0){
                     noteAdapter.searchNotes(s.toString());
                }
            }
        });

        findViewById(R.id.image_add_note).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        new Intent(getApplicationContext(), CreateNoteActivity.class),
                        REQUEST_CODE_ADD_NOTE);
            }
        });

        findViewById(R.id.image_add_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(
                        getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(
                            MainActivity.this,
                            new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_STORAGE_PERMISSION
                    );
                }else {
                    selectImage();
                }
            }
        });

        findViewById(R.id.image_add_web_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddUrlDialog();
            }
        });

        imageAddMainNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        new Intent(getApplicationContext(), CreateNoteActivity.class),
                        REQUEST_CODE_ADD_NOTE);
            }
        });

        notesRecyclerView = findViewById(R.id.recycler_view_note);
        notesRecyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));


        noteList = new ArrayList<>();
        noteAdapter = new NoteAdapter(noteList,this);
        notesRecyclerView.setAdapter(noteAdapter);

        getNotes(REQUEST_CODE_SHOW_NOTES,false);
    }

    private void getNotes(final int requestCode, final boolean isNoteDeleted){
        @SuppressLint("StaticFieldLeak")
        class GetNotesTask extends AsyncTask<Void,Void, List<Note>>{
            @Override
            protected List<Note> doInBackground(Void... voids) {
                return NotesDatabase
                        .getNotesDatabase(getApplicationContext())
                        .noteDao()
                        .getAllNotes();
            }

            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);
                if (requestCode == REQUEST_CODE_SHOW_NOTES){
                    noteList.addAll(notes);
                    noteAdapter.notifyDataSetChanged();
                }else if (requestCode == REQUEST_CODE_ADD_NOTE){
                    noteList.add(0,notes.get(0));
                    noteAdapter.notifyItemInserted(0);
                    notesRecyclerView.smoothScrollToPosition(0);
                }else if (requestCode == REQUEST_CODE_UPDATE_NOTE){
                    noteList.remove(noteClickedPosition);
                    if (isNoteDeleted){
                        noteAdapter.notifyItemRemoved(noteClickedPosition);
                    }else {
                        noteList.add(noteClickedPosition,notes.get(noteClickedPosition));
                        noteAdapter.notifyItemChanged(noteClickedPosition);
                    }
                }
            }
        }
        new GetNotesTask().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==  REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK){
            getNotes(REQUEST_CODE_ADD_NOTE,false);
        }else if (requestCode == REQUEST_CODE_UPDATE_NOTE && resultCode ==  RESULT_OK){
            if (data !=null){
                getNotes(REQUEST_CODE_UPDATE_NOTE,data.getBooleanExtra("isNoteDeleted",false));
            }
        }else if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK){
            if (data !=null){
                Uri selectedImageUri = data.getData();
                if (selectedImageUri !=null){
                    try {
                        String selectesImagePath = getPathFromUri(selectedImageUri);
                        Intent intent = new Intent(getApplicationContext(),CreateNoteActivity.class);
                        intent.putExtra("isFromQuickActions",true);
                        intent.putExtra("quickActionType","image");
                        intent.putExtra("imagePath",selectesImagePath);
                        startActivityForResult(intent,REQUEST_CODE_ADD_NOTE);
                    }catch (Exception exception){
                        Toast.makeText(MainActivity.this,exception.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.length > 0 ){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                selectImage();
            }else {
                Toast.makeText(this,"Permission Denied!",Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getPathFromUri(Uri contentUri){
        String filePath;
        Cursor cursor = getContentResolver()
                .query(contentUri,null,null,null,null);
        if (cursor == null){
            filePath = contentUri.getPath();
        }else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex("_data");
            filePath = cursor.getString(index);
            cursor.close();
        }
        return filePath;
    }

    private void selectImage(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) !=null){
            startActivityForResult(intent,REQUEST_CODE_SELECT_IMAGE);
        }
    }

    @Override
    public void onNoteClicked(Note note, int position) {
        noteClickedPosition = position;
        Intent intent = new Intent(getApplicationContext(),CreateNoteActivity.class);
        intent.putExtra("isViewOrUpdate",true);
        intent.putExtra("note",note);
        startActivityForResult(intent,REQUEST_CODE_UPDATE_NOTE);
    }

    private void showAddUrlDialog(){
        if (alertDialogAddUrl == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            View view = LayoutInflater.from(this)
                    .inflate(R.layout.layout_add_url,
                            (ViewGroup) findViewById(R.id.layout_add_url_container));
            builder.setView(view);
            alertDialogAddUrl = builder.create();
            if (alertDialogAddUrl.getWindow() !=null){
                alertDialogAddUrl.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            final EditText editTextUrl = view.findViewById(R.id.edit_url);
            editTextUrl.requestFocus();

            view.findViewById(R.id.text_add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editTextUrl.getText().toString().trim().isEmpty()){
                        Toast.makeText(MainActivity.this,"Enter URL",
                                Toast.LENGTH_LONG).show();
                    }else  if (!Patterns.WEB_URL.matcher(editTextUrl.getText().toString()).matches()){
                        Toast.makeText(MainActivity.this,"Enter valid URL",
                                Toast.LENGTH_LONG).show();
                    }else {
                        alertDialogAddUrl.dismiss();
                        Intent intent = new Intent(getApplicationContext(),CreateNoteActivity.class);
                        intent.putExtra("isFromQuickActions",true);
                        intent.putExtra("quickActionType","URL");
                        intent.putExtra("URL",editTextUrl.getText().toString());
                        startActivityForResult(intent,REQUEST_CODE_ADD_NOTE);
                    }
                }
            });

            view.findViewById(R.id.text_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialogAddUrl.dismiss();
                }
            });
        }
        alertDialogAddUrl.show();
    }
}