

package za.co.tribalapp.tribal;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MainActivity extends Activity
{
    //create user database
    UserDbHandler userDb = new UserDbHandler(this);
    //declare food database
    FoodDbAdapter foodDb = new FoodDbAdapter(this);
    //Shared preferences key-value set
    SharedPreferences profile;
    //Balance algorithm object
    BalanceAlgorithm balanceAlgorithm;
    //Static payload Strings to be sent to other activities
    static String string_startDate = "startDate";
    static String string_endDate = "endDate";
    static String string_numDays = "numDays";
    static String string_dietIndex = "dietIndex";
    static String string_exerciseIndex = "exerciseIndex";
    static String string_stressIndex = "stressIndex";
    static String string_balanceIndex = "balanceIndex";
    static String string_avgCarbs = "avgCarbs";
    static String string_avgFat = "avgFat";
    static String string_avgProtein = "avgProtein";
    static String string_avgCardio = "avgCardio";
    static String string_avgStrength = "avgStrength";
    static String string_avgWork = "avgWork";
    static String string_avgSleep = "avgSleep";
    static String string_restDay = "restDay";
    static String string_firstDay = "firstDay";
    //Payload data itself
    static long startDate = 0;
    static long endDate = 0;
    static int numDays = 0;
    static int dietIndex = 0;
    static int carbIndex = 0;
    static int fatIndex = 0;
    static int proteinIndex = 0;
    static int exerciseIndex = 0;
    static int cardioIndex = 0;
    static int strengthIndex = 0;
    static int stressIndex = 0;
    static int workIndex = 0;
    static int sleepIndex = 0;
    static int balanceIndex = 0;
    static int avgCarbs = 0;
    static int avgFat = 0;
    static int avgProtein = 0;
    static int avgCardio = 0;
    static int avgStrength = 0;
    static int avgWork = 0;
    static int avgSleep = 0;
    static int restDay = 7;
    static int firstDay = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //remove notification bar if API <=10 for more space
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1)
        {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_main);

        //create and open food database early
        foodDb.launchCreateDatabase();
        foodDb.open();
        foodDb.close();
        //set daily diet, exercise & stress target values
        int carbTargetMin = 100;
        int carbTargetMax = 300;
        int fatTargetMin = 40;
        int fatTargetMax = 100;
        int proteinTargetMin = 40;
        int proteinTargetMax = 100;
        int cardioTargetMin = 15; //exercise targets are averages, ideally 3 days a week each
        int strengthTargetMin = 10;
        int workTargetMax = 8;
        int sleepTargetMin = 8;
        //store values in shared preferences file
        //initialize key value sets
        profile = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        profile.edit().putInt("carbTargetMin",carbTargetMin).apply();
        profile.edit().putInt("carbTargetMax",carbTargetMax).apply();
        profile.edit().putInt("fatTargetMin",fatTargetMin).apply();
        profile.edit().putInt("fatTargetMax",fatTargetMax).apply();
        profile.edit().putInt("proteinTargetMin",proteinTargetMin).apply();
        profile.edit().putInt("proteinTargetMax",proteinTargetMax).apply();
        profile.edit().putInt("cardioTargetMin",cardioTargetMin).apply();
        profile.edit().putInt("strengthTargetMin",strengthTargetMin).apply();
        profile.edit().putInt("workTargetMax",workTargetMax).apply();
        profile.edit().putInt("sleepTargetMin",sleepTargetMin).apply();
        restDay = Integer.parseInt(profile.getString("pref_restDay","7"));
        if (restDay == 7)
        {
            firstDay = 1;
        }
        else {
            firstDay = restDay + 1;
        }

        //determine if rest day
        int currentDay = getDayOfWeek();
        if (currentDay != restDay)
        {
            //Calculate balance index
            balanceAlgorithm = new BalanceAlgorithm(profile.getInt("carbTargetMin",0),
                    profile.getInt("carbTargetMax",0),
                    profile.getInt("fatTargetMin",0),
                    profile.getInt("fatTargetMax",0),
                    profile.getInt("proteinTargetMin",0),
                    profile.getInt("proteinTargetMax",0),
                    profile.getInt("cardioTargetMin",0),
                    profile.getInt("strengthTargetMin",0),
                    profile.getInt("workTargetMax",0),
                    profile.getInt("sleepTargetMin",0));
            calcIndexes();
            //update bal index text view
            TextView balIndexView = (TextView) findViewById(R.id.textView_bal_index);
            balIndexView.setText(Integer.toString(balanceIndex) + " %");
        }
        else //else indicate rest day
        {
            TextView balIndexIsView = (TextView) findViewById(R.id.textView_bal_index_is);
            balIndexIsView.setText("Rest day");
            TextView balIndexView = (TextView) findViewById(R.id.textView_bal_index);
            balIndexView.setText("");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    //override onResume to re-calculate indexes each time
    @Override
    protected void onResume() {
        super.onResume();

        try {
            calcIndexes();
        } catch (Exception e) {
            Log.e("onResume","onResume error");
        }
    }

    //determine day of the week (days elapsed) and week start & current day (end) in millis
    //To-do: do it properly
    public int getDayOfWeek()
    {
        Calendar c = Calendar.getInstance();
        //c.setFirstDayOfWeek(Calendar.MONDAY);
        int currentDay = c.get(Calendar.DAY_OF_WEEK) - 1;

        if (currentDay == 0) {
            currentDay = 7;
        }
        numDays = currentDay;

        //set first day to user defined day
        /*switch (firstDay)
        {
            case 1: c.setFirstDayOfWeek(Calendar.MONDAY); break;
            case 2: c.setFirstDayOfWeek(Calendar.TUESDAY); break;
            case 3: c.setFirstDayOfWeek(Calendar.WEDNESDAY); break;
            case 4: c.setFirstDayOfWeek(Calendar.THURSDAY); break;
            case 5: c.setFirstDayOfWeek(Calendar.FRIDAY); break;
            case 6: c.setFirstDayOfWeek(Calendar.SATURDAY); break;
            case 7: c.setFirstDayOfWeek(Calendar.SUNDAY); break;
        }*/
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); //get time at midnight of first day, default Sunday midnight, i.e. Monday
        c.set(Calendar.HOUR_OF_DAY, 0);
        startDate = c.getTimeInMillis();
        endDate = userDb.getCurrentDate();

        return currentDay;
    }

    //daily averages for each index based on totals over x number of days
    public void calcIndexes()
    {
        //diet
        avgCarbs = userDb.getDietTotal(userDb.COLUMN_CARBS, startDate, endDate) / numDays;
        avgFat = userDb.getDietTotal(userDb.COLUMN_FAT, startDate, endDate) / numDays;
        avgProtein = userDb.getDietTotal(userDb.COLUMN_PROTEIN, startDate, endDate) / numDays;
        //exercise
        avgCardio = userDb.getExerciseTotal("Cardio", startDate, endDate) / numDays;
        avgStrength = userDb.getExerciseTotal("Strength", startDate, endDate) / numDays;
        //stress
        avgWork = userDb.getStressTotal("Work", startDate, endDate) / numDays;
        avgSleep = userDb.getStressTotal("Sleep", startDate, endDate) / numDays;
        //indexes
        balanceAlgorithm.calcDietIndex(avgCarbs, avgFat, avgProtein);
        dietIndex = balanceAlgorithm.getDietIndex();
        carbIndex = balanceAlgorithm.getCarbIndex();
        fatIndex = balanceAlgorithm.getFatIndex();
        proteinIndex = balanceAlgorithm.getProteinIndex();
        balanceAlgorithm.calcExerciseIndex(avgCardio, avgStrength);
        exerciseIndex = balanceAlgorithm.getExerciseIndex();
        cardioIndex = balanceAlgorithm.getCardioIndex();
        strengthIndex = balanceAlgorithm.getStrengthIndex();
        balanceAlgorithm.calcStressIndex(avgWork, avgSleep);
        stressIndex = balanceAlgorithm.getStressIndex();
        workIndex = balanceAlgorithm.getWorkIndex();
        sleepIndex = balanceAlgorithm.getSleepIndex();
        balanceAlgorithm.calcBalanceIndex();
        balanceIndex = balanceAlgorithm.getBalIndex();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String sdate = df.format(startDate);
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        String edate = df2.format(endDate);
        Log.i("currentDay",Integer.toString(getDayOfWeek()));
        Log.i("startDate",(sdate));
        Log.i("endDate",(edate));
    }

    //launch "Eat" activity
    public void logMeal(View view)
    {
        // Do something in response to button
        Intent eat = new Intent(this, LogMealActivity.class);
        startActivity(eat);
    }

    public void logExercise(View view)
    {
        Intent exercise = new Intent(this, LogExerciseActivity.class);
        startActivity(exercise);
    }

    public void logStress(View view)
    {
        Intent exercise = new Intent(this, LogStressActivity.class);
        startActivity(exercise);
    }

    public void advice(View view)
    {
        //send balance index
        Intent advice = new Intent(this, AdviceActivity.class);
        advice.putExtra(string_balanceIndex,balanceIndex);
        advice.putExtra(string_avgCarbs,avgCarbs);
        advice.putExtra(string_avgFat,avgFat);
        advice.putExtra(string_avgProtein,avgProtein);
        advice.putExtra(string_avgCardio,avgCardio);
        advice.putExtra(string_avgStrength,avgStrength);
        advice.putExtra(string_avgWork,avgWork);
        advice.putExtra(string_avgSleep,avgSleep);
        advice.putExtra(string_numDays, numDays);
        startActivity(advice);
    }

    public void reports(View view)
    {
        //send values to reports activity
        Intent reports = new Intent(this, ReportsActivity.class);
        reports.putExtra("dayOfWeek",getDayOfWeek());
        reports.putExtra("dietIndex",dietIndex);
        reports.putExtra("exerciseIndex",exerciseIndex);
        reports.putExtra("stressIndex",stressIndex);
        reports.putExtra("numDays",numDays);
        reports.putExtra("startDate",startDate);
        reports.putExtra("endDate",endDate);
        //send individual indexes
        reports.putExtra("carbIndex",carbIndex);
        reports.putExtra("fatIndex",fatIndex);
        reports.putExtra("proteinIndex",proteinIndex);
        reports.putExtra("cardioIndex",cardioIndex);
        reports.putExtra("strengthIndex",strengthIndex);
        reports.putExtra("workIndex",workIndex);
        reports.putExtra("sleepIndex", sleepIndex);
        startActivity(reports);
    }

    public void help(View view)
    {
        Intent help = new Intent(this, HelpActivity.class);
        startActivity(help);
    }

    public void settings(View view)
    {
        Intent settings = new Intent(this, SettingsActivity.class);
        startActivity(settings);
    }
}
