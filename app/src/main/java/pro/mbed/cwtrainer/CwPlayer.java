package pro.mbed.cwtrainer;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.audiofx.Equalizer;
import android.util.Log;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * Created by m.chichikalov@outlook.com
 *
 *  A .-    F ..-.  K -.-   P .--.  U ..-   Z --..    = -...-   / -..-.   + .-.-.
 *  B -...  G --.   L .-..  Q --.-  V ...-  . .-.-.-  : ---...  " .-..-.  @ .--.-.
 *  C -.-.  H ....  M --    R .-.   W .--   , --..--  ; -.-.-.  $ ...-.-
 *  D -..   I ..    N -.    S ...   X -..-  ? ..--..  ( -.--.   ' .----.
 *  E .     J .---  O ---   T -     Y -.--  - -....-  ) -.--.-  _ ..--.-
 *  0 ----- 5 .....
 *  1 .---- 6 -....
 *  2 ..--- 7 --...
 *  3 ...-- 8 ---..
 *  4 ....- 9 ----.
 *
 *
 *        T = 1200 / W
 * Where: T is the unit time, or dot duration in milliseconds, and W is the speed in wpm.
 * I am going to pregenerate ???
 * if we are using 44100 hz sampleRate, length of buffer should be ???
 */

public class CwPlayer {

    private int[] tableCW = {
            0x0000010, 0x0000000, 0x0045D5D, 0x0000000, // ' ', '!', '"', '#'
            0x00475D5, 0x0000000, 0x0000000, 0x045DDDD, // '$', '%', '&', "'"
            0x0045DD7, 0x0475DD7, 0x0000000, 0x001175D, // '(', ')', '*', '+'
            0x0477577, 0x0047557, 0x011D75D, 0x0011757, // ',', '-', '.', '/'
            0x0477777, 0x011DDDD, 0x0047775, 0x0011DD5, // '0', '1', '2', '3'
            0x0004755, 0x0001155, 0x0004557, 0x0011577, // '4', '5', '6', '7'
            0x0045777, 0x0117777, 0x0115777, 0x01175D7, // '8', '9', ':', ';'
            0x0000000, 0x0011D57, 0x0000000, 0x0045775, // '<', '=', '>', '?'
            0x01175DD, 0x000011D, 0x0001157, 0x00045D7, // '@', 'A', 'B', 'C'
            0x0000457, 0x0000011, 0x0001175, 0x0001177, // 'D', 'E', 'F', 'G'
            0x0000455, 0x0000045, 0x0011DDD, 0x00011D7, // 'H', 'I', 'J', 'K'
            0x000115D, 0x0000477, 0x0000117, 0x0004777, // 'L', 'M', 'N', 'O'
            0x00045DD, 0x0011D77, 0x000045D, 0x0000115, // 'P', 'Q', 'R', 'S'
            0x0000047, 0x0000475, 0x00011D5, 0x00011DD, // 'T', 'U', 'V', 'W'
            0x0004757, 0x0011DD7, 0x0004577}; 	        // 'X', 'Y', 'Z'

    private static final String TAG = "CwPlayer.class"; // for logging
    private static final int sampleRate = 44100; //Hz

    private boolean hasNoise = false;
    private double lengthOfDot; // 60 millesecond -> 20 wpm
    private double freqOfTon; // Hz

    private int numSamples;
    private int period;
    private int numberOfPeriods;

    private short samplePCM[];
    private byte  frontSnd[];
    private byte  middleSnd[];
    private byte  endSnd[];
    private byte  silentSnd[];

    private AudioTrack audioTrack;
    Queue<Byte> queue;
    Random rnd;

    public CwPlayer(int speedWPM, double freqOfTone) {
        freqOfTon = freqOfTone;
        lengthOfDot = 1200/speedWPM;
        period = sampleRate / (int) freqOfTon;
        numSamples = (int) ((lengthOfDot / 5) * (sampleRate/ 1000));

        numberOfPeriods = numSamples / period;
        numSamples = period * numberOfPeriods;

        samplePCM = new short[period];
        frontSnd  = new byte [2 * numSamples];
        middleSnd = new byte [2 * numSamples];
        endSnd    = new byte [2 * numSamples];
        silentSnd = new byte [2 * numSamples];

        queue = new LinkedList<>();

        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, (4 * numSamples),
                AudioTrack.MODE_STREAM);

        Equalizer equalizer = new Equalizer( 0, audioTrack.getAudioSessionId());

        short numberBands = equalizer.getNumberOfBands();
        short lowerLevel  = equalizer.getBandLevelRange()[0];
        short upperLevel  = equalizer.getBandLevelRange()[1];

        equalizer.setBandLevel((short)0, (short) 1500);
        equalizer.setBandLevel((short)1, (short) 1500);
        equalizer.setBandLevel((short)2, (short) 1500);
        equalizer.setBandLevel((short)3, (short)-1500);
        equalizer.setBandLevel((short)4, (short)-1500);

        equalizer.setEnabled(true);

        for (short i = 0; i < numberBands; i++) {
            Log.d(TAG, "Band " + numberBands + " central frequency is " +
                        equalizer.getCenterFreq(i));
        }

        audioTrack.attachAuxEffect(equalizer.getId());
        audioTrack.setAuxEffectSendLevel(1.0f);

        rnd = new Random();
        
        genSinToneSamples();
        generateFrontSnd();
        generateMiddleSnd();
        generateEndSnd();
        generateSilentSnd();

        startThread();
    }

    private void genSinToneSamples() {
        double[] sample = new double[period];
        for (int i = 0; i < period; ++i) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate/freqOfTon));
        }
        int idx = 0;
        // convert to 16 bit pcm sound array
        for (double dVal : sample) {
            short valShort = (short) (dVal * 32767);
            if (valShort > 10000) { valShort = 10000;}
            if (valShort < -10000) { valShort = -10000;}
            samplePCM[idx++] = valShort;
        }
    }

    private void generateMiddleSnd() {
        int idx = 0;
        for (int i = 0; i < numberOfPeriods; i++) {
            for (int j = 0; j < (period); j++) {
                middleSnd[idx++] = (byte)  (samplePCM[j] & 0x00ff);
                middleSnd[idx++] = (byte) ((samplePCM[j] & 0xff00) >>> 8);
            }
        }
    }

    private void generateFrontSnd() {
        double mult = 0.1;
        double step = 0.9 / numberOfPeriods;

        int idx = 0;
        for (int i = 0; i < numberOfPeriods; i++) {
            for (int j = 0; j < (period); j++) {
                short val = samplePCM[j];
                val = (short) (val * mult);
                frontSnd[idx++] = (byte)  (val & 0x00ff);
                frontSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
            }
            mult = mult + step;
        }
    }

    private void generateEndSnd() {
        double mult = 1.0;
        double step = 0.9 / numberOfPeriods;

        int idx = 0;
        for (int i = 0; i < numberOfPeriods; i++) {
            for (int j = 0; j < (period); j++) {
                short val = samplePCM[j];
                val = (short) (val * mult);
                endSnd[idx++] = (byte)  (val & 0x00ff);
                endSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
            }
            mult = mult - step;
        }
    }

    private void generateSilentSnd() {
        int idx = 0;
        for (int i = 0; i < numberOfPeriods; i++) {
            for (int j = 0; j < (2 * period); j++) {
                silentSnd[idx++] = 0;
            }
        }
    }

    public void play() {
        audioTrack.play();
    }


    public void pause() {
//        if (audioTrack.getPlayState() == audioTrack.PLAYSTATE_PLAYING) {
            audioTrack.pause();
//        }
    }

    public void feed(byte charFeed) {
        queue.add(charFeed);
    }


    private void startThread() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    for (;;) {
                        if (!queue.isEmpty()) {
                            // remove character from Queue, decode and feed audio buffer.
                            int unDecodedCW = getUnDecodedCW(queue.remove());
                            int test = (0x00000003 & unDecodedCW);
                            while (test != 0) {
                                switch (test){
                                    case 1:
                                        unDecodedCW = (unDecodedCW >> 2);
                                        test = (0x00000003 & unDecodedCW);
                                        playDot();
                                        playPauseBetweenDots();
                                        Log.d(TAG, "Dot   .");
                                        break;
                                    case 3:
                                        unDecodedCW = (unDecodedCW >> 4);
                                        test = (0x00000003 & unDecodedCW);
                                        playDash();
                                        playPauseBetweenDots();
                                        Log.d(TAG, "Dash  -");
                                        break;
                                }
                            }
                            playPauseBetweenWords();
                            Log.d(TAG, "Pause  ");
                        }
                    }
                } catch (Exception e) { }
            }
        }).start();
    }
    
    private int getUnDecodedCW(byte ch) {
        byte ch_ = ch;
        if (ch_ >= 97 && ch_ <=122) {
            ch_ = (byte) (ch_ - 32);
        } else if (ch_ >= 32 && ch_ <= 90) {
            Log.d(TAG, "Query int - " + tableCW[ch_ - 32] + "from table.");
            return tableCW[ch_ - 32];
        }
        Log.d(TAG, "Error recognize the character.");
        return 0;
    }

    private void playDot (){
        audioTrack.write(frontSnd, 0, (numSamples * 2));
        for (int i = 0; i < 3; i++) {
            audioTrack.write(middleSnd, 0, (numSamples * 2));
        }
        audioTrack.write(endSnd, 0, (numSamples * 2));
    }

    private void playDash (){
        audioTrack.write(frontSnd, 0, (numSamples * 2));
        for (int i = 0; i < 13; i++) {
            audioTrack.write(middleSnd, 0, (numSamples * 2));
        }
        audioTrack.write(endSnd, 0, (numSamples * 2));
    }

    private void playPauseBetweenDots (){
        for (int i = 0; i < 5; i++) {
            audioTrack.write(silentSnd, 0, (numSamples * 2));
        }
    }

    private void playPauseBetweenWords (){
        for (int i = 0; i < 30; i++) {
            audioTrack.write(silentSnd, 0, (numSamples * 2));
        }
    }

    public void test() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    int i;
                    audioTrack.play();
                    audioTrack.flush();
                    for (int j =0; j < 20; j ++) {

                    }
                    audioTrack.stop();
                } catch (Exception e) { }
            }
        }).start();
    }


}
