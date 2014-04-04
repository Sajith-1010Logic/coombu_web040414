package com.coombu.photobook.presentation.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

public class PasswordValidator implements Validator {
	
	/** Must have at least upper case */
	private static final String UPPER_CASE = ".*[A-Z]+.*";
	
	/** Must have at least lower case */
	private static final String LOWER_CASE = ".*[a-z]+.*";
	
	/** Must have at least Numbers */
	private static final String NUMBERS = ".*[0-9]+.*";
	
	/** Must have at lease upper case */
	private static final String NON_ALPHA_NUM = ".*^([A-Z]|[a-z]|[0-9])+.*";
	
	private int trueConditions;
	
	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		/* Create the correct mask */
/*	      Pattern mask =  null;
	      UIInput ccomp = (UIInput)component.getAttributes().get("confirmPassword");
	      String password  = (String)value;
	      String confirmPassword  = (String)ccomp.getSubmittedValue();
	      
	      //Check if they both are filled in.
	      if (password  == null || password.isEmpty() ||
	    	  confirmPassword == null || confirmPassword.isEmpty()
	      	) 
	      {
	            return; // Let required="true" do its job.
	       }
	      List<FacesMessage> errMsgs = new ArrayList<FacesMessage>();
	      
	      mask = Pattern.compile(UPPER_CASE);
	      Matcher match = mask.matcher(password);	      
	      if ( match.matches()) trueConditions++;
	      
	      mask = Pattern.compile(LOWER_CASE);
	      match = mask.matcher(password);	      
	      if ( match.matches()) trueConditions++;
	      
	      mask = Pattern.compile(NUMBERS);
	       match = mask.matcher(password);	      
	      if ( match.matches()) trueConditions++;
	      
	      mask = Pattern.compile(NON_ALPHA_NUM);
	      match = mask.matcher(password);	      
	      if ( match.matches()) trueConditions++;
	      
	      if (trueConditions < 3)
	      {
	    	  FacesMessage message = new FacesMessage();
	          message.setDetail (Messages.getString("PasswordValidator.three.character.class.required"));
	          message.setSummary(Messages.getString("PasswordValidator.three.character.class.required"));
	          message.setSeverity(FacesMessage.SEVERITY_ERROR);
	          errMsgs.add(message);
	      }
	      
	      if ( password.length()< 6)
	      {
	    	  FacesMessage message = new FacesMessage();
	          message.setDetail(Messages.getString("PasswordValidator.six.character.minimum"));
	          message.setSummary(Messages.getString("PasswordValidator.six.character.minimum"));
	          message.setSeverity(FacesMessage.SEVERITY_ERROR);
	          errMsgs.add(message);
	      }
	      if (password.toLowerCase().contains(firstName.toLowerCase()))
	      {
	    	  FacesMessage message = new FacesMessage();
	          message.setDetail(Messages.getString("PasswordValidator.first.name.not.allowed"));
	          message.setSummary(Messages.getString("PasswordValidator.first.name.not.allowed"));
	          message.setSeverity(FacesMessage.SEVERITY_ERROR);
	          errMsgs.add(message);
	      }
	      if (password.toLowerCase().contains(lastName.toLowerCase()))
	      {
	    	  FacesMessage message = new FacesMessage();
	          message.setDetail(Messages.getString("PasswordValidator.last.name.not.allowed"));
	          message.setSummary(Messages.getString("PasswordValidator.last.name.not.allowed"));
	          message.setSeverity(FacesMessage.SEVERITY_ERROR);
	          errMsgs.add(message);
	      }
	      if ( !password.equals(confirmPassword))
	      {
	    	  FacesMessage message = new FacesMessage();
	          message.setDetail(Messages.getString("PasswordValidator.password.confirm.match"));
	          message.setSummary(Messages.getString("PasswordValidator.password.confirm.match"));
	          message.setSeverity(FacesMessage.SEVERITY_ERROR);
	          errMsgs.add(message);
	      }
	      if(!errMsgs.isEmpty())
	      {
	    	  throw new ValidatorException(errMsgs);
	      }
*/	}

}
