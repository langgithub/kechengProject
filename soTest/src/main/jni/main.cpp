


#include "main.h"
#include "test/test.h"


jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {

    LOG(ERROR) << "JNI_OnLoad 开始加载";
    //在 onload 改变 指定函数 函数地址 替换成自己的
    JNIEnv *env = nullptr;

    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) == JNI_OK) {
//        jclass MainClass = (jclass) env->NewGlobalRef(env->FindClass("com/kejian/one/MainActivity"));
//
//        if (env->RegisterNatives(MainClass, gMethods, 1) < 0) {
//            return JNI_ERR;
//        }
        return JNI_VERSION_1_6;
    }
    return 0;

}


extern "C"
JNIEXPORT void JNICALL
Java_com_kejian_sotest_MainActivity_TestA(JNIEnv *env, jobject thiz) {
    LOG(ERROR) << "AAAAAAAAAAAAA";

}


extern "C"
JNIEXPORT void JNICALL
Java_com_kejian_sotest_MainActivity_TestB(JNIEnv *env, jobject thiz) {
    test::TestB();
}


extern "C"
JNIEXPORT void JNICALL
Java_com_kejian_sotest_MainActivity_TestC(JNIEnv *env, jobject thiz) {
    //LibC 函数
    clock_t i = clock();
}