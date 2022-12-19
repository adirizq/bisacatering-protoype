package com.project.bisacatering.component;

import android.content.Context;
import android.util.AttributeSet;

import com.project.bisacatering.R;

public class Radio extends androidx.appcompat.widget.AppCompatRadioButton {

    public Radio(Context context, AttributeSet attrs) {
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