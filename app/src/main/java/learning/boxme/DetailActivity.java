package learning.boxme;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by bhupendrarawat on 18/12/16.
 */
public class DetailActivity extends AppCompatActivity {
    private ImageView userImage;
    private TextView tv_reponame;
    private String repoName;
    private String userImageUrl;
    private String repoUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        postponeEnterTransition();

        repoName = getIntent().getExtras().getString("NAME");
        userImageUrl = getIntent().getExtras().getString("Image");
        repoUrl=getIntent().getExtras().getString("REPO");

        userImage = (ImageView) findViewById(R.id.userimage);
        tv_reponame = (TextView) findViewById(R.id.repo_name);

        tv_reponame.setText(repoName);

        Picasso.with(this).load(userImageUrl).into(userImage);

        scheduleStartPostponedTransition(tv_reponame);
        scheduleStartPostponedTransition(userImage);

    }
    private void scheduleStartPostponedTransition(final View sharedElement) {
        sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                        startPostponedEnterTransition();
                        return true;
                    }
                });
    }

}
