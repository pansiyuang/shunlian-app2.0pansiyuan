package com.shunlian.app.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;

import com.shunlian.app.R;
import com.shunlian.app.bean.PlusDataEntity;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.TransformUtil;
import com.zh.chartlibrary.chart.CustomLineData;
import com.zh.chartlibrary.chart.PointD;
import com.zh.chartlibrary.chart.SplineChart;
import com.zh.chartlibrary.chart.SplineData;
import com.zh.chartlibrary.common.DensityUtil;
import com.zh.chartlibrary.common.DrawHelper;
import com.zh.chartlibrary.common.IFormatterTextCallBack;
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
    private SplineChart chart = new SplineChart();
    private SplineChart lnChart = new SplineChart();
    //分类轴标签集合
    private LinkedList<String> labels = new LinkedList<>();
    private LinkedList<SplineData> chartData = new LinkedList<>();
    private LinkedList<SplineData> chartDataLn = new LinkedList<>();

    private List<PointD> linePoint1 = new ArrayList<>();
    private List<PointD> linePoint2 = new ArrayList<>();

    private List<CustomLineData> mXCustomLineDataset = new ArrayList<>();
    private int[] ltrb = new int[4];

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

    public void initView(int saleNum, int memberNum, List<PlusDataEntity.Chart> chartList) {

        if (saleNum != 0) {
            Max_Sale_Num = saleNum;
        }

        if (memberNum != 0) {
            Max_Member_Num = memberNum;
        }

        if (chartList == null || chartList.size() == 0) {
            return;
        }
        linePoint1.clear();
        linePoint2.clear();
        labels.clear();
        labels.add("");
        for (int i = 0; i < chartList.size(); i++) {
            PlusDataEntity.Chart chart = chartList.get(i);
            PointD pointD1 = new PointD((i + 1) * 10d, Double.valueOf(chart.total_sales));
            PointD pointD2 = new PointD((i + 1) * 10d, Double.valueOf(chart.plus_num));
            linePoint1.add(pointD1);
            linePoint2.add(pointD2);
            labels.add(chart.date);
        }
        labels.add("");
        ltrb[0] = DensityUtil.dip2px(getContext(), 40); //left
        ltrb[1] = DensityUtil.dip2px(getContext(), 40); //top
        ltrb[2] = DensityUtil.dip2px(getContext(), 40); //right
        ltrb[3] = DensityUtil.dip2px(getContext(), 40); //bottom

        chartDataSet();
        chartLnDataSet();

        chartLnRender();
        chartRender();

        invalidate();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //图所占范围大小
        chart.setChartRange(w, h);
        lnChart.setChartRange(w, h);
    }

    private void chartDataSet() {
        SplineData dataSeries1 = new SplineData("", linePoint1, getResources().getColor(R.color.pink_color));
        //把线弄细点
        dataSeries1.getLinePaint().setStrokeWidth(2);
        dataSeries1.setDotRadius(4);
        dataSeries1.getDotPaint().setColor(getResources().getColor(R.color.pink_color));
        chartData.add(dataSeries1);
    }

    private void chartRender() {
        try {
            chart.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);

            //显示边框
//            chart.showRoundBorder();

            //数据源
            chart.setCategories(labels);
            chart.setDataSource(chartData);

            //数据轴最大值
            chart.getDataAxis().setAxisMax(Max_Sale_Num);
            chart.getDataAxis().setAxisMin(0);
            //数据轴刻度间隔
            chart.getDataAxis().setAxisSteps(Max_Sale_Num / 10);//y轴
            chart.getDataAxis().getTickLabelPaint().setColor(getResources().getColor(R.color.value_7C1212));
            chart.getDataAxis().setLabelFormatter(value -> {
                double label = Double.parseDouble(value);
                DecimalFormat df = new DecimalFormat("#0");
                return df.format(label).toString();
            });

            //标签轴最大值
            chart.setCategoryAxisMax((labels.size() - 1) * 10d);
            //标签轴最小值
            chart.setCategoryAxisMin(0);
            chart.getCategoryAxis().setAxisSteps(10d);
            chart.setCategoryAxisCustomLines(mXCustomLineDataset); //x轴
            chart.getCategoryAxis().getTickLabelPaint().setColor(getResources().getColor(R.color.value_7C1212));
            chart.getDataAxis().getTickLabelPaint().setTextSize(TransformUtil.sp2px(getContext(), 11));

            //设置图的背景色
//            chart.setApplyBackgroundColor(true);
//            chart.setBackgroundColor(Color.parseColor("#FBF6E2"));
//            chart.getBorder().setBorderLineColor(Color.rgb(179, 147, 197));

            //调轴线与网络线风格
            chart.getCategoryAxis().showTickMarks();
            chart.getCategoryAxis().setTickLabelRotateAngle(45f);
            chart.getDataAxis().showAxisLine();
            chart.getDataAxis().showTickMarks();
            chart.getPlotGrid().hideHorizontalLines();

            chart.getCategoryAxis().getAxisPaint().setStrokeWidth(chart.getPlotGrid().getHorizontalLinePaint().getStrokeWidth()); //x轴线宽
            chart.getDataAxis().getAxisPaint().setStrokeWidth(chart.getPlotGrid().getHorizontalLinePaint().getStrokeWidth());//y轴线宽

            chart.getCategoryAxis().getAxisPaint().setColor(getResources().getColor(R.color.value_7C1212));
            chart.getDataAxis().getAxisPaint().setColor(getResources().getColor(R.color.value_7C1212));

            chart.getCategoryAxis().getTickMarksPaint().setColor(getResources().getColor(R.color.value_7C1212));
            chart.getDataAxis().getTickMarksPaint().setColor(getResources().getColor(R.color.value_7C1212));

            chart.getCategoryAxis().getTickMarksPaint().setStrokeWidth(chart.getPlotGrid().getHorizontalLinePaint().getStrokeWidth());
            chart.getDataAxis().getTickMarksPaint().setStrokeWidth(chart.getPlotGrid().getHorizontalLinePaint().getStrokeWidth());

            //请自行分析定制
            chart.setDotLabelFormatter(value -> {
                // TODO Auto-generated method stub
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
            chart.hideBorder();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void chartLnDataSet() {
        SplineData dataSeries2 = new SplineData("", linePoint2, getResources().getColor(R.color.value_920783));
        //把线弄细点
        dataSeries2.getLinePaint().setStrokeWidth(2);
        dataSeries2.setDotRadius(4);
        dataSeries2.getDotPaint().setColor(getResources().getColor(R.color.value_920783));
        chartDataLn.add(dataSeries2);
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
            lnChart.getDataAxis().getTickLabelPaint().setTextSize(TransformUtil.sp2px(getContext(), 11));

            lnChart.disableScale();//禁止缩放
            lnChart.disablePanMode();//禁止滑动
        } catch (Exception e) {
            // TODO Auto-generated catch block
        }
    }

    /**
     * 第二y轴相关
     */
    private void renderLnAxis() {
        //标签轴
        lnChart.setCategories(labels);
        lnChart.getCategoryAxis().hide();

        //设定数据源
        lnChart.setDataSource(chartDataLn);
        //数据轴
        lnChart.setDataAxisLocation(XEnum.AxisLocation.RIGHT);
        DataAxis dataAxis = lnChart.getDataAxis();
        dataAxis.setAxisMax(Max_Member_Num);
        dataAxis.setAxisMin(0);
        dataAxis.setAxisSteps(Max_Member_Num / 10f);
        dataAxis.getAxisPaint().setColor(getResources().getColor(R.color.value_7C1212));
        dataAxis.getTickMarksPaint().setColor(getResources().getColor(R.color.value_7C1212));

        //定制数据轴上的标签格式
        lnChart.getDataAxis().setLabelFormatter(value -> {
            double label = Double.parseDouble(value);
            DecimalFormat df = new DecimalFormat("#0");
            return df.format(label).toString();
        });

        //调整右轴显示风格
        lnChart.getDataAxis().setHorizontalTickAlign(Paint.Align.RIGHT);
        lnChart.getDataAxis().getTickLabelPaint().setTextAlign(Paint.Align.LEFT);
        lnChart.getDataAxis().getTickLabelPaint().setColor(getResources().getColor(R.color.value_7C1212));

        lnChart.setCategoryAxisMax((labels.size() - 1) * 10);
        //标签轴最小值
        lnChart.setCategoryAxisMin(0);
        lnChart.getCategoryAxis().setAxisSteps(10);
        lnChart.getCategoryAxis().setTickLabelRotateAngle(45f);
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
            paint.setColor(getResources().getColor(R.color.value_7C1212));
            float textHeight = DrawHelper.getInstance().getPaintFontHeight(paint);
            paint.setTextAlign(Paint.Align.CENTER);
            String saleText = "销售额";
            String memberText = "会员数";
            Rect saleRect = new Rect();
            Rect memberRect = new Rect();
            paint.getTextBounds(saleText, 0, saleText.length(), saleRect);
            paint.getTextBounds(memberText, 0, memberText.length(), memberRect);
            canvas.drawText(saleText, chart.getPlotArea().getLeft(), chart.getPlotArea().getTop() - textHeight, paint);
            canvas.drawText(memberText, lnChart.getPlotArea().getRight(), lnChart.getPlotArea().getTop() - textHeight, paint);

            int saleWidth = saleRect.width();
            int saleHeight = saleRect.height();
            int memberWidth = memberRect.width();
            int memberHeight = memberRect.height();
            paint.setColor(getResources().getColor(R.color.pink_color));
            canvas.drawCircle(chart.getPlotArea().getLeft() - (saleWidth / 2) - 15, chart.getPlotArea().getTop() - (saleHeight / 2) - textHeight + 2, 4, paint);
            paint.setColor(getResources().getColor(R.color.value_920783));
            canvas.drawCircle(lnChart.getPlotArea().getRight() - (memberWidth / 2) - 15, lnChart.getPlotArea().getTop() - (memberHeight / 2) - textHeight + 2, 4, paint);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
