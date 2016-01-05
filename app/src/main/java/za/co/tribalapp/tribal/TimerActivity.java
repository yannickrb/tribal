package za.co.tribalapp.tribal;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ToggleButton;


public class TimerActivity extends ActionBarActivity {

    //initialize globals
    ToggleButton button;
    Chronometer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        //toggle button
        button = (ToggleButton) findViewById(R.id.toggleButton_start_stop);
        //timer
        timer = (Chronometer) findViewById(R.id.chronometer);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.timer, menu);
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

    public void timeSession(View view)
    {
        //start timing when start/stop button pressed
        if (button.isChecked()) {
            //Use simple Chronometer for timing
            //set reference time
            timer.setBase(SystemClock.elapsedRealtime());
            timer.start();
        }
        //stop timing when pressed again
        else {
            timer.stop();
            long ltime = SystemClock.elapsedRealtime() - timer.getBase(); //duration in ms
            Log.i("ltime",Long.toString(ltime));
            int time = (int) (ltime/1000); //convert to seconds
            //send time back to calling activity
            Intent timeIntent = new Intent();
            timeIntent.putExtra("sessionDuration", time);
            setResult(RESULT_OK, timeIntent);
            finish();
        }
    }
}
