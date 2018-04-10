package com.shunlian.app.newchat.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.newchat.entity.BaseMessage;
import com.shunlian.app.newchat.entity.ImageMessage;
import com.shunlian.app.newchat.entity.LinkMessage;
import com.shunlian.app.newchat.entity.MsgInfo;
import com.shunlian.app.newchat.entity.TextMessage;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

import static com.shunlian.app.newchat.util.TimeUtil.getNewChatTime;
import static com.shunlian.app.utils.BitmapUtil.MAX_HEIGHT;
import static com.shunlian.app.utils.BitmapUtil.MAX_WIDTH;
import static com.shunlian.app.utils.BitmapUtil.MIN_HEIGHT;
import static com.shunlian.app.utils.BitmapUtil.MIN_WIDTH;

/**
 * Created by Administrator on 2018/4/10.
 */

public class ChatMessageAdapter extends BaseRecyclerAdapter<MsgInfo> {

    public static final int LEFT_TXT = 1;
    public static final int RIGHT_TXT = 2;
    public static final int LEFT_IMG = 3;
    public static final int RIGHT_IMG = 4;
    public static final int LINK_MSG = 5;
    public static final int SYS_MSG = 6;

    private final ObjectMapper objectMapper;

    public ChatMessageAdapter(Context context, List<MsgInfo> lists) {
        super(context, false, lists);
        objectMapper = new ObjectMapper();
    }

    public void addMsgInfo(MsgInfo msgInfo) {
        BaseMessage message;
        try {
            message = objectMapper.readValue(msgInfo.getMessage(), BaseMessage.class);
            if (message.getSendType() != BaseMessage.VALUE_SYSTEM) {
                addTimeMessage(msgInfo.getSend_time(), getLastMessageTime(false));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        lists.add(resizeImg(msgInfo));
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        String message = lists.get(position).getMessage();
        BaseMessage baseMessage = str2Msg(message);
        int sendType = baseMessage.getSendType();
        LogUtil.httpLogW("position:" + position + "  lists:" + lists.size() + "    message:" + message);
        if (sendType == BaseMessage.VALUE_LEFT) { //左边消息
            switch (baseMessage.msg_type) {
                case "text":
                    return LEFT_TXT;
                case "image":
                    return LEFT_IMG;
            }
        } else if (sendType == BaseMessage.VALUE_RIGHT) {//右边消息
            LogUtil.httpLogW("baseMessage.msg_type:" + baseMessage.msg_type);
            switch (baseMessage.msg_type) {
                case "text":
                    return RIGHT_TXT;
                case "image":
                    return RIGHT_IMG;
            }
        } else { //系统消息
            return SYS_MSG;
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case LEFT_TXT:
                return new LeftTxtViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_msg_left_txt, parent, false));
            case RIGHT_TXT:
                return new RightTxtViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_msg_right_txt, parent, false));
            case LEFT_IMG:
                return new LeftImgViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_msg_left_img, parent, false));
            case RIGHT_IMG:
                return new RightImgViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_msg_right_img, parent, false));
            case LINK_MSG:
                return new LinkViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_msg_link, parent, false));
            case SYS_MSG:
                return new SysMsgViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_msg_system, parent, false));
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        MsgInfo item = lists.get(position);
        String message = item.getMessage();
        BaseMessage baseMessage = str2Msg(message);
        switch (viewType) {
            case LEFT_TXT:
                handLeftTxt(holder, baseMessage);
                break;
            case RIGHT_TXT:
                handRightTxt(holder, baseMessage);
                break;
            case LEFT_IMG:
                handLeftImg(holder, baseMessage);
                break;
            case RIGHT_IMG:
                handRightImg(holder, baseMessage);
                break;
            case LINK_MSG:
                handLink(holder, baseMessage);
                break;
            case SYS_MSG:
                handSysMsg(holder, baseMessage);
                break;
        }
    }

    public void handLeftTxt(RecyclerView.ViewHolder holder, BaseMessage baseMessage) {

    }

    public void handRightTxt(RecyclerView.ViewHolder holder, BaseMessage baseMessage) {
        TextMessage textMessage = (TextMessage) baseMessage;
        RightTxtViewHolder rightTxtViewHolder = (RightTxtViewHolder) holder;
        GlideUtils.getInstance().loadCircleImage(context, rightTxtViewHolder.miv_icon, textMessage.from_headurl);
        rightTxtViewHolder.tv_content.setText(textMessage.msg_body);
    }

    public void handLeftImg(RecyclerView.ViewHolder holder, BaseMessage baseMessage) {

    }

    public void handRightImg(RecyclerView.ViewHolder holder, BaseMessage baseMessage) {

    }

    public void handLink(RecyclerView.ViewHolder holder, BaseMessage baseMessage) {

    }

    public void handSysMsg(RecyclerView.ViewHolder holder, BaseMessage baseMessage) {
        SysMsgViewHolder sysMsgViewHolder = (SysMsgViewHolder) holder;
        TextMessage msgText = (TextMessage) baseMessage;
        long currentTime = Long.valueOf(msgText.msg_body);
        sysMsgViewHolder.tv_systemMessage.setText(getNewChatTime(currentTime));
    }

    public class LeftTxtViewHolder extends BaseRecyclerViewHolder {

        public LeftTxtViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class RightTxtViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.tv_content)
        TextView tv_content;

        @BindView(R.id.rl_msg_status)
        RelativeLayout rl_msg_status;

        @BindView(R.id.miv_status_error)
        MyImageView miv_status_error;

        @BindView(R.id.pb_right)
        ProgressBar pb_right;

        public RightTxtViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class LeftImgViewHolder extends BaseRecyclerViewHolder {

        public LeftImgViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class RightImgViewHolder extends BaseRecyclerViewHolder {

        public RightImgViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class LinkViewHolder extends BaseRecyclerViewHolder {

        public LinkViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class SysMsgViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.tv_systemMessage)
        TextView tv_systemMessage;

        public SysMsgViewHolder(View itemView) {
            super(itemView);
        }
    }

    public BaseMessage str2Msg(String msg) {
        BaseMessage message = null;
        try {
            message = objectMapper.readValue(msg, BaseMessage.class);
            if ("text".equals(message.msg_type) || "system".equals(message.msg_type) || "sys_text".equals(message.msg_type)) {
                message = objectMapper.readValue(msg, TextMessage.class);
            } else if ("image".equals(message.msg_type)) {
                message = objectMapper.readValue(msg, ImageMessage.class);
            } else if ("link".equals(message.msg_type) || "sys_link".equals(message.msg_type)) {
                message = objectMapper.readValue(msg, LinkMessage.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    public String msg2Str(BaseMessage message) {
        String str = null;
        try {
            str = objectMapper.writeValueAsString(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    private void addTimeMessage(long sendTime, long lastmsgTime) {
        addTimeMessage(false, sendTime, lastmsgTime);
    }

    /**
     * @param isPre
     * @param sendTime    发送时间
     * @param lastmsgTime 上一条消息发送时间
     */
    private void addTimeMessage(boolean isPre, long sendTime, long lastmsgTime) {
        if (isPre && lastmsgTime - sendTime <= 30 && sendTime != 0) {
            return;
        }
        if (!isPre && sendTime - lastmsgTime <= 30 && lastmsgTime != 0) {
            return;
        }
        MsgInfo info = new MsgInfo();
        TextMessage textMessage = new TextMessage();
        textMessage.setSendType(BaseMessage.VALUE_SYSTEM);
        textMessage.msg_body = String.valueOf(sendTime);
        textMessage.msg_type = "system";
        try {
            info.setMessage(objectMapper.writeValueAsString(textMessage));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (isPre) {
            lists.add(0, info);
        } else {
            lists.add(info);
        }
    }

    private long getLastMessageTime(boolean isPre) {
        long sendTime = 0;
        if (isEmpty(lists)) {
            return sendTime;
        } else {
            if (isPre) {
                sendTime = lists.get(0).send_time;
            } else {
                sendTime = lists.get(lists.size() - 1).send_time;
            }
            return sendTime;
        }
    }

    private ImageMessage resizeImg(ImageMessage imageMessage) {
        int currentWidth, currentHeight;//图片的新宽高
        float width, height;//图片的宽高
        double ratio;  //图片的比例

        if (imageMessage.msg_body == null) {
            return imageMessage;
        }
        width = Float.valueOf(imageMessage.msg_body.img_width);
        height = Float.valueOf(imageMessage.msg_body.img_height);
        if (width > MAX_WIDTH && height <= MAX_WIDTH) {//假如宽度大于最大宽度
            ratio = MAX_WIDTH / width;
            currentWidth = MAX_WIDTH;
            if (height < MIN_WIDTH) { //假如宽度小于最大宽度则设置为最小宽度
                currentHeight = MIN_WIDTH;
            } else {
                currentHeight = (int) (height / ratio);
            }
        } else if (height > MAX_HEIGHT && width <= MAX_WIDTH) {//假如高度大于最大宽度
            ratio = height / MAX_WIDTH;
            currentHeight = MAX_HEIGHT;
            if (width < MIN_WIDTH) { //假如高度小于最大高度则设置为最小高度
                currentWidth = MIN_WIDTH;
            } else {
                currentWidth = (int) (width / ratio);
            }
        } else if (width <= MAX_WIDTH && height <= MAX_HEIGHT && width >= MIN_WIDTH && height >= MIN_HEIGHT) {
            //都小于等于最大值 不改变比例
            currentWidth = (int) width;
            currentHeight = (int) height;
        } else if (width >= MAX_WIDTH && height >= MAX_HEIGHT) {
            if (width > height) {
                ratio = width / MAX_WIDTH;
                currentWidth = MAX_WIDTH;
                currentHeight = (int) (height / ratio);
            } else {
                ratio = height / MAX_WIDTH;
                currentHeight = MAX_WIDTH;
                currentWidth = (int) (width / ratio);
            }
        } else {
            //都小于最小值 设为最小值
            currentWidth = MIN_WIDTH;
            currentHeight = MIN_HEIGHT;
        }
        imageMessage.msg_body.img_height = currentHeight;
        imageMessage.msg_body.img_width = currentWidth;
        return imageMessage;
    }

    private MsgInfo resizeImg(MsgInfo info) {
        MsgInfo m = info;
        String message = info.getMessage();
        BaseMessage baseMessage = str2Msg(message);
        if ("image".equals(baseMessage.msg_type)) {
            ImageMessage imageMessage = (ImageMessage) baseMessage;
            imageMessage = resizeImg(imageMessage);
            m.setMessage(msg2Str(imageMessage));
        }
        return m;
    }
}
