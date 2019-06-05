package decorator.ui.laboratoryprojects.android2ee.com.decoratorthreestatesview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import decorator.ui.laboratoryprojects.android2ee.com.decoratorthreestatesview.indeterminatedecorator.IndeterminateStateWidgetDecorator;

public class MainActivity extends AppCompatActivity {
    Button btnButton;
    TextView textView;
    ToggleButton toggleButton;
    CheckBox checkBox;
    SeekBar seekBar;

    boolean indterminate=false;
    IndeterminateStateWidgetDecorator decoratorBtn, decoratorSB, decoratorTB, decoratorTV, decoratorCB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnButton= (Button) findViewById(R.id.button);
        toggleButton= (ToggleButton) findViewById(R.id.toggleButton);
        textView= (TextView) findViewById(R.id.textView);
        checkBox= (CheckBox) findViewById(R.id.checkBox);
        seekBar= (SeekBar) findViewById(R.id.seekBar);
        //TODO delete when fixed
        btnButton.setTag("btnButton");
        toggleButton.setTag("toggleButton");
        textView.setTag("textView");
        checkBox.setTag("checkBox");
        seekBar.setTag("seekBar");
        //define the decorator
        decoratorBtn=new IndeterminateStateWidgetDecorator(btnButton);
        decoratorSB=new IndeterminateStateWidgetDecorator(seekBar);
        decoratorTB=new IndeterminateStateWidgetDecorator(toggleButton);
        decoratorTV=new IndeterminateStateWidgetDecorator(textView);
        decoratorCB=new IndeterminateStateWidgetDecorator(checkBox);

        btnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               switchState();
            }
        });
    }

    /**
     * Change the staate of the button
     */
    public void switchState(){
        indterminate=!indterminate;
        if(indterminate){
            decoratorBtn.setIndeterminate();
            decoratorSB.setIndeterminate();
            decoratorTB.setIndeterminate();
            decoratorTV.setIndeterminate();
            decoratorCB.setIndeterminate();
        }else {
            decoratorBtn.setDeterminate();
            decoratorSB.setDeterminate();
            decoratorTB.setDeterminate();
            decoratorTV.setDeterminate();
            decoratorCB.setDeterminate();
        }

    }
    @Override
    protected void onStop() {
        super.onStop();
        decoratorBtn.onStop();
        decoratorSB.onStop();
        decoratorTB.onStop();
        decoratorTV.onStop();
        decoratorCB.onStop();
    }
}
