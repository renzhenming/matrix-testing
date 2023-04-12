package com.rzm.matrix_testing;

import android.app.Application;

import com.tencent.matrix.Matrix;
import com.tencent.matrix.trace.TracePlugin;
import com.tencent.matrix.trace.config.TraceConfig;
import com.tencent.matrix.util.MatrixLog;

import java.io.File;

public class MyApplication extends Application {
    private static final String TAG = "Matrix.Application";
    @Override
    public void onCreate() {
        super.onCreate();
        Matrix.Builder builder = new Matrix.Builder(this); // build matrix
        builder.pluginListener(new TestPluginListener(this)); // add general pluginListener
        DynamicConfigImplDemo dynamicConfig = new DynamicConfigImplDemo(); // dynamic config

        // Configure trace canary.
        TracePlugin tracePlugin = configureTracePlugin(dynamicConfig);

        builder.plugin(tracePlugin);

        //init matrix
        Matrix.init(builder.build());
    }


    private TracePlugin configureTracePlugin(DynamicConfigImplDemo dynamicConfig) {

        boolean fpsEnable = dynamicConfig.isFPSEnable();
        boolean traceEnable = dynamicConfig.isTraceEnable();
        boolean signalAnrTraceEnable = dynamicConfig.isSignalAnrTraceEnable();

        File traceFileDir = new File(getApplicationContext().getFilesDir(), "matrix_trace");
        if (!traceFileDir.exists()) {
            if (traceFileDir.mkdirs()) {
                MatrixLog.e(TAG, "failed to create traceFileDir");
            }
        }

        File anrTraceFile = new File(traceFileDir, "anr_trace");    // path : /data/user/0/sample.tencent.matrix/files/matrix_trace/anr_trace
        File printTraceFile = new File(traceFileDir, "print_trace");    // path : /data/user/0/sample.tencent.matrix/files/matrix_trace/print_trace

        TraceConfig traceConfig = new TraceConfig.Builder()
                .dynamicConfig(dynamicConfig)
                .enableFPS(fpsEnable)
                .enableEvilMethodTrace(traceEnable)
                .enableAnrTrace(traceEnable)
                .enableStartup(traceEnable)
                .enableIdleHandlerTrace(traceEnable)                    // Introduced in Matrix 2.0
                .enableMainThreadPriorityTrace(true)                    // Introduced in Matrix 2.0
                .enableSignalAnrTrace(signalAnrTraceEnable)             // Introduced in Matrix 2.0
                .anrTracePath(anrTraceFile.getAbsolutePath())
                .printTracePath(printTraceFile.getAbsolutePath())
                .splashActivities("com.rzm.matrix_testing.SplashActivity;")
                .enableHistoryMsgRecorder(true)
                .enableDenseMsgTracer(true)
                .isDebug(true)
                .isDevEnv(true)
                .build();

        //Another way to use SignalAnrTracer separately
        //useSignalAnrTraceAlone(anrTraceFile.getAbsolutePath(), printTraceFile.getAbsolutePath());

        return new TracePlugin(traceConfig);
    }
}
