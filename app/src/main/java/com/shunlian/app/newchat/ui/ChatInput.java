package com.shunlian.app.newchat.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.newchat.adapter.EmojisVPAdapter;
import com.shunlian.app.newchat.adapter.FastAdapter;
import com.shunlian.app.newchat.adapter.PanelAdapter;
import com.shunlian.app.newchat.entity.ReplysetEntity;
import com.shunlian.app.presenter.ChatInputPresenter;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IChatInputView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 聊天界面输入控件
 */
public class ChatInput extends RelativeLayout implements TextWatcher, View.OnClickListener, EmojisVPAdapter.OnEmojiClickListenter, IChatInputView {

    @BindView(R.id.btn_add)
    ImageButton btnAdd;

    @BindView(R.id.btnEmoticon)
    ImageButton btnEmotion;

    @BindView(R.id.btn_send)
    Button btnSend;

    @BindView(R.id.input)
    EditText editText;

    @BindView(R.id.emoji_vp)
    ViewPager emoji_vp;

    @BindView(R.id.rlayout_emoji)
    RelativeLayout rlayout_emoji;

    @BindView(R.id.llayout_dot)
    LinearLayout llayout_dot;

    @BindView(R.id.scroll_dot)
    View scroll_dot;

    @BindView(R.id.recyclerView_panel)
    RecyclerView recyclerView_panel;

    @BindView(R.id.recyclerView_fast)
    RecyclerView recyclerView_fast;

    private boolean isSendVisible, isEmoticonReady;
    private InputMode inputMode = InputMode.TEXT;
    private ChatView chatView;
    private int spaceWidthDot;//点之间的间距
    private int startLeftPosi;//最左侧点的距离
    private int inputMethodHeight = -1;
    private final AssetManager am;
    private PanelAdapter mAdapter;
    private FastAdapter fastAdapter;
    private ChatInputPresenter mPresenter;
    private List<String> stringList;
    private List<ReplysetEntity.Replyset> replysetList;
    private String currentStatus, currentJoinId;

    public ChatInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.chat_input, this);
        ButterKnife.bind(this, view);
        initView();
        am = context.getAssets();
    }

    public void mRelease() {
        if (am != null)
            am.close();
    }

    private void initView() {
        btnAdd.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        btnEmotion.setOnClickListener(this);
        setSendBtn();
        editText.addTextChangedListener(this);
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                updateView(InputMode.TEXT, false);
            }
        });
        editText.setOnTouchListener((v, ev) -> {
            if (inputMethodHeight == -1)
                getInputMethodHeight();
            return false;
        });
        isSendVisible = editText.getText().length() != 0;
        initVPListener();

        replysetList = new ArrayList<>();
        stringList = new ArrayList<>();
        stringList.add("拍照");
        stringList.add("相册");

        mAdapter = new PanelAdapter(getContext(), stringList);
        recyclerView_panel.setLayoutManager(new GridLayoutManager(getContext(), 4));
        recyclerView_panel.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((view, position) -> {
            String str = stringList.get(position);
            Activity activity = (Activity) getContext();
            switch (str) {
                case "相册":
                    if (activity != null && requestStorage(activity)) {
                        chatView.sendImage();
                    }
                    break;
                case "拍照":
                    if (activity != null && requestStorage(activity)) {
                        chatView.sendPhoto();
                    }
                    break;
                case "商品":
                    chatView.sendGoods();
                    break;
                case "邀请评价":
                    chatView.sendComment();
                    break;
                case "订单":
                    chatView.sendOrder();
                    break;
                case "快捷回复":
                    updateView(InputMode.FAST, false);

                    if (replysetList.size() == 0) {
                        if (mPresenter != null) {
                            mPresenter.getReplyList(currentJoinId, currentStatus);
                        }
                    }
                    break;
            }
        });

        mPresenter = new ChatInputPresenter(getContext(), this);
    }

    /**
     * 设置用户的身份类型
     *
     * @param roleType
     */
    public void setRoleType(String roleType) {
        switch (roleType) {
            case "1":
            case "2":
                currentStatus = "1";
                break;
            case "3":
            case "4":
                currentStatus = "2";
                break;
        }
        LogUtil.httpLogW("currentStatus:" + currentStatus);
    }

    /**
     * 设置用户的JoinId
     */

    public void setJoinId(String joinId) {
        currentJoinId = joinId;
    }

    private void getInputMethodHeight() {
        postDelayed(() -> {
            int height = getRootView().getHeight();
            Rect rect = new Rect();
            getWindowVisibleDisplayFrame(rect);
            inputMethodHeight = height - rect.bottom;
            LinearLayout.LayoutParams rlayout_emoji_Params = new LinearLayout.LayoutParams(getRootView().getWidth(), inputMethodHeight);
            rlayout_emoji.setLayoutParams(rlayout_emoji_Params);
            recyclerView_panel.setLayoutParams(rlayout_emoji_Params);

            RelativeLayout.LayoutParams emoji_vp_Params = new RelativeLayout.LayoutParams(getRootView().getWidth(), (int) (inputMethodHeight * 0.8f));
            emoji_vp.setLayoutParams(emoji_vp_Params);
        }, 300);
    }

    /**
     * 初始化vp监听
     */
    private void initVPListener() {
        emoji_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                int v = (int) (spaceWidthDot * (positionOffset + position));
                RelativeLayout.LayoutParams layoutParams = (LayoutParams) scroll_dot.getLayoutParams();
                layoutParams.leftMargin = startLeftPosi + v;
                scroll_dot.setLayoutParams(layoutParams);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void updateView(InputMode mode, boolean isAdd) {
        if (mode == inputMode) return;
        leavingCurrentState(isAdd);
        switch (inputMode = mode) {
            case MORE:
                new Handler().postDelayed(() -> recyclerView_panel.setVisibility(VISIBLE), 300);
                break;
            case TEXT:
                if (editText.requestFocus()) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                    btnEmotion.setBackgroundResource(R.mipmap.icon_chat_smiley_n);
                }
                break;
            case EMOTICON:
                new Handler().postDelayed(() -> {
                    if (!isEmoticonReady) {
                        prepareEmoticon();
                    }
                    btnEmotion.setBackgroundResource(R.mipmap.icon_chat_smiley_h);
                    rlayout_emoji.setVisibility(VISIBLE);
                }, 100);
                break;
            case FAST:
                new Handler().postDelayed(() -> recyclerView_fast.setVisibility(VISIBLE), 300);
                break;
        }

    }

    private void leavingCurrentState(boolean isAdd) {
        switch (inputMode) {
            case TEXT:
                View view = ((Activity) getContext()).getCurrentFocus();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                editText.clearFocus();
                if (isAdd) {
                    btnAdd.setBackgroundResource(R.mipmap.icon_chat_sendmore_h);
                }
                break;
            case MORE:
                btnAdd.setBackgroundResource(R.mipmap.icon_chat_sendmore_n);
                recyclerView_panel.setVisibility(GONE);
                break;
            case EMOTICON:
                rlayout_emoji.setVisibility(GONE);
                break;
            case FAST:
                recyclerView_fast.setVisibility(GONE);
                break;
        }
    }


    /**
     * 关联聊天界面逻辑
     */
    public void setChatView(ChatView chatView) {
        this.chatView = chatView;
    }

    /**
     * This method is called to notify you that, within <code>s</code>,
     * the <code>count</code> characters beginning at <code>start</code>
     * are about to be replaced by new text with length <code>after</code>.
     * It is an error to attempt to make changes to <code>s</code> from
     * this callback.
     *
     * @param s
     * @param start
     * @param count
     * @param after
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    /**
     * This method is called to notify you that, within <code>s</code>,
     * the <code>count</code> characters beginning at <code>start</code>
     * have just replaced old text that had length <code>before</code>.
     * It is an error to attempt to make changes to <code>s</code> from
     * this callback.
     *
     * @param s
     * @param start
     * @param before
     * @param count
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        isSendVisible = s != null && s.length() > 0;
        setSendBtn();
        if (isSendVisible) {
//            chatView.sending();
        }
    }

    /**
     * This method is called to notify you that, somewhere within
     * <code>s</code>, the text has been changed.
     * It is legitimate to make further changes to <code>s</code> from
     * this callback, but be careful not to get yourself into an infinite
     * loop, because any changes you make will cause this method to be
     * called again recursively.
     * (You are not told where the change took place because other
     * afterTextChanged() methods may already have made other changes
     * and invalidated the offsets.  But if you need to know here,
     * you can use {@link Spannable#setSpan} in {@link #onTextChanged}
     * to mark your place and then look up from here where the span
     * ended up.
     *
     * @param s
     */
    @Override
    public void afterTextChanged(Editable s) {

    }

    private void setSendBtn() {
        if (isSendVisible) {
            btnAdd.setVisibility(GONE);
            btnSend.setVisibility(VISIBLE);
        } else {
            btnAdd.setVisibility(VISIBLE);
            btnSend.setVisibility(GONE);
        }
    }

    private void prepareEmoticon() {
        if (emoji_vp == null) return;

        EmojisVPAdapter emojisVPAdapter = new EmojisVPAdapter(getContext(), am);
        emoji_vp.setAdapter(emojisVPAdapter);
        emojisVPAdapter.setOnEmojiClickListener(this);

        llayout_dot.removeAllViews();
        int width = TransformUtil.dip2px(getContext(), 2);
        for (int i = 0; i < emojisVPAdapter.getCount(); i++) {
            View view_dot = new View(getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width * 2, width * 2);
            lp.leftMargin = width * 5;
            view_dot.setLayoutParams(lp);
            view_dot.setBackgroundResource(R.drawable.red_dot);
            llayout_dot.addView(view_dot);
            GradientDrawable gd = (GradientDrawable) view_dot.getBackground();
            gd.setColor(getResources().getColor(R.color.my_line_gray));
        }

        llayout_dot.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                llayout_dot.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                startLeftPosi = llayout_dot.getChildAt(0).getLeft();
                spaceWidthDot = llayout_dot.getChildAt(1).getLeft() - startLeftPosi;

                GradientDrawable gd = (GradientDrawable) scroll_dot.getBackground();
                gd.setColor(getResources().getColor(R.color.share_text));
                RelativeLayout.LayoutParams layoutParams = (LayoutParams) scroll_dot.getLayoutParams();
                layoutParams.leftMargin = startLeftPosi;
                scroll_dot.setLayoutParams(layoutParams);

                LogUtil.zhLogW("表情vp高===" + emoji_vp.getHeight());
            }
        });

        isEmoticonReady = true;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                chatView.sendText();
                break;
            case R.id.btn_add:
                updateView(inputMode == InputMode.MORE ? InputMode.TEXT : InputMode.MORE, true);
                break;
            case R.id.btnEmoticon:
                updateView(inputMode == InputMode.EMOTICON ? InputMode.TEXT : InputMode.EMOTICON, false);
                break;
        }
    }


    /**
     * 获取输入框文字
     */
    public Editable getText() {
        return editText.getText();
    }

    /**
     * 设置输入框文字
     */
    public void setText(String text) {
        editText.setText(text);
        editText.setSelection(text.length());
    }

    /**
     * 设置输入框文字
     */
    public void setSpannableText(SpannableString text) {
        String str = editText.getText().toString();
        editText.setText(str + text);
        editText.setSelection((str + text).length());
    }


    /**
     * 设置输入模式
     */
    public void setInputMode(InputMode mode, boolean isAdd) {
        updateView(mode, isAdd);
    }

    public Bitmap getEmojiBitmap(int picName) {
        InputStream is = null;
        Bitmap resizedBitmap = null;

        try {
            is = am.open(String.format("emojis/%d.png", picName));
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            is.close();
            Matrix matrix = new Matrix();
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            // 缩放图片的尺寸
            int i = TransformUtil.dip2px(getContext(), 28);
            float scaleWidth = (float) i / width;
            float scaleHeight = (float) i / height;
            matrix.postScale(scaleWidth, scaleHeight);
            resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resizedBitmap;
    }

    @Override
    public void OnEmojiClick(int emojiIndex, String emojiStr) {
        SpannableString str = new SpannableString(emojiStr);
        Bitmap emojiBitmap = getEmojiBitmap(emojiIndex);

        if (emojiBitmap != null) {
            ImageSpan span = new ImageSpan(getContext(), emojiBitmap);
            str.setSpan(span, 0, emojiStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            editText.setGravity(Gravity.CENTER_VERTICAL);
            editText.append(str);
        }
    }

    @Override
    public void OnEmojiDel() {
        //调用系统删除键事件
        int keyCode = KeyEvent.KEYCODE_DEL;
        KeyEvent keyEventDown = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
        KeyEvent keyEventUp = new KeyEvent(KeyEvent.ACTION_UP, keyCode);
        editText.onKeyDown(keyCode, keyEventDown);
        editText.onKeyUp(keyCode, keyEventUp);
    }

    @Override
    public void getReplysetList(List<ReplysetEntity.Replyset> list) {

        if (list != null && list.size() != 0) {
            replysetList.addAll(list);
        }

        if (fastAdapter == null) {
            fastAdapter = new FastAdapter(getContext(), replysetList);
            recyclerView_fast.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView_fast.setAdapter(fastAdapter);
            fastAdapter.setOnItemClickListener((view, position) -> {
                ReplysetEntity.Replyset replyset = replysetList.get(position);
                if (!TextUtils.isEmpty(replyset.item)) {
                    chatView.sendFast(replyset.item);
                }
            });
        } else {
            fastAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }


    public enum InputMode {
        TEXT,
        VOICE,
        EMOTICON,
        MORE,
        GOODS,
        COMMENT,
        ORDER,
        FAST,
        NONE,
    }

    /*private boolean requestVideo(Activity activity){
        if (afterM()){
            final List<String> permissionsList = new ArrayList<>();
            if ((activity.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) permissionsList.add(Manifest.permission.CAMERA);
            if ((activity.checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)) permissionsList.add(Manifest.permission.RECORD_AUDIO);
            if (permissionsList.size() != 0){
                activity.requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_PERMISSIONS);
                return false;
            }
            int hasPermission = activity.checkSelfPermission(Manifest.permission.CAMERA);
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{Manifest.permission.CAMERA},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return false;
            }
        }
        return true;
    }*/

    private boolean requestCamera(Activity activity) {
       /* if (afterM()){
            int hasPermission = activity.checkSelfPermission(Manifest.permission.CAMERA);
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{Manifest.permission.CAMERA},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return false;
            }
        }*/
        return true;
    }

   /* private boolean requestAudio(Activity activity){
        if (afterM()){
            int hasPermission = activity.checkSelfPermission(Manifest.permission.RECORD_AUDIO);
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return false;
            }
        }
        return true;
    }*/

    private boolean requestStorage(Activity activity) {
       /* if (afterM()){
            int hasPermission = activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return false;
            }
        }*/
        return true;
    }
   /* private boolean afterM(){
        return Build.VERSION.SDK_INT >= 23;
    }*/

    /**
     * dip转换px
     *
     * @param context
     * @param dpValue
     * @return float
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public boolean isBottomPanelVisible() {
        if (recyclerView_panel.getVisibility() == VISIBLE || rlayout_emoji.getVisibility() == VISIBLE) {
            return true;
        }

        return false;
    }

    public void setBottomPanelInVisible() {
        if (recyclerView_panel.getVisibility() == VISIBLE) {
            recyclerView_panel.setVisibility(GONE);
        }
        if (rlayout_emoji.getVisibility() == VISIBLE) {
            rlayout_emoji.setVisibility(GONE);
        }
    }

    /**
     * 显示商品布局
     */
    public void showGoodsBtn() {
        if (!stringList.contains("商品")) {
            stringList.add("商品");
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 显示订单布局
     */
    public void showOrderBtn() {
        if (!stringList.contains("订单")) {
            stringList.add("订单");
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 显示邀请评价布局
     */
    public void showCommentBtn() {
        if (!stringList.contains("邀请评价")) {
            stringList.add("邀请评价");
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 显示快捷回复
     */
    public void showFastComment() {
        if (!stringList.contains("快捷回复")) {
            stringList.add("快捷回复");
        }
        mAdapter.notifyDataSetChanged();
    }
}
