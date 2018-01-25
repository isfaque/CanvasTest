package apps.isfaque.com.canvastest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by apps2 on 1/23/2018.
 */
public class CaptureVideo extends Activity {

    private VideoCapture videoCapture;
    private Button stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_capture);
        videoCapture = (VideoCapture) findViewById(R.id.videoView);
        // Setting the touch listener
        //videoCapture.setZOrderOnTop(true);
        videoCapture.setOnTouchListener(videoCapture);
        stop= (Button) findViewById(R.id.stop);
        stop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                videoCapture.stopCapturingVideo();
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        videoCapture.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoCapture.pause();
    }



}
