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


public class LogExerciseActivity extends AppCompatActivity {

    //initialize globals
    UserDbHandler userDb = new UserDbHandler(this);
    String sessionName = "";
    String sessionType = "";
    int sessionDuration = 0;
    ExerciseSession exerciseSession;
    static final int HEART_RATE_REQUEST = 1;
    int heartRate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_exercise);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.log_exercise, menu);
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

    //this is called when activity returns result
    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        //Make sure request was successful
        if (resultCode == RESULT_OK)
        {
            sessionDuration = (int) Math.ceil( (double)data.getIntExtra("sessionDuration", 0) / 60); //convert secs to mins

            if (requestCode == 1) //cardio
            {
                //HRR
                //send intent to HeartRateMonitor activity
                Intent rateIntent = new Intent(this, HeartRateMonitorActivity.class);
                startActivityForResult(rateIntent, 3);
            }
            else if (requestCode == 2) //strength
            {
                Intent rateIntent = new Intent(this, HeartRateMonitorActivity.class);
                startActivityForResult(rateIntent, 4);
            }
            else
            {
                //get heart rate back from Heart Rate Monitor activity
                heartRate = data.getIntExtra("heartRate", 0);

                //only add to db if heart rate obtained
                if (heartRate > 0) {
                    if (requestCode == 3) //Cardio
                    {
                        userDb.addExerciseSession(new ExerciseSession(sessionName, "Cardio", sessionDuration, heartRate));
                    } else if (requestCode == 4) //Strength
                    {
                        userDb.addExerciseSession(new ExerciseSession(sessionName, "Strength", sessionDuration, heartRate));
                    }
                    //Toast to let user know session was recorded
                    Context context = getBaseContext();
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

    public void timeCardio(View view)
    {
        //Prompt for activity name
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        //linear layouts for each field
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final TextView textName = new TextView(this);
        textName.setText("Name of activity: \n(e.g. Walk, Jog, Soccer)");
        layout.addView(textName);
        // add text field to layout
        final EditText sName = new EditText(this);
        layout.addView(sName);
        //set view to linear layout
        alert.setView(layout);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //extract session name
                sessionName = sName.getText().toString();
                //send intent to Timer activity to get session duration
                Intent sendIntent = new Intent(getBaseContext(), TimerActivity.class);
                startActivityForResult(sendIntent,1);
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

    public void timeStrength(View view)
    {
        //Prompt for activity name
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        //linear layouts for each field
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final TextView textName = new TextView(this);
        textName.setText("Name of activity: \n(e.g. Body-weight, Weights, Pilates)");
        layout.addView(textName);
        // add text field to layout
        final EditText sName = new EditText(this);
        layout.addView(sName);
        //set view to linear layout
        alert.setView(layout);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //extract session name
                sessionName = sName.getText().toString();

                //send intent to Timer activity to get session duration
                Intent sendIntent = new Intent(getBaseContext(), TimerActivity.class);
                startActivityForResult(sendIntent,2);
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

    //Pre-set method
    public void logPreset(View view)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        //linear layouts for each field
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        //Prompt for activity name
        final TextView textName = new TextView(this);
        textName.setTextSize(18);
        textName.setText("Name of activity: \n(e.g. Jog, Soccer, Pilates, Gym)");
        layout.addView(textName);
        // add text field to layout
        final EditText sName = new EditText(this);
        layout.addView(sName);

        //Prompt for session type - radio buttons
        final TextView textType = new TextView(this);
        textType.setTextSize(18);
        textType.setText("Type of exercise:");
        layout.addView(textType);
        final RadioGroup radioType = new RadioGroup(this);
        radioType.setOrientation(LinearLayout.HORIZONTAL);
        final RadioButton radioCardio = new RadioButton(this);
        radioCardio.setText("Cardio");
        final RadioButton radioStrength = new RadioButton(this);
        radioStrength.setText("Strength");
        radioType.addView(radioCardio);
        radioType.addView(radioStrength);
        layout.addView(radioType);

        //Prompt for session duration
        final TextView textDuration = new TextView(this);
        textDuration.setText("Duration in minutes:");
        textDuration.setTextSize(18);
        layout.addView(textDuration);
        final EditText sDuration = new EditText(this);
        sDuration.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(sDuration);

        //set view to linear layout
        alert.setView(layout);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //extract session name
                sessionName = sName.getText().toString();
                //extract type
                if (radioCardio.isChecked())
                {
                    sessionType = "Cardio";
                }
                else
                {
                    sessionType = "Strength";
                }
                //extract duration
                sessionDuration = Integer.parseInt(sDuration.getText().toString());
                //finally add object (session) to db
                userDb.addExerciseSession(new ExerciseSession(sessionName,sessionType,sessionDuration,heartRate));
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
