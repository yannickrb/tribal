package za.co.tribalapp.tribal;

/**
 * Created by Yannick on 2014/09/07.
 */
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;

public class FoodDbAdapter
{
    protected static final String TAG = "FoodDbAdapter";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private FoodDbHandler mDbHelper;

    public FoodDbAdapter(Context context)
    {
        this.mContext = context;
        mDbHelper = new FoodDbHandler(mContext);
    }

    public FoodDbAdapter launchCreateDatabase() throws SQLException
    {
        try
        {
            mDbHelper.createDb();
        }
        catch (IOException mIOException)
        {
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public FoodDbAdapter open() throws SQLException
    {
        try
        {
            mDbHelper.openDb();
            mDbHelper.close();
            mDb = mDbHelper.getReadableDatabase();
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "open >>"+ mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close()
    {
        mDbHelper.close();
    }

    public Cursor getTestData()
    {
        try
        {
            String sql ="SELECT * FROM Foods";

            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur!=null)
            {
                mCur.moveToNext();
            }
            return mCur;
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "Get test data >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }
}
