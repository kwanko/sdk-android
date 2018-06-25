package fr.kwanko.internal.containers;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import fr.kwanko.KwankoAd;
import fr.kwanko.internal.content.ContentController;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static junit.framework.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * SourceCode
 * Created by erusu on 5/2/2017.
 */
@PrepareForTest(BaseContainer.class)
@RunWith(PowerMockRunner.class)
public class BaseContainerTest {

    @Mock
    Context context;
    @Mock
    KwankoAd parent;
    @Mock
    ContentController contentController;
    BaseContainer container;
    BaseContainer spyContainer;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        container = new BaseContainer(context) {
            @Override
            protected void onSetContentController() {

            }
        };
        spyContainer = spy(container);
    }

    @Test
    public void testSetContentController_onSetContentControllerMethodCalled(){
        spyContainer.setContentController(contentController);
        verify(spyContainer,times(1)).onSetContentController();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetAdView_throwsIllegalStateExceptionForWrongParent(){
        when(spyContainer.getParent()).thenReturn(new FrameLayout(context));
        spyContainer.getAdView();
    }

    @Test
    public void testGetAdView_getParentMethodCalled(){
        when(spyContainer.getParent()).thenReturn(parent);
        spyContainer.getAdView();
        verify(spyContainer,times(2)).getParent();
    }

    @Test
    public void testShow_showOnAdViewIsCalled(){
        when(spyContainer.getParent()).thenReturn(parent);
        spyContainer.show();
        verify(parent,times(1)).show();
    }

    @Test
    public void testOnPause_controllerOnPauseMethodCalled(){
        spyContainer.setContentController(contentController);
        spyContainer.onPause();
        verify(contentController,times(1)).onPause();
    }

    @Test
    public void testOnResume_controllerOnResumeMethodCalled(){
        spyContainer.setContentController(contentController);
        spyContainer.onResume();
        verify(contentController,times(1)).onResume();
    }

    @Test
    public void testOnDestroy_controllerOnDestroyMethodCalled(){
        spyContainer.setContentController(contentController);
        spyContainer.onDestroy();
        verify(contentController,times(1)).onDestroy();
    }

    @Test
    public void testAddContentView_addViewMethodCalled() throws Exception {
        View view = mock(View.class);
        FrameLayout.LayoutParams params = mock(FrameLayout.LayoutParams.class);
        PowerMockito.whenNew(FrameLayout.LayoutParams.class)
                .withArguments(MATCH_PARENT, MATCH_PARENT)
                .thenReturn(params);
        spyContainer.addContentView(view);
        verify(spyContainer).addView(view,0,params);
    }

    @Test
    public void testGetContentController_setContentControllerIsReturned(){
        spyContainer.setContentController(contentController);
        ContentController cc = spyContainer.getContentController();
        assertEquals(contentController,cc);
    }


}
