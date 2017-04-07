package com.expense.mvc.model.ui;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.expense.mvc.model.entity.DataKey;
import com.expense.utils.FormatUtils;
import com.expense.utils.Utils;

public class DataKeyUI implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private int dataKey;
	private String description = "";
	private char status;
	private char defaultId;
	private Date startDt;
	private Date endDt;
	private String currency = "";

	public DataKeyUI() {
	}

	public DataKeyUI(DataKey dk) {
		Utils.copyBean(this, dk);

		setStartDt(dk.getStartDt());
		setEndDt(dk.getEndDt());
	}

	public int getDataKey() {
		return dataKey;
	}

	public void setDataKey(int dataKey) {
		this.dataKey = dataKey;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public char getDefaultId() {
		return defaultId;
	}

	public void setDefaultId(char defaultId) {
		this.defaultId = defaultId;
	}

	public Date getStartDt() {
		return startDt;
	}

	public String getStrStartDt() {
		try {
			return FormatUtils.MMMyyyy.format(startDt);
		} catch (Exception e) {
			return "";
		}
	}

	public void setStartDt(Date startDt) {
		try {
			this.startDt = FormatUtils.yyyyMMdd.parse(FormatUtils.yyyyMMdd.format(startDt));
		} catch (ParseException e) {
		}
	}

	public Date getEndDt() {
		return endDt;
	}

	public String getStrEndDt() {
		try {
			return FormatUtils.MMMyyyy.format(endDt);
		} catch (Exception e) {
			return "";
		}
	}

	public void setEndDt(Date endDt) {
		try {
			this.endDt = FormatUtils.yyyyMMdd.parse(FormatUtils.yyyyMMdd.format(endDt));
		} catch (ParseException e) {
		}
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	// Custom methods.
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
