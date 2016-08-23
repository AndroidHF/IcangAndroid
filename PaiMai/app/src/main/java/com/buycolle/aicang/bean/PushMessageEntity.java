package com.buycolle.aicang.bean;

/**
 * Created by joe on 16/5/19.
 */
public class PushMessageEntity {

    private int type;
    private int key_id;
    private int id;
    private String title;
    private String content;
//    private int ent_id;

    public int getKey_id() {
        return key_id;
    }

    public void setKey_id(int key_id) {
        this.key_id = key_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
//    public int getEnt_id() {
//        return ent_id;
//    }
//
//    public void setEnt_id(int ent_id) {
//        this.ent_id = ent_id;
//    }

    @Override
    public String toString() {
        return "PushMessageEntity{" +
                "type=" + type +
                ", key_id=" + key_id +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
//                ", ent_id=" + ent_id +
                '}';
    }
}
