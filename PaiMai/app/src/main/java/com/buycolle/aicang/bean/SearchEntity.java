package com.buycolle.aicang.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by joe on 16/3/26.
 */
@DatabaseTable(tableName = "tb_search")
public class SearchEntity {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "keyword")
    private String keyword;
    @DatabaseField(columnName = "type")//"good" even initDialog
    private String type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "SearchEntity{" +
                "id=" + id +
                ", keyword='" + keyword + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
