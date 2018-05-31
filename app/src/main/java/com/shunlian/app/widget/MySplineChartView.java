package com.shunlian.app.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;

import com.shunlian.app.R;
import com.shunlian.app.bean.PlusDataEntity;
import com.shunlian.app.utils.LogUtil;
import com.zh.chartlibrary.chart.CustomLineData;
import com.zh.chartlibrary.chart.PointD;
import com.zh.chartlibrary.chart.SplineChart;
import com.zh.chartlibrary.chart.SplineData;
import com.zh.chartlibrary.common.DensityUtil;
import com.zh.chartlibrary.common.DrawHelper;
import com.zh.chartlibrary.renderer.XEnum;
import com.zh.chartlibrary.renderer.axis.DataAxis;
import com.zh.chartlibrary.view.ChartView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/29.
 */

public class MySplineChartView extends ChartView {

    private int sale_line_color;
    private int member_line_color;
    private int bg_color;
    private int axis_line_color;

    private String TAG = "MySplineChartView";
    private SplineChart chart = new SplineChart();
    private SplineChart lnChart = new SplineChart();
    //分类轴标签集合
    private LinkedList<String> labels = new LinkedList<>();
    private LinkedList<SplineData> chartData = new LinkedList<>();
    private LinkedList<SplineData> chartDataLn = new LinkedList<>();

    private List<CustomLineData> mXCustomLineDataset = new ArrayList<>();
    private int[] ltrb = new int[4];

    //线1的数据集
    List<PointD> linePoint1 = new ArrayList<>();
    List<PointD> linePoint2 = new ArrayList<>();

    private int Max_Sale_Num = 100;
    private int Max_Member_Num = 100;

    public MySplineChartView(Context context) {
        super(context);
    }

    public MySplineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySplineChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void initView() {

        sale_line_color = getResources().getColor(R.color.pink_color);
        member_line_color = getResources().getColor(R.color.value_920783);
        bg_color = getResources().getColor(R.color.value_FBF6E2);
        axis_line_color = getResources().getColor(R.color.value_7C1212);

        ltrb[0] = DensityUtil.dip2px(getContext(), 40); //left
        ltrb[1] = DensityUtil.dip2px(getContext(), 40); //top
        ltrb[2] = DensityUtil.dip2px(getContext(), 40); //right
        ltrb[3] = DensityUtil.dip2px(getContext(), 40); //bottom

        chartRender();
        chartLnRender();
        //綁定手势滑动事件
        this.bindTouch(this, chart);
        this.bindTouch(this, lnChart);
    }

    public void setMaxSaleNum(int saleNum) {
        Max_Sale_Num = saleNum;
    }

    public void setMaxMemberNum(int memberNum) {
        Max_Member_Num = memberNum;
    }

    public void setChartData(List<PlusDataEntity.Chart> chartList) {
        LogUtil.httpLogW("setChartData( )");
        if (chartList == null || chartList.size() == 0) {
            return;
        }
        linePoint1.clear();
        linePoint2.clear();
        labels.clear();
        labels.add("");
        for (int i = 0; i < chartList.size(); i++) {
            PlusDataEntity.Chart chart = chartList.get(i);
            double d1 = Double.valueOf(chart.total_sales);
            double d2 = Double.valueOf(chart.plus_num);
            linePoint1.add(new PointD((i + 1) * 10d, d1));
            linePoint2.add(new PointD((i + 1) * 10d, d2));
            labels.add(chart.date);
        }
        labels.add("");

        SplineData dataSeries1 = new SplineData("", linePoint1, sale_line_color);
        //把线弄细点
        dataSeries1.getLinePaint().setStrokeWidth(2);
        dataSeries1.setDotRadius(4);
        dataSeries1.getLinePaint().setAntiAlias(true);
        dataSeries1.getDotPaint().setColor(sale_line_color);
        chartData.add(dataSeries1);

        SplineData dataSeries2 = new SplineData("", linePoint2, member_line_color);
        //把线弄细点
        dataSeries2.getLinePaint().setStrokeWidth(2);
        dataSeries2.setDotRadius(4);
        dataSeries2.getLinePaint().setAntiAlias(true);
        dataSeries2.getDotPaint().setColor(member_line_color);
        chartDataLn.add(dataSeries2);

        //数据源
        chart.setCategories(labels);
        chart.setDataSource(chartData);
        lnChart.setCategories(labels);
        lnChart.setDataSource(chartDataLn);

        initView();

        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //图所占范围大小
        chart.setChartRange(w, h);
        lnChart.setChartRange(w, h);
    }

    private void chartRender() {
        try {
            chart.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);

            //数据轴最大值
            chart.getDataAxis().setAxisMax(Max_Sale_Num);
            chart.getDataAxis().setAxisMin(0);
            //数据轴刻度间隔
            chart.getDataAxis().setAxisSteps(Max_Sale_Num / 10f);//y轴
            chart.getDataAxis().getTickLabelPaint().setColor(axis_line_color);
            chart.getDataAxis().setLabelFormatter(value -> {
                double label = Double.parseDouble(value);
                DecimalFormat df = new DecimalFormat("#0");
                return df.format(label).toString();
            });

            //标签轴最大值
            chart.setCategoryAxisMax((labels.size() - 1) * 10);
            //标签轴最小值
            chart.setCategoryAxisMin(0);
            chart.getCategoryAxis().setAxisSteps(10);
            chart.setCategoryAxisCustomLines(mXCustomLineDataset); //x轴
            chart.getCategoryAxis().getTickLabelPaint().setColor(axis_line_color);

            //设置图的背景色
            chart.setApplyBackgroundColor(true);
            chart.setBackgroundColor(bg_color);

            //调轴线与网络线风格
            chart.getCategoryAxis().showTickMarks();
            chart.getDataAxis().showAxisLine();
            chart.getDataAxis().showTickMarks();
            chart.getPlotGrid().hideHorizontalLines();
            //显示边框
            chart.hideBorder();

            chart.getCategoryAxis().getAxisPaint().setStrokeWidth(chart.getPlotGrid().getHorizontalLinePaint().getStrokeWidth()); //x轴线宽
            chart.getDataAxis().getAxisPaint().setStrokeWidth(chart.getPlotGrid().getHorizontalLinePaint().getStrokeWidth());//y轴线宽

            chart.getCategoryAxis().getAxisPaint().setColor(axis_line_color);
            chart.getDataAxis().getAxisPaint().setColor(axis_line_color);

            chart.getCategoryAxis().getTickMarksPaint().setColor(axis_line_color);
            chart.getDataAxis().getTickMarksPaint().setColor(axis_line_color);

            chart.getCategoryAxis().getTickMarksPaint().setStrokeWidth(chart.getPlotGrid().getHorizontalLinePaint().getStrokeWidth());
            chart.getDataAxis().getTickMarksPaint().setStrokeWidth(chart.getPlotGrid().getHorizontalLinePaint().getStrokeWidth());

            //请自行分析定制
            chart.setDotLabelFormatter(value -> {
                String label = "[" + value + "]";
                return (label);
            });

            //激活点击监听
            chart.ActiveListenItemClick();
            //为了让触发更灵敏，可以扩大5px的点击监听范围
            chart.extPointClickRange(5);
            chart.showClikedFocus();

            //显示平滑曲线
            chart.setCrurveLineStyle(XEnum.CrurveLineStyle.BEZIERCURVE);

            //图例显示在正下方
            chart.getPlotLegend().setVerticalAlign(XEnum.VerticalAlign.BOTTOM);
            chart.getPlotLegend().setHorizontalAlign(XEnum.HorizontalAlign.CENTER);

            chart.disableScale();//禁止缩放
            chart.disablePanMode();//禁止滑动

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }
    }

    private void chartLnRender() {
        try {
            lnChart.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);

            renderLnAxis();

            lnChart.getPlotLegend().show();
            lnChart.getPlotLegend().setType(XEnum.LegendType.COLUMN);
            lnChart.getPlotLegend().setVerticalAlign(XEnum.VerticalAlign.TOP);
            lnChart.getPlotLegend().setHorizontalAlign(XEnum.HorizontalAlign.LEFT);
            lnChart.getPlotLegend().hideBackground();
            lnChart.getDataAxis().getAxisPaint().setStrokeWidth(lnChart.getPlotGrid().getHorizontalLinePaint().getStrokeWidth());//y轴线宽
            lnChart.getDataAxis().getTickMarksPaint().setStrokeWidth(lnChart.getPlotGrid().getHorizontalLinePaint().getStrokeWidth());

            lnChart.disableScale();//禁止缩放
            lnChart.disablePanMode();//禁止滑动
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e(TAG, e.toString());
        }
    }

    /**
     * 第二y轴相关
     */
    private void renderLnAxis() {
        //标签轴
        lnChart.getCategoryAxis().hide();

        //数据轴
        lnChart.setDataAxisLocation(XEnum.AxisLocation.RIGHT);
        DataAxis dataAxis = lnChart.getDataAxis();
        dataAxis.setAxisMax(Max_Member_Num);
        dataAxis.setAxisMin(0);
        dataAxis.setAxisSteps(Max_Member_Num / 10f);
        dataAxis.getAxisPaint().setColor(axis_line_color);
        dataAxis.getTickMarksPaint().setColor(axis_line_color);

        //定制数据轴上的标签格式
        lnChart.getDataAxis().setLabelFormatter(value -> {
            double label = Double.parseDouble(value);
            DecimalFormat df = new DecimalFormat("#0");
            return df.format(label).toString();
        });

        //调整右轴显示风格
        lnChart.getDataAxis().setHorizontalTickAlign(Paint.Align.RIGHT);
        lnChart.getDataAxis().getTickLabelPaint().setTextAlign(Paint.Align.LEFT);

        lnChart.setCategoryAxisMax((labels.size() - 1) * 10);
        //标签轴最小值
        lnChart.setCategoryAxisMin(0);
        lnChart.getCategoryAxis().setAxisSteps(10);
        lnChart.setCategoryAxisCustomLines(mXCustomLineDataset); //x轴

        lnChart.hideBorder();
    }

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    @Override
    public void render(Canvas canvas) {
        try {
            chart.render(canvas);
            lnChart.render(canvas);
            paint.setTextSize(22);
            paint.setColor(axis_line_color);
            float textHeight = DrawHelper.getInstance().getPaintFontHeight(paint);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("销售额", chart.getPlotArea().getLeft(), chart.getPlotArea().getTop() - textHeight, paint);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }
}
