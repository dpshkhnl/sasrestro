package sasrestro.sessionejb.account;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import sasrestro.model.account.BudgetModel;
import sasrestro.sessionejb.core.GenericDAO;

@Stateless
@LocalBean
public class BudgetEJB extends GenericDAO<BudgetModel> {

	public BudgetEJB() {
		super(BudgetModel.class);
	}

	public List<BudgetModel> findByFsYr(int fyId) {
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("fsIdPassed", fyId);

		return super.findAllWithGivenCondition(
				BudgetModel.FIND_BUDGET_BY_FY_ID, parameters);
	}

	public List<BudgetModel> findByFsYrAccType(int fyId, int accType) {
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("fyIdPassed", fyId);
		parameters.put("accTypePassed", accType);

		return super.findAllWithGivenCondition(
				BudgetModel.FIND_BUDGET_BY_FY_ID_ACC_TYPE, parameters);
	}

}
