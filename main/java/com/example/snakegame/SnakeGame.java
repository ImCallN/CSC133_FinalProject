package com.example.snakegame;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.io.IOException;

class SnakeGame extends SurfaceView implements Runnable{

    // Objects for the game loop/thread
    private Thread mThread = null;
    // Control pausing between updates
    private long mNextFrameTime;
    // Is the game currently playing and or paused?
    private volatile boolean mPlaying = false;
    private volatile boolean mPaused = true;

    // for playing sound effects
   private Audio audx;

    // The size in segments of the playable area
    private final int NUM_BLOCKS_WIDE = 40;
    private int mNumBlocksHigh;

    // How many points does the player have
    private int mScore;

    // Objects for drawing
    private Canvas mCanvas;
    private SurfaceHolder mSurfaceHolder;
    private Paint mPaint;

    //snake
    private Snake mSnake;
    //apple
    private Apple mApple;

    //obstacle
    private Blocker stick;


    // This is the constructor method that gets called
    // from SnakeActivity
    public SnakeGame(Context context, Point size) {
        super(context);

        // Work out how many pixels each block is
        int blockSize = size.x / NUM_BLOCKS_WIDE;
        // How many blocks of the same size will fit into the height
        mNumBlocksHigh = size.y / blockSize;

        audx = new Audio(context);

        // Initialize the drawing objects
        mSurfaceHolder = getHolder();
        mPaint = new Paint();

        // Call the constructors of our two game objects
        mApple = new Apple(context, new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh),blockSize);
        mSnake = Snake.getInstance();
        mSnake.setBitMaps(context, new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh),blockSize);
        stick = new Blocker(context,new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh),blockSize);

    }
    // Called to start a new game
    public void newGame() {

        // reset the snake
        mSnake.reset(NUM_BLOCKS_WIDE, mNumBlocksHigh);

        // Get the apple ready for dinner
        mApple.spawn();
        stick.spawn();

        // Reset the mScore
        mScore = 0;

        audx.getMusic().start();

        // Setup mNextFrameTime so an update can triggered
        mNextFrameTime = System.currentTimeMillis();

    }


    // Handles the game loop
    @Override
    public void run() {
        while (mPlaying) {
            if(!mPaused) {
                // Update 10 times a second
                if (updateRequired()) {
                    update();
                }
            }
            title();
        }
    }


    // Check to see if it is time for an update
    public boolean updateRequired() {
        // Run at 10 frames per second
        final long TARGET_FPS = 10;
        // There are 1000 milliseconds in a second
        final long MILLIS_PER_SECOND = 1000;

        // Are we due to update the frame
        if(mNextFrameTime <= System.currentTimeMillis()){
            // Tenth of a second has passed

            // Setup when the next update will be triggered
            mNextFrameTime =System.currentTimeMillis()
                    + MILLIS_PER_SECOND / TARGET_FPS;

            // Return true so that the update and draw
            // methods are executed
            return true;
        }
        return false;
    }

    //GAMEPLAY

    // Update all the game objects
    public void update() {
        // Move the snake
        mSnake.move();

        // Did the head of the snake eat the apple?\
        if(mSnake.SnakeBody(mApple.getLoca())){
            // This reminds me of Edge of Tomorrow.
            // One day the apple will be ready!
            mApple.spawn();
            stick.spawn();
            // Add to  mScore
            mScore = ++;
            // Play a sound
            audx.getSoundPool().play(audx.getmEat_ID(), 1, 1, 0, 0, 1);
        }

        // snake dead?
        if (mSnake.detectDeath() || mSnake.SnakeBody(stick.getLoca())) {
            // Pause the game ready to start again
            audx.getSoundPool().play(audx.getmCrashID(), 1, 1, 0, 0, 1);
            audx.getMusic().pause();
            mPaused = true;
        }

    }


    // Do all the drawing
    public void title() {
        // Get a lock on the mCanvas
        if (mSurfaceHolder.getSurface().isValid()) {
            mCanvas = mSurfaceHolder.lockCanvas();

            // Background
            mCanvas.drawColor(Color.argb(255, 0, 0, 0));


            // Set the size and color of the mPaint for the text
            mPaint.setColor(Color.argb(255, 255, 255, 255));
            mPaint.setTextSize(60);

            // Draw the score
            mCanvas.drawText("Score: " + mScore, 20, 120, mPaint);

            // Draw
            mApple.draw(mCanvas, mPaint);
            mSnake.draw(mCanvas, mPaint);
            stick.draw(mCanvas, mPaint);

            // Draw some text while paused
            if(mPaused){
                // Set the size and color of the mPaint for the text
                mPaint.setColor(Color.argb(255, 255, 255, 255));
                mPaint.setTextSize(250);

                // Draw the message
                // We will give this an international upgrade soon
                mCanvas.drawText("Snake Game!", 450, 620, mPaint);

                mPaint.setColor(Color.argb(255, 255, 255, 255));
                mPaint.setTextSize(140);

                mCanvas.drawText("Tap to Play", 500, 750, mPaint);
            }


            // Unlock the mCanvas and reveal the graphics for this frame
            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                if (mPaused) {
                    mPaused = false;
                    newGame();
                    // Don't want to process snake direction for this tap
                    return true;
                }
                // Let the Snake class handle control input
                mSnake.switchHeading(motionEvent);
                break;

            default:
                break;
        }
        return true;
    }


    // Stop the thread
    public void pause() {
        mPlaying = false;
        try {
            mThread.join();
        } catch (InterruptedException e) {
            // Error
        }
    }


    // Start the thread
    public void resume() {
        mPlaying = true;
        mThread = new Thread(this);
        mThread.start();
    }
}
