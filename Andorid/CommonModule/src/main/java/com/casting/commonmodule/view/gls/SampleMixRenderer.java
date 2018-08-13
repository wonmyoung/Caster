package com.casting.commonmodule.view.gls;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class SampleMixRenderer implements Renderer
{
  private String ModelName;
  private boolean background_flag;
  private float mAngle = -45.0F;
  private SampleSquare mSquare0;
  private SampleSquare mSquare1;
  private int mTextureID;
  private Context m_Context = null;
  private float xPosition = 3.0F;
  private float yPosition;
  private float zPosition = -6.0F;

  public SampleMixRenderer(boolean paramBoolean, Context paramContext)
  {
    //this.background_flag = paramBoolean;
	this.background_flag = true;
    this.m_Context = paramContext;
    this.mSquare0 = new SampleSquare(1.0F);
    this.mSquare1 = new SampleSquare(0.5F);
  }

  public void onDrawFrame(GL10 paramGL10)
  {
    paramGL10.glClear(16640);
    paramGL10.glMatrixMode(5888);
    paramGL10.glLoadIdentity();
    paramGL10.glTranslatef(-0.5F, -0.3F, -5.5F);
    paramGL10.glRotatef(0.0F, 0.0F, 0.0F, 0.0F);
    this.mSquare0.draw(paramGL10);
    paramGL10.glLoadIdentity();
    paramGL10.glMatrixMode(5888);
    paramGL10.glDisable(3024);
    paramGL10.glTexEnvx(8960, 8704, 8448);
    paramGL10.glTranslatef(this.xPosition, this.yPosition, this.zPosition);
    paramGL10.glRotatef(this.mAngle, 0.0F, 1.0F, 0.0F);
    paramGL10.glEnableClientState(32884);
    paramGL10.glEnableClientState(32888);
    paramGL10.glActiveTexture(5890);
    paramGL10.glBindTexture(3553, this.mTextureID);
    paramGL10.glTexParameterf(3553, 10241, 9728.0F);
    paramGL10.glTexParameterf(3553, 10240, 9729.0F);
    paramGL10.glTexParameterf(3553, 10242, 10497.0F);
    paramGL10.glTexParameterf(3553, 10243, 10497.0F);
    this.mSquare1.draw(paramGL10);
    if (this.mAngle >= -180.0F)
    {
      this.xPosition = ((float)(this.xPosition - 0.025D));
      this.mAngle -= 1.3F;
      return;
    }
    this.xPosition = 3.0F;
    this.mAngle = -45.0F;
  }

  public void onSurfaceChanged(GL10 paramGL10, int paramInt1, int paramInt2)
  {
    if (paramInt2 == 0)
      paramInt2 = 1;
    paramGL10.glViewport(0, 0, paramInt1, paramInt2);
    paramGL10.glMatrixMode(5889);
    paramGL10.glLoadIdentity();
    GLU.gluPerspective(paramGL10, 45.0F, paramInt1 / paramInt2, 0.1F, 100.0F);
    paramGL10.glMatrixMode(5888);
    paramGL10.glLoadIdentity();
  }

	public void onSurfaceCreated(GL10 paramGL10, EGLConfig paramEGLConfig) {
		if (this.background_flag) {
			paramGL10.glClearColor(68/255.0f, 68/255.0f, 68/255.0f, 0.0F);
//			if ("SM-N900S".equals(Build.MODEL)) {
//				this.ModelName = "note3.png";
//				this.yPosition = -0.5F;
//			} else if ("SHV-E250S".equals(Build.MODEL)) {
//				this.ModelName = "note2.png";
//				this.yPosition = 0.5F;
//			} else if ("SHV-E160S".equals(Build.MODEL)) {
//				this.ModelName = "note1.png";
//				this.yPosition = 0.5F;
//			} else if ("SHV-E300S".equals(Build.MODEL)) {
//				this.ModelName = "s4.png";
//				this.yPosition = -0.7F;
//			} else if ("SHV-E210S".equals(Build.MODEL)) {
//				this.ModelName = "s3.png";
//				this.yPosition = -0.7F;
//			} else if ("Nexus 5".equals(Build.MODEL)) {
//				this.ModelName = "Nexus5.jpg";
//				this.yPosition = -0.7F;
//			} else {
//				this.ModelName = "s2.png";
//				this.yPosition = 0.5F;
//			}
		}
		this.ModelName = "note3.png";
		this.yPosition = 0.5F;

		this.mSquare0.loadGLTexture(paramGL10, this.m_Context, this.ModelName);
		this.mSquare1.loadGLTexture(paramGL10, this.m_Context, "card_img.png");
		paramGL10.glEnable(3553);
		paramGL10.glShadeModel(7425);
		paramGL10.glClearDepthf(1.0F);
		paramGL10.glEnable(2929);
		paramGL10.glDepthFunc(515);
		paramGL10.glHint(3152, 4354);
	}
}
