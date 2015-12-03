package com.example.pein.demo.database;

import android.content.Context;

import com.example.pein.demo.dao.DaoMaster;
import com.example.pein.demo.dao.DaoSession;
import com.example.pein.demo.dao.STORYDao;

import de.greenrobot.dao.async.AsyncSession;

/**
 * Created by Pein on 15/12/3.
 */
public class DBHelper {
    private static final String DBNAME = "zhihuDailyDemo.db";
    private DaoSession daoSession;
    private AsyncSession asyncSession;
    private static DBHelper instance = null;

    public static DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context);
        }

        return instance;
    }

    private DBHelper(Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, DBNAME, null);
        DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());
        daoSession = daoMaster.newSession();
        asyncSession = daoSession.startAsyncSession();
    }

    public STORYDao getSTORYDao() {
        return daoSession.getSTORYDao();
    }

    public AsyncSession getAsyncSession() {
        return asyncSession;
    }
}
