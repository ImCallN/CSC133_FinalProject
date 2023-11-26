package com.example.snakegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import java.util.Random;
public class items {
    private Point location = new Point();
   private Point mSpawnRange;
   private int mSize;
     Bitmap mBitmap;
        items(Context context, Point sr, int s) {
            mSpawnRange = sr;
            mSize = s;
            location.x = -10;
        }

        //spawn item
    void spawn(){
        // Choose two random values and place the apple
        Random random = new Random();
        location.x = random.nextInt(mSpawnRange.x + 10);
        location.y = random.nextInt(mSpawnRange.y - 10);
    }
    void draw(Canvas canvas, Paint paint){
        canvas.drawBitmap(mBitmap, location.x * mSize, location.y * mSize, paint);
    }
    //getters
    public Point getLoca() {
        return location;
    }
}
