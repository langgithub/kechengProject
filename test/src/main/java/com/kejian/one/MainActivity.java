package com.kejian.one;

import com.kejian.test.CLogUtils;

/**
 * Created by Lyh on
 * 2021/3/27
 */

public class MainActivity {


    static {
        try {
            System.loadLibrary("Test");
            CLogUtils.e("So加载完毕");
            init();
        } catch (Throwable e) {
            CLogUtils.e("加载So error "+e.getMessage());
            e.printStackTrace();
        }
    }

    public static native void init();

    public native String md5(String str);

    public native void RegisterNativeTest(String str);





}
