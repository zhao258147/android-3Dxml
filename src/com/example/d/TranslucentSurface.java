package com.example.d;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;


/**
 * Wrapper activity demonstrating the use of {@link GLSurfaceView} to
 * display translucent 3D graphics.
 */
public class TranslucentSurface extends Activity {
	private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private final float TRACKBALL_SCALE_FACTOR = 36.0f;
    private CubeRenderer mRenderer;
    private float mPreviousX;
    private float mPreviousY;
    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	//	setContentView(R.layout.activity_main);
        // Create our Preview view and set it as the content of our
        // Activity
        mGLSurfaceView = new TouchSurfaceView(this);
        
        // We want an 8888 pixel format because that's required for
        // a translucent window.
        // And we want a depth buffer.
   //     mGLSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        // Tell the cube renderer that we want to render a translucent version
        // of the cube:
   //     mGLSurfaceView.setRenderer(new CubeRenderer(true));
        // Use a surface format with an Alpha channel:
        mGLSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        mGLSurfaceView.setZOrderOnTop(true);
        setContentView(mGLSurfaceView);
        mScaleDetector = new ScaleGestureDetector(this, new ScaleListener());
        mGLSurfaceView.requestFocus();
        mGLSurfaceView.setFocusableInTouchMode(true);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mGLSurfaceView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mGLSurfaceView.onPause();
	}

	private TouchSurfaceView mGLSurfaceView;
	
	class TouchSurfaceView extends GLSurfaceView {
		public TouchSurfaceView(Context context) {
	        super(context);
	        
	        mRenderer = new CubeRenderer(true);
	        setRenderer(mRenderer);
	//        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	    }
        
        @Override public boolean onTrackballEvent(MotionEvent e) {
            mRenderer.mAngleX += e.getX() * TRACKBALL_SCALE_FACTOR;
            mRenderer.mAngleY += e.getY() * TRACKBALL_SCALE_FACTOR;
            requestRender();
            return true;
        }
        
        @Override public boolean onTouchEvent(MotionEvent e) {
        	mScaleDetector.onTouchEvent(e);
            float x = e.getX();
            float y = e.getY();
            switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dx = x - mPreviousX;
                float dy = y - mPreviousY;
                mRenderer.mAngleX += dx * TOUCH_SCALE_FACTOR;
                mRenderer.mAngleY += dy * TOUCH_SCALE_FACTOR;
                
                requestRender();
            }
            mPreviousX = x;
            mPreviousY = y;
            return true;
        }
        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.save();
            canvas.scale(mScaleFactor, mScaleFactor);
            //...
            //Your onDraw() code
            //...
            canvas.restore();
        }
	}
	
	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
	    @Override
	    public boolean onScale(ScaleGestureDetector detector) {
	        mScaleFactor *= detector.getScaleFactor();
	        
	        // Don't let the object get too small or too large.
	        mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 4.0f));

	        System.out.println(mScaleFactor);
	        mRenderer.scale = mScaleFactor;
	        return true;
	    }
	}
}

