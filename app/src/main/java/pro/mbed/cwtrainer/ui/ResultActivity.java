package pro.mbed.cwtrainer.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import org.sufficientlysecure.htmltextview.HtmlAssetsImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.Provides;
import pro.mbed.cwtrainer.App;
import pro.mbed.cwtrainer.R;
import pro.mbed.cwtrainer.contracts.ResultContract;
import pro.mbed.cwtrainer.presenters.ResultPresenter;
import pro.mbed.cwtrainer.util.KochMethod;

public class ResultActivity extends AppCompatActivity implements ResultContract.View {

    @BindView(R.id.pBarWaitResult)  ProgressBar pBarWaitResult;
    @BindView(R.id.html_result)     HtmlTextView htmlResult;
    @BindView(R.id.fab)             FloatingActionButton fbShare;
    @Inject                         KochMethod km;

    ResultPresenter rPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);
        App.getComponent().inject(this);

        rPresenter = new ResultPresenter(this);

        organizeActionBar();

        rPresenter.processResult(km);

        fbShare.setOnClickListener(rPresenter.setOnClickListenerFbShare());
    }

    public void showProgressBar() {
        pBarWaitResult.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        pBarWaitResult.setVisibility(View.INVISIBLE);
    }

    @Override
    public void updateHtml(String str) {
        htmlResult.setHtml(str);
    }

    private void organizeActionBar () {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_ab_back_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
