package xmlparsingtest.ranglerz.com.xmlparsingtest;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.VideoView;

public class AboutTest extends AppCompatActivity {

    MediaController mediacontroller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mediacontroller = new MediaController(AboutTest.this);

        VideoView mVideoView2 = (VideoView)findViewById(R.id.videoView1);
        String uriPath2 = "android.resource://" + getPackageName() + "/" + R.raw.about;
        Uri uri2 = Uri.parse(uriPath2);
        mVideoView2.setVideoURI(uri2);
        mVideoView2.requestFocus();

        mVideoView2.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediacontroller.show(0);
            }
        });

        mediacontroller.setAnchorView(mVideoView2);

        mVideoView2.setMediaController(mediacontroller);

        mVideoView2.start();


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mediacontroller.hide();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediacontroller.hide();
    }

}
