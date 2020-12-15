package com.hcl.bb.controller;



import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hcl.bb.appexception.ApplicationException;
import com.hcl.bb.model.DonateBlood;
import com.hcl.bb.model.RequestBlood;
import com.hcl.bb.model.User;
import com.hcl.bb.service.DonateBloodService;
import com.hcl.bb.service.DonationStatus;
import com.hcl.bb.service.RequestBloodService;
import com.hcl.bb.service.RequestStatusService;

@Controller
public class HomePageController {

	@Autowired
	private RequestBloodService requestBloodService;
	
	@Autowired
	private DonateBloodService donateBloodService;
	
	@Autowired
	private RequestStatusService requestStatusService;
	
	@Autowired
	private DonationStatus donationStatus;
	
	@RequestMapping("home")
	public String home(Model model,HttpServletRequest request)
	{
		model.addAttribute("user",request.getSession().getAttribute("user"));
		return "home";
	}
	
	@RequestMapping("requestFront")
	public String requestBloodFront(Model model)
	{
		model.addAttribute("requestBlood",new RequestBlood());
		return "request_blood";
	}
	
	@PostMapping("addRequest")
	public String addRequest(@ModelAttribute("requestBlood") RequestBlood requestBlood,Model model,HttpServletRequest request)
	{ 
		try {
			User user=(User)request.getSession().getAttribute("user");
			requestBloodService.validateFields(requestBlood);
			requestBloodService.addRequest(requestBlood, user);
			return("request-success");
		} catch (ApplicationException e) {
			model.addAttribute("error",e.getMessage());
			return "request_blood";
		}
		
     }
	
	@RequestMapping("donarFront")
	public String donoarFront(Model model)
	{
		model.addAttribute("donationDetails",new DonateBlood());
		return "donate_blood";
	}
	
	@PostMapping("addDonar")
	public String addDonar(@ModelAttribute("donationDetails")DonateBlood donateBlood,Model model,HttpServletRequest request)
	{
		try {
			User user=(User)request.getSession().getAttribute("user");
			donateBloodService.validateDetails(donateBlood);
			donateBloodService.addDonar(donateBlood, user);
			return"donar-success";

		} catch (ApplicationException e) {
			model.addAttribute("error",e.getMessage());
			return "donate_blood";
		}
	}
	
	@RequestMapping("requestStatus")
	public String requestStatus(Model model,HttpServletRequest request)
	{
		User user=(User)request.getSession().getAttribute("user");
		List<RequestBlood> requestStatusList=requestStatusService.getRequestList(user);
		if(requestStatusList.size()>0)
		{
			model.addAttribute("requestList",requestStatusList);
			return "request_status";
		}
		else
		{
			model.addAttribute("message","You haven't raised any request yet");
			return "request_status";
		}

	}
	
	@RequestMapping("donationStatus")
	public String donationStatus(Model model,HttpServletRequest request)
	{
		User user=(User)request.getSession().getAttribute("user");
		List<DonateBlood> donationStatusList=donationStatus.getDonationList(user);
		
		if(donationStatusList.size()>0)
		{
			model.addAttribute("donationList",donationStatusList);
			return "donation_status";
		}
		else
		{
			model.addAttribute("message","You haven't donated yet. Donating blood periodically keeps you healthy.");
			return "donation_status";
		}
	}
}
