package pro.mbed.cwtrainer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

public class main extends AppCompatActivity implements View.OnClickListener{

    Button btStart, btPause, btFeed;
    EditText editText;

    CwPlayer cw;
//    CwPlayer cw2;
    Noise noise;

    String TAG = "mainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // set default value in settings
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);

        editText = findViewById(R.id.edit_text);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String defFrequency = getResources().getString(R.string.pref_freq_default);
        Double frequency = Double.parseDouble(sharedPref.getString(getString(R.string.pref_freq_key), defFrequency));


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cw = new CwPlayer(33, frequency, 3.0, 3.0);
        noise = new Noise();

        KochMethod km = new KochMethod(this);
        editText.setText(km.getCharactersInLesson());
        cw.play();
        km.generateExercise(cw);

        btPause = findViewById(R.id.button_pause);
        btPause.setOnClickListener(this);
        btStart = findViewById(R.id.button_start);
        btStart.setOnClickListener(this);
        btFeed = findViewById(R.id.button_feed);
        btFeed.setOnClickListener(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "KeyEvent is - " + event );
        Log.d(TAG, "keyCode is - " + keyCode );
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
    protected void onResume() {
        noise.play();
        super.onResume();
    }


    @Override
    protected void onPause() {
        noise.pause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        noise.stop();
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_start: {


                break;
            }
            case R.id.button_pause: {
//                cw2.pause();
                cw.cleanBuffer();
                break;
            }
            case R.id.button_feed: {

                cw.play();

                for (int i = 65; i < 91; i++) {
                    cw.feed((byte) i);
                }
//                cw.pause();

                break;
            }
        }
    }
}




