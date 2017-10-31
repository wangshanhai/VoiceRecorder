package com.layuva.activity.adapter;


/**
 * Copyright (C) 2017  Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.layuva.R;
import com.layuva.activity.model.MessageBean;
import com.layuva.activity.utils.EaseCommonUtils;
import com.layuva.activity.utils.emoji.EaseSmileUtils;
import com.layuva.activity.utils.TimeUtils;

import java.util.List;

/**
 * desc:   消息适配器
 * author: wangshanhai
 * email: ilikeshatang@gmail.com
 * date: 2017/10/30 18:38
 */
public class EaseMessageAdapter extends BaseAdapter {

    private Context context;
    private List<MessageBean> list;

    private onItemClickLister onItemClickLister;


    public EaseMessageAdapter(Context mContext, List<MessageBean> list) {
        this.context = mContext;
        this.list = list;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.ease_row_sent_voice, parent, false);

            viewHolder.iv_voice = (ImageView) convertView.findViewById(R.id.iv_voice);
            viewHolder.tv_length = (TextView) convertView.findViewById(R.id.tv_length);
            viewHolder.timestamp = (TextView) convertView.findViewById(R.id.timestamp);
            viewHolder.bubble = (RelativeLayout) convertView.findViewById(R.id.bubble);
            viewHolder.bubble_text = (RelativeLayout) convertView.findViewById(R.id.bubble_text);
            viewHolder.tv_chatcontent = (TextView) convertView.findViewById(R.id.tv_chatcontent);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        final MessageBean bean = list.get(position);

        if ("".equals(bean.path)) {
            viewHolder.bubble_text.setVisibility(View.VISIBLE);
            viewHolder.bubble.setVisibility(View.GONE);

            Spannable span = EaseSmileUtils.getSmiledText(context, bean.msg);
            viewHolder.tv_chatcontent.setText(span, TextView.BufferType.SPANNABLE);
        } else {
            viewHolder.bubble_text.setVisibility(View.GONE);
            viewHolder.bubble.setVisibility(View.VISIBLE);
            //更改并显示录音条长度
            RelativeLayout.LayoutParams ps = (RelativeLayout.LayoutParams) viewHolder.bubble.getLayoutParams();
            ps.width = EaseCommonUtils.getVoiceLineWight2(context, bean.second);
            viewHolder.bubble.setLayoutParams(ps); //更改语音长条长度

            viewHolder.tv_length.setText(bean.second + "");
        }

        viewHolder.timestamp.setText(TimeUtils.getTime(bean.time));

        viewHolder.bubble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickLister != null) {
                    onItemClickLister.onItemClick(viewHolder.iv_voice, bean.path, position);
                }
            }
        });
        return convertView;
    }

    private class ViewHolder {
        ImageView iv_voice;
        TextView tv_length;
        TextView timestamp;
        RelativeLayout bubble;
        RelativeLayout bubble_text;
        TextView tv_chatcontent;
    }

    public interface onItemClickLister {
        void onItemClick(ImageView imageView, String path, int position);
    }

    public void setOnItemClickLister(EaseMessageAdapter.onItemClickLister onItemClickLister) {
        this.onItemClickLister = onItemClickLister;
    }
}

