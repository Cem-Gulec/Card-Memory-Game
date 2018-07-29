package com.example.kartoyun.myapplication;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatDrawableManager;


public class myCard extends AppCompatButton {

    boolean isFlipped= false;
    boolean cevrilebilir=true;
    int arkaplanID;
    int onplanID=0;
    Drawable on;
    Drawable arka;
    Context cardContext;

    public myCard(Context c, int id){
        super(c);
        setId(id);

        arkaplanID = R.drawable.cardback1;


        if(id %8 == 1)
            onplanID= R.drawable.kart1;
        if(id %8 == 2)
            onplanID= R.drawable.kart2;
        if(id %8 == 3)
            onplanID= R.drawable.kart3;
        if(id %8 == 4)
            onplanID= R.drawable.kart4;
        if(id %8 == 5)
            onplanID= R.drawable.kart5;
        if(id %8 == 6)
            onplanID= R.drawable.kart6;
        if(id %8 == 7)
            onplanID= R.drawable.kart7;
        if(id %8 == 0)
            onplanID= R.drawable.kart8;


        arka = AppCompatDrawableManager.get().getDrawable(c,arkaplanID);
        on = AppCompatDrawableManager.get().getDrawable(c, onplanID);

        this.setBackground(arka);

        cardContext=c;

    }

    public Context getCardContext(){
        return cardContext;
    }

    public void cevir(){
        if(cevrilebilir){
            if(!isFlipped){
                setBackground(on);
                isFlipped=true;
            }
            else{
                setBackground(arka);
                isFlipped=false;
            }
        }

    }

}
