package com.shunlian.app.newchat.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.R;
import com.shunlian.app.newchat.adapter.ChatAdpater;
import com.shunlian.app.newchat.entity.BaseEntity;
import com.shunlian.app.newchat.entity.BaseMessage;
import com.shunlian.app.newchat.entity.HistoryEntity;
import com.shunlian.app.newchat.entity.ImageMessage;
import com.shunlian.app.newchat.entity.ImgEntity;
import com.shunlian.app.newchat.entity.LinkMessage;
import com.shunlian.app.newchat.entity.MessageEntity;
import com.shunlian.app.newchat.entity.MsgInfo;
import com.shunlian.app.newchat.entity.MsgInfoDao;
import com.shunlian.app.newchat.entity.TextMessage;
import com.shunlian.app.newchat.entity.UserInfoEntity;
import com.shunlian.app.newchat.event.BaseEvent;
import com.shunlian.app.newchat.event.FramedataEvent;
import com.shunlian.app.newchat.event.MessageRespEvent;
import com.shunlian.app.newchat.greendao.AbstractDatabaseManager;
import com.shunlian.app.newchat.greendao.MsgInfoManager;
import com.shunlian.app.newchat.websocket.EasyWebsocketClient;
import com.shunlian.app.newchat.websocket.MessageStatus;
import com.shunlian.app.newchat.websocket.Status;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.store.StoreAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.widget.MyImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.shunlian.app.newchat.greendao.AbstractDatabaseManager.LIMIT_COUNT;

/**
 * 示例界面
 *
 * @author lucher
 */
public class ChatActivity extends BaseActivity implements ChatView {

//    public static final int OPEN_ALBUM = 1;
//    public static final int OPEN_CAMERA = 2;
//    private Uri tempTakeUri;
//
//    //websocket客户端
//    private EasyWebsocketClient mClient;
//    private String from_id, from_type, from_nickname, from_headurl;
////    private PullToRefreshListView refreshListView;
//    private MyImageView iv_back;
//    private ListView lv_chat;
//    private Handler mHandler;
//    private ChatInput et_input;
//    private List<MsgInfo> messages = new ArrayList<>();
//    private TextView tv_title, tv_store;
//    private UserInfoEntity.Info.Friend chatMember;
////    private MyHttpUtil.HttpCallback uploadCallBack, getHistoryCallBack;
//    private String currentTagId;
//    private MsgInfoManager infoManager;
//    private ObjectMapper mObjectMapper;
//    private ChatAdpater chatAdpater;
//    private String currentUserId;  //用户ID
//    private String chatUserId; //聊天对象ID
//    private String chatRoleType;
//    private String chatName;
//    private UserInfoEntity userInfoEntity;
////    private GoodsItemEntity.Data.Item goodsItem;
////    private ShopHomeEntity.Data.ShopInfo shopInfo;
////    private OrderItemEntity.Data orderItem;
//    private String currentDeviceId;
//    private boolean showStore;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_chat);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);//SOFT_INPUT_ADJUST_NOTHING
//
//        currentDeviceId = DeviceInfoUtil.getDeviceId(this);
//
//        chatMember = (UserInfoEntity.Info.Friend) getIntent().getSerializableExtra("member");
//        goodsItem = getIntent().getParcelableExtra("goodsItem");
//        orderItem = (OrderItemEntity.Data) getIntent().getSerializableExtra("orderItem");
//        shopInfo = (ShopHomeEntity.Data.ShopInfo) getIntent().getSerializableExtra("shopItem");
//        chatName = getIntent().getStringExtra("chatName");
//        showStore = getIntent().getBooleanExtra("showStore", true);
//        init();
//        initPrf();
//        initChat();
//    }
//
    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initData() {

    }
//
//    /**
//     * 初始化
//     */
//    private void init() {
//        EventBus.getDefault().register(this);
//        tv_title = (TextView) findViewById(R.id.tv_title);
//        iv_back = (MyImageView) findViewById(R.id.iv_back);
//        tv_store = (TextView) findViewById(R.id.tv_store);
//        et_input = (ChatInput) findViewById(R.id.input_panel);
//
//        if (chatMember != null) {
//            chatName = chatMember.nickname;
//        }
//        if (!TextUtils.isEmpty(chatName)) {
//            tv_title.setText(chatName);
//        }
//
//        et_input.setChatView(this);
//
//        iv_back.setOnClickListener(l);
//        tv_store.setOnClickListener(l);
//    }
//
//    public void initPrf() {
//        mHandler = new Handler();
//        refreshListView = (PullToRefreshListView) findViewById(R.id.lv_chat);
//        lv_chat = refreshListView.getRefreshableView();
//
//        lv_chat.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
//        lv_chat.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        et_input.setInputMode(ChatInput.InputMode.NONE);
//                        break;
//                }
//                return false;
//            }
//        });
//
//        refreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
//            @Override
//            public void onRefresh(final PullToRefreshBase<ListView> refreshView) {
//                //判断网络是否连接  连接使用网络，否则获取数据库
//                if (isNetworkOpen(ChatActivity.this)) {
//                    getHistoryFromNetwork();
//                } else {
//                    final int beforeCount = messages.size();
//                    List list = getDataFromSQL(false);
//                    chatAdpater.addMsgInfos(0, list);
//                    final int afterCount = messages.size();
//                    chatAdpater.notifyDataSetChanged();
//                    lv_chat.setSelection(afterCount - beforeCount);
//                }
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        refreshView.onRefreshComplete();
//                    }
//                }, 300);
//            }
//        });
//
//        chatAdpater = new ChatAdpater(this, messages);
//        chatAdpater.setOnLinkClickListener(new ChatAdpater.OnLinkClickListener() {
//            @Override
//            public void OnClick() {
//                LogUtil.httpLogW("发送消息");
//                if (goodsItem != null) {
//                    sendLinkMessage(goodsItem);
//                }
//            }
//        });
//        lv_chat.setAdapter(chatAdpater);
//    }
//
//
//    private void initChat() {
//        mObjectMapper = new ObjectMapper();
//
//        if (chatMember != null) {
//            chatUserId = chatMember.uid;
//            chatRoleType = chatMember.type;
//        } else {
//            chatUserId = getIntent().getStringExtra("user_id");
//            chatRoleType = getIntent().getStringExtra("role_type");
//        }
//
//        if (!chatUserId.isEmpty() && !chatUserId.equals("224")) { //224为官方客服
//            if(showStore){
//                tv_store.setVisibility(View.VISIBLE);
//            }
//        }
//
//
//        if (EasyWebsocketClient.getClient() == null) {
//            mClient = EasyWebsocketClient.initWebsocketClient(this);
//        } else {
//            mClient = EasyWebsocketClient.getClient();
//            if (mClient.getUserInfoEntity() == null) {
//                Common.staticToast("初始化聊天失败");
//                return;
//            } else {
//                mClient.initChat(chatUserId);
//                userInfoEntity = mClient.getUserInfoEntity();
//                initUserInfo(userInfoEntity);
//            }
//        }
//
//        //初始化数据库
//        AbstractDatabaseManager.initOpenHelper(getApplicationContext(), "chat_" + currentUserId + "_" + chatUserId);
//        infoManager = new MsgInfoManager();
//
//        //判断网络是否连接  连接使用网络，否则获取数据库
//        if (isNetworkOpen(this)) {
//            getHistoryFromNetwork();
//        } else {
//            chatAdpater.addMsgInfos(0, getDataFromSQL(true));
//        }
//    }
//
//    @Override
//    protected void onRestart() {
//        if (!TextUtils.isEmpty(chatUserId) && !TextUtils.isEmpty(chatRoleType)) {
//            readedMsg(chatUserId, chatRoleType);
//        }
//        super.onRestart();
//    }
//
//    private View.OnClickListener l = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            switch (view.getId()) {
//                case R.id.iv_back:
//                    finish();
//                    break;
//                case R.id.tv_store:
//                    if (chatUserId.isEmpty() || chatUserId.equals("224")) { //224为官方客服
//                        return;
//                    }
//                    Intent intentShop = new Intent(ChatActivity.this, StoreAct.class);
//                    intentShop.putExtra("shopId", chatUserId);
//                    startActivity(intentShop);
//                    break;
//            }
//        }
//    };
//
//    private void initUserInfo(UserInfoEntity userInfoEntity) {
//        currentUserId = userInfoEntity.info.user.user_id;
//        from_headurl = userInfoEntity.info.user.headurl;
//        from_id = userInfoEntity.info.user.member_id;
//        from_nickname = userInfoEntity.info.user.nickname;
//        from_type = userInfoEntity.info.user.type;
//    }
//
    @Override
    public void sendImage() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        try {
//            startActivityForResult(i, OPEN_ALBUM);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(ChatActivity.this, "启动相册异常~", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void sendPhoto() {
//        String photoPath = BaseApplication.CACHE_PATH + "/temp_take_image_" + System.currentTimeMillis() + "_" + Math.random() + ".png";
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        tempTakeUri = Uri.fromFile(new File(photoPath));
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempTakeUri);
//        startActivityForResult(intent, OPEN_CAMERA);
    }

    @Override
    public void sendText() {
//        sendTextMessage(et_input.getText().toString());
//        et_input.setText("");
//
//        if (et_input.isBottomPanelVisible()) {
//            et_input.setBottomPanelInVisible();
//        }
    }
//
//
//    /**
//     * 数据库中取消息列表
//     *
//     * @return
//     */
//    public List<MsgInfo> getDataFromSQL(boolean isFirst) {
//        QueryBuilder builder = infoManager.getQueryBuilder();
//        WhereCondition whereCondition;
//        if (isFirst) {
//            whereCondition = new WhereCondition.StringCondition("1=1");
//        } else {
//            whereCondition = new WhereCondition.StringCondition("MSG_ID<" + getLastMessageId());
//        }
//        WhereCondition whereC = new WhereCondition.StringCondition("MSG_ID");
//        builder.where(whereCondition, whereC).orderDesc(MsgInfoDao.Properties.Msg_id).limit(LIMIT_COUNT);
//        return builder.list();
//    }
//
//    /**
//     * 网络获取历史消息
//     */
//
//    public void getHistoryFromNetwork() {
////        getHistoryCallBack = new MyHttpUtil.HttpCallback() {
////            @Override
////            public void getHistory(Context context, HistoryEntity historyEntity) {
////                if (historyEntity != null && historyEntity.getData().getMessage_list() != null) {
////                    final int beforeCount = messages.size();
////                    chatAdpater.addMsgInfos(0, historyEntity.getData().getMessage_list());
////                    final int afterCount = messages.size();
////                    chatAdpater.notifyDataSetChanged();
////                    lv_chat.setSelection(afterCount - beforeCount);
////                }
////                super.getHistory(context, historyEntity);
////            }
////        };
////        if ("sys_link".equals(getLastMessageId())) {
////            return;
////        }
////        MyHttpUtil.getHistoryMessage(this, getLastMessageId(), chatUserId, true, getHistoryCallBack);
//    }
//
//    private String getLastMessageId() {
//        String currentMsgId;
//        MsgInfo info;
//        if (messages.size() == 0) {
//            currentMsgId = null;
//        } else {
//            info = messages.get(0);//从消息列表的第0项获取历史消息
//            String message = info.getMessage();
//            BaseMessage baseMessage = chatAdpater.str2Msg(message);
//            if ("sys_link".equals(baseMessage.msg_type)) {
//                return "sys_link";
//            }
//            if (info.getId() == null) { //
//                currentMsgId = String.valueOf(messages.get(1).getId());
//            } else {
//                currentMsgId = String.valueOf(messages.get(0).getId());
//            }
//        }
//        return currentMsgId;
//    }
//
//    /**
//     * 发送文字消息
//     */
//    private void sendTextMessage(String msg) {
//        MsgInfo msgInfo = new MsgInfo();
//
//        currentTagId = creatMsgTagId(from_id);
//        TextMessage textMessage = new TextMessage();
//        textMessage.from_user_id = currentUserId;
//        textMessage.from_id = from_id;
//        textMessage.from_type = from_type;
//        textMessage.from_nickname = from_nickname;
//        textMessage.from_headurl = from_headurl;
//        textMessage.to_id = chatUserId;
//        textMessage.msg_type = "text";
//        textMessage.setSendType(BaseMessage.VALUE_RIGHT);
//        textMessage.tag_id = currentTagId;
//        textMessage.type = "send_message";
//        textMessage.msg_body = msg;
//        if (mClient.getStatus() == Status.CONNECTED) {
//            mClient.send(chatAdpater.msg2Str(textMessage));
//            msgInfo.setSend_time(System.currentTimeMillis() / 1000);
//            textMessage.setStatus(MessageStatus.Sending);
//            msgInfo.setMessage(chatAdpater.msg2Str(textMessage));
//            chatAdpater.addMsgInfo(msgInfo);
//        }
//    }
//
//
//    /**
//     * 新建图片消息
//     */
//
//    public void buildImageMessage(String imgPath) {
//        MsgInfo msgInfo = new MsgInfo();
//
//        currentTagId = creatMsgTagId(from_id);
//        ImageMessage imageMessage = new ImageMessage();
//        imageMessage.from_user_id = currentUserId;
//        imageMessage.from_id = from_id;
//        imageMessage.from_type = from_type;
//        imageMessage.from_nickname = from_nickname;
//        imageMessage.from_headurl = from_headurl;
//        imageMessage.to_id = chatUserId;
//        imageMessage.msg_type = "image";
//        imageMessage.setSendType(BaseMessage.VALUE_RIGHT);
//        imageMessage.tag_id = currentTagId;
//        imageMessage.type = "send_message";
//
//        ImageMessage.ImageBody imageBody = new ImageMessage.ImageBody();
//        Bitmap bitmap = null;
//        try {
//            imageBody.localUrl = imgPath;
//            bitmap = BitmapFactory.decodeFile(imgPath);
//            imageBody.img_height = bitmap.getHeight();
//            imageBody.img_width = bitmap.getWidth();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (bitmap != null) {
//                bitmap.recycle();
//            }
//        }
//        imageMessage.msg_body = imageBody;
//        msgInfo.setSend_time(System.currentTimeMillis() / 1000);
//        msgInfo.setMessage(chatAdpater.msg2Str(imageMessage));
//
//        if (mClient.getStatus() == Status.CONNECTED) {
//            chatAdpater.addMsgInfo(msgInfo);
//            chatAdpater.notifyDataSetChanged();
//            lv_chat.setSelection(ListView.FOCUS_DOWN);//刷新到底部
//            chatAdpater.itemSendComplete(currentTagId, MessageStatus.Sending);
//            upLoadImg(imgPath, currentTagId, imageMessage);
//        }
//    }
//
//    /**
//     * 发送图片消息
//     */
//    private void sendImgMessage(ImageMessage imageMessage) {
//        mClient.send(chatAdpater.msg2Str(imageMessage));
//    }
//
//    /**
//     * 构建一条链接系统消息
//     */
//    public void buildLinkMessage(GoodsItemEntity.Data.Item goodsItem) {
//        MsgInfo msgInfo = new MsgInfo();
//
//        LinkMessage linkMessage = new LinkMessage();
//        linkMessage.msg_type = "sys_link";
//        linkMessage.setSendType(BaseMessage.VALUE_SYSTEM);
//
//        LinkMessage.LinkBody linkBody = new LinkMessage.LinkBody();
//        linkBody.goodsImage = goodsItem.getThumb();
//        linkBody.title = goodsItem.getTitle();
//        linkBody.price = goodsItem.getMarketprice();
//        linkBody.goodsId = goodsItem.getGoodsId();
//        linkMessage.msg_body = linkBody;
//        msgInfo.setMessage(chatAdpater.msg2Str(linkMessage));
//
//        chatAdpater.addMsgInfo(msgInfo);
//        chatAdpater.notifyDataSetChanged();
//        lv_chat.setSelection(ListView.FOCUS_DOWN);//刷新到底部
//    }
//
//    /**
//     * 发送链接消息
//     */
//    public void sendLinkMessage(GoodsItemEntity.Data.Item goodsItem) {
//        MsgInfo msgInfo = new MsgInfo();
//
//        currentTagId = creatMsgTagId(from_id);
//        LinkMessage linkMessage = new LinkMessage();
//        linkMessage.from_user_id = currentUserId;
//        linkMessage.from_id = from_id;
//        linkMessage.from_type = from_type;
//        linkMessage.from_nickname = from_nickname;
//        linkMessage.from_headurl = from_headurl;
//        linkMessage.to_id = chatUserId;
//        linkMessage.msg_type = "link";
//        linkMessage.setSendType(BaseMessage.VALUE_RIGHT);
//        linkMessage.tag_id = currentTagId;
//        linkMessage.type = "send_message";
//
//        LinkMessage.LinkBody linkBody = new LinkMessage.LinkBody();
//        linkBody.goodsImage = goodsItem.getThumb();
//        linkBody.title = goodsItem.getTitle();
//        linkBody.price = goodsItem.getMarketprice();
//        linkBody.goodsId = goodsItem.getGoodsId();
//        linkMessage.msg_body = linkBody;
//        String body;
//        try {
//            body = mObjectMapper.writeValueAsString(linkMessage);
//            mClient.send(body);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        msgInfo.setSend_time(System.currentTimeMillis() / 1000);
//        linkMessage.setStatus(MessageStatus.Sending);
//        msgInfo.setMessage(chatAdpater.msg2Str(linkMessage));
//
//        if (mClient.getStatus() == Status.CONNECTED) {
//            chatAdpater.addMsgInfo(msgInfo);
//            chatAdpater.notifyDataSetChanged();
//            lv_chat.setSelection(ListView.FOCUS_DOWN);//刷新到底部
//        }
//    }
//
//    /**
//     * 消息已读上报
//     *
//     * @param userId
//     * @param memberType
//     */
//
//    private void readedMsg(String userId, String memberType) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("type", "msg_read");
//            jsonObject.put("member_id", userId);
//            jsonObject.put("member_type", memberType);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (mClient.getStatus() == Status.CONNECTED) {
//            mClient.send(jsonObject.toString());
//            mClient.updateFriendList(chatUserId);
//        }
//    }
//
//    /**
//     * 根据用户ID和时间戳生成唯一一个tagId标识来判断消息发送成功
//     */
//    public String creatMsgTagId(String useId) {
//        if (TextUtils.isEmpty(useId)) {
//            return null;
//        }
//        return String.valueOf(currentDeviceId + "_" + System.currentTimeMillis()) + useId;
//    }
//
//    /**
//     * 处理websocket响应事件
//     *
//     * @param event
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void handleEvent(BaseEvent event) {
//        Log.w("xiaojian", "state:" + event.getType());
//        switch (event.getType()) {
//            case CONNECTING:
//                break;
//            case OPEN:
//                break;
//            case CLOSE:
//                break;
//            case ERROR:
//                break;
//            case TIMEOUT:
//                break;
//            case MESSAGE:
//                String msg = ((MessageRespEvent) event).getMessage();
//                try {
//                    BaseEntity baseEntity = new ObjectMapper().readValue(msg, BaseEntity.class);
//                    switch (baseEntity.message_type) {
//                        case "init":
//                            mClient.initChat(chatUserId);
//                            userInfoEntity = mClient.getUserInfoEntity();
//                            initUserInfo(userInfoEntity);
//                            break;
//                        case "online":
//                            LogUtil.httpLogW("online：" + msg);
//                            break;
//                        case "logout":
//                            LogUtil.httpLogW("logout：" + msg);
//                            break;
//                        case "dialog_init":
//                            LogUtil.httpLogW("dialog_init：" + msg);
//                            MessageEntity msgEntity = new ObjectMapper().readValue(msg, MessageEntity.class);
////                            Common.staticToast(ChatActivity.this, msgEntity.msg);
//
//                            mClient.setChating(true);
//                            if (goodsItem != null) {
//                                buildLinkMessage(goodsItem);
//                            }
//                            readedMsg(chatUserId, chatRoleType);
//                            break;
//                        case "friend_add":
//                            break;
//                        case "receive_message":
//                            LogUtil.httpLogW("msg:" + msg);
//                            //接收到消息
//                            MessageEntity messageEntity = mObjectMapper.readValue(msg, MessageEntity.class);
//                            MsgInfo msgInfo = messageEntity.msg_info;
//                            // TODO: 2017/9/23
//                            String message1 = msgInfo.getMessage();
//                            LogUtil.httpLogW("message1========" + message1);
//                            BaseMessage baseMessage = mObjectMapper.readValue(message1, BaseMessage.class);
//                            // TODO: 2017/9/23
//                            int sendType = baseMessage.getSendType();
//                            if (sendType == BaseMessage.VALUE_LEFT) {
//                                if (baseMessage.from_id.equals(chatUserId)) {
//                                    chatAdpater.addMsgInfo(msgInfo);
//                                    LogUtil.httpLogW("插入一条消息:" + infoManager.insert(msgInfo));
//                                }
//                            } else if (sendType == BaseMessage.VALUE_RIGHT) {
//                                if (baseMessage.from_user_id.equals(currentUserId)) {
//                                    //tag_id不为空且deviceId相同 是当前手机发送的消息 不同则是其他端发送的消息
//                                    if (!TextUtils.isEmpty(splitDeviceId(baseMessage.tag_id)) && currentDeviceId.equals(splitDeviceId(baseMessage.tag_id))) {
//                                        chatAdpater.itemSendComplete(baseMessage.tag_id, MessageStatus.SendSucc);
//                                        baseMessage.setStatus(MessageStatus.SendSucc);
//                                        msgInfo.setMessage(chatAdpater.msg2Str(baseMessage));
//                                    } else {
//                                        chatAdpater.addMsgInfo(msgInfo);
//                                    }
//                                    LogUtil.httpLogW("插入一条消息:" + infoManager.insert(msgInfo));
//                                }
//                            } else if (sendType == BaseMessage.VALUE_SYSTEM) {
//                                chatAdpater.addMsgInfo(msgInfo);
//                            }
//                            lv_chat.setSelection(ListView.FOCUS_DOWN);//刷新到底部
//                            break;
//                        case "user_ping":
//                            break;
//                        case "leaveline":
//                            break;
//                        case "freeline":
//                            break;
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//
//            case FRAGMENT:
//                String data = new String(((FramedataEvent) event).getFrameData().getPayloadData().array());
//                break;
//            default:
//                break;
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        String filePath;
//        if (resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case OPEN_ALBUM:
//                    filePath = getFileFromMediaUri(this, data.getData());
//                    if (!TextUtils.isEmpty(filePath)) {
//                        buildImageMessage(filePath);
//                    }
//                    break;
//                case OPEN_CAMERA:
//                    filePath = getFileFromMediaUri(this, tempTakeUri);
//                    if (!TextUtils.isEmpty(filePath)) {
//                        buildImageMessage(filePath);
//                    }
//                    break;
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    /**
//     * @param filePath 上传图片
//     */
//    private void upLoadImg(String filePath, final String tagId, final ImageMessage imageMessage) {
//        if (!TextUtils.isEmpty(filePath)) {
//            uploadCallBack = new MyHttpUtil.HttpCallback() {
//                @Override
//                public void uploadImg(Context context, ImgEntity imgEntity) {
//                    if (imgEntity != null && imgEntity.getData() != null) {
//                        ImageMessage.ImageBody imageBody = new ImageMessage.ImageBody();
//                        imageBody.img_height = imgEntity.getData().getImg_height();
//                        imageBody.img_width = imgEntity.getData().getImg_width();
//                        imageBody.img_host = imgEntity.getData().getImg_host();
//                        imageBody.img_small = imgEntity.getData().getImg_small();
//                        imageBody.img_original = imgEntity.getData().getImg_original();
//                        imageMessage.msg_body = imageBody;
//                        sendImgMessage(imageMessage);
//                    } else {
//                        chatAdpater.itemSendComplete(tagId, MessageStatus.SendFail);
//                    }
//                    super.uploadImg(context, imgEntity);
//                }
//
//                @Override
//                public void onFailure(Context context, int statusCode, Header[] headers, Throwable throwable, Object bj) {
//                    chatAdpater.itemSendComplete(tagId, MessageStatus.SendFail);
//                    super.onFailure(context, statusCode, headers, throwable, bj);
//                }
//            };
//            MyHttpUtil.uploadImg(this, uploadCallBack, new File(filePath));
//        }
//    }
//
//    private String splitDeviceId(String tagId) {
//        String deviceId = null;
//        String[] data;
//        if (TextUtils.isEmpty(tagId)) {
//            return null;
//        }
//        try {
//            data = tagId.split("_");
//            deviceId = data[0];
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return deviceId;
//    }
//
//    @Override
//    protected void onPause() {
//        //用户离开聊天页面
//        if (mClient != null && mClient.getStatus() == Status.CONNECTED) {
//            mClient.setChating(false);
//        }
//        super.onPause();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        DIOU.releaseBitmap();
//        if (DIOU.imageLoader != null) {
//            DIOU.imageLoader.resume();
//        }
//        EventBus.getDefault().unregister(this);
//    }
//
//
//    public static Intent buildIntent(Context context, String chatId, String chatName, String roleType,
//                                     GoodsItemEntity.Data.Item goodsItem, ShopHomeEntity.Data.ShopInfo shopInfo, OrderItemEntity.Data orderItem) {
//        Intent intent = new Intent(context, ChatActivity.class);
//        intent.putExtra("user_id", chatId);
//        intent.putExtra("goodsItem", goodsItem);
//        intent.putExtra("chatName", chatName);
//        intent.putExtra("shopItem", shopInfo);
//        intent.putExtra("orderItem", orderItem);
//        intent.putExtra("role_type", roleType);
//        return intent;
//    }
//
//    public static Intent buildIntent(Context context, String chatId, String chatName, String roleType, boolean showStore) {
//        Intent intent = new Intent(context, ChatActivity.class);
//        intent.putExtra("user_id", chatId);
//        intent.putExtra("chatName", chatName);
//        intent.putExtra("role_type", roleType);
//        intent.putExtra("showStore", showStore);
//        return intent;
//    }
}
