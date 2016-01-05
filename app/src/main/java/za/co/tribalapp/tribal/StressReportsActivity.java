package za.co.tribalapp.tribal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYStepMode;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class StressReportsActivity extends ActionBarActivity {

    //global variables
    UserDbHandler userDb = new UserDbHandler(this);
    int stressIndex = 0;
    int workIndex = 0;
    int sleepIndex = 0;
    long startDate = 0;
    int currentDay = 0;
    SharedPreferences profile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stress_reports);
        profile = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        //receive intent from Reports activity
        Intent reports = getIntent();
        //get data
        currentDay = reports.getIntExtra("dayOfWeek",1);
        stressIndex = reports.getIntExtra("stressIndex",0);
        workIndex = reports.getIntExtra("workIndex",0);
        sleepIndex = reports.getIntExtra("sleepIndex",0);
        startDate = reports.getLongExtra("startDate",0);
        /*if (currentDay != profile.getInt("pref_restDay",0))
        {
            //Calculate stress index
            TextView stressIndexView = (TextView)findViewById(R.id.textView_stress_index);
            stressIndexView.setText(Integer.toString(mainActivity.getStressIndex()) + " %");
        }
        else //else indicate rest day
        {
            TextView stressIndexIsView = (TextView) findViewById(R.id.textView_stress_index_is);
            stressIndexIsView.setText("Rest day");
            TextView stressIndexView = (TextView) findViewById(R.id.textView_stress_index);
            stressIndexView.setText("");
        }*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.stress_reports, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Override back button
    /*@Override
    public void onBackPressed()
    {
        //Just restore layout to current activity; user must use Action bar to go back further
        setContentView(R.layout.activity_stress_reports);
    }*/

    public void displayStressIndex(View view)
    {
        setContentView(R.layout.stress_index);
        TextView stressIndexView = (TextView)findViewById(R.id.textView_stress_index);
        stressIndexView.append(stressIndex + "%");
        TextView workIndexView = (TextView)findViewById(R.id.textView_work_index);
        workIndexView.append(" " + workIndex + "%");
        TextView sleepIndexView = (TextView)findViewById(R.id.textView_sleep_index);
        sleepIndexView.append(" " + sleepIndex + "%");
    }

    //To-do
    public void displayStressTotals(View view)
    {
        //create line graph
        setContentView(R.layout.xy_plot);
        //create XYPlot
        XYPlot plot = (XYPlot) findViewById(R.id.mySimpleXYPlot);
        //set labels
        plot.setTitle("Work and sleep recorded this week");
        plot.setDomainLabel("Day");
        plot.setRangeLabel("Amount (hours)");
        //set step to 1 and domain to start at 1
        plot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 1);

        //integer values
        plot.setDomainValueFormat(new DecimalFormat("#"));
        plot.setRangeValueFormat(new DecimalFormat("#"));
        plot.setDomainLowerBoundary(1, BoundaryMode.FIXED);

        //create arrays of y-values to plot
        //increment query range by ms equivalent of 1 day for 6 days
        long dayInc = 24*3600*1000;
        //Day 0 is 0
        Number[] seriesWork = {userDb.getStressTotal("Work",startDate,startDate+dayInc),
                userDb.getStressTotal("Work",startDate+dayInc,startDate+dayInc*2),
                userDb.getStressTotal("Work",startDate+dayInc*2,startDate+dayInc*3),
                userDb.getStressTotal("Work",startDate+dayInc*3,startDate+dayInc*4),
                userDb.getStressTotal("Work",startDate+dayInc*4,startDate+dayInc*5),
                userDb.getStressTotal("Work",startDate+dayInc*5,startDate+dayInc*6)
        };

        Number[] seriesSleep = {userDb.getStressTotal("Sleep",startDate,startDate+dayInc),
                userDb.getStressTotal("Sleep",startDate+dayInc,startDate+dayInc*2),
                userDb.getStressTotal("Sleep",startDate+dayInc*2,startDate+dayInc*3),
                userDb.getStressTotal("Sleep",startDate+dayInc*3,startDate+dayInc*4),
                userDb.getStressTotal("Sleep",startDate+dayInc*4,startDate+dayInc*5),
                userDb.getStressTotal("Work",startDate+dayInc*5,startDate+dayInc*6),
        };

        //turn above arrays into XYSeries
        XYSeries series1 = new SimpleXYSeries(
                Arrays.asList(seriesWork),          // SimpleXYSeries takes a List so turn array into a List
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, // Y_VALS_ONLY means use the element index as the x value
                "Work");                             // Set the display title of the series
        // same as above
        XYSeries series2 = new SimpleXYSeries(Arrays.asList(seriesSleep), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Sleep");

        // Create a formatter to use for drawing a series using LineAndPointRenderer
        // parameters: line paint, vertex paint, fill paint, text paint
        LineAndPointFormatter series1Format = new LineAndPointFormatter(Color.RED, Color.RED, Color.TRANSPARENT, null);

        // add a new series' to the xyplot:
        plot.addSeries(series1, series1Format);

        // same as above:
        LineAndPointFormatter series2Format = new LineAndPointFormatter(Color.GREEN, Color.GREEN, Color.TRANSPARENT, null);
        series2Format.setPointLabelFormatter(new PointLabelFormatter());
        //series2Format.configure(getApplicationContext(),
        //        R.xml.line_point_formatter_with_plf2);
        plot.addSeries(series2, series2Format);

        // reduce the number of range labels
        plot.setTicksPerRangeLabel(3);
        plot.getGraphWidget().setDomainLabelOrientation(-45);
    }

    public void displayStressList(View view)
    {
        //make list horizontally scrollable
        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(this);
        //use list view
        ListView listView = new ListView(this);
        listView.setCacheColorHint(00000000);
        //create custom formatted list based on columns
        List<String> formattedList = new ArrayList<String>();
        String headings = String.format("%-5s%-15s%-10s%-10s%-10s","ID","Type","Duration(hrs)","Heart Rate","Date");
        //String underline = String.format("%3s%15s%5s%5s%5s%10s","-","-","-","-");
        formattedList.add(headings);
        //formattedList.add(underline);
        final List<StressSession> stressList = userDb.getAllStress();
        for (int i=0; i< stressList.size(); i++)
        {
            //get record one by one
            StressSession record = stressList.get(i);
            //format date
            long idate = record.getDate();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String date = df.format(idate);
            //add record to list
            String sRecord = String.format("%-7s%-20s%-10s%-10s%-10s",
                    record.getID(),
                    record.getType(),
                    record.getDuration(),
                    record.getHRV(),
                    date);
            formattedList.add(sRecord);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, formattedList);
        listView.setAdapter(arrayAdapter);

        //make items deletable from db on selection
        //toast to let user know they can delete an item
        Toast toast = Toast.makeText(this, "Select a session to delete it", Toast.LENGTH_SHORT);
        toast.show();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                //ask to confirm delete
                AlertDialog.Builder alert = new AlertDialog.Builder(StressReportsActivity.this);
                alert.setMessage("Confirm delete?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        userDb.deleteStressSession(stressList.get(position-1));
                        //Toast to let user know meal was deleted
                        Context context = StressReportsActivity.this;
                        CharSequence text = "Session deleted";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        //refresh list
                        displayStressList(getCurrentFocus());
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton) {}
                });
                alert.show();
            }
        });
        horizontalScrollView.addView(listView);
        setContentView(horizontalScrollView);
    }
}
