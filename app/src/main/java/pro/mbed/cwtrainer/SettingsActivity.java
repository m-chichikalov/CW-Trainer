package pro.mbed.cwtrainer;

import android.os.Bundle;
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
//    Boolean free;
    EditTextPreference speed, call, pause;
    ListPreference ratio, frequency, lesson;

    public SettingsActivity() {
        super();
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);

        ActionBar actionBar = getSupportActionBar();
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

        cw = (CwPlayer) getLastNonConfigurationInstance();
//      free = true;


        bt = new Button(this);
        bt.setText("Play");
//      bt.setPadding(this.getResources().getDimensionPixelSize(R.dimen.play_sample_bt_padding_left),
//                0,
//                this.getResources().getDimensionPixelSize(R.dimen.play_sample_bt_padding_left), 0);

        bt.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                int speedInt = Integer.parseInt(speed.getText());
                double ratioDouble = Double.parseDouble(ratio.getValue());
                double frequencyDouble = Double.parseDouble(frequency.getValue());
                double pauseDouble = Double.parseDouble(pause.getText());

                    Log.d(TAG, "Play sample");
                    if (cw == null) {
                        cw = new CwPlayer(speedInt, frequencyDouble, ratioDouble, pauseDouble);
                    } else {
                        cw.pause();
                        cw.reInit(speedInt, frequencyDouble, ratioDouble, pauseDouble);
                    }
                    cw.cleanBuffer();
                    cw.play();
                    cw.feed((byte) ' ');
                    cw.feed(call.getText());
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
//                            case "":
//                                break;
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
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        if (cw != null) {
            cw.pause();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (cw != null) {
            cw.close();
            cw = null;
        }
        super.onDestroy();
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        return cw;
    }
}