package com.rzm.matrix_testing;

import android.app.Application;
import android.content.Intent;
import android.os.Build;

import com.rzm.matrix_testing.resources.ManualDumpActivity;
import com.tencent.matrix.Matrix;
import com.tencent.matrix.iocanary.IOCanaryPlugin;
import com.tencent.matrix.iocanary.config.IOConfig;
import com.tencent.matrix.resource.ResourcePlugin;
import com.tencent.matrix.resource.config.ResourceConfig;
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

        // Configure io canary.
        IOCanaryPlugin ioCanaryPlugin = configureIOCanaryPlugin(dynamicConfig);
        builder.plugin(ioCanaryPlugin);

        // Configure resource canary.
        ResourcePlugin resourcePlugin = configureResourcePlugin(dynamicConfig);
        builder.plugin(resourcePlugin);

        //init matrix
        Matrix.init(builder.build());
        tracePlugin.start();
        try {
            Thread.sleep(11000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private IOCanaryPlugin configureIOCanaryPlugin(DynamicConfigImplDemo dynamicConfig) {
        return new IOCanaryPlugin(new IOConfig.Builder()
                .dynamicConfig(dynamicConfig)
                .build());
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

    private ResourcePlugin configureResourcePlugin(DynamicConfigImplDemo dynamicConfig) {
        Intent intent = new Intent();
        ResourceConfig.DumpMode mode = ResourceConfig.DumpMode.MANUAL_DUMP;
        MatrixLog.i(TAG, "Dump Activity Leak Mode=%s", mode);
        intent.setClassName(this.getPackageName(), "com.rzm.matrix_testing.resources.ManualDumpActivity");
        ResourceConfig resourceConfig = new ResourceConfig.Builder()
                .dynamicConfig(dynamicConfig)
                .setAutoDumpHprofMode(mode)
                .setManualDumpTargetActivity(ManualDumpActivity.class.getName())
                .setManufacture(Build.MANUFACTURER)
                .setDetectDebuger(true) //调试时也检测
                .build();
        ResourcePlugin.activityLeakFixer(this);
        return new ResourcePlugin(resourceConfig);
    }
}
