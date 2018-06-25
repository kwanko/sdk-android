package fr.kwanko.internal.mraid.js;

import java.util.Map;
import java.util.concurrent.Executors;

import fr.kwanko.rest.network.http.HttpStack;

/**
 * SourceCode
 * Created by erusu on 3/24/2017.
 */
class EventNotifier {

    private HttpStack stack;

    EventNotifier(HttpStack httpStack) {
        this.stack = httpStack;
    }

    void notify(Map<String,String> mraidEvents,String event){
        if(mraidEvents.containsKey(event)){
            Executors.newCachedThreadPool()
                    .execute(new NotificationRunnable(stack,mraidEvents.get(event)));
        }
    }

    private class NotificationRunnable implements Runnable{

        HttpStack stack ;
        String url;

        NotificationRunnable(HttpStack stack, String url) {
            this.stack = stack;
            this.url = url;
        }

        @Override
        public void run() {
            stack.call(url);
        }
    }


}
