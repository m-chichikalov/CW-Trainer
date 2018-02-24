package pro.mbed.cwtrainer.presenters;

import android.support.design.widget.Snackbar;
import android.view.View;

import pro.mbed.cwtrainer.contracts.ResultContract;
import pro.mbed.cwtrainer.util.KochMethod;

/**
 * Created by m_chichi on 23.02.2018.
 */

public class ResultPresenter implements ResultContract.Presenter {
    private ResultContract.View view;

    public ResultPresenter(ResultContract.View view) {
        this.view = view;
    }



    public View.OnClickListener setOnClickListenerFbShare() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Not yet!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        };

    }

    @Override
    public void processResult(KochMethod km) {
        view.updateHtml("" + km.getError());
    }
}
