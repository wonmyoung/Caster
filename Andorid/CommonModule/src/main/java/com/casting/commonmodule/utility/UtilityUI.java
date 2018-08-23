package com.casting.commonmodule.utility;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.MainThread;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.casting.commonmodule.R;
import com.casting.commonmodule.view.KeyBoardVisibilityListener;
import com.casting.commonmodule.view.gls.Rotate3dAnimation;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

import java.io.IOException;
import java.util.regex.Pattern;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class UtilityUI {

    public static InputFilter getEnglishFilter()
    {
        return new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int i, int i1, Spanned spanned, int i2, int i3) {

                Pattern ps = Pattern.compile("^[a-zA-Z0-9]+$");

                if (!ps.matcher(source).matches())
                {
                    return "";
                }

                return null;
            }
        };
    }

    public static InputFilter getKoreanFilter()
    {
        return new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int i, int i1, Spanned spanned, int i2, int i3) {

                Pattern ps = Pattern.compile("^[ㄱ-가-힣]+$");

                if (!ps.matcher(source).matches())
                {
                    return "";
                }

                return null;
            }
        };
    }

    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static boolean hasSoftNavigationBar(Context context)
    {

        boolean hasSoftNavigationBar = false;

        ViewConfiguration viewConfig = ViewConfiguration.get(context);
        hasSoftNavigationBar = !viewConfig.hasPermanentMenuKey();

        // 위 API 로 올바르지 않은 값이 리턴 되는 경우가 있다 (예를들면 G2 단말의 경우)
        // 따라서 아래 로직으로 한번 더 확인한다.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
        {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();

            DisplayMetrics realDisplayMetrics = new DisplayMetrics();
            display.getRealMetrics(realDisplayMetrics);

            int realHeight = realDisplayMetrics.heightPixels;
            int realWidth = realDisplayMetrics.widthPixels;

            DisplayMetrics displayMetrics = new DisplayMetrics();
            display.getMetrics(displayMetrics);

            int displayHeight = displayMetrics.heightPixels;
            int displayWidth = displayMetrics.widthPixels;

            hasSoftNavigationBar = (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
        }

        return hasSoftNavigationBar;
    }

    public static int getMeasuredWidth(View v) {
        return (v == null) ? 0 : v.getMeasuredWidth();
    }

    public static int getWidth(View v) {
        return (v == null) ? 0 : v.getWidth();
    }

    public static int getWidthWithMargin(View v) {
        return getWidth(v) + getMarginHorizontally(v);
    }

    public static int getStart(View v) {
        return getStart(v, false);
    }

    public static int getStart(View v, boolean withoutPadding) {
        if (v == null) {
            return 0;
        }
        if (isLayoutRtl(v)) {
            return (withoutPadding) ? v.getRight() - getPaddingStart(v) : v.getRight();
        } else {
            return (withoutPadding) ? v.getLeft() + getPaddingStart(v) : v.getLeft();
        }
    }

    public static int getEnd(View v) {
        return getEnd(v, false);
    }

    public static int getEnd(View v, boolean withoutPadding) {
        if (v == null) {
            return 0;
        }
        if (isLayoutRtl(v)) {
            return (withoutPadding) ? v.getLeft() + getPaddingEnd(v) : v.getLeft();
        } else {
            return (withoutPadding) ? v.getRight() - getPaddingEnd(v) : v.getRight();
        }
    }

    public static int getPaddingStart(View v) {
        if (v == null) {
            return 0;
        }
        return ViewCompat.getPaddingStart(v);
    }

    public static int getPaddingEnd(View v) {
        if (v == null) {
            return 0;
        }
        return ViewCompat.getPaddingEnd(v);
    }

    public static int getPaddingHorizontally(View v) {
        if (v == null) {
            return 0;
        }
        return v.getPaddingLeft() + v.getPaddingRight();
    }

    public static int getMarginStart(View v) {
        if (v == null) {
            return 0;
        }
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
        return MarginLayoutParamsCompat.getMarginStart(lp);
    }

    public static int getMarginEnd(View v) {
        if (v == null) {
            return 0;
        }
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
        return MarginLayoutParamsCompat.getMarginEnd(lp);
    }

    public static int getMarginHorizontally(View v) {
        if (v == null) {
            return 0;
        }
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
        return MarginLayoutParamsCompat.getMarginStart(lp) + MarginLayoutParamsCompat.getMarginEnd(lp);
    }

    public static boolean isLayoutRtl(View v) {
        return ViewCompat.getLayoutDirection(v) == ViewCompat.LAYOUT_DIRECTION_RTL;
    }

    public static int getRatioDimension(Context context, int dimensionId, int standardScreenWidth) {
        if (context != null) {
            try {
                Resources resources = context.getResources();
                int dimension = (int) resources.getDimension(dimensionId);
                int standardWidth = (int) resources.getDimension(standardScreenWidth);
                return ((dimension * getScreenWidth(context)) / standardWidth);
            } catch (Exception e) {
                e.printStackTrace();

                return 0;
            }
        } else {
            return 0;
        }
    }

    public static int getDimension(Context context, int resourcesId) {
        if (context != null) {
            try {
                Resources resources = context.getResources();
                return (int) resources.getDimension(resourcesId);
            } catch (Exception e) {
                e.printStackTrace();

                return 0;
            }
        } else {
            return 0;
        }
    }

    public static int getMeasuredHeight(Context context, View view) {
        if (context instanceof Activity) {
            WindowManager windowManager = ((Activity) context).getWindowManager();
            return getMeasuredHeight(windowManager.getDefaultDisplay(), view);
        } else {
            return 0;
        }
    }

    public static int getMeasuredWidth(Context context, View view) {
        if (context instanceof Activity) {
            WindowManager windowManager = ((Activity) context).getWindowManager();
            return getMeasuredWidth(windowManager.getDefaultDisplay(), view);
        } else {
            return 0;
        }
    }

    public static int getMeasuredHeight(Display display, View view) {
        if (display != null && view != null) {
            view.measure(display.getWidth(), display.getHeight());
            return view.getMeasuredHeight();
        } else {
            return 0;
        }
    }

    public static int getMeasuredWidth(Display display, View view) {
        if (display != null && view != null) {
            view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            return view.getMeasuredWidth();
        } else {
            return 0;
        }
    }

    public static SpannableString getColorSpannableString(String str, int color) {
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(
                new ForegroundColorSpan(color), 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public static int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= Build.VERSION_CODES.M) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    public static Drawable getDrawable(Context context, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getDrawable(id);
        } else {
            return context.getResources().getDrawable(id);
        }
    }

    public static void setClickListner(View view, View.OnClickListener clickListener) {

        if (view != null) {
            view.setOnClickListener(clickListener);
        }
    }

    public static View getActivityRoot(Activity activity) {
        return ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
    }

    public static void setKeyboardEventListener
            (final Activity activity, final KeyBoardVisibilityListener eventListener) {

        if (activity == null) {
            return;
        }

        int softInputMethod = activity.getWindow().getAttributes().softInputMode;
        if (WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE != softInputMethod &&
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED != softInputMethod){
            return;
        }

        if (eventListener == null) {
            return;
        }

        final View activityRoot = getActivityRoot(activity);

        final ViewTreeObserver.OnGlobalLayoutListener layoutListener =
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    private boolean wasOpened = false;

                    @Override
                    public void onGlobalLayout() {
                        Rect r = new Rect();
                        activityRoot.getWindowVisibleDisplayFrame(r);

                        int screenHeight = activityRoot.getRootView().getHeight();
                        if (screenHeight > 0 && hasSoftNavigationBar(activity)) {
                            int navigationBarHeight = getNavigationBarHeight(activity);
                            screenHeight -= navigationBarHeight;
                        }
                        int keyboardHeight = screenHeight - (r.bottom);

                        boolean isOpen = (keyboardHeight >= 100);

                        if (wasOpened != isOpen) {
                            wasOpened = isOpen;

                            if (wasOpened) {
                                eventListener.onKeyBoardShow(keyboardHeight);
                            } else {
                                eventListener.onKeyBoardHide();
                            }
                        }
                    }
                };
        activityRoot.getViewTreeObserver().addOnGlobalLayoutListener(layoutListener);

        activity.getApplication()
                .registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
                    @Override
                    public void onActivityCreated(Activity ac, Bundle savedInstanceState) {

                    }

                    @Override
                    public void onActivityStarted(Activity ac) {

                    }

                    @Override
                    public void onActivityResumed(Activity ac) {

                    }

                    @Override
                    public void onActivityPaused(Activity ac) {

                    }

                    @Override
                    public void onActivityStopped(Activity ac) {

                    }

                    @Override
                    public void onActivitySaveInstanceState(Activity ac, Bundle outState) {

                    }

                    @Override
                    public void onActivityDestroyed(Activity ac) {
                        if (activity == ac) {
                            setGlobalLayoutListenerRemoved(activityRoot, layoutListener);

                            activity.getApplication().unregisterActivityLifecycleCallbacks(this);
                        }
                    }
                });
    }

    public static float convertDpToPx(Context context, float dp) {
        Resources res = context.getResources();
        return dp * (res.getDisplayMetrics().densityDpi / 160f);
    }

    public static void setText(TextView view, String strText) {
        if (view != null) {
            view.setText(strText);
        }
    }

    public static void setText(TextView view, SpannableStringBuilder strText) {
        if (view != null) {
            view.setText(strText);
        }
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    /**
     * 더블 클릭 여부 체크.
     */
    private static long mlLastClickTime = 0;

    /**
     *
     * @return {@link Boolean}
     */
    public static boolean isNotDoubleClick() {
        if (SystemClock.elapsedRealtime() - mlLastClickTime <= ViewConfiguration.getDoubleTapTimeout()) {
            return true;
        }
        mlLastClickTime = SystemClock.elapsedRealtime();

        return false;
    }

    /**
     * @auhtor woozie
     *
     * @param view
     * @param globalLayoutListener
     */
    public static void setGlobalLayoutListenerRemoved(View view , ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener) {
        if (view == null) {
            return;
        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            view.getViewTreeObserver().removeOnGlobalLayoutListener(globalLayoutListener);
        } else {
            view.getViewTreeObserver().removeGlobalOnLayoutListener(globalLayoutListener);
        }
    }

    public static <LM extends LinearLayoutManager> int getScrollOffsetY(LM lm , int position) {
        int scrollOffset = 0;

        if (lm != null) {
            View v = lm.findViewByPosition(position);
            scrollOffset = (int) ViewHelper.getY(v);
        }

        return scrollOffset;
    }

    public static <LM extends LinearLayoutManager> int getScrollOffsetX(LM lm , int position) {
        int scrollOffset = 0;

        if (lm != null) {
            View v = lm.findViewByPosition(position);
            scrollOffset = (int) ViewHelper.getX(v);
        }

        return scrollOffset;
    }

    /**
     * @auhtor woozie
     *
     * @param lm
     * @param position
     * @param <LM>
     * @return
     */
    public static <LM extends LinearLayoutManager> int getListItemVisiblePercent(LM lm , int position) {
        int visiblePercent = 0;

        if (lm != null && position >= 0) {
            View v = lm.findViewByPosition(position);
            visiblePercent = getViewVisiblePercent(v);
        }

        return visiblePercent;
    }

    public static int getGlobalRectTop(View view) {
        if (view != null) {
            Rect rect = new Rect();
            view.getGlobalVisibleRect(rect);
            Log.d("confirm" , ">> confirm getGlobalRectTop rect.top " + rect.top);
            return rect.top;
        }
        Log.d("confirm" , ">> confirm getGlobalRectTop view is null ");
        return 0;
    }

    /**
     *
     * @auhtor woozie
     *
     * @param view
     * @return
     */
    public static int getViewVisiblePercent(View view) {
        int visiblePercent = 0;

        try {
            if (view != null) {
                Rect rect = new Rect();
                view.getGlobalVisibleRect(rect);

                visiblePercent = (rect.height() * 100 / view.getHeight());
            }
        } catch (Exception e) {
        }

        return visiblePercent;
    }

    /**
     *
     * @auhtor woozie
     *
     * @param view
     * @return
     */
    public static int getViewVisibleHeight(View view) {
        int visiblePercent = 0;

        try {
            if (view != null) {
                Rect rect = new Rect();
                view.getGlobalVisibleRect(rect);

                return rect.height();
            }
        } catch (Exception e) {
        }

        return visiblePercent;
    }

    public static void setViewText(View view , String text) {
        if (view != null) {

            if (view instanceof TextView) {
                ((TextView) view).setText(text);
            }
        }
    }

    /**
     *
     * @param recyclerView
     *            RecyclerView
     */
    public static void setListNotifyDataSetChanged(RecyclerView recyclerView) {
        if (recyclerView == null) {
            return;
        }
        if (recyclerView.getAdapter() != null) {
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    /**
     *
     * @param view
     * @param y
     */
    public static void setY(View view , float y) {
        if (view != null) {
            ViewHelper.setY(view , y);
        }
    }

    /**
     *
     * @param view
     *            View
     * @param transitionY
     *            float
     */
    public static void setTranslationY(View view, float transitionY) {
        if (view != null) {
            ViewHelper.setTranslationY(view, transitionY);
        }
    }

    /**
     *
     * @param view
     * @param scaleX
     */
    public static void setScaleX(View view , float scaleX) {
        if (view != null) {
            ViewHelper.setScaleX(view , scaleX);
        }
    }

    /**
     *
     * @param view
     * @param scaleY
     */
    public static void setScaleY(View view , float scaleY) {
        if (view != null) {
            ViewHelper.setScaleY(view , scaleY);
        }
    }

    /**
     *
     * @param view
     *            View
     * @param transitionX
     *            float
     */
    public static void setTranslationX(View view, float transitionX) {
        if (view != null) {
            ViewHelper.setTranslationX(view, transitionX);
        }
    }

    public static Drawable getDrawable(Context paramContext, String paramString) throws IOException {
        return Drawable.createFromStream(paramContext.getAssets().open("drawable/" + paramString), null);
    }

    /**
     *
     * @param view
     *            View
     * @return float
     */
    public static float getY(View view) {
        float viewY = 0.0f;
        if (view != null) {
            viewY = ViewHelper.getY(view);
        }
        return viewY;
    }

    /**
     *
     * @param view
     *            View
     * @return float
     */
    public static float getTranslationY(View view) {
        float transitionY = 0.0f;
        if (view != null) {
            transitionY = ViewHelper.getTranslationY(view);
        }
        return transitionY;
    }

    /**
     *
     * @param button
     *            Button
     * @param bEnable
     *            boolean
     */
    public static void setButtonTextEnable(Button button, boolean bEnable) {
        if (button == null) {
            return;
        }

        setEnabled(button, bEnable);

        if (bEnable) {
            button.setTextColor(Color.parseColor("#ffffff"));
        } else {
            button.setTextColor(Color.parseColor("#bbbbbb"));
        }
    }

    /**
     * View에 Enabled 값 세팅.
     *
     * @param vTarget
     *            : Target View
     * @param bEnabled
     *            : Enabled 여부
     */
    public static void setEnabled(View vTarget, boolean bEnabled) {

        if (vTarget == null) {
            return;
        }

        vTarget.setEnabled(bEnabled);
    }

    /**
     *
     * @param textView
     *            TextView
     * @param bEffect
     *            boolean
     */
    public static void setTextLineBoldEffect(TextView textView, boolean bEffect) {
        if (bEffect) {
            textView.setTextColor(Color.parseColor("#333333"));
        } else {
            textView.setTextColor(Color.parseColor("#bbbbbb"));
        }
    }

    /**
     *
     * @param textView
     *            TextView
     * @param bEffect
     *            boolean
     */
    public static void setUnderlineEffect(TextView textView, boolean bEffect) {
        String content = textView.getText().toString();
        SpannableString spannableString = new SpannableString(content);
        if (bEffect) {
            spannableString.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        } else {
            spannableString.removeSpan(new UnderlineSpan());
        }
        textView.setText(spannableString);
    }

    public static void setAlpha(View view , float alpha) {
        if (view != null) {
            ViewHelper.setAlpha(view , alpha);
        }
    }

    public static void setViewTag(View view , Object o) {
        if (view != null) {
            view.setTag(o);
        }
    }

    public static void setVisibility(View view , int visible) {
        if (view != null) {
            view.setVisibility(visible);
        }
    }

    public static void setClickListener(View view, View.OnClickListener clickListener) {
        if (view != null) {
            view.setOnClickListener(clickListener);
        }
    }

    public static void setClickListener(View view, Object tag, View.OnClickListener clickListener) {
        if (view != null) {
            view.setTag(tag);
            view.setOnClickListener(clickListener);
        }
    }

    public static void setBackgroundBitmapDrawable(ImageView imageView , Bitmap bitmap) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN){
            imageView.setBackground(new BitmapDrawable(imageView.getResources() , bitmap));
        } else {
            imageView.setBackgroundDrawable(new BitmapDrawable(imageView.getResources() , bitmap));
        }
    }

    public static void setBackGroundDrawable(View view , int resourceId) {
        if (view != null) {
            try {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN){
                    view.setBackground(ContextCompat.getDrawable(view.getContext(), resourceId));
                } else {
                    view.setBackgroundDrawable(ContextCompat.getDrawable(view.getContext(), resourceId));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap getBlurImage(Context context , Bitmap sentBitmap , int radius){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN){
            Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
            final RenderScript renderScript = RenderScript.create(context);
            final Allocation inputallocation = Allocation.createFromBitmap(renderScript, sentBitmap, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
            final Allocation outputallocation = Allocation.createTyped(renderScript, inputallocation.getType());
            final ScriptIntrinsicBlur scriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
            scriptIntrinsicBlur.setRadius(radius);
            scriptIntrinsicBlur.setInput(inputallocation);
            scriptIntrinsicBlur.forEach(outputallocation);
            outputallocation.copyTo(bitmap);
            return bitmap;
        } else {
            return sentBitmap;
        }
    }

    public static void setThumbNailImageView(Context context , ImageView imageView , String thumnailPath) {
        try {
            if (!TextUtils.isEmpty(thumnailPath)) {
                Glide.with(context).load(thumnailPath)
                        .centerCrop()
                        .crossFade()
                        .into(imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setThumbNailTopRoundedImageView(final Context context , final ImageView imageView , String thumnailPath) {
        try {
            if (!TextUtils.isEmpty(thumnailPath)) {
                int radius = (int) context.getResources().getDimension(R.dimen.dp25);

                Glide.with(context).load(thumnailPath)
                        .bitmapTransform(new RoundedCornersTransformation(context , radius , 0))
                        .centerCrop()
                        .crossFade()
                        .into(imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setThumbNailRoundedImageView(final Context context , final ImageView imageView , String thumbNailPath , int radiusDimension) {
        try {
            if (!TextUtils.isEmpty(thumbNailPath)) {
                int radius = (int) context.getResources().getDimension(radiusDimension);

                Glide.with(context).load(thumbNailPath)
                        .bitmapTransform(new RoundedCornersTransformation(context , radius , 0))
                        .centerCrop()
                        .crossFade()
                        .into(imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setThumbNailTopRoundedBlurImageView(final Context context , final ImageView imageView , String thumnailPath) {
        try {
            if (!TextUtils.isEmpty(thumnailPath)) {
                int radius = (int) context.getResources().getDimension(R.dimen.dp25);

                Glide.with(context).load(thumnailPath)
                        .bitmapTransform(
                                new RoundedCornersTransformation(context , radius , 0) ,
                                new BlurTransformation(context))
                        .centerCrop()
                        .crossFade()
                        .into(imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static View getViewByPosition(int position, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (position < firstListItemPosition || position > lastListItemPosition) {
            return listView.getAdapter().getView(position, null, listView);
        } else {
            final int childIndex = position - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    public static void setForceKeyboardDown(Context context, EditText editText) {
        try
        {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

            if (inputMethodManager != null)
            {
                inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                inputMethodManager.hideSoftInputFromInputMethod(editText.getWindowToken(), 0);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 소프트키 Show/Hide
     *
     * @param oContext
     * @param oView
     *            : windowToken source
     * @param bShow
     */
    public static void setSoftKeyVisibility(Context oContext, View oView, boolean bShow) {

        if (oContext == null) {
            return;
        }

        if (oView == null) {
            return;
        }

        InputMethodManager oInputMethodManager = (InputMethodManager) oContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (oInputMethodManager == null) {
            return;
        }

        if (bShow) {
            oView.requestFocus();
            oInputMethodManager.showSoftInput(oView, InputMethodManager.SHOW_IMPLICIT);
        } else {
            oInputMethodManager.hideSoftInputFromWindow(oView.getApplicationWindowToken(), 0);
        }
    }

    /**
     * @author JJShin
     *
     */
    public static int getWidthOfScreen(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    /**
     * @author JJShin
     *
     */
    public static int getHeightOfScreen(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

    @MainThread
    public static void set3dRotateAnim(final ViewGroup viewGroup) {
        if (viewGroup == null) {
            return;
        }

        final float centerX = viewGroup.getWidth() / 2.0f;
        final float centerY = viewGroup.getHeight() / 10.0f;

        if (viewGroup.getChildCount() < 2) {
            return;
        }

        final View backView = viewGroup.getChildAt(0);
        final View frontView = viewGroup.getChildAt(1);

        Rotate3dAnimation rotate3dAnimation = new Rotate3dAnimation(0f , 90f, centerX, centerY, 600.0f , true);
        rotate3dAnimation.setDuration(200);
        rotate3dAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                backView.setVisibility(View.VISIBLE);
                backView.bringToFront();
                frontView.setVisibility(View.INVISIBLE);

                Rotate3dAnimation rotate3dAnimationnext = new Rotate3dAnimation(90f, 180f, centerX, centerY , 600.0f , false);
                rotate3dAnimationnext.setDuration(200);
                viewGroup.startAnimation(rotate3dAnimationnext);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        viewGroup.startAnimation(rotate3dAnimation);
    }

    /**
     * @author JJShin
     *
     */
    public static void setAnimationYFrom(Context context, View view, float desty) {
        Animator animator = ObjectAnimator.ofFloat(view, "Y", ViewHelper.getY(view) + desty, ViewHelper.getY(view));
        animator.setDuration(400);
        animator.setInterpolator(AnimationUtils.loadInterpolator(context, android.R.anim.decelerate_interpolator));
        animator.start();
    }

    /**
     * @author JJShin
     *
     */
    public static void setAnimationYStart(Context context, View view, float desty) {
        Animator animator = ObjectAnimator.ofFloat(view, "Y", ViewHelper.getY(view), desty);
        animator.setDuration(400);
        animator.setInterpolator(AnimationUtils.loadInterpolator(context, android.R.anim.decelerate_interpolator));
        animator.start();
    }

    public static void setAnimationEffectBounceRemove(View view) {

        if (view != null) {
            AnimatorSet animatorSet = new AnimatorSet();
            Animator animator0 = ObjectAnimator.ofFloat(view, "ScaleX", ViewHelper.getScaleX(view), 1.0f, 1.05f , 0.0f);
            animator0.setDuration(600);
            Animator animator1 = ObjectAnimator.ofFloat(view, "ScaleY", ViewHelper.getScaleY(view), 1.0f, 1.05f , 0.0f);
            animator1.setDuration(600);
            animatorSet.playTogether(animator0, animator1);
            animatorSet.start();
        }
    }

    public static void setAnimationEffectBounceRemoveAndGone(final View view) {
        AnimatorSet animatorSet = new AnimatorSet();
        Animator animator0 = ObjectAnimator.ofFloat(view, "ScaleX", ViewHelper.getScaleX(view), 1.0f, 1.05f , 0.0f);
        animator0.setDuration(600);
        Animator animator1 = ObjectAnimator.ofFloat(view, "ScaleY", ViewHelper.getScaleY(view), 1.0f, 1.05f , 0.0f);
        animator1.setDuration(600);
        animatorSet.playTogether(animator0, animator1);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                setVisibility(view , View.GONE);
            }
        });
        animatorSet.start();
    }

    public static void setAnimationEffectBounceRemove(View view , final Runnable runnable) {
        if (view != null) {
            float scaleX = 0;
            float scaleY = 0;

            scaleX = ViewHelper.getScaleX(view);
            scaleY = ViewHelper.getScaleX(view);

            AnimatorSet animatorSet = new AnimatorSet();
            Animator animator0 = ObjectAnimator.ofFloat(view, "ScaleX", scaleX, 1.0f, 1.05f , 0.0f);
            animator0.setDuration(600);
            Animator animator1 = ObjectAnimator.ofFloat(view, "ScaleY", scaleY, 1.0f, 1.05f , 0.0f);
            animator1.setDuration(600);
            animatorSet.playTogether(animator0, animator1);
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);

                    if (runnable != null) {
                        runnable.run();
                    }
                }
            });
            animatorSet.start();
        }
    }

    public static void setAnimationEffectBounceRemove(View view , int startDelay ,final Runnable runnable) {
        AnimatorSet animatorSet = new AnimatorSet();
        Animator animator0 = ObjectAnimator.ofFloat(view, "ScaleX", ViewHelper.getScaleX(view), 1.0f, 1.05f , 0.0f);
        animator0.setDuration(600);
        Animator animator1 = ObjectAnimator.ofFloat(view, "ScaleY", ViewHelper.getScaleY(view), 1.0f, 1.05f , 0.0f);
        animator1.setDuration(600);
        animatorSet.playTogether(animator0, animator1);
        animatorSet.setStartDelay(startDelay);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                if (runnable != null) {
                    runnable.run();
                }
            }
        });
        animatorSet.start();
    }

    /**
     * @author JJShin
     *
     */
    public static void setAnimationEffectBounce(View view) {
        AnimatorSet animatorSet = new AnimatorSet();
        Animator animator0 = ObjectAnimator.ofFloat(view, "ScaleX", ViewHelper.getScaleX(view), 1.0f, 1.6f , 0.6f , 1.0f);
        animator0.setDuration(600);
        Animator animator1 = ObjectAnimator.ofFloat(view, "ScaleY", ViewHelper.getScaleY(view), 1.0f, 1.6f , 0.6f , 1.0f);
        animator1.setDuration(600);
        animatorSet.playTogether(animator0, animator1);
        animatorSet.setInterpolator(new OvershootInterpolator());
        animatorSet.start();
    }

    /**
     * @author JJShin
     *
     */
    public static void setAnimationEffectAppearBounce(final View view) {
        ViewHelper.setScaleX(view , 0.0f);
        ViewHelper.setScaleY(view , 0.0f);

        AnimatorSet animatorSet = new AnimatorSet();
        Animator animator0 = ObjectAnimator.ofFloat(view, "ScaleX", ViewHelper.getScaleX(view), 0.0f, 1.1f , 0.95f , 1.0f);
        animator0.setDuration(600);
        Animator animator1 = ObjectAnimator.ofFloat(view, "ScaleY", ViewHelper.getScaleY(view), 0.0f, 1.1f , 0.95f , 1.0f);
        animator1.setDuration(600);
        animatorSet.playTogether(animator0, animator1);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);

                setVisibility(view , View.VISIBLE);
            }
        });
        animatorSet.start();
    }

    public static void setAnimationEffectAppearBounce(final View view , int duration) {
        ViewHelper.setScaleX(view , 0.0f);
        ViewHelper.setScaleY(view , 0.0f);

        AnimatorSet animatorSet = new AnimatorSet();
        Animator animator0 = ObjectAnimator.ofFloat(view, "ScaleX", ViewHelper.getScaleX(view), 0.0f, 1.1f , 0.95f , 1.0f);
        animator0.setDuration(duration);
        Animator animator1 = ObjectAnimator.ofFloat(view, "ScaleY", ViewHelper.getScaleY(view), 0.0f, 1.1f , 0.95f , 1.0f);
        animator1.setDuration(duration);
        animatorSet.playTogether(animator0, animator1);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);

                setVisibility(view , View.VISIBLE);
            }
        });
        animatorSet.start();
    }

    public static void setAnimationEffectFadeAwayWithMovingTop(final View view, int delay ,final Runnable runnable) {
        if (view != null) {
            AnimatorSet animatorSet = new AnimatorSet();

            int screenHeight = getHeightOfScreen(view.getContext());
            int screenHeightPivot = (screenHeight  / 2);
            int transitionY0 = (screenHeightPivot * 3) / 6;
            int transitionY1 = (screenHeightPivot * 4) / 6;

            Animator animator0 = ObjectAnimator.ofFloat(view, "TranslationY", ViewHelper.getTranslationY(view), -transitionY0 , -transitionY1);
            animator0.setDuration(600);
            Animator animator1 = ObjectAnimator.ofFloat(view, "Alpha", ViewHelper.getScaleX(view), 1.0f , 1.0f , 0.0f);
            animator1.setDuration(600);
            animatorSet.playTogether(animator0, animator1);
            animatorSet.setInterpolator(AnimationUtils.loadInterpolator(view.getContext() , android.R.anim.decelerate_interpolator));
            animatorSet.addListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (runnable != null) {
                        runnable.run();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }
            });
            animatorSet.start();
        }
    }

    public static void setAnimationEffectFadeAwayWithMovingTop(final View view, final Runnable runnable) {
        if (view != null) {
            AnimatorSet animatorSet = new AnimatorSet();

            Animator animator0 = ObjectAnimator.ofFloat(view, "TranslationY", 100 , 30 , 10 , 0);
            animator0.setDuration(1500);
            Animator animator1 = ObjectAnimator.ofFloat(view, "Alpha", 0.0f , 1.0f);
            animator1.setDuration(300);
            animatorSet.playTogether(animator0, animator1);
            animatorSet.setInterpolator(AnimationUtils.loadInterpolator(view.getContext() , android.R.anim.decelerate_interpolator));
            animatorSet.addListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator animation) {
                    setVisibility(view , View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (runnable != null) {
                        runnable.run();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }
            });
            animatorSet.start();
        }
    }

    /**
     * @author JJShin
     *
     */
    public static void setAnimationEffectAppearBounce(final View view, final Runnable runnable) {
        if (view != null) {
            ViewHelper.setScaleX(view , 0.0f);
            ViewHelper.setScaleY(view , 0.0f);

            Animator animator2 = ObjectAnimator.ofFloat(view, "Alpha", ViewHelper.getScaleX(view), 0.0f, 1.0f);
            animator2.setDuration(500);
            Animator animator0 = ObjectAnimator.ofFloat(view, "ScaleX", ViewHelper.getScaleX(view), 0.0f, 1.05f , 0.95f , 1.0f);
            animator0.setDuration(1000);
            Animator animator1 = ObjectAnimator.ofFloat(view, "ScaleY", ViewHelper.getScaleY(view), 0.0f, 1.05f , 0.95f , 1.0f);
            animator1.setDuration(1000);

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(animator0, animator1 , animator2);
            animatorSet.addListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator animation) {
                    view.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (runnable != null) {
                        runnable.run();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }
            });
            animatorSet.start();
        }
    }

    public static void setAnimationEffectAppearBounce(final View view, int startDelay ,final Runnable runnable) {
        if (view != null) {
            ViewHelper.setScaleX(view , 0.0f);
            ViewHelper.setScaleY(view , 0.0f);

            Animator animator2 = ObjectAnimator.ofFloat(view, "Alpha", ViewHelper.getScaleX(view), 0.0f, 1.0f);
            animator2.setDuration(500);
            Animator animator0 = ObjectAnimator.ofFloat(view, "ScaleX", ViewHelper.getScaleX(view), 0.0f, 1.05f , 0.95f , 1.0f);
            animator0.setDuration(1000);
            Animator animator1 = ObjectAnimator.ofFloat(view, "ScaleY", ViewHelper.getScaleY(view), 0.0f, 1.05f , 0.95f , 1.0f);
            animator1.setDuration(1000);

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(animator0, animator1 , animator2);
            animatorSet.setStartDelay(startDelay);
            animatorSet.addListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator animation) {
                    view.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (runnable != null) {
                        runnable.run();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }
            });
            animatorSet.start();
        }
    }

    public static void setAnimationEffectAppearBounceAlpha(final View view) {
        if (view != null) {
            view.setVisibility(View.VISIBLE);
            ViewHelper.setScaleX(view , 0.0f);
            ViewHelper.setScaleY(view , 0.0f);

            AnimatorSet animatorSet = new AnimatorSet();
            Animator animator2 = ObjectAnimator.ofFloat(view, "Alpha", ViewHelper.getScaleX(view), 0.0f, 1.0f);
            animator2.setDuration(500);
            Animator animator0 = ObjectAnimator.ofFloat(view, "ScaleX", ViewHelper.getScaleX(view), 0.0f, 1.05f , 0.95f , 1.0f);
            animator0.setDuration(1000);
            Animator animator1 = ObjectAnimator.ofFloat(view, "ScaleY", ViewHelper.getScaleY(view), 0.0f, 1.05f , 0.95f , 1.0f);
            animator1.setDuration(1000);
            animatorSet.playTogether(animator0, animator1 , animator2);
            animatorSet.start();
        }
    }

    public static void setAnimationEffectBounce(final View view, final Runnable runnable) {
        if (view != null) {
            AnimatorSet animatorSet = new AnimatorSet();
            Animator animator0 = ObjectAnimator.ofFloat(view, "ScaleX", ViewHelper.getScaleX(view), 1.0f, 0.9f , 1.05f , 1.0f);
            animator0.setDuration(600);
            Animator animator1 = ObjectAnimator.ofFloat(view, "ScaleY", ViewHelper.getScaleY(view), 1.0f, 0.9f , 1.05f , 1.0f);
            animator1.setDuration(600);
            animatorSet.playTogether(animator0, animator1);
            animatorSet.addListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator animation) {
                    view.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (runnable != null) {
                        runnable.run();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }
            });
            animatorSet.start();
        }
    }

    public static void setAnimationEffectAlphaRemoved(View view) {
        Animator animator = ObjectAnimator.ofFloat(view, "Alpha", ViewHelper.getAlpha(view), 0.0f);
        animator.setDuration(500);
        animator.start();
    }

    /**
     * @author JJShin
     *
     */
    public static void setAnimationEffectAlpha(View view) {
        Animator animator = ObjectAnimator.ofFloat(view, "Alpha", ViewHelper.getAlpha(view), 0.2f, 1.0f);
        animator.setDuration(500);
        animator.start();
    }

    public static void setAnimationEffectAlpha(View view , float targetAlphaValue) {
        Animator animator = ObjectAnimator.ofFloat(view, "Alpha", ViewHelper.getAlpha(view), targetAlphaValue);
        animator.setDuration(500);
        animator.start();
    }

    /**
     * @author JJShin
     *
     */
    public static void setAnimationEffectAlpha(View view, final Runnable runnable) {
        Animator animator = ObjectAnimator.ofFloat(view, "Alpha", ViewHelper.getAlpha(view), 0.2f, 1.0f);
        animator.setDuration(500);
        animator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (runnable != null) {
                    runnable.run();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }
        });
        animator.start();
    }

    /**
     * @author JJShin
     *
     */
    public static void setAnimationEffectAlpha(final View view, int duration ,final Runnable runnable) {
        Animator animator = ObjectAnimator.ofFloat(view, "Alpha", 0.0f, 1.0f);
        animator.setDuration(duration);
        animator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                setVisibility(view , View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (runnable != null) {
                    runnable.run();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }
        });
        animator.start();
    }

    public static void setAnimationEffectAlpha(final View view , float targetAlphaValue , final Runnable runnable) {
        Animator animator = ObjectAnimator.ofFloat(view, "Alpha", ViewHelper.getAlpha(view), targetAlphaValue);
        animator.setDuration(500);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                if (runnable != null) {
                    runnable.run();
                }
            }
        });
        animator.start();
    }

    public static void setAnimationEffectAlpha(final View view , int startDelay ,float targetAlphaValue , final Runnable runnable) {
        final Animator animator = ObjectAnimator.ofFloat(view, "Alpha", ViewHelper.getAlpha(view), targetAlphaValue);
        animator.setDuration(500);
        animator.setStartDelay(startDelay);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                if (runnable != null) {
                    runnable.run();
                }
            }
        });

        new Handler(view.getContext().getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                animator.start();
            }
        });
    }

    /**
     * @author JJShin
     *
     */
    public static void setAnimationEffectResize(Context context, View view, int toWidth, int toHeight) {
        int width = view.getLayoutParams().width;
        int height = view.getLayoutParams().height;
        AnimationResize animationResize = new AnimationResize(view, width, height, toWidth, toHeight);
        animationResize.setDuration(600);
        animationResize.setInterpolator(AnimationUtils.loadInterpolator(context, android.R.anim.decelerate_interpolator));
        view.startAnimation(animationResize);
    }

    /**
     * @author JJShin
     *
     */
    public static void setAnimationEffectResize(Context context, View view, int toWidth, int toHeight, final Runnable runnable) {
        AnimationResize animationResize = new AnimationResize(view, view.getWidth(), view.getHeight(), toWidth, toHeight);
        animationResize.setDuration(400);
        animationResize.setInterpolator(AnimationUtils.loadInterpolator(context, android.R.anim.decelerate_interpolator));
        animationResize.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                new Handler().postDelayed(runnable, 200);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }
        });
        view.startAnimation(animationResize);
    }

    /**
     * @author JJShin
     *
     */
    public static void setAnimationEffectResizeWidth(View view, int toWidth) {
        AnimationResize animationResize = new AnimationResize(view, view.getWidth(), view.getHeight(), toWidth, view.getHeight());
        animationResize.setDuration(200);
        view.startAnimation(animationResize);
    }

    /**
     * @author JJShin
     *
     */
    public static void setAnimationEffectResizeHeight(Context context, View view, int toHeight) {
        AnimationResize animationResize = new AnimationResize(view, view.getWidth(), view.getHeight(), view.getWidth(), toHeight);
        animationResize.setDuration(600);
        animationResize.setInterpolator(AnimationUtils.loadInterpolator(context, android.R.anim.decelerate_interpolator));
        view.startAnimation(animationResize);
    }

    /**
     * @author JJShin
     *
     */
    public static void setAnimationEffectResizeHeight(Context context, View view, int toHeight, final Runnable runnable) {
        AnimationResize animationResize = new AnimationResize(view, view.getWidth(), view.getHeight(), view.getWidth(), toHeight);
        animationResize.setDuration(600);
        animationResize.setInterpolator(AnimationUtils.loadInterpolator(context, android.R.anim.decelerate_interpolator));
        animationResize.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                new Handler().postDelayed(runnable, 100);
            }
        });
        view.startAnimation(animationResize);
    }

    /**
     * @author JJShin
     *
     */
    public static class AnimationResize extends Animation {

        private View moview;
        private float motoheight;
        private float mofromheight;
        private float motowidth;
        private float mofromwidth;

        public AnimationResize(View view, float fromWidth, float fromHeight, float toWidth, float toHeight) {
            motoheight = toHeight;
            motowidth = toWidth;
            mofromheight = fromHeight;
            mofromwidth = fromWidth;
            moview = view;
            setDuration(300);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            float height = (motoheight - mofromheight) * interpolatedTime + mofromheight;
            float width = (motowidth - mofromwidth) * interpolatedTime + mofromwidth;

            moview.getLayoutParams().height = (int) height;
            moview.getLayoutParams().width = (int) width;
            moview.requestLayout();
        }
    }
}
