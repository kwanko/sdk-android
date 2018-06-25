package fr.kwanko.internal.mraid.js;

import android.content.Context;
import android.os.Handler;
import android.webkit.JavascriptInterface;

import fr.kwanko.bridge.WebViewBridge;
import fr.kwanko.common.KwankoDownloadUtils;
import fr.kwanko.internal.content.MraidContentController;
import fr.kwanko.internal.mraid.calendar.CalendarController;
import fr.kwanko.internal.mraid.video.MraidVideoActivity;
import fr.kwanko.model.ExpandProperties;
import fr.kwanko.model.KwankoAdContext;
import fr.kwanko.model.KwankoAdState;
import fr.kwanko.model.PositionProperties;
import fr.kwanko.rest.network.http.HttpStack;

/**
 * Created by vfatu on 14.12.2016.
 */

public class KwankoWebAppInterface {
    private static final String CLOSE = "close";
    private static final String RESIZE = "resize";
    private static final String EXPAND = "expand";
    private static final String OPEN = "open";
    private static final String PLAY_VIDEO = "playVideo";
    private static final String CREATE_CALENDAR_EVENT = "createCalendarEvent";
    private static final String STORE_PICTURE = "storePicture";
    private Context context;
    private MraidContentController contentController;
    private KwankoAdContext adContext;
    private WebViewBridge webViewBridge;
    private EventNotifier eventNotifier;
    private CalendarController calendarController;

    public KwankoWebAppInterface(Context context, MraidContentController contentController,
                                 KwankoAdContext adContext, WebViewBridge webViewBridge) {
        this.context = context;
        this.contentController = contentController;
        this.adContext = adContext;
        this.webViewBridge = webViewBridge;
        this.eventNotifier = new EventNotifier(new HttpStack<>(null));
        this.calendarController = new CalendarController(context);
    }

    @JavascriptInterface
    public void close() {
        Handler mainHandler = new Handler(context.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                contentController.collapse();
            }
        });
        eventNotifier.notify(adContext.getCurrentModel().getMraidEvents(),CLOSE);
    }

    @JavascriptInterface
    public void resize(int offsetX, int offsetY, int width, int height, final String customClosePosition, final boolean allowOffscreen) {
        PositionProperties resizeProperties = new PositionProperties(offsetX, offsetY, width, height);
        adContext.setResizeProperties(resizeProperties);
        Handler mainHandler = new Handler(context.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                contentController.resize(KwankoAdState.RESIZED, customClosePosition, allowOffscreen);
                webViewBridge.notifyCallFinished(RESIZE);
            }
        });
        eventNotifier.notify(adContext.getCurrentModel().getMraidEvents(),RESIZE);
    }

    @JavascriptInterface
    public void expand(String url, final boolean shouldUseCustomClose) {
        Handler mainHandler = new Handler(context.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                ExpandProperties prop = adContext.getExpandProperties();
                if(prop == null){
                    prop = new ExpandProperties();
                    adContext.setExpandProperties(prop);
                }
                prop.setUseCustomClose(shouldUseCustomClose);
                contentController.expand();
                webViewBridge.notifyCallFinished(EXPAND);
            }
        });
        eventNotifier.notify(adContext.getCurrentModel().getMraidEvents(),EXPAND);
    }

    @JavascriptInterface
    public void setOrientationProperties(boolean allowOrientationChange, String forceOrientation) {
        adContext.getOrientationProperties().setAllowOrientationChange(allowOrientationChange);
        adContext.getOrientationProperties().setForceOrientation(forceOrientation);
        Handler mainHandler = new Handler(context.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                contentController.notifyOrientationChangeListener();
                webViewBridge.notifyCallFinished("setOrientationProperties");
            }
        });
        eventNotifier.notify(adContext.getCurrentModel().getMraidEvents(),"setOrientationProperties");
    }

    @JavascriptInterface
    public void open(final String url) {
        Handler mainHandler = new Handler(context.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                OpenExecutor executor =
                        OpenExecutor.forUrlAndStrategy(url,adContext.getCurrentModel().getOpenStrategy());
                if(executor != null){
                    executor.open(context,url);
                }else{
                    webViewBridge.notifyError(OPEN, "Url is not valid");
                }
                webViewBridge.notifyCallFinished(OPEN);
            }
        });
    }

    @JavascriptInterface
    public void createCalendarEvent(final String ... args){
        Handler handler = new Handler(context.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                calendarController.manageCalendarEventFromArgs(args);
                webViewBridge.notifyCallFinished(CREATE_CALENDAR_EVENT);
            }
        });
        eventNotifier.notify(adContext.getCurrentModel().getMraidEvents(), CREATE_CALENDAR_EVENT);
    }

    @JavascriptInterface
    public void playVideo(final String uri){
        Handler handler = new Handler(context.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                MraidVideoActivity.startActivity(context,uri);
                webViewBridge.notifyCallFinished(PLAY_VIDEO);
            }
        });
        eventNotifier.notify(adContext.getCurrentModel().getMraidEvents(), PLAY_VIDEO);
    }

    @JavascriptInterface
    public void storePicture(final String uri){
        Handler handler = new Handler(context.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                KwankoDownloadUtils.download(context,uri);
                webViewBridge.notifyCallFinished(STORE_PICTURE);
            }
        });
    }

}