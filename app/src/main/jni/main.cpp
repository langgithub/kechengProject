

#include "main.h"

void RegisterNativeTest(JNIEnv *env, jobject jclazz, jstring str);

#define NATIVE_METHOD(func_ptr, func_name, signature) { func_name, signature, reinterpret_cast<void*>(func_ptr) }


static JNINativeMethod gMethods[] = {
        {"RegisterNativeTest", "(Ljava/lang/String;)V", (void *) RegisterNativeTest},
};

jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {

    LOG(ERROR) << "Test JNI_OnLoad 开始加载";
    //在 onload 改变 指定函数 函数地址 替换成自己的
    JNIEnv *env = nullptr;


    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) == JNI_OK) {
        jclass MainClass = (jclass) env->NewGlobalRef(
                env->FindClass("com/kejian/one/MainActivity"));

        if (env->RegisterNatives(MainClass, gMethods, 1) < 0) {
            return JNI_ERR;
        }
        return JNI_VERSION_1_6;
    }
    return 0;

}


char *secret = nullptr;

extern "C"
JNIEXPORT jstring JNICALL
Java_com_kejian_one_MainActivity_md5(JNIEnv *env, jobject thiz, jstring str) {
    //将jstring转化成jstring类型
    const string &basicString = parse::jstring2str(env, str);

    MD5 md5 = MD5(basicString + secret);

    std::string md5Result = md5.hexdigest();

    LOG(ERROR) << " MD5 返回结果  " << md5Result;

    //将char *类型转化成jstring返回给Java层
    return env->NewStringUTF(md5Result.c_str());
}

extern "C"
JNIEXPORT void JNICALL
Java_com_kejian_one_MainActivity_init(JNIEnv *env, jclass clazz) {
    secret = "abcd";
}



extern "C"
JNIEXPORT void JNICALL
Java_com_kejian_one_MainActivity_TestC(JNIEnv *env, jobject thiz) {
    // TODO: implement TestC()
}


void (*Source_RegisterNativeTest)(JNIEnv *env, jobject jclazz, jstring str) = nullptr;


void RegisterNativeTest(JNIEnv *env, jobject jclazz, jstring str) {
    LOG(ERROR) << "二次注册 RegisterNativeTest 执行";
}


void MyRegisterNativeTest(JNIEnv *env, jobject jclazz, jstring str) {
    LOG(ERROR) << "MyRegisterNativeTest 执行";
    return Source_RegisterNativeTest(env, jclazz, str);
}

void MyTestB() {
    LOG(ERROR) << "MyTestB ";
}


extern "C"
JNIEXPORT void JNICALL
Java_com_kejian_one_MainActivity_NativeHook(JNIEnv *env, jobject thiz) {

    //inlineHook
//    if (ELE7EN_OK == registerInlineHook((uint32_t) 目标函数的地址,
//                                        (uint32_t) 你自己的函数的地址,
//                                        (uint32_t **) &目标函数指针,保存的函数的地址)) {
//        if (ELE7EN_OK == inlineHook((uint32_t) 目标函数的地址)) {
//            LOGE("inlineHook  success");
//        }
//    }

//    if (ELE7EN_OK == registerInlineHook((uint32_t) RegisterNativeTest,
//                                        (uint32_t) MyRegisterNativeTest,
//                                        (uint32_t **) Source_RegisterNativeTest)) {
//        if (ELE7EN_OK == inlineHook((uint32_t) RegisterNativeTest)) {
//            LOGE("inlineHook  success");
//        }
//    }



    //Substrate
//    MSHookFunction((void *)RegisterNativeTest,
//            (void *)MyRegisterNativeTest,
//            (void **)&Source_RegisterNativeTest);




    //dobby
    if (DobbyHook((void *) RegisterNativeTest,
                  (void *) MyRegisterNativeTest,
                  (void **) &Source_RegisterNativeTest) == RT_SUCCESS) {
        LOG(ERROR) << "DobbyHook sucess";
    }


    LOG(ERROR) << "NativeHook 执行完毕";

}



//int MyOpenAt(int fd, const char *path, int oflag, mode_t mode) {
//    if(strcmp(path,"/system/xbin/su") != 0||strcmp(path,"/system/bin/su") != 0){
//        LOG(ERROR) << "MyOpenAt path  " << path;
//        return -1;
//    }
//    return source_openat(fd,path,oflag,mode);
//}



int (*source_openat)(int fd, const char *path, int oflag, int mode) = nullptr;

int MyOpenAt(int fd, const char *pathname, int flags, int mode) {
    //LOG(ERROR) << "MyOpenAt  pathname   "<<pathname;

    if (strcmp(pathname, "/system/xbin/su") != 0 || strcmp(pathname, "/system/bin/su") != 0) {
        pathname = "/system/xbin/Mysu";
    }
    return source_openat(fd, pathname, flags, mode);
}


int (*source_execve)(const char *filename, char *const argv[], char *const envp[]) = nullptr;

int MyExecve(const char *filename, char *const argv[], char *const envp[]) {
    //LOG(ERROR) << "MyExecve filename "<<filename;
    //filename = "MySu";

    if (strcmp(filename, "su") != 0) {
        filename = "Mysu";
    }

    return source_execve(filename, argv, envp);
}

void HookExecve() {
    void *execve =
            DobbySymbolResolver("libc.so", "execve");


    if (execve == nullptr) {
        LOG(ERROR) << "execve null ";
        return;
    }

    LOG(ERROR) << "拿到 execve 地址 ";

    //dobby
    if (DobbyHook((void *) execve,
                  (void *) MyExecve,
                  (void **) &source_execve) == RT_SUCCESS) {
        LOG(ERROR) << "DobbyHook execve sucess";
    }
}

void HookOpenAt() {
    void *__openat =
            DobbySymbolResolver("libc.so", "__openat");

    if (__openat == nullptr) {
        LOG(ERROR) << "__openat null ";
        return;
    }

    LOG(ERROR) << "拿到 __openat 地址 ";

    //dobby
    if (DobbyHook((void *) __openat,
                  (void *) MyOpenAt,
                  (void **) &source_openat) == RT_SUCCESS) {
        LOG(ERROR) << "DobbyHook __openat sucess";
    }
}


extern "C"
JNIEXPORT void JNICALL
Java_com_kejian_one_MainActivity_PassRootCheck(JNIEnv *env, jclass clazz) {
    LOG(ERROR) << "DobbyBuildVersion " << &DobbyBuildVersion;

    HookExecve();

    LOG(ERROR) << "PassRootCheck 执行完毕";
}







extern "C"
JNIEXPORT void JNICALL
Java_com_kejian_one_MainActivity_GetSystemCall(JNIEnv *env, jobject thiz) {
    LOG(ERROR) << "读取文件内容  "
        <<FileUtils::getFileText((char *)"/sys/class/net/p2p0/address",20);
}




//https://gcc.gnu.org/onlinedocs/gcc/Extended-Asm.html
//https://blog.csdn.net/tidyjiang/article/details/52138598
long test_inline_asm_add(long base) {
    long result = 0;

    //arm
    __asm__ __volatile__("mov r0, %[base]\r\n"
                         "add r0, r0 \r\n"
                         "mov %[result], r0\r\n"

    :[result] "=r" (result)     //传出来的结果(输出)
    :[base] "r" (base)                //传进去的参数(输入)
    );
    return result;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_kejian_one_MainActivity_InlineAsm(JNIEnv *env, jobject thiz) {

    //简单的一个加法
    LOG(ERROR) << "返回结果  "<<test_inline_asm_add(20);

}



static JNINativeMethod RegisterNativeHookTest[] = {
        {"RegisterNativeHookTest", "()V", (void *) RegisterNativeTest},
};

extern "C"
JNIEXPORT void JNICALL
Java_com_kejian_one_MainActivity_RegisterNativeHook(JNIEnv *env, jobject thiz) {


    //Hook之前需要先拿到原方法注册的函数地址
    auto MainClass = (jclass) env->NewGlobalRef(env->FindClass("com/kejian/one/MainActivity"));

    jint resut = env->RegisterNatives(MainClass, RegisterNativeHookTest,1);



    if(resut<0){
        LOG(ERROR) << " 注册失败   ";
    } else{
        LOG(ERROR) << " 注册成功   ";
    }
    
}

extern "C"
JNIEXPORT void JNICALL
Java_com_kejian_one_MainActivity_RegisterNativeHookTest(JNIEnv *env, jobject thiz) {
    LOG(ERROR) << " RegisterNativeHookTest 原始函数执行  ";
}

void code_switch(int i){
    switch(i) {
        case 1:
            printf("1111");
        case 2:
            printf("2222");
        case 3:
            printf("3333");
        case 4:
            printf("4444");
        case 5:
            printf("5555");
    }
}


void code_if(int a){
     if(a>5){
         printf("AAAA");
     }
}

void code_for(int i){
    for(int i=0;i<5;i++){
        printf("AAAA");
    }
}

void code_ifElse(int i){
    if(i>5){
        printf("AAAA");
    }else if(i==5){
        printf("BBBB");
    }else{
        printf("CCCC");
    }
}

void code_while(int i){
    while (i<5){
        i++;
    }
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_kejian_one_MainActivity_NatibeBase64(JNIEnv *env, jobject thiz, jstring str) {

    return nullptr;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_kejian_one_MainActivity_NativeMD5(JNIEnv *env, jobject thiz, jstring str) {
    // 初始化Md5
    MD5 md5 = MD5(parse::jstring2str(env, str));
    // 加密
    std::string md5Result = md5.hexdigest();

    LOG(ERROR) << " NativeMD5 返回结果  " << md5Result;


    return parse::char2jstring(env,md5Result.c_str());
}