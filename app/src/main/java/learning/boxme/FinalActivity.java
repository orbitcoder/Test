package learning.boxme;

import android.animation.Animator;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;
import learning.boxme.adapters.FeedAdapter;
import learning.boxme.pojo.UserEvent;
import learning.boxme.utils.Constants;

import static learning.boxme.R.id.fab1;
import static learning.boxme.R.id.fab2;
import static learning.boxme.R.id.fab3;

public class FinalActivity extends AppCompatActivity implements View.OnClickListener {

    private String gitUser, userImageUrl;
    private RecyclerView recyclerView;
    private ProgressBar pb;
    private ImageButton btFab;
    private CircleImageView btFab1, btFab2, btFab3;
    private boolean isFABOpen = false;
    private String jakeImageUrl = "https://avatars.githubusercontent.com/u/66577?";
    private String jakeProfileUrl = "https://api.github.com/users/JakeWharton/events";
    private String chrishProfileUrl = "https://api.github.com/users/chrisbanes/events";
    private String currentProfileUrl = "https://api.github.com/users/" + gitUser + "/events";

    private String chrishImageUrl = "https://avatars.githubusercontent.com/u/227486?";
    private String keyboardsurferImageUrl = "https://avatars.githubusercontent.com/u/336005?";
    private TextView tv_nodata;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_new);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        gitUser = getIntent().getExtras().getString("USER");
        userImageUrl = getIntent().getExtras().getString(Constants.USER_IMAGE);

        pb = (ProgressBar) findViewById(R.id.pb);
        if (getWindow().getSharedElementEnterTransition() != null) {
            getWindow().getSharedElementEnterTransition().addListener(new TransitionUtils.TransitionListenerAdapter() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    getWindow().getSharedElementEnterTransition().removeListener(this);
                    finishSetup();
                }
            });
        } else {
            finishSetup();
        }

        pb.setVisibility(View.VISIBLE);

        currentProfileUrl = "https://api.github.com/users/" + gitUser + "/events";

        getDataFromNetwork(currentProfileUrl);
    }

    private void getDataFromNetwork(String Url) {
        new AsyncTask<String, Void, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                setVisiblity(true, pb);
                setVisiblity(false, tv_nodata);
                setVisiblity(true, recyclerView);


            }

            @Override
            protected String doInBackground(String... voids) {
                String result = null;

                HttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(voids[0]);
                try {
                    HttpResponse httpResponse = client.execute(httpGet);
                    InputStream inputStream = httpResponse.getEntity().getContent();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    String bufferedStrChunk = null;
                    while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
                        stringBuilder.append(bufferedStrChunk);
                    }

                    result = stringBuilder.toString();

                } catch (ClientProtocolException cpe) {
                    System.out.println("ClientProtocolException :" + cpe);
                    cpe.printStackTrace();
                } catch (IOException ioe) {
                    System.out.println("IOException :" + ioe);
                    ioe.printStackTrace();
                }

                Log.i("GIT", "result \n" + result);
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                pb.setVisibility(View.GONE);

                UserEvent[] arr = new Gson().fromJson(s, UserEvent[].class);

                if (arr != null) {
                    FeedAdapter feedAdapter = new FeedAdapter(FinalActivity.this);
                    feedAdapter.setData(Arrays.asList(arr));
                    recyclerView.setAdapter(feedAdapter);
                    if (arr.length == 0) {
                        setVisiblity(true, tv_nodata);
                        setVisiblity(false, recyclerView);

                    }

                }
            }
        }.
                execute(Url);


    }

    private void finishSetup() {
        if (gitUser != null) {
            Toast.makeText(this, "Welcome  " + gitUser.toUpperCase(), Toast.LENGTH_SHORT).show();
        }

        tv_nodata = (TextView) findViewById(R.id.text_no_data);
        btFab = (ImageButton) findViewById(R.id.fab);
        btFab1 = (CircleImageView) findViewById(fab1);
        btFab2 = (CircleImageView) findViewById(fab2);
        btFab3 = (CircleImageView) findViewById(fab3);
//        Picasso.with(this).load(userImageUrl).into(btFab1);
        Picasso.with(this).load(userImageUrl).into(btFab1);
        /*chrisbanes*/
        Picasso.with(this).load(chrishImageUrl).into(btFab2);
        /*jake wharton*/
        Picasso.with(this).load(jakeImageUrl).into(btFab3);

        btFab1.setOnClickListener(this);
        btFab2.setOnClickListener(this);
        btFab3.setOnClickListener(this);


        btFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFABOpen) {
                    showFABMenu();
                } else {
                    closeFABMenu();
                }

            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private void showFABMenu() {
        setVisiblity(true, btFab1);
        setVisiblity(true, btFab2);
        setVisiblity(true, btFab3);
        isFABOpen = true;
        btFab1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        btFab2.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        btFab3.animate().translationY(-getResources().getDimension(R.dimen.standard_155));
    }

    private void closeFABMenu() {
        isFABOpen = false;
        btFab1.animate().translationY(0);
        btFab2.animate().translationY(0);
        btFab3.animate().translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

                if (!isFABOpen) {
                    setVisiblity(false, btFab1);
                    setVisiblity(false, btFab2);
                    setVisiblity(false, btFab3);

                }


            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });


    }

    public void setVisiblity(boolean visiblity, View v) {

        if (v != null) {
            if (visiblity)
                v.setVisibility(View.VISIBLE);
            else
                v.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab1:
                getDataFromNetwork(currentProfileUrl);
                closeFABMenu();

                break;
            case R.id.fab2:
                getDataFromNetwork(chrishProfileUrl);
                closeFABMenu();


                break;
            case R.id.fab3:
                getDataFromNetwork(jakeProfileUrl);
                closeFABMenu();


                break;
        }

    }
}
