package com.shunlian.app.yjfk;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.shunlian.app.ui.MainActivity;

/**
 * Created by Administrator on 2019/4/24.
 */

public class MyOnTouch implements View.OnTouchListener {
    int[] temp = new int[] { 0, 0 };

    Boolean ismove = false;

    int downX = 0;

    int downY = 0;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int eventaction = event.getAction();



        int x = (int) event.getRawX();

        int y = (int) event.getRawY();



        switch (eventaction) {



            case MotionEvent.ACTION_DOWN: // touch down so check if the

                temp[0] = (int) event.getX();

                temp[1] = y - v.getTop();

                downX = (int) event.getRawX();

                downY = (int) event.getRawY();

                ismove = false;

                break;



            case MotionEvent.ACTION_MOVE: // touch drag with the ball

                v.layout(x - temp[0], y - temp[1], x + v.getWidth() - temp[0], y - temp[1] + v.getHeight());




                if (Math.abs(downX - x) > 10 || Math.abs(downY - y) > 10)

                    ismove = true;

                break;

            case MotionEvent.ACTION_UP:

                if (!ismove)


                break;

        }

        return false;

    }

}
