package com.example.snakegame;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

public class DrawGameOver extends DrawScreen{
    public DrawGameOver(Paint paint, SurfaceHolder surface, int score) {
        super(paint, surface, score);
    }

    public void gameOver(){
        // Get a lock on the mCanvas
        if (mySurfaceHolder.getSurface().isValid()) {
            myCanvas = mySurfaceHolder.lockCanvas();

            //Set initial values for background color and text color
            setBackground();

            // Draw Game Over message
            int gameOverCenter = calculateCenter("Game Over");
            myCanvas.drawText("Game Over", gameOverCenter, 400, myPaint);

            //Draw Final Score message
            myPaint.setTextSize(120);
            int finalCenter = calculateCenter("Final Score: " + myScore);
            myCanvas.drawText("Final Score: " + myScore, finalCenter,525, myPaint);

            //Draw Restart message
            int restartCenter = calculateCenter("Tap to Restart");
            myCanvas.drawText("Tap to Restart", restartCenter, 750, myPaint);

            // Unlock the mCanvas and reveal the graphics for this frame
            mySurfaceHolder.unlockCanvasAndPost(myCanvas);
        }
    }
}
