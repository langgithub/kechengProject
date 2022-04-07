

#include "main.h"
#include "dobby.h"
#include "../../../../../Dobby/include/dobby.h"


void into_TestSo();

void into_RegisterNative(void *pVoid);

void *getArtSo();

void Test(void *pVoid);

void PassRoot();

jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {

    LOG(ERROR) << "插件 JNI_OnLoad 开始加载 ";
    //在 onload 改变 指定函数 函数地址 替换成自己的
    JNIEnv *env = nullptr;
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) == JNI_OK) {

        PassRoot();

        return JNI_VERSION_1_6;
    }

    return 0;
}










int (*source_openat)(int fd, const char *path, int oflag ,int  mode)= nullptr;

int MyOpenAt(int fd, const char *pathname, int flags ,int  mode) {

    if(strcmp(pathname,"/system/xbin/su") != 0||strcmp(pathname,"/system/bin/su") != 0){
        pathname="/system/xbin/Mysu";
    }

    return source_openat(fd,pathname,flags,mode);
}



int (*source_execve)(const char *filename, char *const argv[], char *const envp[])= nullptr;

int MyExecve(const char *filename, char *const argv[], char *const envp[]) {
    LOG(ERROR) << "execve  "<< filename;

    return source_execve(filename,argv,envp);
}



void PassRoot() {
    LOG(ERROR) << "PassRoot "<< &DobbyBuildVersion;

//    void *openAt =
//            DobbySymbolResolver("libc.so", "__openat");

//    if (openAt == nullptr) {
//        LOG(ERROR) << "openAt null ";
//        return;
//    }
//
//    LOG(ERROR) << "拿到 openAt 地址 ";
//
//    //dobby
//    if(DobbyHook((void *) openAt,
//                 (void *) MyOpenAt,
//                 (void **) &source_openat)==RT_SUCCESS) {
//        LOG(ERROR) << "DobbyHook sucess";
//    }




    void *__execve =
            DobbySymbolResolver("libc.so", "__execve");

    if (__execve == nullptr) {
        LOG(ERROR) << "__execve null ";
        return;
    }

    LOG(ERROR) << "拿到 __execve 地址 ";

    //dobby
    if(DobbyHook((void *) __execve,
                 (void *) MyExecve,
                 (void **) &source_execve)==RT_SUCCESS) {
        LOG(ERROR) << "DobbyHook sucess";
    }


    LOG(ERROR) << "PassRootCheck 执行完毕";
}

//void Test(void *part) {
//    LOG(ERROR) << "开始执行Test  函数 ";
//
//    void *test = dlsym_compat(part,"_ZN3art9JavaVMExt18SetCheckJniEnabledEb");
//    LOG(ERROR) << "Test   函数执行完毕 ";
//
//    if(test!= nullptr){
//        LOG(ERROR) << "1111111111";
//    } else{
//        LOG(ERROR) << "2222222222";
//    }
//
//
//}

//void *getArtSo() {
//    auto *pLibart = dlopen_compat("/system/lib/libart.so", RTLD_NOW);
//    if(pLibart== nullptr){
//        pLibart = dlopen_compat("com.android.runtime|libart.so", RTLD_NOW);
//        //pLibart=dlopen_compat("/apex/com.android.runtime/lib/libart.so", RTLD_NOW);
//    }
//    if(pLibart!= nullptr){
//        LOG(ERROR) << "拿到   Libart ";
//        return pLibart;
//    }
//    LOG(ERROR) << "getArtSo  nullptr ";
//    return nullptr;
//}

void * My_RegisterNative(void* ArtMethodThis,const void* native_method, bool is_fast){
    LOG(ERROR) << "Hook 到  RegisterNative";
    const string &basicString = Source_ArtMethodPrettyMethod(ArtMethodThis, true);

    LOG(ERROR) << "Hook 到  RegisterNative "<<basicString.c_str();

    return Source_RegisterNative(ArtMethodThis,native_method,is_fast);
}

//void into_RegisterNative(void *part) {
//    //_ZN3art9ArtMethod14RegisterNativeEPKvb
//    void *RegisterNative = dlsym_compat(part,
//                                  "_ZN3art9ArtMethod14RegisterNativeEPKvb");
//
//
//
//    if (RegisterNative) {
//
//        //1,我们拿到的原函数的地址
//        //2,我们自己实现对应函数的函数地址
//        //3,被Hook原函数地址
//        if (ELE7EN_OK == registerInlineHook((uint32_t) RegisterNative,
//                                            (uint32_t) My_RegisterNative,
//                                            (uint32_t **) &Source_RegisterNative)) {
//            if (ELE7EN_OK == inlineHook((uint32_t) RegisterNative)) {
//                LOGE("inlineHook RegisterNative success");
//            }
//        }
//    }
//    void *pPertMethod = dlsym_compat(part, "_ZN3art12PrettyMethodEPNS_9ArtMethodEb");
//    if(pPertMethod!= nullptr){
//        Source_ArtMethodPrettyMethod = reinterpret_cast<std::string (*)(void *,bool)>(pPertMethod);
//
//        LOGE("拿到  Source_ArtMethodPrettyMethod 地址");
//    }
//
//
//}

//获取 ArtSo基地址 函数
// 从 lib64和普通的lib里面去寻找
//void *getTestSo() {
//    //1,看对应的Map文件
//    //2，MT管理器去 data/app 下面
//
//    //so路径需注意
//
//    auto *pLibart = dlopen_compat("com.kejian.one|libTest.so", RTLD_NOW);
//
////    auto *pLibart = dlopen_compat(
////            "/data/app/com.kejian.one-X5q5ERrm-bW9b98-rRoKvQ==/lib/arm/libTest.so", RTLD_NOW);
//
//    if (pLibart != nullptr) {
//        LOGE("拿到 TestSo.so");
//        return pLibart;
//    }
//    return nullptr;
//}


jstring My_Java_com_kejian_one_MainActivity_md5(JNIEnv *env, jobject thiz, jstring str) {

    LOG(ERROR) << "插件Hook成功 " << parse::jstring2str(env, str);

    return Source_Java_com_kejian_one_MainActivity_md5(env, thiz, str);
}


//void into_TestSo() {
//    //先拿到对应的So文件句柄
//    void *TestSoLib = getTestSo();
//    if (TestSoLib != nullptr) {
//        LOG(ERROR) << "拿到  TestSo 句柄";
//
//
//        void *Test_MD5 = dlsym_compat(TestSoLib,
//                                      "Java_com_kejian_one_MainActivity_md5");
//        if (Test_MD5) {
//
//            //1,我们拿到的原函数的地址
//            //2,我们自己实现对应函数的函数地址
//            //3,被Hook原函数地址
//            if (ELE7EN_OK == registerInlineHook((uint32_t) Test_MD5,
//                                                (uint32_t) My_Java_com_kejian_one_MainActivity_md5,
//                                                (uint32_t **) &Source_Java_com_kejian_one_MainActivity_md5)) {
//                if (ELE7EN_OK == inlineHook((uint32_t) Test_MD5)) {
//                    LOGE("inlineHook Test_MD5 success");
//                }
//            }
//        }
//    }
//
//}





