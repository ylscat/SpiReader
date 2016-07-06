LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_CFLAGS += -fPIE
LOCAL_LDFLAGS += -fPIE -pie

LOCAL_MODULE := read_gpio
LOCAL_SRC_FILES := read_gpio.c


include $(BUILD_EXECUTABLE)