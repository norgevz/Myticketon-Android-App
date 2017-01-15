package com.application.norgevz.myticketon.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.application.norgevz.myticketon.DashboardScreen;
import com.application.norgevz.myticketon.R;
import com.application.norgevz.myticketon.models.SalesByAuditorium;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by norgevz on 12/11/2016.
 */

@EFragment(R.layout.statistics_layout)
public class StatisticsFragment extends Fragment  implements AdapterView.OnItemSelectedListener {

    @ViewById(R.id.graph_spinner)
    Spinner graphSpinner;

    @ViewById(R.id.select_date_btn)
    Button selectDateButton;

    @ViewById(R.id.in_date)
    EditText in_date;

    @ViewById(R.id.money_bar_chart)
    BarChart moneyBarChart;


    @ViewById(R.id.tickets_bar_chart)
    BarChart ticketsBarChart;

    private int mYear, mMonth, mDay;
    private boolean date_selected_already = false;

    private DashboardScreen activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (DashboardScreen) context;
    }

    @AfterViews
    public void afterViews() {
        ArrayAdapter<CharSequence> spinnerAdapter =
                ArrayAdapter.createFromResource(getContext(), R.array.graphs, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        graphSpinner.setAdapter(spinnerAdapter);
        graphSpinner.setOnItemSelectedListener(this);
        in_date.setKeyListener(null);
        selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                if(!date_selected_already) {
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                    date_selected_already = true;
                }

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.YEAR, year);
                        cal.set(Calendar.MONTH, monthOfYear);
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        Date date = cal.getTime();
                        String text = String.format("%1$s %2$tB %2$td, %2$tY", "", date );
                        in_date.setText(text);
                        activity.fetchSales(date);
                        mYear = year;
                        mMonth = monthOfYear;
                        mDay = dayOfMonth;
                        return;
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        initComponentsValues();
    }

    private void initComponentsValues()  {

        Date today = Calendar.getInstance().getTime();
        String text = String.format("%1$s %2$tB %2$td, %2$tY", "", today );
        in_date.setText(text);
        graphSpinner.setSelection(0);
        activity.fetchSales(today);
    }

    class Data{
        public String label;
        public double value;

        public Data(String label, double value) {
            this.label = label;
            this.value = value;
        }
    }

    public void setUpBarChart(BarChart chart, ArrayList<Data> data, String label){
        ArrayList<BarEntry> entries = new ArrayList<>();
        for( int i = 0; i < data.size(); i++ ){
            Data curr = data.get(i);
            entries.add(new BarEntry((float)curr.value, i));
        }

        BarDataSet dataSet = new BarDataSet(entries, label);

        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        ArrayList<String> labels = new ArrayList<>();
        for( int i = 0; i < data.size(); i++ ){
            Data curr = data.get(i);
            labels.add(curr.label);
        }

        BarData barData = new BarData(labels, dataSet);

        chart.setData(barData);

        barData.notifyDataChanged();

        chart.notifyDataSetChanged(); // let the chart know it's data changed
        chart.invalidate(); // refresh

        chart.setDescription("");
        chart.setDoubleTapToZoomEnabled(false);
        chart.getXAxis().setLabelsToSkip(0);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        System.out.println(position);
        showCurrentGraph();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        System.out.println("Nothing selected");
    }

    public void updateSales(ArrayList<SalesByAuditorium> sales){
        ArrayList<Data> dataTickets = new ArrayList<>();
        ArrayList<Data> dataMoney = new ArrayList<>();
        for( int i = 0; i < sales.size(); i++ ){
            SalesByAuditorium curr = sales.get(i);
            dataTickets.add(new Data(curr.AuditoriumName, curr.TicketsCount));
            dataMoney.add(new Data(curr.AuditoriumName, curr.SaleAmount));
        }

        setUpBarChart(ticketsBarChart, dataTickets, "Tickets Chart");
        setUpBarChart(moneyBarChart, dataMoney, "Money Chart");
        showCurrentGraph();
    }

    @UiThread
    public void showCurrentGraph(){
        ticketsBarChart.setVisibility(graphSpinner.getSelectedItemPosition() == 1 ? View.VISIBLE : View.INVISIBLE);
        moneyBarChart.setVisibility(graphSpinner.getSelectedItemPosition() == 0 ? View.VISIBLE : View.INVISIBLE);
    }
}
