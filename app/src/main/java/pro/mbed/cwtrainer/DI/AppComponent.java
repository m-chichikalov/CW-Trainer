package pro.mbed.cwtrainer.DI;

import dagger.Component;
import pro.mbed.cwtrainer.ui.Main;
import pro.mbed.cwtrainer.ui.ResultActivity;
import pro.mbed.cwtrainer.ui.SettingsActivity;

/**
 * Created by m_chichi on 22.02.2018.
 */


@ApplicationScope
@Component(modules = {AppModule.class, CWModule.class})
public interface AppComponent {
    void inject(Main main);
    void inject(SettingsActivity settingsActivity);
    void inject(ResultActivity resutlActivity);
}
