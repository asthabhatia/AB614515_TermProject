package com.loginregister;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@ManagedBean
@SessionScoped
@FacesValidator("registerationValidator")
public class RegisterationValidator implements Validator {

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\."
			+ "[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*"
			+ "(\\.[A-Za-z]{2,})$";

	private static final String phone_PATTERN = "[0-9 ]{10,10}+";

	private Pattern emailPattern;
	private Pattern numberPattern;
	private Matcher matcher;

	public RegisterationValidator() {
		emailPattern = Pattern.compile(EMAIL_PATTERN);
		numberPattern = Pattern.compile(phone_PATTERN);
	}

	public void emailValidate(FacesContext arg0, UIComponent arg1, Object obj)
			throws ValidatorException {

		matcher = emailPattern.matcher(obj.toString());
		if (!matcher.matches()) {

			FacesMessage msg = new FacesMessage("E-mail validation failed.",
					"Invalid E-mail format.");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);

		}

	}

	public void numberValidate(FacesContext arg0, UIComponent arg1, Object value)
			throws ValidatorException {

		matcher = numberPattern.matcher(value.toString());
		if (!matcher.matches()) {

			FacesMessage msg = new FacesMessage("Phone validation failed",
					"Invalid phone number.");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);

		}
	}

	public void usernameExistValidate(FacesContext arg0, UIComponent arg1,
			Object user) throws ValidatorException {
		String username = (String) user;

		boolean userExist = RegisterationDAO.isUserExist(username);
		if (userExist == true) {

			FacesMessage msg = new FacesMessage("Username validation failed",
					"This user already exist, Please choose another Username");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);

		}
	}

	@Override
	public void validate(FacesContext arg0, UIComponent arg1, Object arg2)
			throws ValidatorException {
		// TODO Auto-generated method stub

	}
}
