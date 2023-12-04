package com.example.snakegame;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

public class DrawPauseScreen extends DrawScreen{
    public DrawPauseScreen(Paint paint, SurfaceHolder surface,int score) {
        super(paint, surface,score);
    }

    public void pause(){
        // Get a lock on the myCanvas
        if (mySurfaceHolder.getSurface().isValid()) {
            myCanvas = mySurfaceHolder.lockCanvas();

            //Set initial values for background color and text color
            setBackground();

            // Draw Game Paused text
            int gameOverCenter = calculateCenter("Game Paused");
            myCanvas.drawText("Game Paused", gameOverCenter, 400, myPaint);

            //Draw Current Score
            myPaint.setTextSize(120);
            int currentScoreCenter = calculateCenter("Current Score: " + myScore);
            myCanvas.drawText("Current Score: " + myScore, currentScoreCenter, 525,myPaint);

            //Draw Resume text
            int restartCenter = calculateCenter("Tap to Resume");
            myCanvas.drawText("Tap to Resume", restartCenter, 750, myPaint);

            // Unlock the mCanvas and reveal the graphics for this frame
            mySurfaceHolder.unlockCanvasAndPost(myCanvas);
        }
    }

    public void setScore(int score){
        myScore = score;
    }
}
