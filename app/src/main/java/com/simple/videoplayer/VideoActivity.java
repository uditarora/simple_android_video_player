package com.simple.videoplayer;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.ipaulpro.afilechooser.utils.FileUtils;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class VideoActivity extends Activity {
    private static final String TAG = "VideoPlayer";
    private String videoPath;
    private int playbackMode;
    private MediaController mediaController = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        videoPath = intent.getStringExtra("VIDEO_PATH");
        playbackMode = intent.getIntExtra("PLAYBACK_MODE", 1);

        playVideo();
    }

    private void playVideo() {
        Toast.makeText(VideoActivity.this,
                "File Selected: " + videoPath, Toast.LENGTH_LONG).show();
        TextView text = (TextView)findViewById(R.id.textView1);

        final VideoView videoView = (VideoView) findViewById(R.id.videoView);
        videoView.setVideoPath(videoPath);

        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;
        int width = dm.widthPixels;

        if (playbackMode == 1) {
            mediaController = new MediaControllerFixed(VideoActivity.this);
        }
        else if (playbackMode == 2) {
            mediaController = new MediaController(VideoActivity.this);
        }
        else if (playbackMode == 3) {
            mediaController = new MediaControllerRandom(VideoActivity.this);
        }
        else if (playbackMode == 4) {
            mediaController = new MediaControllerColor(VideoActivity.this);
        }
        mediaController.setMediaPlayer(videoView);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.i(TAG, "Duration: "+videoView.getDuration());
                if (playbackMode == 4) {
                    styleMediaController(mediaController);
                }
            }
        });

        videoView.setMediaController(mediaController);
        videoView.requestFocus();

        videoView.setMinimumWidth(width);
        videoView.setMinimumHeight(height);

        videoView.start();
    }

    // MediaController with fixed controls
    private class MediaControllerFixed extends MediaController {
        public MediaControllerFixed(Context context) {
            super(context);
            super.show();
        }

        @Override
        public void hide() {}

        public void hideController() {
            super.hide();
        }

        @Override
        public boolean dispatchKeyEvent(KeyEvent event)
        {
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                ((Activity) getContext()).finish();
            }
            else {
                super.dispatchKeyEvent(event);
            }

            return true;
        }
    }

    // MediaController with controls appearing at random times
    private class MediaControllerRandom extends MediaController {
        public MediaControllerRandom(Context context) {
            super(context);
        }

        @Override
        public void hide() {}

        @Override
        public void show() {}

        public void hideController() {
            super.hide();
        }

        public void showController() {
            super.show();
        }

        public void setVideoLength(int length) {

        }
    }

    // MediaController with random colors in SeekBar
    // MediaController with random colors in SeekBar
    private class MediaControllerColor extends MediaController {
        public MediaControllerColor(Context context) {
            super(context);
        }
    }

    // Assign random colors to the SeekBar in MediaController
    public void styleMediaController(View view) {
        if (view instanceof MediaController) {
            MediaController v = (MediaController) view;
            int count = v.getChildCount();
            for(int i = 0; i < count; i++) {
                styleMediaController(v.getChildAt(i));
            }
        } else
        if (view instanceof LinearLayout) {
            LinearLayout ll = (LinearLayout) view;
            for(int i = 0; i < ll.getChildCount(); i++) {
                styleMediaController(ll.getChildAt(i));
            }
        } else if (view instanceof SeekBar) {
            ((SeekBar) view).setBackgroundColor(Color.argb(0xFF, (int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255)));
            Log.i(TAG, "Styling seekbar");
        }
        else if (view instanceof ImageButton) {
            ((ImageButton) view).setBackgroundColor(Color.argb(0xFF, (int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255)));
        }
    }
}
