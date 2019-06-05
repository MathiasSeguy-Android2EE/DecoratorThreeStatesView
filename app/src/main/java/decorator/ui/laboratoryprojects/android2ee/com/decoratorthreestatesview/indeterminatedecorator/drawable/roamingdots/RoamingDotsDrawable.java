/**
 * <ul>
 * <li>RondBleu</li>
 * <li>com.android2ee.formation.paris.atos.customview</li>
 * <li>07/07/2015</li>
 * <p/>
 * <li>======================================================</li>
 * <p/>
 * <li>Projet : Mathias Seguy Project</li>
 * <li>Produit par MSE.</li>
 * <p/>
 * /**
 * <ul>
 * Android Tutorial, An <strong>Android2EE</strong>'s project.</br>
 * Produced by <strong>Dr. Mathias SEGUY</strong>.</br>
 * Delivered by <strong>http://android2ee.com/</strong></br>
 * Belongs to <strong>Mathias Seguy</strong></br>
 * ***************************************************************************************************************</br>
 * This code is free for any usage but can't be distribute.</br>
 * The distribution is reserved to the site <strong>http://android2ee.com</strong>.</br>
 * The intelectual property belongs to <strong>Mathias Seguy</strong>.</br>
 * <em>http://mathias-seguy.developpez.com/</em></br> </br>
 * <p/>
 * *****************************************************************************************************************</br>
 * Ce code est libre de toute utilisation mais n'est pas distribuable.</br>
 * Sa distribution est reservée au site <strong>http://android2ee.com</strong>.</br>
 * Sa propriété intellectuelle appartient à <strong>Mathias Seguy</strong>.</br>
 * <em>http://mathias-seguy.developpez.com/</em></br> </br>
 * *****************************************************************************************************************</br>
 */

package decorator.ui.laboratoryprojects.android2ee.com.decoratorthreestatesview.indeterminatedecorator.drawable.roamingdots;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import decorator.ui.laboratoryprojects.android2ee.com.decoratorthreestatesview.R;
import decorator.ui.laboratoryprojects.android2ee.com.decoratorthreestatesview.indeterminatedecorator.drawable.IndeterminateDrawable;
import decorator.ui.laboratoryprojects.android2ee.com.decoratorthreestatesview.indeterminatedecorator.drawable.IndeterminateDrawableCallBackIntf;

/**
 * Created by Mathias Seguy - Android2EE on 07/07/2015.
 */
public class RoamingDotsDrawable extends IndeterminateDrawable {
    /**
     * To initialized variable when the animation is launched for the first time
     */
    private static final int NOT_INITIALIZED = -1;
    /**Width and height of the view*/
    private int w, h;
    /**absissa of center of the dot*/
    private int centerX;
    /**ordinate of center of the dot*/
    private int centerY;
    /**initial/theorical radius of the dot*/
    private int radius;
    /**Radius of the ball displayed
     * if you want to change the size of the ball during animation,it's the real size of the displayed ball*/
    private float smallDotsRadius;
    /**Pain used to draw the dots
     * When the state is indeterminate
     * (animation is running)*/
    private Paint indeterminatePaint;
    /**Pain used to draw the dot
     * When the state is determinate*/
    private Paint determinatePaint;
    /**Shader used by the paint to draw the dots
     * When the state is indeterminate
     * (animation is running)*/
    Shader indetereminateShader;
    /**Shader used by the paint to draw the dot
     * When the state is determinate*/
    Shader detereminateShader;
    //String when gingerbread
    private String gingerbread = "Gingerdread :(";
    /**
     * The view to decorate
     * Mainly because we need to call invalidate on it
     */
    private View decoratedView;
    /**
     * The graphical context
     */
    Context ctx;
    /**
     * Callback to say, ok the transition to the determinate state is over
     */
    IndeterminateDrawableCallBackIntf callback;
    /***********************************************************
     *  Type for the effect
     **********************************************************/
    private int shaderEffectType;
    public static final int DEFAULT_TYPE=0;
    public static final int RAINBOW_TYPE=1;
    public static final int RADIAL_RAINBOW_TYPE=2;
    public static final int RADIAL_PRIMARYCOLORS_TYPE=3;

    /***********************************************************
    *  Constructors
    **********************************************************/

    public RoamingDotsDrawable(View ownerView, IndeterminateDrawableCallBackIntf callback, int w, int h) {
        this(DEFAULT_TYPE,ownerView,callback,w,h);
    }
    public RoamingDotsDrawable(int type,View ownerView, IndeterminateDrawableCallBackIntf callback, int w, int h) {
        this.shaderEffectType=type;
        decoratedView=ownerView;
        ctx=decoratedView.getContext();
        this.callback=callback;
        init();
        initGingerAnim(ctx);
        initializeSize(w,h);
    }

    /***********************************************************
     * Managing initialization
     **********************************************************/
    /**
     * Initialize
     */
    private void init() {
        postICS=ctx.getResources().getBoolean(R.bool.postICS);
        // instantiate paints
        determinatePaint = new Paint();
        indeterminatePaint = new Paint();
        determinatePaint.setColor(Color.BLUE);
        // Define how to draw text
        determinatePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        determinatePaint.setAntiAlias(true);
        //Initialize the Gingerbread animation is necessary
        if(!postICS){
            animGinger= AnimationUtils.loadAnimation(ctx, R.anim.bump);
        }
    }


    /**
     * Initialize the Gingerbread animation is necessary
     * @param context
     */
    private void initGingerAnim(Context context) {
        //TODO load the bump anim
    }


    /**
     * Call this method to initialize the size of bluedot
     * @param w
     * @param h
     */
    private void initializeSize(int w, int h) {
        this.w = w;
        this.h = h;
        centerX = w / 2;
        centerY = h / 2;
        radius = Math.min(w / 8, h / 8);
        //initialize shaders
        initializeShaders();

    }


    /**
     * Initialize the Shaders of the Paints
     */
    private void initializeShaders() {
        Matrix matrix;
        //initialize the shader (stuff that make the color of the paint depending on the location in the screen and a set of colors)
        //@chiuki at droidcon london
        switch(shaderEffectType) {
            case RAINBOW_TYPE:
                indetereminateShader = new LinearGradient(0, 0, 0, w, ColorsFactory.getRainbowColors(ctx),
                        null, Shader.TileMode.MIRROR);
                matrix = new Matrix();
                matrix.setRotate(90);
                indetereminateShader.setLocalMatrix(matrix);
                    break;
            case RADIAL_RAINBOW_TYPE:
                indetereminateShader = new RadialGradient(centerX + radius / 4, centerY + radius / 2,
                        radius, ColorsFactory.getRainbowColors(ctx), null, Shader.TileMode.MIRROR);
                break;
            case RADIAL_PRIMARYCOLORS_TYPE:
                indetereminateShader = new RadialGradient(centerX + radius / 4, centerY + radius / 2,
                        radius, ColorsFactory.getRadialColors(ctx), null, Shader.TileMode.MIRROR);
                break;

            case DEFAULT_TYPE:
//                indetereminateShader = new LinearGradient(0, 0, h, w, ColorsFactory.getRadialColors(ctx),
//                        null, Shader.TileMode.MIRROR);
                indetereminateShader = new LinearGradient(0, 0, 0, w, ColorsFactory.getRadialColors(ctx),
                        null, Shader.TileMode.MIRROR);
                matrix = new Matrix();
                matrix.setRotate(90);
                indetereminateShader.setLocalMatrix(matrix);
                break;
            default:
                // Load the bitmap "cover.png"
                Bitmap mBitmap = BitmapFactory.decodeResource(ctx.getResources(),
                        R.mipmap.ic_shader_background);
                // Create the shader
                indetereminateShader = new BitmapShader(mBitmap,
                        Shader.TileMode.REPEAT,
                        Shader.TileMode.REPEAT);
                break;
        }
        indeterminatePaint.setShader(indetereminateShader);
        detereminateShader = new RadialGradient(centerX + radius / 4, centerY + radius / 2,
                radius, ColorsFactory.getRainbowColors(ctx), null, Shader.TileMode.MIRROR);
        determinatePaint.setShader(detereminateShader);
    }
    /***********************************************************
     *  IndeterminateDrawableIntf instantiation
     **********************************************************/
    /**
     * To call to set the component in its indeterminate state
     * Usually if it's a drawable it launches an animation (because indeterminate state) and call invalidate
     */
    @Override
    public void setIndeterminate() {
        startAnimation();
    }

    /**
     * To call to set the component in its determinate state
     * Usually it stops the running animation and become transparent
     */
    @Override
    public void setDeterminate() {
        stopAnimation();
    }
    /***********************************************************
     * Managing Animation
     **********************************************************/
    /***
     * The ObjectAnimator that handles the animation
     */
    ObjectAnimator animSpreading, animGathering;
    /**
     * The animation played when under gingerbread
     */
    Animation animGinger;
    /***
     * Is post IceCreamSandwich
     */
    boolean postICS = true;
    /**
     * When set to true, we need to stop every thing that try to run
     */
    boolean onStop=false;
    /***
     * Is animating right now ?
     */
    boolean animating = false;
    /**
     * Is Toto Animation has been reversed ?
     */
    boolean animTotoRepeating = false;
    /***
     * Number of blue dits when splitted
     */
    int numberOfSplittedCircle = NOT_INITIALIZED;
    /***
     * X,Y coordinates of each dots (colorFilterArray of primitive is the leightweigth way to do that)
     */
    int[] splittedCirlceX, splittedCirlceY;
    /***
     * Direction way of each dots to stay inside the area of the View
     */
    boolean[] positiveXDirection, positiveYDirection;
    /***
     * Direction of each dots
     */
    double[] cosDirI, sinDirI;
    /***
     * Direction of each dots
     */
    float[] deltaX, deltaY;

    /***********************************************************
     *  Managing animations "Life cycle"
     **********************************************************/

    /**
     * Start animating the rondBleu
     *
     */
    @SuppressLint("NewApi")
    private void startAnimation() {
        onStop=false;
        Log.e("BlueDot", "StartAnimating postICS=" + postICS);
        //will be done only for ICS and above
        if (postICS) {
            //do chet haaze animation(https://www.youtube.com/watch?v=vCTcmPIKgpM)
            //initalize if needed
            if (numberOfSplittedCircle == NOT_INITIALIZED) {
                initializeAnimationsParameters();
                updateRadius(numberOfSplittedCircle);
                initializeSpreadingAnimation();
            }
            //run the animation
            if (!animating) {
                for (int i = 0; i < numberOfSplittedCircle; i++) {
                    positiveXDirection[i] = true;
                    positiveYDirection[i] = true;
                }
                animSpreading.start();
            }
        } else {
            //do nothing fucking gingerbread, or do the minimal
            //TODO make the bump anim laready loaded
        }
    }
    /**
     * Call to stop the curent animation if it is running
     */
    @SuppressLint("NewApi")
    private void stopAnimation(){
        if (postICS) {
            if(animSpreading.isRunning()){
                //it means animSpread stop and then run gathering (define in the listeners of the spread anim)
                animSpreading.cancel();
            }
        }else{
            this.stopAnimation();
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onStop() {
        onStop=true;
        if (postICS) {
            if(animSpreading!=null&&animSpreading.isRunning()){
                animSpreading.cancel();
                animSpreading
            }
            if(animGathering!=null&&animGathering.isRunning()){
                animGathering.cancel();
            }
        }
    }

    /**
     * This method changes animations parameter to redfine movement
     * Passing from spreading to gathering
     */
    @SuppressLint("NewApi")
    private void launchReturnToCenterAnimation() {
        //break condition
        if(onStop)return;
        //normal execution
        initializeGatheringAnimator();
        if (!animating) {
            animGathering.start();
            animating = true;
        }

    }

    /**
     * initialize all the parameters needed to launch the animations
     */
    private void initializeAnimationsParameters() {
        //initialize all the elements needed for the animation
        numberOfSplittedCircle = (int) (Math.random() * 11) + 7;
        splittedCirlceX = new int[numberOfSplittedCircle];
        splittedCirlceY = new int[numberOfSplittedCircle];
        positiveXDirection = new boolean[numberOfSplittedCircle];
        positiveYDirection = new boolean[numberOfSplittedCircle];
        deltaX = new float[numberOfSplittedCircle];
        deltaY = new float[numberOfSplittedCircle];
        //calculate the cos and sin in the i direction (360*i/numberOfCircle) is the direction
        cosDirI = new double[numberOfSplittedCircle];
        sinDirI = new double[numberOfSplittedCircle];
        for (int i = 0; i < numberOfSplittedCircle; i++) {
            splittedCirlceX[i] = centerX;
            splittedCirlceY[i] = centerY;
            cosDirI[i] = Math.cos((i * (2 * Math.PI / (float) numberOfSplittedCircle)));
            sinDirI[i] = Math.sin((i * (2 * Math.PI / (float) numberOfSplittedCircle)));
            positiveXDirection[i] = true;
            positiveYDirection[i] = true;
        }
        //speed depending on size
        //And I want my speed to be cm/Ms constant on all devices
        DisplayMetrics metrics = ctx.getResources().getDisplayMetrics();
        dpFactor = metrics.density;
    }



    /***********************************************************
     *  Manage Spreading Animation
     **********************************************************/

    /**
     * The main Method that returns a ObjectAnimator
     *
     * @return The ObjectAnimator that animates the views
     */
    @SuppressLint("NewApi")
    ObjectAnimator getSpreadingAnimator() {
        return ObjectAnimator.ofInt(this, "spreading", 0, w/48);
    }
    /**
     * Initialize the Spreading Animation
     */
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void initializeSpreadingAnimation() {
        animSpreading = getSpreadingAnimator();
        animSpreading.setDuration(5000);
        animSpreading.setRepeatMode(ValueAnimator.REVERSE);
        animSpreading.setRepeatCount(ValueAnimator.INFINITE);
        animSpreading.setInterpolator(new LinearInterpolator());
//        animSpreading.setInterpolator(new AccelerateInterpolator());
        animSpreading.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                animating = true;
                animTotoRepeating = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animating = false;
                launchReturnToCenterAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                animating = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                animating = true;
                animTotoRepeating = true;
            }
        });
    }

    float dpFactor;
    /**
     * The property to animate
     * It will make the ball move around like after an explosion and bounce on the walls
     * @param parameter value of the state to calculate the animation of the object
     */
    private void setSpreading(int parameter) {
        //important to define the dirty area not to have to redraw every pixel
        Rect dirtyRect = new Rect(splittedCirlceX[0] - radius, splittedCirlceY[0] - radius, splittedCirlceX[0] + radius, splittedCirlceY[0] + radius);
        //the current dirty rectangle for one dot
        Rect currentRect = new Rect(splittedCirlceX[0] - radius, splittedCirlceY[0] - radius, splittedCirlceX[0] + radius, splittedCirlceY[0] + radius);
        //for each dot, updates its property (direction, color...)
        for (int i = 0; i < numberOfSplittedCircle; i++) {
            //important to define the dirty area not to have to redraw every pixel
            currentRect.left = splittedCirlceX[i] - radius;
            currentRect.top = splittedCirlceY[i] - radius;
            currentRect.right = splittedCirlceX[i] + radius;
            currentRect.bottom = splittedCirlceY[i] + radius;
            dirtyRect.union(currentRect);
            //find the x coordinate of the dot i
            if (positiveXDirection[i]) {
                splittedCirlceX[i] = (int) (splittedCirlceX[i] + cosDirI[i] * 5* dpFactor);
            } else {
                splittedCirlceX[i] = (int) (splittedCirlceX[i] - cosDirI[i] * 5* dpFactor);
            }
            //find if we need to reverse the movement in x of dot i to stay in the area of the view
            if (splittedCirlceX[i]-radius <= 0 || splittedCirlceX[i]+radius >= w) {
                //change direction
                positiveXDirection[i] = !positiveXDirection[i];
                if (positiveXDirection[i]) {
                    splittedCirlceX[i] = (int) (splittedCirlceX[i] + cosDirI[i] * 5* dpFactor);
                    splittedCirlceX[i] = (int) (splittedCirlceX[i] + cosDirI[i] * 5* dpFactor);
                } else {
                    splittedCirlceX[i] = (int) (splittedCirlceX[i] - cosDirI[i] * 5* dpFactor);
                    splittedCirlceX[i] = (int) (splittedCirlceX[i] - cosDirI[i] * 5* dpFactor);
                }
            }
            //find the y coordinates of the dot i
            if (positiveYDirection[i]) {
                splittedCirlceY[i] = (int) (splittedCirlceY[i] + sinDirI[i] * 5 * dpFactor);
            } else {
                splittedCirlceY[i] = (int) (splittedCirlceY[i] - sinDirI[i] * 5 * dpFactor);
            }
            //find if we need to reverse the movement in y of dot i to stay in the area of the view
            if (splittedCirlceY[i]-radius <= 0 || splittedCirlceY[i]+radius >= h) {
                positiveYDirection[i] = !positiveYDirection[i];
                //move the ball to be in the area
                if (positiveYDirection[i]) {
                    splittedCirlceY[i] = (int) (splittedCirlceY[i] + sinDirI[i] * 5 * dpFactor);
                    splittedCirlceY[i] = (int) (splittedCirlceY[i] + sinDirI[i] * 5 * dpFactor);
                } else {
                    splittedCirlceY[i] = (int) (splittedCirlceY[i] - sinDirI[i] * 5 * dpFactor);
                    splittedCirlceY[i] = (int) (splittedCirlceY[i] - sinDirI[i] * 5 * dpFactor);
                }
            }
            //update dirty zone
            currentRect.left = splittedCirlceX[i] - radius;
            currentRect.top = splittedCirlceY[i] - radius;
            currentRect.right = splittedCirlceX[i] + radius;
            currentRect.bottom = splittedCirlceY[i] + radius;
            dirtyRect.union(currentRect);
        }
        if (parameter < 25 && !animTotoRepeating) {
            updateRadius(parameter);
        }
        //then invalidate the dirty rectangle
        decoratedView.invalidate(dirtyRect);
    }





    /***********************************************************
     *  Manage Gathering animation
     **********************************************************/

    /**
     * The main Method that returns a ObjectAnimator
     *
     * @return The ObjectAnimator that animates the views
     */
    @SuppressLint("NewApi")
    ObjectAnimator getGatheringAnimator() {
        //define value
        for (int i = 0; i < numberOfSplittedCircle; i++) {
            deltaX[i] = ((float) (splittedCirlceX[i] - centerX)) / 100;
            deltaY[i] = ((float) (splittedCirlceY[i] - centerY)) / 100;
            Log.e("BlueDot", "calcul pour X " + i + " : " + splittedCirlceX[i] + "-" + centerX + "/100=" + deltaX[i]);
            Log.e("BlueDot", "calcul pour Y " + i + " : " + splittedCirlceY[i] + "-" + centerY + "/100=" + deltaY[i]);
        }
        return ObjectAnimator.ofInt(this, "gathering", 0, 100);
    }

    /**
     * Initialize the GatheringAnimator object
     */
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void initializeGatheringAnimator() {
        animGathering = getGatheringAnimator();
        animGathering.setDuration(600);
        animGathering.setRepeatMode(ValueAnimator.RESTART);
        animGathering.setRepeatCount(0);
        animGathering.setInterpolator(new LinearInterpolator());
        animGathering.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                animating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animating = false;
                callback.onDeterminated();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                animating = false;
                callback.onDeterminated();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                animating = true;
            }
        });
        animGathering.setInterpolator(new DecelerateInterpolator());
    }
    /**
     * Gather the balls in the middle
     *
     * @param parameter Between 0 and 100
     */
    private void setGathering(int parameter) {
        Log.e("TAG","Circle of the 1 dot  pour param="+parameter );
        //important to define the dirty area not to have to redraw every pixel
        Rect dirtyRect = new Rect(splittedCirlceX[0] - radius, splittedCirlceY[0] - radius, splittedCirlceX[0] + radius, splittedCirlceY[0] + radius);
        //the current dirty rectangle for one dot
        Rect currentRect = new Rect(splittedCirlceX[0] - radius, splittedCirlceY[0] - radius, splittedCirlceX[0] + radius, splittedCirlceY[0] + radius);
        //for each dot, updates its property (direction, color...)
        for (int i = 0; i < numberOfSplittedCircle; i++) {
            //update dirty zone
            currentRect.left = splittedCirlceX[i] - radius;
            currentRect.top = splittedCirlceY[i] - radius;
            currentRect.right = splittedCirlceX[i] + radius;
            currentRect.bottom = splittedCirlceY[i] + radius;
            dirtyRect.union(currentRect);
            //find the x coordinate of the dot i
            splittedCirlceX[i] = (int) (centerX + ((100 - parameter) * deltaX[i]));
            //find the y coordinates of the dot i
            splittedCirlceY[i] = (int) (centerY + ((100 - parameter) * deltaY[i]));
            if(i==1){
                Log.e("TAG","Circle of the 1 dot x="+splittedCirlceX[i]+", y="+splittedCirlceY[i]+" pour param="+parameter );
            }

            //update dirty zone
            currentRect.left = splittedCirlceX[i] - radius;
            currentRect.top = splittedCirlceY[i] - radius;
            currentRect.right = splittedCirlceX[i] + radius;
            currentRect.bottom = splittedCirlceY[i] + radius;
            dirtyRect.union(currentRect);
        }
        // smallDotsRadius =((radius- initialDotsRadius)*parameter)/100;
        if (parameter > 50) {
            updateRadius(parameter);
        }
        //then invalidate the dirty rectangle
        decoratedView.invalidate(dirtyRect);
    }

    private void updateRadius(int parameter) {
//        smallDotsRadius = ((radius - initialDotsRadius) * parameter / 50) + (2 * initialDotsRadius - radius);
        smallDotsRadius = radius;
    }

    /***********************************************************
     * Drawable Abstract methods
     **********************************************************/

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (!animating) {
            canvas.drawCircle(centerX, centerY, radius, determinatePaint);
        }
        if (animating && !postICS) {
            canvas.drawText(gingerbread, centerX - w, centerY, determinatePaint);
        }
        if (animating && postICS) {
            indeterminatePaint.setShader(indetereminateShader);
            for (int i = 0; i < numberOfSplittedCircle; i++) {
                canvas.drawCircle(splittedCirlceX[i], splittedCirlceY[i], smallDotsRadius, indeterminatePaint);
            }
        }
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        indeterminatePaint.setAlpha(alpha);
        determinatePaint.setAlpha(alpha);
    }


    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }


}
