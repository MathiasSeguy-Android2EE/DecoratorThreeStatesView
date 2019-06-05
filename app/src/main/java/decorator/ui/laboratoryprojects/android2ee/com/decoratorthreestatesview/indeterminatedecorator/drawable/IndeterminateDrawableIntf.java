package decorator.ui.laboratoryprojects.android2ee.com.decoratorthreestatesview.indeterminatedecorator.drawable;

/**
 * Created by Mathias Seguy - Android2EE on 18/05/2017.
 */

public interface IndeterminateDrawableIntf {
    /**
     * To call to set the component in its indeterminate state
     * Usually if it's a drawable it launches an animation (because indeterminate state) and call invalidate
     */
    public void setIndeterminate();

    /**
     * To call to set the component in its determinate state
     * Usually it stops the running animation and become transparent
     */
    public void setDeterminate();

    /**
     * Like in Android: stop animation, release memory
     */
    public void onStop();
}
