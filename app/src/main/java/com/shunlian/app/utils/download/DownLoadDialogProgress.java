package com.shunlian.app.utils.download;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.CBProgressBar;
import com.shunlian.app.widget.circle.DrawHookView;
import com.shunlian.app.widget.dialog.CommonDialog;


public class DownLoadDialogProgress {
    private long time = 1500;
    private CommonDialog nomalBuildl;
    private TextView tv_down_2g;
    private CBProgressBar cbProgressBar;
    private View show_line;
    private TextView  tv_down_ok;
    private LinearLayout  tv_ready_down;
    private LinearLayout tv_down_success;
    private View bottom_view;
    private LinearLayout down_bottom;
    private DrawHookView draw_hook;
    public void showProgress(int progress){
        cbProgressBar.setProgress(progress);
    };

    public void showStartDown(){
        tv_down_2g.setVisibility(View.GONE);
        tv_ready_down.setVisibility(View.GONE);
        cbProgressBar.setVisibility(View.VISIBLE);
        show_line.setVisibility(View.GONE);
        tv_down_ok.setVisibility(View.GONE);
        tv_down_success.setVisibility(View.GONE);
    };

    public void downLoadSuccess(){
        tv_down_2g.setVisibility(View.GONE);
        tv_ready_down.setVisibility(View.GONE);
        cbProgressBar.setVisibility(View.GONE);
        show_line.setVisibility(View.GONE);
        tv_down_ok.setVisibility(View.GONE);
        tv_down_success.setVisibility(View.VISIBLE);
        bottom_view.setVisibility(View.GONE);
        down_bottom.setVisibility(View.INVISIBLE);
        draw_hook.setInitData();
        draw_hook.invalidate();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nomalBuildl.dismiss();
            }
        },time);
    };
    public void dissMissDialog(){
        if(nomalBuildl!=null&&nomalBuildl.isShowing()){
            nomalBuildl.dismiss();
        }
    };
    public void showDownLoadDialogProgress(Context context,downStateListen stateListen,boolean is2g){
        if(nomalBuildl!=null&&nomalBuildl.isShowing()){
            return;
        }
        if(nomalBuildl==null) {
            CommonDialog.Builder nomalBuild = new CommonDialog.Builder(context).setWidth((int) TransformUtil.dip2px(context, 250f)).loadAniamtion()
                    .setView(R.layout.dialog_down_file);
            nomalBuild.setOnClickListener(R.id.tv_down_calcel, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stateListen.cancelDownLoad();
                    nomalBuildl.dismiss();
                }
            });

            nomalBuildl = nomalBuild.create();
        }
        nomalBuildl.setCancelable(false);
        nomalBuildl.show();
        tv_down_2g= nomalBuildl.getView(R.id.tv_down_2g);
        cbProgressBar = nomalBuildl.getView(R.id.my_progress);
        show_line= nomalBuildl.getView(R.id.show_line);
        tv_down_ok = nomalBuildl.getView(R.id.tv_down_ok);
        tv_ready_down= nomalBuildl.getView(R.id.tv_ready_down);
        tv_down_success= nomalBuildl.getView(R.id.tv_down_success);
        bottom_view = nomalBuildl.getView(R.id.bottom_view);
        down_bottom = nomalBuildl.getView(R.id.down_bottom);
        draw_hook= nomalBuildl.getView(R.id.draw_hook);
        cbProgressBar.setProgress(0);
        bottom_view.setVisibility(View.VISIBLE);
        down_bottom.setVisibility(View.VISIBLE);
        if(is2g){
            tv_down_2g.setVisibility(View.VISIBLE);
            cbProgressBar.setVisibility(View.GONE);
            show_line.setVisibility(View.VISIBLE);
            tv_down_ok.setVisibility(View.VISIBLE);
            tv_down_success.setVisibility(View.GONE);
        }else{
//            tv_down_2g.setVisibility(View.GONE);
//            tv_ready_down.setVisibility(View.VISIBLE);
//            cbProgressBar.setVisibility(View.GONE);
//            show_line.setVisibility(View.GONE);
//            tv_down_success.setVisibility(View.GONE);
//            tv_down_ok.setVisibility(View.GONE);
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    showStartDown();
//                    stateListen.fileDownLoad();
//                }
//            },time);
            showStartDown();
            stateListen.fileDownLoad();

        }
        tv_down_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStartDown();
                stateListen.fileDownLoad();
//                tv_down_2g.setVisibility(View.GONE);
//                tv_ready_down.setVisibility(View.VISIBLE);
//                cbProgressBar.setVisibility(View.GONE);
//                show_line.setVisibility(View.GONE);
//                tv_down_success.setVisibility(View.GONE);
//                tv_down_ok.setVisibility(View.GONE);
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        showStartDown();
//                        stateListen.fileDownLoad();
//                    }
//                },time);
            }
        });

    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    public interface downStateListen{
        void cancelDownLoad();
        void fileDownLoad();
    }
}
