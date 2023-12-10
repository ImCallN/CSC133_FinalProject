package com.example.snakegame;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Point;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;

import java.io.IOException;

public class Audio {

    private SoundPool mSP;
    private MediaPlayer bgm;
    private int mEat_ID;
    private int mCrashID;

    private int ochID;

    //private int soundfile[]; //might be used for storing audio

    Audio(Context context) {
        // Initialize the SoundPool
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            mSP = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            mSP = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }
        try {
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            // Prepare the sounds in memory
            descriptor = assetManager.openFd("get_apple.ogg");
            mEat_ID = mSP.load(descriptor, 0);

            descriptor = assetManager.openFd("snake_death.ogg");
            mCrashID = mSP.load(descriptor, 0);

            descriptor = assetManager.openFd("Minecraft-sound.pm3");
            ochID = mSP.load(descriptor, 0);

        } catch (IOException e) {
            // Error
        }

        bgm = new MediaPlayer();
        bgm = MediaPlayer.create(context, R.raw.elevator_music);
        bgm.setVolume(1,1);

    }
    //Methods
    public SoundPool getSoundPool() {
        return mSP;
    }
    public MediaPlayer getMusic() {return bgm;
    }
    public int getmCrashID() {
        return mCrashID;
    }
    public int getmEat_ID() {
        return mEat_ID;
    }
    public int getOchID() {return ochID;}
}

