package com.example.test2;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class CannonService {

    private int silence;
    private int cannon;
    private boolean volumeOn = true;
    private static SoundPool soundPool;

    private static CannonService instance;

    public static CannonService getInstance(Context context) {
        if (instance == null) {
            instance = new CannonService();
            instance.init(context);
        }

        return instance;
    }

    public void init(Context context) {
        if (soundPool == null) {
            soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 100);
            silence = soundPool.load(context, R.raw.silence_short, 1);
            cannon = soundPool.load(context, R.raw.cannon4, 1);
        }
    }

    public void close() {
        soundPool.release();
    }

    public void toggleVolume() {
        volumeOn = !volumeOn;
    }

    public boolean isVolumeOn() {
        return volumeOn;
    }

    public void playCannonSound() {
        try {
            soundPool.play(cannon, 0.5f, 0.5f, 1, 0, 1f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
