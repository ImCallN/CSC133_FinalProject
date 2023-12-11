package com.example.snakegame;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
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
    private GoldenApple gApple;

    //obstacle
    private Blocker stick;
    private int direct;

    //Screens
    private DrawTitle myTitle;
    private DrawGameOver myGameOver;
    private DrawPauseScreen myPaused;
    private DrawHighScoreScreen myHighScore;

    private SnakeObserver snakeObs;
    private Poison trap;


    //Which screen should be displayed
    private boolean mNewGame = true;
    private boolean mGameOver = false;
    private boolean onTitle = true;
    private boolean onScore = false;

    //pause button
    Bitmap myBitmapPauseButton;
    Rect pauseButton;


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

        // Call the constructors of our objects
        mApple = new Apple(context, new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh),blockSize);

        gApple = new GoldenApple(context, new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh), blockSize);
        mSnake = Snake.getInstance();
        mSnake.setBitMaps(context, new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh),blockSize);
        snakeObs = new SnakeObserver(new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh));
        trap = new Poison(context, new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh), blockSize);

        stick = new Blocker(context,new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh),blockSize);
        direct=1;

        //Initialize pause button rect
        pauseButton = new Rect(0,0,0,0);

        //Initialize screens
        myTitle = new DrawTitle(context,mPaint,mSurfaceHolder);
        myGameOver = new DrawGameOver(mPaint, mSurfaceHolder, mScore);
        myPaused = new DrawPauseScreen(mPaint,mSurfaceHolder,mScore);
        myHighScore = new DrawHighScoreScreen(context,mPaint,mSurfaceHolder);

    }
    // Called to start a new game
    public void newGame() {

        // reset the snake
        snakeObs.spawnSnake(mSnake, NUM_BLOCKS_WIDE, mNumBlocksHigh);
        // Get the apple ready for dinner
        mApple.spawn();
        stick.spawn();
        trap.spawn();

        // Reset the mScore
        mScore = 0;

        //Start background music
        audx.getMusic().start();

        // Setup mNextFrameTime so an update can triggered
        mNextFrameTime = System.currentTimeMillis();

    }


    // Handles the game loop
    @Override
    public void run() {
        while (mPlaying) {
            if(!mNewGame) {
                // Update 10 times a second
                if (updateRequired()) {
                    update();
                }
            }

            //Determine what to display
            if(mNewGame) {
                if(!onScore)
                    myTitle.title();
                else
                    myHighScore.highScoreScreen();
            }
            else if(mGameOver) {
                myGameOver.setScore(mScore);
                myGameOver.gameOver();
            }
            else
                drawGame();
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
    int direction = 1;

    boolean gAppleSpawn = true;
    // Update all the game objects
    public void update() {
        // Move the snake
        snakeObs.moveSnake(mSnake);

        //Blocker movement
        if (stick.getLoca().x == NUM_BLOCKS_WIDE - 3) {
            direct = -1;
        } else if (stick.getLoca().x == 1) {
            direct = 1;
        }
        stick.move(direct);

        if(gAppleSpawn && mScore % 5 == 0 && mScore != 0)
        {
            gApple.spawn();
            gAppleSpawn = false;
        }
        // Did the head of the snake eat the apple?\
        if(snakeObs.detectCollision(mSnake, mApple)){
            // This reminds me of Edge of Tomorrow.
            // One day the apple will be ready!
            snakeObs.growSnake(mSnake, 1);
            mApple.spawn();
            stick.spawn();
            trap.spawn();
            // Add to  mScore
            mScore++;
            // Play a sound
            audx.getSoundPool().play(audx.getmEat_ID(), 1, 1, 0, 0, 1);
        }
        if(snakeObs.detectCollision(mSnake, gApple))
        {
            gAppleSpawn = true;
            gApple.setLocation(new Point(-10, -10));
            snakeObs.growSnake(mSnake, 4);
            mScore+=4;
        }
        if(snakeObs.detectCollision(mSnake, trap))
        {

            if(mScore <= 1)
            {
                audx.getSoundPool().play(audx.getmCrashID(), 1, 1, 0, 0, 1);
                mSnake.reset(NUM_BLOCKS_WIDE, mNumBlocksHigh);
                audx.getMusic().pause();
                mScore = 0;
                mGameOver = true;
            }

            else
            {
                mScore -= 2;
                //audx.getMusic().play();
                snakeObs.cutSnake(mSnake,2);
                trap.spawn();
            }

        }

        // snake dead?
        if (snakeObs.detectCollision(mSnake)|| snakeObs.detectCollision(mSnake,stick)|| snakeObs.detectTailCollision(mSnake,stick)){
            // Pause the game ready to start again
            audx.getSoundPool().play(audx.getmCrashID(), 1, 1, 0, 0, 1);
            audx.getMusic().pause();
            mGameOver = true;
        }
    }

    // Do all the drawing
    public void drawGame() {
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

            //Draw Pause Button Rect
            int width = 120;
            int padding = 50;
            pauseButton = new Rect(mCanvas.getWidth()-(width+padding),padding,mCanvas.getWidth()-padding,width + padding);

            //Draw Pause Button Image
            myBitmapPauseButton = BitmapFactory.decodeResource(getResources(),R.drawable.pausebutton);
            myBitmapPauseButton = Bitmap.createScaledBitmap(myBitmapPauseButton,width,width,true);
            mCanvas.drawBitmap(myBitmapPauseButton,mCanvas.getWidth()-(width+padding),padding,mPaint);

            // Draw
            mApple.draw(mCanvas, mPaint);
            mSnake.draw(mCanvas, mPaint);
            stick.draw(mCanvas, mPaint);
            gApple.draw(mCanvas, mPaint);
            trap.draw(mCanvas, mPaint);

            // Unlock the mCanvas and reveal the graphics for this frame
            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                int i = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO) {
                    i = motionEvent.getActionIndex();
                }
                int x = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ECLAIR) {
                    x = (int) motionEvent.getX(i);
                }
                int y = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ECLAIR) {
                    y = (int) motionEvent.getY(i);
                }

                if(pauseButton.contains(x,y) && !mNewGame){//checks if user tapped within pause Rect
                    pause();
                    // Don't want to process snake direction for this tap
                    return true;
                }
                else if(myTitle.getPlayRect().contains(x,y) && onTitle){
                    onTitle = false;
                    mNewGame = false;
                    newGame();
                    return true;
                }
                else if(myTitle.getScoreRect().contains(x,y) && onTitle){
                    onTitle=false;
                    onScore = true;
                    return true;
                }
                else if(myHighScore.getBackRect().contains(x,y) && onScore){
                    onScore = false;
                    onTitle = true;
                    return true;
                }
                else if(!mPlaying){
                    resume();
                    return true;
                }
                else if(mGameOver){
                    onTitle = true;
                    mNewGame = true;
                    mGameOver = false;
                    return true;
                }
                else if(!mNewGame && !onScore) {
                    // Let the Snake class handle control input
                    snakeObs.switchHeading(mSnake, motionEvent);
                    break;
                }
            default:
                break;
        }
        return true;
    }


    // Stop the thread
    public void pause() {
        mPlaying = false;

        //Draw Pause Screen
        myPaused.setScore(mScore);
        myPaused.pause();

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