# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.22.1)

# Declares and names the project.

project("application")

include_directories(
        home/user/libusb-1.0.26/android/obj/local/arm64_v8a
        /usr/include
        #/usr/include/x86_64-linux-gnu/
        /home/user/libusb-1.0.26/libusb
        /usr/include/x86_64-linux-gnu/ruby-2.7.0/
        #/home/user/Android/Sdk/ndk-bundle/toolchains/llvm/prebuilt/linux-x86_64/sysroot/usr/lib/x86_64-linux-android
        /home/user/Last/app/src/main/cpp /usr/lib/jvm/java-17-openjdk-amd64/include/
        /usr/lib/jvm/java-17-openjdk-amd64/include/linux usr/include
        /usr/lib
        /usr/local/include/libusb-1.0
        /home/user/Android/Sdk/ndk/25.2.9519653/toolchains/llvm/prebuilt/linux-x86_64/sysroot/usr/include/android/
        #/home/user/Android/Sdk/ndk-bundle/toolchains/llvm/prebuilt/linux-x86_64/sysroot/usr/include/
        #/usr/lib/x86_64-linux-gnu/perl/5.30.0/CORE/
        )

link_directories(/home/user/libusb-1.0.26/android/obj/local/x86_64
        /home/user/Android/Sdk/ndk/25.2.9519653/toolchains/llvm/prebuilt/linux-x86_64/sysroot/usr/include/android
        )


add_library( # Sets the name of the library.
        application

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        usb.c
        #unrooted_android.c
        )

add_library(xyz
        SHARED
        IMPORTED)

set_target_properties(xyz PROPERTIES IMPORTED_LOCATION /home/user/libusb-1.0.26/android/obj/local/arm64-v8a/libusb1.0.so
        )

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)
target_link_libraries( # Specifies the target library.
        application

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib}
        xyz
        )
