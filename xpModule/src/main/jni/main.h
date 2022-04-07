//
// Created by Administrator on 2020-08-20.
//

#ifndef FENXIANG_MAIN_H
#define FENXIANG_MAIN_H


#include <jni.h>
#include <dlfcn.h>
#include <android/log.h>
#include <string.h>
#include <malloc.h>
#include <stdbool.h>
#include "Hook/HLua.h"
#include <stdlib.h>
#include "utils/parse.h"
#include <stdio.h>
#include "inlineHook/inlineHook.h"
#include "dlfc/dlfcn_compat.h"
#include "dlfc/dlfcn_nougat.h"
#include "utils/Log.h"
#include "utils/logging.h"

jstring  (*Source_Java_com_kejian_one_MainActivity_md5)(JNIEnv *env, jobject thiz, jstring str) = nullptr;

jstring  (*Source_RegisterNative)(void* ArtMethodThis,const void* native_method, bool is_fast) = nullptr;

std::string(*Source_ArtMethodPrettyMethod)(void *ArtMethod, bool with_signature) =NULL;

#endif //FENXIANG_MAIN_H
