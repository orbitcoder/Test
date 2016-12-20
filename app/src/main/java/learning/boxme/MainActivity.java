package learning.boxme;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final int RC_NEW_LOGIN = 1000;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.bt_signup);

        float[] radii = new float[8];


        float[] newradii = new float[8];
        float[] midradii = new float[8];
        for (int j = 0; j < 8; j++) {
            radii[j] = 70;
            midradii[j] = 30;
            newradii[j] = 0;
        }


        final AnimationDrawable anim_drawable;
        ShapeDrawable selctShape = new ShapeDrawable(new RoundRectShape(radii, null, null));
        selctShape.getPaint().setColor(Color.WHITE);
        ShapeDrawable midShape = new ShapeDrawable(new RoundRectShape(midradii, null, null));
        midShape.getPaint().setColor(Color.WHITE);

        ShapeDrawable defShape = new ShapeDrawable();
        defShape.setShape(new RoundRectShape(newradii, null, null));
        defShape.getPaint().setColor(Color.WHITE);

        anim_drawable = new AnimationDrawable();
        anim_drawable.addFrame(selctShape, 10);
        anim_drawable.addFrame(midShape, 10);
        anim_drawable.addFrame(defShape, 10);
        anim_drawable.setOneShot(true);
        button.setBackground(anim_drawable);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                anim_drawable.start();
                CountDownTimer cdt = new CountDownTimer(40, 10) {

                    @Override
                    public void onTick(long l) {
                    }

                    @Override
                    public void onFinish() {
                        startSignupForm();
                    }
                };
                cdt.start();

            }
        });
    }

    private void startSignupForm() {
        Intent intent = new Intent(this, SignupActivity.class);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, button,
                getString(R.string.transition_signup));
        startActivity(intent, options.toBundle());
    }
}
