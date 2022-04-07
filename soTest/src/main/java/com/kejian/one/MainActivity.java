package com.kejian.one;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.kejian.one.utils.CLogUtils;

public class MainActivity extends AppCompatActivity {




    static {
        try {
            System.loadLibrary("TestA");
            System.loadLibrary("TestB");

        } catch (Throwable e) {
            CLogUtils.e("加载So出现异常 " + e.toString());
            e.printStackTrace();
        }
    }



    public native void TestA();


    public native void TestB();

    public native void TestC();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TestA();

        TestB();
    }



}