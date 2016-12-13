package sasrestro.misc;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import sasrestro.model.user.User;


@FacesConverter(value="userConverter", forClass=User.class)
public class UserConverter implements Converter{

	public Object getAsObject(FacesContext context, UIComponent component, String submittedValue) {
		if(submittedValue!=null && !submittedValue.equals("") && !submittedValue.equals("Select") )
		{
			@SuppressWarnings("unchecked")
			List<User> applicationList = (List<User>) context.getApplication().
			evaluateExpressionGet(context, "#{userMB.allUsers}", List.class);

			System.out.println("1CCC---->>>" +submittedValue);

			for (User application : applicationList) {

				if (Integer.valueOf(application.getId()).equals(Integer.valueOf(submittedValue))) {
					return application;
				}
			}
		}
		return null;
	}

	public String getAsString(FacesContext context, UIComponent component, Object object) {
		//return Integer.toString(((Child) object).getChildId());
		if(object != null && !object.equals(""))
		{
			return  Integer.toString(((User) object).getId());
			
		}
		else
			return null;
	}

}