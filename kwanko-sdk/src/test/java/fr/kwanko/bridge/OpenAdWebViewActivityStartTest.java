package fr.kwanko.bridge;

import android.content.Context;
import android.content.Intent;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;

/**
 * SourceCode
 * Created by erusu on 4/26/2017.
 */

@PrepareForTest(OpenAdWebViewActivity.class)
@RunWith(PowerMockRunner.class)
public class OpenAdWebViewActivityStartTest {

    static final String MOCK_URL = "http://www.mock.ro";
    @Mock
    Context context;

    @Before
    public  void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStart_throwsExceptionForNullContextArgument(){
        OpenAdWebViewActivity.start(null,MOCK_URL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStart_throwsExceptionForNullUrlArgument(){
        OpenAdWebViewActivity.start(context,null);
    }

    @Test
    public void testStart_callsStartActivity(){
        OpenAdWebViewActivity.start(context,MOCK_URL);
        verify(context).startActivity(isA(Intent.class));
    }

    @Test
    public void testStart_intentHasUrlInBundle() throws Exception {
        Intent mockIntent = Mockito.mock(Intent.class);
        PowerMockito.whenNew(Intent.class).withAnyArguments().thenReturn(mockIntent);
        OpenAdWebViewActivity.start(context,MOCK_URL);
        verify(mockIntent).putExtra("url",MOCK_URL);
        verify(context).startActivity(mockIntent);
    }
}
