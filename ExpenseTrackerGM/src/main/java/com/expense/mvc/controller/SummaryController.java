package com.expense.mvc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.expense.mvc.helper.SummaryHelper;
import com.expense.mvc.service.LoginService;
import com.expense.mvc.service.SummaryService;
import com.expense.utils.AjaxBean;
import com.expense.utils.Errors;
import com.expense.utils.JsonUtils;
import com.expense.utils.Utils;

@Controller
@RequestMapping("/summary")
public class SummaryController {

	@Autowired
	private LoginService loginService;

	@Autowired
	private SummaryService summaryService;

	@RequestMapping(value = "/page")
	public String summary(Model model, HttpSession session) {
		if (!loginService.isLoggedIn(model, session)) {
			return Utils.LOGIN;
		}

		SummaryHelper helper = summaryService.getSummary('0', false, loginService.getDataKey(session));
		session.setAttribute("summary_helper", helper);

		return "summary";
	}

	@RequestMapping(value = "/fetch", method = RequestMethod.POST)
	@ResponseBody
	public String fetch(@RequestParam("foreCastInd") String foreCastInd, @RequestParam("adhocInd") char adhocInd,
			@RequestParam("pageNum") int pageNum, Model model, HttpServletRequest request, HttpSession session) {
		if (!loginService.isLoggedIn(model, session)) {
			return Utils.LOGIN;
		}
		boolean forecast = BooleanUtils.toBoolean(foreCastInd);

		SummaryHelper helper = summaryService.getSummary(adhocInd, forecast, loginService.getDataKey(session));
		session.setAttribute("summary_helper", helper);

		if (helper.isPageValid(pageNum)) {
			return JsonUtils.toJson(new AjaxBean(Errors.OK, "", helper.getDataForPg(pageNum)));
		} else {
			return JsonUtils.toJson(new AjaxBean(Errors.PROCESS, ""));
		}
	}

	@RequestMapping(value = "/paginate/{inc}/{pageNum}", method = RequestMethod.GET)
	@ResponseBody
	public String paginate(@PathVariable int inc, @PathVariable int pageNum, Model model, HttpServletRequest request,
			HttpSession session) {
		if (!loginService.isLoggedIn(model, session)) {
			return Utils.LOGIN;
		}

		SummaryHelper helper = (SummaryHelper) session.getAttribute("summary_helper");
		int newPg = pageNum + inc;

		if (helper.isPageValid(newPg)) {
			return JsonUtils.toJson(new AjaxBean(Errors.OK, "", helper.getDataForPg(newPg)));
		} else {
			return JsonUtils.toJson(new AjaxBean(Errors.PROCESS, ""));
		}
	}
}
