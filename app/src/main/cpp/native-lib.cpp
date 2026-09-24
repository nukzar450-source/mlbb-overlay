#include <jni.h>
#include <android/log.h>
#include "imgui.h"
#include "backends/imgui_impl_android.h"
#include "backends/imgui_impl_opengl3.h"

#define LOG_TAG "ImGuiOverlay"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

extern "C" JNIEXPORT jint JNICALL
Java_com_example_overlay_OverlayService_nativeInit(JNIEnv *env, jobject thiz) {
    LOGI("Инициализация ImGui...");
    
    IMGUI_CHECKVERSION();
    ImGui::CreateContext();
    ImGuiIO& io = ImGui::GetIO();
    
    // Настройка стиля под классические аккуратные прямоугольники
    ImGui::StyleColorsDark();

    // Инициализация OpenGL ES 3 бэкенда
    ImGui_ImplOpenGL3_Init("#version 300 es");

    LOGI("ImGui успешно инициализирован!");
    return 0;
}
