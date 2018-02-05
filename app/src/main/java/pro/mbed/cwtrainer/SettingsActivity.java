package pro.mbed.cwtrainer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class SettingsActivity extends AppCompatPreferenceActivity {

    private final String TAG = this.getClass().getSimpleName();

    CwPlayer cw;
    Button bt;
    EditTextPreference speed, call, pause;
    ListPreference ratio, frequency, lesson;

    private Handler settingsUiHandler;

    public SettingsActivity() {
        super();
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        speed     = (EditTextPreference) findPreference(getString(R.string.pref_wpm_key));
        call      = (EditTextPreference) findPreference(getString(R.string.pref_call_key));
        pause     = (EditTextPreference) findPreference(getString(R.string.pref_pause_key));
        ratio     = (ListPreference)     findPreference(getString(R.string.pref_ratio_key));
        frequency = (ListPreference)     findPreference(getString(R.string.pref_freq_key));
        lesson    = (ListPreference)     findPreference(getString(R.string.pref_lesson_key));

        Log.d(TAG, "SettingsActivity was created, hash: " + this.hashCode());


        bt = new Button(this);
        bt.setText(R.string.bt_play_text);

        bt.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bt.getText().toString() == getString(R.string.bt_play_text)) {
                    int speedInt = Integer.parseInt(speed.getText());
                    double ratioDouble = Double.parseDouble(ratio.getValue());
                    double frequencyDouble = Double.parseDouble(frequency.getValue());
                    double pauseDouble = Double.parseDouble(pause.getText());

                    Log.d(TAG, "Play sample");
                    cw.reInit(speedInt, frequencyDouble, ratioDouble, pauseDouble);

                    cw.playString("rv9yw");
                } else {
                    if (cw != null) {
                        cw.stop();
                    }

                }

            }
        });

        ListView v = getListView();
        v.addFooterView(bt);

        bindChangeListener(speed);
        bindChangeListener(call);
        bindChangeListener(ratio);
        bindChangeListener(frequency);
        bindChangeListener(lesson);
        bindChangeListener(pause);

        settingsUiHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case CwPlayer.PLAY:
                        bt.setText(R.string.bt_stop_text);
//                        actionBar.hide();
                        break;
                    case CwPlayer.STOP:
                        bt.setText(R.string.bt_play_text);
//                        actionBar.show();
                        break;
                    case CwPlayer.PAUSE:
                        break;
                    default:
                        super.handleMessage(msg);
                }
            }
        };

        cw = (CwPlayer) getLastNonConfigurationInstance();
        if (cw == null) {
            cw = new CwPlayer(30, 700, 3, 2);
        }
        cw.setOuterUiHandler(settingsUiHandler);

    }

    private void bindChangeListener (Preference pref) {
        pref.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
        sBindPreferenceSummaryToValueListener.onPreferenceChange(pref, PreferenceManager
                .getDefaultSharedPreferences(pref.getContext())
                .getString(pref.getKey(), ""));
    }

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener =
            new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference pref, Object value) {
                    String stringValue = value.toString();

                    if (pref instanceof ListPreference) {
                        ListPreference listPref = (ListPreference) pref;
                        int index = listPref.findIndexOfValue(stringValue);
                        pref.setSummary(
                                index >= 0
                                        ? listPref.getEntries()[index]
                                        : null);
                    } else if (pref instanceof EditTextPreference) {
                        switch (pref.getKey()){
                            case "speed_wpm":
                                stringValue += " wpm.";
                                break;
                        }
                        pref.setSummary(stringValue);
                    } else {
                        pref.setSummary(stringValue);
                    }
                    return true;
                }
            };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                cw.close();

                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cw != null) {
            cw.stop();
        }
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        return cw;
    }
}