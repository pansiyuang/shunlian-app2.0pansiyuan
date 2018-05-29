package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.InvitationEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/5/28.
 */

public class InvitationRecordAdapter extends BaseRecyclerAdapter<InvitationEntity.Invitation> {

    public InvitationRecordAdapter(Context context, List<InvitationEntity.Invitation> lists) {
        super(context, false, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new InvitationViewHolder(LayoutInflater.from(context).inflate(R.layout.item_invitation_record, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        InvitationEntity.Invitation invitation = lists.get(position);
        InvitationViewHolder invitationViewHolder = (InvitationViewHolder) holder;
        GlideUtils.getInstance().loadCircleImage(context, invitationViewHolder.miv_icon, invitation.avatar);
        invitationViewHolder.tv_name.setText(invitation.nickname);
        invitationViewHolder.tv_date.setText(invitation.enter_time);
        if (position == lists.size() - 1) {
            invitationViewHolder.view_line.setVisibility(View.GONE);
        } else {
            invitationViewHolder.view_line.setVisibility(View.VISIBLE);
        }
    }

    public class InvitationViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.tv_name)
        TextView tv_name;

        @BindView(R.id.tv_date)
        TextView tv_date;

        @BindView(R.id.view_line)
        View view_line;

        public InvitationViewHolder(View itemView) {
            super(itemView);
        }
    }
}
