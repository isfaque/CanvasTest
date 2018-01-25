package apps.isfaque.com.canvastest;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.Random;

/**
 * Created by apps2 on 1/23/2018.
 */
public class VideoCapture extends SurfaceView implements SurfaceHolder.Callback, Runnable, SurfaceView.OnTouchListener{


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



    private MediaRecorder recorder;
    private SurfaceHolder holder;
    public Context context;
    private Camera camera;
    public static String videoPath = Environment.getExternalStorageDirectory()
            .getPath() +"/YOUR_VIDEO.mp4";

    public VideoCapture(Context context) {
        super(context);
        this.context = context;
        this.setZOrderOnTop(true);
        //this.getHolder().setFormat(PixelFormat.TRANSPARENT);
        init();
    }

    public VideoCapture(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setZOrderOnTop(true);
        init();
    }

    public VideoCapture(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setZOrderOnTop(true);
        init();
    }

    @SuppressLint("NewApi")
    public void init() {
        try {
            recorder = new MediaRecorder();
            holder = getHolder();

            // Initializing the X position
            mX = -100;

            // Initializing the Y position
            mY = -100;

            // Initializing the paint object mPaint
            mPaint = new Paint();

            // Setting the color for the paint object
            mPaint.setColor(Color.BLUE);



            holder.addCallback(this);
            //setZOrderOnTop(true);
            holder.setFormat(PixelFormat.TRANSPARENT);

            holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            camera = getCameraInstance();
            if(android.os.Build.VERSION.SDK_INT > 7)
                camera.setDisplayOrientation(90);
            camera.unlock();
            recorder.setCamera(camera);
            recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
            recorder.setOutputFile(videoPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
    }

    public void surfaceCreated(SurfaceHolder mHolder) {
        try {

            recorder.setPreviewDisplay(mHolder.getSurface());
            recorder.prepare();
            recorder.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopCapturingVideo() {
        try {
            recorder.stop();
            camera.lock();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(5)
    public void surfaceDestroyed(SurfaceHolder arg0) {
        if (recorder != null) {
            stopCapturingVideo();
            recorder.release();
            camera.lock();
            camera.release();
            recorder = null;
        }
    }

    private Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c;
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
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                // Getting the X-Coordinate of the touched position
                mX = event.getX();

                // Getting the Y-Coordinate of the touched position
                mY = event.getY();
                break;
        }
        return true;
    }

    @Override
    public void run() {
        while(mFlag){
            // Check whether the object holds a valid surface
            if(!holder.getSurface().isValid())
                continue;
            //Start editing the surface
            Canvas canvas = holder.lockCanvas();

            // Draw a background color
            //canvas.drawARGB(255, 255, 255, 255);

            // Draw a circle at (mX,mY) with radius 5
            canvas.drawCircle(mX, mY, 20, mPaint);

            // Finish editing the canvas and show to the user
            holder.unlockCanvasAndPost(canvas);
        }
    }






}
