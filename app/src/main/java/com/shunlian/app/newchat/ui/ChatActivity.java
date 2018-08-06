package com.shunlian.app.newchat.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.App;
import com.shunlian.app.R;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.ImageEntity;
import com.shunlian.app.bean.MyOrderEntity;
import com.shunlian.app.bean.StoreGoodsListEntity;
import com.shunlian.app.bean.UploadPicEntity;
import com.shunlian.app.eventbus_bean.NewMessageEvent;
import com.shunlian.app.newchat.adapter.ChatMessageAdapter;
import com.shunlian.app.newchat.entity.BaseMessage;
import com.shunlian.app.newchat.entity.ChatGoodsEntity;
import com.shunlian.app.newchat.entity.ChatMemberEntity;
import com.shunlian.app.newchat.entity.EvaluateEntity;
import com.shunlian.app.newchat.entity.EvaluateMessage;
import com.shunlian.app.newchat.entity.GoodsMessage;
import com.shunlian.app.newchat.entity.ImageMessage;
import com.shunlian.app.newchat.entity.LinkMessage;
import com.shunlian.app.newchat.entity.MessageEntity;
import com.shunlian.app.newchat.entity.MsgInfo;
import com.shunlian.app.newchat.entity.OrderMessage;
import com.shunlian.app.newchat.entity.TextMessage;
import com.shunlian.app.newchat.entity.UserInfoEntity;
import com.shunlian.app.newchat.websocket.EasyWebsocketClient;
import com.shunlian.app.newchat.websocket.MemberStatus;
import com.shunlian.app.newchat.websocket.MessageStatus;
import com.shunlian.app.newchat.websocket.Status;
import com.shunlian.app.photopick.PhotoPickerActivity;
import com.shunlian.app.photopick.PhotoPickerIntent;
import com.shunlian.app.photopick.SelectModel;
import com.shunlian.app.presenter.ChatPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.help.HelpOneAct;
import com.shunlian.app.ui.store.StoreAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.NetworkUtils;
import com.shunlian.app.view.IChatView;
import com.shunlian.app.widget.ChatOrderDialog;
import com.shunlian.app.widget.refresh.turkey.SlRefreshView;
import com.shunlian.app.widget.refreshlayout.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.shunlian.app.utils.BitmapUtil.getFileFromMediaUri;

/**
 * 示例界面
 *
 * @author lucher
 */
public class ChatActivity extends BaseActivity implements ChatView, IChatView, ChatOrderDialog.OnOrderSendListener, EasyWebsocketClient.OnMessageReceiveListener {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @BindView(R.id.input_panel)
    ChatInput et_input;

    @BindView(R.id.recycler_chat)
    RecyclerView recycler_chat;

    @BindView(R.id.refreshview)
    SlRefreshView refreshview;

    @BindView(R.id.ll_rootView)
    LinearLayout ll_rootView;

    public static final int OPEN_ALBUM = 1;
    public static final int OPEN_CAMERA = 2;
    public static final int SELECT_GOODS = 3;
    public static final int SELECT_STORE_GOODS = 4;
    private Uri tempTakeUri;

    //websocket客户端
    private EasyWebsocketClient mWebsocketClient;
    private ObjectMapper objectMapper;
    private String from_id, from_type, from_nickname, from_headurl;
    private List<MsgInfo> messages = new ArrayList<>();
    private PhotoPickerIntent picIntent;
    private String currentTagId;
    private ObjectMapper mObjectMapper;
    private ChatMessageAdapter mAdapter;
    private String currentUserId;  //用户ID
    private String chat_m_user_Id; //管理员的id;
    private String chatRoleType; //0，普通用户，1平台客服管理员，2平台普通客服，3商家客服管理员，4商家普通客服
    private String chatName;
    private String chatShopId;
    private UserInfoEntity.Info.User mCurrentUser;
    private String currentDeviceId;
    private LinearLayoutManager manager;
    private ChatMemberEntity.ChatMember currentChatMember;
    private ChatPresenter mPresenter;
    private ChatOrderDialog chatOrderDialog;
    private String lastMessageSendTime;
    private boolean isFirst = true;
    private GoodsDeatilEntity mGoodsDeatilEntity;
    private int firstPosition;
    private int refreshViewHeight;
    private String currentSid;

    public static void startAct(Context context, ChatMemberEntity.ChatMember chatMember) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("chatMember", chatMember);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void startAct(Context context, ChatMemberEntity.ChatMember chatMember, GoodsDeatilEntity goodsDeatilEntity) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("chatMember", chatMember);
        intent.putExtra("goods", goodsDeatilEntity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initData() {
        immersionBar.statusBarColor(R.color.white)
                .statusBarDarkFont(true, 0.2f)
                .keyboardEnable(true)
                .init();
        currentChatMember = (ChatMemberEntity.ChatMember) getIntent().getSerializableExtra("chatMember");
        mGoodsDeatilEntity = getIntent().getParcelableExtra("goods");

        currentDeviceId = DeviceInfoUtil.getDeviceId(this);
        chatName = getIntent().getStringExtra("chatName");

        try {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.turkey_release);
            refreshViewHeight = bitmap.getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }

        init();
        initPrf();
        initChat();
        initRightTxt();
    }

    @Override
    protected void initListener() {
        tv_title_right.setOnClickListener(this);
        recycler_chat.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    et_input.setInputMode(ChatInput.InputMode.NONE, false);
                    break;
            }
            return false;
        });
        refreshview.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                firstPosition = manager.findLastVisibleItemPosition();
                LogUtil.httpLogW("onRefresh:" + firstPosition);
                getChatHistory(isFirst);
            }

            @Override
            public void onLoadMore() {

            }
        });
        ll_rootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            int heightDiff = ll_rootView.getRootView().getHeight() - ll_rootView.getHeight();
            if (heightDiff > 100) { //高度变小100像素则认为键盘弹出
                LogUtil.httpLogW("键盘弹出");
                recycler_chat.scrollToPosition(mAdapter.getItemCount() - 1);//刷新到底部
            }
        });
        super.initListener();
    }

    /**
     * 初始化
     */
    private void init() {
        objectMapper = new ObjectMapper();

        if (currentChatMember != null) {
            chatName = currentChatMember.nickname;
        }
        if (!TextUtils.isEmpty(chatName)) {
            tv_title.setText(chatName);
        }
        et_input.setChatView(this);
    }

    public void initPrf() {

        ((SimpleItemAnimator) recycler_chat.getItemAnimator()).setSupportsChangeAnimations(false);//取消刷新动画
        recycler_chat.setNestedScrollingEnabled(false);
        manager = new LinearLayoutManager(this);
        manager.setRecycleChildrenOnDetach(true);
        RecyclerView.RecycledViewPool pool = new RecyclerView.RecycledViewPool();
        pool.setMaxRecycledViews(0, 20);
        recycler_chat.setRecycledViewPool(pool);
        recycler_chat.setLayoutManager(manager);
        mAdapter = new ChatMessageAdapter(this, messages, recycler_chat);
        recycler_chat.setAdapter(mAdapter);
        mPresenter = new ChatPresenter(this, this);
    }

    private void initChat() {
        mObjectMapper = new ObjectMapper();

        if (currentChatMember != null) {
            if (isEmpty(currentChatMember.m_user_id)) {
                chat_m_user_Id = currentChatMember.user_id;
            } else {
                chat_m_user_Id = currentChatMember.m_user_id;
            }
            chatShopId = currentChatMember.shop_id;
            chatRoleType = currentChatMember.type;
            currentSid = currentChatMember.sid;
        } else {
            chatRoleType = getIntent().getStringExtra("role_type");
        }
        mWebsocketClient = EasyWebsocketClient.getInstance(this);
        if (mWebsocketClient.getUserInfoEntity() == null) {
            refreshview.setCanRefresh(false);
            refreshview.setCanLoad(false);
            Common.staticToast("初始化聊天失败");
            return;
        } else {
            refreshview.setCanRefresh(true);
            refreshview.setCanLoad(false);

            mWebsocketClient.addOnMessageReceiveListener(this);
            mCurrentUser = mWebsocketClient.getUser();
            initUser(mCurrentUser);
            mAdapter.setUser(mCurrentUser);
            mAdapter.setCurrentStatus(mWebsocketClient.getMemberStatus());
            mAdapter.setChatRole(Integer.valueOf(chatRoleType));
        }
        //获取历史消息
        if (NetworkUtils.isNetworkOpen(this)) {
            getChatHistory(isFirst);
        }

        readedMsg(chat_m_user_Id);
    }

    private void initRightTxt() {
        switch (mWebsocketClient.getMemberStatus()) {
            case Admin: //
                tv_title_right.setText(getStringResouce(R.string.switch_other));
                et_input.showCommentBtn();
                et_input.showFastComment();
                tv_title_right.setVisibility(View.VISIBLE);
                break;
            case Seller:
                tv_title_right.setText(getStringResouce(R.string.switch_other));
                et_input.showGoodsBtn();
                et_input.showCommentBtn();
                et_input.showFastComment();
                tv_title_right.setVisibility(View.VISIBLE);
                break;
            case Member:
                if ("1".equals(chatRoleType) || "2".equals(chatRoleType)) { //对方是平台客服
                    tv_title_right.setText(getStringResouce(R.string.help_center));
                    et_input.showOrderBtn();
                    tv_title_right.setVisibility(View.GONE);
                } else {
                    tv_title_right.setText(getStringResouce(R.string.to_shop));
                    tv_title_right.setVisibility(View.VISIBLE);
                    et_input.showGoodsBtn();
                }
                break;
        }
    }

    public void getChatHistory(boolean b) {
        switch (chatRoleType) {
            case "0":
                if (mWebsocketClient.getMemberStatus() == MemberStatus.Seller) {
                    mPresenter.shopChatUserHistoryData(b, chat_m_user_Id, mCurrentUser.user_id, lastMessageSendTime);
                } else if (mWebsocketClient.getMemberStatus() == MemberStatus.Admin) {
                    mPresenter.platformChatUserHistoryData(b, chat_m_user_Id, mCurrentUser.user_id, lastMessageSendTime);
                }
                break;
            case "1":
            case "2":
                mPresenter.getChatHistoryMessage(b, currentUserId, "1", currentChatMember.m_user_id, "", lastMessageSendTime);
                break;
            case "3":
            case "4":
                mPresenter.getChatHistoryMessage(b, currentUserId, "2", "", chatShopId, lastMessageSendTime);
                break;
        }
    }

    @Override
    protected void onResume() {
        if (mWebsocketClient.getStatus() == Status.CONNECTED) {
            mWebsocketClient.setChating(true);
        }
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_title_right:
                switch2Jump();
                break;
        }
    }

    public void switch2Jump() {
        switch (mWebsocketClient.getMemberStatus()) {
            case Admin: //
                if (isEmpty(currentSid)) {
                    return;
                }
                SwitchOtherActivity.startAct(this, currentUserId, chat_m_user_Id, currentSid);
                break;
            case Seller:
                if (isEmpty(currentSid)) {
                    return;
                }
                SwitchOtherActivity.startAct(this, currentUserId, chat_m_user_Id, currentSid);
                break;
            case Member:
                if ("1".equals(chatRoleType) || "2".equals(chatRoleType)) { // 对方是平台客服
                    //跳转帮助中心
                    HelpOneAct.startAct(this);
                } else {//对方是商家客服
                    //跳转去店铺
                    StoreAct.startAct(this, chatShopId);
                }
                break;
        }
    }

    private void initUser(UserInfoEntity.Info.User user) {
        if (user != null) {
            currentUserId = user.user_id;
            from_headurl = user.headurl;
            from_id = user.join_id;
            from_nickname = user.nickname;
            from_type = user.type;

            et_input.setJoinId(user.join_id);
            et_input.setRoleType(from_type);
        }

    }

    @Override
    public void sendImage() {
        if (picIntent == null) {
            picIntent = new PhotoPickerIntent(this);
            picIntent.setSelectModel(SelectModel.MULTI);
            picIntent.setShowCarema(false);
            picIntent.setMaxTotal(5); // 最多选择照片数量，默认为9
        }
        try {
            startActivityForResult(picIntent, OPEN_ALBUM);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "启动相册异常~", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void sendPhoto() {
        String photoPath = App.CACHE_PATH + "/temp_take_image_" + System.currentTimeMillis() + "_" + Math.random() + ".png";
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        tempTakeUri = Uri.fromFile(new File(photoPath));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempTakeUri);
        startActivityForResult(intent, OPEN_CAMERA);
    }

    @Override
    public void sendText() {
        sendTextMessage(et_input.getText().toString());
        et_input.setText("");

//        if (et_input.isBottomPanelVisible()) {
//            et_input.setBottomPanelInVisible();
//        }
    }

    @Override
    public void sendGoods() {
        if (mWebsocketClient.getMemberStatus() == MemberStatus.Member) {
            SelectGoodsActivity.startActForResult(this, chatShopId, SELECT_GOODS);
        } else {
            SelectStoreGoodsActivity.startActForResult(this, mCurrentUser.shop_id, SELECT_STORE_GOODS);
        }
    }

    @Override
    public void sendOrder() {
        if (chatOrderDialog == null) {
            chatOrderDialog = new ChatOrderDialog(this);
            chatOrderDialog.setOnOrderListener(this);
        }
        chatOrderDialog.show();
    }

    @Override
    public void sendComment() {
        sendEvaluteMessage();
    }

    @Override
    public void sendFast(String content) {
        sendTextMessage(content);
    }

    /**
     * 发送文字消息
     */
    private void sendTextMessage(String msg) {
        MsgInfo msgInfo = new MsgInfo();

        currentTagId = creatMsgTagId(from_id);
        TextMessage textMessage = new TextMessage();
        textMessage.from_user_id = currentUserId;
        textMessage.from_type = from_type;
        textMessage.from_nickname = from_nickname;
        textMessage.from_headurl = from_headurl;
        textMessage.to_user_id = chat_m_user_Id;
        textMessage.to_type = chatRoleType;
        textMessage.to_shop_id = chatShopId;
        textMessage.msg_type = "text";
        textMessage.setSendType(BaseMessage.VALUE_RIGHT);
        textMessage.tag_id = currentTagId;
        textMessage.type = "send_message";

        TextMessage.TextMessageBody textMessageBody = new TextMessage.TextMessageBody();
        textMessageBody.text = msg;
        textMessage.msg_body = textMessageBody;

        if (mWebsocketClient.getStatus() == Status.CONNECTED) {
            LogUtil.httpLogW("发送的文字消息:" + mAdapter.msg2Str(textMessage));
            mWebsocketClient.send(mAdapter.msg2Str(textMessage));
            msgInfo.send_time = System.currentTimeMillis() / 1000;
            textMessage.setStatus(MessageStatus.Sending);
            msgInfo.message = mAdapter.msg2Str(textMessage);
            mAdapter.addMsgInfo(msgInfo);
        }
    }

    /**
     * 新建图片消息
     */

    public void buildImageMessage(String imgPath) {
        MsgInfo msgInfo = new MsgInfo();

        currentTagId = creatMsgTagId(from_id);
        ImageMessage imageMessage = new ImageMessage();
        imageMessage.from_user_id = currentUserId;
        imageMessage.from_type = from_type;
        imageMessage.from_nickname = from_nickname;
        imageMessage.from_headurl = from_headurl;
        imageMessage.to_user_id = chat_m_user_Id;
        imageMessage.to_type = chatRoleType;
        imageMessage.to_shop_id = chatShopId;
        imageMessage.msg_type = "image";
        imageMessage.setSendType(BaseMessage.VALUE_RIGHT);
        imageMessage.tag_id = currentTagId;
        imageMessage.type = "send_message";

        ImageMessage.Image image = new ImageMessage.Image();
        ImageMessage.ImageBody imageBody = new ImageMessage.ImageBody();
        Bitmap bitmap = null;
        try {
            image.localUrl = imgPath;
            bitmap = BitmapFactory.decodeFile(imgPath);
            image.img_height = bitmap.getHeight();
            image.img_width = bitmap.getWidth();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
        imageBody.image = image;
        imageMessage.msg_body = imageBody;
        msgInfo.send_time = System.currentTimeMillis() / 1000;
        LogUtil.httpLogW("发送图片消息:" + mAdapter.msg2Str(imageMessage));
        msgInfo.message = mAdapter.msg2Str(imageMessage);
        if (mWebsocketClient.getStatus() == Status.CONNECTED) {
            mAdapter.addMsgInfo(msgInfo);
            mAdapter.itemSendComplete(currentTagId, MessageStatus.Sending);
            compressImgs(imgPath, imageMessage.tag_id, imageMessage);
        }
    }

    /**
     * 发送图片消息
     */
    private void sendImgMessage(ImageMessage imageMessage) {
        if (mWebsocketClient.getStatus() == Status.CONNECTED) {
            mWebsocketClient.send(mAdapter.msg2Str(imageMessage));
        }
    }

    /**
     * 构建一条商品消息
     */
    public void sendGoodsMessage(ChatGoodsEntity.Goods goods) {
        MsgInfo msgInfo = new MsgInfo();

        currentTagId = creatMsgTagId(from_id);
        GoodsMessage goodsMessage = new GoodsMessage();
        goodsMessage.from_user_id = currentUserId;
        goodsMessage.from_type = from_type;
        goodsMessage.from_nickname = from_nickname;
        goodsMessage.from_headurl = from_headurl;
        goodsMessage.to_user_id = chat_m_user_Id;
        goodsMessage.to_type = chatRoleType;
        goodsMessage.to_shop_id = chatShopId;
        goodsMessage.msg_type = "goods";
        goodsMessage.setSendType(BaseMessage.VALUE_RIGHT);
        goodsMessage.tag_id = currentTagId;
        goodsMessage.type = "send_message";


        GoodsMessage.Goods good = new GoodsMessage.Goods();
        GoodsMessage.GoodsBody goodsBody = new GoodsMessage.GoodsBody();
        good.goodsImage = goods.thumb;
        good.title = goods.title;
        good.goodsId = goods.goods_id;
        good.price = goods.price;
        goodsBody.goods = good;
        goodsMessage.msg_body = goodsBody;

        if (mWebsocketClient.getStatus() == Status.CONNECTED) {
            LogUtil.httpLogW("发送的商品消息:" + mAdapter.msg2Str(goodsMessage));
            mWebsocketClient.send(mAdapter.msg2Str(goodsMessage));
            msgInfo.send_time = System.currentTimeMillis() / 1000;
            goodsMessage.setStatus(MessageStatus.Sending);
            msgInfo.message = mAdapter.msg2Str(goodsMessage);
            mAdapter.addMsgInfo(msgInfo);
        }
    }

    /**
     * 构建一条商品消息
     */
    public void sendGoodsMessage(StoreGoodsListEntity.MData mData) {
        MsgInfo msgInfo = new MsgInfo();

        currentTagId = creatMsgTagId(from_id);
        GoodsMessage goodsMessage = new GoodsMessage();
        goodsMessage.from_user_id = currentUserId;
        goodsMessage.from_type = from_type;
        goodsMessage.from_nickname = from_nickname;
        goodsMessage.from_headurl = from_headurl;
        goodsMessage.to_user_id = chat_m_user_Id;
        goodsMessage.to_type = chatRoleType;
        goodsMessage.to_shop_id = chatShopId;
        goodsMessage.msg_type = "goods";
        goodsMessage.setSendType(BaseMessage.VALUE_RIGHT);
        goodsMessage.tag_id = currentTagId;
        goodsMessage.type = "send_message";

        GoodsMessage.Goods good = new GoodsMessage.Goods();
        GoodsMessage.GoodsBody goodsBody = new GoodsMessage.GoodsBody();
        good.goodsImage = mData.whole_thumb;
        good.title = mData.title;
        good.goodsId = mData.id;
        good.price = mData.price;
        goodsBody.goods = good;
        goodsMessage.msg_body = goodsBody;

        if (mWebsocketClient.getStatus() == Status.CONNECTED) {
            LogUtil.httpLogW("发送的商品消息:" + mAdapter.msg2Str(goodsMessage));
            mWebsocketClient.send(mAdapter.msg2Str(goodsMessage));
            msgInfo.send_time = System.currentTimeMillis() / 1000;
            goodsMessage.setStatus(MessageStatus.Sending);
            msgInfo.message = mAdapter.msg2Str(goodsMessage);
            mAdapter.addMsgInfo(msgInfo);
        }
    }

    /**
     * 构建一条商品消息
     */
    public void sendGoodsMessage(GoodsMessage.GoodsBody body) {
        MsgInfo msgInfo = new MsgInfo();

        currentTagId = creatMsgTagId(from_id);
        GoodsMessage goodsMessage = new GoodsMessage();
        goodsMessage.from_user_id = currentUserId;
        goodsMessage.from_type = from_type;
        goodsMessage.from_nickname = from_nickname;
        goodsMessage.from_headurl = from_headurl;
        goodsMessage.to_user_id = chat_m_user_Id;
        goodsMessage.to_type = chatRoleType;
        goodsMessage.to_shop_id = chatShopId;
        goodsMessage.msg_type = "goods";
        goodsMessage.setSendType(BaseMessage.VALUE_RIGHT);
        goodsMessage.tag_id = currentTagId;
        goodsMessage.type = "send_message";

        goodsMessage.msg_body = body;

        if (mWebsocketClient.getStatus() == Status.CONNECTED) {
            LogUtil.httpLogW("发送的商品消息:" + mAdapter.msg2Str(goodsMessage));
            mWebsocketClient.send(mAdapter.msg2Str(goodsMessage));
            msgInfo.send_time = System.currentTimeMillis() / 1000;
            goodsMessage.setStatus(MessageStatus.Sending);
            msgInfo.message = mAdapter.msg2Str(goodsMessage);
            mAdapter.addMsgInfo(msgInfo);
        }
    }

    /**
     * 发送一条评价消息
     */

    public void sendEvaluteMessage() {
        MsgInfo msgInfo = new MsgInfo();

        currentTagId = creatMsgTagId(from_id);
        EvaluateMessage evaluateMessage = new EvaluateMessage();
        evaluateMessage.from_user_id = currentUserId;
        evaluateMessage.from_type = from_type;
        evaluateMessage.from_nickname = from_nickname;
        evaluateMessage.from_headurl = from_headurl;
        evaluateMessage.to_user_id = chat_m_user_Id;
        evaluateMessage.to_shop_id = chatShopId;
        evaluateMessage.to_type = currentChatMember.type;
        evaluateMessage.msg_type = "evaluate";
        evaluateMessage.setSendType(BaseMessage.VALUE_SYSTEM);
        evaluateMessage.tag_id = currentTagId;
        evaluateMessage.type = "send_message";

        if (isEmpty(currentSid)) {
            return;
        }
        EvaluateMessage.EvaluateMessageBody evaluateMessageBody = new EvaluateMessage.EvaluateMessageBody();
        EvaluateMessage.Evaluate evaluate = new EvaluateMessage.Evaluate();
        evaluate.sid = currentSid;
        evaluateMessageBody.evaluate = evaluate;
        evaluateMessage.msg_body = evaluateMessageBody;

        if (mWebsocketClient.getStatus() == Status.CONNECTED) {
            LogUtil.httpLogW("发送的评价消息:" + mAdapter.msg2Str(evaluateMessage));
            mWebsocketClient.send(mAdapter.msg2Str(evaluateMessage));
            msgInfo.send_time = System.currentTimeMillis() / 1000;
            evaluateMessage.setStatus(MessageStatus.Sending);
            msgInfo.message = mAdapter.msg2Str(evaluateMessage);
            mAdapter.addMsgInfo(msgInfo);
        }
    }

    /**
     * 发送评分消息
     */
    public void createEvalute(EvaluateMessage evaluateMessage) {
        if (evaluateMessage.msg_body != null) {
            EvaluateMessage.EvaluateMessageBody evaluateMessageBody = evaluateMessage.msg_body;
            if (evaluateMessageBody.evaluate != null) {
                EvaluateMessage.Evaluate evaluate = evaluateMessageBody.evaluate;
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("type", "pingjia");
                    jsonObject.put("msg_id", evaluateMessage.id);
                    if (evaluate.selectScore != 0) {
                        jsonObject.put("score", evaluate.selectScore);
                    } else if (evaluate.score != 0) {
                        jsonObject.put("score", evaluate.score);
                    }
                    jsonObject.put("evaluat_id", evaluate.id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (mWebsocketClient.getStatus() == Status.CONNECTED) {
                    LogUtil.httpLogW("发送的评价消息:" + jsonObject.toString());
                    mWebsocketClient.send(jsonObject.toString());
                }
            }
        }
    }

    /**
     * 发送一条订单消息
     */

    public void sendOrderMessage(MyOrderEntity.Orders orders) {
        MsgInfo msgInfo = new MsgInfo();

        currentTagId = creatMsgTagId(from_id);
        OrderMessage orderMessage = new OrderMessage();
        orderMessage.from_user_id = currentUserId;
        orderMessage.from_type = from_type;
        orderMessage.from_nickname = from_nickname;
        orderMessage.from_headurl = from_headurl;
        orderMessage.to_user_id = chat_m_user_Id;
        orderMessage.to_type = chatRoleType;
        orderMessage.to_shop_id = chatShopId;
        orderMessage.msg_type = "order";
        orderMessage.setSendType(BaseMessage.VALUE_SYSTEM);
        orderMessage.tag_id = currentTagId;
        orderMessage.type = "send_message";

        OrderMessage.OrderMessageBody orderMessageBody = new OrderMessage.OrderMessageBody();
        OrderMessage.Order order = new OrderMessage.Order();
        order.orderId = orders.id;
        order.ordersn = orders.order_sn;

        List<OrderMessage.OrderGoods> goodsList = new ArrayList<>();
        for (int i = 0; i < orders.order_goods.size(); i++) {
            MyOrderEntity.OrderGoodsBean orderGoodsBean = orders.order_goods.get(i);
            OrderMessage.OrderGoods goods = new OrderMessage.OrderGoods();
            goods.goodsId = orderGoodsBean.goods_id;
            goods.goodsImage = orderGoodsBean.thumb;
            goods.price = orderGoodsBean.price;
            goods.title = orderGoodsBean.title;
            goodsList.add(goods);
        }
        order.orderGoods = goodsList;
        order.store_id = orders.store_id;
        order.store_name = orders.store_name;
        order.create_time = orders.create_time;
        order.express_sn = orders.express_sn;
        orderMessageBody.order = order;
        orderMessage.msg_body = orderMessageBody;

        if (mWebsocketClient.getStatus() == Status.CONNECTED) {
            LogUtil.httpLogW("发送的订单消息:" + mAdapter.msg2Str(orderMessage));
            mWebsocketClient.send(mAdapter.msg2Str(orderMessage));
            msgInfo.send_time = System.currentTimeMillis() / 1000;
            orderMessage.setStatus(MessageStatus.Sending);
            msgInfo.message = mAdapter.msg2Str(orderMessage);
            mAdapter.addMsgInfo(msgInfo);
        }
    }

    /**
     * 获取商家自动回复帮助内容
     */
    public void getHelpContent(String helpId, String sId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", "seller_help_content");
            jsonObject.put("id", helpId);
            jsonObject.put("sid", sId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mWebsocketClient.getStatus() == Status.CONNECTED) {
            LogUtil.httpLogW("发送的订单消息:" + jsonObject.toString());
            mWebsocketClient.send(jsonObject.toString());
        }
    }

    /**
     * 构建一条链接系统消息
     */
    public void buildLinkMessage(GoodsDeatilEntity goodsDeatilEntity) {
        MsgInfo msgInfo = new MsgInfo();

        LinkMessage linkMessage = new LinkMessage();
        linkMessage.msg_type = "sys_link";
        linkMessage.setSendType(BaseMessage.VALUE_SYSTEM);

        LinkMessage.LinkBody linkBody = new LinkMessage.LinkBody();
        linkBody.goodsImage = goodsDeatilEntity.thumb;
        linkBody.title = goodsDeatilEntity.title;
        linkBody.price = goodsDeatilEntity.price;
        linkBody.goodsId = goodsDeatilEntity.id;
        linkMessage.msg_body = linkBody;
        msgInfo.message = mAdapter.msg2Str(linkMessage);

        mAdapter.addMsgInfo(0, msgInfo);
    }


    /**
     * 消息已读上报
     */

    private void readedMsg(String chatUserId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", "msg_read");
            jsonObject.put("to_user_id", chatUserId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mWebsocketClient.getStatus() == Status.CONNECTED) {
            mWebsocketClient.send(jsonObject.toString());
            LogUtil.httpLogW("消息已读上报:" + jsonObject.toString());
            int unreadCount = currentChatMember.unread_count;
            EventBus.getDefault().post(new NewMessageEvent(-unreadCount)); //消息已读,减去消息数量
        }
    }

    /**
     * 根据用户ID和时间戳生成唯一一个tagId标识来判断消息发送成功
     */
    public String creatMsgTagId(String useId) {
        if (TextUtils.isEmpty(useId)) {
            return null;
        }
        return String.valueOf(currentDeviceId + "_" + System.currentTimeMillis()) + useId;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String filePath;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case OPEN_ALBUM:
                    ArrayList<String> picturePaths = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
                    for (String path : picturePaths) {
                        if (!TextUtils.isEmpty(path)) {
                            buildImageMessage(path);
                        }
                    }
                    break;
                case OPEN_CAMERA:
                    filePath = getFileFromMediaUri(this, tempTakeUri);
                    if (!TextUtils.isEmpty(filePath)) {
                        buildImageMessage(filePath);
                    }
                    break;
                case SELECT_GOODS:
                    List<ChatGoodsEntity.Goods> goodsList = (List<ChatGoodsEntity.Goods>) data.getSerializableExtra("select_goods");
                    for (ChatGoodsEntity.Goods goods : goodsList) {
                        sendGoodsMessage(goods);
                    }
                    break;
                case SELECT_STORE_GOODS:
                    StoreGoodsListEntity.MData mData = (StoreGoodsListEntity.MData) data.getSerializableExtra("select_goods");
                    if (mData != null) {
                        sendGoodsMessage(mData);
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String splitDeviceId(String tagId) {
        String deviceId = null;
        String[] data;
        if (TextUtils.isEmpty(tagId)) {
            return null;
        }
        try {
            data = tagId.split("_");
            deviceId = data[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deviceId;
    }

    @Override
    protected void onPause() {
        //用户离开聊天页面
        if (mWebsocketClient != null && mWebsocketClient.getStatus() == Status.CONNECTED) {
            mWebsocketClient.setChating(false);
        }
        super.onPause();
    }

    @Override
    public void showFailureView(int SELECT_STORE_GOODS) {

    }

    @Override
    public void showDataEmptyView(int SELECT_STORE_GOODS) {

    }

    @Override
    public void uploadImg(UploadPicEntity picEntity, String tagId, ImageMessage imageMessage) {
        if (picEntity != null) {
            ImageMessage.Image image = new ImageMessage.Image();
            List<UploadPicEntity.SizeInfo> sizeInfos = picEntity.sizeInfo;
            if (!isEmpty(sizeInfos)) {
                image.img_height = sizeInfos.get(0).height;
                image.img_width = sizeInfos.get(0).width;
            }
            image.img_host = picEntity.domain;
            image.img_small = picEntity.relativePath.get(0);
            image.img_original = picEntity.newFileName.get(0);
            imageMessage.msg_body.image = image;
            sendImgMessage(imageMessage);
        } else {
            mAdapter.itemSendComplete(tagId, MessageStatus.SendFail);
        }
    }

    @Override
    public void getHistoryMsg(List<MsgInfo> msgInfoList, String lasetSendTime, boolean noMore) {
        refreshview.stopRefresh(true);

        if (isFirst) {
            messages.clear();

            if (mGoodsDeatilEntity != null) {
                buildLinkMessage(mGoodsDeatilEntity);
            }
        }

        if (!isEmpty(msgInfoList)) {
            mAdapter.addMsgInfos(0, msgInfoList);
        }
        if (noMore) {
            refreshview.setCanRefresh(false);
        } else {
            refreshview.setCanRefresh(true);
        }

        lastMessageSendTime = lasetSendTime;

        if (isFirst) {
            recycler_chat.scrollToPosition(mAdapter.getItemCount() - 1);//刷新到底部
        }
        isFirst = false;
    }

    //压缩图片
    public void compressImgs(String imgPath, String tagId, ImageMessage imageMessage) {
        Luban.with(this).load(imgPath).putGear(3).setCompressListener(new OnCompressListener() {

            @Override
            public void onStart() {
                LogUtil.httpLogW("onStart()");
            }

            @Override
            public void onSuccess(File file) {
                ImageEntity imageEntity = new ImageEntity(imgPath);
                imageEntity.file = file;
                mPresenter.uploadPic(imageEntity, "chat", tagId, imageMessage);//上传图片
            }

            @Override
            public void onError(Throwable e) {
                Common.staticToast("上传图片失败");
            }
        }).launch();
    }

    public void upDataChatMemberInfo(BaseMessage baseMessage) {
        if (baseMessage.to_user_id.equals(currentUserId)) { //发给自己的消息
            currentChatMember.m_user_id = baseMessage.from_user_id;
            currentChatMember.nickname = baseMessage.from_nickname;
            currentChatMember.headurl = baseMessage.from_headurl;
            currentSid = baseMessage.sid;
        }
    }

    @Override
    public void OnOrderSelect(MyOrderEntity.Orders orders) {
        //发送订单消息
        sendOrderMessage(orders);
    }


    public int getSendType(String fromUserId) {
        if (isEmpty(fromUserId)) {
            return BaseMessage.VALUE_SYSTEM;
        }

        if (fromUserId.equals(currentUserId)) {
            return BaseMessage.VALUE_RIGHT;
        }

        return BaseMessage.VALUE_LEFT;
    }


    @Override
    public void initMessage() {
        mCurrentUser = mWebsocketClient.getUser();
        initUser(mCurrentUser);
    }

    @Override
    public void receiveMessage(String message) {
        LogUtil.httpLogW("receiveMessage111");
        try {
            MessageEntity messageEntity = mObjectMapper.readValue(message, MessageEntity.class);
            MsgInfo msgInfo = messageEntity.msg_info;
            String message1 = msgInfo.message;
            BaseMessage baseMessage = objectMapper.readValue(message1, BaseMessage.class);
            upDataChatMemberInfo(baseMessage);//更新聊天对象的信息

            if (getSendType(baseMessage.from_user_id) == BaseMessage.VALUE_LEFT) {
                if (msgInfo.m_user_id.equals(chat_m_user_Id) || baseMessage.from_user_id.equals(chat_m_user_Id)) {
                    mAdapter.addMsgInfo(msgInfo);
                    readedMsg(chat_m_user_Id);
                    currentSid = msgInfo.sid;
                }
            } else if (getSendType(baseMessage.from_user_id) == BaseMessage.VALUE_RIGHT) {
                if (baseMessage.from_user_id.equals(currentUserId)) {
                    if (baseMessage.msg_type.equals("evaluate") && mWebsocketClient.getMemberStatus() != MemberStatus.Member) {//当前身份是客服 邀请评价成功
                        finish();
                    } else {
                        //tag_id不为空且deviceId相同 是当前手机发送的消息 不同则是其他端发送的消息
                        if (!isEmpty(splitDeviceId(baseMessage.tag_id)) && currentDeviceId.equals(splitDeviceId(baseMessage.tag_id))) {
                            mAdapter.itemSendComplete(baseMessage.tag_id, MessageStatus.SendSucc);
                            baseMessage.setStatus(MessageStatus.SendSucc);
                            msgInfo.message = mAdapter.msg2Str(baseMessage);
                        } else {
                            if (msgInfo.m_user_id.equals(chat_m_user_Id)) { //判断服务Id是否一致
                                mAdapter.addMsgInfo(msgInfo);
                            }
                        }
                    }
                }
            } else if (getSendType(baseMessage.from_user_id) == BaseMessage.VALUE_SYSTEM) {
                mAdapter.addMsgInfo(msgInfo);
                readedMsg(chat_m_user_Id);
            }
            runOnUiThread(() -> {
                recycler_chat.scrollToPosition(mAdapter.getItemCount() - 1);//刷新到底部
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void evaluateMessage(String msg) {
        try {
            EvaluateEntity entity = mObjectMapper.readValue(msg, EvaluateEntity.class);
            mAdapter.updateEvaluateStatus(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void roleSwitchMessage(String message) {

    }

    @Override
    public void transferMessage(String msg) {

    }

    @Override
    public void transferMemberAdd(String msg) {

    }

    @Override
    public void onLine() {

    }

    @Override
    public void logout() {

    }

    @Override
    protected void onDestroy() {
        mWebsocketClient.removeOnMessageReceiveListener(this);
//        暂时不能释放，会奔溃
//        if (mAdapter!=null)
//            mAdapter.mRelease();
//        if (et_input!=null)
//            et_input.mRelease();
        super.onDestroy();
    }
}
