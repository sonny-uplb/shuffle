package com.sheenergizer.games.shuffle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Question {
	private long id;
	private String category;
	private String question;
	private String answer;
	private String opt1;
	private String opt2;
	private String opt3;
	private int pts;
	private int level;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getQuestion() {
		return question;
	}
	
	public void setQuestion(String question) {
		this.question = question;
	}
	
	public String getAnswer() {
		return answer;
	}
	
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	public String getOpt1() {
		return opt1;
	}
	
	public void setOpt1(String opt1) {
		this.opt1 = opt1;
	}
	
	public String getOpt2() {
		return opt2;
	}
	
	public void setOpt2(String opt2) {
		this.opt2 = opt2;
	}
	
	public String getOpt3() {
		return opt3;
	}
	
	public void setOpt3(String opt3) {
		this.opt3 = opt3;
	}
	
	public int getPts() {
		return pts;
	}
	
	public void setPts(int pts) {
		this.pts = pts;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public List<String> getQuestionOptions(){
		List<String> shuffle = new ArrayList<String>();
		shuffle.add(answer);
		shuffle.add(opt1);
		shuffle.add(opt2);
		shuffle.add(opt3);
		Collections.shuffle(shuffle);
		return shuffle;
	}
}