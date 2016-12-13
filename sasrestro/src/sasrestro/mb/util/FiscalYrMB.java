package sasrestro.mb.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import sasrestro.mb.user.UserMB;
import sasrestro.misc.AbstractMB;
import sasrestro.model.util.FiscalYrModel;
import sasrestro.sessionejb.util.FiscalYrEJB;

@ViewScoped
@ManagedBean(name = "fiscalYrMB")

public class FiscalYrMB extends AbstractMB implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = UserMB.INJECTION_NAME)
	private UserMB userMB;
	
	@EJB
	private FiscalYrEJB fsYrEJB;
	
	private List<FiscalYrModel> listFsYr;

	public List<FiscalYrModel> getListFsYr() {
		listFsYr = new ArrayList<FiscalYrModel>();
		listFsYr = fsYrEJB.listAllFiscalYr();
		return listFsYr;
	}

	public UserMB getUserMB() {
		return userMB;
	}

	public void setUserMB(UserMB userMB) {
		this.userMB = userMB;
	}

}
