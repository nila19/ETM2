package com.expense.utils;

public class Menus {
	public static String prefix = Props.appPath + "/servlet/";

	public enum Page {
		ENTRY("ENTRY", "Expense Entry", prefix + "/entry/page"), SUMMARY("SUMMARY", "Expense Summary", prefix
				+ "summary/page"), SEARCH("SEARCH", "Expense Search", prefix + "search/page");

		public String type;
		public String name;
		public String url;

		private Page(String type, String name, String url) {
			this.type = type;
			this.name = name;
			this.url = url;
		}
	}
}
