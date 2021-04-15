package com.onlineapteka.noteandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button button;
    MainAdapter adapter;
    List<ItemModel> itemModelList;
    EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.main_recycler);
        button = findViewById(R.id.button_add);
        editText = findViewById(R.id.title_edit_text);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editText.getText().toString())){

                }else {

                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MainAdapter(this,itemModelList);
        recyclerView.setAdapter(adapter);

    }
}