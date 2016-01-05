package za.co.tribalapp.tribal;

/**
 * Yannick Butorano
 * 9/10/2014
 */

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivityTest extends ActivityUnitTestCase<MainActivity> {

    private Intent mainIntent;
    //initialize intent-signalling UI components (buttons)
    private ImageButton button_eat;
    private ImageButton button_exercise;
    private ImageButton button_work_sleep;
    private Button button_advice;
    private ImageButton button_settings;
    private Button button_reports;
    private ImageButton button_help;
    private TextView textView_bal_index_is;
    private TextView textView_bal_index;

    //constructor called by test runner to instantiate test class
    public MainActivityTest() {
        super(MainActivity.class);
    }

    //this is run first, before other methods; analogous to onCreate
    //define instance variables that store state of test fixture
    //create and save reference to instance of Activity being tested
    //obtain reference to any UI components in Activity to test
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        //initialize activity to be tested
        mainIntent = new Intent(getInstrumentation().getTargetContext(), MainActivity.class);
    }

    //verify test activity launched
    @MediumTest
    public void testActivityLaunched() {
        startActivity(mainIntent,null,null);
        assertNotNull("MainActivity is null", getActivity());
    }


    //verify UI components
    @MediumTest
    public void testUIComponentsInitialized() {
        startActivity(mainIntent,null,null);
        //initialize UI components
        button_eat = (ImageButton) getActivity().findViewById(R.id.button_eat);
        button_exercise = (ImageButton) getActivity().findViewById(R.id.button_exercise);
        button_work_sleep = (ImageButton) getActivity().findViewById(R.id.button_work_sleep);
        button_advice = (Button) getActivity().findViewById(R.id.button_advice);
        button_settings = (ImageButton) getActivity().findViewById(R.id.button_settings);
        button_reports = (Button) getActivity().findViewById(R.id.button_reports);
        button_help = (ImageButton) getActivity().findViewById(R.id.button_help);
        textView_bal_index_is = (TextView) getActivity().findViewById(R.id.textView_bal_index_is);
        textView_bal_index = (TextView) getActivity().findViewById(R.id.textView_bal_index);
        //test
        assertNotNull("button_eat is null", button_eat);
        assertNotNull("button_exercise is null", button_exercise);
        assertNotNull("button_work/sleep is null", button_work_sleep);
        assertNotNull("button_advice is null", button_advice);
        assertNotNull("button_settings is null", button_settings);
        assertNotNull("button_reports is null", button_reports);
        assertNotNull("button_help is null", button_help);
        assertNotNull("textView_bal_index_is is null", textView_bal_index_is);
        assertNotNull("textView_bal_index is null", textView_bal_index);
    }

    //verify launch of other activities
    @MediumTest
    public void testNextActivityLaunched()
    {
        startActivity(mainIntent, null, null);
        //Initialize "Advice" button
        button_advice = (Button) getActivity().findViewById(R.id.button_advice);
        //Must explicitly click button as this is an isolated ActivityUnitTestCase
        button_advice.performClick();
        //Intent for next started activity
        final Intent adviceIntent = getStartedActivityIntent();
        //Verify intent is not null
        assertNotNull("Intent is null", adviceIntent);
    }

    //verify correct payload data in intents
    @MediumTest
    public void testNextActivityCorrectPayload()
    {
        startActivity(mainIntent, null, null);
        //Initialize "Advice" button
        button_advice = (Button) getActivity().findViewById(R.id.button_advice);
        //Must explicitly click button as this is an isolated ActivityUnitTestCase
        button_advice.performClick();
        //Intent for next activity
        final Intent adviceIntent = getStartedActivityIntent();
        //Get payload data from next activity
        final int payload_bal = adviceIntent.getIntExtra(AdviceActivity.string_balanceIndex,0);
        //Verify payload data
        assertEquals("Payload_bal is empty", MainActivity.balanceIndex, payload_bal);
        //Repeat for every payload
        //Get payload data from next activity
        final int payload_carbs = adviceIntent.getIntExtra(AdviceActivity.string_avgCarbs,0);
        assertEquals("Payload_carbs is empty", MainActivity.avgCarbs, payload_carbs);
        final int payload_fat = adviceIntent.getIntExtra(AdviceActivity.string_avgFat,0);
        assertEquals("Payload is empty", MainActivity.avgFat, payload_fat);
        final int payload_protein = adviceIntent.getIntExtra(AdviceActivity.string_avgProtein,0);
        assertEquals("Payload is empty", MainActivity.avgProtein, payload_protein);
        final int payload_numDays = adviceIntent.getIntExtra(AdviceActivity.string_numDays,0);
        assertEquals("Payload is empty", MainActivity.numDays, payload_numDays);
    }
}