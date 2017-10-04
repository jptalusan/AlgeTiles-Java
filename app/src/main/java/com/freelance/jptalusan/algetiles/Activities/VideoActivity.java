package com.freelance.jptalusan.algetiles.Activities;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.freelance.jptalusan.algetiles.R;
import com.freelance.jptalusan.algetiles.Utilities.Constants;

/**
 * Created by jptalusan on 9/30/17.
 */

public class VideoActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_video);
        int id = getIntent().getIntExtra(Constants.VIDEO_ID, 0);
        VideoView vv = findViewById(R.id.video);
        MediaController mediaController = new MediaController(this, true);
        vv.setMediaController(mediaController);
        String path = "android.resource://" + this.getPackageName() + "/" + id;

        Uri uri = Uri.parse(path);
        mediaController.setAnchorView(vv);
        //mediaController.Show(2000);
        vv.setVideoURI(uri);
        vv.requestFocus();
        vv.start();
    }
}
