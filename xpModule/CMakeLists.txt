
# Sets the minimum version of CMake required to build the native library.
#指定 需要CMAKE的 最小版本要求
cmake_minimum_required(VERSION 3.4.1)


# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.


#添加一个 远程链接库
add_library( # Sets the name of the library. (我们用C/C++ 文件生成的 动态库 so名字 )
        LVmp

        # Sets the library as a shared library.(表明用于共享的 动态库 so结尾)
        SHARED

        # Provides a relative path to your source file(s).(
        # 这里就是 我们的 C的 文件的 路径 可以修改 有多少个 文件
        # 就添加多个 文件 如果 需要 生成 多个 so就需要 添加多个 add_library)
        src/main/jni/utils/parse.cpp

        src/main/jni/Hook/HLua.cpp

        src/main/jni/main.cpp

        #        Substrate
#        src/main/jni/Substrate/hde64.c
#        src/main/jni/Substrate/SubstrateDebug.cpp
#        src/main/jni/Substrate/SubstrateHook.cpp
#        src/main/jni/Substrate/SubstratePosixMemory.cpp

        #        inlineHook
        src/main/jni/dlfc/dlfcn_nougat.cpp
        src/main/jni/dlfc/dlfcn_compat.cpp
        src/main/jni/inlineHook/inlineHook.cpp
        src/main/jni/inlineHook/relocate.cpp


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

target_link_libraries( # Specifies the target library. 链接 我们自己生成的库 名字要和add_library 一样
        LVmp
        dobby
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
