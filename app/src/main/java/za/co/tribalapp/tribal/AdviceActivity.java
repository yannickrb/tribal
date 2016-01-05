package za.co.tribalapp.tribal;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class AdviceActivity extends Activity {

    //for intent payload
    static String string_avgCarbs = "avgCarbs";
    static String string_avgFat = "avgFat";
    static String string_avgProtein = "avgProtein";
    static String string_avgCardio = "avgCardio";
    static String string_avgStrength = "avgStrength";
    static String string_avgWork = "avgWork";
    static String string_avgSleep = "avgSleep";
    static String string_numDays = "numDays";
    static String string_balanceIndex = "balanceIndex";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice);

        //will add each point to list view
        ListView listView = (ListView)findViewById(R.id.listView_advice);
        listView.setCacheColorHint(00000000); //for scroll display; make scroll cache transparent
        List<String> adviceList = new ArrayList<String>();

        //Get balance index
        Intent mainActivity = getIntent();
        int balanceIndex = mainActivity.getIntExtra(string_balanceIndex,0);
        //update text view
        TextView balIndexView = (TextView) findViewById(R.id.textView_bal_index);
        balIndexView.setText(Integer.toString(balanceIndex) + " %");

        //get targets
        SharedPreferences profile = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        int carbTargetMin = profile.getInt("carbTargetMin",0);
        int carbTargetMax = profile.getInt("carbTargetMax",0);
        int fatTargetMin = profile.getInt("fatTargetMin",0);
        int fatTargetMax = profile.getInt("fatTargetMax",0);
        int proteinTargetMin = profile.getInt("proteinTargetMin",0);
        int proteinTargetMax = profile.getInt("proteinTargetMin",0);
        int cardioTargetMin = profile.getInt("cardioTargetMin",0);
        int strengthTargetMin = profile.getInt("strengthTargetMin",0);
        int workTargetMax = profile.getInt("workTargetMax",0);
        int sleepTargetMin = profile.getInt("sleepTargetMin",0);
        //get averages
        int avgCarbs = mainActivity.getIntExtra(string_avgCarbs,0);
        int avgFat = mainActivity.getIntExtra(string_avgFat,0);
        int avgProtein = mainActivity.getIntExtra(string_avgProtein,0);
        int avgCardio = mainActivity.getIntExtra(string_avgCardio,0);
        int avgStrength = mainActivity.getIntExtra(string_avgStrength,0);
        int avgWork = mainActivity.getIntExtra(string_avgWork,0);
        int avgSleep = mainActivity.getIntExtra(string_avgSleep,0);
        int numDays = mainActivity.getIntExtra(string_numDays,0);

        if (numDays != Integer.parseInt(profile.getString("pref_restDay","7"))) {
            //call getAdvice methods for different message for each metric
            // parameters: metric name, actual, target min, target max
            String advice = "";
            //Diet
            advice = getDietAdvice("carbohydrates",avgCarbs,carbTargetMin,carbTargetMax);
            adviceList.add(advice);
            advice = getDietAdvice("fat",avgFat,fatTargetMin,fatTargetMax);
            adviceList.add(advice);
            advice = getDietAdvice("protein",avgProtein,proteinTargetMin,proteinTargetMax);
            adviceList.add(advice);
            //Exercise
            advice = getExerciseAdvice("cardio exercise",avgCardio,cardioTargetMin);
            adviceList.add(advice);
            advice = getExerciseAdvice("strength exercise",avgStrength,strengthTargetMin);
            adviceList.add(advice);
            //Stress
            advice = getStressAdvice("Work",avgWork,workTargetMax); //No work min
            adviceList.add(advice);
            advice = getStressAdvice("Sleep",avgSleep,sleepTargetMin); //No sleep max
            adviceList.add(advice);
        }
        else //else indicate rest day
        {
            adviceList.add("Rest");
        }
        //display advice list to screen
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, adviceList);
        listView.setAdapter(arrayAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.advice, menu);
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

    public void reports(View view)
    {
        Intent reports = new Intent(this, ReportsActivity.class);
        startActivity(reports);
    }

    //Diet advice for either of the three nutrients
    public String getDietAdvice(String nutrient, int actual, int targetMin, int targetMax)
    {
        String advice = "";
        if (actual < targetMin)  //if eating less than target
        {
            int diff = targetMin - actual;
            if (diff >= 0.5*targetMin) //if >50% less
            {
                advice = "Eat much more " + nutrient;
            }
            else
            {
                advice = "Eat a bit more " + nutrient;
            }
        }
        else if (actual > targetMax) //eating more than target
        {
            int diff = actual - targetMax;
            if (diff >= 0.5*targetMax) //if >50% more
            {
                advice = "Eat much less " + nutrient;
            }
            else
            {
                advice = "Eat a bit less " + nutrient;
            }
        }
        else //eating enough
        {
            advice = "Eating right amount of " + nutrient + ", keep it up!";
        }
        return advice;
    }

    //Exercise advice for either of the two activities
    public String getExerciseAdvice(String activity, int actual, int targetMin)
    {
        String advice = "";
        if (actual < targetMin) //doing less than target
        {
            int diff = targetMin - actual;
            if (diff >= 0.5*targetMin)
            {
                advice = "Do much more " + activity;
            }
            else
            {
                advice = "Do a bit more " + activity;
            }
        }
        else //exercising enough
        {
            advice = "Doing right amount of " + activity + ", keep it up!";
        }
        return advice;
    }

    public String getStressAdvice(String activity, int actual, int target)
    {
        String advice = "";

        //Sleep
        if (activity.equals("Sleep")) {
            if (actual < target) //doing less than target
            {
                int diff = target - actual;
                if (diff >= 0.5 * target) {
                    advice = activity + " much more";
                } else {
                    advice = activity + " a bit more";
                }
            }
            else //doing enough
            {
                advice = activity + " hours within range, keep it up!";
            }
        }

        else //Work
        {
            if (actual > target) //doing more than target
            {
                int diff = actual - target;
                if (diff >= 0.5 * target) {
                    advice = activity + " much less";
                } else {
                    advice = activity + " a bit less";
                }
            } else //doing enough
            {
                advice = activity + " hours within range, keep it up!";
            }
        }
        return advice;
    }
}
