package com.lanou.baidumusicdemo.db;

import com.lanou.baidumusicdemo.MyApp;
import com.litesuits.orm.LiteOrm;

/**
 * Created by dllo on 16/6/29.
 */
public class LiteOrmSingle {

    private static LiteOrmSingle liteOrmSingle;
    private LiteOrm liteOrm;

    public LiteOrm getLiteOrm() {
        return liteOrm;
    }

    private LiteOrmSingle() {
        liteOrm = LiteOrm.newCascadeInstance(MyApp.context,"BaiMusic.db");
    }

    public static LiteOrmSingle getInstance(){
        if (liteOrmSingle == null){
            synchronized (LiteOrmSingle.class){
                if (liteOrmSingle == null){
                    liteOrmSingle = new LiteOrmSingle();
                }
            }
        }
        return liteOrmSingle;
    }

    public void add(PlayList playList){
        LiteOrmSingle.getInstance().getLiteOrm().delete(PlayList.class);
        LiteOrmSingle.getInstance().getLiteOrm().insert(playList);
    }


}
