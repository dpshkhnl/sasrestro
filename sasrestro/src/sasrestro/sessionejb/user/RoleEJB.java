package sasrestro.sessionejb.user;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import sasrestro.model.user.UserRole;
import sasrestro.sessionejb.core.GenericDAO;

@Stateless
@LocalBean
public class RoleEJB extends GenericDAO<UserRole> {
	 public RoleEJB() {
	        super(UserRole.class);
	        System.out.println("RoleDAO EJB created!");
	    }
	    @PostConstruct
	    private void startBeanLife()
	    {
	    	System.out.println("Role EJBean is Ready for Use!");
	    	}
}
