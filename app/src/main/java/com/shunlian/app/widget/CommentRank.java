package com.shunlian.app.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.shunlian.app.R;
import com.shunlian.app.utils.TransformUtil;

/**
 * Created by Administrator on 2017/12/11.
 */

public class CommentRank extends LinearLayout {
    public CommentRank(Context context) {
        this(context,null);
    }

    public CommentRank(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CommentRank(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);
        praiseRank();
    }

    /**
     * 好评
     */
   public void praiseRank(){
        removeAllViews();
       for (int i = 0; i < 5; i++) {
           MyImageView imageView = new MyImageView(getContext());
           imageView.setImageResource(R.mipmap.icon_haoping_h);
           addView(imageView);
           LinearLayout.LayoutParams layoutParams = (LayoutParams) imageView.getLayoutParams();
           layoutParams.rightMargin = TransformUtil.dip2px(getContext(),5);
           imageView.setLayoutParams(layoutParams);
       }
   }

    /**
     * 中评
     */
   public void middleRank(){
       removeAllViews();
       for (int i = 0; i < 3; i++) {
           MyImageView imageView = new MyImageView(getContext());
           imageView.setImageResource(R.mipmap.icon_zhongping_h);
           addView(imageView);
           LinearLayout.LayoutParams layoutParams = (LayoutParams) imageView.getLayoutParams();
           layoutParams.rightMargin = TransformUtil.dip2px(getContext(),5);
           imageView.setLayoutParams(layoutParams);
       }
   }

    /**
     * 差评
     */
   public void badRank(){
       removeAllViews();
       MyImageView imageView = new MyImageView(getContext());
       imageView.setImageResource(R.mipmap.icon_chaping_h);
       addView(imageView);
//       LinearLayout.LayoutParams layoutParams = (LayoutParams) imageView.getLayoutParams();
//       layoutParams.rightMargin = TransformUtil.dip2px(getContext(),5);
//       imageView.setLayoutParams(layoutParams);
   }
}
