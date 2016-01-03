package com.expense.mvc.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.expense.utils.FormatUtils;

@Entity
@Table(name = "DATAKEY")
public class DataKey extends com.expense.mvc.model.BaseEntity implements java.io.Serializable, Comparable<DataKey> {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "DATA_KEY", unique = true, nullable = false)
	private Integer dataKey;

	@Column(name = "DESCRIPTION", nullable = false, length = 25)
	private String description;

	@Column(name = "START_DT", nullable = false)
	private String strStartDt;

	@Column(name = "END_DT", nullable = false)
	private String strEndDt;

	@Column(name = "CURRENCY", length = 3)
	private String currency;

	public enum Currency {
		INR("INR"), USD("USD");

		public String currency;

		private Currency(String currency) {
			this.currency = currency;
		}
	}

	@Column(name = "STATUS", length = 1)
	private Character status;

	public enum Status {
		ACTIVE('A'), INACTIVE('I');

		public char status;

		private Status(char status) {
			this.status = status;
		}
	}

	@Column(name = "DEFAULT_IND", length = 1)
	private Character defaultInd;

	public enum DefaultInd {
		YES('Y'), NO('N');

		public char defaultInd;

		private DefaultInd(char defaultInd) {
			this.defaultInd = defaultInd;
		}
	}

	public DataKey() {
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Character getDefaultInd() {
		return defaultInd;
	}

	public void setDefaultInd(Character defaultInd) {
		this.defaultInd = defaultInd;
	}

	public String getStrStartDt() {
		return strStartDt;
	}

	public void setStrStartDt(String strStartDt) {
		this.strStartDt = strStartDt;
	}

	public String getStrEndDt() {
		return strEndDt;
	}

	public void setStrEndDt(String strEndDt) {
		this.strEndDt = strEndDt;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public void setDataKey(Integer dataKey) {
		this.dataKey = dataKey;
	}

	public Date getStartDt() {
		try {
			return FormatUtils.yyyyMMdd.parse(strStartDt);
		} catch (Exception e) {
			return null;
		}
	}

	public void setStartDt(Date startDt) {
		strStartDt = FormatUtils.yyyyMMdd.format(startDt);
	}

	public Date getEndDt() {
		try {
			return FormatUtils.yyyyMMdd.parse(strEndDt);
		} catch (Exception e) {
			return null;
		}
	}

	public void setEndDt(Date endDt) {
		strEndDt = FormatUtils.yyyyMMdd.format(endDt);
	}

	@Override
	public int compareTo(DataKey o) {
		if (o.dataKey == null) {
			return -1;
		} else if (dataKey == null) {
			return 1;
		} else {
			return dataKey.compareTo(o.dataKey);
		}
	}
}
