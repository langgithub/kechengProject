package com.example.xphelloword;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * @author Zhenxi on 2020/12/25
 */
public class HelloHook implements IXposedHookLoadPackage {


    /**
     * Hook的包名
     */
    public String pagageName="com.kejian.one";

    /**
     * 方法名字
     */
    public String MethodName="getText";

    /**
     * Hook的类的路径
     */
    public String ClassPath="com.kejian.one.MainActivity";







    public static Context mContext;
    public static ClassLoader mLoader;

    /**
     * 每次加载一个App都会执行的方法
     *
     * @param lpparam
     * @throws Throwable
     */
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
            //如果加载我们需要Hook的包(当被Hook App 启动以后)
            if(lpparam.packageName.equals(pagageName)){
                CLogUtils.e("发现被Hook的 App ");
                HookAttach(lpparam);
            }
    }
    private void HookAttach(XC_LoadPackage.LoadPackageParam lpparam) {

         XposedHelpers.findAndHookMethod(
                Application.class,
                "attach",
                Context.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        CLogUtils.e("走了 attachBaseContext方法 ");
                        mContext = (Context) param.args[0];
                        mLoader = mContext.getClassLoader();
                        CLogUtils.e("拿到classloader");

                        HookMethod();
                    }

                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);

                    }
                });

    }

    private void HookMethod() {
        CLogUtils.e("执行 HookMethod");
        try {
            XposedHelpers.findAndHookMethod(
                    XposedHelpers.findClass(ClassPath, mLoader),
                    MethodName,
                    String.class,

                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            String arg1= (String)param.args[0];
                            CLogUtils.e("原参数1 打印 "+arg1);
                            param.args[0]="你得参数被修改了";

                        }

                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            //param.setResult("你被Hook了");
                        }
                    }
            );
        } catch (Throwable e) {
            CLogUtils.e("HookMethod erro "+e.getMessage());
            e.printStackTrace();
        }
    }

}
