/*
 * Tencent is pleased to support the open source community by making wechat-matrix available.
 * Copyright (C) 2018 THL A29 Limited, a Tencent company. All rights reserved.
 * Licensed under the BSD 3-Clause License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tencent.matrix.iocanary;

import android.app.Application;

import com.tencent.matrix.iocanary.config.IOConfig;
import com.tencent.matrix.iocanary.config.SharePluginInfo;
import com.tencent.matrix.iocanary.core.IOCanaryCore;
import com.tencent.matrix.iocanary.util.IOCanaryUtil;
import com.tencent.matrix.plugin.Plugin;
import com.tencent.matrix.plugin.PluginListener;

/**
 * Core logic for hookers, detectors and reporter
 * <p>
 * Logic stream like:
 * hooker -> detector -> reporter
 * <p>
 * @author liyongjie
 *         Created by liyongjie on 2017/6/29.
 * tag[io]type[1];key[null];content[{"path":"\/sdcard\/a.txt","size":40960,"op":102505,"buffer":400,"cost":709,"opType":1,"opSize":41002000,"thread":"main","stack":"android.app.ActivityThread$AndroidOs.open(ActivityThread.java:7542)\ncom.rzm.matrix_testing.io.TestIOActivity.leakSth(TestIOActivity.java:204)\ncom.rzm.matrix_testing.io.TestIOActivity.onClick(TestIOActivity.java:117)\njava.lang.reflect.Method.invoke(Native Method)\nandroid.view.View$DeclaredOnClickListener.onClick(View.java:6263)\nandroid.view.View.performClick(View.java:7448)\nandroid.view.View.performClickInternal(View.java:7425)\nandroid.view.View.access$3600(View.java:810)\nandroid.view.View$PerformClick.run(View.java:28305)\nandroid.app.ActivityThread.main(ActivityThread.java:7656)\n","repeat":3,"tag":"io","type":1,"process":"com.rzm.matrix_testing","time":1683420595133}]
 * tag[io]type[2];key[null];content[{"path":"\/sdcard\/a.txt","size":40960,"op":102505,"buffer":400,"cost":709,"opType":1,"opSize":41002000,"thread":"main","stack":"android.app.ActivityThread$AndroidOs.open(ActivityThread.java:7542)\ncom.rzm.matrix_testing.io.TestIOActivity.leakSth(TestIOActivity.java:204)\ncom.rzm.matrix_testing.io.TestIOActivity.onClick(TestIOActivity.java:117)\njava.lang.reflect.Method.invoke(Native Method)\nandroid.view.View$DeclaredOnClickListener.onClick(View.java:6263)\nandroid.view.View.performClick(View.java:7448)\nandroid.view.View.performClickInternal(View.java:7425)\nandroid.view.View.access$3600(View.java:810)\nandroid.view.View$PerformClick.run(View.java:28305)\nandroid.app.ActivityThread.main(ActivityThread.java:7656)\n","repeat":0,"tag":"io","type":2,"process":"com.rzm.matrix_testing","time":1683420595147}]
 * tag[io]type[2];key[null];content[{"path":"\/sdcard\/a_long.txt","size":40960000,"op":80000,"buffer":512,"cost":395,"opType":2,"opSize":40960000,"thread":"main","stack":"android.app.ActivityThread$AndroidOs.open(ActivityThread.java:7542)\ncom.rzm.matrix_testing.io.TestIOActivity.writeLongSth(TestIOActivity.java:143)\ncom.rzm.matrix_testing.io.TestIOActivity.onClick(TestIOActivity.java:113)\njava.lang.reflect.Method.invoke(Native Method)\nandroid.view.View$DeclaredOnClickListener.onClick(View.java:6263)\nandroid.view.View.performClick(View.java:7448)\nandroid.view.View.performClickInternal(View.java:7425)\nandroid.view.View.access$3600(View.java:810)\nandroid.view.View$PerformClick.run(View.java:28305)\nandroid.app.ActivityThread.main(ActivityThread.java:7656)\n","repeat":0,"tag":"io","type":2,"process":"com.rzm.matrix_testing","time":1683420395881}]
 */

public class IOCanaryPlugin extends Plugin {
    private static final String TAG = "Matrix.IOCanaryPlugin";

    private final IOConfig     mIOConfig;
    private IOCanaryCore mCore;

//    public IOCanaryPlugin() {
//        mIOConfig = IOConfig.DEFAULT;
//    }

    public IOCanaryPlugin(IOConfig ioConfig) {
        mIOConfig = ioConfig;
    }

    @Override
    public void init(Application app, PluginListener listener) {
        super.init(app, listener);
        IOCanaryUtil.setPackageName(app);
        mCore = new IOCanaryCore(this);
    }

    @Override
    public void start() {
        super.start();
        mCore.start();
    }

    @Override
    public void stop() {
        super.stop();
        mCore.stop();
    }

    public IOConfig getConfig() {
        return mIOConfig;
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public String getTag() {
        return SharePluginInfo.TAG_PLUGIN;
    }
}
