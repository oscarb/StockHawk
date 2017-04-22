package com.udacity.stockhawk.ui;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ID_DETAIL_LOADER = 4711;

    private Uri stockUri;

    @BindView(R.id.chart)
    LineChart chart;

    // Projection - desired columns to retrieve
    private static final String[] QUOTE_COLUMNS = {
            Contract.Quote.TABLE_NAME + "." + Contract.Quote._ID,
            Contract.Quote.COLUMN_SYMBOL,
            Contract.Quote.COLUMN_PRICE,
            Contract.Quote.COLUMN_ABSOLUTE_CHANGE,
            Contract.Quote.COLUMN_PERCENTAGE_CHANGE,
            Contract.Quote.COLUMN_HISTORY
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        stockUri = getIntent().getData();

        if (stockUri == null) {
            throw new NullPointerException("URI for DetailActivity cannot be null");
        }



        Timber.d("Uri: " + stockUri.toString());
        Timber.d("Symbol: " + stockUri.getLastPathSegment());

        getSupportLoaderManager().initLoader(ID_DETAIL_LOADER, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id != ID_DETAIL_LOADER) {
            throw new RuntimeException("No loader implemented with id " + id);
        } else {
            return new CursorLoader(this,
                    stockUri,
                    QUOTE_COLUMNS,
                    null,
                    null,
                    null);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Check for valid data
        boolean validDataInCursor = false;
        if (data != null && data.moveToFirst()) {
            validDataInCursor = true;
        }

        if (!validDataInCursor) {
            return;
        }

        ChartUtils.applySettings(chart, loader.getContext());

        setTitle(data.getString(Contract.Quote.POSITION_SYMBOL));
        // Bind data from loader to views
        List<Entry> entries = new ArrayList<>();

        String history = data.getString(Contract.Quote.POSITION_HISTORY);

        if(history.isEmpty()) {
            return;
        }

        // Sort by date...
        CSVReader csvReader = new CSVReader(new StringReader(history));
        String[] nextLine;
        try {
            while((nextLine = csvReader.readNext()) != null) {
                int stockTimestamp = Integer.valueOf(nextLine[0].substring(0, nextLine[0].length() - 3));
                float stockValue = Float.valueOf(nextLine[1]);

                entries.add(new Entry(stockTimestamp, stockValue));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



        Collections.sort(entries, new EntryXComparator());

        LineDataSet dataSet = new LineDataSet(entries, "Label");
        ChartUtils.applyStyling(dataSet, loader.getContext());


        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // No data stored from the cursor, no references to remove
    }
}
