package pro.mbed.cwtrainer.contracts;

import pro.mbed.cwtrainer.util.KochMethod;

/**
 * Created by m_chichi on 23.02.2018.
 */

public class ResultContract {
    public interface View {
        void showProgressBar();
        void hideProgressBar();
        void updateHtml(String str);
    }
    public interface Presenter {
        void processResult(KochMethod km);

    }
}
