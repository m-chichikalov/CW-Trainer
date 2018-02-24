package pro.mbed.cwtrainer.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import pro.mbed.cwtrainer.App;
import pro.mbed.cwtrainer.R;
import pro.mbed.cwtrainer.util.CwPlayer;
import pro.mbed.cwtrainer.util.KochMethod;

public class Main extends AppCompatActivity{

    @BindView(R.id.text_char_of_lesson) TextView lettersOfLesson;
    @BindView(R.id.edit_text) EditText editText;
    @BindView(R.id.fbPlayStop) FloatingActionButton fbPlay;
    @BindView(R.id.fbCheckResult) FloatingActionButton fbCheck;
    @Inject CwPlayer cw;
    @Inject KochMethod km;
    @Inject SharedPreferences sharedPref;

    double frequency, ratio, pause;
    int speed, lesson;
//  Noise noise;

    private static final String TAG = "mActCwTrainer";
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        App.getComponent().inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        updateLocalSettings();
        cw.reInit(speed, frequency, ratio, pause);
        cw.play();

        lettersOfLesson.setText(km.getCharactersInLesson());

        mainHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };

        fbCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                km.setUserRawStr(editText.getText().toString().toUpperCase());
                cw.stop();
                startActivity(new Intent(Main.this, ResultActivity.class));
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        fbPlay.setOnClickListener(setOnclickLestenerOnPlay());
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLocalSettings();
        cw.reInit(speed, frequency, ratio, pause);

        mainHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case CwPlayer.PLAY:
                        fbPlay.setOnClickListener(setOnclickLestenerOnPause());
                        fbPlay.setImageResource(android.R.drawable.ic_media_pause);
                        break;
                    case CwPlayer.STOP:
                        fbPlay.setOnClickListener(setOnclickLestenerOnPlay());
                        fbPlay.setImageResource(android.R.drawable.ic_media_play);
                        break;
                    case CwPlayer.PAUSE:
                        break;
                    default:
                        super.handleMessage(msg);
                }
            }
        };

        cw.setOuterUiHandler(mainHandler);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            cw.pause();
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateLocalSettings () {
        String defFrequency = getResources().getString(R.string.pref_freq_default);
        frequency = Double.parseDouble(sharedPref.getString(getString(R.string.pref_freq_key), defFrequency));

        String defSpeed = getResources().getString(R.string.pref_wpm_default);
        speed = Integer.parseInt(sharedPref
                .getString(getString(R.string.pref_wpm_key), defSpeed));

        String defLesson = getResources().getString(R.string.pref_lesson_default);
        lesson = Integer.parseInt(sharedPref
                .getString(getString(R.string.pref_lesson_key), defLesson));

        String defRatio = getResources().getString(R.string.pref_ratio_default);
        ratio = Double.parseDouble(sharedPref
                .getString(getString(R.string.pref_ratio_key), defRatio));

        String defPause = getResources().getString(R.string.pref_pause_default);
        pause = Double.parseDouble(sharedPref
                .getString(getString(R.string.pref_pause_key), defPause));
    }

    private View.OnClickListener setOnclickLestenerOnPlay () {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                km.generateExercise();
                cw.play();
                cw.playString(km.toString());
                lettersOfLesson.setText(km.toString());
            }
        };
    }

    private View.OnClickListener setOnclickLestenerOnPause () {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cw.stop();
            }
        };
    }
}










