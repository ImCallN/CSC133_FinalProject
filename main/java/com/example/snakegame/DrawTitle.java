package com.example.snakegame;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.SurfaceHolder;

public class DrawTitle extends DrawScreen {
    private Bitmap myBitmapPlayButton;
    private Bitmap myBitmapScoreButton;
    private Rect playButton = new Rect(0,0,0,0);
    private Rect highscoreButton = new Rect(0,0,0,0);
    public DrawTitle(Context context, Paint paint, SurfaceHolder surface) {
        super(context,paint, surface);
    }

    public void title() {
        // Get a lock on the mCanvas
        if (mySurfaceHolder.getSurface().isValid()) {
            myCanvas = mySurfaceHolder.lockCanvas();

            //Set initial values for background color and text color
            setBackground();

            //Draw play button
            int width = 300;
            int padding = 100;
            int screenWidth = myCanvas.getWidth();
            int screenHeight = myCanvas.getHeight();
            playButton = new Rect(screenWidth/2-(width+padding),screenHeight/2+padding,screenWidth/2-padding,screenHeight/2 +width + padding);

            //Draw Play Button Image
            myBitmapPlayButton = BitmapFactory.decodeResource(context.getResources(),R.drawable.playbutton);
            myBitmapPlayButton = Bitmap.createScaledBitmap(myBitmapPlayButton,width,width,true);
            myCanvas.drawBitmap(myBitmapPlayButton,screenWidth/2-(width+padding),screenHeight/2+padding,myPaint);

            //Draw leaderboard button
            highscoreButton = new Rect(screenWidth/2+padding,screenHeight/2+padding,screenWidth/2+(width+ padding),screenHeight/2 +width + padding);

            //Draw Leaderboard Button Image
            myBitmapScoreButton = BitmapFactory.decodeResource(context.getResources(),R.drawable.scoresbutton);
            myBitmapScoreButton = Bitmap.createScaledBitmap(myBitmapScoreButton,width,width,true);
            myCanvas.drawBitmap(myBitmapScoreButton,screenWidth/2+padding,screenHeight/2+padding,myPaint);


            // Draw Game Title
            int titleCenter = calculateCenter("Snake Game!");
            myCanvas.drawText("Snake Game!", titleCenter, 420, myPaint);

            // Unlock the mCanvas and reveal the graphics for this frame
            mySurfaceHolder.unlockCanvasAndPost(myCanvas);
        }
    }

    public Rect getPlayRect(){
        return playButton;
    }

    public Rect getScoreRect(){
        return highscoreButton;
    }

}
