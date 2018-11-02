package com.shunlian.app.utils.download;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.CBProgressBar;
import com.shunlian.app.widget.dialog.CommonDialog;

public class DownLoadDialogProgress {
    private CommonDialog nomalBuildl;
    private TextView tv_down_2g;
    private CBProgressBar cbProgressBar;
    private View show_line;
    private TextView  tv_down_ok;
    public void showProgress(int progress){
        cbProgressBar.setProgress(progress);
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
            CommonDialog.Builder nomalBuild = new CommonDialog.Builder(context).setWidth((int) TransformUtil.dip2px(context, 250f))
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
        cbProgressBar.setProgress(0);
        if(is2g){
            tv_down_2g.setVisibility(View.VISIBLE);
            cbProgressBar.setVisibility(View.GONE);
            show_line.setVisibility(View.VISIBLE);
            tv_down_ok.setVisibility(View.VISIBLE);
        }else{
            tv_down_2g.setVisibility(View.GONE);
            cbProgressBar.setVisibility(View.VISIBLE);
            show_line.setVisibility(View.GONE);
            tv_down_ok.setVisibility(View.GONE);
            stateListen.fileDownLoad();
        }
        tv_down_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateListen.fileDownLoad();
                tv_down_2g.setVisibility(View.GONE);
                cbProgressBar.setVisibility(View.VISIBLE);
                show_line.setVisibility(View.GONE);
                tv_down_ok.setVisibility(View.GONE);
            }
        });

    }

    public interface downStateListen{
        void cancelDownLoad();
        void fileDownLoad();
    }
}
