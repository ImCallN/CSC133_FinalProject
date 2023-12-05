package com.example.snakegame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import kotlin.OverloadResolutionByLambdaReturnType;

public class DrawScreen{
    public Canvas myCanvas;
    public Paint myPaint;
    public SurfaceHolder mySurfaceHolder;
    public int myScore;

    public DrawScreen(Paint paint, SurfaceHolder surface, int score){
        this.myPaint = paint;
        this.mySurfaceHolder = surface;
        this.myScore = score;
    }

    public void setBackground(){
            // Background
            myCanvas.drawColor(Color.argb(255, 0, 0, 0));

            // Set color and main message text size
            myPaint.setColor(Color.argb(255, 255, 255, 255));
            myPaint.setTextSize(250);
    }
    public int calculateCenter(String text){
        float textWidth = myPaint.measureText(text);
        int textCenter = (int) ((myCanvas.getWidth()/2) - (textWidth/2));

        return textCenter;
    }


}