package za.co.tribalapp.tribal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Yannick on 2014/08/22.
 * */
public class UserDbHandler extends SQLiteOpenHelper
{
    //Must increment Db version incremented when schema changed
    public static final int DATABASE_VERSION = 7;
    public static final String DATABASE_NAME = "UserDb.db";
    //Create database
    public UserDbHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    private static final String COMMA_SEP = ",";
    //Table name constants
    public static final String TABLE_DIET = "diet";
    public static final String TABLE_EXERCISE = "exercise";
    public static final String TABLE_STRESS = "stress";
    //Column name constants for each table
    public static final String COLUMN_DATE = "date";
    //Diet
    public static final String COLUMN_MEAL_ID = "meal_id";
    public static final String COLUMN_MEAL_NAME = "meal_name";
    public static final String COLUMN_CARBS = "carbohydrates_g";
    public static final String COLUMN_FAT = "fat_g";
    public static final String COLUMN_PROTEIN = "protein_g";
    //Exercise & stress
    public static final String COLUMN_SESSION_ID = "session_id";
    public static final String COLUMN_SESSION_NAME = "session_name"; //For log_exercise, e.g. jog, hike, Pilates, gym
    public static final String COLUMN_SESSION_TYPE = "session_type"; //i.e. cardio/strength, work/sleep
    public static final String COLUMN_SESSION_DURATION_MIN = "session_duration_min";
    public static final String COLUMN_SESSION_DURATION_HRS = "session_duration_hrs";
    public static final String COLUMN_HRR = "hrr";
    public static final String COLUMN_HRV = "hrv";

    //Constants to create and delete table
    //Diet table
    private static final String SQL_CREATE_TABLE_DIET =
            "CREATE TABLE IF NOT EXISTS " + TABLE_DIET + " (" +
                    COLUMN_MEAL_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_MEAL_NAME + " TEXT DEFAULT ' '" + COMMA_SEP +
                    COLUMN_CARBS + " INTEGER DEFAULT 0" + COMMA_SEP +
                    COLUMN_FAT + " INTEGER DEFAULT 0" + COMMA_SEP +
                    COLUMN_PROTEIN + " INTEGER DEFAULT 0" + COMMA_SEP +
                    COLUMN_DATE + " INTEGER DEFAULT 0" +
                    " )";
    private static final String SQL_DELETE_TABLE_DIET =
            "DROP TABLE IF EXISTS " + TABLE_DIET;

    //Exercise table
    private static final String SQL_CREATE_TABLE_EXERCISE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_EXERCISE + " (" +
                    COLUMN_SESSION_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_SESSION_NAME + " TEXT DEFAULT ' '" + COMMA_SEP +
                    COLUMN_SESSION_TYPE + " TEXT DEFAULT ' '" + COMMA_SEP +
                    COLUMN_SESSION_DURATION_MIN + " INTEGER DEFAULT 0" + COMMA_SEP +
                    COLUMN_HRR + " INTEGER DEFAULT 0" + COMMA_SEP +
                    COLUMN_DATE + " INTEGER DEFAULT 0" +
                    " )";
    private static final String SQL_DELETE_TABLE_EXERCISE =
            "DROP TABLE IF EXISTS " + TABLE_EXERCISE;

    //Stress table
    private static final String SQL_CREATE_TABLE_STRESS =
            "CREATE TABLE IF NOT EXISTS " + TABLE_STRESS + " (" +
                    COLUMN_SESSION_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_SESSION_TYPE + " TEXT DEFAULT ' '" + COMMA_SEP +
                    COLUMN_SESSION_DURATION_HRS + " INTEGER DEFAULT 0" + COMMA_SEP +
                    COLUMN_HRV + " INTEGER DEFAULT 0" + COMMA_SEP +
                    COLUMN_DATE + " INTEGER DEFAULT 0" +
                    " )";
    private static final String SQL_DELETE_TABLE_STRESS =
            "DROP TABLE IF EXISTS " + TABLE_STRESS;

    //Create tables
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(SQL_CREATE_TABLE_DIET);
        db.execSQL(SQL_CREATE_TABLE_EXERCISE);
        db.execSQL(SQL_CREATE_TABLE_STRESS);
    }

    //Upgrade database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_TABLE_DIET);
        db.execSQL(SQL_DELETE_TABLE_EXERCISE);
        db.execSQL(SQL_DELETE_TABLE_STRESS);
        onCreate(db);
    }

    //Downgrade database
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    //Get date or time automatically
    public long getCurrentDate()
    {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        long currentDate = c.getTimeInMillis();
        return currentDate;
    }

    /*Select, Insert, Update, Delete operations for each table*/

    /*Diet*/
    // Add new meal
    public void addMeal(Meal meal)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MEAL_NAME, meal.getName()); // Food name
        values.put(COLUMN_CARBS, meal.getCarbs()); // Carb content
        values.put(COLUMN_FAT, meal.getFat()); //Fat content
        values.put(COLUMN_PROTEIN, meal.getProtein()); // Protein content
        values.put(COLUMN_DATE, getCurrentDate()); //Date of entry
        // Inserting Row
        db.insert(TABLE_DIET, null, values);
        db.close(); // Closing database connection
    }

    // Get single meal based on id
    public Meal getMeal(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_DIET, new String[] { COLUMN_MEAL_ID,
                        COLUMN_MEAL_NAME, COLUMN_CARBS, COLUMN_FAT, COLUMN_PROTEIN, COLUMN_DATE }, COLUMN_MEAL_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null); //group by, having, order by, limit
        if (cursor != null)
            cursor.moveToFirst();

        Meal meal = new Meal(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getLong(5));
        cursor.close();
        db.close();
        // return meal
        return meal;
    }

    //get meal from name
    public Meal getMeal(String name)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_DIET, new String[] { COLUMN_MEAL_ID,
                        COLUMN_MEAL_NAME, COLUMN_CARBS, COLUMN_FAT, COLUMN_PROTEIN, COLUMN_DATE }, COLUMN_MEAL_NAME + "=?",
                new String[] { String.valueOf(name) }, null, null, null, null); //group by, having, order by, limit
        if (cursor != null)
            cursor.moveToFirst();

        Meal meal = new Meal(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getLong(5));
        cursor.close();
        db.close();
        // return meal
        return meal;
    }

    // Get all meals
    public List<Meal> getAllMeals() {
        List<Meal> mealList = new ArrayList<Meal>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DIET;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Meal meal = new Meal();
                meal.setID(Integer.parseInt(cursor.getString(0)));
                meal.setName(cursor.getString(1));
                meal.setCarbs(cursor.getInt(2));
                meal.setFat(cursor.getInt(3));
                meal.setProtein(cursor.getInt(4));
                meal.setDate(cursor.getLong(5));
                // Adding food to list
                mealList.add(meal);
            } while (cursor.moveToNext());
        }
        //Close cursor and db and return contact list
        cursor.close();
        db.close();
        return mealList;
    }

    // Update single meal
    public int updateMeal(Meal meal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MEAL_NAME, meal.getName()); // Meal name
        values.put(COLUMN_CARBS, meal.getCarbs()); // Carb content
        values.put(COLUMN_FAT, meal.getFat()); //Fat content
        values.put(COLUMN_PROTEIN, meal.getProtein()); // Protein content

        // updating row
        return db.update(TABLE_DIET, values, COLUMN_MEAL_ID + " = ?",
                new String[] { String.valueOf(meal.getID()) });
    }

    // Delete single meal
    public void deleteMeal(Meal meal) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DIET, COLUMN_MEAL_ID + " = ?",
                new String[] { String.valueOf(meal.getID()) });
        db.close();
    }

    /*Exercise*/
    // Add new log_exercise session
    public void addExerciseSession(ExerciseSession exerciseSession)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SESSION_NAME, exerciseSession.getName()); // Session name
        values.put(COLUMN_SESSION_TYPE, exerciseSession.getType()); // Session type
        values.put(COLUMN_SESSION_DURATION_MIN, exerciseSession.getDuration()); // Session duration
        values.put(COLUMN_HRR, exerciseSession.getHRR()); //Heart rate recovery
        values.put(COLUMN_DATE, getCurrentDate()); //Date of entry
        // Insert Row
        db.insert(TABLE_EXERCISE, null, values);
        db.close(); // Close database connection
    }

    // Get single session
    public ExerciseSession getExerciseSession(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EXERCISE, new String[] { COLUMN_SESSION_ID,
                        COLUMN_SESSION_NAME, COLUMN_SESSION_TYPE, COLUMN_SESSION_DURATION_MIN, COLUMN_HRR, COLUMN_DATE }, COLUMN_SESSION_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        ExerciseSession exerciseSession = new ExerciseSession(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5));
        cursor.close();
        db.close();
        // return meal
        return exerciseSession;
    }

    // Get all exercises
    public List<ExerciseSession> getAllExercise() {
        List<ExerciseSession> eSessionList = new ArrayList<ExerciseSession>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_EXERCISE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // loop through all rows, adding to list
        if (cursor.moveToFirst()) {
            do {
                ExerciseSession exerciseSession = new ExerciseSession();
                exerciseSession.setID(Integer.parseInt(cursor.getString(0)));
                exerciseSession.setName(cursor.getString(1));
                exerciseSession.setType(cursor.getString(2));
                exerciseSession.setDuration(cursor.getInt(3));
                exerciseSession.setHRR(cursor.getInt(4));
                exerciseSession.setDate(cursor.getLong(5));
                // Adding food to list
                eSessionList.add(exerciseSession);
            } while (cursor.moveToNext());
        }
        //Close cursor and db and return contact list
        cursor.close();
        db.close();
        return eSessionList;
    }

    // Update single session
    public int updateExerciseSession(ExerciseSession exerciseSession) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SESSION_NAME, exerciseSession.getName()); // Session name
        values.put(COLUMN_SESSION_TYPE, exerciseSession.getType()); // Session type
        values.put(COLUMN_SESSION_DURATION_MIN, exerciseSession.getDuration()); //Session duration
        values.put(COLUMN_HRR, exerciseSession.getHRR()); //HRR
        // update row
        return db.update(TABLE_DIET, values, COLUMN_SESSION_ID + " = ?",
                new String[] { String.valueOf(exerciseSession.getID()) });
    }

    // Delete single session
    public void deleteExerciseSession(ExerciseSession exerciseSession) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXERCISE, COLUMN_SESSION_ID + " = ?",
                new String[] { String.valueOf(exerciseSession.getID()) });
        db.close();
    }


    /*Stress*/
    // Add new stress session
    public void addStressSession(StressSession stressSession)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SESSION_TYPE, stressSession.getType()); // Session type
        values.put(COLUMN_SESSION_DURATION_HRS, stressSession.getDuration()); // Session duration
        values.put(COLUMN_HRV, stressSession.getHRV());
        values.put(COLUMN_DATE, getCurrentDate()); //Date of entry
        // Insert Row
        db.insert(TABLE_STRESS, null, values);
        db.close(); // Closing database connection
    }

    // Get single stress-related session
    public StressSession getStressSession(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_STRESS, new String[] { COLUMN_SESSION_ID,
                        COLUMN_SESSION_TYPE, COLUMN_SESSION_DURATION_HRS, COLUMN_HRV, COLUMN_DATE }, COLUMN_SESSION_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        StressSession stressSession = new StressSession(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4));
        cursor.close();
        db.close();
        // return meal
        return stressSession;
    }

    // Get all stress-related sessions
    public List<StressSession> getAllStress() {
        List<StressSession> sSessionList = new ArrayList<StressSession>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_STRESS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // loop through all rows, adding to list
        if (cursor.moveToFirst()) {
            do {
                StressSession stressSession = new StressSession();
                stressSession.setID(Integer.parseInt(cursor.getString(0)));
                stressSession.setType(cursor.getString(1));
                stressSession.setDuration(cursor.getInt(2));
                stressSession.setHRV(cursor.getInt(3));
                stressSession.setDate(cursor.getLong(4));
                // Adding food to list
                sSessionList.add(stressSession);
            } while (cursor.moveToNext());
        }
        //Close cursor and db and return contact list
        cursor.close();
        db.close();
        return sSessionList;
    }

    // Update single session
    public int updateStressSession(StressSession stressSession) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SESSION_TYPE, stressSession.getType()); // Session type
        values.put(COLUMN_SESSION_DURATION_HRS, stressSession.getDuration()); //Session duration
        values.put(COLUMN_HRV, stressSession.getHRV());
        // update row
        return db.update(TABLE_STRESS, values, COLUMN_SESSION_ID + " = ?",
                new String[] { String.valueOf(stressSession.getID()) });
    }

    // Delete single session
    public void deleteStressSession(StressSession stressSession) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STRESS, COLUMN_SESSION_ID + " = ?",
                new String[] { String.valueOf(stressSession.getID()) });
        db.close();
    }

    /*General queries and operations*/

    //Get total of nutrient column in diet table
    public int getDietTotal(String column, long startDate, long endDate)
    {
        int total = 0;
        String sTotal = "SELECT SUM(" + column + ") FROM " + TABLE_DIET  + " WHERE " +
                COLUMN_DATE + " >= " + startDate  + " AND " + COLUMN_DATE + " <= " + endDate;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sTotal, null);
        if (cursor.moveToFirst()) {
            total = cursor.getInt(0);
            cursor.close();
        }
        db.close();
        return total;
    }

    //total of exercise table column
    public int getExerciseTotal(String type, long startDate, long endDate)
    {
        int total = 0;
        String sTotal = "SELECT SUM(" + COLUMN_SESSION_DURATION_MIN + ") FROM " + TABLE_EXERCISE  + " WHERE " +
                COLUMN_SESSION_TYPE + " = '" + type + "' AND " +
                COLUMN_DATE + " >= " + startDate  + " AND " + COLUMN_DATE + " <= " + endDate;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sTotal, null);
        if (cursor.moveToFirst()) {
            total = cursor.getInt(0);
            cursor.close();
        }
        db.close();
        return total;
    }

    //total of stress table column
    public int getStressTotal(String type, long startDate, long endDate)
    {
        int total = 0;
        String sTotal = "SELECT SUM(" + COLUMN_SESSION_DURATION_HRS + ") FROM " + TABLE_STRESS  + " WHERE " +
                COLUMN_SESSION_TYPE + " = '" + type + "' AND " +
                COLUMN_DATE + " >= " + startDate  + " AND " + COLUMN_DATE + " < " + endDate;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sTotal, null);
        if (cursor.moveToFirst()) {
            total = cursor.getInt(0);
            cursor.close();
        }
        db.close();
        return total;
    }
}
