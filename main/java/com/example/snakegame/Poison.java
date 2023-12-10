package com.example.snakegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

public class Poison extends items{
    Poison(Context context, Point danger, int size){
        super(context, danger, size);
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.trap);
        // Resize the bitmap
        mBitmap = Bitmap.createScaledBitmap(mBitmap, size, size, false);
    }

}
