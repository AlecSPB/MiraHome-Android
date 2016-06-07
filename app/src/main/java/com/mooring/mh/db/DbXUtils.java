package com.mooring.mh.db;

import android.content.Context;
import android.os.Environment;

import org.xutils.DbManager;

import java.io.File;

/**
 * xutils Db 工具类
 * <p/>
 * Created by Will on 16/3/31.
 */
public class DbXUtils {

    static DbManager.DaoConfig daoConfig;

    public static DbManager.DaoConfig getDaoConfig(Context mContext) {
        String path = mContext.getFilesDir().getPath();
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            path = mContext.getExternalFilesDir(null).getPath();
        }
        File f = new File(path + "/dbFile");
        if (!f.exists()) {
            f.mkdirs();
        }
        if (daoConfig == null) {
            daoConfig = new DbManager.DaoConfig().setDbName("mooring_common.db").setDbDir(f)
                    .setDbVersion(1).setAllowTransaction(true)
                    .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                        @Override
                        public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                        }
                    });
        }
        return daoConfig;
    }
}
