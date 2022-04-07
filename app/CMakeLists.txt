
# Sets the minimum version of CMake required to build the native library.
#指定 需要CMAKE的 最小版本要求
cmake_minimum_required(VERSION 3.4.1)
set(DobbyHome /Users/yuanlang/Work/android/project/Dobby)

##引入加密库OpenSSL，静态链接库
#INCLUDE_DIRECTORIES(
#        src/main/cpp/openssllib/include/openssl/
#)
#LINK_DIRECTORIES(src/main/cpp/openssllib/${ANDROID_ABI}/)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

#添加内联支持
enable_language(C ASM)


#添加一个 远程链接库
add_library( # Sets the name of the library. (我们用C/C++ 文件生成的 动态库 so名字 )
        Test

        # Sets the library as a shared library.(表明用于共享的 动态库 so结尾)
        SHARED


        src/main/jni/main.cpp
        src/main/jni/encrypt/MD5.cpp
        src/main/jni/utils/parse.cpp

        #inlineHook
        src/main/jni/inlineHook/relocate.cpp
        src/main/jni/inlineHook/inlineHook.cpp

        #Utils
        src/main/jni/utils/FileUtils.cpp
        src/main/jni/utils/RegisterNativeUtils.cpp
        #syscall32.s
        src/main/jni/utils/syscall32.s



        #Substrate
#        src/main/jni/Substrate/hde64.c
#        src/main/jni/Substrate/SubstrateDebug.cpp
#        src/main/jni/Substrate/SubstrateHook.cpp
#        src/main/jni/Substrate/SubstratePosixMemory.cpp

        #xHook
#        src/main/jni/xhook/xh_core.c
#        src/main/jni/xhook/xh_elf.c
#        src/main/jni/xhook/xh_jni.c
#        src/main/jni/xhook/xh_log.c
#        src/main/jni/xhook/xh_version.c
#        src/main/jni/xhook/xhook.c
#        src/main/jni/xhook/xh_util.c
        )

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.（调用系统的日志库）
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

# 在A启动的时候进行B的关联
target_link_libraries( # Specifies the target library. 链接 我们自己生成的库 名字要和add_library 一样
        Test
        dobby
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})






#添加一个 远程链接库
add_library( # Sets the name of the library. (我们用C/C++ 文件生成的 动态库 so名字 )
        TestB

        # Sets the library as a shared library.(表明用于共享的 动态库 so结尾)
        SHARED

        src/main/jni/testB/test.cpp
        src/main/jni/utils/logging.h

        #        src/main/jni/encrypt/MyMD5.cpp
        #        src/main/jni/utils/parse.cpp

        )

target_link_libraries( # Specifies the target library. 链接 我们自己生成的库 名字要和add_library 一样
        TestB
        Test
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})






#设置路径,引入Dobby
include_directories(
        ${DobbyHome}/include
        ${DobbyHome}/source
        ${DobbyHome}/builtin-plugin
        ${DobbyHome}/builtin-plugin/AndroidRestriction
        ${DobbyHome}/builtin-plugin/SymbolResolver
        ${DobbyHome}/external/logging
)



macro(SET_OPTION option value)
    set(${option} ${value} CACHE INTERNAL "" FORCE)
endmacro()

SET_OPTION(DOBBY_DEBUG ON)
SET_OPTION(DOBBY_GENERATE_SHARED ON)
SET_OPTION(Plugin.LinkerLoadCallback OFF)

add_subdirectory(/Users/yuanlang/Work/android/project/Dobby dobby.build)



