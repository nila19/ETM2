package com.expense.mvc.model.ui;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.expense.utils.FormatUtils;

public class MonthUI implements java.io.Serializable, Comparable<MonthUI> {
	private static final long serialVersionUID = 1L;

	private Date transMonth;
	private String fTransMonth;
	private char type;
	private String desc;

	public enum Type {
		MONTH('M'), YEAR('Y');

		public char type;

		private Type(char type) {
			this.type = type;
		}
	}

	private Integer seq;

	public MonthUI(Date transMonth) {
		this(transMonth, Type.MONTH);
	}

	public MonthUI(Date transMonth, Type type) {
		this.transMonth = transMonth;
		this.setfTransMonth(FormatUtils.yyyyMM.format(this.transMonth));
		this.type = type.type;

		// If year, transMonth will have 01 as month. Make it 13, so it gets sorted ahead of all
		// months in desc order.
		if (type == Type.YEAR) {
			this.desc = FormatUtils.yyyy.format(transMonth);
			this.seq = (Integer.valueOf(FormatUtils.yyyy.format(transMonth)) * 100) + 13;
		} else {
			this.desc = FormatUtils.MMMyy.format(transMonth);
			this.seq = Integer.valueOf(FormatUtils.yyyyMM.format(transMonth));
		}
	}

	public Date getTransMonth() {
		return this.transMonth;
	}

	public void setTransMonth(Date transMonth) {
		this.transMonth = transMonth;
		this.setfTransMonth(FormatUtils.yyyyMM.format(this.transMonth));
	}

	public char getType() {
		return this.type;
	}

	public void setType(char type) {
		this.type = type;
	}

	public int getSeq() {
		return this.seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getDesc() {
		return this.desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getfTransMonth() {
		return this.fTransMonth;
	}

	public void setfTransMonth(String fTransMonth) {
		this.fTransMonth = fTransMonth;
	}

	@Override
	public int compareTo(MonthUI o) {
		return this.seq.compareTo(o.seq);
	}

	@Override
	public int hashCode() {
		return this.seq;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		return this.seq == ((MonthUI) o).seq;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
