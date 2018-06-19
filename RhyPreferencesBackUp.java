package com.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.channels.FileChannel;

/**
 * 作者: rhyme(rhymelph@qq.com).
 * 日期: 2018/6/19.
 * 描述: .
 */
public class RhyPreferencesBackUp {
    private static final String Method_Name = "getSharedPreferencesPath";
    @SuppressLint("StaticFieldLeak")
    private static RhyPreferencesBackUp rhyPreferencesBackUp;
    private Context context;

    public static RhyPreferencesBackUp Instance(Context context) {
        if (rhyPreferencesBackUp == null) {
            synchronized (RhyPreferencesBackUp.class) {
                if (rhyPreferencesBackUp == null) {
                    rhyPreferencesBackUp = new RhyPreferencesBackUp(context);
                }
            }
        }
        return rhyPreferencesBackUp;
    }

    private RhyPreferencesBackUp(Context context) {
        this.context = context;
    }

    public static RhyPreferencesBackUp Instance() {
        if (rhyPreferencesBackUp == null) {
            throw new IllegalArgumentException("RhyPreferencesBackUp not instance");
        }
        return rhyPreferencesBackUp;
    }

    /**
     * 备份SharePerences文件
     *
     * @param sharePreferenceName sharePreferences文件名(share preferences name)
     * @param path                备份目录文件(backup file parent path)
     */
    public void backup(String sharePreferenceName, String path) {
        try {
            Class pclass = context.getClass();
            Method getSharedPreferencesPath = pclass.getMethod(Method_Name, String.class);

            Object object = getSharedPreferencesPath.invoke(context, sharePreferenceName);
            File mFile = new File(object.toString());

            FileUtils.copyFile(mFile, path + "/");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * * @param context             上下文
     *
     * @param sharePreferenceName sharePreferences文件名(share preferences name)
     * @param path                备份目录文件(backup file parent path)
     * @return 返回是否恢复成功(Is restore success ?)
     */
    public  boolean restore( String sharePreferenceName, String path) {
        boolean isSuccess = false;
        try {
            File backUpFile = new File(path + "/" + sharePreferenceName + ".xml");
            if (backUpFile.exists()) {
                context.getSharedPreferences("a", Context.MODE_PRIVATE);
                Class pclass = context.getClass();
                Method getSharedPreferencesPath = pclass.getMethod(Method_Name, String.class);
                Object object = getSharedPreferencesPath.invoke(context, "a");
                File mFile = new File(object.toString());
                isSuccess = copyFile(backUpFile, mFile.getParent() + "/");
                mFile.delete();
            }
        } catch (Exception e) {
            //backupFile no exists
            e.printStackTrace();
        }
        return isSuccess;
    }


    /**
     * 检查Preferences文件是否存在
     *
     * @param context              上下文
     * @param SharePreferencesName Preferences文件名
     * @return
     */
    public static boolean checkPreferenceFile(Context context, String SharePreferencesName) {
        try {
            Class pclass = context.getClass();
            Method getSharedPreferencesPath = pclass.getMethod(Method_Name, String.class);
            Object object = getSharedPreferencesPath.invoke(context, SharePreferencesName);
            File mFile = new File(object.toString());
            return mFile.exists();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    private static boolean copyFile(File copyFile, String destPath) {
        boolean result = false;
        if ((copyFile.getAbsolutePath() == null) || (destPath == null)) {
            return result;
        }
        File dest = new File(destPath + copyFile.getName());
        if (dest != null && dest.exists()) {
            dest.delete(); // delete file
        }
        try {
            dest.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileChannel srcChannel = null;
        FileChannel dstChannel = null;

        try {
            srcChannel = new FileInputStream(copyFile.getAbsolutePath()).getChannel();
            dstChannel = new FileOutputStream(dest).getChannel();
            srcChannel.transferTo(0, srcChannel.size(), dstChannel);
            result = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return result;
        }
        try {
            srcChannel.close();
            dstChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
