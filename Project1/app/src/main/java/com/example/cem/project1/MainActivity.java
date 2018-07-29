package com.example.cem.project1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       tv= (TextView) findViewById(R.id.cevap);
       Button b= (Button) findViewById(R.id.button);
       b.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
       tv.setText(R.string.bilmece_cevabi);
    }
}
