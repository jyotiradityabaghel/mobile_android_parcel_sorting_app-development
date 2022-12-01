package com.zoom2uwarehouse.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class TextViewNormal  extends androidx.appcompat.widget.AppCompatTextView {

    public TextViewNormal(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public TextViewNormal(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public TextViewNormal(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), "gothamrnd_book.otf");
        setTypeface(customFont);
    }
}
