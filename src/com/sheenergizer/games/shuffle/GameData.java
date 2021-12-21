package com.sheenergizer.games.shuffle;

import java.util.ArrayList;
import java.util.List;

public class GameData {
	private int game_limit;
	private long score;
	private int life;
	private int level;
	private static int idx;
	private int cur_idx;
	private int quest_count;
	private List<Question> questlist = new ArrayList<Question>();
	
	public GameData(int game_limit) {
		score = 0;
		life = 3;
		level = 1;
		idx = 0;
		quest_count = 0;
		cur_idx = 0;
		this.game_limit = game_limit;
	}
	
	public long getScore() {
		return score;
	}
	
	public void addScore(int score) {
		this.score += score;
		addCount();
	}
	
	public void deductScore(int score) {
		this.score -= score;
	}
	
	public void deductLife(int life) {
		this.life--;
	}
	
	public boolean isGameOver() {
		return (life==0);
	}
	
	public boolean hasCompletedSet() {
		return (getCount()==game_limit);
	}
	
	public int getLevel() {
		return level;
	}

	public int getIndex() {
		return cur_idx;
	}
	
	public void addCount() {
		quest_count++;
	}
	
	public int getCount() {
		return quest_count;
	}
	
	public boolean removeQuestion(int num) {
		Question tmp = null;
		int ctr=0;

		questlist.set(num, tmp);
		//Log.d("GAME DATA", "Size " + questlist.size());
		
		for (int i=0; i < questlist.size(); i++) {
			if (questlist.get(i) != null) {
				ctr++;
			}
		}
		
		if (ctr > life) return true;
		
		return false;
	}
	
	public void setQuestionList(List<Question> questlist) {
		this.questlist = questlist;
	}
	
	public Question getCurrentQuestion(int num) {
		return questlist.get(num);
	}
	
	public Question getNextQuestion() {
		
		if (questlist.size() <= idx)
			idx=0;		
		//Log.d("GAME DATA", "Size:" + questlist.size() + " Idx:" + idx);
		
		while (questlist.get(idx) == null) {
			idx++;
			if (questlist.size() <= idx)
				idx=0;
		}
		Question next = questlist.get(idx);
		cur_idx = idx;
		idx++;
		return next;
	}
	
}