package com.shunlian.app.newchat.adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.utils.EmojisUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2018/4/11.
 */

public class EmojiAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private int mEmojiStart;//每页开始位置
    private int mPageSize;//第几页表情
    private final AssetManager am;
    private EmojisVPAdapter mVp;

    public EmojiAdapter(Context context, int pageSize, EmojisVPAdapter emojiAdapter,AssetManager am) {
        mContext = context;
        mEmojiStart = pageSize * 21;
        mPageSize = pageSize;
        this.am = am;
        this.mVp = emojiAdapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_emoji, parent, false);
        int height = parent.getHeight();
        EmojiHolder emojiHolder = new EmojiHolder(view);
        ViewGroup.LayoutParams layoutParams = emojiHolder.itemView.getLayoutParams();
        layoutParams.height = (int) (height / 3.5f);
        return emojiHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        EmojiHolder mHloder = (EmojiHolder) holder;
        position += mEmojiStart;
        if (position != mEmojiStart && (position % 20 - mPageSize) == 0) {
            mHloder.imageView.setImageResource(R.mipmap.icon_chat_smiley_del);
        } else {
            int emojisId = position - mPageSize;
            if (emojisId > 112) {
                return;
            }
            InputStream is = null;
            try {
                is = am.open(String.format("emojis/%d.png", emojisId));
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                is.close();
                Matrix matrix = new Matrix();
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                // 缩放图片的尺寸
                int i = TransformUtil.dip2px(mContext, 28);
                float scaleWidth = (float) i / width;
                float scaleHeight = (float) i / height;
                matrix.postScale(scaleWidth, scaleHeight);
                final Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
                mHloder.imageView.setImageBitmap(resizedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public int getItemCount() {
        return 21;
    }

    public class EmojiHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final MyImageView imageView;

        public EmojiHolder(View itemView) {
            super(itemView);
            imageView = (MyImageView) itemView.findViewById(R.id.miv_emojis);
            itemView.setOnClickListener(this);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            int i = getAdapterPosition() + mEmojiStart;
            if (i != mEmojiStart && (i % 20 - mPageSize) == 0) {
                mVp.onDel();
            } else {
                String s = EmojisUtils.emojisName(i - mPageSize);
                mVp.OnEmojiClick(i - mPageSize, s);
            }
        }
    }
}
