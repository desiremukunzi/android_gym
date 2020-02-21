package com.example.trynfc;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class Home extends AppCompatActivity {
    Button button;
    EditText editText1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        button = findViewById(R.id.button);

//        editText1 = findViewById(R.id.editText1);



//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent tes = new Intent(Home.this, MainActivity2.class);
//                tes.putExtra ("MyData", editText1.getText().toString());
//                startActivity(tes);
//            }
//        });

    }


    public void fetch(View view) {

    }
}