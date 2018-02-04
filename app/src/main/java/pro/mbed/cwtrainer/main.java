package pro.mbed.cwtrainer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

public class main extends AppCompatActivity implements View.OnClickListener{

    Button btStart, btPause, btFeed;
    TextView lettersOfLesson;
    EditText editText;
    CwPlayer cw = null;
    KochMethod km = null;
    SharedPreferences sharedPref;
    double frequency, ratio, pause;
    int speed, lesson;
//  Noise noise;

    private static final String TAG = "mActCwTrainer";
    static Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // set default value in settings
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
//      noise = new Noise();

        HashMap<String, Object> savedValues =
                (HashMap<String, Object>) this.getLastCustomNonConfigurationInstance();
        updateLocalSettings();

        if (savedValues == null) {
            cw = new CwPlayer(speed, frequency, ratio, pause);
            km = new KochMethod();
            cw.play();
        } else {
            cw = (CwPlayer) savedValues.get("cw");
            km = (KochMethod) savedValues.get("km");
        }


        editText        = findViewById(R.id.edit_text);
        lettersOfLesson = findViewById(R.id.text_char_of_lesson);

        lettersOfLesson.setText(km.getCharactersInLesson());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        btPause = findViewById(R.id.button_pause);
        btPause.setOnClickListener(this);
        btStart = findViewById(R.id.button_start);
        btStart.setOnClickListener(this);
        btFeed = findViewById(R.id.button_feed);
        btFeed.setOnClickListener(this);

        mainHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLocalSettings();
        cw.reInit(speed, frequency, ratio, pause);
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
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_start: {
//                cw.play();
//                km.generateExercise(cw);
                break;
            }
            case R.id.button_pause:
//                mainHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        cw.play();
//                        cw.feed("rv9yw");
//                    }
//                }, 1000);
//              cw.pause();
//                cw.cleanBuffer();
                break;
            case R.id.button_feed: {
//                cw.play();
//                for (int i = 65; i < 91; i++) {
//                    cw.feed((byte) i);
//                }
//              cw.pause();
                break;
            }
        }
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        HashMap<String, Object> savedValues = new HashMap<>();
        savedValues.put("cw", cw);
        savedValues.put("km", km);
        return savedValues;
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

}




