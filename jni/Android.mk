ROOT_PATH := $(call my-dir)

###########################################################################

include $(CLEAR_VARS)

LOCAL_MODULE     := libcyassl
LOCAL_PATH       := $(ROOT_PATH)/cyassl
LOCAL_C_INCLUDES := $(ROOT_PATH) $(LOCAL_PATH)/include
LOCAL_SRC_FILES  := src/internal.c \
                    src/io.c \
                    src/keys.c \
                    src/sniffer.c \
                    src/ssl.c \
                    src/tls.c \
                    ctaocrypt/src/aes.c \
                    ctaocrypt/src/arc4.c \
                    ctaocrypt/src/asm.c \
                    ctaocrypt/src/asn.c \
                    ctaocrypt/src/coding.c \
                    ctaocrypt/src/des3.c \
                    ctaocrypt/src/dh.c \
                    ctaocrypt/src/dsa.c \
                    ctaocrypt/src/ecc.c \
                    ctaocrypt/src/ecc_fp.c \
	                ctaocrypt/src/hc128.c \
	                ctaocrypt/src/hmac.c \
	                ctaocrypt/src/integer.c \
                    ctaocrypt/src/md4.c \
                    ctaocrypt/src/md5.c \
                    ctaocrypt/src/memory.c \
	                ctaocrypt/src/misc.c \
	                ctaocrypt/src/pwdbased.c \
	                ctaocrypt/src/rabbit.c \
	                ctaocrypt/src/random.c \
                    ctaocrypt/src/ripemd.c \
	                ctaocrypt/src/rsa.c \
	                ctaocrypt/src/sha.c \
                    ctaocrypt/src/sha256.c \
                    ctaocrypt/src/sha512.c \
	                ctaocrypt/src/tfm.c
#                   ctaocrypt/src/aes_asm.s 

LOCAL_CFLAGS     := -DHAVE_CONFIG_H -DBUILDING_CYASSL -DNDEBUG -DNO_HC128 -DNO_PSK -Wall -D_THREAD_SAFE -Os -fomit-frame-pointer

include $(BUILD_STATIC_LIBRARY)

###########################################################################

include $(CLEAR_VARS)

LOCAL_MODULE     := libctctest
LOCAL_PATH       := $(ROOT_PATH)
LOCAL_C_INCLUDES := $(LOCAL_PATH) $(LOCAL_PATH)/cyassl/include
LOCAL_SRC_FILES  := TestCTaoCrypt.c

LOCAL_CFLAGS     := -DHAVE_CONFIG_H -DNO_MAIN_DRIVER -DNDEBUG -DNO_HC128 -DNO_PSK -Wall -Wno-unused -DTHREAD_SAFE
LOCAL_LDLIBS     := -llog

LOCAL_STATIC_LIBRARIES := libcyassl

include $(BUILD_SHARED_LIBRARY)

###########################################################################

