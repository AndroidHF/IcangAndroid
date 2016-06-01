package com.buycolle.aicang.bean;

import java.util.ArrayList;

/**
 * Created by joe on 16/3/24.
 */
public class HomeGoodsBean {

    ArrayList<HomeGoodsChildBean> homeGoodsChildBeens;
    private boolean isSingle = false;

    public boolean isSingle() {
        return isSingle;
    }

    public void setSingle(boolean single) {
        isSingle = single;
    }

    public ArrayList<HomeGoodsChildBean> getHomeGoodsChildBeens() {
        return homeGoodsChildBeens;
    }

    public void setHomeGoodsChildBeens(ArrayList<HomeGoodsChildBean> homeGoodsChildBeens) {
        this.homeGoodsChildBeens = homeGoodsChildBeens;
    }
}
