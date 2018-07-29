package com.example.kartoyun.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {

        Intent i = new Intent(ResultActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        ((Button)findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
        TextView tv =(TextView) findViewById(R.id.textView3);
        TextView tv2=  (TextView) findViewById(R.id.textView4);
        Intent i = getIntent();
        int hata = i.getIntExtra("puan",0);
        int score1 = i.getIntExtra("score1",0);
        int score2 = i.getIntExtra("score2",0);


        tv.setText(hata + " hata ile oyun bitti");

        if(score1>score2)
            tv2.setText("KAZANDIN");
        else if(score1 == score2)
            tv2.setText("BERABERE");
        else
            tv2.setText("KAYBETTİN");
    }
}
