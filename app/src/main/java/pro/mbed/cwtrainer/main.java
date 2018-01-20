package pro.mbed.cwtrainer;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class main extends AppCompatActivity implements View.OnClickListener{

    Button btStart, btPause, btFeed;

    CwPlayer cw;
    CwPlayer cw2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        cw = new CwPlayer(18, 840.0);
        cw2 = new CwPlayer(30, 640.0);

        btPause = findViewById(R.id.button_pause);
        btPause.setOnClickListener(this);
        btStart = findViewById(R.id.button_start);
        btStart.setOnClickListener(this);
        btFeed = findViewById(R.id.button_feed);
        btFeed.setOnClickListener(this);

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_start: {
                cw.test();
                cw2.test();
                break;
            }
            case R.id.button_pause: {
                cw.pause();
                break;
            }
            case R.id.button_feed: {
                cw2.play();
                cw2.feed((byte) 32);

                for (int i = 65; i < 91; i++) {
                    cw2.feed((byte) i);
                }
                break;
            }
        }
    }
}




