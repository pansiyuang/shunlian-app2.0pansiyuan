package com.shunlian.app.ui.collection;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.FootAdapter;
import com.shunlian.app.adapter.FootprintAdapter;
import com.shunlian.app.bean.CalendarEntity;
import com.shunlian.app.bean.FootprintEntity;
import com.shunlian.app.presenter.FootPrintPresenter;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IFootPrintView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.calendar.Calendar;
import com.shunlian.app.widget.calendar.CalendarView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;

import static com.shunlian.app.utils.FastClickListener.isFastClick;

/**
 * Created by Administrator on 2018/1/22.
 * 足迹
 */

public class FootprintFrag extends CollectionFrag implements View.OnClickListener, IFootPrintView, CalendarView.OnYearChangeListener, CalendarView.OnDateSelectedListener, CalendarView.OnStatusChangeListener, FootAdapter.OnChildClickListener {

    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    @BindView(R.id.tv_date)
    TextView tv_date;

    @BindView(R.id.miv_pre)
    MyImageView miv_pre;

    @BindView(R.id.miv_next)
    MyImageView miv_next;

    @BindView(R.id.content)
    CoordinatorLayout content;

    @BindView(R.id.rl_date_control)
    RelativeLayout rl_date_control;

    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;

    @BindView(R.id.calendarView)
    CalendarView calendarView;

    @BindView(R.id.miv_calendar_more)
    MyImageView miv_calendar_more;

    private FootPrintPresenter printPresenter;
    private List<Calendar> markData = new ArrayList<>();
    private FootAdapter footAdapter;
    private List<FootprintEntity.MarkData> markDataList;
    private List<FootprintEntity.DateInfo> dateInfoList;
    private List<Object> objectList = new ArrayList<>();
    private GridLayoutManager manager;
    private CoordinatorLayout.LayoutParams params;
    private RelativeLayout.LayoutParams calendarParams;
    private int totalCount = 0;
    private int residueCount;
    private int index = 0;
    private int dateCount = 0;
    private int isDelAll = 2;  //0 全选  1 部分选择 2 全不选
    private boolean isSelectAll;
    private List<FootprintEntity.MarkData> delList;
    private FootprintAdapter footprintAdapter;


    private List<FootprintEntity.MarkData> markDataLists = new ArrayList<>();

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.frag_footprint, null, false);
    }

    @Override
    protected void initData() {
        recycler_list.setHasFixedSize(false);
        printPresenter = new FootPrintPresenter(baseContext, this);
        markDataList = new ArrayList<>();
        dateInfoList = new ArrayList<>();
        delList = new ArrayList<>();
        manager = new GridLayoutManager(getContext(), 3);
        recycler_list.setLayoutManager(manager);
        ((SimpleItemAnimator) recycler_list.getItemAnimator()).setSupportsChangeAnimations(false); //解决刷新item图片闪烁的问题

        tv_date.setText(calendarView.getCurYear() + "年" + calendarView.getCurMonth() + "月");

        printPresenter.getMarkCalendar();
        printPresenter.getMarklist(String.valueOf(calendarView.getCurYear()), String.valueOf(calendarView.getCurMonth()), false);

        params = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.WRAP_CONTENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);
        calendarParams = new RelativeLayout.LayoutParams(CoordinatorLayout.LayoutParams.WRAP_CONTENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);

//        calendarView.shrink();
    }

    @Override
    protected void initListener() {
        miv_next.setOnClickListener(this);
        miv_pre.setOnClickListener(this);
        miv_calendar_more.setOnClickListener(this);

        calendarView.setOnYearChangeListener(this);
        calendarView.setOnDateSelectedListener(this);
        calendarView.setOnStatusChangeListener(this);
        recycler_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (manager != null) {
                    int lastPosition = manager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == manager.getItemCount()) {
                        if (printPresenter != null) {
                            printPresenter.onRefresh();
                        }
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        super.initListener();
    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        return calendar;
    }


    /**
     * 删除
     */
    @Override
    public void delete() {
        String ids = getDelIds();
        if (isEmpty(ids)) {
            return;
        }
        printPresenter.deleteBatch(ids);
    }

    /**
     * 选择所有
     */
    @Override
    public void selectAll() {
        if (isSelectAll) {
            isSelectAll = false;
        } else {
            isSelectAll = true;
        }
        selectTotalItem(isSelectAll);
    }

    /**
     * 操作管理
     */
    @Override
    public void operationMange() {
        LogUtil.httpLogW("operationMange()");
    }

    /**
     * 完成管理
     */
    @Override
    public void finishManage() {
        selectTotalItem(false);
    }

    /**
     * 管理是否可点击
     *
     * @return
     */
    @Override
    public boolean isClickManage() {
        if (!isEmpty(markDataList) && footAdapter != null) {
            if (!footAdapter.getEditMode()) {
                footAdapter.setEditMode(true);
            } else {
                footAdapter.setEditMode(false);
            }
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (isFastClick())
            return;
        switch (v.getId()) {
            case R.id.miv_pre:
                calendarView.scrollToPre();
                break;
            case R.id.miv_next:
                calendarView.scrollToNext();
                break;
            case R.id.miv_calendar_more:
                if (calendarView.isExpand()) {
                    calendarView.shrink();
                    rl_date_control.setVisibility(View.GONE);
                } else {
                    calendarView.expand();
                    rl_date_control.setVisibility(View.VISIBLE);
                }
                calendarView.setLayoutParams(calendarParams);
                break;
        }
    }

    @Override
    public void getCalendarList(List<CalendarEntity> calendarEntityList) {
        if (!isEmpty(calendarEntityList)) {
            for (CalendarEntity calendarEntity : calendarEntityList) {
                if ("1".equals(calendarEntity.has_data)) {
                    int year = Integer.valueOf(calendarEntity.year);
                    int month = Integer.valueOf(calendarEntity.month);
                    int day = Integer.valueOf(calendarEntity.date);
                    markData.add(getSchemeCalendar(year, month, day, R.color.pink_color, "标"));
                }
            }
            calendarView.setSchemeDate(markData);
        }
    }

    @Override
    public void getMarkList(List<FootprintEntity.MarkData> list, List<FootprintEntity.DateInfo> dateList, int page, int allPage) {
        if (page == 1) {
            markDataList.clear();
            objectList.clear();
            dateInfoList.addAll(dateList);
        }
        if (isSelectAll) {
            for (FootprintEntity.DateInfo dateInfo : dateInfoList) {
                dateInfo.isSelect = true;
            }
        }

        if (!isEmpty(list)) {
            markDataList.addAll(list);
//            getObjectList(page);
        }



        /***************新增start******************/

        if (!isEmpty(list)){
            markDataLists.addAll(list);
        }

        if (footprintAdapter == null) {
            footprintAdapter = new FootprintAdapter(baseActivity, markDataLists,dateList);
            recycler_list.setAdapter(footprintAdapter);
        }else {
            footprintAdapter.notifyDataSetChanged();
        }

        /***************新增end******************/


//        if (footAdapter == null) {
//            footAdapter = new FootAdapter(getContext(), objectList);
//            recycler_list.setAdapter(footAdapter);
//            footAdapter.setPageLoading(page, allPage);
//            footAdapter.setOnReloadListener(new BaseRecyclerAdapter.OnReloadListener() {
//                @Override
//                public void onReload() {
//                    if (printPresenter != null) {
//                        printPresenter.onRefresh();
//                    }
//                }
//            });
//            footAdapter.setOnChildClickListener(this);
//        } else {
//            footAdapter.setPageLoading(page, allPage);
//            if (markDataList.size() <= printPresenter.PAGE_SIZE)
//                footAdapter.notifyDataSetChanged();
//            else
//                footAdapter.notifyItemInserted(printPresenter.PAGE_SIZE);
//        }
//        footAdapter.notifyDataSetChanged();
    }

    @Override
    public void delSuccess(String msg) {
        notifyList();
        Common.staticToast(msg);
    }

    public void getObjectList(int page) {
        int childCount;
        for (int i = index; i < dateInfoList.size(); i++) {
            if (residueCount > 0) { //结余数量大于0
                childCount = residueCount;
                totalCount = childCount + totalCount;
                residueCount = 0;
            } else {  //否则没有结余
                FootprintEntity.DateInfo dateInfo = dateInfoList.get(i);
                childCount = Integer.valueOf(dateInfo.counts);
                objectList.add(dateInfo); //添加title
                dateCount++;
                if (childCount + totalCount > page * 20) {  //当前日期的子数量加上总数大于当前页的总数
                    childCount = page * 20 - totalCount;
                    totalCount = page * 20;
                    residueCount = Integer.valueOf(dateInfoList.get(i).counts) - childCount; //剩余的数量
                } else {
                    totalCount = childCount + totalCount;
                    residueCount = 0;
                }
            }
            boolean isSelect = dateInfoList.get(i).isSelect; //获取当前日期是否已经被选中
            for (int j = 0; j < childCount; j++) {
                FootprintEntity.MarkData mark = markDataList.get(objectList.size() - dateCount);
                mark.isSelect = isSelect;
                objectList.add(mark);
                if (isSelect) {
                    delList.add(mark);
                }
            }
            if (residueCount <= 0) {
                index++;
            }
            if (totalCount >= page * 20) {
                //总数大于等于当前页乘于数量、需要加载下一页数据了
                break;
            }
        }
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void onYearChange(int year) {

    }

    @Override
    public void onDateSelected(Calendar calendar, boolean isClick) {
        tv_date.setText(calendar.getYear() + "年" + calendar.getMonth() + "月");
    }

    @Override
    public void OnExpand() {
        int height = TransformUtil.dip2px(getActivity(), 440.5f);
        params.height = height;
        appBarLayout.setLayoutParams(params);
        calendarParams.height = TransformUtil.dip2px(getActivity(), 350f);
        calendarView.setLayoutParams(calendarParams);
        if (rl_date_control.getVisibility() == View.GONE) {
            rl_date_control.setVisibility(View.VISIBLE);
        }
        miv_calendar_more.setRotation(180f);
    }

    @Override
    public void OnShrink() {
        int height = TransformUtil.dip2px(getActivity(), 130.5f);
        params.height = height;
        appBarLayout.setLayoutParams(params);
        calendarParams.height = TransformUtil.dip2px(getActivity(), 100f);
        calendarView.setLayoutParams(calendarParams);
        if (rl_date_control.getVisibility() == View.VISIBLE) {
            rl_date_control.setVisibility(View.GONE);
        }
        miv_calendar_more.setRotation(0f);
    }

    @Override
    public void OnDateSelect(int position, FootprintEntity.DateInfo dateInfo) {
        if (!footAdapter.isEdit) {
            return;
        }
        FootprintEntity.DateInfo mDateInfo = (FootprintEntity.DateInfo) objectList.get(position);
        if (dateInfo.isSelect) {
            dateInfo.isSelect = mDateInfo.isSelect = false;
            setDateItemStatus(dateInfo.date, false);
        } else {
            dateInfo.isSelect = mDateInfo.isSelect = true;
            setDateItemStatus(dateInfo.date, true);
        }
        setCurrentSelectStatus();
    }

    @Override
    public void OnItemSelect(int position, FootprintEntity.MarkData markData) {
        FootprintEntity.MarkData mMarkData = (FootprintEntity.MarkData) objectList.get(position);
        if (!footAdapter.isEdit) {
            GoodsDetailAct.startAct(baseActivity, mMarkData.goods_id);
        } else {
            if (markData.isSelect) {
                mMarkData.isSelect = false;
                delItem(mMarkData.id, markData.date_normal);
            } else {
                mMarkData.isSelect = true;
                selectItem(mMarkData.id, markData.date_normal);
            }
        }
        setCurrentSelectStatus();
    }

    /**
     * 设置当前日期的状态
     *
     * @param date
     */
    public void setDateItemStatus(String date, boolean isSelect) {
        for (int i = 0; i < objectList.size(); i++) {
            if (objectList.get(i) instanceof FootprintEntity.DateInfo && ((FootprintEntity.DateInfo) objectList.get(i)).date.equals(date)) {
                ((FootprintEntity.DateInfo) objectList.get(i)).isSelect = isSelect;
            }
            if (objectList.get(i) instanceof FootprintEntity.MarkData && ((FootprintEntity.MarkData) objectList.get(i)).date_normal.equals(date)) {
                ((FootprintEntity.MarkData) objectList.get(i)).isSelect = isSelect;
                if (isSelect) {
                    delList.add(((FootprintEntity.MarkData) objectList.get(i)));
                } else {
                    delList.remove(objectList.get(i));
                }
            }
        }
        footAdapter.notifyDataSetChanged();
    }

    /**
     * 选择单个Item
     *
     * @param markId
     * @param date
     * @return
     */
    public void selectItem(String markId, String date) {
        boolean b = true;

        for (int i = 0; i < objectList.size(); i++) {
            FootprintEntity.MarkData markData;
            if (objectList.get(i) instanceof FootprintEntity.MarkData) {
                markData = (FootprintEntity.MarkData) objectList.get(i);
                if (markData.id.equals(markId) && markData.date_normal.equals(date)) {
                    markData.isSelect = true;
                    delList.add(markData);
                    footAdapter.notifyItemChanged(i);
                }
                if (markData.date_normal.equals(date) && !markData.isSelect) {
                    b = false;
                }
            }
        }
        if (!b) {
            return;
        }
        for (int i = 0; i < objectList.size(); i++) {
            if (objectList.get(i) instanceof FootprintEntity.DateInfo) {
                if (((FootprintEntity.DateInfo) objectList.get(i)).date.equals(date)) {
                    ((FootprintEntity.DateInfo) objectList.get(i)).isSelect = true;
                    footAdapter.notifyItemChanged(i);
                    break;
                }
            }
        }
    }

    /**
     * 删除单个Item
     *
     * @param markId
     * @param date
     */
    public void delItem(String markId, String date) {
        for (int i = 0; i < objectList.size(); i++) {
            FootprintEntity.MarkData markData;
            FootprintEntity.DateInfo dateInfo;
            if (objectList.get(i) instanceof FootprintEntity.MarkData) {
                markData = (FootprintEntity.MarkData) objectList.get(i);
                if (markData.id.equals(markId) && markData.date_normal.equals(date)) {
                    markData.isSelect = false;
                    footAdapter.notifyItemChanged(i);
                    delList.remove(markData);
                }
            }
            if (objectList.get(i) instanceof FootprintEntity.DateInfo) {
                dateInfo = (FootprintEntity.DateInfo) objectList.get(i);
                if (dateInfo.isSelect && dateInfo.date.equals(date)) {
                    dateInfo.isSelect = false;
                }
                footAdapter.notifyItemChanged(i);
            }
        }
    }

    /**
     * 全选/反选择
     *
     * @param
     */

    public void selectTotalItem(boolean selectAll) {
        FootprintEntity.MarkData markData;
        FootprintEntity.DateInfo dateInfo;
        delList.clear();
        for (int i = 0; i < objectList.size(); i++) {
            if (objectList.get(i) instanceof FootprintEntity.DateInfo) {
                dateInfo = (FootprintEntity.DateInfo) objectList.get(i);
                dateInfo.isSelect = selectAll;
            }

            if (objectList.get(i) instanceof FootprintEntity.MarkData) {
                markData = (FootprintEntity.MarkData) objectList.get(i);
                markData.isSelect = selectAll;
                if (selectAll) {
                    delList.add(markData);
                }
            }
        }
        footAdapter.notifyDataSetChanged();
        setCurrentSelectStatus();
    }

    /**
     * 设置当前选择状态
     */
    public void setCurrentSelectStatus() {
        if (delList.size() == 0) {
            isDelAll = 2;
            isSelectAll = false;
        } else if (delList.size() == markDataList.size()) {
            isDelAll = 0;
            isSelectAll = true;
        } else {
            isDelAll = 1;
            isSelectAll = false;
        }
        ((MyCollectionAct) baseActivity).setManageState(isDelAll);
    }

    public String getDelIds() {
        if (isEmpty(delList)) {
            return null;
        }
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < delList.size(); i++) {
            result.append(delList.get(i).id);
            if (i != delList.size() - 1) {
                result.append(",");
            }
        }
        return result.toString();
    }

    public void notifyList() {
        for (Iterator it = objectList.iterator(); it.hasNext(); ) {
            Object object = it.next();
            if (object instanceof FootprintEntity.MarkData && ((FootprintEntity.MarkData) object).isSelect) {
                it.remove();
            }

            if (object instanceof FootprintEntity.DateInfo && ((FootprintEntity.DateInfo) object).isSelect) {
                it.remove();
            }
        }
        footAdapter.notifyDataSetChanged();
    }
}
