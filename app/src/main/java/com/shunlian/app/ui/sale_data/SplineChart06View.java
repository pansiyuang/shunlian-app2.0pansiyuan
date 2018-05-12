package com.shunlian.app.ui.sale_data;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.shunlian.app.R;
import com.shunlian.app.bean.SalesChartEntity;
import com.shunlian.app.utils.TransformUtil;
import com.zh.chartlibrary.chart.PointD;
import com.zh.chartlibrary.chart.SplineChart;
import com.zh.chartlibrary.chart.SplineData;
import com.zh.chartlibrary.common.DensityUtil;
import com.zh.chartlibrary.event.click.PointPosition;
import com.zh.chartlibrary.renderer.XEnum;
import com.zh.chartlibrary.renderer.plot.PlotGrid;
import com.zh.chartlibrary.view.ChartView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/12.
 */

public class SplineChart06View extends ChartView {

    private SplineChart chart = new SplineChart();
    //X轴
    private LinkedList<String> mLabels = new LinkedList<>();
    //数据源集合
    private LinkedList<SplineData> mChartData = new LinkedList<>();
    //自定义线
//    private List<CustomLineData> mXCustomLineDataset = new ArrayList<>();
//    private List<CustomLineData> mYCustomLineDataset = new ArrayList<>();

//    private Paint mPaintTooltips = new Paint(Paint.ANTI_ALIAS_FLAG);
    private SalesChartEntity mSalesChart;
    private int child_store_color;//小店颜色
    private int grand_child_store_color;//分店颜色
    private int consume_money_color;//消费金额颜色
    private int custome_line_color;//自定义线的颜色
    private int lable_text_color;//X轴标签文字颜色
    public String key_line1 = "小店销售额";
    public String key_line2 = "分店销售额";
    public String key_line3 = "消费金额";
    public String key_line4 = "预估收益";
    private OnSaleChartListener mChartListener;

    public SplineChart06View(Context context) {
        super(context);

    }

    public SplineChart06View(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public SplineChart06View(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }


    public void init(SalesChartEntity salesChart) {
        mSalesChart = salesChart;
        child_store_color = getResources().getColor(R.color.pink_color);
        grand_child_store_color = Color.parseColor("#43D060");
        consume_money_color = Color.parseColor("#F9A31C");
        custome_line_color = Color.parseColor("#eeeeee");
        lable_text_color = getResources().getColor(R.color.new_text);
        creatLables();
//        chartCustomeLines();
        creatChartData();
        chartRender();
        this.invalidate();
    }

    /*private void chartCustomeLines() {
        mYCustomLineDataset.clear();
        int i = TransformUtil.dip2px(getContext(), 0.5f);

        CustomLineData h_line = new CustomLineData("",
                parseDouble(mSalesChart.max_value)/2, custome_line_color, i);
        mYCustomLineDataset.add(h_line);

        mXCustomLineDataset.clear();
        CustomLineData v_line1 = new CustomLineData("", 1d, custome_line_color, i);
        CustomLineData v_line2 = new CustomLineData("", 2d, custome_line_color, i);
        CustomLineData v_line3 = new CustomLineData("", 3d, custome_line_color, i);
        CustomLineData v_line4 = new CustomLineData("", 4d, custome_line_color, i);
        CustomLineData v_line5 = new CustomLineData("", 5d, custome_line_color, i);

        mXCustomLineDataset.add(v_line1);
        mXCustomLineDataset.add(v_line2);
        mXCustomLineDataset.add(v_line3);
        mXCustomLineDataset.add(v_line4);
        mXCustomLineDataset.add(v_line5);
    }*/

    protected int[] getBarLnDefaultSpadding()
    {
        int [] ltrb = new int[4];
        ltrb[0] = DensityUtil.dip2px(getContext(), 40); //left
        ltrb[1] = DensityUtil.dip2px(getContext(), 60); //top
        ltrb[2] = DensityUtil.dip2px(getContext(), 20); //right
        ltrb[3] = DensityUtil.dip2px(getContext(), 40); //bottom
        return ltrb;
    }

    private void chartRender() {
        int[] bs = getBarLnDefaultSpadding();
        chart.setPadding(bs[2], bs[0], bs[2], bs[0]);

        //设置自定义线
//        chart.setCustomLines(mYCustomLineDataset);
//        chart.setCategoryAxisCustomLines(mXCustomLineDataset);

        //设置数据源
        chart.setCategories(mLabels);
        chart.setDataSource(mChartData);

        int line_w = TransformUtil.dip2px(getContext(), 0.5f);
        PlotGrid plotGrid = chart.getPlotGrid();
        plotGrid.showHorizontalLines();
        plotGrid.showVerticalLines();

        plotGrid.getHorizontalLinePaint().setStrokeWidth(line_w);
        plotGrid.getHorizontalLinePaint().setFakeBoldText(false);
        plotGrid.getHorizontalLinePaint().setColor(custome_line_color);

        plotGrid.getVerticalLinePaint().setStrokeWidth(line_w);
        plotGrid.getVerticalLinePaint().setFakeBoldText(false);
        plotGrid.getVerticalLinePaint().setColor(custome_line_color);

        //设置Y轴
        chart.getDataAxis().setAxisMax(parseDouble(mSalesChart.max_value));
        chart.getDataAxis().setAxisMin(0);
        //数据轴刻度间隔
        chart.getDataAxis().setAxisSteps(parseDouble(mSalesChart.max_value) / 2);
        //设置x轴
        chart.setCategoryAxisMax(listSize(mSalesChart.list) - 1);
        chart.setCategoryAxisMin(0);

        chart.getCategoryAxis().getAxisPaint().setColor(custome_line_color);
        chart.getCategoryAxis().getTickLabelPaint().setColor(lable_text_color);
        int textSize = TransformUtil.sp2px(getContext(), 8);
        chart.getCategoryAxis().getTickLabelPaint().setTextSize(textSize);
        chart.getCategoryAxis().setTickLabelMargin(TransformUtil.dip2px(getContext(),10));
        chart.getCategoryAxis().getAxisPaint()
                .setStrokeWidth(TransformUtil.dip2px(getContext(), 0.5f));
        chart.getDataAxis().hide();
        chart.getCategoryAxis().hideTickMarks();
        //不使用精确计算，忽略Java计算误差,提高性能
        chart.disableHighPrecision();


        //激活点击监听
        chart.ActiveListenItemClick();
        //为了让触发更灵敏，可以扩大5px的点击监听范围
        chart.extPointClickRange(TransformUtil.dip2px(getContext(), 10));
        chart.showClikedFocus();

        //显示平滑曲线
        chart.setCrurveLineStyle(XEnum.CrurveLineStyle.BEZIERCURVE);

        //图例显示在正下方
        chart.getPlotLegend().setVerticalAlign(XEnum.VerticalAlign.BOTTOM);
        chart.getPlotLegend().setHorizontalAlign(XEnum.HorizontalAlign.CENTER);
        chart.getPlotLegend().hide();
    }

    private void creatChartData() {
        mChartData.clear();
        List<SalesChartEntity.Item> list = mSalesChart.list;
        //小店销售额
        addChildStore(list);


        //分店销售额
        addGrandChildStore(list);


        //消费金额
        addConsumeMoney(list);


        //预估收益
        addProfit(list);

    }

    private void addProfit(List<SalesChartEntity.Item> list) {
        if (!isEmpty(list) && !TextUtils.isEmpty(list.get(0).profit)) {
            List<PointD> profit = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                SalesChartEntity.Item item = list.get(i);
                if (!TextUtils.isEmpty(item.profit) && !"0".equals(item.profit)) {
                    profit.add(new PointD(i, parseDouble(item.profit)));
                }
            }
            if (profit.size() > 0 &&  profit.size() < list.size()){
                double x = profit.get(0).x - 1;
                profit.add(0,new PointD(x, 0));
            }
            SplineData splineData4 = new SplineData(key_line4, profit, child_store_color);
            splineData4.getLinePaint().setStrokeWidth(3);
            splineData4.setLineStyle(XEnum.LineStyle.DASH);
            splineData4.setLabelVisible(false);
            splineData4.setDotStyle(XEnum.DotStyle.RING);
            splineData4.getDotPaint().setColor(Color.WHITE);
            splineData4.getPlotLine().getPlotDot().setRingInnerColor(child_store_color);//

            mChartData.add(splineData4);
        }
    }

    private void addConsumeMoney(List<SalesChartEntity.Item> list) {
        if (!isEmpty(list) && !TextUtils.isEmpty(list.get(0).consume_money)) {
            List<PointD> xf = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                SalesChartEntity.Item item = list.get(i);
                if (!TextUtils.isEmpty(item.consume_money) && !"0".equals(item.consume_money)) {
                    xf.add(new PointD(i, parseDouble(item.consume_money)));
                }
            }
            if (xf.size() > 0 && xf.size() < list.size()){
                double x = xf.get(0).x - 1;
                xf.add(0,new PointD(x, 0));
            }
            SplineData splineData3 = new SplineData(key_line3, xf, consume_money_color);
            splineData3.getLinePaint().setStrokeWidth(3);
            splineData3.setLineStyle(XEnum.LineStyle.DASH);
            splineData3.setLabelVisible(false);
            splineData3.setDotStyle(XEnum.DotStyle.RING);
            splineData3.getDotPaint().setColor(Color.WHITE);
            splineData3.getPlotLine().getPlotDot().setRingInnerColor(consume_money_color);//

            mChartData.add(splineData3);
        }
    }

    private void addGrandChildStore(List<SalesChartEntity.Item> list) {
        if (!isEmpty(list) && !TextUtils.isEmpty(list.get(0).grand_child_store)) {
            List<PointD> fd = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                SalesChartEntity.Item item = list.get(i);
                if (!TextUtils.isEmpty(item.grand_child_store)
                        && !"0".equals(item.grand_child_store)) {
                    fd.add(new PointD(i, parseDouble(item.grand_child_store)));
                }
            }
            if (fd.size() > 0 && fd.size() < list.size()){
                double x = fd.get(0).x - 1;
                fd.add(0,new PointD(x, 0));
            }
            SplineData splineData2 = new SplineData(key_line2, fd, grand_child_store_color);
            //设置线的粗细
            splineData2.getLinePaint().setStrokeWidth(3);
            splineData2.setLineStyle(XEnum.LineStyle.DASH);
            splineData2.setLabelVisible(false);
            splineData2.setDotStyle(XEnum.DotStyle.RING);
            splineData2.getDotPaint().setColor(Color.WHITE);
            splineData2.getPlotLine().getPlotDot().setRingInnerColor(grand_child_store_color);//

            mChartData.add(splineData2);
        }
    }

    private void addChildStore(List<SalesChartEntity.Item> list) {
        if (!isEmpty(list) && !TextUtils.isEmpty(list.get(0).child_store)) {
            List<PointD> xd = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                SalesChartEntity.Item item = list.get(i);
                if (!TextUtils.isEmpty(item.child_store) && !"0".equals(item.child_store)) {
                    xd.add(new PointD(i, parseDouble(item.child_store)));
                }
            }
            if (xd.size() > 0 && xd.size() > 0 && xd.size() < list.size()){
                double x = xd.get(0).x - 1;
                xd.add(0,new PointD(x, 0));
            }
            SplineData splineData1 = new SplineData(key_line1, xd, child_store_color);
            //设置线的粗细
            splineData1.getLinePaint().setStrokeWidth(3);
            splineData1.setLineStyle(XEnum.LineStyle.DASH);

            splineData1.setLabelVisible(false);
            splineData1.setDotStyle(XEnum.DotStyle.RING);
            splineData1.getDotPaint().setColor(Color.WHITE);
            splineData1.getPlotLine().getPlotDot().setRingInnerColor(child_store_color);//

            mChartData.add(splineData1);
        }
    }

    private void creatLables() {
        mLabels.clear();
        //30day and 60day只显示首尾日期
        if (!TextUtils.isEmpty(mSalesChart.start_date) &&
                !TextUtils.isEmpty(mSalesChart.end_date)){
            List<SalesChartEntity.Item> list = mSalesChart.list;
            mLabels.add(mSalesChart.start_date);
            for (int i = 0; i < list.size()-2; i++) {
                mLabels.add("");
            }
            mLabels.add(mSalesChart.end_date);
        }else {
            List<SalesChartEntity.Item> list = mSalesChart.list;
            if (!isEmpty(list)) {
                for (int i = 0; i < list.size(); i++) {
                    mLabels.add(list.get(i).date);
                }
            }
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        chart.setChartRange(w, h);
    }

    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        try {
            canvas.drawColor(Color.WHITE);
            chart.render(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                triggerClick(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
               if (mChartListener != null){
                   mChartListener.onSaleCharts(null,null);
               }
                break;
        }
        return true;
    }

    private void triggerClick(float x, float y) {
        if (!chart.getListenItemClickStatus()) return;

        PointPosition record = chart.getPositionRecord(x, y);
        if (null == record) return;

        if (record.getDataID() >= mChartData.size()) return;

        int xAxisPos = 0;//x轴位置
        List<String> data = new ArrayList<>();
        for (int i = 0; i < mChartData.size(); i++) {
            SplineData splineData = mChartData.get(i);
            if (record.getDataChildID() < splineData.getLineDataSet().size()) {
                PointD pointD = splineData.getLineDataSet().get(record.getDataChildID());
//            LogUtil.zhLogW(splineData.getLineKey()+"："+pointD.y);
//            LogUtil.zhLogW("pointD.x="+pointD.x);
                xAxisPos = (int) pointD.x;
                data.add(splineData.getLineKey() + "：" + pointD.y);
            }
        }
//        LogUtil.zhLogW("--------------------------------------------------------------------------");
        if (mChartListener != null) {
            List<SalesChartEntity.Item> list = mSalesChart.list;
            mChartListener.onSaleCharts(data, list.get(xAxisPos).date);
        }
    }

    /**
     * 销售数据折线图监听
     *
     * @param chartListener
     */
    public void setOnSaleChartListener(OnSaleChartListener chartListener) {
        mChartListener = chartListener;
    }

    public interface OnSaleChartListener {
        void onSaleCharts(List<String> data, String date);
    }

    /*private void triggerClick1(float x, float y) {
        if (!chart.getListenItemClickStatus()) return;

        PointPosition record = chart.getPositionRecord(x, y);
        if (null == record) return;

        if (record.getDataID() >= mChartData.size()) return;
        SplineData lData = mChartData.get(record.getDataID());
        List<PointD> linePoint = lData.getLineDataSet();
        int pos = record.getDataChildID();
        int i = 0;
        Iterator it = linePoint.iterator();
        while (it.hasNext()) {
            PointD entry = (PointD) it.next();

            if (pos == i) {
                Double xValue = entry.x;
                Double yValue = entry.y;

                float r = record.getRadius();
                chart.showFocusPointF(record.getPosition(), r + r * 0.8f);
                chart.getFocusPaint().setStyle(Paint.Style.FILL);
                chart.getFocusPaint().setStrokeWidth(3);
                if (record.getDataID() >= 2) {
                    chart.getFocusPaint().setColor(Color.BLUE);
                } else {
                    chart.getFocusPaint().setColor(Color.RED);
                }
                //在点击处显示tooltip
                mPaintTooltips.setColor(Color.RED);
                chart.getToolTip().setCurrentXY(x, y);
                chart.getToolTip().addToolTip(" Key:" + lData.getLineKey(), mPaintTooltips);
                chart.getToolTip().addToolTip(
                        " Current Value:" + Double.toString(xValue) + "," + Double.toString(yValue), mPaintTooltips);
                chart.getToolTip().getBackgroundPaint().setAlpha(100);
                chart.getToolTip().hideBackground();
                this.invalidate();

                break;
            }
            i++;
        }//end while
    }*/


    private double parseDouble(String num) {
        if (TextUtils.isEmpty(num)) {
            num = "0";
        }
        return Double.parseDouble(num);
    }


    private int listSize(List list) {
        if (list != null && list.size() > 0) {
            return list.size();
        }
        return 0;
    }

    private boolean isEmpty(List list) {
        if (list == null)
            return true;
        if (list.size() == 0)
            return true;
        return false;
    }
}
