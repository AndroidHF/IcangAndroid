/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.buycolle.aicang.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;

import com.buycolle.aicang.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SmileUtils {
    public static final String ee_1 = "^高兴^";
    public static final String ee_2 = "^心碎^";
    public static final String ee_3 = "^喷血^";
    public static final String ee_4 = "^掀桌^";
    public static final String ee_5 = "^害羞^";
    public static final String ee_6 = "^点蜡烛^";
    public static final String ee_7 = "^加一^";
    public static final String ee_8 = "^坏笑^";
    public static final String ee_9 = "^心塞^";
    public static final String ee_10 = "^眼馋^";
    public static final String ee_11 = "^给跪^";
    public static final String ee_12 = "^可爱^";
    public static final String ee_13 = "^战个痛^";
    public static final String ee_14 = "^科科^";
    public static final String ee_15 = "^红包^";
    public static final String ee_16 = "^吃药^";
    //添加
    public static final String ee_17 = "^戳瞎^";
    public static final String ee_18 = "^小船^";




    public static ArrayList<String> getSmailStrs() {
        ArrayList<String> smailStr = new ArrayList<>();
        smailStr.add(ee_1);
        smailStr.add(ee_2);
        smailStr.add(ee_3);
        smailStr.add(ee_4);
        smailStr.add(ee_5);
        smailStr.add(ee_6);
        smailStr.add(ee_7);
        smailStr.add(ee_8);
        smailStr.add(ee_9);
        smailStr.add(ee_10);
        smailStr.add(ee_11);
        smailStr.add(ee_12);
        smailStr.add(ee_13);
        smailStr.add(ee_14);
        smailStr.add(ee_15);
        smailStr.add(ee_16);
        //添加
        smailStr.add(ee_17);
        smailStr.add(ee_18);
        return smailStr;
    }

    private static final Factory spannableFactory = Factory
            .getInstance();

    private static final Map<Pattern, Integer> emoticons = new HashMap<Pattern, Integer>();

    static {

        addPattern(emoticons, ee_1, R.drawable.ee_1);
        addPattern(emoticons, ee_2, R.drawable.ee_2);
        addPattern(emoticons, ee_3, R.drawable.ee_3);
        addPattern(emoticons, ee_4, R.drawable.ee_4);
        addPattern(emoticons, ee_5, R.drawable.ee_5);
        addPattern(emoticons, ee_6, R.drawable.ee_6);
        addPattern(emoticons, ee_7, R.drawable.ee_7);
        addPattern(emoticons, ee_8, R.drawable.ee_8);
        addPattern(emoticons, ee_9, R.drawable.ee_9);
        addPattern(emoticons, ee_10, R.drawable.ee_10);
        addPattern(emoticons, ee_11, R.drawable.ee_11);
        addPattern(emoticons, ee_12, R.drawable.ee_12);
        addPattern(emoticons, ee_13, R.drawable.ee_13);
        addPattern(emoticons, ee_14, R.drawable.ee_14);
        addPattern(emoticons, ee_15, R.drawable.ee_15);
        addPattern(emoticons, ee_16, R.drawable.ee_16);
        //添加
        addPattern(emoticons, ee_17, R.drawable.ee_17);
        addPattern(emoticons, ee_18, R.drawable.ee_18);

    }

    private static void addPattern(Map<Pattern, Integer> map, String smile,
                                   int resource) {
        map.put(Pattern.compile(Pattern.quote(smile)), resource);
    }

    /**
     * @param context
     * @param spannable
     * @return
     */
    public static boolean addSmiles(Context context, Spannable spannable) {
        boolean hasChanges = false;
        for (Entry<Pattern, Integer> entry : emoticons.entrySet()) {
            Matcher matcher = entry.getKey().matcher(spannable);
            while (matcher.find()) {
                boolean set = true;
                for (ImageSpan span : spannable.getSpans(matcher.start(),
                        matcher.end(), ImageSpan.class))
                    if (spannable.getSpanStart(span) >= matcher.start()
                            && spannable.getSpanEnd(span) <= matcher.end())
                        spannable.removeSpan(span);
                    else {
                        set = false;
                        break;
                    }
                if (set) {
                    hasChanges = true;
                    Resources res = context.getResources();
                    Bitmap bmp = BitmapFactory.decodeResource(res, entry.getValue());
                    //这里是修改表情在输入框中的大小
                    int imgSize = UIUtil.dip2px(context, 25);
                    int imgSize11 = UIUtil.dip2px(context, 23);
                    spannable.setSpan(new ImageSpan(context, zoomImage(bmp, imgSize, imgSize11), ImageSpan.ALIGN_BOTTOM),
                            matcher.start(), matcher.end(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }

        }
        return hasChanges;
    }

    /**
     * @param context
     * @param spannable
     * @return
     */
    public static boolean addSmilesBig(Context context, Spannable spannable) {
        boolean hasChanges = false;
        for (Entry<Pattern, Integer> entry : emoticons.entrySet()) {
            Matcher matcher = entry.getKey().matcher(spannable);
            while (matcher.find()) {
                boolean set = true;
                for (ImageSpan span : spannable.getSpans(matcher.start(),
                        matcher.end(), ImageSpan.class))
                    if (spannable.getSpanStart(span) >= matcher.start()
                            && spannable.getSpanEnd(span) <= matcher.end())
                        spannable.removeSpan(span);
                    else {
                        set = false;
                        break;
                    }
                if (set) {
                    hasChanges = true;
                    Resources res = context.getResources();
                    Bitmap bmp = BitmapFactory.decodeResource(res, entry.getValue());
                    //change by :胡峰，评论表情的修改，这个是设置商品问与答界面的显示图标
                    int imgSize = UIUtil.dip2px(context,55);
                    int imgSizess = UIUtil.dip2px(context,48);

                    spannable.setSpan(new ImageSpan(context, zoomImage(bmp, imgSize, imgSizess), ImageSpan.ALIGN_BOTTOM),
                            matcher.start(), matcher.end(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        return hasChanges;
    }

    /**
     * @param context
     * @param spannable
     * @return
     */
    public static boolean addSmilesSubBig(Context context, Spannable spannable) {
        boolean hasChanges = false;
        for (Entry<Pattern, Integer> entry : emoticons.entrySet()) {
            Matcher matcher = entry.getKey().matcher(spannable);
            while (matcher.find()) {
                boolean set = true;
                for (ImageSpan span : spannable.getSpans(matcher.start(),
                        matcher.end(), ImageSpan.class))
                    if (spannable.getSpanStart(span) >= matcher.start()
                            && spannable.getSpanEnd(span) <= matcher.end())
                        spannable.removeSpan(span);
                    else {
                        set = false;
                        break;
                    }
                if (set) {
                    hasChanges = true;
                    Resources res = context.getResources();
                    Bitmap bmp = BitmapFactory.decodeResource(res, entry.getValue());
                    //change by :胡峰，评论表情的修改，这个是修改拍品详情界面的表情的大小
                    int imgSize = UIUtil.dip2px(context,55);
                    int imgSize11 = UIUtil.dip2px(context,48);

                    spannable.setSpan(new ImageSpan(context, zoomImage(bmp, imgSize, imgSize11), ImageSpan.ALIGN_BOTTOM),
                            matcher.start(), matcher.end(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        return hasChanges;
    }


    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }


    public static boolean addSmiles(Context context, SpannableStringBuilder spannable) {
        boolean hasChanges = false;
        for (Entry<Pattern, Integer> entry : emoticons.entrySet()) {
            Matcher matcher = entry.getKey().matcher(spannable);
            while (matcher.find()) {
                boolean set = true;
                for (ImageSpan span : spannable.getSpans(matcher.start(),
                        matcher.end(), ImageSpan.class))
                    if (spannable.getSpanStart(span) >= matcher.start()
                            && spannable.getSpanEnd(span) <= matcher.end())
                        spannable.removeSpan(span);
                    else {
                        set = false;
                        break;
                    }
                if (set) {
                    hasChanges = true;
                    spannable.setSpan(new ImageSpan(context, entry.getValue()),
                            matcher.start(), matcher.end(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        return hasChanges;
    }

    public static Spannable getSmiledText(Context context, CharSequence text) {
        Spannable spannable = spannableFactory.newSpannable(text);
        addSmiles(context, spannable);
        return spannable;
    }

    public static Spannable getSmiledCommentText(Context context, CharSequence text) {
        Spannable spannable = spannableFactory.newSpannable(text);
        addSmilesBig(context, spannable);
        return spannable;
    }

    public static Spannable getSmiledSubCommentText(Context context, CharSequence text) {
        Spannable spannable = spannableFactory.newSpannable(text);
        addSmilesSubBig(context, spannable);
        return spannable;
    }

    public static boolean containsKey(String key) {
        boolean b = false;
        for (Entry<Pattern, Integer> entry : emoticons.entrySet()) {
            Matcher matcher = entry.getKey().matcher(key);
            if (matcher.find()) {
                b = true;
                break;
            }
        }

        return b;
    }


}
