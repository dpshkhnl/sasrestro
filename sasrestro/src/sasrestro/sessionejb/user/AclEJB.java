package sasrestro.sessionejb.user;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import sasrestro.model.user.AccessControlList;
import sasrestro.sessionejb.core.GenericDAO;

@Stateless
@LocalBean
public class AclEJB extends GenericDAO<AccessControlList> {

	public AclEJB()
	{
		super(AccessControlList.class);
		System.out.println("ACL EJB created!");
	}
	@PostConstruct
	private void startBeanLife()
	{
		System.out.println("AclDAO EJBean is Ready for Use!");
	}

}
