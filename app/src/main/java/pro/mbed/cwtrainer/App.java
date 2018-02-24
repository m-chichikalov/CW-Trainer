package pro.mbed.cwtrainer;

import android.app.Application;
import android.preference.PreferenceManager;

import pro.mbed.cwtrainer.DI.AppComponent;
import pro.mbed.cwtrainer.DI.AppModule;
import pro.mbed.cwtrainer.DI.DaggerAppComponent;

/**
 * Created by m_chichi on 22.02.2018.
 */

public class App extends Application {
    private static AppComponent component;

    public static AppComponent getComponent() {
        return component;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
        component = buildComponent();
    }

    protected  AppComponent buildComponent() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(getApplicationContext()))
                .build();
    }
}
