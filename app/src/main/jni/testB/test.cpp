//
// Created by Administrator on 2020-10-31.
//

#include <jni.h>
#include "test.h"


extern "C"
void test::TestB(JNIEnv *env, jobject thiz_or_clazz) {
    LOG(ERROR) << "BBBBBBBBBBB";
}

static JNINativeMethod gMethods[] = {
        {"TestB", "()V",  (void *) test::TestB},
};


jint JNICALL
JNI_OnLoad(JavaVM *vm, void *reserved) {

    LOG(ERROR) << "TestB  JNI_OnLoad 开始加载";
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