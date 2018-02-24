package pro.mbed.cwtrainer.DI;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by m_chichi on 22.02.2018.
 */
@Module
public class AppModule {
    private Context appContext;

    public AppModule (@NonNull Context context) {
        appContext = context;
    }

    @Provides
    @ApplicationScope
    Context provideContext() {
        return appContext;
    }

    @Provides
    @ApplicationScope
    SharedPreferences provideDefPreference() {
        return PreferenceManager.getDefaultSharedPreferences(appContext);
    }
}
