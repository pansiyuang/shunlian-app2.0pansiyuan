package com.shunlian.app.widget.calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.widget.calendar.component.State;
import com.shunlian.app.widget.calendar.interf.IDayRenderer;
import com.shunlian.app.widget.calendar.model.CalendarDate;
import com.shunlian.app.widget.calendar.view.DayView;

/**
 * Created by ldf on 17/6/26.
 */

@SuppressLint("ViewConstructor")
public class CustomDayView extends DayView {

    private TextView dateTv;
    private ImageView marker;
    private final CalendarDate today = new CalendarDate();

    /**
     * 构造器
     *
     * @param context        上下文
     * @param layoutResource 自定义DayView的layout资源
     */
    public CustomDayView(Context context, int layoutResource) {
        super(context, layoutResource);
        dateTv = (TextView) findViewById(R.id.date);
        marker = (ImageView) findViewById(R.id.maker);
    }

    @Override
    public void refreshContent() {
//        renderToday(day.getDate());
        renderSelect(day.getState(), day.getDate());
        renderMarker(day.getDate(), day.getState());
        super.refreshContent();
    }

    private void renderMarker(CalendarDate date, State state) {
        if (Utils.loadMarkData().containsKey(date.toString())) {
//            if (state == State.SELECT || date.toString().equals(today.toString())) {
//                marker.setVisibility(GONE);
//            } else {
            if (Utils.loadMarkData().get(date.toString()).equals("1")) {
                marker.setVisibility(VISIBLE);
            } else {
                marker.setVisibility(GONE);
            }
        } else {
            marker.setVisibility(GONE);
        }
//        } else {
//            marker.setVisibility(GONE);
//        }
    }

    private void renderSelect(State state, CalendarDate date) {
        if (state == State.SELECT) {
            dateTv.setTextColor(Color.WHITE);
            dateTv.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.selected_background));
        } else if (state == State.NEXT_MONTH || state == State.PAST_MONTH) {
            dateTv.setTextColor(getContext().getResources().getColor(R.color.new_gray));
            dateTv.setBackgroundDrawable(null);
        } else {
            if (date.equals(today)) {
                dateTv.setTextColor(getContext().getResources().getColor(R.color.pink_color));
                dateTv.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.today_background));
            } else {
                dateTv.setTextColor(getContext().getResources().getColor(R.color.new_text));
                dateTv.setBackgroundDrawable(null);
            }
        }
        dateTv.setText(date.day + "");
    }

    private void renderToday(CalendarDate date) {
        if (date != null) {
            if (date.equals(today)) {
                dateTv.setText(date.day + "");
                dateTv.setTextColor(getContext().getResources().getColor(R.color.pink_color));
                dateTv.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.today_background));
            } else {
                dateTv.setText(date.day + "");
                dateTv.setTextColor(getContext().getResources().getColor(R.color.new_text));
                dateTv.setBackgroundDrawable(null);
            }
        }
    }

    @Override
    public IDayRenderer copy() {
        return new CustomDayView(context, layoutResource);
    }
}
