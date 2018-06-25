package fr.kwanko.internal.mraid.video;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.VideoView;

import com.ads.kwanko.kwankoandroidsdk.base.R;

import fr.kwanko.common.KwankoDimensionUtils;
import fr.kwanko.internal.video.BaseVideoController;

import static android.support.v4.content.ContextCompat.*;
import static android.view.ViewGroup.LayoutParams.*;

/**
 * SourceCode
 * Created by erusu on 4/24/2017.
 */

class MraidVideoController extends BaseVideoController {

    private static final int BUTTON_SIZE = 48;
    private static final int BUTTON_PADDING = 8;
    static final String VIDEO_SOURCE = "video_source";

    private VideoView videoView;
    private ImageButton closeButton;

    MraidVideoController(Context context, Intent intent,VideoControllerListener listener) {
        super(context,intent, listener);
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        createVideoView();
        createCloseButton();
        videoView.setVideoURI(Uri.parse(getVideoSource()));
    }

    private void createCloseButton(){
        closeButton = new ImageButton(getContext());
        int size = Math.round(KwankoDimensionUtils.getPixelsFromDp(getContext(),BUTTON_SIZE));
        int padding = Math.round(KwankoDimensionUtils.getPixelsFromDp(getContext(),BUTTON_PADDING));
        closeButton.setPadding(padding,padding,padding,padding);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(size,size);
        params.gravity = Gravity.TOP|Gravity.END;
        closeButton.setImageResource(R.drawable.kwanko_ic_default_close);
        closeButton.setScaleType(ImageView.ScaleType.FIT_XY);
        closeButton.setBackgroundColor(getColor(getContext(),android.R.color.transparent));
        getLayout().addView(closeButton,params);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVideoControllerListener().onFinish();
                videoView.stopPlayback();
            }
        });
        closeButton.setVisibility(View.GONE);
    }

    private void createVideoView(){
        videoView = new VideoView(getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        getLayout().addView(videoView,params);
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                closeButton.setVisibility(View.VISIBLE);
            }
        });
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                closeButton.setVisibility(View.VISIBLE);
                return false;
            }
        });
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.start();
            }
        });
    }

    @Override
    protected String getVideoSource() {
        return getIntent().getStringExtra(VIDEO_SOURCE);
    }

    @Override
    protected void onResume() {
        //do nothing
    }

    @Override
    protected void onPause() {
        //do nothing
    }

    @Override
    protected void onDestroy() {
        //Make this to avoid a memory leak because of VideoView
        if(videoView!=null){
            videoView.stopPlayback();
            videoView.setOnPreparedListener(null);
            videoView.setOnCompletionListener(null);
            videoView.setOnErrorListener(null);
        }
        if(closeButton != null){
            closeButton.setOnClickListener(null);
        }
        videoView = null;
        closeButton = null;
    }
}
