package za.co.tribalapp.tribal;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yannick on 2014/09/07.
 */
public class FoodDbHandler extends SQLiteOpenHelper
{
    private static String TAG = "FoodDbHandler"; // Tag just for the LogCat window
    //destination path (location) of our database on device
    public static final int DB_VERSION = 2;
    private static String DB_PATH = "";
    private static String DB_NAME ="FoodDb.db";// Database name
    private SQLiteDatabase mDataBase;
    public static final String TABLE_FOODS = "foods";
    private final Context mContext;
    public static final String COLUMN_FOOD_ID = "food_id";
    public static final String COLUMN_FOOD_NAME = "food_name";
    public static final String COLUMN_CARBS = "carbs";
    public static final String COLUMN_FAT = "fat";
    public static final String COLUMN_PROTEIN = "protein";
    public static final String COLUMN_CMEASURE = "c_measure";
    public static final String COLUMN_UNIT = "unit";

    public FoodDbHandler(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);//
        /*if(android.os.Build.VERSION.SDK_INT >= 17){
            DB_PATH = context.getDatabasePath(FoodDbHandler.DB_NAME).toString();
        }
        else
        {*/
            //context.getPackageName();
            DB_PATH = "/data/data/za.co.tribalapp.tribal/databases/";
            //DB_PATH = context.getFilesDir().getPath();
        //}
        this.mContext = context;
    }

    public void createDb() throws IOException
    {
        //If database doesn't exist, copy it from assets
        boolean mDataBaseExist = checkDb();
        if(!mDataBaseExist)
        {
            this.getReadableDatabase();
            this.close();
            try
            {
                //Copy the database from assets
                copyDbFromAssets();
                Log.e(TAG, "createDatabase database created");
            }
            catch (IOException mIOException)
            {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }
    //Check that the database exists here: /data/data/TriBal/databases/DB_NAME
    private boolean checkDb()
    {
        File dbFile = new File(DB_PATH + DB_NAME);
        Log.v("dbFile", dbFile + "   "+ dbFile.exists());
        return dbFile.exists();
    }

    //Copy the database from assets
    private void copyDbFromAssets() throws IOException
    {
        InputStream mInput = mContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer))>0)
        {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    //Open the database to query it
    public boolean openDb() throws SQLException
    {
        String mPath = DB_PATH + DB_NAME;
        //Log.v("mPath", mPath);
        mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        //mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        return mDataBase != null;
    }

    //Create tables
    @Override
    public void onCreate(SQLiteDatabase db)
    {
    }

    //Upgrade database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        onCreate(db);
    }

    //Downgrade database
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    @Override
    public synchronized void close()
    {
        if(mDataBase != null)
            mDataBase.close();
        super.close();
    }

    // Get single food based on name
    public Food getFood(String name)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FOODS, new String[] { COLUMN_FOOD_ID,
                        COLUMN_FOOD_NAME, COLUMN_CARBS, COLUMN_FAT, COLUMN_PROTEIN, COLUMN_CMEASURE, COLUMN_UNIT }, COLUMN_FOOD_NAME + "=?",
                new String[] { String.valueOf(name) }, null, null, null, null); //id + 1000 as first ID is 1001
        if (cursor != null)
            cursor.moveToFirst();

        Food food = new Food(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getFloat(2), cursor.getFloat(3), cursor.getFloat(4), cursor.getFloat(5), cursor.getString(6));
        db.close();
        // return food
        return food;
    }

    //Get food unit based on name
    public String getUnit(String name)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT " + COLUMN_UNIT + " FROM " + TABLE_FOODS + " WHERE " + COLUMN_FOOD_NAME + " = '" + name + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        String unit = "";
        if (cursor != null) {
            cursor.moveToFirst();
            String[] aUnit = cursor.getString(0).split(" "); //looking for second word
            unit = aUnit[1];
        }
        db.close();
        // return unit
        return unit;
    }

    //Get common measure of food based on name
    public int getCommonMeasure(String name)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT " + COLUMN_CMEASURE + " FROM " + TABLE_FOODS + " WHERE " + COLUMN_FOOD_NAME + " = '" + name + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        int cm = 0;
        if (cursor != null) {
            cursor.moveToFirst();
            cm = cursor.getInt(0);
        }
        db.close();
        // return unit
        return cm;
    }

    //Get all foods
    public List<String> getAllFoodNames(Context context) {
        List<String> foodList = new ArrayList<String>();
        //Toast to let user know it will take long to query large food database
        CharSequence text = "Loading USDA food database...";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        // Select All Query
        String selectQuery = "SELECT " + COLUMN_FOOD_NAME + " FROM " + TABLE_FOODS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // loop through all rows, adding to list
        if (cursor.moveToFirst()) {
            do {
                foodList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        // return contact list
        db.close();
        return foodList;
    }

    //Search db for similar foods
    public List<Food> getSimilarFoods(String partName)
    {
        List<Food> foodList = new ArrayList<Food>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_FOODS + " WHERE " + partName + " LIKE " + COLUMN_FOOD_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Food food = new Food();
                food.setID(Integer.parseInt(cursor.getString(0)));
                food.setName(cursor.getString(1));
                food.setCarbs(cursor.getInt(2));
                food.setFat(cursor.getInt(3));
                food.setProtein(cursor.getInt(4));
                food.setCMeasure(cursor.getInt(5));
                // Adding food to list
                foodList.add(food);
            } while (cursor.moveToNext());
        }
        // return contact list
        db.close();
        return foodList;
    }
}