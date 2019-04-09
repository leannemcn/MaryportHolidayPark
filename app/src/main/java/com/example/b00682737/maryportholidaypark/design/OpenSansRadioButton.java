package com.example.b00682737.maryportholidaypark.design;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class OpenSansRadioButton extends android.support.v7.widget.AppCompatRadioButton {

    public OpenSansRadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public OpenSansRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OpenSansRadioButton(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/OpenSans-Regular.ttf");
            setTypeface(tf);
        }
    }

}
