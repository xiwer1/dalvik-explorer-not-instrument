package org.jessies.dalvikexplorer.test;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.io.DataInput;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

public class JacocoUtils {
    static String TAG = "JacocoUtils";
    static int i = 0;

    //ec文件的路径
    private static String DEFAULT_COVERAGE_FILE_PATH = "/mnt/sdcard/Pictures/coverage.ec";

    /**
     * 生成ec文件
     *
     * @param isNew 是否重新创建ec文件
     */
    public static void generateEcFile(boolean isNew) {
        //String index = String.valueOf(i++);
        String index = String.valueOf(System.currentTimeMillis());
        DEFAULT_COVERAGE_FILE_PATH = "/mnt/sdcard/Pictures/coverage" + index + ".ec";
        Log.d(TAG, "生成覆盖率文件: " + DEFAULT_COVERAGE_FILE_PATH);
        OutputStream out = null;
        File mCoverageFilePath = new File(DEFAULT_COVERAGE_FILE_PATH);

        //create and delete coverage.ec
        try {
            if (isNew && mCoverageFilePath.exists()) {
                Log.d(TAG, "JacocoUtils_generateEcFile: 清除旧的ec文件");
                // mCoverageFilePath.delete();
            }
            if (!mCoverageFilePath.exists()) {
                mCoverageFilePath.createNewFile();
            }
            out = new FileOutputStream(mCoverageFilePath.getPath(), true);

            //反射：获取org.jacoco.agent.rt.IAgent
            Object agent = Class.forName("org.jacoco.agent.rt.RT"   )
                    .getMethod("getAgent")
                    .invoke(null);

            //反射：getExecutionData(boolean reset)，获取当前执行数据，以jacoco二进制格式转储当前执行数据
            // getExecutionData(boolean reset)，reset如果为true，则之后清除当前执行数据
            out.write((byte[]) agent.getClass().getMethod("getExecutionData", boolean.class)
                    .invoke(agent, false))  ;

        } catch (Exception e) {
            Log.e(TAG, "generateEcFile: " + e.getMessage());
        } finally {
            if (out == null)
                return;
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

