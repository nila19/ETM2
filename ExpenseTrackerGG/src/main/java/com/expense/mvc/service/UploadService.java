package com.expense.mvc.service;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.expense.mvc.model.entity.Transaction;
import com.expense.mvc.model.ui.TransactionUI;

@Service
public class UploadService {
	private static final Logger logger = LogManager.getLogger("log." + UploadService.class);

	@Autowired
	private EntryService entryService;

	@Transactional(propagation = Propagation.REQUIRED)
	public String upload(HttpServletRequest request, int dataKey) throws Exception {
		String msg = "";

		ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
		List<FileItem> items = upload.parseRequest(request);
		for (FileItem item : items) {
			if (!item.isFormField()) {
				msg += processFile(item, dataKey);
			}
		}

		if (StringUtils.isBlank(msg)) {
			throw new Exception("No file uploaded...");
		}

		return msg;
	}

	private String processFile(FileItem item, int dataKey) throws IOException, ParseException {
		List<TransactionUI> uis = buildUIs(new String(item.get()));
		String msg = loadDB(uis, dataKey);
		return msg;
	}

	private List<TransactionUI> buildUIs(String data) throws IOException, ParseException {
		List<TransactionUI> uis = new ArrayList<TransactionUI>();

		CSVParser parser = new CSVParser(new StringReader(data), CSVFormat.DEFAULT);

		/*
		 * #0.AdjustInd (Y) #1.TransDt (mm/dd/yyyy) #2.Description #3.CategoryID #4.FromAccID #5.ToAccID #6.AdhocInd (Y)
		 * #7.Amount
		 */

		try {
			for (CSVRecord rec : parser) {
				// while (fields != null) {
				TransactionUI ui = new TransactionUI();

				ui.setEntryDate(new Date());
				ui.setEntryMonth(DateUtils.truncate(new Date(), Calendar.MONTH));

				// #0 AdjustInd
				boolean adjust = BooleanUtils.toBoolean(rec.get(0).trim(), "Y", "");
				if (adjust) {
					ui.setAdjustInd(Transaction.Adjust.YES.type);
				} else {
					ui.setAdjustInd(Transaction.Adjust.NO.type);
				}

				// #1 TransDt
				ui.setTransDate(DateUtils.parseDate(rec.get(1).trim(), "MM/dd/yyyy"));
				ui.setTransMonth(DateUtils.truncate(ui.getTransDate(), Calendar.MONTH));

				// #2 Description
				ui.setDescription(rec.get(2).trim());

				// #3 Category ID
				if (StringUtils.isNotBlank(rec.get(3))) {
					ui.setCategoryId(Integer.parseInt(rec.get(3).trim()));
				}

				// #4 From Acct ID
				ui.setFromAccountId(Integer.parseInt(rec.get(4).trim()));

				// #5 To Acct ID
				if (StringUtils.isNotBlank(rec.get(5))) {
					ui.setToAccountId(Integer.parseInt(rec.get(5).trim()));
				}

				// #6 AdhocInd
				boolean adhoc = BooleanUtils.toBoolean(rec.get(6).trim(), "Y", "");
				if (adhoc) {
					ui.setAdhocInd(Transaction.Adhoc.YES.type);
				} else {
					ui.setAdhocInd(Transaction.Adhoc.NO.type);
				}

				// #7 Amount
				ui.setAmount(Double.parseDouble(rec.get(7).trim()));

				uis.add(ui);
				// fields = parser.getLine();
			}
		} finally {
			parser.close();
		}

		return uis;
	}

	private String loadDB(List<TransactionUI> uis, int dataKey) {
		String msg = "";

		int count = 0;
		try {
			for (TransactionUI ui : uis) {
				this.entryService.addExpense(ui, dataKey);
				count++;
			}
			msg = "SUCCESSFULL...";
		} catch (Exception e) {
			msg = "FAILED... Error => " + e;
			UploadService.logger.error("Upload Failed... ", e);
		}

		msg += " :: Total = " + uis.size() + " :: Processed = " + count + " <br> ";
		return msg;
	}
}
