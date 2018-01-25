package apps.isfaque.com.canvastest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * Created by apps2 on 1/24/2018.
 */
public class PaintSurface extends SurfaceView implements Runnable, View.OnTouchListener {

    private Path mPath;
    private static final float TOLERANCE = 5;

    // Surface holder allows to control and monitor the surface
    private SurfaceHolder mHolder;

    // A thread where the painting activities are taking place
    private Thread mThread;

    // A flag which controls the start and stop of the repainting of the SurfaceView
    private boolean mFlag = false;

    // X coordinate of the touched position
    private float mX;

    // Y Coordinate of the touched position
    private float mY;

    // Paint
    private Paint mPaint;

    public PaintSurface(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Getting the holder
        mHolder = getHolder();

        // Initializing the X position
        mX = -100;

        // Initializing the Y position
        mY = -100;

        // Initializing the paint object mPaint
        mPaint = new Paint();

        // Setting the color for the paint object
        mPaint.setColor(Color.BLUE);

    }

    public void resume(){
        // Instantiating the thread
        mThread = new Thread(this);

        // setting the mFlag to true for start repainting
        mFlag = true;

        // Start repaint the SurfaceView
        mThread.start();
    }

    public void pause(){
        mFlag = false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        switch(event.getAction()){
            /*
            case MotionEvent.ACTION_DOWN:
                // Getting the X-Coordinate of the touched position
                mX = event.getX();
                // Getting the Y-Coordinate of the touched position
                mY = event.getY();
                break;*/

            case MotionEvent.ACTION_DOWN:
                startTouch(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                moveTouch(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                upTouch();
                invalidate();
                break;
        }
        return true;
    }

    // when ACTION_DOWN start touch according to the x,y values
    private void startTouch(float x, float y) {
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    // when ACTION_MOVE move touch according to the x,y values
    private void moveTouch(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOLERANCE || dy >= TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    public void clearCanvas() {
        mPath.reset();
        invalidate();
    }

    // when ACTION_UP stop touch
    private void upTouch() {
        mPath.lineTo(mX, mY);
    }

    @Override
    public void run() {
        while(mFlag){
            // Check whether the object holds a valid surface
            if(!mHolder.getSurface().isValid())
                continue;
            // Start editing the surface
            Canvas canvas = mHolder.lockCanvas();
            // Draw a background color
            canvas.drawARGB(255, 255, 255, 255);
            // Draw a circle at (mX,mY) with radius 5
            canvas.drawCircle(mX, mY, 5, mPaint);
            // Finish editing the canvas and show to the user
            mHolder.unlockCanvasAndPost(canvas);
        }
    }
}
