package learning.boxme;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.transition.Transition;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import learning.boxme.pojo.User;
import learning.boxme.utils.Constants;
import learning.boxme.utils.JsonUtils;
import learning.boxme.utils.Utils;
import learning.boxme.view.MorphedButton;
import learning.boxme.view.MorphingAnimation;


/**
 * Created by bhupendrarawat on 16/12/16.
 */
public class SignupActivity extends Activity implements View.OnClickListener {

    private LinearLayout rel_container;
    private MorphedButton submit_info;
    private boolean isValidUser = false;
    private String gituser = "";
    private User current_user;
    private EditText ed_username, ed_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
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
    }

    private void finishSetup() {
        submit_info = (MorphedButton) findViewById(R.id.submit_info);
        rel_container = (LinearLayout) findViewById(R.id.container);
//        morphToCircle(submit_info);
        submit_info.setVisibility(View.VISIBLE);
        morphToSquare(submit_info);
        ed_username = (EditText) findViewById(R.id.etusername);
        ed_password = (EditText) findViewById(R.id.set_password);

        submit_info.setOnClickListener(this);

    }

    private void morphToSquare(final MorphedButton btnMorph) {
        MorphingAnimation.Listener square_anim_listener = new MorphingAnimation.Listener() {
            @Override
            public void onAnimationEnd() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        submit_info.setEnabled(true);
                    }
                });

            }
        };
        MorphedButton.Params square = MorphedButton.Params.create()
                .duration(300)
                .cornerRadius(Utils.dpToPx(this, 28))
                .width(Utils.dpToPx(this, 150))
                .height(Utils.dpToPx(this, 56))
                .color(getColor(R.color.mb_spring_green))
                .colorPressed(getColor(R.color.mb_spring_green))
                .text(getString(R.string.submit_info_bt_text)).animationListener(square_anim_listener);
        btnMorph.morph(square);
    }

    private void morphToCircle(MorphedButton submit_info) {
        MorphingAnimation.Listener square_anim_listener = new MorphingAnimation.Listener() {
            @Override
            public void onAnimationEnd() {

            }
        };
        MorphedButton.Params circle = MorphedButton.Params.create()
                .duration(200)
                .cornerRadius(Utils.dpToPx(this, 56))
                .width(Utils.dpToPx(this, 56))
                .height(Utils.dpToPx(this, 56))
                .color(getColor(R.color.mb_spring_green))
                .colorPressed(getColor(R.color.mb_spring_green)).animationListener(square_anim_listener);
        submit_info.morph(circle);

    }

    public void dismiss(View view) {
        onBackPressed();
    }


    public void proceed() {
        Intent intent = new Intent(this, FinalActivity.class);


        Pair<View, String> p1 = Pair.create((View) submit_info, "button");
        Pair<View, String> p2 = Pair.create((View) rel_container, "transition_signup");
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, p1, p2);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("USER", gituser);
        intent.putExtra(Constants.USER_IMAGE, current_user.getAvatarUrl());
        startActivity(intent, options.toBundle());
        finish();

    }


    public String Authorization(String username, String password) {
        String result = null;
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("https://api.github.com/user");

        String encode = Base64.encodeToString((username + ":" + password).getBytes(), Base64.DEFAULT).replace("\n", "");
        httpGet.setHeader("Authorization", "Basic " + encode);
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

//        Log.i("GIT", "result \n" + result);
        return result;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit_info:
                morphToCircle(submit_info);
                submit_info.setEnabled(false);
                new AsyncTask<String, Void, String>() {

                    @Override
                    protected String doInBackground(String... userInfo) {
                        return Authorization(userInfo[0], userInfo[1]);
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        if (s != null) {
                            isValidUser = false;
                            validateUser(s);

                        } else {
                            Toast.makeText(SignupActivity.this, "Not able to varify", Toast.LENGTH_SHORT).show();
                            submit_info.setEnabled(true);
                        }

                    }
                }.execute(ed_username.getText().toString(), ed_password.getText().toString());


                break;
        }


    }

    private void validateUser(String uData) {

        try {
            current_user = null;
            current_user = (User) JsonUtils.objectify(uData, User.class);
            gituser = current_user.getLogin();
            if (current_user != null && !current_user.getLogin().isEmpty()) {
                isValidUser = true;
                proceed();
            } else {
                Toast.makeText(this, "Enter valid github username and password", Toast.LENGTH_SHORT).show();
                isValidUser = false;
                morphToSquare(submit_info);
            }
        } catch (Exception ex) {
            isValidUser = false;
            Toast.makeText(this, "Enter valid github username and password", Toast.LENGTH_SHORT).show();
            morphToSquare(submit_info);

        }


    }
}
