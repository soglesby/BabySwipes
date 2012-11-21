package com.babyswipes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;
import android.util.Log;

public class BabySwipesDB extends SQLiteOpenHelper 
{
    private static final String TAG = "BSSQLiteOpenHelper";
    
    private static final String DATABASE_NAME = "bs_database.db";
    private static final int DATABASE_VERSION = 1;
    
    private SQLiteDatabase mDB;
    
	public static final String TABLE_TAG_TYPES = "tag_types";
	public static final String ID = "_id";
	public static final String TAG_NAME = "tag_name";
	public static final String TAG_REMINDER = "tag_reminder";
    
	public static final String TABLE_TAG_USAGE = "tag_usage";
	public static final String TAG_SWIPE_TIME = "tag_swipe_time";
    
	private static final String DATABASE_CREATE_1 = "create table "
      + TABLE_TAG_TYPES + "(" + ID
      + " integer primary key autoincrement, " + TAG_NAME
      + " text not null, " + TAG_REMINDER + " integer);";
    
	private static final String DATABASE_CREATE_2 = "create table "
      + TABLE_TAG_USAGE + "(" + ID
      + " integer primary key autoincrement, " + TAG_NAME
      + " text not null, " + TAG_SWIPE_TIME + " integer not null);";

	public class BabySwipe
	{
		String tagName;
		int swipeTime;
	}
	

    /* (non-Javadoc)
     * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
     * 
     * Creates the database when needed. Sets up the tables in the DB.
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.d(TAG, "creating BabySwipes DB");
    	db.execSQL(DATABASE_CREATE_1);
    	db.execSQL(DATABASE_CREATE_2);
    }

    /* (non-Javadoc)
     * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
     * 
     * Upgrades the database when needed. Deletes all data, then re-sets up the tables in the DB.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
    {
        Log.d(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);
        
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAG_TYPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAG_USAGE);
        onCreate(db);
    }
	
    /**
     * Standard constructor.
     * 
     * @param context The context to use (recommend using getBaseContext()).
     */
    public BabySwipesDB(Context context) 
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        
        mDB = this.getReadableDatabase();
        
        Log.d(TAG, "called constructor for BabySwipesDB");
    }

	/**
	 * Deletes all data in the database.
	 * Specifically, drops the tables and recreates the tables, so the database
	 * is left with empty data tables.
	 */
	public void clearAllData()
	{
        Log.d(TAG, "clearing all data");
        
		mDB.execSQL("DROP TABLE IF EXISTS " + TABLE_TAG_TYPES);
		mDB.execSQL("DROP TABLE IF EXISTS " + TABLE_TAG_USAGE);
		
		mDB.execSQL(DATABASE_CREATE_1);
		mDB.execSQL(DATABASE_CREATE_2);
	}

	/**
	 * Get the number of tag types in the database
	 * 
	 * @return the number of tag types in the database.
	 */
	public int getNumberOfTags()
	{
        Log.d(TAG, "getting number of tags");
        
        String[] cols = new String[1];
        cols[0] = ID;
        
		Cursor result = mDB.query(TABLE_TAG_TYPES, cols, null, null, null, null, null);
		
		return result.getCount();
	}
	
	/**
	 * Adds a new tag into the TAG_TYPES table. Fails (returns false) if the
	 * tag already exists in the database.
	 * 
	 * @param tagName the name of the new tag
	 * @return true on success, false on failure
	 */
	public boolean addTagType(String tagName)
	{
        Log.d(TAG, "inserting new tag: " + tagName);
        
        String[] cols = new String[1];
        cols[0] = ID;
		Cursor result = mDB.query(TABLE_TAG_TYPES, cols, (TAG_NAME + "='" + tagName + "'"), null, null, null, null);
		if(result.getCount() != 0)
		{
			Log.d(TAG, "tag " + tagName + " already exists in database!");
			return false;
		}
        
        ContentValues values = new ContentValues();

        values.put(TAG_NAME, tagName);
        values.put(TAG_REMINDER, 0);
        
        long res = mDB.insert(TABLE_TAG_TYPES, null, values);
        
		return (res != -1);
	}
	
	/**
	 * Retrieve all tag names from the database
	 * 
	 * @return Array of strings representing all the tag names in the database.
	 */
	public String[] getAllTagNames()
	{
        Log.d(TAG, "getting all tag names");
        
		String[] tagNames;
        
        String[] cols = new String[1];
        cols[0] = TAG_NAME;
        
		Cursor result = mDB.query(TABLE_TAG_TYPES, cols, null, null, null, null, null);
		
		int length = result.getCount();
		
		tagNames = new String[length];
		
		result.moveToFirst();
		for(int i=0; i<length; ++i)
		{
			tagNames[i] = result.getString(0);
			result.moveToNext();
		}
		
		return tagNames;
	}
	
	/**
	 * Sets a reminder for a tag in the database.
	 * The tag must already exist in the database.
	 * No type restriction on the reminder value, recommended that the client
	 * set and use the value in consistent ways.
	 * 
	 * @param tagName The name of the tag to which to set the reminder
	 * @param reminderTime The reminder time, no type restriction
	 * @return true on success, false on failure
	 */
	public boolean setReminderForTag(String tagName, int reminderTime)
	{
        Log.d(TAG, "adding reminder for tag: " + tagName + ", reminder time: " + reminderTime);
        
        String[] cols = new String[1];
        cols[0] = ID;
		Cursor result = mDB.query(TABLE_TAG_TYPES, cols, (TAG_NAME + "='" + tagName + "'"), null, null, null, null);
		if(result.getCount() == 0)
		{
			Log.d(TAG, "tag " + tagName + " doesn't exist in database!");
			return false;
		}
        
        ContentValues values = new ContentValues();

        values.put(TAG_REMINDER, reminderTime);
        
        long res = mDB.update(TABLE_TAG_TYPES, values, (TAG_NAME + "='" + tagName + "'"), null);
        
		return (res != 0);
	}
	
	/**
	 * Get the reminder value for a given tag.
	 * The tag must already exist in the database.
	 * No type restriction on the reminder value, recommended that the client
	 * set and use the value in consistent ways.
	 * 
	 * @param tagName The tag for which to retrieve the reminder.
	 * @return the integer value of the reminder time, or -1 if there's an error.
	 */
	public long getReminderForTag(String tagName)
	{
        Log.d(TAG, "getting reminder for tag: " + tagName);
        
        String[] cols = new String[1];
        cols[0] = ID;
		Cursor result = mDB.query(TABLE_TAG_TYPES, cols, (TAG_NAME + "='" + tagName + "'"), null, null, null, null);
		if(result.getCount() == 0)
		{
			Log.d(TAG, "tag " + tagName + " doesn't exist in database!");
			return -1;
		}
        
        cols[0] = TAG_REMINDER;

		result = mDB.query(TABLE_TAG_TYPES, cols, (TAG_NAME + "='" + tagName + "'"), null, null, null, null);
		result.moveToFirst();
        
		return result.getLong(0);
	}
	
	/**
	 * Add a single swipe data into the database.
	 * Tag type must already exist in the database.
	 * 
	 * @param tagName The tag for this swipe.
	 * @param swipeTime The time for this swipe.
	 * @return true on success, false on failure.
	 */
	public boolean addSwipe(String tagName, int swipeTime)
	{
        Log.d(TAG, "adding swipe for tag: " + tagName + ", time: " + swipeTime);
        
        String[] cols = new String[1];
        cols[0] = ID;
		Cursor result = mDB.query(TABLE_TAG_TYPES, cols, (TAG_NAME + "='" + tagName + "'"), null, null, null, null);
		if(result.getCount() == 0)
		{
			Log.d(TAG, "tag " + tagName + " doesn't exist in database!");
			return false;
		}
        
        ContentValues values = new ContentValues();

        values.put(TAG_NAME, tagName);
        values.put(TAG_SWIPE_TIME, swipeTime);
        
        long res = mDB.insert(TABLE_TAG_USAGE, null, values);
		
		return (res != -1);
	}
	
	/**
	 * Retrieve the total number of swipes in the database for a given tag type.
	 * Tag type must already exist in the database.
	 * 
	 * @param tagName The tag for which to get the total swipe count.
	 * @return Number of swipes in the DB for this tag, or -1 on error.
	 */
	public int getNumberSwipesForTag(String tagName)
	{
        Log.d(TAG, "getting number swipes for tag: " + tagName);
        
        String[] cols = new String[1];
        cols[0] = ID;
		Cursor result = mDB.query(TABLE_TAG_TYPES, cols, (TAG_NAME + "='" + tagName + "'"), null, null, null, null);
		if(result.getCount() == 0)
		{
			Log.d(TAG, "tag " + tagName + " doesn't exist in database!");
			return -1;
		}

		result = mDB.query(TABLE_TAG_USAGE, cols, (TAG_NAME + "='" + tagName + "'"), null, null, null, null);
		
		return result.getCount();
	}
	
	/**
	 * Get all the swipe times for a given tag type.
	 * Tag type must already exist in the database.
	 * 
	 * @param tagName The tag for which to retrieve all the swipe times
	 * @return an integer array of all swipes in the database for the given tag type, or null on error.
	 */
	public int[] getAllSwipeTimesForTag(String tagName)
	{
        Log.d(TAG, "getting all swipes for tag: " + tagName);
        
        String[] cols = new String[1];
        cols[0] = ID;
		Cursor result = mDB.query(TABLE_TAG_TYPES, cols, (TAG_NAME + "='" + tagName + "'"), null, null, null, null);
		if(result.getCount() == 0)
		{
			Log.d(TAG, "tag " + tagName + " doesn't exist in database!");
			return null;
		}
		
		int length = this.getNumberSwipesForTag(tagName);
		
		int[] swipeTimes = new int[length];

        cols[0] = TAG_SWIPE_TIME;
		result = mDB.query(TABLE_TAG_USAGE, cols, (TAG_NAME + "='" + tagName + "'"), null, null, null, null);
		
		result.moveToFirst();
		for(int i=0; i<length; ++i)
		{
			swipeTimes[i] = result.getInt(0);
			result.moveToNext();
		}
		
		return swipeTimes;
	}
	
	/**
	 * Get the most recent swipes from the database, regardless of tag type.
	 * 
	 * @param number Number of swipes to return, must be non-negative
	 * @return Array of type BabySwipe, each representing a single tag-time pair, or null if error
	 */
	public BabySwipe[] getRecentSwipes(int number)
	{
        Log.d(TAG, "getting most recent number of swipes: " + number);
        
        if(number < 0)
        {
        	Log.d(TAG, "can't ask for a negative number of swipes");
        	return null;
        }
        
        String[] cols = new String[2];
        cols[0] = ID;
		Cursor result = mDB.query(TABLE_TAG_USAGE, cols, null, null, null, null, null);
		
		int baseLength = result.getCount();
		int length = (baseLength < number ? baseLength : number);
		
		BabySwipe[] swipes = new BabySwipe[length];
		for(int i=0; i<length; ++i)
		{
			swipes[i] = new BabySwipe();
		}
		
		Log.d(TAG, "using length " + length);

        cols[0] = TAG_NAME;
        cols[1] = TAG_SWIPE_TIME;
        result = mDB.query(TABLE_TAG_USAGE, cols, null, null, null, null, (TAG_SWIPE_TIME + " desc"), ("0, " + length));
		
		result.moveToFirst();
		for(int i=0; i<length; ++i)
		{
			swipes[i].tagName = result.getString(0);
			swipes[i].swipeTime = result.getInt(1);
			
			result.moveToNext();
		}
		
		return swipes;
	}
	
	/**
	 * Gets the most recent swipes from the database, for a specific tag type.
	 * 
	 * @param tagName Tag type for which to return data.
	 * @param number Number of swipes to return, must be non-negative
	 * @return Array of integers, that represents the swipe times for this tag
	 */
	public int[] getRecentSwipesForTag(String tagName, int number)
	{
        Log.d(TAG, "getting recent " + number + " swipes for tag: " + tagName);
        
        if(number < 0)
        {
        	Log.d(TAG, "can't ask for a negative number of swipes");
        	return null;
        }
        
        String[] cols = new String[1];
        cols[0] = ID;
		Cursor result = mDB.query(TABLE_TAG_TYPES, cols, (TAG_NAME + "='" + tagName + "'"), null, null, null, null);
		if(result.getCount() == 0)
		{
			Log.d(TAG, "tag " + tagName + " doesn't exist in database!");
			return null;
		}
		
		int baseLength = this.getNumberSwipesForTag(tagName);
		int length = (baseLength < number ? baseLength : number);
		
		int[] swipeTimes = new int[length];

        cols[0] = TAG_SWIPE_TIME;
        result = mDB.query(TABLE_TAG_USAGE, cols, (TAG_NAME + "='" + tagName + "'"), null, null, null, (TAG_SWIPE_TIME + " desc"), ("0, " + length));
		
		result.moveToFirst();
		for(int i=0; i<length; ++i)
		{
			swipeTimes[i] = result.getInt(0);
			result.moveToNext();
		}
		
		return swipeTimes;
	}
}
