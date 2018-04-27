package com.shunlian.app.newchat.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.SimpleRecyclerAdapter;
import com.shunlian.app.adapter.SimpleViewHolder;
import com.shunlian.app.bean.BigImgEntity;
import com.shunlian.app.bean.MyOrderEntity;
import com.shunlian.app.newchat.entity.BaseMessage;
import com.shunlian.app.newchat.entity.EvaluateEntity;
import com.shunlian.app.newchat.entity.EvaluateMessage;
import com.shunlian.app.newchat.entity.GoodsMessage;
import com.shunlian.app.newchat.entity.ImageMessage;
import com.shunlian.app.newchat.entity.LinkMessage;
import com.shunlian.app.newchat.entity.MsgInfo;
import com.shunlian.app.newchat.entity.OrderMessage;
import com.shunlian.app.newchat.entity.SellerHelpMessage;
import com.shunlian.app.newchat.entity.TextMessage;
import com.shunlian.app.newchat.entity.TransferMessage;
import com.shunlian.app.newchat.ui.ChatActivity;
import com.shunlian.app.newchat.websocket.MessageStatus;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.ui.my_comment.LookBigImgAct;
import com.shunlian.app.utils.CenterAlignImageSpan;
import com.shunlian.app.utils.EmojisUtils;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.RatingBarView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public static final int LEFT_GOODS = 5;
    public static final int RIGHT_GOODS = 6;
    public static final int LINK_MSG = 7;
    public static final int EVALUATE_MSG = 8;
    public static final int ORDER_MSG = 9;
    public static final int CHECK_ORDER = 10;
    public static final int SYS_MSG = 11;
    public static final int SELLER_HELP = 12;
    public static final int TRANSFER_MSG = 13;
    public static final int SYS_TEXT = 14;

    private final ObjectMapper objectMapper;
    private RecyclerView recycler;
    private AssetManager am;
    private Pattern patternEmotion;

    public ChatMessageAdapter(Context context, List<MsgInfo> lists, RecyclerView recyclerView) {
        super(context, false, lists);
        objectMapper = new ObjectMapper();
        recycler = recyclerView;
        am = context.getAssets();


        String regexEmotion = "\\[([\u4e00-\u9fa5\\w])+\\]";
        patternEmotion = Pattern.compile(regexEmotion);
    }

    public void addMsgInfo(MsgInfo msgInfo) {
        BaseMessage message;
        try {
            message = objectMapper.readValue(msgInfo.message, BaseMessage.class);
            if (message.getSendType() != BaseMessage.VALUE_SYSTEM) {
                addTimeMessage(msgInfo.send_time, getLastMessageTime(false));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        lists.add(resizeImg(msgInfo));
        ((Activity) context).runOnUiThread(() -> notifyDataSetChanged()); //解决异常
        recycler.scrollToPosition(getItemCount() - 1); //滚动到最底部
    }

    public void addMsgInfo(int position, MsgInfo msgInfo) {
        BaseMessage message;
        try {
            message = objectMapper.readValue(msgInfo.message, BaseMessage.class);
            if (message.getSendType() != BaseMessage.VALUE_SYSTEM) {
                addTimeMessage(true, msgInfo.send_time, getLastMessageTime(true));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        lists.add(position, resizeImg(msgInfo));
    }

    public void addMsgInfos(int position, List<MsgInfo> infos) {
        for (MsgInfo info : infos) {
            addMsgInfo(position, info);
        }
        ((Activity) context).runOnUiThread(() -> notifyDataSetChanged());
    }

    public void updateEvaluateStatus(EvaluateEntity evaluateEntity) {
        for (int i = 0; i < lists.size(); i++) {
            MsgInfo info = lists.get(i);

            BaseMessage baseMsg = str2Msg(info.message);
            EvaluateMessage evaluateMessage;
            if ("evaluate".equals(baseMsg.msg_type)) {
                evaluateMessage = (EvaluateMessage) baseMsg;
                String evaluateId = evaluateMessage.msg_body.evaluate.id;
                if (evaluateEntity.evaluat_id.equals(evaluateId)) {
                    evaluateMessage.msg_body.evaluate.score = evaluateEntity.score;
                    info.message = msg2Str(evaluateMessage);
                    break;
                }
            }
        }
        ((Activity) context).runOnUiThread(() -> notifyDataSetChanged());
    }

    @Override
    public int getItemViewType(int position) {
        String message = lists.get(position).message;
        BaseMessage baseMessage = str2Msg(message);
        int sendType = baseMessage.getSendType();
        if (sendType == BaseMessage.VALUE_LEFT) { //左边消息
            switch (baseMessage.msg_type) {
                case "text":
                    return LEFT_TXT;
                case "image":
                    return LEFT_IMG;
                case "goods":
                    return LEFT_GOODS;
                case "order_confirm":
                    return CHECK_ORDER;
                case "evaluate":
                    return EVALUATE_MSG;
                case "seller_help":
                    return SELLER_HELP;
                case "sys_text":
                    return SYS_TEXT;
                case "order":
                    return ORDER_MSG;
                case "zhuanjie":
                    return TRANSFER_MSG;
            }
        } else if (sendType == BaseMessage.VALUE_RIGHT) {//右边消息
            switch (baseMessage.msg_type) {
                case "text":
                    return RIGHT_TXT;
                case "image":
                    return RIGHT_IMG;
                case "goods":
                    return RIGHT_GOODS;
                case "order_confirm":
                    return CHECK_ORDER;
                case "order":
                    return ORDER_MSG;
                case "evaluate":
                    return EVALUATE_MSG;
                case "sys_text":
                    return SYS_TEXT;
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
            case LEFT_GOODS:
                return new LeftGoodsViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_msg_left_goods, parent, false));
            case RIGHT_GOODS:
                return new RightGoodsViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_msg_right_goods, parent, false));
            case EVALUATE_MSG:
                return new EvaluateViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_msg_evaluate, parent, false));
            case ORDER_MSG:
                return new OrderViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_msg_order, parent, false));
            case LINK_MSG:
                return new LinkViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_msg_link, parent, false));
            case SYS_MSG:
                return new SysMsgViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_msg_system, parent, false));
            case SELLER_HELP:
                return new SellerHelpHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_msg_left_seller_help, parent, false));
            case TRANSFER_MSG:
                return new TransferViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_msg_transfer, parent, false));
            case SYS_TEXT:
                return new SysTextHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_msg_sys_text, parent, false));
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
        String message = item.message;
        BaseMessage baseMessage = str2Msg(message);
        baseMessage.id = item.id;
        baseMessage.sendTime = item.send_time;
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
            case LEFT_GOODS:
                handLeftGoods(holder, baseMessage);
                break;
            case RIGHT_GOODS:
                handRightGoods(holder, baseMessage);
                break;
            case EVALUATE_MSG:
                handEvaluate(holder, baseMessage);
                break;
            case CHECK_ORDER:
                handCheckOrder(holder, baseMessage);
                break;
            case ORDER_MSG:
                handOrder(holder, baseMessage);
                break;
            case LINK_MSG:
                handLink(holder, baseMessage);
                break;
            case SYS_MSG:
                handSysMsg(holder, baseMessage);
                break;
            case SELLER_HELP:
                handSellerHelp(holder, baseMessage);
                break;
            case TRANSFER_MSG:
                handTransfer(holder, baseMessage);
                break;
            case SYS_TEXT:
                handSysText(holder, baseMessage);
                break;
        }
    }

    public void handLeftTxt(RecyclerView.ViewHolder holder, BaseMessage baseMessage) {
        TextMessage textMessage = (TextMessage) baseMessage;
        TextMessage.TextMessageBody messageBody = textMessage.msg_body;
        LeftTxtViewHolder leftTxtViewHolder = (LeftTxtViewHolder) holder;
        GlideUtils.getInstance().loadCornerImage(context, leftTxtViewHolder.miv_icon, textMessage.from_headurl, 3);
        leftTxtViewHolder.tv_content.setText(getEmotionContent(messageBody.text));
    }

    public void handRightTxt(RecyclerView.ViewHolder holder, BaseMessage baseMessage) {
        TextMessage textMessage = (TextMessage) baseMessage;
        TextMessage.TextMessageBody messageBody = textMessage.msg_body;
        RightTxtViewHolder rightTxtViewHolder = (RightTxtViewHolder) holder;
        GlideUtils.getInstance().loadCornerImage(context, rightTxtViewHolder.miv_icon, textMessage.from_headurl, 3);
        rightTxtViewHolder.tv_content.setText(getEmotionContent(messageBody.text));

        if (baseMessage.getStatus() == MessageStatus.SendFail) {
            rightTxtViewHolder.miv_status_error.setVisibility(View.VISIBLE);
            rightTxtViewHolder.pb_right.setVisibility(View.GONE);
        } else if (baseMessage.getStatus() == MessageStatus.Sending) {
            rightTxtViewHolder.miv_status_error.setVisibility(View.GONE);
            rightTxtViewHolder.pb_right.setVisibility(View.VISIBLE);
        } else {
            rightTxtViewHolder.rl_msg_status.setVisibility(View.GONE);
        }
    }

    public void handLeftImg(RecyclerView.ViewHolder holder, BaseMessage baseMessage) {
        LeftImgViewHolder leftImgViewHolder = (LeftImgViewHolder) holder;
        ImageMessage imageMessage = (ImageMessage) baseMessage;
        ImageMessage.ImageBody imageBody = imageMessage.msg_body;
        List<String> imgs = new ArrayList<>();
        if (imageMessage.msg_body != null) {
            ImageMessage.Image image = imageBody.image;
            String imgUrl = image.img_host + image.img_small;
            setImg(leftImgViewHolder.miv_img, image);

            if (!isScrolling) {
                GlideUtils.getInstance().loadImage(context, leftImgViewHolder.miv_img, imgUrl);
                GlideUtils.getInstance().loadCornerImage(context, leftImgViewHolder.miv_icon, imageMessage.from_headurl, 3);
            } else {
                leftImgViewHolder.miv_img.setImageResource(R.mipmap.img_guige_moren);
                leftImgViewHolder.miv_icon.setImageResource(R.mipmap.img_guige_moren);
            }

            leftImgViewHolder.miv_img.setOnClickListener(v -> {
                if (isEmpty(image.img_original)) {
                    return;
                }
                imgs.add(image.img_host + image.img_original);
                //查看大图
                BigImgEntity bigImgEntity = new BigImgEntity();
                bigImgEntity.itemList = (ArrayList<String>) imgs;
                bigImgEntity.index = 0;
                LookBigImgAct.startAct(context, bigImgEntity);
            });
        }
    }

    public void handRightImg(RecyclerView.ViewHolder holder, BaseMessage baseMessage) {
        ImageMessage imageMessag = (ImageMessage) baseMessage;
        ImageMessage.ImageBody imageBody = imageMessag.msg_body;
        RightImgViewHolder rightImgViewHolder = (RightImgViewHolder) holder;
        GlideUtils.getInstance().loadCornerImage(context, rightImgViewHolder.miv_icon, imageMessag.from_headurl, 3);
        final String current_small_url, current_bigl_url;
        if (imageBody.image != null) {
            ImageMessage.Image image = imageBody.image;
            setImg(rightImgViewHolder.miv_img, image);
            if (isEmpty(image.localUrl)) {
                String imagehost = image.img_host;
//                if (!imagehost.startsWith("https")) {
//                    imagehost = "https://" + imagehost;
//                }
                current_small_url = imagehost + image.img_small;

                if (!isScrolling) {
                    GlideUtils.getInstance().loadImage(context, rightImgViewHolder.miv_img, current_small_url);
                } else {
                    rightImgViewHolder.miv_img.setImageResource(R.mipmap.img_guige_moren);
                }
            } else {
                current_bigl_url = "file://" + image.localUrl;
                if (!isScrolling) {
                    GlideUtils.getInstance().loadImage(context, rightImgViewHolder.miv_img, current_bigl_url);
                } else {
                    rightImgViewHolder.miv_img.setImageResource(R.mipmap.img_guige_moren);
                }
            }
            rightImgViewHolder.miv_img.setOnClickListener(v -> {
                List<String> imgs = new ArrayList<>();
                imgs.clear();
                if (isEmpty(image.localUrl)) {
                    imgs.add(image.localUrl + image.img_small);
                } else {
                    imgs.add("file://" + image.localUrl);
                }
                //查看大图
                BigImgEntity bigImgEntity = new BigImgEntity();
                bigImgEntity.itemList = (ArrayList<String>) imgs;
                bigImgEntity.index = 0;
                LookBigImgAct.startAct(context, bigImgEntity);
            });
        }
        if (baseMessage.getStatus() == MessageStatus.SendFail) {
            rightImgViewHolder.miv_status_error.setVisibility(View.VISIBLE);
            rightImgViewHolder.pb_right.setVisibility(View.GONE);
        } else if (baseMessage.getStatus() == MessageStatus.Sending) {
            rightImgViewHolder.miv_status_error.setVisibility(View.GONE);
            rightImgViewHolder.pb_right.setVisibility(View.VISIBLE);
        } else {
            rightImgViewHolder.rl_msg_status.setVisibility(View.GONE);
        }
    }

    public void handLeftGoods(RecyclerView.ViewHolder holder, BaseMessage baseMessage) {
        LeftGoodsViewHolder leftGoodsViewHolder = (LeftGoodsViewHolder) holder;
        GoodsMessage goodsMessage = (GoodsMessage) baseMessage;
        GoodsMessage.GoodsBody goodsBody = goodsMessage.msg_body;
        GlideUtils.getInstance().loadCornerImage(context, leftGoodsViewHolder.miv_icon, goodsMessage.from_headurl, 3);
        if (goodsBody.goods != null) {
            GoodsMessage.Goods goods = goodsBody.goods;
            if (!isScrolling) {
                GlideUtils.getInstance().loadImage(context, leftGoodsViewHolder.miv_good_img, goods.goodsImage);
            } else {
                leftGoodsViewHolder.miv_good_img.setImageResource(R.mipmap.img_guige_moren);
            }

            leftGoodsViewHolder.tv_goods_title.setText(goods.title);
            leftGoodsViewHolder.tv_goods_price.setText(goods.price);

            leftGoodsViewHolder.layout_goods.setOnClickListener(v -> {
                // TODO: 2018/4/13 点击无法跳转
                GoodsDetailAct.startAct(context, goods.goodsId);
            });
        }
    }

    public void handRightGoods(RecyclerView.ViewHolder holder, BaseMessage baseMessage) {
        RightGoodsViewHolder rightGoodsViewHolder = (RightGoodsViewHolder) holder;
        GoodsMessage goodsMessage = (GoodsMessage) baseMessage;
        GoodsMessage.GoodsBody goodsBody = goodsMessage.msg_body;
        GlideUtils.getInstance().loadCornerImage(context, rightGoodsViewHolder.miv_icon, goodsMessage.from_headurl, 3);
        if (goodsBody.goods != null) {
            GoodsMessage.Goods goods = goodsBody.goods;
            if (!isScrolling) {
                GlideUtils.getInstance().loadImage(context, rightGoodsViewHolder.miv_good_img, goods.goodsImage);
            } else {
                rightGoodsViewHolder.miv_good_img.setImageResource(R.mipmap.img_guige_moren);
            }
            rightGoodsViewHolder.tv_goods_title.setText(goods.title);
            rightGoodsViewHolder.tv_goods_price.setText(goods.price);

            rightGoodsViewHolder.layout_goods.setOnClickListener(v -> {
                // TODO: 2018/4/13 点击无法跳转
                GoodsDetailAct.startAct(context, goods.goodsId);
            });
        }

        if (baseMessage.getStatus() == MessageStatus.SendFail) {
            rightGoodsViewHolder.miv_status_error.setVisibility(View.VISIBLE);
            rightGoodsViewHolder.pb_right.setVisibility(View.GONE);
        } else if (baseMessage.getStatus() == MessageStatus.Sending) {
            rightGoodsViewHolder.miv_status_error.setVisibility(View.GONE);
            rightGoodsViewHolder.pb_right.setVisibility(View.VISIBLE);
        } else {
            rightGoodsViewHolder.rl_msg_status.setVisibility(View.GONE);
        }
    }

    public void handEvaluate(RecyclerView.ViewHolder holder, BaseMessage baseMessage) {
        EvaluateMessage evaluateMessage = (EvaluateMessage) baseMessage;
        EvaluateViewHolder evaluateViewHolder = (EvaluateViewHolder) holder;
        EvaluateMessage.EvaluateMessageBody evaluateMessageBody;
        if (evaluateMessage.msg_body != null) {
            evaluateMessageBody = evaluateMessage.msg_body;
            if (evaluateMessageBody.evaluate != null) {
                EvaluateMessage.Evaluate evaluate = evaluateMessageBody.evaluate;
                initEvaluteStatus(evaluate.score, evaluateViewHolder);

                evaluateViewHolder.ratingBar.setOnRatingChangeListener(ratingCount -> {
                    evaluate.score = (int) ratingCount;
                    updateEvaluteStatus(evaluate.score, evaluateViewHolder);
                });
                evaluateViewHolder.tv_comment_status.setOnClickListener(v -> {
                    ((ChatActivity) context).createEvalute(evaluateMessage);
                });
            }
        }
    }

    public void handOrder(RecyclerView.ViewHolder holder, BaseMessage baseMessage) {
        OrderViewHolder orderViewHolder = (OrderViewHolder) holder;
        OrderMessage orderMessage = (OrderMessage) baseMessage;

        if (orderMessage.msg_body != null) {
            OrderMessage.OrderMessageBody orderMessageBody = orderMessage.msg_body;
            if (orderMessageBody.order != null) {
                OrderMessage.Order order = orderMessageBody.order;
                orderViewHolder.tv_seller.setText(order.store_name);
                orderViewHolder.tv_seller_id.setText(order.store_id);
                if (!isEmpty(order.orderGoods)) {
                    MyOrderEntity.OrderGoodsBean orderGoodsBean = order.orderGoods.get(0);
                    if (!isScrolling) {
                        GlideUtils.getInstance().loadImage(context, orderViewHolder.miv_icon, orderGoodsBean.thumb);
                    } else {
                        orderViewHolder.miv_icon.setImageResource(R.mipmap.img_guige_moren);
                    }
                    orderViewHolder.tv_title.setText(orderGoodsBean.title);
                    orderViewHolder.tv_price.setText(orderGoodsBean.price);
                    orderViewHolder.tv_order_number.setText(orderGoodsBean.order_sn);
                }
                orderViewHolder.tv_date.setText(order.create_time);

                orderViewHolder.tv_search_express.setOnClickListener(v -> {
                    //查快递
                    if (!isEmpty(order.express_sn)) {

                    }
                });
            }

            if ("1".equals(orderMessage.from_type) || "2".equals(orderMessage.from_type)) {
                //平台发给用户的
                orderViewHolder.ll_detail.setVisibility(View.VISIBLE);
            } else {
                orderViewHolder.ll_detail.setVisibility(View.GONE);
            }
        }
    }

    public void handCheckOrder(RecyclerView.ViewHolder holder, BaseMessage baseMessage) {
        CheckOrderViewHolder checkOrderViewHolder = (CheckOrderViewHolder) holder;
        OrderMessage orderMessage = (OrderMessage) baseMessage;
        OrderMessage.OrderMessageBody orderMessageBody = orderMessage.msg_body;
        if (orderMessageBody.order != null) {
            OrderMessage.Order order = orderMessageBody.order;
            checkOrderViewHolder.tv_phone_num.setText(String.format(getString(R.string.phone_num), order.phone));
            checkOrderViewHolder.tv_receiver.setText(String.format(getString(R.string.receiver), order.realname));
            checkOrderViewHolder.tv_address_detail.setText(String.format(getString(R.string.address_detail), order.address));
            checkOrderViewHolder.tv_price.setText(order.price);
            if (!isEmpty(order.orderGoods)) {
                List<MyOrderEntity.OrderGoodsBean> orderGoodsBeanList = order.orderGoods;
                MyOrderEntity.OrderGoodsBean orderGoodsBean = orderGoodsBeanList.get(0);
                checkOrderViewHolder.tv_title.setText(orderGoodsBean.title);
                if (!isScrolling) {
                    GlideUtils.getInstance().loadImage(context, checkOrderViewHolder.miv_icon, orderGoodsBean.thumb);
                } else {
                    checkOrderViewHolder.miv_icon.setImageResource(R.mipmap.img_guige_moren);
                }
            }
        }
    }

    public void handLink(RecyclerView.ViewHolder holder, BaseMessage baseMessage) {

    }

    public void handTransfer(RecyclerView.ViewHolder holder, BaseMessage baseMessage) {
        TransferViewHolder transferViewHolder = (TransferViewHolder) holder;
        TransferMessage transferMessage = (TransferMessage) baseMessage;
        if (transferMessage.msg_body != null) {
            TransferMessage.TransferMessageBody transferMessageBody = transferMessage.msg_body;
            String content = transferMessageBody.j_nickname + "将该用户转给" + transferMessageBody.z_nickname;
            int jnameLength = transferMessageBody.j_nickname.length();
            int znameLength = transferMessageBody.z_nickname.length();
            SpannableString styledText = new SpannableString(content);

            styledText.setSpan(new StyleSpan(Typeface.BOLD), 0, jnameLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            styledText.setSpan(new StyleSpan(Typeface.BOLD), jnameLength + 6, jnameLength + 6 + znameLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

//            styledText.setSpan(new AbsoluteSizeSpan(TransformUtil.sp2px(context, 14)), 0, transferMessageBody.j_nickname.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            styledText.setSpan(new AbsoluteSizeSpan(TransformUtil.sp2px(context, 14)), transferMessageBody.j_nickname.length() + 6,
//                    transferMessageBody.j_nickname.length() + 6 + transferMessageBody.z_nickname.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            transferViewHolder.tv_content.setText(styledText + "处理");
            transferViewHolder.tv_reason.setText("备注:" + transferMessageBody.item);
        }
    }

    public void handSysMsg(RecyclerView.ViewHolder holder, BaseMessage baseMessage) {
        SysMsgViewHolder sysMsgViewHolder = (SysMsgViewHolder) holder;
        TextMessage msgText = (TextMessage) baseMessage;
        TextMessage.TextMessageBody messageBody = msgText.msg_body;
        long currentTime = Long.valueOf(messageBody.text);
        sysMsgViewHolder.tv_systemMessage.setText(getNewChatTime(currentTime));
    }

    public void handSellerHelp(RecyclerView.ViewHolder holder, BaseMessage baseMessage) {
        SellerHelpHolder sellerHelpHolder = (SellerHelpHolder) holder;
        SellerHelpMessage sellerHelpMessage = (SellerHelpMessage) baseMessage;
        if (sellerHelpMessage.msg_body != null) {
            SellerHelpMessage.HelpBody msgBody = sellerHelpMessage.msg_body;
            sellerHelpHolder.tv_content.setText(msgBody.help_item);
            if (!isEmpty(msgBody.help_list)) {
                List<SellerHelpMessage.HelpEntity> helpEntityList = msgBody.help_list;
                SimpleRecyclerAdapter simpleRecyclerAdapter = new SimpleRecyclerAdapter<SellerHelpMessage.HelpEntity>(context, R.layout.item_seller_help, helpEntityList) {
                    @Override
                    public void convert(SimpleViewHolder holder, SellerHelpMessage.HelpEntity helpEntity, int position) {
                        TextView textView = holder.getView(R.id.tv_seller_help);
                        textView.setText(helpEntity.item);
                        textView.setOnClickListener(v -> {
                            ((ChatActivity) context).getHelpContent(helpEntity.id, sellerHelpMessage.sid);
                        });
                    }
                };
                sellerHelpHolder.recycler_help.setAdapter(simpleRecyclerAdapter);
            }
        }
        if (!isScrolling) {
            GlideUtils.getInstance().loadCornerImage(context, sellerHelpHolder.miv_icon, sellerHelpMessage.from_headurl, 3);
        } else {
            sellerHelpHolder.miv_icon.setImageResource(R.mipmap.img_guige_moren);
        }
    }

    public void handSysText(RecyclerView.ViewHolder holder, BaseMessage baseMessage) {
        SysTextHolder sysTextHolder = (SysTextHolder) holder;
        TextMessage textMessage = (TextMessage) baseMessage;
        sysTextHolder.tv_date.setText("(" + baseMessage.sendTime + ")");
        if (textMessage.msg_body != null) {
            TextMessage.TextMessageBody textMessageBody = textMessage.msg_body;
            sysTextHolder.tv_content.setText(textMessageBody.text);
        }
    }

    public class LeftTxtViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.tv_content)
        TextView tv_content;

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
        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.miv_img)
        MyImageView miv_img;

        public LeftImgViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class RightImgViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.miv_img)
        MyImageView miv_img;

        @BindView(R.id.rl_msg_status)
        RelativeLayout rl_msg_status;

        @BindView(R.id.miv_status_error)
        MyImageView miv_status_error;

        @BindView(R.id.pb_right)
        ProgressBar pb_right;


        public RightImgViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class LeftGoodsViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.layout_goods)
        RelativeLayout layout_goods;

        @BindView(R.id.tv_goods_price)
        TextView tv_goods_price;

        @BindView(R.id.tv_goods_title)
        TextView tv_goods_title;

        @BindView(R.id.miv_good_img)
        MyImageView miv_good_img;

        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        public LeftGoodsViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class RightGoodsViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.layout_goods)
        RelativeLayout layout_goods;

        @BindView(R.id.tv_goods_price)
        TextView tv_goods_price;

        @BindView(R.id.tv_goods_title)
        TextView tv_goods_title;

        @BindView(R.id.miv_good_img)
        MyImageView miv_good_img;

        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.rl_msg_status)
        RelativeLayout rl_msg_status;

        @BindView(R.id.miv_status_error)
        MyImageView miv_status_error;

        @BindView(R.id.pb_right)
        ProgressBar pb_right;

        public RightGoodsViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class EvaluateViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.tv_comment_status)
        TextView tv_comment_status;

        @BindView(R.id.tv_rating)
        TextView tv_rating;

        @BindView(R.id.ratingBar)
        RatingBarView ratingBar;

        public EvaluateViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class OrderViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.tv_order_number)
        TextView tv_order_number;

        @BindView(R.id.tv_date)
        TextView tv_date;

        @BindView(R.id.tv_title)
        TextView tv_title;

        @BindView(R.id.tv_price)
        TextView tv_price;

        @BindView(R.id.tv_seller)
        TextView tv_seller;

        @BindView(R.id.tv_seller_id)
        TextView tv_seller_id;

        @BindView(R.id.tv_search_express)
        TextView tv_search_express;

        @BindView(R.id.tv_express_number)
        TextView tv_express_number;

        @BindView(R.id.ll_detail)
        LinearLayout ll_detail;

        public OrderViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class CheckOrderViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.tv_title)
        TextView tv_title;

        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.tv_price)
        TextView tv_price;

        @BindView(R.id.tv_receiver)
        TextView tv_receiver;

        @BindView(R.id.tv_phone_num)
        TextView tv_phone_num;

        @BindView(R.id.tv_address_detail)
        TextView tv_address_detail;

        public CheckOrderViewHolder(View itemView) {
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

    public class SellerHelpHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.tv_content)
        TextView tv_content;

        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.recycler_help)
        RecyclerView recycler_help;

        public SellerHelpHolder(View itemView) {
            super(itemView);
            LinearLayoutManager manager = new LinearLayoutManager(context);
            recycler_help.setLayoutManager(manager);
        }
    }

    public class TransferViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.tv_date)
        TextView tv_date;

        @BindView(R.id.tv_content)
        TextView tv_content;

        @BindView(R.id.tv_reason)
        TextView tv_reason;

        public TransferViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class SysTextHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.tv_content)
        TextView tv_content;

        @BindView(R.id.tv_date)
        TextView tv_date;

        public SysTextHolder(View itemView) {
            super(itemView);
        }
    }

    public BaseMessage str2Msg(String msg) {
        BaseMessage message = null;
        try {
            message = objectMapper.readValue(msg, BaseMessage.class);
            switch (message.msg_type) {
                case "text":
                case "system":
                case "sys_text":
                    message = objectMapper.readValue(msg, TextMessage.class);
                    break;
                case "image":
                    message = objectMapper.readValue(msg, ImageMessage.class);
                    break;
                case "link":
                case "sys_link":
                    message = objectMapper.readValue(msg, LinkMessage.class);
                    break;
                case "goods":
                    message = objectMapper.readValue(msg, GoodsMessage.class);
                    break;
                case "evaluate":
                    message = objectMapper.readValue(msg, EvaluateMessage.class);
                    break;
                case "order":
                    message = objectMapper.readValue(msg, OrderMessage.class);
                    break;
                case "zhuanjie":
                    message = objectMapper.readValue(msg, TransferMessage.class);
                    break;
                case "seller_help":
                    message = objectMapper.readValue(msg, SellerHelpMessage.class);
                    break;
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
        TextMessage.TextMessageBody textMessageBody = new TextMessage.TextMessageBody();
        textMessage.setSendType(BaseMessage.VALUE_SYSTEM);
        textMessageBody.text = String.valueOf(sendTime);
        textMessage.msg_body = textMessageBody;
        textMessage.msg_type = "system";
        info.send_time = sendTime;
        try {
            info.message = objectMapper.writeValueAsString(textMessage);
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

        if (imageMessage.msg_body.image == null) {
            return imageMessage;
        }
        ImageMessage.Image image = imageMessage.msg_body.image;
        width = Float.valueOf(image.img_width);
        height = Float.valueOf(image.img_height);
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
        image.img_height = currentHeight;
        image.img_width = currentWidth;
        return imageMessage;
    }

    private MsgInfo resizeImg(MsgInfo info) {
        MsgInfo m = info;
        String message = info.message;
        BaseMessage baseMessage = str2Msg(message);
        if ("image".equals(baseMessage.msg_type)) {
            ImageMessage imageMessage = (ImageMessage) baseMessage;
            imageMessage = resizeImg(imageMessage);
            m.message = msg2Str(imageMessage);
        }
        return m;
    }

    public SpannableString getEmotionContent(String source) {
        int position = -1;
        SpannableString spannableString = new SpannableString(source);
        Matcher matcherEmotion = patternEmotion.matcher(spannableString);

        while (matcherEmotion.find()) {
            // 获取匹配到的具体字符
            String value = matcherEmotion.group();
            Map<Integer, String> emojiMap = EmojisUtils.emojismap();
            if (emojiMap.containsValue(value)) {
                for (int key : emojiMap.keySet()) {
                    if (emojiMap.get(key).equals(value)) {
                        position = key;
                    }
                }
            }
            // 匹配字符串的开始位置
            int start = matcherEmotion.start();
            // 利用表情名字获取到对应的图片
            InputStream is = null;
            try {
                is = am.open(String.format("emojis/%d.png", position));
                // 压缩表情图片
                int size = TransformUtil.dip2px(context, 28);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                Bitmap scaleBitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);
                int end = start + value.length();
                CenterAlignImageSpan span = new CenterAlignImageSpan(scaleBitmap);
                spannableString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                        is = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return spannableString;
    }

    public void itemSendComplete(String tagId, int status) {
        for (int i = 0; i < lists.size(); i++) {
            MsgInfo info = lists.get(i);

            BaseMessage baseMsg;
            ImageMessage imageMsg;
            TextMessage textMsg;
            LinkMessage linkMessage;
            GoodsMessage goodsMessage;
            EvaluateMessage evaluateMessage;
            OrderMessage orderMessage;

            baseMsg = str2Msg(info.message);
            if (tagId.equals(baseMsg.tag_id)) {
                switch (baseMsg.msg_type) {
                    case "image":
                        imageMsg = (ImageMessage) baseMsg;
                        imageMsg.setStatus(status);
                        info.message = msg2Str(imageMsg);
                        break;
                    case "link":
                        linkMessage = (LinkMessage) baseMsg;
                        linkMessage.setStatus(status);
                        info.message = msg2Str(linkMessage);
                        break;
                    case "goods":
                        goodsMessage = (GoodsMessage) baseMsg;
                        goodsMessage.setStatus(status);
                        info.message = msg2Str(goodsMessage);
                        break;
                    case "order":
                        orderMessage = (OrderMessage) baseMsg;
                        orderMessage.setStatus(status);
                        info.message = msg2Str(orderMessage);
                        break;
                    case "evalute ":
                        evaluateMessage = (EvaluateMessage) baseMsg;
                        evaluateMessage.setStatus(status);
                        info.message = msg2Str(evaluateMessage);
                        break;
                    default:
                        textMsg = (TextMessage) baseMsg;
                        textMsg.setStatus(status);
                        info.message = msg2Str(textMsg);
                        break;
                }
                int finalI = i;
                ((Activity) context).runOnUiThread(() -> notifyItemChanged(finalI)); //解决异常
                break;
            }
        }
    }

    public void initEvaluteStatus(int score, EvaluateViewHolder evaluateViewHolder) {
        if (score == 0) {
            evaluateViewHolder.tv_comment_status.setText(getString(R.string.submit));
            evaluateViewHolder.tv_comment_status.setEnabled(true);
            evaluateViewHolder.ratingBar.setClickable(true);
            evaluateViewHolder.tv_comment_status.setTextColor(getColor(R.color.pink_color));
            evaluateViewHolder.tv_comment_status.setBackgroundDrawable(getDrawable(R.drawable.rounded_corner_solid_fef0f3_60px));
        } else {
            evaluateViewHolder.tv_comment_status.setText(getString(R.string.submitted));
            evaluateViewHolder.tv_comment_status.setEnabled(false);
            evaluateViewHolder.ratingBar.setClickable(false);
            evaluateViewHolder.tv_comment_status.setTextColor(getColor(R.color.new_gray));
            evaluateViewHolder.tv_comment_status.setBackgroundDrawable(getDrawable(R.drawable.rounded_corner_solid_f7_60px));
            updateEvaluteStatus(score, evaluateViewHolder);
        }
        evaluateViewHolder.ratingBar.setStar(score);
    }

    public void updateEvaluteStatus(int score, EvaluateViewHolder evaluateViewHolder) {
        switch (score) {
            case 1:
                evaluateViewHolder.tv_rating.setText(getString(R.string.very_unsatisfy));
                break;
            case 2:
                evaluateViewHolder.tv_rating.setText(getString(R.string.unsatisfy));
                break;
            case 3:
                evaluateViewHolder.tv_rating.setText(getString(R.string.commonly));
                break;
            case 4:
                evaluateViewHolder.tv_rating.setText(getString(R.string.satisfy));
                break;
            case 5:
                evaluateViewHolder.tv_rating.setText(getString(R.string.very_satisfy));
                break;
        }
        evaluateViewHolder.tv_rating.setVisibility(View.VISIBLE);
    }

    private void setImg(ImageView iv, ImageMessage.Image image) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) iv.getLayoutParams();
        lp.width = image.img_width;
        lp.height = image.img_height;
        if (image.img_width == 0 || image.img_height == 0) {
            return;
        }
        iv.setLayoutParams(lp);
    }

}
