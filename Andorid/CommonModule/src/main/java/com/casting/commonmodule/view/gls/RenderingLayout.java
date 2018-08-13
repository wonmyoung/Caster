package com.casting.commonmodule.view.gls;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.widget.LinearLayout;


public class RenderingLayout extends LinearLayout {
	public RenderingLayout(Context paramContext) {
		super(paramContext);
		initViewSetting(paramContext);
	}

	public RenderingLayout(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		initViewSetting(paramContext);
	}

	public RenderingLayout(Context paramContext,
			AttributeSet paramAttributeSet, int paramInt) {
		super(paramContext, paramAttributeSet);
		initViewSetting(paramContext);
	}

	public void initViewSetting(Context paramContext) {
		// setOrientation(1);
		setBackgroundColor(Color.parseColor("#444444"));

		GLSurfaceView localGLSurfaceView = new GLSurfaceView(paramContext);
		localGLSurfaceView.setEGLConfigChooser(false);
		localGLSurfaceView.setRenderer(new SampleMixRenderer(false, paramContext));
		localGLSurfaceView.getHolder().setFormat(-2);
		addView(localGLSurfaceView, new LayoutParams(-1, -2));
	}
}