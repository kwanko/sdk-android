package fr.kwanko.internal.video;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.FrameLayout;

/**
 * SourceCode
 * Created by erusu on 4/24/2017.
 */

public abstract class BaseVideoController {

    private final Context context;
    private final FrameLayout layout;
    private final VideoControllerListener videoControllerListener;
    private final Intent intent;

    public BaseVideoController(Context context, Intent intent,VideoControllerListener listener){
        this.context = context;
        this.videoControllerListener = listener;
        this.layout = new FrameLayout(context);
        this.intent = intent;
    }

    protected void onCreate(){
        layout.setBackgroundColor(Color.BLACK);
        videoControllerListener.setContentView(layout);
    }

    protected abstract void onResume();
    protected abstract void onPause();
    protected abstract void onDestroy();

    protected abstract String getVideoSource();

    protected FrameLayout getLayout(){
        return layout;
    }

    public Context getContext() {
        return context;
    }

    public VideoControllerListener getVideoControllerListener() {
        return videoControllerListener;
    }

    public Intent getIntent() {
        return intent;
    }

    public interface VideoControllerListener{

        void setContentView(View view);
        void setRequestOrientation(int orientation);
        void onFinish();
    }


}
