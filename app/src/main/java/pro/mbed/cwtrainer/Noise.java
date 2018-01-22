package pro.mbed.cwtrainer;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import java.util.Random;

/**
 * Created by m.chichikalov@outlook.com on 20.01.2018.
 */

public class Noise {

    private static final int sampleRate = 44100; //Hz
    private static final int numSamples = 150;

    private  byte[] noiseSnd = new byte [2 * numSamples];
    private double volume = 1.0;
    AudioTrack audioTrack;
    Random rnd;
    Thread noiseThread;

    public Noise () {
        rnd = new Random();
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, (2 * numSamples),
                AudioTrack.MODE_STREAM);

        noiseThread = new Thread(new Runnable() {
            public void run() {
                try {
                    while (!noiseThread.isInterrupted()) {
                        generateNoise();
                        audioTrack.write(noiseSnd, 0, noiseSnd.length);
                    }
                    return;
                } catch (Exception e) {
                    return;
                }
            }
        });
        noiseThread.start();

    }


    private void generateNoise () {
        int idx = 0;
        for (int i = 0; i < numSamples; i++) {
            double dVal = rnd.nextDouble()/32;
            short sample = (short) (dVal * 32767);
            noiseSnd[idx++] = (byte) (sample & 0x00ff);
            noiseSnd[idx++] = (byte) ((sample & 0xff00) >>> 8);
        }
    }

    public void play() {
        audioTrack.flush();
        audioTrack.play();
    }

    public void pause() {
//        if (noiseThread != null) {
//            if (noiseThread.getState() == Thread.State.RUNNABLE) {
//                noiseThread.interrupt();
//            }
//        }
        audioTrack.pause();
        audioTrack.flush();
//        audioTrack.release();
    }

}
