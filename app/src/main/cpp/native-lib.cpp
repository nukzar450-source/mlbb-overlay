#include <jni.h>
#include <android/log.h>
#include "imgui.h"
#include "backends/imgui_impl_android.h"
#include "backends/imgui_impl_opengl3.h"

#define LOG_TAG "ImGuiOverlay"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

// Переменные состояния
static bool show_menu = true;
static bool feature_1 = false;
static bool feature_2 = false;

// Отрисовка интерфейса ImGui
void DrawMenu() {
    if (!show_menu) return;

    ImGui::SetNextWindowSize(ImVec2(320, 260), ImGuiCond_FirstUseEver);

    if (ImGui::Begin("Mod Menu", &show_menu, ImGuiWindowFlags_NoCollapse)) {
        
        // Центрирование заголовка
        float windowWidth = ImGui::GetWindowSize().x;
        float textWidth = ImGui::CalcTextSize("Expodium External").x;
        ImGui::SetCursorPosX((windowWidth - textWidth) * 0.5f);
        ImGui::Text("Expodium External");

        ImGui::Separator();

        // Кнопки
        if (ImGui::Button("Load Cheat", ImVec2(-1, 40))) {
            // Код действия при нажатии
        }

        if (ImGui::Button("Unban Device", ImVec2(-1, 40))) {
            // Код действия при нажатии
        }

        ImGui::Spacing();

        // Чекбоксы
        ImGui::Checkbox("Enable Feature 1", &feature_1);
        ImGui::Checkbox("Enable Feature 2", &feature_2);

        ImGui::Separator();

        if (ImGui::Button("Join Telegram", ImVec2(-1, 35))) {
            // Код действия при нажатии
        }

        ImGui::End();
    }
}

// JNI: Инициализация ImGui
extern "C" JNIEXPORT jint JNICALL
Java_com_example_overlay_OverlayService_nativeInit(JNIEnv *env, jobject thiz) {
    LOGI("Инициализация ImGui...");

    IMGUI_CHECKVERSION();
    ImGui::CreateContext();
    ImGuiIO& io = ImGui::GetIO();

    ImGui::StyleColorsDark();
    ImGui_ImplOpenGL3_Init("#version 300 es");

    LOGI("ImGui успешно инициализирован!");
    return 0;
}

// JNI: Отрисовка каждого кадра
extern "C" JNIEXPORT void JNICALL
Java_com_example_overlay_OverlayService_nativeRender(JNIEnv *env, jobject thiz) {
    ImGui_ImplOpenGL3_NewFrame();
    ImGui_ImplAndroid_NewFrame();
    ImGui::NewFrame();

    DrawMenu();

    ImGui::Render();
    ImGui_ImplOpenGL3_RenderDrawData(ImGui::GetDrawData());
}
