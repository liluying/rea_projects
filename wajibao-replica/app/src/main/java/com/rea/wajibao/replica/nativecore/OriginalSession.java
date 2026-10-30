package com.rea.wajibao.replica.nativecore;

/** Owns the native object explicitly. Never falls back to the demo algorithm. */
public final class OriginalSession implements AutoCloseable {
    private final WajiBackend backend;
    private long pointer;
    public OriginalSession(WajiBackend backend, OriginalProfile profile) {
        this.backend=backend; pointer=backend.create();
        if (pointer == 0) throw new IllegalStateException("创建原版计算对象失败");
        try {
            if (!backend.initMachine(pointer,profile)) throw new IllegalArgumentException("原版核心拒绝机械参数");
            if (!backend.initAttachment(pointer,profile)) throw new IllegalArgumentException("原版核心拒绝附件参数");
        } catch (RuntimeException | Error e) { close(); throw e; }
    }
    public WajiBackend.Result calculate(OriginalFrame frame) {
        if (pointer == 0) throw new IllegalStateException("原版计算会话已关闭");
        if (!backend.demoAuthorized()) throw new NativeAuthorizationException();
        backend.calculate(pointer,frame);
        WajiBackend.Result result = backend.result(pointer);
        if (result.x == 0 && result.y == 0 && result.z == 0 && (frame.x() != 0 || frame.y() != 0 || frame.z() != 0))
            throw new IllegalStateException("原版核心返回空坐标，已停止施工显示。请核对授权和参数。");
        return result;
    }
    @Override public void close() { if (pointer != 0) { long owned=pointer; pointer=0; backend.delete(owned); } }
}
