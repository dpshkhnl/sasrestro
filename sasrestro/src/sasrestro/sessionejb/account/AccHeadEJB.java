package sasrestro.sessionejb.account;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

import sasrestro.model.account.AccHeadMcg;
import sasrestro.sessionejb.core.GenericDAO;

/**
 * @author nebula
 * 
 */
@Stateless
@LocalBean
public class AccHeadEJB extends GenericDAO<AccHeadMcg> {
	public AccHeadEJB() {
		super(AccHeadMcg.class);
	}

	public AccHeadMcg findAllByPkJoin(int passedAccHeadId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("accHeadIdPassed", passedAccHeadId);

		return super.findOneResult(AccHeadMcg.FIND_BY_ACC_HEAD_ID, parameters);
	}

	public List<AccHeadMcg> listParentNodeOnly() {
		return findAllWithGivenCondition(AccHeadMcg.LIST_PARENT_NODE_ONLY, null);
	}

	public List<AccHeadMcg> getChildren(int parentId) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("parentPassed", parentId);

		return findAllWithGivenCondition(AccHeadMcg.LIST_CHILDREN, parameter);
	}

	public List<AccHeadMcg> listAll() {
		List<AccHeadMcg> result = findAll();
		return result;
	}

	public List<AccHeadMcg> listRootNodes() {
		return super
				.findAllWithGivenCondition(AccHeadMcg.LIST_ROOT_NODES, null);
	}

	public void createAccountHead(AccHeadMcg accHeadMcg) {
		save(accHeadMcg);
	}

	public void updateAccHead(AccHeadMcg accHeadMcg) {
		AccHeadMcg persistedAccHeadMcg = find(accHeadMcg.getAccHeadId());
		persistedAccHeadMcg = accHeadMcg;

		update(persistedAccHeadMcg);
	}

	public void deleteAccHead(AccHeadMcg accHeadMcg) {
		delete(accHeadMcg.getAccHeadId(), AccHeadMcg.class);
	}

	public AccHeadMcg findAccHeadById(int accHeadId) {
		// accHeadDAO.beginTransaction();
		AccHeadMcg accHeadMcg = find(accHeadId);
		// accHeadDAO.closeTransaction();
		return accHeadMcg;
	}

	/**
	 * @param accCode
	 * 
	 * @return
	 */
	public AccHeadMcg getSelectedAccByAccCode(String accCode) {
		AccHeadMcg result = getAccDataByAccCode(accCode);
		return result;
	}

	public List<AccHeadMcg> getUltimateAccountsHeads() {
		Query query = getEntityManager()
				.createQuery(
						"select a from AccHeadMcg a where a.accHeadId not in (select distinct(aa.parent) from AccHeadMcg aa) order by a.accName asc");
		@SuppressWarnings("unchecked")
		List<AccHeadMcg> listUltimateNodes = query.getResultList();
		return listUltimateNodes;
	}

	/**
	 * @param accCode
	 * @author Ganesh-Magnus This function is used for getting AccData By
	 *         AccCode. Used in Journal VoucherMB Form
	 * @return AccHeadMcg
	 */
	public AccHeadMcg getAccDataByAccCode(String accCode) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("accCode", accCode);
		return super.findOneResult(AccHeadMcg.GET_ALL_BY_ACC_CODE, parameter);
	}

	@SuppressWarnings("unchecked")
	public List<AccHeadMcg> getForAccHeadAutoComplete(String accHead) {
		Query query = getEntityManager()
				.createNativeQuery(
						"SELECT a FROM AccHeadMcg a WHERE a.accName LIKE '"
								+ accHead
								+ "%' AND a.parent != 0 AND a.accHeadId NOT IN (SELECT aa.parent FROM AccHeadMcg aa WHERE aa.parent != 0)",
						AccHeadMcg.class);
		return (List<AccHeadMcg>) query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<AccHeadMcg> getForAccHeadAutoCompleteByAccCode(String accCode) {
		Query query = getEntityManager()
				.createNativeQuery(
						"SELECT a FROM AccHeadMcg a WHERE a.accCode LIKE '"
								+ accCode
								+ "%' AND a.parent != 0 AND a.accHeadId NOT IN (SELECT aa.parent FROM AccHeadMcg aa WHERE aa.parent != 0)",
						AccHeadMcg.class);
		return (List<AccHeadMcg>) query.getResultList();
	}

	public Object getChildrenCount(int parentId) {
		return getEntityManager().createQuery(
				"select count(a) from AccHeadMcg a where a.parent=" + parentId
						+ "").getSingleResult();
	}

	public List<AccHeadMcg> getUltimateAccountsOfType(int accType) {
		Query query = getEntityManager()
				.createQuery(
						"select a from AccHeadMcg a where a.accType="
								+ accType
								+ " and a.accHeadId not in (select distinct(aa.parent) from AccHeadMcg aa) "
								+ "order by a.accName");
		@SuppressWarnings("unchecked")
		List<AccHeadMcg> listUltimateNodes = query.getResultList();
		return listUltimateNodes;
	}

	public AccHeadMcg getCashAccount() {
		Query query = getEntityManager()
				.createQuery(
						"select a from AccHeadMcg a where a.accName='Cash In Hand' and a.accHeadId not in (select distinct(aa.parent) from AccHeadMcg aa)");
		return (AccHeadMcg) query.getSingleResult();
	}

	/*
	 * @SuppressWarnings("unchecked") public List<AccHeadMcg> getBankAccount(){
	 * // Query query = getEntityManager().createQuery(
	 * "SELECT a FROM AccHeadMcg a WHERE a.parent =( SELECT aaa.accHeadId FROM AccHeadMcg aaa WHERE aaa.accName = 'Bank' AND  aaa.accHeadId  IN (select distinct(aa.parent) from AccHeadMcg aa))"
	 * ); Query query = getEntityManager().createQuery(
	 * "SELECT a FROM AccHeadMcg a WHERE a.accHeadId IN (SELECT aa.accHeadModel.accHeadId FROM AccHeadMap aa WHERE aa.mappingPurpose='Bank Account')"
	 * );
	 * 
	 * // SELECT a.* FROM acc_head_map_mcg ah INNER JOIN acc_head_mcg a ON
	 * ah.acc_head_id=a.acc_head_id WHERE ah.map_purpose='Bank Account' return
	 * (List<AccHeadMcg>) query.getResultList(); }
	 */

	/**
	 * Lists all the Bank Accounts mapped by Account Head Map
	 * 
	 * @return List object of AccHeadMcg
	 */
	public List<AccHeadMcg> getBankAccount() {
		return findAllWithGivenCondition(AccHeadMcg.GET_ALL_BANK_ACCOUNTS, null);
	}

	/**
	 * Lists all the Cash In Hand Accounts mapped by Account Head Map
	 * 
	 * @return List object of AccHeadMcg
	 */
	public List<AccHeadMcg> getAllCashInHand() {
		return findAllWithGivenCondition(AccHeadMcg.FIND_ALL_CASH_IN_HAND, null);
	}

	/**
	 * This method checks the uniqueness of AccHeadName. Returns true if unique
	 * else false
	 * 
	 * @param name
	 * @return boolean value
	 */
	public boolean checkDuplicateName(String name) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("accName", name);

		if (super.findAllWithGivenCondition(AccHeadMcg.CHECK_DUPICATE_NAME,
				parameters).size() > 0) {
			return false;
		} else {
			return true;
		}
	}

	public List<AccHeadMcg> getChildrenList(int parentId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("parentPassed", parentId);

		return super.findAllWithGivenCondition(AccHeadMcg.LIST_CHILDREN,
				parameters);
	}

	public List<AccHeadMcg> findAllNonRoot(String accCode) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("acc_code_passed", accCode + '%');

		return super.findAllWithGivenCondition(AccHeadMcg.FIND_ALL_NON_ROOT,
				parameters);
	}

	public List<AccHeadMcg> listAllNonRoot() {
		return super.findAllWithGivenCondition(
				AccHeadMcg.FIND_ALL_NON_ROOT_LIST, null);
	}

	public List<AccHeadMcg> listAllNonParent(int parentId) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("passParent", parentId);

		return findAllWithGivenCondition(AccHeadMcg.LIST_NON_PARENT_NODE_ONLY,
				parameter);
	}
}
