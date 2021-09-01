#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring

JNICALL
Java_com_halfbyte_danv1_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
extern "C"
JNIEXPORT void JNICALL
Java_com_halfbyte_danv1_NativePanorama_processPanorama(JNIEnv *env, jclass type,
                                                       jlongArray imageAddressArray_,
                                                       jlong outputAddress) {
    jlong *imageAddressArray = env->GetLongArrayElements(imageAddressArray_, NULL);

    // TODO

    env->ReleaseLongArrayElements(imageAddressArray_, imageAddressArray, 0);
}