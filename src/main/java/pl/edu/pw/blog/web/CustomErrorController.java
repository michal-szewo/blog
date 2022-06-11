package pl.edu.pw.blog.web;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController  {

	@RequestMapping("/error")
	public String handleError(HttpServletRequest request, Model model) {
	    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
	    
	    String errorMsg = "Ups! Coś poszło nie tak.";
	    if (status != null) {
	        Integer statusCode = Integer.valueOf(status.toString());
	    
	        if(statusCode == HttpStatus.NOT_FOUND.value()) {
	        	errorMsg = "Nie znaleziono strony, której szukasz.";
	        }
	        else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
	        	errorMsg = "Błąd serwera";
	        }
	        else if(statusCode == HttpStatus.FORBIDDEN.value()) {
	        	errorMsg = "Nie masz uprawnień do tego zasobu";
	        }
	    }
	    model.addAttribute("errorMsg",errorMsg);
	    return "error";
	}
}