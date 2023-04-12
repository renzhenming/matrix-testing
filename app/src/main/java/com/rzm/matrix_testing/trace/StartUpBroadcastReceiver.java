package com.rzm.matrix_testing.trace;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tencent.matrix.util.MatrixLog;

public class StartUpBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "Matrix.StartUpBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        MatrixLog.i(TAG, "[onReceive]");
        context.startActivity(new Intent(context, TestOtherProcessActivity.class));
    }
}
