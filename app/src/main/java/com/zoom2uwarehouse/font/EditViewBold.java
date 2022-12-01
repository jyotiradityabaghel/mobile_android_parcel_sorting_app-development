package com.zoom2uwarehouse.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;


/**@author avadhesh mourya
 * Created by ubuntu on 7/2/18.
 */

public class EditViewBold extends androidx.appcompat.widget.AppCompatEditText {

    public EditViewBold(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public EditViewBold(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public EditViewBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), "gothamrnd_bold.otf");
        setTypeface(customFont);
    }
}