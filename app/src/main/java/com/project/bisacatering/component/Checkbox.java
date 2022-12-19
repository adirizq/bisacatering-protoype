package com.project.bisacatering.component;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import com.project.bisacatering.R;

public class Checkbox extends androidx.appcompat.widget.AppCompatCheckBox {

    public Checkbox(Context context, AttributeSet attrs) {
        super(context, attrs);
        //setButtonDrawable(new StateListDrawable());
    }
    @Override
    public void setChecked(boolean t){
        if(t)
        {
            this.setBackgroundResource(R.drawable.checkbox_selected);
        }
        else
        {
            this.setBackgroundResource(R.drawable.checkbox_normal);
        }
        super.setChecked(t);
    }
}