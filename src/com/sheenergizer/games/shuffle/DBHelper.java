package com.sheenergizer.games.shuffle;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{

	public static final String TBL_TRIVIA = "questionnaire";
	public static final String COL_ID = "_id";
	public static final String Q_FIELD = "question";
	public static final String CAT_FIELD = "category";
	
	private static final int DBVERSION = 3;
	private static final String DBNAME = "triviadb";
	
	public DBHelper(Context context) {
		super(context, DBNAME, null, DBVERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TRIVIA = "CREATE TABLE " + TBL_TRIVIA + "(" + 
				COL_ID + " INTEGER PRIMARY KEY autoincrement, " +
				CAT_FIELD + " TEXT, " + Q_FIELD +" TEXT, " +
				"answer TEXT, opt1 TEXT, opt2 TEXT, opt3 TEXT, " +
				"pts INTEGER, level INTEGER)";
		db.execSQL(CREATE_TRIVIA);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TBL_TRIVIA);
		onCreate(db);
	}	
	
}