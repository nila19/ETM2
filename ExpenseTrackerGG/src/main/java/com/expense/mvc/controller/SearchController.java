package com.expense.mvc.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.expense.mvc.model.ui.TransactionUI;
import com.expense.mvc.service.LoginService;
import com.expense.mvc.service.SummaryService;
import com.expense.utils.AjaxBean;
import com.expense.utils.Errors;
import com.expense.utils.JsonUtils;
import com.expense.utils.Utils;

@Controller
@RequestMapping("/search")
public class SearchController {

	@Autowired
	private MessageSource messages;

	@Autowired
	private LoginService loginService;

	@Autowired
	private SummaryService summaryService;

	@RequestMapping(value = "/page")
	public String summary(Model model, HttpSession session) {
		if (!loginService.isLoggedIn(model, session)) {
			return Utils.LOGIN;
		}
		// Fetch the latest Search dropdown lists.
		session.setAttribute("login_entrymonths", loginService.getAllEntryMonths(loginService.getDataKey(session)));
		session.setAttribute("login_transmonths", loginService.getAllTransMonths(loginService.getDataKey(session)));

		return "search";
	}

	@RequestMapping(value = "/drill")
	public String drill(@ModelAttribute("tui") TransactionUI tui, Model model, HttpSession session) {
		if (!loginService.isLoggedIn(model, session)) {
			return Utils.LOGIN;
		}

		model.addAttribute("tui", tui);
		return "forward:page";
	}

	@RequestMapping(value = "/execute")
	@ResponseBody
	public String execute(@ModelAttribute("tui") TransactionUI tui, Model model, HttpSession session) {
		if (!loginService.isLoggedIn(null, session)) {
			return Utils.ajaxLoginExpired(messages);
		}
		try {
			session.setAttribute("search_trans", summaryService.search(tui, loginService.getDataKey(session)));
			return JsonUtils.toJson(new AjaxBean(Errors.OK, "", session.getAttribute("search_trans")));
		} catch (Exception e) {
			return JsonUtils.toJson(new AjaxBean(Errors.PROCESS, ""));
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/total")
	@ResponseBody
	public String total(Model model, HttpSession session) {
		if (!loginService.isLoggedIn(null, session)) {
			return Utils.ajaxLoginExpired(messages);
		}
		try {
			return JsonUtils.toJson(new AjaxBean(Errors.OK, "", summaryService.getTotals((List<TransactionUI>) session
					.getAttribute("search_trans"))));
		} catch (Exception e) {
			return JsonUtils.toJson(new AjaxBean(Errors.PROCESS, ""));
		}
	}
}
