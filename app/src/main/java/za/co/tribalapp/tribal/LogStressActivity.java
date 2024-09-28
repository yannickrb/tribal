package za.co.tribalapp.tribal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


public class LogStressActivity extends AppCompatActivity {

    //initialize globals
    UserDbHandler userDb = new UserDbHandler(this);
    String sessionType = "";
    int sessionDuration = 0;
    int heartRate = 0;
    StressSession stressSession = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_stress);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.log_stress, menu);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // Ensure request was successful
        if (resultCode == RESULT_OK)
        {
            sessionDuration = (int) Math.ceil( (double)data.getIntExtra("sessionDuration",0)/3600); //convert secs to hrs
            // Check request being responded to
            if (requestCode == 1)
            {
                //HRR
                //send intent to HeartRateMonitor activity
                Intent rateIntent = new Intent(this, HeartRateMonitorActivity.class);
                startActivityForResult(rateIntent, 3);
            }
            else if (requestCode == 2)
            {
                //HRR
                //send intent to HeartRateMonitor activity
                Intent rateIntent = new Intent(this, HeartRateMonitorActivity.class);
                startActivityForResult(rateIntent, 4);
            }
            else {
                //get heart rate back from Heart Rate Monitor activity
                heartRate = data.getIntExtra("heartRate", 0);

                //only add to db if heart rate obtained
                if (heartRate > 0)
                {
                    if (requestCode == 3) //Work
                    {
                        // add object (session) to db
                        userDb.addStressSession(new StressSession("Work", sessionDuration, heartRate));
                    }
                    else if (requestCode == 4) //Sleep
                    {
                        userDb.addStressSession(new StressSession("Sleep", sessionDuration, heartRate));
                    }
                    //Toast to let user know session was recorded
                    Context context = getApplicationContext();
                    CharSequence text = "Session recorded";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        }
    }

    //override key press actions
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        //re-calculate balance index on main screen when back key pressed
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            Intent main = new Intent(this, MainActivity.class);
            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(main);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void timeWork(View view)
    {
        //send intent to Timer activity to get session duration
        Intent sendIntent = new Intent(this, TimerActivity.class);
        startActivityForResult(sendIntent,1);
    }

    public void timeSleep(View view)
    {
        //send intent to Timer activity to get session duration
        Intent sendIntent = new Intent(this, TimerActivity.class);
        startActivityForResult(sendIntent,2);
    }

    public void logPreset(View view)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        //linear layouts for each field
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        //Prompt for session type - radio buttons
        final TextView textType = new TextView(this);
        textType.setTextSize(18);
        textType.setText("Type of session:");
        layout.addView(textType);
        final RadioGroup radioType = new RadioGroup(this);
        radioType.setOrientation(LinearLayout.HORIZONTAL);
        final RadioButton radioWork = new RadioButton(this);
        radioWork.setText("Work");
        final RadioButton radioSleep = new RadioButton(this);
        radioSleep.setText("Sleep");
        radioType.addView(radioWork);
        radioType.addView(radioSleep);
        layout.addView(radioType);

        //Prompt for session duration
        final TextView textDuration = new TextView(this);
        textDuration.setText("Enter duration in hours:");
        textDuration.setTextSize(18);
        layout.addView(textDuration);
        final EditText sDuration = new EditText(this);
        sDuration.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(sDuration);

        //set view to linear layout
        alert.setView(layout);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //extract type
                if (radioWork.isChecked())
                {
                    sessionType = "Work";
                }
                else if (radioSleep.isChecked())
                {
                    sessionType = "Sleep";
                }
                else
                {
                    //prompt user to choose one
                }
                //extract duration
                sessionDuration = Integer.parseInt(sDuration.getText().toString());
                //finally add object (session) to db
                stressSession = new StressSession(sessionType,sessionDuration,heartRate);
                userDb.addStressSession(stressSession);
                //Toast to let user know session was recorded
                Context context = getApplicationContext();
                CharSequence text = "Session recorded";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Cancelled
            }
        });
        //show dialog
        alert.show();
    }
}
