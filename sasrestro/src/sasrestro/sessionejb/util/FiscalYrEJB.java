package sasrestro.sessionejb.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import sasrestro.model.util.FiscalYrModel;
import sasrestro.sessionejb.core.GenericDAO;

@Stateless
@LocalBean

public class FiscalYrEJB extends GenericDAO<FiscalYrModel> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FiscalYrEJB() {
		super(FiscalYrModel.class);
		// TODO Auto-generated constructor stub
	}

	public List<FiscalYrModel> listAllFiscalYr(){
		List<FiscalYrModel> fsList = new ArrayList<FiscalYrModel>();
		fsList = findAllWithGivenCondition(FiscalYrModel.FIND_ALL_FISCAL_YR, null);
		return fsList;
	}
	
	public FiscalYrModel getActiveFsYr(){
				
		return findOneResult(FiscalYrModel.FIND_ACTIVE_FISCAL_YR, null);
	}
}
