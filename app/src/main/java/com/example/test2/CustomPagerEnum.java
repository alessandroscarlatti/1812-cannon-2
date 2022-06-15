package com.example.test2;

/**
 * Created by pc on 6/20/2017.
 */

public enum CustomPagerEnum {

    RED(R.string.app_name, R.layout.page1),
    BLUE(R.string.app_name, R.layout.page2);

    private int mTitleResId;
    private int mLayoutResId;

    CustomPagerEnum(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }
}
