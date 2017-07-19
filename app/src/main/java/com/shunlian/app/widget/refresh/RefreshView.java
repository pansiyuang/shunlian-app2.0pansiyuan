package com.shunlian.app.widget.refresh;

//                            _ooOoo_
//                           o8888888o
//                           88" . "88
//                           (| -_- |)
//                            O\ = /O
//                        ____/`---'\____
//                        . ' \\| |// `.
//                       / \\||| : |||// \
//                     / _||||| -:- |||||- \
//                       | | \\\ - /// | |
//                     | \_| ''\---/'' | |
//                      \ .-\__ `-` ___/-. /
//                   ___`. .' /--.--\ `. . __
//                ."" '< `.___\_<|>_/___.' >'"".
//               | | : `- \`.;`\ _ /`;.`/ - ` : | |
//                 \ \ `-. \_ __\ /__ _/ .-` / /
//         ======`-.____`-.___\_____/___.-`____.-'======
//                            `=---='
//
//         .............................................
//                佛祖保佑                 永无BUG

/**
 * Created by zhang on 2017/7/10 11 : 44.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.utils.TransformUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017/6/20.
 */

public class RefreshView extends View {

    private static final String TAG = "ElmRefreshView";

    private Paint foodPaint, boxPaint, handPaint;
    private Paint bitmapPaint;
    private final static int boxResource = R.mipmap.home_box;
    private final static int textResource = R.mipmap.home_text;
    private final static int foodResource[] = {R.drawable.icon_chicken, R.drawable.icon_flower, R.drawable.icon_orange
            , R.drawable.icon_ufo, R.drawable.icon_pear};
    private final static int handResource[] = {R.drawable.icon_left, R.drawable.icon_right};
    private final static int ANIMATOR_DURATION = 800; //食物抛出动画持续一秒
    private static int MOVE_RADIUS; // 食物移动轨迹为圆环
    private final static int MOVE_END_ANGLE = 120;  //在140度时候 结束

    public final static int STATUS_STOP = 0;
    public final static int STATUS_RUNNING = 1;
    public final static int STATUS_MOVING = 2;
    private int status = STATUS_MOVING;

    private final static float HAND_START_ANGLE = 90;
    private final static float HAND_END_ANGLE = 225;
    private float currentHandAngle = HAND_END_ANGLE;

    private int width, height;  //整个view的大小
    private Point boxCenterPoint;  //box中心点坐标
    private Point leftHandPoint, rightHandPoint; //左右两个手柄的点，根据这个点做手柄的旋转 旋转范围(-45，45)
    private int boxWidth, boxHeight;  //box的大小
    private Bitmap boxBitmap;
    private Bitmap foodBitmap[] = new Bitmap[foodResource.length];
    private Bitmap leftHandBitmap, rightHandBitmap;
    private int handWidth, handHeight;
    private int foodWidth, foodHeight;
    private List<ElmFood> elmFoodList;

    private boolean isFirstAnimator = true; //这里用来防止抖动的bug
    private Paint lPaint;
    private Bitmap textBitmap;
    private int textWidth;
    private int textHeight;
    private float xishu = 0.01f;
    private float totalMax = 1.0f;

    public RefreshView(Context context) {
        super(context);
    }

    public RefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        init();
    }

    private void init(){
        MOVE_RADIUS = TransformUtil.dip2px(getContext(), 45);
        initPaint();
        initBitmap();
        initSize();
        initElmFoodList();
    }

    private void initSize() {
        //初始化box中心点坐标
        boxCenterPoint = new Point();
        boxCenterPoint.x = width/2;
        boxCenterPoint.y = 100 + boxHeight/2;
        //初始化左右手柄的坐标
        leftHandPoint = new Point();
        leftHandPoint.x = width/2 - boxWidth/2 + 5; //向右偏移
        leftHandPoint.y = boxCenterPoint.y - boxHeight/2;
        rightHandPoint = new Point();
        rightHandPoint.x = width/2 + boxWidth/2 - 5; //向左偏移
        rightHandPoint.y = boxCenterPoint.y - boxHeight/2;
    }

    private void initBitmap() {
        boxBitmap = ((BitmapDrawable)getResources().getDrawable(boxResource)).getBitmap();
        boxWidth = boxBitmap.getWidth();
        boxHeight = boxBitmap.getHeight();

        textBitmap = BitmapFactory.decodeResource(getResources(), textResource);
        textWidth = textBitmap.getWidth();
        textHeight = textBitmap.getHeight();

        for(int i = 0; i < foodResource.length; i++){
            foodBitmap[i] = BitmapFactory.decodeResource(getResources(),foodResource[i]);
            if(i == 0) {
                foodWidth = foodBitmap[i].getWidth();
                foodHeight = foodBitmap[i].getHeight();
            }
        }

        leftHandBitmap = ((BitmapDrawable)getResources().getDrawable(handResource[0])).getBitmap();
        rightHandBitmap = ((BitmapDrawable)getResources().getDrawable(handResource[1])).getBitmap();
        handWidth = leftHandBitmap.getWidth();
        handHeight = leftHandBitmap.getHeight();
    }

    public int getHeight_box() {
        return boxHeight + 100;
    }

    private void initPaint(){
        if(bitmapPaint == null) {
            bitmapPaint = new Paint();
            bitmapPaint.setAntiAlias(true);
            bitmapPaint.setDither(true);
            bitmapPaint.setFilterBitmap(true);
        }

        lPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        lPaint.setColor(Color.parseColor("#FF8202"));
        lPaint.setStyle(Paint.Style.FILL);
        lPaint.setStrokeWidth(6);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(status == STATUS_RUNNING) {
            onDrawFood(canvas);
        }
        //box最后画  可以遮挡食物
        if (status == STATUS_RUNNING) {
            onHeartbeatDrawBox(canvas);
//            onDrawBox(canvas);
        }else{
            onDrawBox(canvas);
        }
        onDrawText(canvas);

        if(status == STATUS_RUNNING)
            postInvalidate();
    }

    private void onHeartbeatDrawBox(Canvas canvas) {
        canvas.save();
        totalMax -= xishu;
        if (totalMax < 0.8f || totalMax > 1.0f){
            xishu = - xishu;
        }
        canvas.scale(1.0f,totalMax,0,boxCenterPoint.y + boxHeight / 2);
        onDrawBox(canvas);
        canvas.restore();
    }


    //画左右手柄
    private void onDrawHand(Canvas canvas) {
        canvas.save();
        canvas.rotate(-(currentHandAngle - 180), leftHandPoint.x, leftHandPoint.y);
        canvas.drawLine(leftHandPoint.x,leftHandPoint.y,leftHandPoint.x - boxWidth / 2,leftHandPoint.y,lPaint);
        canvas.restore();

        canvas.save();
        canvas.rotate((currentHandAngle - 180), rightHandPoint.x, rightHandPoint.y);
        canvas.drawLine(rightHandPoint.x,rightHandPoint.y,rightHandPoint.x + boxWidth / 2,rightHandPoint.y,lPaint);
        canvas.restore();
    }


    //画批量食物
    private void onDrawFood(Canvas canvas) {
        for(int i=0; i<elmFoodList.size(); i++){
            if(i != 0 && isFirstAnimator) {
                if(elmFoodList.get(i-1).angle > 27) {
                    drawFood(i, canvas);
                    if(i == elmFoodList.size() -1)
                        isFirstAnimator = false;
                }
            } else {
                drawFood(i, canvas);
            }
        }
    }

    //画单个食物
    private void drawFood(int foodPosition, Canvas canvas){
        RectF rectF = new RectF();
        ElmFood food = elmFoodList.get(foodPosition);
        if(food.startTime == 0){
            food.angle = 0;
            food.startTime = System.currentTimeMillis();
        } else {
            food.angle = (System.currentTimeMillis() - food.startTime) * 1.0f / ANIMATOR_DURATION * MOVE_END_ANGLE;
        }
        if(food.angle > MOVE_END_ANGLE) {
            food.angle = 0;
            food.startTime = System.currentTimeMillis();
            if(food.direction == 0)
                food.direction = 1;
            else
                food.direction = 0;
        }
        if(food.direction == 0) {
            food.x = boxCenterPoint.x - getXByAngle(food.angle, MOVE_RADIUS);
            food.y = boxCenterPoint.y - getYByAngle(food.angle, MOVE_RADIUS);
        } else {
            food.x = boxCenterPoint.x + getXByAngle(food.angle, MOVE_RADIUS);
            food.y = boxCenterPoint.y - getYByAngle(food.angle, MOVE_RADIUS);
        }
        rectF.left = food.x - foodWidth / 10;
        rectF.right = food.x + foodWidth / 10;
        rectF.top = food.y - foodHeight / 10;
        rectF.bottom = food.y + foodHeight / 10;
        canvas.drawBitmap(foodBitmap[foodPosition], null, rectF, bitmapPaint);
//        onDrawHeartbeatBox(canvas);
    }


    /**
     * 心跳箱
     * @param canvas
     */
    private void onDrawHeartbeatBox(Canvas canvas) {
//        canvas.save();
        //指定个区域画box
        RectF rectF = new RectF();
        rectF.left = boxCenterPoint.x - boxWidth/2;
        rectF.right = boxCenterPoint.x + boxWidth/2;
        rectF.top = boxCenterPoint.y - boxHeight/2;
        rectF.bottom = boxCenterPoint.y + boxHeight/2;
        canvas.drawBitmap(boxBitmap, null, rectF, bitmapPaint);
        canvas.scale(0.5f,0.5f);
//        canvas.restore();
    }

    private void onDrawBox(Canvas canvas) {
        //指定个区域画box
        RectF rectF = new RectF();
        rectF.left = boxCenterPoint.x - boxWidth/2;
        rectF.right = boxCenterPoint.x + boxWidth/2;
        rectF.top = boxCenterPoint.y - boxHeight/2;
        rectF.bottom = boxCenterPoint.y + boxHeight/2;
        canvas.drawBitmap(boxBitmap, null, rectF, bitmapPaint);
        onDrawHand(canvas);
    }

    private void onDrawText(Canvas canvas) {
        //指定个区域画text
        RectF rectF = new RectF();
        rectF.left = boxCenterPoint.x - textWidth/2 ;
        rectF.right = boxCenterPoint.x + textWidth/2;
        rectF.top = boxCenterPoint.y + boxHeight/2 + 20;
        rectF.bottom = boxCenterPoint.y + boxHeight/2 + 20 + textHeight;
        canvas.drawBitmap(textBitmap, null, rectF, bitmapPaint);
    }


    private class ElmFood{
        public float angle;
        public float x,y;   //坐标
        public int direction; ////方向 0左  1为右  -1为未设置状态
        public long startTime;
    }

    private void initElmFoodList(){
        elmFoodList = new ArrayList<>();
        for(int i = 0;i < foodResource.length; i++){
            ElmFood food = new ElmFood();
            initElmFoodPosition(food, i);
            elmFoodList.add(food);
        }
    }

    private void initElmFoodPosition(ElmFood food, int position){
        food.angle = 0;
        food.x = boxCenterPoint.x;
        food.y = boxCenterPoint.y;
        food.startTime = 0;
        if(position % 2 == 0){
            food.direction = 0;
        } else {
            food.direction = 1;
        }
    }

    private float getYByAngle(float angle, float radium){
        double radian = 2* Math.PI/360*angle;
        float length = (float) Math.sin(radian) * radium;
        return length;
    }

    private float getXByAngle(float angle, float radium){
//        angle =(angle + 180);
        double radian = 2* Math.PI/360*angle;
        float length = radium - (float) Math.cos(radian) * radium;
        return length;
    }

    public void setStatus(int status){
        this.status = status;
        if(status == STATUS_RUNNING){
            postInvalidate();
        } else if(status == STATUS_STOP){
            //动画结束重置
            isFirstAnimator = true;
            initElmFoodList();
        }
    }

    public void setPullPositionChanged(float percent){
        currentHandAngle = (percent * (HAND_END_ANGLE - HAND_START_ANGLE) + HAND_START_ANGLE);
        if(currentHandAngle < HAND_START_ANGLE)
            currentHandAngle = HAND_START_ANGLE;
        if(currentHandAngle > HAND_END_ANGLE)
            currentHandAngle = HAND_END_ANGLE;
        if(status == STATUS_MOVING)
            postInvalidate();
    }

}

