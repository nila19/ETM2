package com.expense.utils;

import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class Props {
	public static PropertyResourceBundle expense;
	public static String dojoPath;
	public static String appPath;
	public static String appName;
	public static String aLogin;

	public enum ALogin {
		YES("Y"), NO("N");

		public String aLogin;

		private ALogin(String aLogin) {
			this.aLogin = aLogin;
		}
	}

	static {
		Props.expense = (PropertyResourceBundle) ResourceBundle.getBundle("expense");
		Props.dojoPath = Props.expense.getString("DOJO.PATH");
		Props.appPath = Props.expense.getString("APP.PATH");
		Props.appName = Props.expense.getString("APP.NAME");
		Props.aLogin = Props.expense.getString("AUTO_LOGIN");
	}
}
