package sasrestro.sessionejb.restaurant;

import java.io.Serializable;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import sasrestro.model.restaurant.MenuItemModel;
import sasrestro.sessionejb.core.GenericDAO;

@Stateless
@LocalBean
public class MenuItemEJB extends GenericDAO<MenuItemModel> implements Serializable {

	private static final long serialVersionUID = 1L;

	public MenuItemEJB() {
		super(MenuItemModel.class);

	}

}
