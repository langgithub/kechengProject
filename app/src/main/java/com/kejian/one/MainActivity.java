package com.kejian.one;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kejian.one.Test.ErrorTest;
import com.kejian.one.Test.Test;
import com.kejian.one.Test.Test1;
import com.kejian.one.Test.TestObject2;
import com.kejian.one.net.Verifier;
import com.kejian.one.utils.CLogUtils;
import com.kejian.one.utils.Encrypt;
import com.kejian.one.utils.PermissionUtils;
import com.kejian.one.utils.RootUtils;
import com.kejian.one.utils.ThreadUtils;
import com.kejian.one.utils.ToastUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private TextView tvText;
    private EditText etInput;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button HttpNet;
    private Button okHttp;
    private Button button4;
    private Button btHook;
    private Button btList;
    private Button btApplist;
    private Button btRegister;
    private Button btNativeHook;
    private Button btTestB;
    private Button checkRoot;
    private Button btPassroot;
    private Button btSyscall;
    private Button btKillerMe;
    private Button btTestXp;
    private Button btDoubleClick;
    private Button btProxy;
    private Button btSmali;
    private Button btInterfaceSmali;
    private TextView mTvText;
    private EditText mEtInput;
    private Button mButton1;
    private Button mButton2;
    private Button mButton3;
    private Button mHttpNet;
    private Button mOkHttp;
    private Button mBtHook;
    private Button mBtApplist;
    private Button mBtRegister;
    private Button mBtNativeHook;
    private Button mBtTestB;
    private Button mCheckRoot;
    private Button mBtPassroot;
    private Button mBtSyscall;
    private Button mBtInlineAsm;
    private Button mBtTestXp;
    private Button mBtDoubleClick;
    private Button mBtProxy;
    private Button mBtSmali;
    private Button mBtInterfaceSmali;
    private Button mBtError;
    private Button mBtFix;
    private Button mBtRegisterHookNative;
    private Button mBtRegisterTest;

    private Button mBtEMD5;
    private Button mBtEBase64;

    static {
        try {
            CLogUtils.e("开始加载 Test so文件 ");
            System.loadLibrary("Test");
            System.loadLibrary("TestB");

        } catch (Throwable e) {
            CLogUtils.e("加载So出现异常 " + e.toString());
            e.printStackTrace();
        }
    }

    private Button mBtIfelse;
    private Button mBtWhile;


    public native String NativeMD5(String str);

    public native String NatibeBase64(String str);


    public native String md5(String str);



    public native void NativeHook();

    public static native void init();

    public native void RegisterNativeHook();

    public native void RegisterNativeHookTest();


    public static native void TestB();

    public native void RegisterNativeTest(String str);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        PermissionUtils.initPermission(this, PermissionUtils.permissionList);

        //PathClassLoader;
        //DexClassLoader;

        //CLogUtils.e(getClassLoader().getClass().getName());

        //getClassLoader().loadClass("")


        init();
        initView();


    }


    public Test mTest = null;

    private void initView() {

        mTest = new TestObject2();

        tvText = (TextView) findViewById(R.id.tv_text);
        etInput = (EditText) findViewById(R.id.et_input);


        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);

        button1.setOnClickListener(v -> {
            String string = etInput.getText().toString().trim();
            if (string.length() == 0) {
                return;
            }
            tvText.setText(md5(string));
        });

        button2.setOnClickListener(v -> tvText.setText(Encrypt.md5(Encrypt.base64(etInput.getText().toString().trim()))));

        button3.setOnClickListener(v -> tvText.setText(Encrypt.byteArrToHex(Encrypt.Encrypt(etInput.getText().toString().trim(), "654321"))));


        HttpNet = (Button) findViewById(R.id.HttpNet);
        okHttp = (Button) findViewById(R.id.okHttp);

        HttpNet.setOnClickListener(v -> HttpConnect());

        okHttp.setOnClickListener(v -> OkHttp());


        btHook = (Button) findViewById(R.id.bt_hook);
        btHook.setOnClickListener(v -> HookTest(1, 2, (a, b) -> ToastUtils.makeToast((a + b) + "", getBaseContext())));


        btList = (Button) findViewById(R.id.bt_Applist);
        btList.setOnClickListener(v -> getAppList());
        btRegister = (Button) findViewById(R.id.bt_register);


        btRegister.setOnClickListener(v -> RegisterNativeTest("123"));


        btNativeHook = (Button) findViewById(R.id.bt_NativeHook);
        btNativeHook.setOnClickListener(v -> NativeHook());

        btTestB = (Button) findViewById(R.id.bt_TestB);
        btTestB.setOnClickListener(v -> TestB());


        checkRoot = (Button) findViewById(R.id.check_root);
        checkRoot.setOnClickListener(v -> CLogUtils.e("检测Root " + RootUtils.isDeviceRooted()));


        btPassroot = (Button) findViewById(R.id.bt_passroot);
        btPassroot.setOnClickListener(v -> PassRootCheck());


        btSyscall = (Button) findViewById(R.id.bt_syscall);
        btSyscall.setOnClickListener(v -> GetSystemCall());

        btKillerMe = (Button) findViewById(R.id.bt_inlineAsm);
        btKillerMe.setOnClickListener(v -> InlineAsm());

        btTestXp = (Button) findViewById(R.id.bt_TestXp);
        btTestXp.setOnClickListener(v -> CLogUtils.e(getText(System.currentTimeMillis() + "")));


        btDoubleClick = (Button) findViewById(R.id.bt_doubleClick);
        btDoubleClick.setOnTouchListener(new SingleDoubleClickListener(new SingleDoubleClickListener.MyClickCallBack() {
            @Override
            public void oneClick() {
                CLogUtils.e("单击");
            }

            @Override
            public void doubleClick() {
                CLogUtils.e("双击");
            }
        }));


        btProxy = (Button) findViewById(R.id.bt_proxy);
        btProxy.setOnClickListener(v -> {
            try {
                Class TestClass = Class.forName("com.kejian.one.Test.Test", false, Test.class.getClassLoader());
                //java.lang.reflect
                Object TestInstance = java.lang.reflect.Proxy.newProxyInstance(Test.class.getClassLoader(),
                        new Class[]{TestClass}, (proxy, method, args) -> {
                            CLogUtils.e("当前调用方法名字是 " + method.getName());
                            //method.invoke(proxy,method);
                            return 2;
                        });
                //方式1
                //TestProxy((Test) TestInstance);
                //方式2
                Method testProxy = MainActivity.class.getDeclaredMethod("TestProxy", TestClass);
                testProxy.setAccessible(true);
                testProxy.invoke(MainActivity.this, TestInstance);


            } catch (Throwable e) {
                e.printStackTrace();
            }
        });


        btSmali = (Button) findViewById(R.id.bt_Smali);
        btSmali.setOnClickListener(v -> CLogUtils.e("当前Hp " + Test1.getHp()));


        btInterfaceSmali = (Button) findViewById(R.id.bt_interfaceSmali);
        btInterfaceSmali.setOnClickListener(v -> TestProxy(mTest));


        mBtError = findViewById(R.id.bt_error);

        mBtError.setOnClickListener(v -> {
            try {
                FixError();
                CLogUtils.e("正常打印 " + ErrorTest.TestError());
            } catch (Exception e) {
                CLogUtils.e("发现异常 " + e);
                e.printStackTrace();
            }
        });


        mBtFix = findViewById(R.id.bt_Fix);
        mBtFix.setOnClickListener(v -> FixError());

        mBtRegisterHookNative = findViewById(R.id.bt_RegisterHookNative);
        mBtRegisterHookNative.setOnClickListener(v -> RegisterNativeHook());

        mBtRegisterTest = findViewById(R.id.bt_RegisterTest);
        mBtRegisterTest.setOnClickListener(v -> RegisterNativeHookTest());


        mBtEMD5 = findViewById(R.id.bt_eMD5);
        mBtEBase64 = findViewById(R.id.bt_eBase64);
        mBtEMD5.setOnClickListener(v -> {
            CLogUtils.e(NativeMD5("1234"));
        });
        mBtEBase64.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



    }

    private void FixError() {
        File dexOutputDir = getDir("dex", 0);

        CLogUtils.e("dexOutputDir  dex  path " + dexOutputDir.getAbsolutePath());
        // 定义DexClassLoader
        // 第一个参数：是dex压缩文件的路径
        // 第二个参数：是dex解压缩后存放的目录
        // 第三个参数：是C/C++依赖的本地库文件目录,可以为null
        // 第四个参数：是上一级的类加载器
        DexClassLoader mDexClassLoader = new DexClassLoader(
                "/storage/emulated/0/dex/1.dex",
                dexOutputDir.getAbsolutePath(),
                null,
                getClassLoader());


        //先拿到 自定义 加载的 classloader
        Object[] MyDexClassloader = getClassLoaderElements(mDexClassLoader);

        //拿到当前进程的 classloader
        Object[] otherClassloader = getClassLoaderElements(getClassLoader());


        if (otherClassloader != null && MyDexClassloader != null) {
            //把两个 数组 DexElements合并 把自己正确的 dex放在前面
            // 这样就可以 在需要的时候 先拿到 我们自己定义的classloader
            // 首先开辟一个 新的 数组 大小 是前里个大小的 和
            Object[] combined = (Object[]) Array.newInstance(otherClassloader.getClass().getComponentType(),
                    MyDexClassloader.length + otherClassloader.length);

            //将自己classloader 数组的内容 放到 前面位置
            System.arraycopy(MyDexClassloader, 0, combined, 0, MyDexClassloader.length);
            //把 原来的 进行 拼接
            System.arraycopy(otherClassloader, 0, combined, MyDexClassloader.length, otherClassloader.length);

            //判断 是否合并 成功
            if ((MyDexClassloader.length + otherClassloader.length) != combined.length) {
                CLogUtils.e("合并 elements数组 失败  null");
            }
            //将 生成的 classloader进行 set回原来的 element数组
            if (SetDexElements(combined, MyDexClassloader.length + otherClassloader.length, getClassLoader())) {
                CLogUtils.e("替换成功");
            } else {
                CLogUtils.e("替换失败 ");
            }
        } else {
            CLogUtils.e("没有 拿到 classloader");
        }
    }

    /**
     * 将 Elements 数组 set回系统的 classloader里面
     */
    private boolean SetDexElements(Object[] dexElementsResut, int conunt, ClassLoader classLoader) {
        try {
            Field pathListField = null;
            if (classLoader instanceof PathClassLoader || classLoader instanceof DexClassLoader) {
                pathListField = Objects.requireNonNull(classLoader.getClass().getSuperclass()).getDeclaredField("pathList");
            } else if (classLoader instanceof BaseDexClassLoader) {
                pathListField = classLoader.getClass().getDeclaredField("pathList");
            }
            if (pathListField != null) {
                pathListField.setAccessible(true);
                Object dexPathList = pathListField.get(classLoader);
                Field dexElementsField = dexPathList.getClass().getDeclaredField("dexElements");
                dexElementsField.setAccessible(true);
                //先 重新设置一次
                dexElementsField.set(dexPathList, dexElementsResut);
                //重新 get 用
                Object[] dexElements = (Object[]) dexElementsField.get(dexPathList);
                if (dexElements.length == conunt && Arrays.hashCode(dexElements) == Arrays.hashCode(dexElementsResut)) {
                    CLogUtils.e("替换 以后的 长度 是 " + dexElements.length);
                    return true;
                } else {
                    CLogUtils.e("合成   长度  " + dexElements.length + "传入 数组 长度   " + conunt);
                    CLogUtils.e("   dexElements hashCode " + Arrays.hashCode(dexElements) + "  " + Arrays.hashCode(dexElementsResut));
                    return false;
                }
            } else {
                CLogUtils.e("SetDexElements  获取 pathList == null");
            }
        } catch (Throwable e) {
            CLogUtils.e("SetDexElements  NoSuchFieldException   " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取classloader里面的elements数组
     */
    private Object[] getClassLoaderElements(ClassLoader classLoader) {
        try {
            Field pathListField = null;
            if (classLoader instanceof PathClassLoader || classLoader instanceof DexClassLoader) {
                pathListField = Objects.requireNonNull(classLoader.getClass().getSuperclass()).getDeclaredField("pathList");
            } else if (classLoader instanceof BaseDexClassLoader) {
                pathListField = classLoader.getClass().getDeclaredField("pathList");
            }
            if (pathListField != null) {
                pathListField.setAccessible(true);
                Object dexPathList = pathListField.get(classLoader);
                Field dexElementsField = dexPathList.getClass().getDeclaredField("dexElements");
                dexElementsField.setAccessible(true);
                Object[] dexElements = (Object[]) dexElementsField.get(dexPathList);
                if (dexElements != null) {
                    return dexElements;
                } else {
                    CLogUtils.e("AddElements  获取 dexElements == null");
                }
                //ArrayUtils.addAll(first, second);
            } else {
                CLogUtils.e("AddElements  获取 pathList == null");
            }
        } catch (Throwable e) {
            CLogUtils.e("AddElements  NoSuchFieldException   " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


    public void TestProxy(Test test) {
        int add = test.add();
        CLogUtils.e("TestProxy返回结果 " + add);
    }

    public native void InlineAsm();


    public static String getText(String text) {
        return ">>>>>>>>>>>  " + text;
    }

    public native void GetSystemCall();


    public static native void PassRootCheck();


    private void getAppList() {
        PackageManager pm = getPackageManager();
        List<PackageInfo> pkgList = pm.getInstalledPackages(0);
        if (pkgList.size() > 0) {
            for (PackageInfo pi : pkgList) {
                if (pi.applicationInfo.publicSourceDir.startsWith("/data/app/")) {
                    CLogUtils.e("dir: " + pi.applicationInfo.publicSourceDir);
                }
            }
        }
    }


    interface Abc {
        public void Add(int a, int b);
    }

    public void HookTest(int a, int b, Abc abc) {
        abc.Add(a, b);
    }

    /**
     * HttpURLConnection 请求网络
     */
    private void HttpConnect() {


        ThreadUtils.runOnNonUIThread(() -> {
            try {
                URL url = new URL("https://www.baidu.com/");
                //得到connection对象。
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                //设置证书验证
                HttpsURLConnection.setDefaultSSLSocketFactory(Verifier.getSSLFactory());
                //信任所有主机名。
                //sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

                HttpsURLConnection.setDefaultHostnameVerifier(new Verifier.MyHostnameVerifier());

                //设置请求方式
                connection.setRequestMethod("GET");
                //连接
                connection.connect();
                //得到响应码
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    //得到响应流
                    InputStream inputStream = connection.getInputStream();
                    //将响应流转换成字符串
                    byte[] bytes = new byte[inputStream.available()];
                    inputStream.read(bytes);
                    String result = new String(bytes);
                    ToastUtils.makeToast("请求成功", getBaseContext());

                    CLogUtils.e("请求返回结果 " + result);
                } else {
                    ToastUtils.makeToast("请求失败 错误码  " + responseCode, getBaseContext());
                }

            } catch (Throwable e) {
                CLogUtils.e("HttpConnect error " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private void OkHttp() {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newBuilder()
                //证书验证
                .sslSocketFactory(Verifier.getSSLFactory())

//                .certificatePinner()
                //域名验证
                .hostnameVerifier(new Verifier.MyHostnameVerifier())
                .proxy(Proxy.NO_PROXY);

        //证书锁定(约束哪些证书是可信的)
        //.certificatePinner()

        final Request request = new Request.Builder()
                .url("http://wwww.baidu.com")
                .get()//默认就是GET请求，可以不写
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                CLogUtils.e("onFailure: " + e.getMessage());
                ToastUtils.makeToast("请求失败 " + e.getMessage(), MainActivity.this);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ToastUtils.makeToast("请求成功 ", MainActivity.this);
            }
        });
    }

}