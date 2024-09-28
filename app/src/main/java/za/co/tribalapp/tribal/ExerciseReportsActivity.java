package za.co.tribalapp.tribal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
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


public class ExerciseReportsActivity extends AppCompatActivity {

    //global variables
    UserDbHandler userDb = new UserDbHandler(this);
    int exerciseIndex = 0;
    int cardioIndex = 0;
    int strengthIndex = 0;
    long startDate = 0;
    int currentDay = 0;
    SharedPreferences profile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_reports);
        profile = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        //receive intent from Reports activity
        Intent reports = getIntent();
        //get data
        currentDay = reports.getIntExtra("dayOfWeek",1);
        exerciseIndex = reports.getIntExtra("exerciseIndex",0);
        cardioIndex = reports.getIntExtra("cardioIndex",0);
        strengthIndex = reports.getIntExtra("strengthIndex",0);
        startDate = reports.getLongExtra("startDate",0);
        /*if (currentDay != profile.getInt("pref_restDay",0))
        {
            //Get exercise index
            TextView exerciseIndexView = (TextView)findViewById(R.id.textView_exercise_index);
            exerciseIndexView.setText(Integer.toString(mainActivity.getExerciseIndex()) + " %");
        }
        else //else indicate rest day
        {
            TextView dietIndexIsView = (TextView) findViewById(R.id.textView_diet_index_is);
            dietIndexIsView.setText("Rest day");
            TextView dietIndexView = (TextView) findViewById(R.id.textView_diet_index);
            dietIndexView.setText("");
        }*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.exercise_reports, menu);
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
        setContentView(R.layout.activity_exercise_reports);
    }*/

    public void displayExerciseIndex(View view)
    {
        setContentView(R.layout.exercise_index);
        TextView exerciseIndexView = (TextView)findViewById(R.id.textView_exercise_index);
        exerciseIndexView.append(exerciseIndex + "%");
        TextView cardioIndexView = (TextView)findViewById(R.id.textView_cardio_index);
        cardioIndexView.append(" " + cardioIndex + "%");
        TextView strengthIndexView = (TextView)findViewById(R.id.textView_strength_index);
        strengthIndexView.append(" " + strengthIndex + "%");
    }

    //To-do
    public void displayExerciseTotals(View view)
    {
        //create line graph
        setContentView(R.layout.xy_plot);
        //create XYPlot
        XYPlot plot = (XYPlot) findViewById(R.id.mySimpleXYPlot);
        //set labels
        plot.setTitle("Exercise recorded this week");
        plot.setDomainLabel("Day");
        plot.setRangeLabel("Amount (minutes)");
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
        Number[] seriesCardio = {userDb.getExerciseTotal("Cardio",startDate,startDate+dayInc),
                userDb.getExerciseTotal("Cardio",startDate+dayInc,startDate+dayInc*2),
                userDb.getExerciseTotal("Cardio",startDate+dayInc*2,startDate+dayInc*3),
                userDb.getExerciseTotal("Cardio",startDate+dayInc*3,startDate+dayInc*4),
                userDb.getExerciseTotal("Cardio",startDate+dayInc*4,startDate+dayInc*5),
                userDb.getExerciseTotal("Cardio",startDate+dayInc*5,startDate+dayInc*6)
        };

        Number[] seriesStrength = {userDb.getExerciseTotal("Strength",startDate,startDate+dayInc),
                userDb.getExerciseTotal("Strength",startDate+dayInc,startDate+dayInc*2),
                userDb.getExerciseTotal("Strength",startDate+dayInc*2,startDate+dayInc*3),
                userDb.getExerciseTotal("Strength",startDate+dayInc*3,startDate+dayInc*4),
                userDb.getExerciseTotal("Strength",startDate+dayInc*4,startDate+dayInc*5),
                userDb.getExerciseTotal("Strength",startDate+dayInc*4,startDate+dayInc*6)
        };

        //turn above arrays into XYSeries
        XYSeries series1 = new SimpleXYSeries(
                Arrays.asList(seriesCardio),          // SimpleXYSeries takes a List so turn array into a List
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, // Y_VALS_ONLY means use the element index as the x value
                "Cardio");                             // Set the display title of the series
        // same as above
        XYSeries series2 = new SimpleXYSeries(Arrays.asList(seriesStrength), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Strength");

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

    public void displayExerciseList(View view)
    {
        /*//fields to choose date range to query
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        //DatePicker datePicker = new DatePicker(this);
        Log.i("", Long.toString(startDate));
        Log.i("",Long.toString(endDate));*/

        //make list horizontally scrollable
        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(this);
        //use list view
        ListView listView = new ListView(this);
        listView.setCacheColorHint(00000000);
        //create custom formatted list based on columns
        List<String> formattedList = new ArrayList<String>();
        String headings = String.format("%-5s%-15s%-15s%-15s%-15s%-15s","ID","Name","Type","Duration(mins)","Heart Rate","Date");
        //String underline = String.format("%3s%15s%5s%5s%5s%10s","-","-","-","-");
        formattedList.add(headings);
        //formattedList.add(underline);
        final List<ExerciseSession> exerciseList = userDb.getAllExercise();

        for (int i=0; i< exerciseList.size(); i++)
        {
            //get record one by one
            ExerciseSession record = exerciseList.get(i);
            //format date
            long idate = record.getDate();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String date = df.format(idate);
            //add record to list
            String sRecord = String.format("%-7s%-20s%-15s%-15s%-15s%-15s",
                    record.getID(),
                    record.getName(),
                    record.getType(),
                    record.getDuration(),
                    record.getHRR(),
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
                AlertDialog.Builder alert = new AlertDialog.Builder(ExerciseReportsActivity.this);
                alert.setMessage("Confirm delete?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        userDb.deleteExerciseSession(exerciseList.get(position-1));
                        //Toast to let user know meal was deleted
                        Context context = ExerciseReportsActivity.this;
                        CharSequence text = "Session deleted";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        //refresh list
                        displayExerciseList(getCurrentFocus());
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
