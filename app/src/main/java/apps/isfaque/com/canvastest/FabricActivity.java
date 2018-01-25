package apps.isfaque.com.canvastest;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by apps2 on 1/25/2018.
 */
public class FabricActivity extends Activity {

    FabricView fabricView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fabric);

        fabricView = (FabricView) findViewById(R.id.fabricview);



    }
}
