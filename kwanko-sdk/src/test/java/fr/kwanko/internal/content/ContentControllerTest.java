package fr.kwanko.internal.content;

import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fr.kwanko.internal.close.CloseEvents;
import fr.kwanko.internal.close.OnCloseListener;
import fr.kwanko.internal.containers.BaseContainer;
import fr.kwanko.internal.model.AdModel;
import fr.kwanko.internal.model.CloseButtonMetadata;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * SourceCode
 * Created by erusu on 6/14/2017.
 */

@RunWith(JUnit4.class)
public class ContentControllerTest  {

    @Mock
    BaseContainer baseContainer;

    private ContentController contentController;

    @Mock
    OnCloseListener listenerForGeneral;
    @Mock
    OnCloseListener secondListenerForGeneral;
    @Mock
    OnCloseListener listenerForResize;
    @Mock
    OnCloseListener listenerForExpand;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        contentController = new ContentController(baseContainer,null) {
            @Override
            public void onCloseButtonClicked(View view) {

            }

            @Override
            public void loadContentBasedOn(AdModel adModel) {

            }
        };
        contentController.subscribeForCloseEvent(CloseEvents.GENERAL,listenerForGeneral);
        contentController.subscribeForCloseEvent(CloseEvents.GENERAL,secondListenerForGeneral);
        contentController.subscribeForCloseEvent(CloseEvents.RESIZE,listenerForResize);
        contentController.subscribeForCloseEvent(CloseEvents.EXPAND,listenerForExpand);
    }

    @Test
    public void testOnCloseResizeEvent_onlyResizeCloseEventListenersAreCalled(){
        contentController.notifyCloseListener(null,CloseEvents.RESIZE);
        verify(listenerForResize, times(1)).onCloseEvent(null,CloseEvents.RESIZE);
        verify(listenerForExpand,never()).onCloseEvent(null,CloseEvents.RESIZE);
        verify(listenerForGeneral,never()).onCloseEvent(null,CloseEvents.RESIZE);
        verify(secondListenerForGeneral, never()).onCloseEvent(null, CloseEvents.RESIZE);
    }

    @Test
    public void testOnCloseExpandEvent_onlyExpandCloseEventListenerAreCalled(){
        contentController.notifyCloseListener(null,CloseEvents.EXPAND);
        verify(listenerForExpand, times(1)).onCloseEvent(null,CloseEvents.EXPAND);
        verify(listenerForResize,never()).onCloseEvent(null,CloseEvents.EXPAND);
        verify(listenerForGeneral,never()).onCloseEvent(null,CloseEvents.EXPAND);
        verify(secondListenerForGeneral, never()).onCloseEvent(null, CloseEvents.EXPAND);
    }

    @Test
    public void testOnCloseGeneralEvent_onlyGeneralCloseEventListenerAreCalled(){
        contentController.notifyCloseListener(null,CloseEvents.GENERAL);
        verify(listenerForResize, never()).onCloseEvent(null,CloseEvents.GENERAL);
        verify(listenerForExpand,never()).onCloseEvent(null,CloseEvents.GENERAL);
        verify(listenerForGeneral,times(1)).onCloseEvent(null,CloseEvents.GENERAL);
        verify(secondListenerForGeneral, times(1)).onCloseEvent(null, CloseEvents.GENERAL);
    }

    @Test
    public void testOnUnSubscribeFromGeneralEvent_theListenerIsNotCalled(){
        contentController.unSubscribeForCloseEvent(CloseEvents.GENERAL,listenerForGeneral);
        contentController.notifyCloseListener(null,CloseEvents.GENERAL);
        verify(listenerForGeneral,never()).onCloseEvent(null,CloseEvents.GENERAL);
    }
}