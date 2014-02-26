
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := Test118
LOCAL_SRC_FILES := Test118.cpp
LOCAL_LDLIBS    := -lz -ljnigraphics

LOCAL_STATIC_LIBRARIES += libjpeg

LOCAL_IS_SUPPORT_LOG := true
ifeq ($(LOCAL_IS_SUPPORT_LOG),true)
	LOCAL_LDLIBS += -llog
endif

include $(BUILD_SHARED_LIBRARY)
