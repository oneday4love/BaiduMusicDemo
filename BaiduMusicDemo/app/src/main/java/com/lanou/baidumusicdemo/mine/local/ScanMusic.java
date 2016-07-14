package com.lanou.baidumusicdemo.mine.local;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.lanou.baidumusicdemo.MyApp;
import com.lanou.baidumusicdemo.db.LiteOrmSingle;
import com.lanou.baidumusicdemo.db.MyFavoriteSong;
import com.litesuits.orm.db.assit.QueryBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by dllo on 16/7/7.
 */
public class ScanMusic {

    public static void query(Context mContext) {
//        //创建ArryList
//        ArrayList<LocalSongBean> arrayList;
//        //实例化ArryList对象
//        arrayList = new ArrayList<LocalSongBean>();
        //创建一个扫描游标
        Cursor c=mContext.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,null);
        if(c!=null) {
            //创建Model对象
            LocalSongBean localSongBean;
            //循环读取
            //实例化Model对象
            while(c.moveToNext()){

                localSongBean = new LocalSongBean();
                //扫描本地文件，得到歌曲的相关信息
                String music_name=c.getString(c.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String music_singer=c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String path = c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA));

                //设置值到Model的封装类中
                localSongBean.setTitle(music_name);
                localSongBean.setAuthor(music_singer);
                localSongBean.setPath(path);
                //将model值加入到数组中
//                arrayList.add(localSongBean);

                QueryBuilder<LocalSongBean> queryBuilder = new QueryBuilder<>(LocalSongBean.class);
                queryBuilder.whereEquals("title", music_name);
                if (LiteOrmSingle.getInstance().getLiteOrm().query(queryBuilder).size() == 0){
                    LiteOrmSingle.getInstance().getLiteOrm().insert(new LocalSongBean(music_name,music_singer,path));
                }

            }

        }
    }
//    public static ArrayList<LrcModel> redLrc(String path) {
//        ArrayList<LrcModel> alist = new ArrayList<LrcModel>();
//
//        File f = new File(path.replace(".mp3", ".lrc"));
//
//        try {
//            FileInputStream fs = new FileInputStream(f);
//            InputStreamReader inputStreamReader = new InputStreamReader(fs,
//                    "utf-8");
//            BufferedReader br = new BufferedReader(inputStreamReader);
//            String s = "";
//            while (null != (s = br.readLine())) {
//                if (!TextUtils.isEmpty(s)) {
//                    LrcModel lrcModle = new LrcModel();
//                    String lylrc = s.replace("[", "");
//                    String data_ly[] = lylrc.split("]");
//                    if (data_ly.length > 1) {
//                        String time = data_ly[0];
//                        lrcModle.setTime(LrcData(time));
//                        String lrc = data_ly[1];
//                        lrcModle.setLrc(lrc);
//                        alist.add(lrcModle);
//                    }
//
//                }
//
//            }
//
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return alist;
//
//    }

//    public static int LrcData(String time) {
//        time = time.replace(":", "#");
//        time = time.replace(".", "#");
//        String mTime[] = time.split("#");
//        int mtime = Integer.parseInt(mTime[0]);
//        int stime = Integer.parseInt(mTime[1]);
//        int mitime = Integer.parseInt(mTime[2]);
//        int ctime = (mtime * 60 + stime) * 1000 + mitime * 10;
//        return ctime;
//    }
}
