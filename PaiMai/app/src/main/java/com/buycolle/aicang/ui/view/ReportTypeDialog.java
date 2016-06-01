package com.buycolle.aicang.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.buycolle.aicang.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by joe on 16/1/5.
 */
public class ReportTypeDialog extends Dialog {

    private Context mContext;

    public CallBack callBack;

    private ImageView iv_close;
    private ListView list;


    public ReportTypeDialog(final Context context, ArrayList<String> types) {
        super(context, R.style.CustomMaterialDialog);
        setContentView(R.layout.dialog_report_type);
        iv_close = (ImageView) findViewById(R.id.iv_close);
        list = (ListView) findViewById(R.id.list);
        getWindow().getAttributes().gravity = Gravity.CENTER;
        mContext = context;
        setCanceledOnTouchOutside(false);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        MyAdapter myAdapter = new MyAdapter(mContext,types);
        list.setAdapter(myAdapter);
        list.setOnItemClickListener(myAdapter);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);

    }

    public ReportTypeDialog setCallBack(CallBack back) {
        this.callBack = back;
        return this;

    }

    public interface CallBack {
        void ok(int type);
    }

    public class MyAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.row_report_type, null);
                myholder.title = (TextView) convertView.findViewById(R.id.title);
                convertView.setTag(myholder);
            } else {
                myholder = (Myholder) convertView.getTag();

            }
            myholder.title.setText(strList.get(position));
            return convertView;
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            dismiss();
            if (callBack != null) {
                callBack.ok(i);
            }
        }

        public class Myholder {
            TextView title;

        }
    }
}
