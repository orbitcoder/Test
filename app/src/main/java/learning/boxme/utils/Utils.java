package learning.boxme.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by bhupendrarawat on 18/12/16.
 */

public class Utils {

    public static int dpToPx(Context context,int dp) {
        DisplayMetrics metrics = new DisplayMetrics();

        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(metrics);
        float logicalDensity = metrics.density;

        int px = (int) Math.ceil(dp * logicalDensity);
        return px;
    }
}
