package com.rea.wajibao.replica.nativecore;

public final class NativeAuthorizationException extends IllegalStateException {
    public NativeAuthorizationException() {
        super("原版 SDK 演示功能尚未授权。请取得适用于当前应用的 SDK 授权后重试；未使用零坐标或演示算法代替原版结果。");
    }
}
