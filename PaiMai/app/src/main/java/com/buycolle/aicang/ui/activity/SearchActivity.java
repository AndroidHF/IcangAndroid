package com.buycolle.aicang.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buycolle.aicang.Constans;
import com.buycolle.aicang.R;
import com.buycolle.aicang.bean.HotTagBean;
import com.buycolle.aicang.bean.SearchEntity;
import com.buycolle.aicang.dao.SearchKeyDao;
import com.buycolle.aicang.ui.view.MeasuredListView;
import com.buycolle.aicang.util.ACache;
import com.buycolle.aicang.util.UIHelper;
import com.buycolle.aicang.util.superlog.KLog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.next.tagview.TagCloudView;

/**
 * Created by joe on 16/3/11.
 */
public class SearchActivity extends BaseActivity implements TagCloudView.OnTagClickListener {

    @Bind(R.id.iv_search_icon)
    ImageView ivSearchIcon;
    @Bind(R.id.et_input)
    EditText etInput;
    @Bind(R.id.tv_cancle)
    TextView tvCancle;
    @Bind(R.id.tag_cloud_view)
    TagCloudView tagCloudView8;
    @Bind(R.id.lv_search_history)
    MeasuredListView lvSearchHistory;
    @Bind(R.id.ll_delete_history)
    LinearLayout llDeleteHistory;

    List<String> history;
    private MyAdapter historyAdapter;
    private ACache aCache;

    private int index = 0;
    private int event_type = 0;

    SearchKeyDao searchKeyDao;
    ArrayList<SearchEntity> searchEntities;
    List<String> tags = new ArrayList<>();

    ArrayList<HotTagBean> hotTagBeens = new ArrayList<>();

    private  boolean isDetail = false;
    private int cate_id;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        KLog.d("SearchActivity.class----", "onNewIntent");
        hotTagBeens = new ArrayList<>();
        aCache = ACache.get(this);
        searchKeyDao = new SearchKeyDao(this);
        index = _Bundle.getInt("index");
        event_type = _Bundle.getInt("event_type");
        isDetail = _Bundle.getBoolean("isDetail",false);
        if(isDetail){
            cate_id =_Bundle.getInt("cate_id");
        }
        etInput.setText("");

        if (index == 1) {//普通拍品
            searchEntities = searchKeyDao.getAllByType("good");
        }
        if (index == 2) {//拍卖会的
            searchEntities = searchKeyDao.getAllByType("event");
        }
        if (index == 3) {
            searchEntities = searchKeyDao.getAllByType("initDialog");
        }

        getHotTag(index);
        //历史搜索
        history = new ArrayList<>();
        for (SearchEntity searchEntity : searchEntities) {
            KLog.d("是什么----", searchEntity.getKeyword());
            history.add(searchEntity.getKeyword());
        }
        historyAdapter = new MyAdapter(mContext, history);
        lvSearchHistory.setAdapter(historyAdapter);
        lvSearchHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                gotoReault(index, false, true, history.get(i));
            }
        });
        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    ((InputMethodManager) etInput.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(
                                    mActivity
                                            .getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);

                    if (!TextUtils.isEmpty(etInput.getText().toString().trim())) {
                        gotoReault(index, false, false, etInput.getText().toString().trim());
                    } else {
                        UIHelper.t(mContext, "请输入您需要搜索的内容");
                    }
                    return true;
                }
                return false;
            }
        });

        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        llDeleteHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index == 1) {
                    searchKeyDao.deleteAllByType("good");
                }
                if (index == 2) {
                    searchKeyDao.deleteAllByType("event");

                }
                if (index == 3) {
                    searchKeyDao.deleteAllByType("initDialog");

                }
                history.clear();
                historyAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        KLog.d("SearchActivity.class----", "onCreate");

        ButterKnife.bind(this);
        aCache = ACache.get(this);
        hotTagBeens = new ArrayList<>();
        searchKeyDao = new SearchKeyDao(this);
        index = _Bundle.getInt("index");
        event_type = _Bundle.getInt("event_type");
        isDetail = _Bundle.getBoolean("isDetail",false);
        if(isDetail){
            cate_id =_Bundle.getInt("cate_id");
        }

        if (index == 1) {
            searchEntities = searchKeyDao.getAllByType("good");
        }
        if (index == 2) {
            searchEntities = searchKeyDao.getAllByType("event");
        }
        if (index == 3) {
            searchEntities = searchKeyDao.getAllByType("initDialog");
        }

        getHotTag(index);
        //历史搜索
        history = new ArrayList<>();
        for (SearchEntity searchEntity : searchEntities) {
            KLog.d("是什么----", searchEntity.getKeyword());
            history.add(searchEntity.getKeyword());
        }
        historyAdapter = new MyAdapter(mContext, history);
        lvSearchHistory.setAdapter(historyAdapter);
        lvSearchHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                gotoReault(index, false, true, history.get(i));
            }
        });
        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    ((InputMethodManager) etInput.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(
                                    mActivity
                                            .getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);

                    if (!TextUtils.isEmpty(etInput.getText().toString().trim())) {
                        gotoReault(index, false, false, etInput.getText().toString().trim());
                    } else {
                        UIHelper.t(mContext, "请输入您需要搜索的内容");
                    }
                    return true;
                }
                return false;
            }
        });

        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        llDeleteHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index == 1) {
                    searchKeyDao.deleteAllByType("good");
                }
                if (index == 2) {
                    searchKeyDao.deleteAllByType("event");

                }
                if (index == 3) {
                    searchKeyDao.deleteAllByType("initDialog");

                }
                history.clear();
                historyAdapter.notifyDataSetChanged();
            }
        });
    }

    private void getHotTag(int index) {
        KLog.d("----",index+"");
        hotTagBeens.clear();
        tags.clear();
        if (index == 1) {
            JSONObject goodObj = aCache.getAsJSONObject(Constans.TAG_HOT_SEARCH_GOOD);
            if (goodObj != null){
                try {
                    JSONArray jsonArray = goodObj.getJSONArray("rows");

                    hotTagBeens = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<HotTagBean>>() {
                    }.getType());
                    for (HotTagBean hotTagBeen : hotTagBeens) {
                        tags.add(hotTagBeen.getItem_name());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        if (index == 2) {
            JSONObject goodObj = aCache.getAsJSONObject(Constans.TAG_HOT_SEARCH_EVENT);
           if (goodObj != null){
               try {

                   JSONArray jsonArray = goodObj.getJSONArray("rows");
                   if (jsonArray != null){
                       hotTagBeens = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<HotTagBean>>() {
                       }.getType());
                       for (HotTagBean hotTagBeen : hotTagBeens) {
                           tags.add(hotTagBeen.getItem_name());
                       }
                   }
               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }
        }
        if (index == 3) {
            JSONObject goodObj = aCache.getAsJSONObject(Constans.TAG_HOT_SEARCH_SHOW);
            if (goodObj != null){
                try {
                    JSONArray jsonArray = goodObj.getJSONArray("rows");
                    if (jsonArray != null){
                        hotTagBeens = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<HotTagBean>>() {
                        }.getType());
                        for (HotTagBean hotTagBeen : hotTagBeens) {
                            tags.add(hotTagBeen.getItem_name());
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        KLog.d("----hotTagBeens.size()--",hotTagBeens.size()+"");
        KLog.d("----tags.size()--",tags.size()+"");

        if (hotTagBeens.size() > 0) {
            tagCloudView8.removeAllViews();
            tagCloudView8.setTags(tags);
            tagCloudView8.setOnTagClickListener(this);
        }
    }

    @Override
    public void onTagClick(int position) {
        hotTagId = hotTagBeens.get(position).getDir_id();
        gotoReault(index, true, false, hotTagBeens.get(position).getItem_name());
    }


    public class MyAdapter extends BaseAdapter {

        private List<String> strList;
        private Context mContext;

        public MyAdapter(Context mContext, List<String> strList) {
            this.strList = strList;
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return strList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Myholder myholder = null;
            if (convertView == null) {
                myholder = new Myholder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.row_search_history_item, null);
                myholder.tv_history_value = (TextView) convertView.findViewById(R.id.tv_history_value);
                convertView.setTag(myholder);
            } else {
                myholder = (Myholder) convertView.getTag();

            }
            myholder.tv_history_value.setText(strList.get(position));
            return convertView;
        }

        public class Myholder {
            TextView tv_history_value;
        }
    }

    private int hotTagId;

    private void gotoReault(int index, boolean isHotTag, boolean ishistory, String keyWord) {
        if (index == 1) {
            Bundle bundle = new Bundle();
            if (isHotTag) {
                bundle.putInt("hot_id", hotTagId);
                bundle.putBoolean("ishot", true);
                bundle.putString("key_word", keyWord);
                if(isDetail){
                    bundle.putBoolean("isDetail", isDetail);
                    bundle.putInt("cate_id",cate_id);
                }

                SearchEntity searchEntity = new SearchEntity();
                searchEntity.setType("good");
                searchEntity.setKeyword(keyWord);
                searchKeyDao.add(searchEntity, "good");
            } else {
                if (ishistory) {
                    bundle.putString("key_word", keyWord);
                    if(isDetail){
                        bundle.putBoolean("isDetail", isDetail);
                        bundle.putInt("cate_id",cate_id);
                    }

                    SearchEntity searchEntity = new SearchEntity();
                    searchEntity.setType("good");
                    searchEntity.setKeyword(keyWord);
                    searchKeyDao.add(searchEntity, "good");
                } else {
                    SearchEntity searchEntity = new SearchEntity();
                    searchEntity.setType("good");
                    searchEntity.setKeyword(etInput.getText().toString().trim());
                    searchKeyDao.add(searchEntity, "good");
                    bundle.putString("key_word", keyWord);
                    if(isDetail){
                        bundle.putBoolean("isDetail", isDetail);
                        bundle.putInt("cate_id",cate_id);
                    }

                }

            }
            UIHelper.jump(mActivity, SearchPaiPinActivity.class, bundle);
            return;
        }
        if (index == 2) {
            Bundle bundle = new Bundle();
            if (isHotTag) {
                bundle.putInt("hot_id", hotTagId);
                bundle.putInt("event_type", event_type);
                bundle.putBoolean("ishot", true);
                bundle.putString("key_word", keyWord);

                SearchEntity searchEntity = new SearchEntity();
                searchEntity.setType("event");
                searchEntity.setKeyword(keyWord);
                searchKeyDao.add(searchEntity, "event");

            } else {
                if (ishistory) {
                    bundle.putString("key_word", keyWord);
                    bundle.putInt("event_type", event_type);

                    SearchEntity searchEntity = new SearchEntity();
                    searchEntity.setType("event");
                    searchEntity.setKeyword(keyWord);
                    searchKeyDao.add(searchEntity, "event");
                } else {
                    SearchEntity searchEntity = new SearchEntity();
                    searchEntity.setType("event");
                    searchEntity.setKeyword(etInput.getText().toString().trim());
                    searchKeyDao.add(searchEntity, "event");
                    bundle.putString("key_word", keyWord);
                    bundle.putInt("event_type", event_type);

                }
            }
            KLog.d("-------------------------event_type-----------"+event_type);
            UIHelper.jump(mActivity, SearchEventActivity.class, bundle);
            return;
        }
        if (index == 3) {
            Bundle bundle = new Bundle();
            if (isHotTag) {
                bundle.putInt("hot_id", hotTagId);
                bundle.putBoolean("ishot", true);
                bundle.putString("key_word", keyWord);

                SearchEntity searchEntity = new SearchEntity();
                searchEntity.setType("initDialog");
                searchEntity.setKeyword(keyWord);
                searchKeyDao.add(searchEntity, "initDialog");

            } else {
                if (ishistory) {
                    bundle.putString("key_word", keyWord);
                    SearchEntity searchEntity = new SearchEntity();
                    searchEntity.setType("initDialog");
                    searchEntity.setKeyword(keyWord);
                    searchKeyDao.add(searchEntity, "initDialog");
                } else {
                    SearchEntity searchEntity = new SearchEntity();
                    searchEntity.setType("initDialog");
                    searchEntity.setKeyword(etInput.getText().toString().trim());
                    searchKeyDao.add(searchEntity, "initDialog");
                    bundle.putString("key_word", keyWord);
                }
            }
            UIHelper.jump(mActivity, SearchShowActivity.class, bundle);
            return;
        }
    }

}
