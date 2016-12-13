package sasrestro.mb.account;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import sasrestro.mb.user.UserMB;
import sasrestro.misc.AbstractMB;
import sasrestro.misc.DirectSqlUtils;
import sasrestro.model.account.AccHeadMcg;
import sasrestro.model.account.BudgetModel;
import sasrestro.model.account.CodeValue;
import sasrestro.sessionejb.account.AccHeadEJB;
import sasrestro.sessionejb.account.BudgetEJB;
import sasrestro.sessionejb.account.CodeValueEJB;
import sasrestro.sessionejb.util.FiscalYrEJB;

@ManagedBean(name = "budgetMB")
@ViewScoped
public class BudgetMB extends AbstractMB implements Serializable {
	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = UserMB.INJECTION_NAME)
	private UserMB userMB;

	@ManagedProperty(value = "#{openingBalMB}")
	private OpeningBalanceMB openingBalMB;

	@EJB
	private BudgetEJB budgetEJB;

	@EJB
	private AccHeadEJB accHeadEJB;

	@EJB
	private CodeValueEJB codeValueEJB;

	@EJB
	private FiscalYrEJB fyEJB;

	private List<BudgetModel> lstBudgetModel;
	private List<CodeValue> lstCodeValueModel;

	private BudgetModel budgetModel;
	private AccHeadMcg accHeadModel;
	private CodeValue codeValueModel;
	private int cvId;
	boolean save;

	int fsId;

	/**
	 * @return the userMB
	 */
	public UserMB getUserMB() {
		return userMB;
	}

	/**
	 * @param userMB
	 *            the userMB to set
	 */
	public void setUserMB(UserMB userMB) {
		this.userMB = userMB;
	}

	/**
	 * @return the openingBalMB
	 */
	public OpeningBalanceMB getOpeningBalMB() {
		return openingBalMB;
	}

	/**
	 * @param openingBalMB
	 *            the openingBalMB to set
	 */
	public void setOpeningBalMB(OpeningBalanceMB openingBalMB) {
		this.openingBalMB = openingBalMB;
	}

	public List<BudgetModel> getLstBudgetModel() {
		return lstBudgetModel;
	}

	public void setLstBudgetModel(List<BudgetModel> lstBudgetModel) {
		this.lstBudgetModel = lstBudgetModel;
	}

	public BudgetModel getBudgetModel() {
		return budgetModel;
	}

	public void setBudgetModel(BudgetModel budgetModel) {
		this.budgetModel = budgetModel;
	}

	public List<CodeValue> getLstCodeValueModel() {
		lstCodeValueModel = codeValueEJB.findByCVType("Account Type");

		return lstCodeValueModel;
	}

	public void setLstCodeValueModel(List<CodeValue> lstCodeValueModel) {
		this.lstCodeValueModel = lstCodeValueModel;
	}

	public AccHeadMcg getAccHeadModel() {
		if (accHeadModel == null) {
			accHeadModel = new AccHeadMcg();
		}

		return accHeadModel;
	}

	public void setAccHeadModel(AccHeadMcg accHeadModel) {
		this.accHeadModel = accHeadModel;
	}

	public CodeValue getCodeValueModel() {
		if (codeValueModel == null) {
			codeValueModel = new CodeValue();
		}

		return codeValueModel;
	}

	public void setCodeValueModel(CodeValue codeValueModel) {
		this.codeValueModel = codeValueModel;
	}

	public int getCvId() {
		return cvId;
	}

	public void setCvId(int cvId) {
		this.cvId = cvId;
	}

	public List<AccHeadMcg> getRootHeads() {
		return accHeadEJB.listRootNodes();
	}

	/**
	 * @return the fsId
	 */
	public int getFsId() {
		return fsId;
	}

	/**
	 * @param fsId
	 *            the fsId to sets
	 */
	public void setFsId(int fsId) {
		this.fsId = fsId;
	}

	public List<BudgetModel> getBudgetList() {
		if (fsId > 0) {
			return DirectSqlUtils
					.getListOfResultSets(
							"SELECT ROW_NUMBER() OVER(ORDER BY b.acc_head_id ASC) AS budget_id,\n"
									+ "b.budget_amt,\n"
									+ "SUM(l.dr_amt)-SUM(l.cr_amt) closing_amt, \n"
									+ "b.acc_head_id,b.fy_id \n"
									+ "FROM budget b\n"
									+ "INNER JOIN acc_head_mcg a ON b.acc_head_id=a.acc_head_id\n"
									+ "INNER JOIN fiscal_yr f ON b.fy_id=f.fy_id\n"
									+ "INNER JOIN ledger_mcg l on a.acc_head_id=l.acc_code_id\n"
									+ "WHERE l.fy_id='"
									+ fsId
									+ "'\n"
									+ "GROUP BY b.acc_head_id,b.fy_id,b.budget_amt",
							BudgetModel.class);
		} else {
			return null;
		}
	}

	public void save() {
		if (save) {
			budgetEJB.saveList(lstBudgetModel);
			save = false;
			displayInfoMessageToUser("Save successful.");
		} else {
			budgetEJB.updateList(lstBudgetModel);
			displayInfoMessageToUser("Update successful.");
		}

		lstBudgetModel = null;
		lstCodeValueModel = null;
		cvId = 0;
	}

	public void getCurrentBudgetList() {
		BudgetModel objBudMod = new BudgetModel();
		int fyId = userMB.getUser().getFyModel().getFyId();
		lstBudgetModel = budgetEJB.findByFsYrAccType(fyId, cvId);

		if (lstBudgetModel.isEmpty()) {
			save = true;
			for (AccHeadMcg obj : accHeadEJB.getUltimateAccountsOfType(cvId)) {
				objBudMod = new BudgetModel();

				objBudMod.setAccHead(accHeadEJB.find(obj.getAccHeadId()));
				objBudMod.setAccType(codeValueEJB.find(cvId));
				objBudMod.setBudgetAmt(0d);
				objBudMod.setFsYr(fyEJB.find(fyId));

				lstBudgetModel.add(objBudMod);
			}
		}
	}
}
