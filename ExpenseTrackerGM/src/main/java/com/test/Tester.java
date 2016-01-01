package com.test;

import org.apache.commons.lang3.StringUtils;

public class Tester {

	public int add(int a, int b) {
		return a + b;
	}

	public String add(String a, String b) {
		return StringUtils.upperCase(a) + StringUtils.upperCase(b);
	}
}
