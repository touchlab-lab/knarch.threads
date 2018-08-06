cmake_minimum_required(VERSION 3.11)
project(cpp)

set(CMAKE_CXX_STANDARD 11)

include_directories(knarch/src/main/cpp)
include_directories(/Users/kgalligan/temp4/kotlin-native-latest/runtime/src/main/cpp)

add_executable(cpp
        knarch/src/main/cpp/ThreadOnly.cpp
        )
