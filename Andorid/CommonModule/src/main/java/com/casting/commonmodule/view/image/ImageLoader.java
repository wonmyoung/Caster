package com.casting.commonmodule.view.image;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ImageLoader {

    public static <V extends ImageView> void loadImage(Context c, V v, String path)
    {
        try
        {
            if (!TextUtils.isEmpty(path))
            {
                Glide.with(c).load(path).centerCrop().crossFade().into(v);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static <V extends ImageView> void loadRoundImage(Context c, V v, String path, int radius)
    {
        try
        {
            if (!TextUtils.isEmpty(path))
            {
                Glide.with(c).load(path)
                        .bitmapTransform(new RoundedCornersTransformation(c , radius , 0))
                        .centerCrop()
                        .crossFade()
                        .into(v);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
