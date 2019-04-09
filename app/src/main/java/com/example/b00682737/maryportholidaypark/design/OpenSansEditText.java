package com.example.b00682737.maryportholidaypark.design;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class OpenSansEditText extends android.support.v7.widget.AppCompatEditText {
    public OpenSansEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public OpenSansEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OpenSansEditText(Context context) {
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
