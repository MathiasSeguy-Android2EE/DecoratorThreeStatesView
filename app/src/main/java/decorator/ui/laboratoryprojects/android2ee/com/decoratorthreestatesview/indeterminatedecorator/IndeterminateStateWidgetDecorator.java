package decorator.ui.laboratoryprojects.android2ee.com.decoratorthreestatesview.indeterminatedecorator;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import decorator.ui.laboratoryprojects.android2ee.com.decoratorthreestatesview.indeterminatedecorator.drawable.IndeterminateDrawable;
import decorator.ui.laboratoryprojects.android2ee.com.decoratorthreestatesview.indeterminatedecorator.drawable.IndeterminateDrawableCallBackIntf;
import decorator.ui.laboratoryprojects.android2ee.com.decoratorthreestatesview.indeterminatedecorator.drawable.roamingdots.RoamingDotsDrawable;

/**
 * Created by Mathias Seguy - Android2EE on 16/05/2017.
 * If you want a component (any view) to have an indeterminate state
 * by changing its background or by having a "layer" upon it
 * or by having a translucent animation,
 *
 * Use this class this way:
*    IndeterminateStateWidgetDecorator decorator;
*
*    @Override
*    protected void onCreate(Bundle savedInstanceState) {
*        super.onCreate(savedInstanceState);
*        setContentView(R.layout.activity_main);
*        btnButton= (Button) findViewById(R.id.button);
*        decorator=new IndeterminateStateWidgetDecorator(btnButton);
*        btnButton.setOnClickListener(new View.OnClickListener() {
*            @Override
*            public void onClick(View v) {
*               switchState();
*            }
*        });
*    }
*
*    //
*     // Change the staate of the button
*     //
*    public void switchState(){
*        indterminate=!indterminate;
*        if(indterminate){
*            decorator.setIndeterminate();
*        }else {
*            decorator.setDeterminate();
*        }
*
*    }
  */
public class IndeterminateStateWidgetDecorator implements IndeterminateDrawableCallBackIntf {
    private static final String TAG = "DecoratorThreeStatesWid";
    /***********************************************************
     *  Attributes
     **********************************************************/
    /**
     * The decoratedView we manage
     */
    private View decoratedView;
    /**
     * The size of the DecoratedView
     */
    private int height,width;
    /**
     * The background drawable of the decoratedView we manage
     */
    private Drawable decoratedDrawable;
    /**
     * The drawable that decorate the initial background
     * It should implements IndetermlinateDrawableIntf
     */
    private IndeterminateDrawable decoratingDrawable =null;
    /**
     * The new background of the decorated view when the state is indeterminate
     */
    private LayerDrawable layerDrawable=null;

    /***********************************************************
     *  State constants
     **********************************************************/
    private int state=DETERMINATE;
    private static final int DETERMINATE=0;
    private static final int INDETERMINATE=1;
    /***********************************************************
    *  Constructors
    **********************************************************/
    public IndeterminateStateWidgetDecorator(View decoratedView) {
        this(decoratedView,null);
    }
    public IndeterminateStateWidgetDecorator(View decoratedView,IndeterminateDrawable decoratingDrawable) {
        this.decoratedView = decoratedView;
        if(decoratingDrawable!=null){
            this.decoratingDrawable=decoratingDrawable;
        }
        init();
    }
    /***********************************************************
     *  Initialisation
     **********************************************************/
    /**
     * initialize the components and size
     */
    private void init(){
        //get the background
        decoratedDrawable=decoratedView.getBackground();
        //find the size of the decorated view
        findDecoratedViewSize();
    }

    /**
     * Find the size of the View we decorate
     */
    private void findDecoratedViewSize() {
        //this is an usual trick when we want to know the dimension of the elements of our view
        // find the dimension of the decoratedView
        ViewTreeObserver vto = decoratedView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    decoratedView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }else{
                    decoratedView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                width = decoratedView.getMeasuredWidth();
                height= decoratedView.getMeasuredHeight();
                createLayerlDrawable();
                Log.e(TAG,"Found dimension : w="+width+", h="+height);
                Log.e(TAG,"FoundNOTMEASUE  dimension : w="+ decoratedView.getWidth()+", h="+ decoratedView.getHeight());
            }
        });
    }

    /**
     * Create the Drawable to use as background when the state is indeterminate
     */
    private void createLayerlDrawable(){
        Log.e(TAG,"Found visible rectangle for "+decoratedView.getTag()+": w="+width+", h="+height);
        //create the new drawable if not provided
        if(decoratingDrawable==null){
            //create default
            decoratingDrawable = new RoamingDotsDrawable(RoamingDotsDrawable.DEFAULT_TYPE,decoratedView,this,width,height);
        }
        Log.e(TAG,"decoratedDrawable == null ? "+(decoratedDrawable==null));
        Log.e(TAG,"decoratingDrawable == null ? "+(decoratingDrawable==null));
        //define the layerDrawable
        Drawable[] backgroundsDrawable=new Drawable[2];
        backgroundsDrawable[0]=decoratedDrawable==null?decoratingDrawable:decoratedDrawable;
        backgroundsDrawable[1]= decoratingDrawable;
        layerDrawable=new LayerDrawable(backgroundsDrawable);
    }

    /***********************************************************
     *  Implement Public method
     **********************************************************/
    /**
     * Switch to the indeterminate state
     */
    public void setIndeterminate(){
        state=INDETERMINATE;
        //if the layerDrawable is not defined yet, return
        if(layerDrawable ==null) {
            return;
        }
        //make the switch
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            decoratedView.setBackground(layerDrawable);
        }else{
            decoratedView.setBackgroundDrawable(layerDrawable);
        }
        //then set the indeterminate state on the drawable
        decoratingDrawable.setIndeterminate();        //
    }

    public int getState(){
        return state;
    }

    /**
     * Like in Android: stop animation, release memory
     * Should be called in onStop
     */
    public void onStop(){
        //if the layerDrawable is not defined yet, return
        if(layerDrawable ==null) {
            return;
        }
        decoratingDrawable.onStop();
        //then release memory

    }

    /**
     * Switch to the determinate state
     */
    public void setDeterminate(){
        state=DETERMINATE;
        //if the layerDrawable is not defined yet, return
        if(layerDrawable ==null) {
            return;
        }
        //switch to determinate
        decoratingDrawable.setDeterminate();
    }

    /**
     * To implement to know when the transition to determinate state is over
     */
    @Override
    public void onDeterminated() {
        //switch background, the state is transition to state determinate is finished
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            decoratedView.setBackground(decoratedDrawable);
        }else{
            decoratedView.setBackgroundDrawable(decoratedDrawable);
        }
    }
}
