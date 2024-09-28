package za.co.tribalapp.tribal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class LogMealActivity extends AppCompatActivity
{
    //global user database object
    UserDbHandler userDb = new UserDbHandler(this);
    //global food database object
    FoodDbHandler foodDb = new FoodDbHandler(this);
    //global nutrient values to be incremented for each meal
    float carbs = 0;
    float fat = 0;
    float protein = 0;
    float totalCarbs = 0;
    float totalFat = 0;
    float totalProtein = 0;
    //other variables
    String foodName = "";
    int mealID = 0;
    int amount = 0;
    AutoCompleteTextView editText_foodName;
    EditText editText_amount;
    EditText editText_carbs;
    EditText editText_fat;
    EditText editText_protein;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_meal);
        //inititalize auto-complete text view
        editText_foodName = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView_food_name);
        Resources res = getResources();
        int colour = res.getColor(android.R.color.black);
        editText_foodName.setTextColor(colour);
        //search Food db for similar foods as user types
        List<String> foodList = foodDb.getAllFoodNames(this); //send this context as parameter for toast message
        //create adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item, foodList);
        editText_foodName.setThreshold(1);
        editText_foodName.setAdapter(adapter);
        //when food selected
        editText_foodName.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //get name from text field
                foodName = editText_foodName.getText().toString();
                //populate other fields based on this
                //Extract food unit from db - not doing this for now, american units can be confusing
                //String portionUnit = foodDb.getUnit(foodName);
                String portionUnit = "servings";
                //Update textView
                TextView textView_unit = (TextView)findViewById(R.id.textView_portion_unit);
                textView_unit.setText(portionUnit);
                //Extract common measure (grams) of unit from db
                int cm = foodDb.getCommonMeasure(foodName);
                //Calculate nutrition values: nutrient content given per 100g; must convert to appropriate unit as: C = (N*CM)/100; where N is per 100g, CM is common measure grams
                final Food food = foodDb.getFood(foodName);
                final int baseCarbs = (int) food.carbs * cm / 100;
                final int baseFat = (int) food.fat * cm / 100;
                final int baseProtein = (int) food.protein * cm / 100;
                editText_carbs = (EditText) findViewById(R.id.editText_carbs);
                editText_fat = (EditText) findViewById(R.id.editText_fat);
                editText_protein = (EditText) findViewById(R.id.editText_protein);
                editText_carbs.setText(Float.toString(baseCarbs));
                editText_fat.setText(Float.toString(baseFat));
                editText_protein.setText(Float.toString(baseProtein));

                //will multiply each nutrient by portion amount
                editText_amount = (EditText) findViewById(R.id.editText_portion_amount);
                editText_amount.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        //only if field isn't blank
                        if (s.length() > 0) {
                            amount = Integer.parseInt(editText_amount.getText().toString());
                            carbs = amount * baseCarbs;
                            fat = amount * baseFat;
                            protein = amount * baseProtein;

                            editText_carbs.setText(Float.toString(carbs));
                            editText_fat.setText(Float.toString(fat));
                            editText_protein.setText(Float.toString(protein));
                        }
                        else
                        {
                            editText_carbs.setText("");
                            editText_fat.setText("");
                            editText_protein.setText("");
                        }
                    }
                });
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.log_meal, menu);
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

    public void addToMeal(View view)
    {
        if (editText_foodName.length() > 0) {
            //accumulate values
            totalCarbs += carbs;
            totalFat += fat;
            totalProtein += protein;
            //Toast to let user know food was added

            Context context = getApplicationContext();
            CharSequence text = "Food added";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        //reset totals
        carbs = 0;
        fat = 0;
        protein = 0;
        //Clear text fields
        try {
            editText_foodName.setText("");
        }
        catch (NullPointerException e){}
        try {
            editText_amount.setText("");
        }
        catch (NullPointerException e){}
        try {
            editText_carbs.setText("");
        }
        catch (NullPointerException e){}
        try {
            editText_fat.setText("");
        }
        catch (NullPointerException e){}
        try {
            editText_protein.setText("");
        }
        catch (NullPointerException e){}
    }

    public void saveMeal(View view)
    {
        /*Get user input: save meal as...*/
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Save meal as: ");
        alert.setMessage("E.g. Cheese and tomato sandwich");
        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                String mealName = input.getText().toString();
                userDb.addMeal(new Meal(mealName, (int)totalCarbs, (int)totalFat, (int)totalProtein)); //add meal to user db
                carbs = fat = protein = 0; //reset meal nutrient values
                //Toast to let user know meal was recorded
                Context context = getApplicationContext();
                CharSequence text = "Meal recorded";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                //return to main menu
                finish();
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Cancelled.
            }
        });
        alert.show();
    }

    //List of all previously recorded meals
    public void previousMeals(View view)
    {
        //make list horizontally scrollable
        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(this);
        final ListView listView = new ListView(this);
        listView.setCacheColorHint(00000000); //for scroll display; make scroll cache transparent
        //create custom formatted list based on columns
        List<String> formattedList = new ArrayList<String>();
        String headings = String.format("%-5s%-20s%-15s%-15s%-15s%-15s","ID","Name","Carbs(g)","Fat(g)","Protein(g)","Date");
        //String underline = String.format("%3s%15s%5s%5s%5s%10s","-","-","-","-","-","-");
        formattedList.add(headings);
        //formattedList.add(underline);
        List<Meal> mealList = userDb.getAllMeals();
        for (int i=0; i< mealList.size(); i++)
        {
            //get record
            Meal record = mealList.get(i);
            mealID = record.getID();
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
        horizontalScrollView.addView(listView);
        setContentView(horizontalScrollView);
        //when meal selected
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                //ask to confirm add
                AlertDialog.Builder alert = new AlertDialog.Builder(LogMealActivity.this);
                alert.setMessage("Confirm to add?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Object object = (listView.getItemAtPosition(position));
                        String[] smeal = object.toString().split(" ");
                        mealID = Integer.parseInt(smeal[0]);
                        //add meal to user db
                        Meal meal = userDb.getMeal(mealID);
                        userDb.addMeal(meal);
                        //Toast to let user know meal was recorded
                        Context context = getApplicationContext();
                        CharSequence text = "Meal recorded";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        //return to main menu
                        finish();
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                alert.show();
            }
        });
    }
}
