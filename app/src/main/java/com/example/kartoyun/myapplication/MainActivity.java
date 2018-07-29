package com.example.kartoyun.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatDrawableManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.socket.client.Socket;
import io.socket.client.IO;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {

    int sonkart=0;
    int score1=0;
    int score2=0;
    int hata =0;
    boolean connectioncheck=false;

    TextView player1;
    TextView tv2;
    TextView player2 ;

    myCard lastCard;
    myCard currentCard;

    myCard kartlar[] = new myCard[16];
    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        connectSocket();
        setContentView(R.layout.activity_main);
        Intent i = getIntent();


        ((Button)findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
        GridLayout cards= (GridLayout)findViewById(R.id.cardLayout);
        configSocketEvents();

        for(int j =1; j<=16; j++){   //card logic kısmı
            kartlar[j-1] = new myCard(this,j);
            final int k = j-1;
            kartlar[j-1].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    currentCard = (myCard) v; //1.kart
                    // the card is turned up
                    if (currentCard.cevrilebilir == false)    return;

                    currentCard.cevir();
                    updatesocketCard(k);


                    if(sonkart == 0) { //ilk kez çevrildi
                        sonkart =currentCard.getId();
                    }

                    // once card already is turned up
                    else {
                        lastCard = (myCard)findViewById(sonkart);
                        // same picture and same object --> same exact card: turn back
                        if (lastCard == currentCard) {
                            sonkart = 0;
                            return;
                        }
                        // same picture and different objects --> matched
                        if(lastCard.onplanID==currentCard.onplanID && lastCard.getId()!=currentCard.getId()){
                            lastCard.cevrilebilir=false;
                            currentCard.cevrilebilir=false;
                            score1++;

                            player1 = (TextView) findViewById(R.id.textView5);
                            player1.setText("my score : "+ score1);

                            JSONObject dataScore = new JSONObject();
                            try{
                                dataScore.put("score",score1);
                                socket.emit("scoreEvent",dataScore);
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            if((score1+score2)==8){ //oyun bitti
                                Intent i = new Intent(MainActivity.this,ResultActivity.class);
                                i.putExtra("puan",hata);
                                i.putExtra("score1",score1);
                                i.putExtra("score2",score2);
                                startActivity(i);
                            }
                            sonkart = 0;
                        }
                        else { //eşleşmediler geri çevir
                            Handler h =new Handler();
                            h.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    currentCard.cevir();
                                    updatesocketCard(k);

                                    lastCard.cevir();
                                    updatesocketCard(k);

                                }
                            },250);
                            hata++;

                            tv2 = (TextView) findViewById(R.id.textView6);
                            tv2.setText("Hata sayısı : "+ hata);
                            sonkart=0;
                        }

                    }

                }

            });
        }

       /* for(int j=0; j<16; j++){ //Shuffle
            int rg = (int)(Math.random()*16);
            myCard k = kartlar[rg];
            kartlar[rg]=kartlar[j];
            kartlar[j]= k;

        }*/

        for(int j=0; j<16; j++)
            cards.addView(kartlar[j]);

    }


    public void updatesocketCard(int id){

        JSONObject data = new JSONObject();
        try{
            data.put("onplanID",id);
            socket.emit("cevirEvent",data);

        }catch (Exception e){
            e.printStackTrace();
        }


    }


    public void connectSocket(){
        try{
            socket = IO.socket("http://10.0.2.2:3000");
            socket.connect();
            connectioncheck=true;
            socket.emit("join","Other player");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void configSocketEvents(/*final myCard eventcard*/) {
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("SocketIO", "Connected");


            }
        }).on("userjoinedthechat", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                final String data = (String) args[0];

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Toast.makeText(MainActivity.this,data,Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).on("socketID", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                final JSONObject data = (JSONObject) args[0];

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String id = data.getString("id");
                            Log.d("SocketIO", "My ID: " + id);
                        } catch (JSONException e) {
                            Log.d("SocketIO", "Error getting ID");

                        }
                    }
                });
            }
        }).on("playerDisconnected", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                final JSONObject data = (JSONObject) args[0];

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String id = data.getString("id");
                            Log.d("SocketIO", id+ "disconnected");
                        } catch (JSONException e) {
                            Log.d("SocketIO", "Error getting New Player ID");

                        }
                    }
                });
            }
        }).on("scoreEventhappen", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                final JSONObject data = (JSONObject) args[0];

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            player2= (TextView)findViewById(R.id.textView);
                            int score = data.getInt("score");

                            player2.setText("enemy score: "+score);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).on("cevirEventhappen", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                final JSONObject data = (JSONObject) args[0];

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            int onplanID = data.getInt("onplanID");

                            if (kartlar[onplanID].cevrilebilir == false)    return;
                            kartlar[onplanID].cevir();

                            if(sonkart == 0) { //ilk kez çevrildi
                                sonkart =kartlar[onplanID].getId();
                            }

                            else {
                                lastCard = (myCard)findViewById(sonkart);
                                // same picture and same object --> same exact card: turn back
                                if (lastCard == kartlar[onplanID]) {
                                    sonkart = 0;
                                    return;
                                }
                                // same picture and different objects --> matched
                                if(lastCard.onplanID == kartlar[onplanID].onplanID && lastCard.getId()!= kartlar[onplanID].getId()){
                                    lastCard.cevrilebilir=false;
                                    kartlar[onplanID].cevrilebilir=false;
                                    score2++;

                                    if((score1+score2)==8){ //oyun bitti
                                        Intent i = new Intent(MainActivity.this,ResultActivity.class);
                                        i.putExtra("puan",hata);
                                        i.putExtra("score1",score1);
                                        i.putExtra("score2",score2);
                                        startActivity(i);
                                    }
                                    sonkart = 0;
                                }
                                else { //eşleşmediler geri çevir

                                            kartlar[onplanID].cevir();

                                            lastCard.cevir();



                                    sonkart=0;
                                }

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }
}