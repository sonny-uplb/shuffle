package com.sheenergizer.games.shuffle;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class QuestionSource {
	private SQLiteDatabase db;
	private DBHelper dbh;
	
	public QuestionSource(Context context) {
		dbh = new DBHelper(context);
	}
	
	public void openRead() throws SQLException {
		db = dbh.getReadableDatabase();
	}
	
	public void openWrite() throws SQLException {
		db = dbh.getWritableDatabase();
	}
	
	public void close() {
		dbh.close();
	}

	public int getCount() {
		int i;
		Cursor cur = db.rawQuery("SELECT COUNT(" + DBHelper.Q_FIELD + ") FROM " + 
				DBHelper.TBL_TRIVIA, null);
		if (cur.moveToFirst()) {
			i = cur.getInt(0);
			cur.close();
			return i;
		}
		i = cur.getInt(0);
		cur.close();
		return i;
	}
	
	public List<Question> getQuestionSet(int level, int max) {
		List<Question> questions = new ArrayList<Question>();
		String query = "SELECT * FROM " + DBHelper.TBL_TRIVIA + " WHERE level<=" +
				level + " ORDER BY RANDOM() ";
		if (max > 0) {
			query = query + "LIMIT " + max;
		}
		
		Cursor cur = db.rawQuery( query, null);
		cur.moveToFirst();
		while (!cur.isAfterLast()) {
			Question question = cursorToQuestion(cur);
			questions.add(question);
			cur.moveToNext();
		}
		cur.close();
		return questions;
	}
	
	private Question cursorToQuestion(Cursor cursor) {
		Question q = new Question();
		q.setId(cursor.getLong(0));
		q.setCategory(cursor.getString(1));
		q.setQuestion(cursor.getString(2));
		q.setAnswer(cursor.getString(3));
		q.setOpt1(cursor.getString(4));
		q.setOpt2(cursor.getString(5));
		q.setOpt3(cursor.getString(6));
		q.setPts(cursor.getInt(7));
		q.setLevel(cursor.getInt(8));
		return q;
	}
	
	private void insertData(String category, String question, String answer, String opt1,
			String opt2, String opt3, int pts, int level) {
		ContentValues values = new ContentValues();
		values.put(DBHelper.CAT_FIELD, category);
		values.put(DBHelper.Q_FIELD, question);
		values.put("answer", answer);
		values.put("opt1", opt1);
		values.put("opt2", opt2);
		values.put("opt3", opt3);
		values.put("pts", pts);
		values.put("level", level);
		
		db.insert(DBHelper.TBL_TRIVIA, null, values);
	}
	
	// fill-up Question Table, done only at the beginning
	public void fillData() {
		openWrite();
		insertData("Acronym","M in IBM, a multinational technology and consulting corporation","Machines","Multinatioal","Manufacturer","Market",5,1);
		insertData("Acronym","E in OEM, which refers to the original manufacturer of a product","Equipment","Enterprise","Exchange","Economic",10,1);
		insertData("Acronym","What P stands for in ASAP","Possible","Party","Portable","Passable",5,1);
		insertData("Acronym","Meaning of RADAR","Radio Detection and Ranging","Radio Distance and Ranging","Radio Detection and Recon","Radio Distance and Recon",5,1);
		insertData("Acronym","E in UNESCO","Educational","Environmental","Ecological","Economical",10,1);
		insertData("Acronym","A in NATO","Atlantic","Alliance","America","Arms",10,1);
		insertData("Acronym","P in OPEC","Petroleum","Peace","Power","Persian",15,2);
		insertData("Advertising","McDonald's symbol","Golden Arches","Golden M","Golden Smiles","Golden Bridges",5,1);
		insertData("Advertising","Just Do It","Nike","Adidas","Converse","Reebok",5,1);
		insertData("Advertising","Melts in your mouth, not in your hands","M&M's","Reese","Nips","Cadbury",5,1);
		
	}
}