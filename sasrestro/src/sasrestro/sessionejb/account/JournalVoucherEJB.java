package sasrestro.sessionejb.account;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.Query;

import sasrestro.misc.JCalendarFunctions;
import sasrestro.model.account.JournalVoucherDetailModel;
import sasrestro.model.account.JournalVoucherModel;
import sasrestro.model.account.LedgerMcg;
import sasrestro.sessionejb.core.GenericDAO;

/**
 * @author Ganesh-Magnus
 * 
 */
@Stateless
@Local
public class JournalVoucherEJB extends GenericDAO<JournalVoucherModel> {

	@Resource
	EJBContext ejbContext;
	/*
	 * @EJB AccHeadEJB accEjb;
	 */

	public JournalVoucherEJB() {
		super(JournalVoucherModel.class);
		//	System.out.println("Hello i am ----------------");
		// TODO Auto-generated constructor stub
	}

	public List<JournalVoucherModel> getAllPendingVoucher() {

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("status", 0);
		return super.findAllWithGivenCondition(
				JournalVoucherModel.Find_Pending_JV, parameters);

	}

	public List<JournalVoucherModel> getAllUnApproved(){
		return super.findAllWithGivenCondition(JournalVoucherModel.FIND_UNAPPROVED_JV, null);
	}

	/**
	 * @param jv_no
	 * @return Single Journal Voucher Data
	 */
	public JournalVoucherModel getVoucher(int jv_no) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("jv_no", jv_no);
		return super.findOneResult(JournalVoucherModel.FIND_BY_JV_NO,
				parameters);

	}

	public List<JournalVoucherModel> findByJvNo(int jvNo){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("jv_no", jvNo);

		return super.findAllWithGivenCondition(JournalVoucherModel.FIND_BY_JV_NO,
				parameters);

	}

	public List<JournalVoucherModel> listAll() {

		/*List<JournalVoucherModel> result = getAllPendingVoucher();

		return result;*/
		return getAllUnApproved();
	}

	public void updateJournalVoucher(JournalVoucherModel objJVM) {
		//		System.out.println(" Approve Voucher method is called.............");
		JournalVoucherModel jvm = find((int) objJVM.getJvNo());
		jvm = objJVM;
		update(jvm);

	}

	public boolean saveJournalVoucher(JournalVoucherModel jvm) throws Exception {
		try {
			save(jvm);
			return true;
		} catch (Throwable t) {
			ejbContext.setRollbackOnly();
			t.printStackTrace();
			return false;
		}

	}

	public JournalVoucherModel getJournalVoucher(int jv_no) {

		JournalVoucherModel jvm = getVoucher(jv_no);

		return jvm;

	}

	public JournalVoucherModel getJournalVoucherWithJvNoAndFyId(int jvNo,
			int fyId) {
		Query query = getEntityManager().createNativeQuery(
				"SELECT j from JournalVoucherModel j  "
						+ " WHERE  j.journalPk.jvNo=" + jvNo
						+ " and j.journalPk.fyId=" + fyId + "",
						JournalVoucherModel.class);
		JournalVoucherModel jvm = (JournalVoucherModel) query.getSingleResult();
		return jvm;
	}

	//Added by Sudeep
	public JournalVoucherModel getJournalVoucherWithJvNoFyIdAndJvType(int jvNo, int fyId, int jvType){
		Query query = getEntityManager().createNativeQuery("SELECT j from JournalVoucherModel j  "
				+ " WHERE  j.journalPk.jvNo=" + jvNo
				+ " and j.journalPk.fyId=" + fyId + " AND j.journalPk.jvType="+jvType+" ",
				JournalVoucherModel.class);
		return (JournalVoucherModel) query.getSingleResult();
	}

	public double getCurrentBalance(int accCodeId, int accType) {
		Query query = getEntityManager()
				.createQuery(
						"SELECT  ("
								+ (accType == 1 || accType == 2 ? "SUM(l.drAmt)-SUM(l.crAmt)"
										: "SUM(l.crAmt)-SUM(l.drAmt)")
										+ ") as currbal    FROM LedgerMcg l inner join l.accountHead a "
										+ " WHERE    a.accHeadId=" + accCodeId + "");
		Number result = (Number) query.getSingleResult();
		return (Double) (result == null ? 0.00 : result);
	}

	public int getMaxJvNo(int fyId) {
		Query query = getEntityManager()
				.createQuery(
						"SELECT MAX(jvm.journalPk.jvNo) as jvNo from JournalVoucherModel jvm "
								+ "inner join jvm.fiscalYrModel fy"
								+ " WHERE fy.fyId="
								+ fyId + "");
		Number result = (Number) query.getSingleResult();
		int retValue = (Integer) (result == null ? 0 : result);
		return retValue = retValue + 1;
	}

	public int getMaxJvNoWithJvType(int fyId,int jv_type){
		Query query = getEntityManager().createQuery("SELECT MAX(jvm.journalPk.jvNo) as jvNo FROM JournalVoucherModel jvm "
				+ "INNER JOIN jvm.fiscalYrModel fy "
				+ "INNER JOIN jvm.jvType jt "
				+ "WHERE fy.fyId="+fyId+" AND jt.cvId="+jv_type+"");

		Number result = (Number) query.getSingleResult();
		int retValue = (Integer) (result == null ? 0 : result);
		return retValue = retValue + 1;
	}

	/*@PostConstruct
	public void hello() {
		System.out
				.println("************************Hello I am Journal EJB called");
	}*/

	public boolean isUltimateNode(int accHeadId) {
		Query query = getEntityManager().createQuery(
				"SELECT  count(a) as cnt  FROM AccHeadMcg a where a.parent='"
						+ accHeadId + "' ");
		long result = (Long) query.getSingleResult();
		if (result > 0l) {
			return false;
		} else
			return true;
	}

	public boolean postToLedger(JournalVoucherModel jvm) {
		/*
		 * Explicit validation to prevent mall-inputs
		 */
		if (jvm != null && jvm.getJournalPk().getJvNo() > 0
				&& jvm.getJournalPk().getFyId() > 0 && jvm.getJournalPk().getJvType() > 0
				&& jvm.getJvdmList().size() > 0) {
			List<JournalVoucherDetailModel> jvdmList = jvm.getJvdmList();
			// AccHeadMcg
			// testAccHead=accEjb.find(jvdmList.get(0).getAccountHead().getAccHeadId());

			List<JournalVoucherDetailModel> drList = new ArrayList<JournalVoucherDetailModel>();
			List<JournalVoucherDetailModel> crList = new ArrayList<JournalVoucherDetailModel>();
			for (JournalVoucherDetailModel jvdm : jvdmList) {
				// separate the dr and cr jvdm
				if (jvdm.getCrAmt() == 0.00)
					drList.add(jvdm);
				else
					crList.add(jvdm);

				// add the ledger object into jvmSummary's ledgerlist
			}
			List<LedgerMcg> ledgerList = populateLedgerList(jvm, drList, crList);
			// set the ledger info list of jvmSummary
			jvm.setLedgerList(ledgerList);
			try {
				// System.out.println("Selected test AccHead="+testAccHead.getAccHeadId()+" and name ="+testAccHead.getAccName());
				// testAccHead.setAccName(null);//case of failure
				// System.out.println("After modification Selected test AccHead="+testAccHead.getAccHeadId()+" and name ="+testAccHead.getAccName());
				update(jvm);
				// accEjb.save(testAccHead);
				return true;
			} catch (Throwable t) {
				ejbContext.setRollbackOnly();
				t.printStackTrace();
				return false;
			}
		} else
			return false;

	}

	public boolean postToLedgerDirectApproval(JournalVoucherModel jvm) {
		/*
		 * Explicit validation to prevent mall-inputs
		 */
		if (jvm != null && jvm.getJournalPk().getJvNo() > 0
				//		&& jvm.getJournalPk().getFyId() > 0 && jvm.getJournalPk().getJvType() > 0
				&& jvm.getJvdmList().size() > 0) {
			List<JournalVoucherDetailModel> jvdmList = jvm.getJvdmList();
			// AccHeadMcg
			// testAccHead=accEjb.find(jvdmList.get(0).getAccountHead().getAccHeadId());

			List<JournalVoucherDetailModel> drList = new ArrayList<JournalVoucherDetailModel>();
			List<JournalVoucherDetailModel> crList = new ArrayList<JournalVoucherDetailModel>();
			for (JournalVoucherDetailModel jvdm : jvdmList) {
				// separate the dr and cr jvdm
				if (jvdm.getCrAmt() == 0.00)
					drList.add(jvdm);
				else
					crList.add(jvdm);

				// add the ledger object into jvmSummary's ledgerlist
			}
			List<LedgerMcg> ledgerList = populateLedgerList(jvm, drList, crList);
			// set the ledger info list of jvmSummary
			jvm.setLedgerList(ledgerList);
			try {
				// System.out.println("Selected test AccHead="+testAccHead.getAccHeadId()+" and name ="+testAccHead.getAccName());
				// testAccHead.setAccName(null);//case of failure
				// System.out.println("After modification Selected test AccHead="+testAccHead.getAccHeadId()+" and name ="+testAccHead.getAccName());
				save(jvm);
				// accEjb.save(testAccHead);
				return true;
			} catch (Throwable t) {
				ejbContext.setRollbackOnly();
				t.printStackTrace();
				return false;
			}
		} else
			return false;

	}

	/*public boolean postReceiptToAccount(JournalVoucherModel jvm,
			List<ReceiptModel> listReceipt) {
		try {
			// List<Long> pkList= new ArrayList<Long>();
			int result = 0;

			if ((result > 0 && result == listReceipt.size())
					&& postToLedger(jvm)) {
				// Query
				// query=getEntityManager().createNativeQuery("UPDATE receipt set  jv_no="+jvm.getJvNo()+",status=1 where receipt_id IN ('"+pkList+"')");
				// Query
				// query=getEntityManager().createQuery("Select r from ReceiptModel r where r IN :rmList");
				// query.setParameter("rmList", pkList);
				// List<ReceiptModel>rmList=query.getResultList();
				
				 * int result=query.executeUpdate(); if(result>0)
				 
				return true;
			} else {
				ejbContext.setRollbackOnly();
				return false;
			}
		} catch (Exception e) {
			ejbContext.setRollbackOnly();
			e.printStackTrace();
			return false;
		}
	}*/

	private LedgerMcg setCommonLedgerFields(JournalVoucherModel jvm,
			LedgerMcg ledgerMcg) {
		ledgerMcg.setBrId(1);// hard coded
		// ledgerMcg.setRemarks(jvm.getNarration());
		ledgerMcg.setJournalVourcher(jvm);
		ledgerMcg.setFiscalYear(jvm.getFiscalYrModel());
		ledgerMcg.setPostedDate(jvm.getJvDateAd());
		ledgerMcg.setPostedBy(jvm.getPostedBy());
		ledgerMcg.setCreatedDateNp(new JCalendarFunctions()
		.getNepaliDate(ledgerMcg.getPostedDate()));
		ledgerMcg.setUpdatedBy(jvm.getUpdatedBy());
		ledgerMcg.setJvType(jvm.getJvType());

		return ledgerMcg;
	}

	private List<LedgerMcg> populateLedgerList(JournalVoucherModel jvm,
			List<JournalVoucherDetailModel> drJvdmList,
			List<JournalVoucherDetailModel> crJvdmList) {
		if ((drJvdmList != null && crJvdmList != null)
				&& (drJvdmList.size() > 0 && crJvdmList.size() > 0)) {
			List<LedgerMcg> retList = new ArrayList<LedgerMcg>();
			LedgerMcg ledgerMcg = null;
			JournalVoucherDetailModel drJvdm = null, crJvdm = null;

			if (drJvdmList.size() == crJvdmList.size()
					&& drJvdmList.size() == 1) {
				ledgerMcg = new LedgerMcg();
				// case of One vs One dr cr trxn
				drJvdm = drJvdmList.get(0);
				crJvdm = crJvdmList.get(0);

				// dr side
				ledgerMcg = setCommonLedgerFields(jvm, ledgerMcg);

				ledgerMcg.setAccountHead(drJvdm.getAccountHead());
				ledgerMcg.setToAccountHead(crJvdm.getAccountHead());
				ledgerMcg.setCrAmt(drJvdm.getCrAmt());
				ledgerMcg.setDrAmt(drJvdm.getDrAmt());
				ledgerMcg.setRemarks(drJvdm.getNarration());

				// add into list
				retList.add(ledgerMcg);
				// re-initialize for cr side
				ledgerMcg = new LedgerMcg();
				ledgerMcg = setCommonLedgerFields(jvm, ledgerMcg);

				ledgerMcg.setAccountHead(crJvdm.getAccountHead());
				ledgerMcg.setToAccountHead(drJvdm.getAccountHead());
				ledgerMcg.setCrAmt(crJvdm.getCrAmt());
				ledgerMcg.setDrAmt(crJvdm.getDrAmt());
				ledgerMcg.setRemarks(crJvdm.getNarration());

				// add into list
				retList.add(ledgerMcg);

			} else if (crJvdmList.size() > drJvdmList.size()
					&& drJvdmList.size() == 1) {
				// case when Multiple Cr Vs Single Dr.
				for (JournalVoucherDetailModel jvdmLoopCr : crJvdmList) {
					// initialize ledger
					ledgerMcg = new LedgerMcg();
					drJvdm = drJvdmList.get(0);

					// dr side
					ledgerMcg = setCommonLedgerFields(jvm, ledgerMcg);

					ledgerMcg.setAccountHead(drJvdm.getAccountHead());
					ledgerMcg.setToAccountHead(jvdmLoopCr.getAccountHead());
					ledgerMcg.setCrAmt(0.00);
					ledgerMcg.setDrAmt(jvdmLoopCr.getCrAmt());
					ledgerMcg.setRemarks(jvdmLoopCr.getNarration());

					// add into list
					retList.add(ledgerMcg);
					// re-initialize for cr side
					ledgerMcg = new LedgerMcg();

					ledgerMcg = setCommonLedgerFields(jvm, ledgerMcg);

					ledgerMcg.setAccountHead(jvdmLoopCr.getAccountHead());
					ledgerMcg.setToAccountHead(drJvdm.getAccountHead());
					ledgerMcg.setCrAmt(jvdmLoopCr.getCrAmt());
					ledgerMcg.setDrAmt(0.00);
					ledgerMcg.setRemarks(jvdmLoopCr.getNarration());

					// add into list
					retList.add(ledgerMcg);

				}

			} else if (drJvdmList.size() > crJvdmList.size()
					&& crJvdmList.size() == 1) {
				// case when Multiple Dr Vs Single Cr.
				for (JournalVoucherDetailModel jvdmLoopDr : crJvdmList) {
					// initialize ledger
					ledgerMcg = new LedgerMcg();
					crJvdm = crJvdmList.get(0);
					// cr side
					ledgerMcg = setCommonLedgerFields(jvm, ledgerMcg);

					ledgerMcg.setAccountHead(crJvdm.getAccountHead());
					ledgerMcg.setToAccountHead(jvdmLoopDr.getAccountHead());
					ledgerMcg.setCrAmt(jvdmLoopDr.getCrAmt());
					ledgerMcg.setDrAmt(0.00);
					ledgerMcg.setRemarks(jvdmLoopDr.getNarration());

					// add into list
					retList.add(ledgerMcg);
					// re-initialize for cr side
					ledgerMcg = new LedgerMcg();
					ledgerMcg = setCommonLedgerFields(jvm, ledgerMcg);

					ledgerMcg.setAccountHead(jvdmLoopDr.getAccountHead());
					ledgerMcg.setToAccountHead(crJvdm.getAccountHead());
					ledgerMcg.setCrAmt(0.00);
					ledgerMcg.setDrAmt(jvdmLoopDr.getCrAmt());
					ledgerMcg.setRemarks(jvdmLoopDr.getNarration());
					// add into list
					retList.add(ledgerMcg);

				}

			} else if (drJvdmList.size() > 1 && crJvdmList.size() > 1
					&& (drJvdmList.size() > crJvdmList.size())) {
				/*
				 * case when Multiple Dr Vs Multiple Cr with More Dr fewer Cr
				 * Take the advantage of posting lesser data in ledger coz we
				 * are free to do so, as client's degree of freedom
				 */
				for (JournalVoucherDetailModel jvdmLoopCr : crJvdmList) {
					// initialize ledger
					ledgerMcg = new LedgerMcg();
					drJvdm = drJvdmList.get(0);// pairing only with first acc
					// head of drList
					// cr side
					ledgerMcg = setCommonLedgerFields(jvm, ledgerMcg);

					ledgerMcg.setAccountHead(drJvdm.getAccountHead());
					ledgerMcg.setToAccountHead(jvdmLoopCr.getAccountHead());
					ledgerMcg.setCrAmt(0.00);
					ledgerMcg.setDrAmt(jvdmLoopCr.getCrAmt());
					ledgerMcg.setRemarks(jvdmLoopCr.getNarration());

					// add into list
					retList.add(ledgerMcg);
					// re-initialize for cr side
					ledgerMcg = new LedgerMcg();
					ledgerMcg = setCommonLedgerFields(jvm, ledgerMcg);

					ledgerMcg.setAccountHead(jvdmLoopCr.getAccountHead());
					ledgerMcg.setToAccountHead(drJvdm.getAccountHead());
					ledgerMcg.setCrAmt(jvdmLoopCr.getCrAmt());
					ledgerMcg.setDrAmt(0.00);
					ledgerMcg.setRemarks(jvdmLoopCr.getNarration());

					// add into list
					retList.add(ledgerMcg);

				}

			}

			else if (drJvdmList.size() > 1 && crJvdmList.size() > 1
					&& (crJvdmList.size() > drJvdmList.size())) {
				/*
				 * case when Multiple Dr Vs Multiple Cr with More Dr fewer Cr
				 * Take the advantage of posting lesser data in ledger coz we
				 * are free to do so, as client's degree of freedom
				 */
				for (JournalVoucherDetailModel jvdmLoopDr : drJvdmList) {
					// initialize ledger
					ledgerMcg = new LedgerMcg();
					crJvdm = crJvdmList.get(0);// pairing only with first acc
					// head of drList
					// cr side
					ledgerMcg = setCommonLedgerFields(jvm, ledgerMcg);

					ledgerMcg.setAccountHead(crJvdm.getAccountHead());
					ledgerMcg.setToAccountHead(jvdmLoopDr.getAccountHead());
					ledgerMcg.setCrAmt(jvdmLoopDr.getDrAmt());
					ledgerMcg.setDrAmt(0.00);
					ledgerMcg.setRemarks(jvdmLoopDr.getNarration());

					// add into list
					retList.add(ledgerMcg);
					// re-initialize for cr side
					ledgerMcg = new LedgerMcg();
					ledgerMcg = setCommonLedgerFields(jvm, ledgerMcg);

					ledgerMcg.setAccountHead(jvdmLoopDr.getAccountHead());
					ledgerMcg.setToAccountHead(crJvdm.getAccountHead());
					ledgerMcg.setCrAmt(0.00);
					ledgerMcg.setDrAmt(jvdmLoopDr.getDrAmt());
					ledgerMcg.setRemarks(jvdmLoopDr.getNarration());
					// add into list
					retList.add(ledgerMcg);

				}
			}
			else if (drJvdmList.size() > 1 && crJvdmList.size() > 1
					&& (crJvdmList.size() == drJvdmList.size())) {
				/*
				 * case when multiple Dr vs multiple Cr with equal dr and cr
				 * side
				 * Added By Dipesh
				 */
				int i = 0; // to get the multiple credit account
				for (JournalVoucherDetailModel jvdmLoopDr : drJvdmList) {
					// initialize ledger
					ledgerMcg = new LedgerMcg();
					crJvdm = crJvdmList.get(i);// pairing only with first acc
					// head of drList
					// cr side
					ledgerMcg = setCommonLedgerFields(jvm, ledgerMcg);

					ledgerMcg.setAccountHead(crJvdm.getAccountHead());
					ledgerMcg.setToAccountHead(jvdmLoopDr.getAccountHead());
					ledgerMcg.setCrAmt(crJvdm.getCrAmt());
					ledgerMcg.setDrAmt(0.00);
					ledgerMcg.setRemarks(jvdmLoopDr.getNarration());

					// add into list
					retList.add(ledgerMcg);
					// re-initialize for cr side
					ledgerMcg = new LedgerMcg();
					ledgerMcg = setCommonLedgerFields(jvm, ledgerMcg);

					ledgerMcg.setAccountHead(jvdmLoopDr.getAccountHead());
					ledgerMcg.setToAccountHead(crJvdm.getAccountHead());
					ledgerMcg.setCrAmt(0.00);
					ledgerMcg.setDrAmt(jvdmLoopDr.getDrAmt());
					ledgerMcg.setRemarks(jvdmLoopDr.getNarration());
					// add into list
					retList.add(ledgerMcg);
					i++;

				}


			}

			return retList;
		} else
			return null;
	}

	public double checkCurrentBalance(int accCodeId, String drcr) {
		Query query = getEntityManager()
				.createQuery(
						"SELECT  ("
								+ (drcr.equalsIgnoreCase("dr") ? "SUM(l.drAmt)-SUM(l.crAmt)"
										: "SUM(l.crAmt)-SUM(l.drAmt)")
										+ ") as currbal    FROM LedgerMcg l inner join l.accountHead a "
										+ " WHERE    a.accHeadId=" + accCodeId + "");
		Number result = (Number) query.getSingleResult();
		return (Double) (result == null ? 0.00 : result);
	}

	public List<JournalVoucherModel> getByFsYr(int fsYrId){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("fsYrPassed", fsYrId);

		return super.findAllWithGivenCondition(JournalVoucherModel.FIND_BY_FS_YR, parameters);
	}

	public List<JournalVoucherModel> getByJvType(int jvTypeId){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("jvTypePassed", jvTypeId);

		return super.findAllWithGivenCondition(JournalVoucherModel.FIND_BY_JV_TYPE, parameters);
	}

	public List<JournalVoucherModel> getByJvDt(Date frmDt, Date toDt){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("frDt", frmDt);
		parameters.put("toDt", toDt);

		return super.findAllWithGivenCondition(JournalVoucherModel.FIND_BY_JV_DATE, parameters);
	}

	public List<JournalVoucherModel> getByAccHeadId(int accHeadId){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("accHeadPassed", accHeadId);

		return super.findAllWithGivenCondition(JournalVoucherModel.FIND_BY_ACC_HEAD_ID, parameters);
	}

	public List<JournalVoucherModel> getByFsYrJvType(int fsyrId, int jvTypeId){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("fsYrIdPassed", fsyrId);
		parameters.put("jvTypePassed",jvTypeId);

		return super.findAllWithGivenCondition(JournalVoucherModel.FIND_BY_FS_YR_JV_TYPE, parameters);
	}

	public List<JournalVoucherModel> getByFsYrJvDt(int fsYrId, Date frmDt, Date toDt){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("fsYrIdPassed", fsYrId);
		parameters.put("frDt", frmDt);
		parameters.put("toDt", toDt);

		return super.findAllWithGivenCondition(JournalVoucherModel.FIND_BY_FS_YR_JV_DATE, parameters);
	}

	public List<JournalVoucherModel> getByFsYrJvNo(int fsYrId, int jvNo){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("fsYrIdPassed", fsYrId);
		parameters.put("jv_no", jvNo);

		return super.findAllWithGivenCondition(JournalVoucherModel.FIND_BY_FS_YR_JV_NO, parameters);

	}

	public List<JournalVoucherModel> getByFsYrAccHead(int fsYrId, int accHeadId){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("fsYrIdPassed", fsYrId);
		parameters.put("accHeadPassed", accHeadId);

		return super.findAllWithGivenCondition(JournalVoucherModel.FIND_BY_FS_YR_ACC_HEAD_ID, parameters);
	}

	public List<JournalVoucherModel> getByJvTypeJvNo(int jvTypeId, int jvNo){
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("jvTypePassed", jvTypeId);
		parameters.put("jv_no",jvNo);

		return super.findAllWithGivenCondition(JournalVoucherModel.FIND_BY_JV_TYPE_JV_NO, parameters);
	}

	public List<JournalVoucherModel> getByJvTypeJvDate(int jvTypeId, Date frmDt, Date toDt){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("jvTypePassed", jvTypeId);
		parameters.put("frDt", frmDt);
		parameters.put("toDt", toDt);

		return super.findAllWithGivenCondition(JournalVoucherModel.FIND_BY_JV_TYPE_JV_DT, parameters);
	}

	public List<JournalVoucherModel> getByJvTypeAccHead(int jvTypeId, int accHeadId){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("jvTypePassed", jvTypeId);
		parameters.put("accHeadPassed", accHeadId);

		return super.findAllWithGivenCondition(JournalVoucherModel.FIND_BY_JV_TYPE_ACC_HEAD, parameters);
	}

	public List<JournalVoucherModel> getByJvDtJvNo(Date frmDt, Date toDt, int jvNo){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("frDt", frmDt);
		parameters.put("toDt", toDt);
		parameters.put("jv_no", jvNo);

		return super.findAllWithGivenCondition(JournalVoucherModel.FIND_BY_JV_DT_JV_NO, parameters);
	}

	public List<JournalVoucherModel> getByJvDtAccHead(Date frmDt, Date toDt, int accHeadId){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("frDt", frmDt);
		parameters.put("toDt", toDt);
		parameters.put("accHeadPassed", accHeadId);

		return super.findAllWithGivenCondition(JournalVoucherModel.FIND_BY_JV_DT_ACC_HEAD, parameters);
	}

	public List<JournalVoucherModel> getByJvNoAccHead(int jvNo, int accHeadId){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("jv_no", jvNo);
		parameters.put("accHeadPassed", accHeadId);

		return super.findAllWithGivenCondition(JournalVoucherModel.FIND_BY_JV_NO_ACC_HEAD, parameters);
	}

	public List<JournalVoucherModel> getByFsYrJvTypeJvDt(int fsYrId, int jvTypeId, Date frmDt, Date toDt){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("fsYrIdPassed", fsYrId);
		parameters.put("jvTypePassed", jvTypeId);
		parameters.put("frDt", frmDt);
		parameters.put("toDt", toDt);

		return super.findAllWithGivenCondition(JournalVoucherModel.FIND_BY_FS_YR_JV_TYPE_JV_DT, parameters);
	}

	public List<JournalVoucherModel> getByJvTypeJvDtJvNo(int jvTypeId, Date frmDt, Date toDt, int jvNo){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("jvTypePassed", jvTypeId);
		parameters.put("frDt", frmDt);
		parameters.put("toDt", toDt);
		parameters.put("jv_no", jvNo);

		return super.findAllWithGivenCondition(JournalVoucherModel.FIND_BY_JV_TYPE_JV_DT_JV_NO, parameters);
	}

	public List<JournalVoucherModel> getByJvDtJvNoAccHead( Date frmDt, Date toDt, int jvNo, int accHeadId){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("frDt", frmDt);
		parameters.put("toDt", toDt);
		parameters.put("jv_no", jvNo);
		parameters.put("accHeadPassed", accHeadId);

		return super.findAllWithGivenCondition(JournalVoucherModel.FIND_BY_JV_DT_JV_NO_ACC_HEAD, parameters);

	}

	public List<JournalVoucherModel> getByFsYrJvTypeJvDtJvNo(int fsYrId, int jvTypeId, Date frmDt, Date toDt, int jvNo){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("fsYrIdPassed", fsYrId);
		parameters.put("jvTypePassed", jvTypeId);
		parameters.put("frDt", frmDt);
		parameters.put("toDt", toDt);
		parameters.put("jv_no", jvNo);

		return super.findAllWithGivenCondition(JournalVoucherModel.FIND_BY_FS_YR_JV_TYPE_JV_DT_JV_NO, parameters);

	}
}
