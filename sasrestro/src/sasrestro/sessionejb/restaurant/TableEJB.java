package sasrestro.sessionejb.restaurant;

import java.io.Serializable;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import sasrestro.model.restaurant.TableModel;
import sasrestro.sessionejb.core.GenericDAO;

@Stateless
@LocalBean
public class TableEJB extends GenericDAO<TableModel> implements Serializable {

	private static final long serialVersionUID = 1L;

	public TableEJB() {
		super(TableModel.class);
	
	}

}
