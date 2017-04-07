package com.expense.mvc.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.expense.mvc.model.entity.DataKey;
import com.expense.mvc.model.ui.DataKeyUI;
import com.expense.mvc.service.LoginService;
import com.expense.mvc.service.UploadService;
import com.expense.utils.AjaxBean;
import com.expense.utils.Errors;
import com.expense.utils.JsonUtils;
import com.expense.utils.Utils;

@Controller
@RequestMapping("/upload")
public class UploadController {
	private static final Logger logger = LogManager.getLogger("log." + UploadController.class);

	@Autowired
	private MessageSource messages;

	@Autowired
	private LoginService loginService;

	@Autowired
	private UploadService uploadService;

	@RequestMapping(value = "/process", method = RequestMethod.POST)
	@ResponseBody
	public String process1(Model model, HttpServletRequest request, HttpSession session)
			throws FileUploadException, IllegalStateException, IOException, ServletException {
		if (!loginService.isLoggedIn(null, session)) {
			return Utils.ajaxLoginExpired(messages);
		}
		try {
			if (ServletFileUpload.isMultipartContent(request)) {
				DataKeyUI ui = loginService.getDataKeyUI(session);
				// Update only if DataKey is active.
				if (ui.getStatus() == DataKey.Status.ACTIVE.status) {
					String msg = uploadService.upload(request, ui.getDataKey());
					UploadController.logger.info("File uploaded... :: " + msg);
					return JsonUtils.toJson(new AjaxBean(Errors.OK, msg));
				} else {
					return JsonUtils.toJson(new AjaxBean(Errors.PROCESS, Utils.getMsg(messages, "datakey.inactive")));
				}
			} else {
				throw new Exception("No file uploaded..");
			}
		} catch (Exception e) {
			UploadController.logger.error("Upload Failed... ", e);
			return JsonUtils.toJson(new AjaxBean(Errors.PROCESS, "Upload Failed... " + e.getMessage()));
		}
	}
}
