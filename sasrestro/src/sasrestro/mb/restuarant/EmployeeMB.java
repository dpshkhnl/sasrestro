package sasrestro.mb.restuarant;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import sasrestro.misc.AbstractMB;
import sasrestro.model.restaurant.EmployeeModel;
import sasrestro.sessionejb.restaurant.EmployeeEJB;

@ViewScoped
@ManagedBean(name="employeeMB")
public class EmployeeMB extends AbstractMB implements Serializable {
	
	@EJB
	EmployeeEJB employeeEJB;
	
	private static final long serialVersionUID = 1L;
	
	private EmployeeModel employeeModel;

	public EmployeeModel getEmployeeModel() {
		if (employeeModel == null)
			employeeModel = new EmployeeModel();
		return employeeModel;
	}

	public void setEmployeeModel(EmployeeModel employeeModel) {
		this.employeeModel = employeeModel;
	}

}
