package com.shunlian.app.newchat.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.AttributeSet;
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
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.TransformUtil;


/**
 * 聊天界面输入控件
 */
public class ChatInput extends RelativeLayout implements TextWatcher, View.OnClickListener {

//    private static final String TAG = "ChatInput";

    private ImageButton btnAdd, btnEmotion;
    private Button btnSend;
    private EditText editText;
    private boolean isSendVisible, isEmoticonReady;
    private InputMode inputMode = InputMode.TEXT;
    private ChatView chatView;
    private LinearLayout morePanel;
    private ViewPager emoji_vp;
    private RelativeLayout rlayout_emoji;
    private LinearLayout llayout_dot;
    private View scroll_dot;
    private int spaceWidthDot;//点之间的间距
    private int startLeftPosi;//最左侧点的距离
    private int inputMethodHeight = -1;
    //    private final int REQUEST_CODE_ASK_PERMISSIONS = 100;


    public ChatInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.chat_input, this);
        initView();
    }

    private void initView() {
        btnAdd = (ImageButton) findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(this);
        btnSend = (Button) findViewById(R.id.btn_send);
        btnSend.setOnClickListener(this);
        btnEmotion = (ImageButton) findViewById(R.id.btnEmoticon);
        btnEmotion.setOnClickListener(this);
        morePanel = (LinearLayout) findViewById(R.id.morePanel);
        LinearLayout BtnImage = (LinearLayout) findViewById(R.id.btn_photo);
        BtnImage.setOnClickListener(this);
        LinearLayout BtnPhoto = (LinearLayout) findViewById(R.id.btn_image);
        BtnPhoto.setOnClickListener(this);
        LinearLayout btnGoods = (LinearLayout) findViewById(R.id.btn_goods);
        btnGoods.setOnClickListener(this);
        LinearLayout btnComment = (LinearLayout) findViewById(R.id.btn_comment);
        btnComment.setOnClickListener(this);
        setSendBtn();
        editText = (EditText) findViewById(R.id.input);
        editText.addTextChangedListener(this);
        editText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    updateView(InputMode.TEXT, false);
                }
            }
        });
        editText.setOnTouchListener((v,ev)->{
            if (inputMethodHeight == -1)
                getInputMethodHeight();
            return false;
        });
        isSendVisible = editText.getText().length() != 0;
        emoji_vp = (ViewPager) findViewById(R.id.emoji_vp);
        rlayout_emoji = (RelativeLayout) findViewById(R.id.rlayout_emoji);
        llayout_dot = (LinearLayout) findViewById(R.id.llayout_dot);
        scroll_dot = findViewById(R.id.scroll_dot);
        initVPListener();
    }

    private void getInputMethodHeight() {
        postDelayed(()->{
            int height = getRootView().getHeight();
            Rect rect = new Rect();
            getWindowVisibleDisplayFrame(rect);
            inputMethodHeight = height - rect.bottom;
            LinearLayout.LayoutParams rlayout_emoji_Params = new
                    LinearLayout.LayoutParams(getRootView().getWidth(),inputMethodHeight);
            rlayout_emoji.setLayoutParams(rlayout_emoji_Params);

            RelativeLayout.LayoutParams emoji_vp_Params = new
                    RelativeLayout.LayoutParams(getRootView().getWidth(),
                    (int) (inputMethodHeight*0.8f));
            emoji_vp.setLayoutParams(emoji_vp_Params);
        },300);
    }

    /**
     * 初始化vp监听
     */
    private void initVPListener() {
        emoji_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

                int v = (int) (spaceWidthDot * (positionOffset + position));
                RelativeLayout.LayoutParams layoutParams = (LayoutParams)
                        scroll_dot.getLayoutParams();
                layoutParams.leftMargin = startLeftPosi+v;
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
                morePanel.setVisibility(VISIBLE);
                break;
            case TEXT:
                if (editText.requestFocus()) {
                    InputMethodManager imm = (InputMethodManager) getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                    btnEmotion.setBackgroundResource(R.mipmap.icon_chat_smiley_n);
                }
                break;
            case EMOTICON:
                if (!isEmoticonReady) {
                    prepareEmoticon();
                }
                btnEmotion.setBackgroundResource(R.mipmap.icon_chat_smiley_h);
                rlayout_emoji.setVisibility(VISIBLE);
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
                morePanel.setVisibility(GONE);
                break;
            case EMOTICON:
                rlayout_emoji.setVisibility(GONE);
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

        EmojisVPAdapter emojisVPAdapter = new EmojisVPAdapter(getContext());
        emoji_vp.setAdapter(emojisVPAdapter);

        llayout_dot.removeAllViews();
        int width = TransformUtil.dip2px(getContext(), 2);
        for (int i = 0; i < emojisVPAdapter.getCount(); i++) {
            View view_dot = new View(getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width*2,width*2);
            lp.leftMargin = width * 5;
            view_dot.setLayoutParams(lp);
            view_dot.setBackgroundResource(R.drawable.red_dot);
            llayout_dot.addView(view_dot);
            GradientDrawable gd = (GradientDrawable) view_dot.getBackground();
            gd.setColor(getResources().getColor(R.color.my_line_gray));
        }

        llayout_dot.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                llayout_dot.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                startLeftPosi = llayout_dot.getChildAt(0).getLeft();
                spaceWidthDot = llayout_dot.getChildAt(1).getLeft() - startLeftPosi;

                GradientDrawable gd = (GradientDrawable) scroll_dot.getBackground();
                gd.setColor(getResources().getColor(R.color.share_text));
                RelativeLayout.LayoutParams layoutParams = (LayoutParams)
                        scroll_dot.getLayoutParams();

                layoutParams.leftMargin = startLeftPosi;
                scroll_dot.setLayoutParams(layoutParams);

                LogUtil.zhLogW("表情vp高==="+emoji_vp.getHeight());
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
        Activity activity = (Activity) getContext();
        switch (v.getId()) {
            case R.id.btn_send:
                chatView.sendText();
                break;
            case R.id.btn_add:
                updateView(inputMode == InputMode.MORE ? InputMode.TEXT : InputMode.MORE, true);
                break;
            case R.id.btn_photo:
                if (activity != null && requestCamera(activity)) {
                    chatView.sendPhoto();
                }
                break;
            case R.id.btn_image:
                if (activity != null && requestStorage(activity)) {
                    chatView.sendImage();
                }
                break;
            case R.id.btn_goods:
//                if (getContext() instanceof FragmentActivity){
//                FragmentActivity fragmentActivity = (FragmentActivity) getContext();
//                if (requestVideo(fragmentActivity)){
//                    VideoInputDialog.show(fragmentActivity.getSupportFragmentManager());
//                }
//            }
                break;
            case R.id.btnEmoticon:
                updateView(inputMode == InputMode.EMOTICON ? InputMode.TEXT : InputMode.EMOTICON, false);
                break;
            case R.id.btn_comment:
//                chatView.sendFile();
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
    }


    /**
     * 设置输入模式
     */
    public void setInputMode(InputMode mode,boolean isAdd) {
        updateView(mode,isAdd);
    }


    public enum InputMode {
        TEXT,
        VOICE,
        EMOTICON,
        MORE,
        VIDEO,
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
        if (morePanel.getVisibility() == VISIBLE || rlayout_emoji.getVisibility() == VISIBLE) {
            return true;
        }

        return false;
    }

    public void setBottomPanelInVisible() {
        if (morePanel.getVisibility() == VISIBLE) {
            morePanel.setVisibility(GONE);
        }
        if (rlayout_emoji.getVisibility() == VISIBLE) {
            rlayout_emoji.setVisibility(GONE);
        }
    }

}
