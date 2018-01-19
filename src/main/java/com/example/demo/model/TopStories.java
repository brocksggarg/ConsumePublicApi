package com.example.demo.model;

public class TopStories {

	private String section;
	private String title;
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public TopStories(String section, String title) {
		super();
		this.section = section;
		this.title = title;
	}
	public TopStories() {
		super();
	}
	
	
}
