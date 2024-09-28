package za.co.tribalapp.tribal;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TextView;


public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        //Paragraph explaining each feature of the app
        TextView textView = new TextView(this);
        String helpText = "TriBal(TM) is a health and fitness app intended to help you, the user of any age, gender or fitness level" +
                " (unless a professional athlete), " +
                "achieve a balance in the three (as the name suggests) major areas of your health: \n\n" +
                "1. Diet - The macronutrients you eat (carbohydrates, fats, protein)\n" +
                "2. Exercise - The type you do (cardiovascular vs. strength/resistance)\n" +
                "3. Stress - The two main activities that contribute to this (work and sleep)\n" +
                "\n" +
                "You should find the app itself simple to use, with every feature and function being self-explanatory.\n" +
                "However, an explanation of the main features and functionality follows...\n" +
                "\n" +
                "Home Screen\n" +
                "-----------------" +
                "\nThe centre of the screen gives you 3 main logging options: " +
                "Log a meal (top), Log an exercise session (left), or Log a work or sleep session (right)." +
                "These will be explained in detail shortly. " +
                "\n\nThe Balance Index at the top is the heart of the app (pun intended), " +
                "telling you in a nutshell how balanced you are, based on the current week. " +
                "This will be explained further shortly." +
                "\n\nJust below this is the Advice button/screen, which gives you, in a bit less of a nutshell, feedback on how you can improve " +
                "your balance index based on the activities you have recorded so far. " +
                "\n\nThe Reports button/screen gives you a further, more detailed breakdown of the activities you have recorded; "+
                "mathematically, graphically and in a list, for each of the 3 areas." +
                "\n\n" +
                "Balance Index\n" +
                "------------------\n" +
                "This is based on a target range of average daily values for each area (diet, exercise and stress) derived from local and" +
                " international health publications by organizations such as WHO and the USDA.\n\n" +
                "The target ranges are as follows:\n" +
                "Diet: 100-300g Carbohydrates, 40-100g Fat, 40-100g Protein\n" +
                "Exercise: >=15 min Cardio, >=10 min Strength\n" +
                "Stress: <=8 hrs Work, >=8 hrs Sleep" +
                "\n\n" +
                "The app only requires you to monitor 6 days of the week, with the seventh being a designated rest day. This is chosen in " +
                " 'Settings', with the default day being Sunday." +
                "\n\n" +
                "Log Meal\n" +
                "------------\n" +
                "This is ideally done before eating a meal. Instructions:\n" +
                "-Begin typing the name of a food item in the 'Food name' text field. \n" +
                "-As you type, an auto-complete list suggests matching foods. These are linked to the official " +
                "USDA food database (hence the American names). \n" +
                "-After selecting a food item, click 'Next' on the keyboard and enter the portion size. The 'Nutritional information' fields populate " +
                "themselves for a default portion size of 1.\n" +
                "-Click 'Add to meal' to add the current food to the overall meal.\n" +
                "-After repeating the process for every food item, click 'Finish meal' to save the meal under a chosen name for " +
                "future recall.\n" +
                "-The next time you wish to log the same meal, simply click 'Add previous meal' at the top of the screen" +
                "and select it from the list." +
                "\n\n" +
                "Log Exercise\n" +
                "---------------\n" +
                "This gives you 3 options of recording a workout session beforehand. The first two are self-explanatory, using a stopwatch timer to " +
                "keep track of the session length.\n\nAfter stopping the timer, you will be presented with arguably the stand-out " +
                "feature of the app: a camera-operated heart rate monitor. This uses PPG imaging to detect a colour change to red in your fingertip; " +
                "signifying blood flow as your veins pulse. \n" +
                "\nInstructions:\n" +
                "-2 minutes after exercising, place your index finger on the lens of the device's camera, covering it completely but not too firmly as to cut off circulation.\n" +
                "-You should see the icon at the top of the screen flash red in unison with your pulse. \n" +
                "-Hold your finger in place for at least 10 seconds, " +
                "at which point your heart rate will show in the top-left corner.\n" +
                "-This can later be used to determine your HRR (Heart Rate Recovery); the time it takes for your heart rate to return to normal" +
                "\n-Press the Back button to finally record the session." +
                "\n\n" +
                "The third logging option, 'Pre-set duration', allows you to record your workout prematurely or in retrospect, instead of in real-time, " +
                " for whatever reason; be it forgetfulness or not having access to your device at the time of workout. Naturally your heart rate will not be measured in this case." +
                "\n\n" +
                "Log Stress\n" +
                "-------------\n" +
                "As with the other activities, this is done immediately before beginning a session; either" +
                " work (including homework and studying) or sleep.\n\n" +
                "The timing and heart rate measurement procedure is the same as with exercise logging, except that " +
                "work and sleep sessions replace cardio and strength sessions, and session lengths are measured in hours, " +
                "not minutes. \n" +
                "The pre-set option works as before." +
                "\n\n" +
                "This is the end of the Help manual. You know all you need to know to start using TriBal.";

        textView.setText(helpText);
        textView.setTextColor(getResources().getColor(R.color.white));
        ScrollView scrollView = (ScrollView)findViewById(R.id.scrollView);
        scrollView.addView(textView);
        Log.i("Help",helpText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.help, menu);
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
}
