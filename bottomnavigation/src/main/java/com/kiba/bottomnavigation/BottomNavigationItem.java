package com.kiba.bottomnavigation;

import android.graphics.Typeface;

/**
 * Created by KiBa-PC on 2016/6/21.
 */
public class BottomNavigationItem {

    private String label;

    private int defaultIcon;

    private int afterSelectedIcon;
    // TODO: 2016/6/22 color to do. 
    private int color;

    private int badgeNumber = -1;

    private Typeface textViewStyle;

    public BottomNavigationItem(String label, int defaultIcon, int afterSelectedIcon) {
        this(label, defaultIcon, afterSelectedIcon, 0, null);
    }

    public BottomNavigationItem(String label, int defaultIcon, int afterSelectedIcon, int color, Typeface textViewStyle) {
        this.label = label;
        this.defaultIcon = defaultIcon;
        this.afterSelectedIcon = afterSelectedIcon;
        this.color = color;
        this.textViewStyle = textViewStyle;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getDefaultIcon() {
        return defaultIcon;
    }

    public void setDefaultIcon(int defaultIcon) {
        this.defaultIcon = defaultIcon;
    }

    public int getAfterSelectedIcon() {
        return afterSelectedIcon;
    }

    public void setAfterSelectedIcon(int afterSelectedIcon) {
        this.afterSelectedIcon = afterSelectedIcon;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getBadgeNumber() {
        return this.badgeNumber;
    }

    public void setBadgeNumber(int badgeNumber) {
        this.badgeNumber = badgeNumber;
    }

    public Typeface getTextViewStyle() {
        return textViewStyle;
    }
}
