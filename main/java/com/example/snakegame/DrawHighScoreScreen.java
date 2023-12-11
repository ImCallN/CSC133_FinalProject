package com.example.snakegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;

public class DrawHighScoreScreen extends DrawScreen{
    private Bitmap myBitmapBackButton;
    private Rect backButton = new Rect(0,0,0,0);
    public DrawHighScoreScreen(Context context, Paint paint, SurfaceHolder surface) {
        super(context,paint, surface);
    }

    public void highScoreScreen(){
        // Get a lock on the mCanvas
        if (mySurfaceHolder.getSurface().isValid()) {
            myCanvas = mySurfaceHolder.lockCanvas();

            //Set initial values for background color and text color
            setBackground();

            //Draw Pause Button Rect
            int width = 150;
            int height = 100;
            int padding = 50;

            backButton = new Rect(padding,padding,width+padding,height + padding);

            //Draw Pause Button Image
            myBitmapBackButton = BitmapFactory.decodeResource(context.getResources(),R.drawable.backbutton);
            myBitmapBackButton = Bitmap.createScaledBitmap(myBitmapBackButton,width,height,true);
            myCanvas.drawBitmap(myBitmapBackButton,padding,padding,myPaint);

            // Draw Leaderboard Title
            myPaint.setTextSize(150);
            int titleCenter = calculateCenter("Leaderboard");
            myCanvas.drawText("Leaderboard", titleCenter, 180, myPaint);

            myPaint.setTextSize(100);
            int subTitleCenter = calculateCenter("Top 5 Scores");
            myCanvas.drawText("Top 5 Scores", subTitleCenter, 300,myPaint);

            //Draw HighScores already formatted
            drawLeaderBoard();
            
            // Unlock the mCanvas and reveal the graphics for this frame
            mySurfaceHolder.unlockCanvasAndPost(myCanvas);
        }
    }

    private void drawLeaderBoard(){
        myPaint.setTextSize(80);
        for( int i = 1; i < 6; i++){
            //output is just a placeholder
            int output = 20 - i;
            String line = String.format("%30s %15s",i + ".", output);
            myCanvas.drawText(line,0,320+(i*130),myPaint);
        }
    }

    public Rect getBackRect(){
        return backButton;
    }
}
