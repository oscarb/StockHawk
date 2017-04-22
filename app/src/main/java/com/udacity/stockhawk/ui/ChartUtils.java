package com.udacity.stockhawk.ui;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.udacity.stockhawk.R;

import java.text.DateFormat;
import java.util.Date;

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
        xAxis.setValueFormatter(new DateXAxisFormatter());
        xAxis.setAvoidFirstLastClipping(res.getBoolean(R.bool.axis_x_avoid_first_last_clipping));
        xAxis.setLabelCount(res.getInteger(R.integer.axis_x_label_count));
        xAxis.setDrawLabels(res.getBoolean(R.bool.axis_x_draw_labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(ContextCompat.getColor(context, R.color.axis_x_text_color));
        xAxis.setTextSize(getSizeInDp(R.dimen.axis_x_text_size, res));
    }

    private static float getSizeInDp(int id, Resources resources) {
        return resources.getDimension(id) / resources.getDisplayMetrics().density;
    }


    private static class DateXAxisFormatter implements  IAxisValueFormatter {

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            Date date = new Date((long) value * 1000);
            return DateFormat.getDateInstance().format(date);
        }
    }

}
