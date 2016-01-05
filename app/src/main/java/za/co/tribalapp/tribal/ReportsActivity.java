package za.co.tribalapp.tribal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class ReportsActivity extends ActionBarActivity {

    //Initialise globals
    UserDbHandler userDb = new UserDbHandler(this);
    FoodDbHandler foodDb = new FoodDbHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.reports, menu);
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

    public void dietReports(View view)
    {
        //receive intent from Main activity and send to relevant Reports activity
        Intent main = getIntent();
        int dietIndex = main.getIntExtra("dietIndex",0);
        int carbIndex = main.getIntExtra("carbIndex",0);
        int fatIndex = main.getIntExtra("fatIndex",0);
        int proteinIndex = main.getIntExtra("proteinIndex",0);
        long startDate = main.getLongExtra("startDate",0);
        long endDate = main.getLongExtra("endDate",0);
        Intent dietReports = new Intent(this, DietReportsActivity.class);
        dietReports.putExtra("dietIndex",dietIndex);
        dietReports.putExtra("carbIndex",carbIndex);
        dietReports.putExtra("fatIndex",fatIndex);
        dietReports.putExtra("proteinIndex",proteinIndex);
        dietReports.putExtra("startDate",startDate);
        dietReports.putExtra("endDate",endDate);
        startActivity(dietReports);
    }

    public void exerciseReports(View view)
    {
        //receive intent from Main activity and send to relevant Reports activity
        Intent main = getIntent();
        int exerciseIndex = main.getIntExtra("exerciseIndex",0);
        int cardioIndex = main.getIntExtra("cardioIndex",0);
        int strengthIndex = main.getIntExtra("strengthIndex",0);
        long startDate = main.getLongExtra("startDate",0);
        long endDate = main.getLongExtra("endDate",0);
        Intent exerciseReports = new Intent(this, ExerciseReportsActivity.class);
        exerciseReports.putExtra("exerciseIndex",exerciseIndex);
        exerciseReports.putExtra("cardioIndex",cardioIndex);
        exerciseReports.putExtra("strengthIndex",strengthIndex);
        exerciseReports.putExtra("startDate",startDate);
        exerciseReports.putExtra("endDate",endDate);
        startActivity(exerciseReports);
    }

    public void stressReports(View view)
    {
        //receive intent from Main activity and send to relevant Reports activity
        Intent main = getIntent();
        int stressIndex = main.getIntExtra("stressIndex",0);
        int workIndex = main.getIntExtra("workIndex",0);
        int sleepIndex = main.getIntExtra("sleepIndex",0);
        long startDate = main.getLongExtra("startDate",0);
        long endDate = main.getLongExtra("endDate",0);
        Intent stressReports = new Intent(this, StressReportsActivity.class);
        stressReports.putExtra("stressIndex",stressIndex);
        stressReports.putExtra("workIndex",workIndex);
        stressReports.putExtra("sleepIndex",sleepIndex);
        stressReports.putExtra("startDate",startDate);
        stressReports.putExtra("endDate",endDate);
        startActivity(stressReports);
    }
}
