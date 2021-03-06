package com.chessen.project.stylestumble.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by carolinamarin on 5/8/16.
 * From Udacity course material design
 */


public class ThreeTwoImageView  extends ImageView{
    public ThreeTwoImageView(Context context) {
        super(context);
    }
    public ThreeTwoImageView(Context context,AttributeSet attrs) {
        super(context,attrs);
    }
    public ThreeTwoImageView(Context context, AttributeSet attrs, int defStyle){
        super(context,attrs,defStyle);
    }
    @Override
    protected void onMeasure(int widthSpec, int heightSpec){
        int threeTwoHeight=MeasureSpec.getSize(widthSpec)*2/3;
        int threeTwoHeightSpec=MeasureSpec.makeMeasureSpec(threeTwoHeight,MeasureSpec.EXACTLY);
        super.onMeasure(widthSpec,threeTwoHeightSpec);
    }
}
