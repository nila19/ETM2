package com.expense.utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class FormatUtils {
	public static final DecimalFormat AMOUNT = new DecimalFormat("#,##0.00");
	public static final DecimalFormat AMOUNT_NOCOMMA = new DecimalFormat("###0.00");
	public static final DecimalFormat N0000 = new DecimalFormat("0000");

	public static final DateFormat ddMMMyyyyhhmmssa = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
	public static final DateFormat ddMMMyyhhmma = new SimpleDateFormat("dd-MMM-yy hh:mm a");
	public static final DateFormat ddMMM = new SimpleDateFormat("dd-MMM");
	public static final DateFormat ddMMMyy = new SimpleDateFormat("dd-MMM-yy");
	public static final DateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
	public static final DateFormat yyyyMM = new SimpleDateFormat("yyyyMM");
	public static final DateFormat yyyy = new SimpleDateFormat("yyyy");
	public static final DateFormat MMMyy = new SimpleDateFormat("MMM-yy");
	public static final DateFormat MMMyyyy = new SimpleDateFormat("MMM-yyyy");
	public static final DateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
}
