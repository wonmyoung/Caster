package com.casting.commonmodule.view.gls;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.opengl.GLUtils;

import com.casting.commonmodule.utility.UtilityUI;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class SampleSquare
{
  private float VERT = 1.0F;
  private byte[] indices;
  private ByteBuffer m_indexBuffer;
  private FloatBuffer m_textureBuffer;
  private int[] m_textures = new int[1];
  private FloatBuffer m_vertexBuffer;
  private float[] texture;
  private float[] vertices;
  //private BitmapDrawable localBitmapDrawable;

  public SampleSquare(float paramFloat)
  {
    float[] arrayOfFloat = new float[12];
    arrayOfFloat[0] = (-this.VERT);
    arrayOfFloat[1] = ((float)(1.5D * -this.VERT));
    arrayOfFloat[2] = this.VERT;
    arrayOfFloat[3] = ((float)(1.5D * this.VERT));
    arrayOfFloat[4] = ((float)(1.5D * -this.VERT));
    arrayOfFloat[5] = this.VERT;
    arrayOfFloat[6] = (-this.VERT);
    arrayOfFloat[7] = (2.0F * this.VERT);
    arrayOfFloat[8] = this.VERT;
    arrayOfFloat[9] = ((float)(1.5D * this.VERT));
    arrayOfFloat[10] = (2.0F * this.VERT);
    arrayOfFloat[11] = this.VERT;
    this.vertices = arrayOfFloat;
    this.texture = new float[] { 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, 1.0F, 1.0F };
    byte[] arrayOfByte = new byte[6];
    arrayOfByte[1] = 1;
    arrayOfByte[2] = 3;
    arrayOfByte[4] = 3;
    arrayOfByte[5] = 2;
    this.indices = arrayOfByte;
    this.VERT = paramFloat;
    ByteBuffer localByteBuffer1 = ByteBuffer.allocateDirect(4 * this.vertices.length);
    localByteBuffer1.order(ByteOrder.nativeOrder());
    this.m_vertexBuffer = localByteBuffer1.asFloatBuffer();
    this.m_vertexBuffer.put(this.vertices);
    this.m_vertexBuffer.position(0);
    ByteBuffer localByteBuffer2 = ByteBuffer.allocateDirect(4 * this.texture.length);
    localByteBuffer2.order(ByteOrder.nativeOrder());
    this.m_textureBuffer = localByteBuffer2.asFloatBuffer();
    this.m_textureBuffer.put(this.texture);
    this.m_textureBuffer.position(0);
    this.m_indexBuffer = ByteBuffer.allocateDirect(this.indices.length);
    this.m_indexBuffer.put(this.indices);
    this.m_indexBuffer.position(0);
  }

  public void draw(GL10 paramGL10)
  {
    paramGL10.glBindTexture(3553, this.m_textures[0]);
    paramGL10.glEnableClientState(32884);
    paramGL10.glEnableClientState(32888);
    paramGL10.glFrontFace(2304);
    paramGL10.glVertexPointer(3, 5126, 0, this.m_vertexBuffer);
    paramGL10.glTexCoordPointer(2, 5126, 0, this.m_textureBuffer);
    paramGL10.glDrawElements(4, this.indices.length, 5121, this.m_indexBuffer);
    paramGL10.glDisableClientState(32884);
    paramGL10.glDisableClientState(32888);
  }

  public void loadGLTexture(GL10 paramGL10, Context paramContext, String paramString)
  {
    try
    {
      BitmapDrawable localBitmapDrawable = (BitmapDrawable) UtilityUI.getDrawable(paramContext, paramString);
      Bitmap localBitmap = localBitmapDrawable.getBitmap();
      paramGL10.glGenTextures(1, this.m_textures, 0);
      paramGL10.glBindTexture(3553, this.m_textures[0]);
      paramGL10.glTexParameterf(3553, 10241, 9728.0F);
      paramGL10.glTexParameterf(3553, 10240, 9729.0F);
      paramGL10.glTexParameterf(3553, 10242, 10497.0F);
      paramGL10.glTexParameterf(3553, 10243, 10497.0F);
      GLUtils.texImage2D(3553, 0, localBitmap, 0);
      localBitmap.recycle();
      return;
    }
    catch (IOException localIOException)
    {
        localIOException.printStackTrace();
        BitmapDrawable localBitmapDrawable = null;
    }
  }
}
