package be.kdg.FastradaMobile.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import be.kdg.FastradaMobile.R;

/**
 * Created by Peter Van Akelyen on 26/02/14.
 */
public class AboutActivity extends Activity {
    private int clicks = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        TextView noteView = (TextView) findViewById(R.id.about);
        Linkify.addLinks(noteView, Linkify.ALL);
        final ImageView image = (ImageView) findViewById(R.id.aboutpicture);
        image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                clicks++;
                if (clicks == 7) {
                    image.setImageResource(R.drawable.fastrada2);
                }
                return false;
            }
        });
    }


}
