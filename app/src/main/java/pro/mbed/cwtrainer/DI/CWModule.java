package pro.mbed.cwtrainer.DI;

/**
 * Created by m_chichi on 22.02.2018.
 */

import dagger.Module;
import dagger.Provides;
import pro.mbed.cwtrainer.util.CwPlayer;
import pro.mbed.cwtrainer.util.KochMethod;

@Module
public class CWModule {

    @Provides
    @ApplicationScope
    CwPlayer provideCwPlayer() {
        return new CwPlayer();
    }

    @Provides
    @ApplicationScope
    KochMethod provideKochMethod() {
        return new KochMethod();
    }
}
