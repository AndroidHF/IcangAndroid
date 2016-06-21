package com.buycolle.aicang.bean;

import java.util.ArrayList;

/**
 * Created by joe on 16/4/1.
 */
public class ShowDetailBean {


    /**
     * cate_icon : 测试内容14a2
     * cate_id : 70374
     * cate_name : 测试内容o2j7
     * col_count : 57525
     * col_id : 17842
     * comment_count : 12714
     * context : 测试内容qs9h
     * cover_pic : 测试内容dlr7
     * create_date : 测试内容pp45
     * intro : 测试内容d5om
     * recomList : [{"a_user_avatar":1,"a_user_nick":1,"aim_id":1,"c_user_avatar":1,"c_user_nick":1,"context":1,"create_date":1,"create_user_id":1,"id":1,"pid":1,"root_id":1,"subVec":[],"z_count":1}]
     * show_id : 66871
     * title : 测试内容28x5
     * user_avatar : 测试内容1u72
     * user_nick : 测试内容x7nt
     * z_count : 38736
     * z_id : 52300
     */

    private String cate_icon;
    private int cate_id;
    private String cate_name;
    private int col_count;
    private int col_id;
    private int comment_count;
    private String context;
    private String cover_pic;
    private String create_date;
    private String last_update_date;
    private String intro;
    private int show_id;
    private String title;
    private String user_avatar;
    private String user_nick;
    private int z_count;
    private int z_id;

    public String getLast_update_date() {
        return last_update_date;
    }

    public void setLast_update_date(String last_update_date) {
        this.last_update_date = last_update_date;
    }

    /**
     * a_user_avatar : 1
     * a_user_nick : 1
     * aim_id : 1
     * c_user_avatar : 1
     * c_user_nick : 1
     * context : 1
     * create_date : 1
     * create_user_id : 1
     * id : 1
     * pid : 1
     * root_id : 1
     * subVec : []
     * z_count : 1
     */



    private ArrayList<RecomListEntity> recomList;

    public String getCate_icon() {
        return cate_icon;
    }

    public void setCate_icon(String cate_icon) {
        this.cate_icon = cate_icon;
    }

    public int getCate_id() {
        return cate_id;
    }

    public void setCate_id(int cate_id) {
        this.cate_id = cate_id;
    }

    public String getCate_name() {
        return cate_name;
    }

    public void setCate_name(String cate_name) {
        this.cate_name = cate_name;
    }

    public int getCol_count() {
        return col_count;
    }

    public void setCol_count(int col_count) {
        this.col_count = col_count;
    }

    public int getCol_id() {
        return col_id;
    }

    public void setCol_id(int col_id) {
        this.col_id = col_id;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getCover_pic() {
        return cover_pic;
    }

    public void setCover_pic(String cover_pic) {
        this.cover_pic = cover_pic;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getShow_id() {
        return show_id;
    }

    public void setShow_id(int show_id) {
        this.show_id = show_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public String getUser_nick() {
        return user_nick;
    }

    public void setUser_nick(String user_nick) {
        this.user_nick = user_nick;
    }

    public int getZ_count() {
        return z_count;
    }

    public void setZ_count(int z_count) {
        this.z_count = z_count;
    }

    public int getZ_id() {
        return z_id;
    }

    public void setZ_id(int z_id) {
        this.z_id = z_id;
    }

    public ArrayList<RecomListEntity> getRecomList() {
        return recomList;
    }

    public void setRecomList(ArrayList<RecomListEntity> recomList) {
        this.recomList = recomList;
    }


    public static class RecomListEntity {
        private String a_user_avatar;
        private String a_user_nick;
        private int aim_id;
        private String c_user_avatar;
        private String c_user_nick;
        private String context;
        private String create_date;
        private int create_user_id;
        private int id;
        private int pid;
        private int root_id;
        private int z_count;
        private ArrayList<SubRecomListEntity> subVec;


        public String getA_user_avatar() {
            return a_user_avatar;
        }

        public void setA_user_avatar(String a_user_avatar) {
            this.a_user_avatar = a_user_avatar;
        }

        public String getA_user_nick() {
            return a_user_nick;
        }

        public void setA_user_nick(String a_user_nick) {
            this.a_user_nick = a_user_nick;
        }

        public int getAim_id() {
            return aim_id;
        }

        public void setAim_id(int aim_id) {
            this.aim_id = aim_id;
        }

        public String getC_user_avatar() {
            return c_user_avatar;
        }

        public void setC_user_avatar(String c_user_avatar) {
            this.c_user_avatar = c_user_avatar;
        }

        public String getC_user_nick() {
            return c_user_nick;
        }

        public void setC_user_nick(String c_user_nick) {
            this.c_user_nick = c_user_nick;
        }

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }

        public String getCreate_date() {
            return create_date;
        }

        public void setCreate_date(String create_date) {
            this.create_date = create_date;
        }

        public int getCreate_user_id() {
            return create_user_id;
        }

        public void setCreate_user_id(int create_user_id) {
            this.create_user_id = create_user_id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public int getRoot_id() {
            return root_id;
        }

        public void setRoot_id(int root_id) {
            this.root_id = root_id;
        }

        public int getZ_count() {
            return z_count;
        }

        public void setZ_count(int z_count) {
            this.z_count = z_count;
        }

        public ArrayList<SubRecomListEntity> getSubVec() {
            return subVec;
        }

        public void setSubVec(ArrayList<SubRecomListEntity> subVec) {
            this.subVec = subVec;
        }

        public static class SubRecomListEntity {
            private String a_user_avatar;
            private String a_user_nick;
            private int aim_id;
            private String c_user_avatar;
            private String c_user_nick;
            private String context;
            private String create_date;
            private int create_user_id;
            private int id;
            private int pid;
            private int root_id;
            private int z_count;

            public String getA_user_avatar() {
                return a_user_avatar;
            }

            public void setA_user_avatar(String a_user_avatar) {
                this.a_user_avatar = a_user_avatar;
            }

            public String getA_user_nick() {
                return a_user_nick;
            }

            public void setA_user_nick(String a_user_nick) {
                this.a_user_nick = a_user_nick;
            }

            public int getAim_id() {
                return aim_id;
            }

            public void setAim_id(int aim_id) {
                this.aim_id = aim_id;
            }

            public String getC_user_avatar() {
                return c_user_avatar;
            }

            public void setC_user_avatar(String c_user_avatar) {
                this.c_user_avatar = c_user_avatar;
            }

            public String getC_user_nick() {
                return c_user_nick;
            }

            public void setC_user_nick(String c_user_nick) {
                this.c_user_nick = c_user_nick;
            }

            public String getContext() {
                return context;
            }

            public void setContext(String context) {
                this.context = context;
            }

            public String getCreate_date() {
                return create_date;
            }

            public void setCreate_date(String create_date) {
                this.create_date = create_date;
            }

            public int getCreate_user_id() {
                return create_user_id;
            }

            public void setCreate_user_id(int create_user_id) {
                this.create_user_id = create_user_id;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getPid() {
                return pid;
            }

            public void setPid(int pid) {
                this.pid = pid;
            }

            public int getRoot_id() {
                return root_id;
            }

            public void setRoot_id(int root_id) {
                this.root_id = root_id;
            }

            public int getZ_count() {
                return z_count;
            }

            public void setZ_count(int z_count) {
                this.z_count = z_count;
            }
        }
    }
}
