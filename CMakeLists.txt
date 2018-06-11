cmake_minimum_required (VERSION 2.8.0)
project ("ltp4j")

set(BUILD_USE_64BITS on)

find_package(JNI)
set (LTP_HOME "${PROJECT_SOURCE_DIR}/ltp" CACHE STRING "Use to specified ltp path") # change it to your ltp root
set (LIBRARY_OUTPUT_PATH    ${PROJECT_SOURCE_DIR}/libs)
set (JNI_SOURCE_DIR         ${PROJECT_SOURCE_DIR}/jni)

message("LTP_HOME=")
message(${LTP_HOME})

include_directories (
    ${JNI_SOURCE_DIR}
    ${LTP_HOME}/include
    ${JNI_INCLUDE_DIRS})

#if(WIN32)
#    include_directories ($ENV{JAVA_HOME}/include/win32)
#else(WIN32)
#    include_directories ($ENV{JAVA_HOME}/include/linux)
#endif(WIN32)

if(APPLE)
    set(CMAKE_CXX_FLAGS "-stdlib=libstdc++")
endif(APPLE)

if(WIN32)
    message("link_directories for windows")
    link_directories (${LTP_HOME}/lib/Debug)
else(UNIX)
    message("link_directories for unix")
    link_directories (${LTP_HOME}/lib)
ENDIF()

add_library (split_sentence_jni SHARED
    ${JNI_SOURCE_DIR}/edu_hit_ir_ltp4j_SplitSentence.cpp)
target_link_libraries (split_sentence_jni splitsnt)

add_library (segmentor_jni SHARED ${JNI_SOURCE_DIR}/edu_hit_ir_ltp4j_Segmentor.cpp)
target_link_libraries (segmentor_jni segmentor)

add_library(postagger_jni SHARED ${JNI_SOURCE_DIR}/edu_hit_ir_ltp4j_Postagger.cpp)
target_link_libraries (postagger_jni postagger)

add_library (ner_jni SHARED ${JNI_SOURCE_DIR}/edu_hit_ir_ltp4j_NER.cpp)
target_link_libraries (ner_jni ner)

add_library (parser_jni SHARED ${JNI_SOURCE_DIR}/edu_hit_ir_ltp4j_Parser.cpp)
target_link_libraries (parser_jni parser)

#add_library (srl_jni SHARED ${JNI_SOURCE_DIR}/edu_hit_ir_ltp4j_SRL.cpp)
#target_link_libraries (srl_jni srl)

# -----------------------------------------------
# TOOLKIT
