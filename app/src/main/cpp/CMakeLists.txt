cmake_minimum_required(VERSION 3.22.1)
project("imgui_overlay")

add_library(
    imgui_overlay
    SHARED
    native-lib.cpp
    imgui/imgui.cpp
    imgui/imgui_demo.cpp
    imgui/imgui_draw.cpp
    imgui/imgui_tables.cpp
    imgui/imgui_widgets.cpp
    imgui/backends/imgui_impl_opengl3.cpp
    imgui/backends/imgui_impl_android.cpp
)

target_include_directories(imgui_overlay PRIVATE imgui imgui/backends)

find_library(log-lib log)
find_library(gles-lib GLESv3)
find_library(EGL-lib EGL)

target_link_libraries(
    imgui_overlay
    ${log-lib}
    ${gles-lib}
    ${EGL-lib}
)
