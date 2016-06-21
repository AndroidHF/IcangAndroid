package com.buycolle.aicang.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.buycolle.aicang.MainApplication;
import com.buycolle.aicang.R;
import com.buycolle.aicang.commen.PhotoViewMultPicActivity;
import com.buycolle.aicang.util.UIHelper;

import java.util.ArrayList;

/**
 * Created by joe on 16/3/24.
 */
public class ImageViewerAdapter extends PagerAdapter {

    private Activity context;
    private ArrayList<String> imageBeans;

    private boolean isEvent;

    public ImageViewerAdapter(Activity context, ArrayList<String> imageBeans) {
        this.imageBeans = imageBeans;
        this.context = context;
    }
    public ImageViewerAdapter(Activity context, ArrayList<String> imageBeans,boolean isEvent) {
        this.imageBeans = imageBeans;
        this.context = context;
        this.isEvent = isEvent;
    }

    @Override
    public int getCount() {
        return imageBeans.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View images = inflater.inflate(R.layout.view_goods_detail_image, null);
        ImageView image = (ImageView) images.findViewById(R.id.iv_goods_desc_image);
        if(isEvent){
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }else{
            if(position==0){
                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }else{
                image.setScaleType(ImageView.ScaleType.FIT_CENTER);
            }
        }
        //change by :胡峰，拍品详情界面对于一般拍品和竞拍会拍品的默认加载图片的修改
        if (isEvent){
            MainApplication.getInstance().setShowImages(imageBeans.get(position), image);
        }else {
            MainApplication.getInstance().setImages(imageBeans.get(position), image);
        }
        //MainApplication.getInstance().setImages(imageBeans.get(position), image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("position",position);
                bundle.putStringArrayList("path",imageBeans);
                UIHelper.jump(context, PhotoViewMultPicActivity.class,bundle);
            }
        });
        container.addView(images);
        return images;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}