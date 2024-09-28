package za.co.tribalapp.tribal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
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


public class DietReportsActivity extends AppCompatActivity {

    //initialize db objects
    UserDbHandler userDb = new UserDbHandler(this);
    //variables
    int dietIndex = 0;
    int carbIndex = 0;
    int fatIndex = 0;
    int proteinIndex = 0;
    long startDate = 0;
    SharedPreferences profile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_reports);
        profile = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        //receive intent from Reports activity
        Intent reports = getIntent();
        //get data
        dietIndex = reports.getIntExtra("dietIndex", 0);
        carbIndex = reports.getIntExtra("carbIndex",0);
        fatIndex = reports.getIntExtra("fatIndex",0);
        proteinIndex = reports.getIntExtra("proteinIndex",0);
        startDate = reports.getLongExtra("startDate",0);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.diet_reports, menu);
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
        //If window in focus is not Diet Reports (activity_diet_reports.xml), return to it, not two screens back
        if (! (getWindow().getDecorView().getRootView().hasWindowFocus()) )
        {
            setContentView(R.layout.activity_diet_reports);
        }
    }*/

    //Display diet index
    public void displayDietIndex(View view)
    {
        setContentView(R.layout.diet_index);
        TextView dietIndexView = (TextView)findViewById(R.id.textView_diet_index);
        dietIndexView.append(dietIndex + "%");
        TextView carbIndexView = (TextView)findViewById(R.id.textView_carb_index);
        carbIndexView.append(" " + carbIndex + "%");
        TextView fatIndexView = (TextView)findViewById(R.id.textView_fat_index);
        fatIndexView.append(" " + fatIndex + "%");
        TextView proteinIndexView = (TextView)findViewById(R.id.textView_protein_index);
        proteinIndexView.append(" " + proteinIndex + "%");
    }

    //To-do
    public void displayDietTotals(View view)
    {
        //create line graph
        setContentView(R.layout.xy_plot);
        //create XYPlot
        XYPlot plot = (XYPlot) findViewById(R.id.mySimpleXYPlot);
        //set labels
        plot.setTitle("Nutrient intake this week");
        plot.setDomainLabel("Day");
        plot.setRangeLabel("Intake (grams)");
        //set step to 1 and domain to start at 1
        plot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 1);
        plot.setDomainLowerBoundary(1, BoundaryMode.FIXED);
        //integer values
        plot.setDomainValueFormat(new DecimalFormat("#"));
        plot.setRangeValueFormat(new DecimalFormat("#"));
        //domain label position
        plot.getGraphWidget().getDomainLabelPaint().setTextAlign(Paint.Align.RIGHT);

        //create arrays of y-values to plot
        //increment query range by ms equivalent of 1 day for 6 days
        long dayInc = 24*3600*1000;
        //Day 0 is 0
        Number[] seriesCarbs = {userDb.getDietTotal(userDb.COLUMN_CARBS,startDate,startDate+dayInc),
                userDb.getDietTotal(userDb.COLUMN_CARBS,startDate+dayInc,startDate+dayInc*2),
                userDb.getDietTotal(userDb.COLUMN_CARBS,startDate+dayInc*2,startDate+dayInc*3),
                userDb.getDietTotal(userDb.COLUMN_CARBS,startDate+dayInc*3,startDate+dayInc*4),
                userDb.getDietTotal(userDb.COLUMN_CARBS,startDate+dayInc*4,startDate+dayInc*5),
                userDb.getDietTotal(userDb.COLUMN_CARBS,startDate+dayInc*5,startDate+dayInc*6)
        };

        Number[] seriesFat = {userDb.getDietTotal(userDb.COLUMN_FAT,startDate,startDate+dayInc),
                userDb.getDietTotal(userDb.COLUMN_FAT,startDate+dayInc,startDate+dayInc*2),
                userDb.getDietTotal(userDb.COLUMN_FAT,startDate+dayInc*2,startDate+dayInc*3),
                userDb.getDietTotal(userDb.COLUMN_FAT,startDate+dayInc*3,startDate+dayInc*4),
                userDb.getDietTotal(userDb.COLUMN_FAT,startDate+dayInc*4,startDate+dayInc*5),
                userDb.getDietTotal(userDb.COLUMN_FAT,startDate+dayInc*5,startDate+dayInc*6)
        };

        Number[] seriesProtein = {userDb.getDietTotal(userDb.COLUMN_PROTEIN,startDate,startDate+dayInc),
                userDb.getDietTotal(userDb.COLUMN_PROTEIN,startDate+dayInc,startDate+dayInc*2),
                userDb.getDietTotal(userDb.COLUMN_PROTEIN,startDate+dayInc*2,startDate+dayInc*3),
                userDb.getDietTotal(userDb.COLUMN_PROTEIN,startDate+dayInc*3,startDate+dayInc*4),
                userDb.getDietTotal(userDb.COLUMN_PROTEIN,startDate+dayInc*4,startDate+dayInc*5),
                userDb.getDietTotal(userDb.COLUMN_PROTEIN,startDate+dayInc*5,startDate+dayInc*6)
        };
        //turn above arrays into XYSeries
        XYSeries series1 = new SimpleXYSeries(
                Arrays.asList(seriesCarbs),          // SimpleXYSeries takes a List so turn array into a List
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, // Y_VALS_ONLY means use the element index as the x value
                "Carbs");                             // Set the display title of the series
        // same as above
        XYSeries series2 = new SimpleXYSeries(Arrays.asList(seriesFat), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Fat");
        XYSeries series3 = new SimpleXYSeries(Arrays.asList(seriesProtein), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Protein");

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
        LineAndPointFormatter series3Format = new LineAndPointFormatter(Color.BLUE, Color.BLUE, Color.TRANSPARENT, null);

        series3Format.setPointLabelFormatter(new PointLabelFormatter());
        //series3Format.configure(getApplicationContext(),
        //        R.xml.line_point_formatter_with_plf2);
        plot.addSeries(series3, series3Format);

        // reduce the number of range labels
        plot.setTicksPerRangeLabel(3);
        plot.getGraphWidget().setDomainLabelOrientation(-45);
    }

    public void displayMealsList(View view)
    {
        /*//fields to choose date range to query
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        //DatePicker datePicker = new DatePicker(this);
        Log.i("",Long.toString(startDate));
        Log.i("",Long.toString(endDate));*/

        //make list horizontally scrollable
        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(this);
        //use list view
        ListView listView = new ListView(this);
        listView.setCacheColorHint(00000000); //for scroll display; make scroll cache transparent
        //create custom formatted list based on columns
        List<String> formattedList = new ArrayList<String>();
        String headings = String.format("%-5s%-20s%-15s%-15s%-15s%-15s","ID","Name","Carbs(g)","Fat(g)","Protein(g)","Date");
        //String underline = String.format("%3s%15s%5s%5s%5s%10s","-","-","-","-","-","-");
        formattedList.add(headings);
        //formattedList.add(underline);
        final List<Meal> mealList = userDb.getAllMeals();
        for (int i=0; i< mealList.size(); i++)
        {
            //get record
            Meal record = mealList.get(i);
            //format date
            long idate = record.getDate();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String date = df.format(idate);
            //add record to list
            String sRecord = String.format("%-7d%-25s%-15d%-15d%-15d%-10s",
                    record.getID(),
                    record.getName(),
                    record.getCarbs(),
                    record.getFat(),
                    record.getProtein(),
                    date);
            formattedList.add(sRecord);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, formattedList);
        listView.setAdapter(arrayAdapter);
        //make items deletable from db on selection
        //toast to let user know they can delete an item
        Toast toast = Toast.makeText(this, "Select a meal to delete it", Toast.LENGTH_SHORT);
        toast.show();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(DietReportsActivity.this);
                alert.setMessage("Confirm delete?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        userDb.deleteMeal(mealList.get(position-1));
                        //Toast to let user know meal was deleted
                        Context context = DietReportsActivity.this;
                        CharSequence text = "Meal deleted";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        //refresh list
                        displayMealsList(getCurrentFocus());
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
