package com.ranmal.ssd.Assignment.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.ranmal.ssd.Assignment.model.UploadFile;
import com.ranmal.ssd.Assignment.service.AuthorizationService;
import com.ranmal.ssd.Assignment.service.DriveService;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class MainController {

	private Logger mylogger = LoggerFactory.getLogger(MainController.class);

	@Autowired
  AuthorizationService myauthorizationService;

	@Autowired
  DriveService mydriveService;

	//To check whether the user is already authenticated
	@GetMapping("/")
	public String showHomePage() throws Exception {
		if (myauthorizationService.isUserAuthenticated()) {
			mylogger.debug("User is authenticated. Now redirecting to homepage");
			return "redirect:/home";
		} else {
			mylogger.debug("User is not authenticated. Redirecting to sso...");
			return "redirect:/login";
		}
	}

	// Login Page
	@GetMapping("/login")
	public String goToLogin() {
		return "index.html";
	}

 //Home Page
	@GetMapping("/home")
	public String goToHome() {
		return "home.html";
	}


	//Authorize the app with OAuth
	@GetMapping("/googlesignin")
	public void doGoogleSignIn(HttpServletResponse response) throws Exception {
		mylogger.debug("Called SSO ");
		response.sendRedirect(myauthorizationService.authenticateUserViaGoogle());
	}

  //Call back URL
  @GetMapping("/oauth/callback")
  public String saveAuthorizationCode(HttpServletRequest request) throws Exception {
    mylogger.debug("SSO Callback ");
    String code = request.getParameter("code");
    mylogger.debug("SSO Callback Code Value is , " + code);

    if (code != null) {
      myauthorizationService.exchangeCodeForTokens(code);
      return "redirect:/home";
    }
    return "redirect:/login";
  }

  // To Handle file Upload To GDrive
  @PostMapping("/upload")
  public String uploadFile(HttpServletRequest request, @ModelAttribute UploadFile uploadedFile) throws Exception {
    MultipartFile multipartFile = uploadedFile.getMultipartFile();
    mydriveService.uploadFile(multipartFile);
    return "redirect:/home?status=success";
  }

  	//To Handle Logout
	@GetMapping("/logout")
	public String logout(HttpServletRequest request) throws Exception {
		mylogger.debug("Logout invoked...");
		myauthorizationService.removeUserSession(request);
		return "redirect:/login";
	}


}
