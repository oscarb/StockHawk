package com.udacity.stockhawk.ui;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.udacity.stockhawk.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

public class ChartUtils {

    public static void applyStyling(LineDataSet dataSet, Context context) {
        Resources res = context.getResources();

        dataSet.setColors(ContextCompat.getColor(context, R.color.line_color)); // Color of the line
        dataSet.setValueTextColor(ContextCompat.getColor(context, R.color.line_text_color));
        dataSet.setValueTextSize(getSizeInDp(R.dimen.line_text_size, res));
        dataSet.setDrawCircles(res.getBoolean(R.bool.line_draw_circles));
        dataSet.setDrawFilled(res.getBoolean(R.bool.line_draw_filled));
    }

    public static void applySettings(LineChart chart, Context context) {
        Resources res = context.getResources();

        // Chart
        chart.setNoDataText(res.getString(R.string.no_data_text));
        chart.setNoDataTextColor(ContextCompat.getColor(context, R.color.no_data_text_color));
        chart.setPinchZoom(res.getBoolean(R.bool.pinch_zoom));
        chart.setDoubleTapToZoomEnabled(res.getBoolean(R.bool.double_tap_to_zoom));
        chart.setAutoScaleMinMaxEnabled(res.getBoolean(R.bool.auto_scale_min_max));
        chart.setKeepPositionOnRotation(res.getBoolean(R.bool.keep_position_on_rotation));
        chart.getLegend().setEnabled(res.getBoolean(R.bool.legend));

        chart.setViewPortOffsets(getSizeInDp(R.dimen.viewport_offset_left, res),
                getSizeInDp(R.dimen.viewport_offset_top, res),
                getSizeInDp(R.dimen.viewport_offset_right, res),
                getSizeInDp(R.dimen.viewport_offset_bottom, res));

        Description description = new Description();
        description.setEnabled(res.getBoolean(R.bool.description));
        description.setText(res.getString(R.string.description_text));
        chart.setDescription(description);

        // Left axis
        chart.getAxisLeft().setEnabled(res.getBoolean(R.bool.axis_left));

        // Right axis
        YAxis yAxis = chart.getAxisRight();
        yAxis.setEnabled(res.getBoolean(R.bool.axis_right));
        yAxis.setTextColor(ContextCompat.getColor(context, R.color.axis_y_text_color));
        yAxis.setTextSize(getSizeInDp(R.dimen.axis_y_text_size, res));
        yAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);

        // Bottom axis
        XAxis xAxis = chart.getXAxis();
        xAxis.setEnabled(res.getBoolean(R.bool.axis_x));
        xAxis.setGranularity(res.getInteger(R.integer.axis_x_granularity));
        xAxis.setGranularityEnabled(res.getBoolean(R.bool.axis_x_granularity_enabled));
        xAxis.setAvoidFirstLastClipping(res.getBoolean(R.bool.axis_x_avoid_first_last_clipping));
        //xAxis.setLabelCount(res.getInteger(R.integer.axis_x_label_count));
        xAxis.setDrawLabels(res.getBoolean(R.bool.axis_x_draw_labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setTextColor(ContextCompat.getColor(context, R.color.axis_x_text_color));
        xAxis.setTextSize(getSizeInDp(R.dimen.axis_x_text_size, res));
        //xAxis.setValueFormatter(new EmptyXAxisFormatter());
        xAxis.setDrawGridLines(true);
        DateAndQuoteMarkerView markerView = new DateAndQuoteMarkerView (context, R.layout.chart_marker_view);
        markerView.setChartView(chart);
        chart.setMarker(markerView);
    }

    private static float getSizeInDp(int id, Resources resources) {
        return resources.getDimension(id) / resources.getDisplayMetrics().density;
    }

    private static class DateAndQuoteMarkerView extends MarkerView {

        TextView markerText;

        public DateAndQuoteMarkerView(Context context, int layoutResource) {
            super(context, layoutResource);
            markerText = (TextView) findViewById(R.id.marker_text);
        }

        @Override
        public void refreshContent(Entry e, Highlight highlight) {
            DecimalFormat dollarFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
            String price = dollarFormat.format(e.getY());
            String date = DateFormat.getDateInstance().format(new Date((long) e.getX() * 1000));
            markerText.setText(date + ": " + price);
            super.refreshContent(e, highlight);
        }

        @Override
        public MPPointF getOffsetForDrawingAtPoint(float posX, float posY) {
            int offsetFromTop = getResources().getDimensionPixelOffset(R.dimen.marker_offset_from_top);
            if (offsetFromTop + getHeight() > getChartView().getHeight()) {
                offsetFromTop = getChartView().getHeight() - getHeight();
            }

            return new MPPointF(-posX - getWidth()/2 + getChartView().getWidth()/2, -posY + offsetFromTop);
        }

    }


    private static class DateXAxisFormatter implements IAxisValueFormatter {

        private LineChart chart;

        public DateXAxisFormatter(LineChart chart) {
            this.chart = chart;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            Date date = new Date((long) value * 1000);
            return DateFormat.getDateInstance().format(date);
        }
    }

    private static class EmptyXAxisFormatter implements IAxisValueFormatter {

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return "";
        }
    }

}
