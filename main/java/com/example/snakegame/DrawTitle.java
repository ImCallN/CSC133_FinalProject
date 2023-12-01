package com.example.snakegame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

public class DrawTitle extends DrawScreen {
    public DrawTitle(Paint paint, SurfaceHolder surface, int score) {
        super(paint, surface,score);
    }

    public void title() {
        // Get a lock on the mCanvas
        if (mySurfaceHolder.getSurface().isValid()) {
            myCanvas = mySurfaceHolder.lockCanvas();

            //Set initial values for background color and text color
            setBackground();

            // Draw Game Title
            int titleCenter = calculateCenter("Snake Game!");
            myCanvas.drawText("Snake Game!", titleCenter, 620, myPaint);

            //Draw Tap to Play message
            myPaint.setTextSize(140);
            int playCenter = calculateCenter("Tap to Play");
            myCanvas.drawText("Tap to Play", playCenter, 750, myPaint);

            // Unlock the mCanvas and reveal the graphics for this frame
            mySurfaceHolder.unlockCanvasAndPost(myCanvas);
        }
    }

}
