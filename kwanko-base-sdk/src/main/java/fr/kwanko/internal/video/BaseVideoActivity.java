package fr.kwanko.internal.video;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * SourceCode
 * Created by erusu on 4/24/2017.
 */

public abstract class BaseVideoActivity extends AppCompatActivity{

    protected BaseVideoController controller;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controller = createController();
        controller.onCreate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(controller != null){
            controller.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(controller != null){
            controller.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(controller != null){
            controller.onDestroy();
        }
        controller = null;
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (am != null) {
            am.abandonAudioFocus(null);
        }
    }

    public abstract BaseVideoController createController();
}
