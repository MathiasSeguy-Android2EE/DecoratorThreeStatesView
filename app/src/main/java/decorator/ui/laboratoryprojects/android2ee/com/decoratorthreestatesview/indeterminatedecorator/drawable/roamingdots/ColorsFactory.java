package decorator.ui.laboratoryprojects.android2ee.com.decoratorthreestatesview.indeterminatedecorator.drawable.roamingdots;

import android.content.Context;

import decorator.ui.laboratoryprojects.android2ee.com.decoratorthreestatesview.R;

/**
 * Created by Mathias Seguy - Android2EE on 18/05/2017.
 * Return an array of colors for playing with shaders
 */

public class ColorsFactory {

    /**
     * @return
     * @chiuki http://chiuki.github.io/android-shaders-filters/#/5
     */
    public static int[] getRainbowColors(Context ctx) {
        return new int[]{
                ctx.getResources().getColor(R.color.rainbow_red),
                ctx.getResources().getColor(R.color.rainbow_yellow),
                ctx.getResources().getColor(R.color.rainbow_green),
                ctx.getResources().getColor(R.color.rainbow_turquoise),
                ctx.getResources().getColor(R.color.rainbow_blue),
                ctx.getResources().getColor(R.color.rainbow_purple)
        };
    }

    /**
     * @return
     * a list of colors
     */
    public static int[] getRadialColors(Context ctx) {
        return new int[]{
                ctx.getResources().getColor(R.color.colorAccent),
                ctx.getResources().getColor(R.color.colorPrimaryDark),
                ctx.getResources().getColor(R.color.colorPrimary)
        };
    }
}
