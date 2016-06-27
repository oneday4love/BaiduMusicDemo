package com.lanou.baidumusicdemo.musiclibrary.recommend;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dllo on 16/6/23.
 */
public class RecRvBean {

    /**
     * error_code : 22000
     * content : {"title":"热门歌单","list":[{"listid":"6727","pic":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_732a7dc8bfbf4d15fd654378f053fa85.jpg","listenum":"15531","collectnum":"350","title":"耳边歌声，似是故人来","tag":"百度音乐人,原创,古风,温柔,抒情","type":"gedan"},{"listid":"6721","pic":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_8d2ba6ff02d3d985fedb20d0308f0689.jpg","listenum":"10128","collectnum":"461","title":"逗比青年欢乐多","tag":"开心,好听,舒服,搞笑","type":"gedan"},{"listid":"5429","pic":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_c09a28c5dfe4c5312296ada5ed5bd12e.jpg","listenum":"131709","collectnum":"582","title":"坚强的理由","tag":"华语,励志,经典","type":"gedan"},{"listid":"6725","pic":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_4ecc4e7f2e5bafc8c6b894af0cef4868.jpg","listenum":"19941","collectnum":"351","title":"入耳就爱上的华语歌曲","tag":"好听,经典,舒服","type":"gedan"}]}
     */

    private int error_code;
    /**
     * title : 热门歌单
     * list : [{"listid":"6727","pic":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_732a7dc8bfbf4d15fd654378f053fa85.jpg","listenum":"15531","collectnum":"350","title":"耳边歌声，似是故人来","tag":"百度音乐人,原创,古风,温柔,抒情","type":"gedan"},{"listid":"6721","pic":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_8d2ba6ff02d3d985fedb20d0308f0689.jpg","listenum":"10128","collectnum":"461","title":"逗比青年欢乐多","tag":"开心,好听,舒服,搞笑","type":"gedan"},{"listid":"5429","pic":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_c09a28c5dfe4c5312296ada5ed5bd12e.jpg","listenum":"131709","collectnum":"582","title":"坚强的理由","tag":"华语,励志,经典","type":"gedan"},{"listid":"6725","pic":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_4ecc4e7f2e5bafc8c6b894af0cef4868.jpg","listenum":"19941","collectnum":"351","title":"入耳就爱上的华语歌曲","tag":"好听,经典,舒服","type":"gedan"}]
     */

    private ContentBean content;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public static class ContentBean {
        private String title;
        /**
         * listid : 6727
         * pic : http://business.cdn.qianqian.com/qianqian/pic/bos_client_732a7dc8bfbf4d15fd654378f053fa85.jpg
         * listenum : 15531
         * collectnum : 350
         * title : 耳边歌声，似是故人来
         * tag : 百度音乐人,原创,古风,温柔,抒情
         * type : gedan
         */

        private List<ListBean> list;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private String listid;
            private String pic;
            private String listenum;
            private String collectnum;
            @SerializedName("title")
            private String titleSong;
            private String tag;
            private String type;

            public String getListid() {
                return listid;
            }

            public void setListid(String listid) {
                this.listid = listid;
            }

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }

            public String getListenum() {
                return listenum;
            }

            public void setListenum(String listenum) {
                this.listenum = listenum;
            }

            public String getCollectnum() {
                return collectnum;
            }

            public void setCollectnum(String collectnum) {
                this.collectnum = collectnum;
            }

            public String getTitleSong() {
                return titleSong;
            }

            public void setTitleSong(String titleSong) {
                this.titleSong = titleSong;
            }

            public String getTag() {
                return tag;
            }

            public void setTag(String tag) {
                this.tag = tag;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }
}
