package com.yunji.deliveryman.other.log;

import android.content.Context;
import android.text.TextUtils;

import static com.yunji.deliveryman.other.log.FLConst.Level.D;
import static com.yunji.deliveryman.other.log.FLConst.Level.E;
import static com.yunji.deliveryman.other.log.FLConst.Level.I;
import static com.yunji.deliveryman.other.log.FLConst.Level.V;
import static com.yunji.deliveryman.other.log.FLConst.Level.W;


public class FL {

    private volatile static boolean sEnabled;
    private volatile static FLConfig sConfig;

    public static void setEnabled(boolean enabled) {
        sEnabled = enabled;
    }

    public static void init(Context context) {
        init(new FLConfig.Builder(context).build());
    }

    public static void init(FLConfig config) {
        sConfig = config;
    }

    public static void v(String fmt, Object... args) {
        v(null, fmt, args);
    }

    public static void v(String tag, String fmt, Object... args) {
        log(V, tag, FLUtil.format(fmt, args));
    }

    public static void d(String fmt, Object... args) {
        d(null, fmt, args);
    }

    public static void d(String tag, String fmt, Object... args) {
        log(D, tag, FLUtil.format(fmt, args));
    }

    public static void i(String fmt, Object... args) {
        log(I, null, FLUtil.format(fmt, args));
    }

    public static void i(String tag, String fmt, Object... args) {
        log(I, tag, FLUtil.format(fmt, args));
    }

    public static void w(String fmt, Object... args) {
        w(null, fmt, args);
    }

    public static void w(String tag, String fmt, Object... args) {
        log(W, tag, FLUtil.format(fmt, args));
    }

    public static void e(String fmt, Object... args) {
        e((String) null, fmt, args);
    }

    public static void e(String tag, String fmt, Object... args) {
        log(E, tag, FLUtil.format(fmt, args));
    }

    public static void e(Throwable tr) {
        e(null, tr);
    }

    public static void e(String tag, Throwable tr) {
        e(tag, tr, null);
    }

    public static void e(Throwable tr, String fmt, Object... args) {
        e(null, tr, fmt, args);
    }

    public static void e(String tag, Throwable tr, String fmt, Object... args) {
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(fmt)) {
            sb.append(FLUtil.format(fmt, args));
            sb.append("\n");
        }
        if (tr != null) {
            sb.append(FLUtil.formatThrowable(tr));
        }
        log(E, tag, sb.toString());
    }

    private static void log(FLConst.Level level, String tag, String log) {
        if (!sEnabled) {
            return;
        }

        ensureStatus();

        FLConfig config = sConfig;
        if (TextUtils.isEmpty(tag)) {
            tag = config.b.defaultTag;
        }

        Loggable logger = config.b.logger;
        if (logger != null) {
            switch (level) {
                case V:
                    logger.v(tag, log);
                    break;
                case D:
                    logger.d(tag, log);
                    break;
                case I:
                    logger.i(tag, log);
                    break;
                case W:
                    logger.w(tag, log);
                    break;
                case E:
                    logger.e(tag, log);
                    break;
            }
        }

        if (config.b.logToFile && !TextUtils.isEmpty(config.b.dirPath)) {
            long timeMs = System.currentTimeMillis();
            String fileName = config.b.formatter.formatFileName(timeMs);
            String line = config.b.formatter.formatLine(timeMs, level.name(), tag, log);
            FileLoggerService.logFile(config.b.context, fileName, config.b.dirPath, line,
                    config.b.retentionPolicy, config.b.maxFileCount, config.b.maxSize);
        }
    }

    private static void ensureStatus() {
        if (sConfig == null) {
            throw new IllegalStateException(
                    "FileLogger is not initialized. Forgot to call FL.init()?");
        }
    }
}
