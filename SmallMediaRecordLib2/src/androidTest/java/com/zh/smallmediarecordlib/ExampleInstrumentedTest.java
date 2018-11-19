package com.zh.smallmediarecordlib;

import android.content.Context;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.zh.smallmediarecordlib.test", appContext.getPackageName());
    }

    @Test
    public void getSDPath() {
       String CACHE_PATH_EXTERNAL = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + File.separator + "shunlian";

        File sdDir = Environment.getExternalStorageDirectory();
        File eis = new File(CACHE_PATH_EXTERNAL + "/RecordVideo/");
        if (!eis.exists()) {
            eis.mkdir();
        }
        System.out.println(sdDir.toString() + "/RecordVideo/");
        System.out.println(eis.getAbsolutePath());
    }
}
