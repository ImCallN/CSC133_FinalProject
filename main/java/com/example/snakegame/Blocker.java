package com.example.snakegame;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.Random;

public class Blocker extends items {
    Blocker(Context t, Point p, int size){
        super(t,p,size);
        mBitmap = BitmapFactory.decodeResource(t.getResources(),R.drawable.engi);
        mBitmap = Bitmap.createScaledBitmap(mBitmap,size,size,false);
    }
    public void move(int direction)
    {
        getLoca().x += direction;
    }
}
