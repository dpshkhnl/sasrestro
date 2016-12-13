package sasrestro.mb.account;

import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import sasrestro.mb.user.UserMB;
import sasrestro.misc.AbstractMB;
import sasrestro.misc.JCalendarFunctions;
import sasrestro.misc.JrUtils;
import sasrestro.model.account.AccHeadMcg;
import sasrestro.model.account.CodeValue;
import sasrestro.model.account.JournalPk;
import sasrestro.model.account.JournalVoucherDetailModel;
import sasrestro.model.account.JournalVoucherModel;
import sasrestro.model.account.VoucherReport;
import sasrestro.model.user.User;
import sasrestro.model.util.FiscalYrModel;
import sasrestro.sessionejb.account.AccHeadEJB;
import sasrestro.sessionejb.account.AccHeadMapEJB;
import sasrestro.sessionejb.account.CodeValueEJB;
import sasrestro.sessionejb.account.JournalVoucherEJB;

/**
 * @author Ganesh-Magnus
 * 
 */
@ViewScoped
@ManagedBean(name = "journalMB")
public class JournalVoucherDetailMB extends AbstractMB implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String INJECTION_NAME_JV = "#{journalMB}";

	private JournalVoucherModel jvmSummary;
	private JournalVoucherDetailModel jvmDetails;

	private List<JournalVoucherModel> journalVoucherDataList;
	private List<JournalVoucherDetailModel> journalDataList;

	@EJB
	private JournalVoucherEJB journalEJB;

	@EJB
	private AccHeadEJB accHeadEjb;

	@EJB
	private CodeValueEJB codeValueEJB;

	@EJB
	private AccHeadMapEJB accHeadMapEJB;

	Date d = new Date();
	java.sql.Date sd = new java.sql.Date(d.getTime());

	@ManagedProperty(value = UserMB.INJECTION_NAME)
	private UserMB userMB;

	private TreeNode root;

	private String narration;
	private double drAmt, crAmt, totalDr, totalCr, amount, displyAMount;
	private int accType;
	private AccHeadMcg accMcg;

	private double drMinusCr;

	private Map<String, Integer> jvTypeMap;

	private int jvType;

	private String searchedAccHead;

	private int fsYr;

	private Date frmDt;

	private Date toDt;

	private String frmDtNp;

	private String toDtNp;

	private JournalVoucherModel jvPrint;

	/**
	 * @return the frmDtNp
	 */
	public String getFrmDtNp() {
		return frmDtNp;
	}

	/**
	 * @param frmDtNp
	 *            the frmDtNp to set
	 */
	public void setFrmDtNp(String frmDtNp) {
		this.frmDtNp = frmDtNp;
	}

	private int jvNo;

	private boolean unApproved;

	/**
	 * @return the fsYr
	 */
	public int getFsYr() {
		return fsYr;
	}

	/**
	 * @param fsYr
	 *            the fsYr to set
	 */
	public void setFsYr(int fsYr) {
		this.fsYr = fsYr;
	}

	/**
	 * @return the frmDt
	 */
	public Date getFrmDt() {
		return frmDt;
	}

	/**
	 * @param frmDt
	 *            the frmDt to set
	 */
	public void setFrmDt(Date frmDt) {
		this.frmDt = frmDt;
	}

	/**
	 * @return the toDt
	 */
	public Date getToDt() {
		return toDt;
	}

	/**
	 * @param toDt
	 *            the toDt to set
	 */
	public void setToDt(Date toDt) {
		this.toDt = toDt;
	}

	public int getAccType() {
		return accType;
	}

	public void setAccType(int accType) {
		this.accType = accType;
	}

	public double getDisplyAMount() {
		return displyAMount;
	}

	public void setDisplyAMount(double displyAMount) {
		this.displyAMount = displyAMount;
	}

	public double getTotalDr() {
		return totalDr;
	}

	public void setTotalDr(double totalDr) {
		this.totalDr = totalDr;
	}

	public double getTotalCr() {
		return totalCr;
	}

	public void setTotalCr(double totalCr) {
		this.totalCr = totalCr;
	}

	public String getNarration() {
		return narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public void setUserMB(UserMB userMB) {
		this.userMB = userMB;
	}

	public AccHeadMcg getAccMcg() {
		return accMcg;
	}

	public void setAccMcg(AccHeadMcg accMcg) {
		this.accMcg = accMcg;
	}

	public double getDrAmt() {

		return drAmt;
	}

	public void setDrAmt(double drAmt) {
		this.drAmt = drAmt;
	}

	public double getCrAmt() {
		return crAmt;
	}

	public void setCrAmt(double crAmt) {
		this.crAmt = crAmt;
	}

	public JournalVoucherDetailModel getJvmDetails() {
		if (jvmDetails == null) {
			jvmDetails = new JournalVoucherDetailModel();
		}
		return jvmDetails;
	}

	public void setJvmDetails(JournalVoucherDetailModel jvmDetails) {
		this.jvmDetails = jvmDetails;
	}

	public List<JournalVoucherDetailModel> getJournalDataList() {
		if (journalDataList == null) {
			journalDataList = new ArrayList<JournalVoucherDetailModel>();
		}
		return journalDataList;
	}

	public void setJournalDataList(
			List<JournalVoucherDetailModel> setJournalData) {
		this.journalDataList = setJournalData;
	}
	
	public void loadTableData() {
		this.jvmDetails = null;

		if (accMcg == null || accMcg.getAccHeadId() <= 0) {
			displayErrorMessageToUser("Please select the account head. ");
		} else if (getDrAmt() == 0.0 && getCrAmt() == 0.0) {
			displayErrorMessageToUser("Dr and Cr is Empty ");
		} else if (getDrAmt() > 0 && getCrAmt() > 0) {
			displayErrorMessageToUser("Please entry the amount properly");
		} else if ((accMcg.getAccType() == 1 || accMcg.getAccType() == 2)
				&& getCrAmt() > 0 && getDisplyAMount() < getCrAmt()) {
			displayErrorMessageToUser(accMcg.getAccCode()
					+ "--"
					+ accMcg.getAccName()
					+ " is a Dr. Type of Account with insufficent Current Balance.");
		} else if ((accMcg.getAccType() == 3 || accMcg.getAccType() == 4)
				&& getDrAmt() > 0 && getDisplyAMount() < getDrAmt()) {
			displayErrorMessageToUser(accMcg.getAccCode()
					+ "--"
					+ accMcg.getAccName()
					+ " is a Cr. Type of Account with insufficent Current Balance.");
		} else {
			getJvmSummary();
			getJvmDetails();
			// further treatement of jvSummary
			jvmSummary.setJvDateAd(new Date());
			jvmSummary.setJvDateBs(new JCalendarFunctions()
					.getNepaliDate(jvmSummary.getJvDateAd()));
			jvmSummary.setToFromType(1);

			jvmDetails.setAccountHead(getAccMcg());
			jvmDetails.setNarration(getNarration());
			// jvmDetails.setUpdatedDate(sd);
			// jvmDetails.setRemarks(jvmSummary.getNarration());
			// jvmDetails.setCreatedDate(sd);
			jvmDetails.setDrAmt(getDrAmt());
			jvmDetails.setCrAmt(getCrAmt());
			jvmDetails.setBr_id(1);
			// jvmDetails.setCreatedBy(userMB.getUser().getId());

			if (accMcg.getAccCode() != null) {
				jvmDetails.setAccountHead(accHeadEjb
						.getSelectedAccByAccCode(accMcg.getAccCode()));
			}

			jvmDetails.setJvm(jvmSummary);

			getJournalDataList().add(jvmDetails);

			updateBalanceSums();
			/* reinitialize the variable */
			accMcg = null;
			drAmt = 0.0;
			crAmt = 0.0;
			// accCode = "";
			// accHead = "";
			displyAMount = 0.0;
			narration = "";
		}

	}

	private void updateBalanceSums() {
		// update sum balances
		if (journalDataList != null && journalDataList.size() > 0) {
			double drSum = 0.00, crSum = 0.00;
			for (JournalVoucherDetailModel jvdm : journalDataList) {
				if (jvdm.getDrAmt() > 0.00 && jvdm.getCrAmt() <= 0.00)
					drSum += jvdm.getDrAmt();
				else if (jvdm.getCrAmt() > 0.00 && jvdm.getDrAmt() <= 0.00)
					crSum += jvdm.getCrAmt();

			}
			totalDr = drSum;
			totalCr = crSum;

			setDrMinusCr(totalDr - totalCr);
		} else {
			totalDr = 0.00;
			totalCr = 0.00;
			setDrMinusCr(0.0);
		}
	}

	private List<JournalVoucherDetailModel> objJVMDeatils;

	public List<JournalVoucherDetailModel> getObjJVMDeatils() {
		if (objJVMDeatils == null) {
			objJVMDeatils = new ArrayList<JournalVoucherDetailModel>();
		}
		return objJVMDeatils;
	}

	public void setObjJVMDeatils(List<JournalVoucherDetailModel> objJVMDeatils) {
		this.objJVMDeatils = objJVMDeatils;
	}

	public JournalVoucherModel getJvmSummary() {
		if (jvmSummary == null) {
			jvmSummary = new JournalVoucherModel();
			if (jvmSummary.getFiscalYrModel() == null)
				jvmSummary.setFiscalYrModel(new FiscalYrModel());

			if (jvmSummary.getJvType() == null) {
				jvmSummary.setJvType(new CodeValue());
			}

			if (jvmSummary.getApprovedBy() == null)
				jvmSummary.setApprovedBy(new User());

			if (jvmSummary.getAuditedBy() == null)
				jvmSummary.setAuditedBy(new User());

			if (jvmSummary.getSubmittedBy() == null)
				jvmSummary.setSubmittedBy(new User());

			if (jvmSummary.getReceivedBy() == null)
				jvmSummary.setReceivedBy(new User());

			if (jvmSummary.getPostedBy() == null)
				jvmSummary.setPostedBy(new User());

			if (jvmSummary.getCreatedBy() == null)
				jvmSummary.setCreatedBy(new User());
			if (jvmSummary.getUpdatedBy() == null)
				jvmSummary.setUpdatedBy(new User());

		}
		return jvmSummary;
	}

	public void setJvmSummary(JournalVoucherModel jvmSummary) {
		this.jvmSummary = jvmSummary;
	}

	public List<JournalVoucherModel> getObjAllJVM() {
		if (journalVoucherDataList == null)
			journalVoucherDataList = new ArrayList<JournalVoucherModel>();

		// journalVoucherDataList = journalEJB.listAll();

		if (unApproved) {
			return journalVoucherDataList = journalEJB.listAll();
		}
		if (jvType > 0 && frmDt != null && toDt != null && jvNo > 0) {
			return journalEJB.getByJvTypeJvDtJvNo(jvType, frmDt, toDt, jvNo);
		} else if (jvType > 0 && frmDt != null && toDt != null) {
			return journalEJB.getByJvTypeJvDate(jvType, frmDt, toDt);
		} else if (jvType > 0 && jvNo > 0) {
			return journalEJB.getByJvTypeJvNo(jvType, jvNo);
		} else if (frmDt != null && toDt != null && jvNo > 0) {
			return journalEJB.getByJvDtJvNo(frmDt, toDt, jvNo);
		} else if (jvType > 0) {
			return journalEJB.getByJvType(jvType);
		} else if (frmDt != null && toDt != null) {
			return journalEJB.getByJvDt(frmDt, toDt);
		} else if (jvNo > 0) {
			return journalEJB.findByJvNo(jvNo);
		}

		return journalVoucherDataList;
	}

	/*
	 * public void setObjAllJVM(List<JournalVoucherModel> objAllJVM) {
	 * this.objAllJVM = objAllJVM; }
	 */

	public void setObjAllJVM(List<JournalVoucherModel> objAllJVM) {

		this.journalVoucherDataList = objAllJVM;
	}

	public void viewJournalEntryVoucher() {

	}

	public void valueChangedListener(ValueChangeEvent event) {
		/*
		 * System.out.println("Helloooooooooo");
		 * System.out.println("get Value  " + event.getNewValue().toString());
		 */
		displyAMount = 1000;
	}

	public void saveJournalVoucher() {
		if (userMB.getUser().isAdmin()) {
			try {

				getJvmSummary();

				if ((totalDr > 0.00 && totalCr > 0.00) && totalDr == totalCr) {
					// jvmSummary.setStatus(0);//unapproved journal
					jvmSummary.setReceiptAmt(totalDr);
					jvmSummary.setNarration(getNarration());
					if (jvmSummary.getJournalPk() == null) {
						jvmSummary.setJvDateAd(userMB.getUser()
								.getDayInStatus().getTransDateEn());
						jvmSummary.setJvDateBs(userMB.getUser()
								.getDayInStatus().getTransDateNp());
					}
					/*
					 * jvmSummary.setJvDateAd(new Date());
					 * jvmSummary.setJvDateBs(new
					 * JCalendarFunctions().getNepaliDate
					 * (jvmSummary.getJvDateAd()));
					 */

					// jvmSummary.setPostedBy(userMB.getUser());
					// jvmSummary.setUpdatedBy(userMB.getUser());
					jvmSummary.setBrId(1);

					// List<JournalVoucherDetailModel> listJVDet =
					// journalDataList;
					for (JournalVoucherDetailModel jvdm : journalDataList) {
						// update the jv_no in jvdm items
						jvdm.setJvm(jvmSummary);
						// jvdm.setNarration(jvmSummary.getNarration());
					}
					jvmSummary.setJvdmList(journalDataList);
					// journalEJB.saveJournalVoucher(jvmSummary);
					if (jvmSummary.getJournalPk() != null
							&& (jvmSummary.getJournalPk().getJvNo() > 0
									&& jvmSummary.getJournalPk().getFyId() > 0 && jvmSummary
									.getJournalPk().getJvType() > 0)) {
						// update case
						// jvmSummary.setUpdatedDate(new Date());
						jvmSummary.setUpdatedBy(userMB.getUser());
						jvmSummary.getLedgerList().clear();
						journalEJB.postToLedger(jvmSummary);
						// journalEJB.update(jvmSummary);
						displayInfoMessageToUser("Journal Voucher Updated Sucessfully.");
						jvmSummary = null;
						jvmDetails = null;
						accMcg = null;
						journalDataList = null;
						journalVoucherDataList = null;
						totalCr = 0.0;
						totalDr = 0.0;
						loadTree();
					} else {
						jvmSummary.setJvType(codeValueEJB.find(jvType));
						jvmSummary.setFiscalYrModel(userMB.getUser()
								.getFyModel());
						// jvmSummary.setJvNo(journalEJB.getMaxJvNo(jvmSummary.getFiscalYrModel().getFyId()));
						jvmSummary.setJvNo(journalEJB.getMaxJvNoWithJvType(
								jvmSummary.getFiscalYrModel().getFyId(),
								jvmSummary.getJvType().getCvId()));
						// jvmSummary.setUpdatedDate(sd);
						// jvmSummary.setCreatedDate(sd);
						// jvmSummary.setRemarks(getNarration());
						jvmSummary.setCreatedBy(userMB.getUser());
						jvmSummary.setApprovedBy(null);
						jvmSummary.setAuditedBy(null);
						jvmSummary.setPostedBy(null);
						jvmSummary.setSubmittedBy(null);
						jvmSummary.setUpdatedBy(null);
						jvmSummary.setStatus(1);// direct approval
						/*
						 * if(journalEJB.saveJournalVoucher(jvmSummary)) {
						 * jvmSummary=null; jvmDetails=null; accMcg=null;
						 * journalDataList=null; journalVoucherDataList=null;
						 * totalCr=0.0; totalDr=0.0; getJvmSummary();
						 * displayInfoMessageToUser
						 * ("Journal Voucher Created With Sucess");
						 * 
						 * }
						 */

						if (journalEJB.postToLedgerDirectApproval(jvmSummary)) {
							jvmSummary = null;
							jvmDetails = null;
							accMcg = null;
							journalDataList = null;
							journalVoucherDataList = null;
							totalCr = 0.0;
							totalDr = 0.0;
							unApproved = true;
							getJvmSummary();
							displayInfoMessageToUser("Journal Voucher Created With Sucess");
							loadTree();
						}
					}
				} else {
					displayErrorMessageToUser("Total Dr. and Cr. balances of Transaction are not equal. Invalid Trxn Attempt.");
					return;
				}

			} catch (Exception e) {
				displayErrorMessageToUser("Opps... something went wrong.");
				e.printStackTrace();
				// displayErrorMessageToUser("Ops, we could not create the Journal Voucher now. Try again later");
			}
		} else {
			// displayErrorMessageToUser("Sorry, You are not ADMIN user. CREATE operation is allowed only to ADMIN Users.");
		}
	}

	/**
	 * Approve journal voucher by jv_no and fy_id
	 * 
	 * @author Ganesh-Magnus
	 * 
	 */
	public void approveVoucher() {
		if (userMB.getUser().isAdmin()) {
			FacesContext fc = FacesContext.getCurrentInstance();
			Map<String, String> val = fc.getExternalContext()
					.getRequestParameterMap();
			int jvNo = Integer.parseInt(val.get("jv_no"));
			// this is not used because fiscal id is not set but used later by
			// Ganesh
			int fyId = Integer.parseInt(val.get("fy_id"));
			int jvType = Integer.parseInt(val.get("jv_type"));
			/*
			 * System.out
			 * .println("--------------Approve Voucher methood is called.");
			 * System.out.println("Jv no-- " + jvNo);
			 */
			JournalPk jp = new JournalPk();
			jp.setFyId(fyId);
			jp.setJvNo(jvNo);
			jp.setJvType(jvType);
			try {
				getJvmSummary();
				jvmSummary = journalEJB.find(jp);
				jvmSummary.setUpdatedBy(userMB.getUser());
				jvmSummary.setStatus(1);
				jvmSummary.setUpdatedDate(sd);

				// Only auditedBy field is updated.(Because Ledger Posting is
				// already done while saving.)
				jvmSummary.setAuditedBy(userMB.getUser());

				// journalDataList=jvmSummary.getJvdmList();
				journalEJB.update(jvmSummary);
				displayInfoMessageToUser("Journal Voucher No. " + jvNo
						+ " has been approved.");
				loadTree();

				/*
				 * if(journalEJB.postToLedger(jvmSummary)) {
				 * displayInfoMessageToUser("Journal Voucher No. " + jvNo+
				 * " has been approved."); loadTree(); }
				 */

			} catch (Exception e) {
				displayErrorMessageToUser("Approval Operation of Journal Voucher No. "
						+ jvNo + " has Failed.");
				e.printStackTrace();

			}
		} else {
			FacesMessage fms = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Not Authorized User",
					"JSorry, You are not ADMIN user. CREATE operation is allowed only to ADMIN Users.");
			FacesContext.getCurrentInstance().addMessage("Error", fms);

		}

	}

	/**
	 * 
	 */
	@SuppressWarnings("unused")
	public void rejectVoucher() {
		if (userMB.getUser().isAdmin()) {
			FacesContext fc = FacesContext.getCurrentInstance();
			Map<String, String> val = fc.getExternalContext()
					.getRequestParameterMap();
			int jvNo = Integer.parseInt(val.get("jv_no"));
			int fyId = Integer.parseInt(val.get("fy_id"));
			int jvType = Integer.parseInt(val.get("jv_type"));
			JournalPk jp = new JournalPk();
			jp.setFyId(fyId);
			jp.setJvNo(jvNo);
			jp.setJvType(jvType);
			try {
				getJvmSummary();
				jvmSummary = journalEJB.find(jp);
				jvmSummary.setUpdatedBy(userMB.getUser());
				jvmSummary.getJournalPk().setJvNo(jvNo);
				jvmSummary.getJournalPk().setFyId(fyId);
				// jvmSummary.setJv_no(jv_no);
				jvmSummary.setStatus(2);
				jvmSummary.setUpdatedDate(sd);
				journalEJB.update(jvmSummary);
				FacesMessage fms = new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Approved", "Journal Voucher number " + jvNo
								+ " is Rejected");

				loadTree();
			} catch (Exception e) {
				FacesMessage fms = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Reject Fail",
						"Journal Voucher is not rejected. Please try again");
			}
		} else {
			FacesMessage fms = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Not Authorized User",
					"JSorry, You are not ADMIN user. CREATE operation is allowed only to ADMIN Users.");
		}

	}

	/**
	 * 
	 */
	public void editVoucher() {
		if (userMB.getUser().isAdmin()) {
			refreshModel();
			journalDataList = null;
			jvmSummary = null;
			getJournalDataList();
			FacesContext fc = FacesContext.getCurrentInstance();
			Map<String, String> val = fc.getExternalContext()
					.getRequestParameterMap();
			int jvNo = Integer.parseInt(val.get("jv_no"));
			int fyId = Integer.parseInt(val.get("fy_id"));
			int jvType = Integer.parseInt(val.get("jv_type"));
			try {
				getJvmSummary();
				// jvmSummary =
				// journalEJB.getJournalVoucherWithJvNoAndFyId(jvNo, fyId);
				JournalPk jp = new JournalPk();
				jp.setFyId(fyId);
				jp.setJvNo(jvNo);
				jp.setJvType(jvType);
				jvmSummary = journalEJB.find(jp);
				List<JournalVoucherDetailModel> jDetail = jvmSummary
						.getJvdmList();
				for (JournalVoucherDetailModel jv : jDetail) {
					journalDataList.add(jv);
					totalCr += jv.getCrAmt();
					totalDr += jv.getDrAmt();
					// System.out.println(jv.getJvDetailId());
				}
				setNarration(jvmSummary.getNarration());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	/**
	 * Refresh the value of journal voucher entry form reset to null called from
	 * journal details(create voucher)
	 */
	public void refreshModel() {
		/*
		 * System.out
		 * .println("Set null to the voucher model and variables.......");
		 */
		journalDataList = null;
		jvmSummary = null;
		jvmDetails = null;
		accMcg = null;
		getJvmSummary();
		getJvmDetails();
		jvmSummary.setReceivedBy(userMB.getUser());
		drAmt = crAmt = displyAMount = totalCr = totalDr = amount = 0.0;
		// accCode = accHead = "";
		narration = "";
		loadTree();
		/*
		 * JournalTreeBean jt = new JournalTreeBean(); jt.getRoot();
		 */
	}

	/**
	 * @param event
	 */
	public void journalEntryRowSelect(SelectEvent event) {
		/*
		 * JournalVoucherDetailModel jvdm = (JournalVoucherDetailModel) event
		 * .getObject();
		 */
		// update currentbalance of this selected account head
		journalDataList.remove(jvmDetails);
		// accCode = jvm.getAccountHead().getAccCode();
		accMcg = jvmDetails.getAccountHead();
		displyAMount = journalEJB.getCurrentBalance(accMcg.getAccHeadId(),
				accMcg.getAccType());
		drAmt = jvmDetails.getDrAmt();
		crAmt = jvmDetails.getCrAmt();
		narration = jvmDetails.getNarration();
		// accHead = jvm.getAccountHead().getAccName();
		// update currBalance on the UI as per selected account head
		/*
		 * if(journalEJB.isUltimateNode(accMcg.getAccHeadId())) {
		 * displyAMount=journalEJB
		 * .getCurrentBalance(accMcg.getAccHeadId(),accMcg.getAccType()); }
		 */
		// System.out.println("Acc Type- "+accType);
		updateBalanceSums();

	}

	/**
	 * 
	 */
	/*
	 * public void updateJournalEntry() { if (userMB.getUser().isAdmin()) {
	 * FacesContext fc = FacesContext.getCurrentInstance(); Map<String, String>
	 * val = fc.getExternalContext() .getRequestParameterMap(); int jv_no =
	 * Integer.parseInt(val.get("jv_no")); int fy_id =
	 * Integer.parseInt(val.get("jv_no")); System.out.println("Jv no-- " +
	 * jv_no);
	 * 
	 * try { getJvmSummary(); jvmSummary.setUpdatedBy(userMB.getUser());
	 * jvmSummary.setUpdatedDate(sd); jvmSummary.setJvdmList(journalDataList);
	 * 
	 * journalEJB.updateJournalVoucher(jvmSummary); FacesMessage fms = new
	 * FacesMessage(FacesMessage.SEVERITY_INFO, "Updated",
	 * "Journal Voucher number " + jv_no + " is Updated");
	 * 
	 * JournalTreeBean jt = new JournalTreeBean(); jt.getRoot(); } catch
	 * (Exception e) { FacesMessage fms = new FacesMessage(
	 * FacesMessage.SEVERITY_ERROR, "Update Fail",
	 * "Journal Voucher is not Updated. Please try again"); } } else {
	 * FacesMessage fms = new FacesMessage( FacesMessage.SEVERITY_ERROR,
	 * "Not Authorized User",
	 * "JSorry, You are not ADMIN user. CREATE operation is allowed only to ADMIN Users."
	 * ); } }
	 */

	public void onNodeSelectForJournal(NodeSelectEvent event) {
		// first reset the UI input fields
		drAmt = crAmt = displyAMount = totalCr = totalDr = amount = 0.0;
		// accCode = accHead = "";
		narration = "";

		TreeNode nodeSelected = event.getTreeNode();
		// System.out.println("Node is selected");
		accMcg = (AccHeadMcg) nodeSelected.getData();
		// System.out.println("From JournalDoc mb selected Node--" +
		// accMcg.getAccCode()+" -- "+accMcg.getAccName());
		if (journalEJB.isUltimateNode(accMcg.getAccHeadId())) {
			displyAMount = journalEJB.getCurrentBalance(accMcg.getAccHeadId(),
					accMcg.getAccType());
		}
		// get and set current balance of the acchead

	}

	public void onAccHeadAutoComplete() {
		drAmt = crAmt = displyAMount = totalCr = totalDr = amount = 0.0;
		narration = "";
		if (journalEJB.isUltimateNode(accMcg.getAccHeadId())) {
			displyAMount = journalEJB.getCurrentBalance(accMcg.getAccHeadId(),
					accMcg.getAccType());
		}
	}

	public TreeNode getRoot() {

		// loadTree();
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	@SuppressWarnings("unused")
	public void loadTree() {
		root = new DefaultTreeNode("Root", null);
		List<JournalVoucherModel> listJVM = getObjAllJVM();
		Iterator<JournalVoucherModel> itr = listJVM.iterator();
		while (itr.hasNext()) {
			JournalVoucherModel jvm = itr.next();
			TreeNode documents = new DefaultTreeNode(new JournalDocument(jvm),
					root);

			List<JournalVoucherDetailModel> listJVDM = jvm.getJvdmList();
			Iterator<JournalVoucherDetailModel> itrjvdm = listJVDM.iterator();
			while (itrjvdm.hasNext()) {
				JournalVoucherDetailModel jvdm = itrjvdm.next();
				TreeNode work = new DefaultTreeNode(new JournalDocument(jvdm),
						documents);
			}
			collapsingORexpanding(documents, true);
		}

	}

	public void collapsingORexpanding(TreeNode n, boolean option) {
		if (n.getChildren().size() == 0) {
			n.setSelected(false);
		} else {
			for (TreeNode s : n.getChildren()) {
				collapsingORexpanding(s, option);
			}
			n.setExpanded(option);
			n.setSelected(false);
		}
	}

	/**
	 * @return the jvTypeMap
	 */
	public Map<String, Integer> getJvTypeMap() {
		if (jvTypeMap == null) {
			jvTypeMap = new HashMap<String, Integer>();

			for (Iterator<CodeValue> iterator = codeValueEJB.findByCVType(
					"Journal Voucher Type").iterator(); iterator.hasNext();) {
				CodeValue codeObj = (CodeValue) iterator.next();
				jvTypeMap.put(codeObj.getCvLbl(), codeObj.getCvId());
			}
		}
		return jvTypeMap;
	}

	public List<CodeValue> getJvTypeList() {
		return codeValueEJB.findByCVType("Journal Voucher Type");
	}

	/**
	 * @return the jvType
	 */
	public int getJvType() {
		return jvType;
	}

	/**
	 * @param jvType
	 *            the jvType to set
	 */
	public void setJvType(int jvType) {
		this.jvType = jvType;
	}

	/**
	 * @param jvTypeMap
	 *            the jvTypeMap to set
	 */
	/*
	 * public void setJvTypeMap(Map<String,CodeValueMB> jvTypeMap) {
	 * this.jvTypeMap = jvTypeMap; }
	 */

	public List<AccHeadMcg> getAccountHeadList(String query) {
		if (query.length() > 1) {
			return accHeadEjb.getForAccHeadAutoComplete(query);
		}
		return null;
	}

	public List<AccHeadMcg> getAccHeadByAccCode(String query) {
		if (query.length() > 1) {
			return accHeadEjb.getForAccHeadAutoCompleteByAccCode(query);
		}
		return null;
	}

	public List<AccHeadMcg> getNonRoots() {
		return accHeadEjb.listAllNonRoot();
	}

	/**
	 * @return the searchedAccHead
	 */
	public String getSearchedAccHead() {
		return searchedAccHead;
	}

	/**
	 * @param searchedAccHead
	 *            the searchedAccHead to set
	 */
	public void setSearchedAccHead(String searchedAccHead) {
		this.searchedAccHead = searchedAccHead;
	}

	public void onAutocompleteItemSelect(SelectEvent se) {
		searchedAccHead = se.getObject().toString();
	}

	@PostConstruct
	protected void setDefaultValues() {
		setJvType(codeValueEJB.getCodeValueByTypeAndLabel(
				"Journal Voucher Type", "Journal Voucher").getCvId());
		/*
		 * setFsYr(userMB.getUser().getFyModel().getFyId());
		 * setFrmDt(userMB.getUser().getFyModel().getStartDate()); setToDt(new
		 * Date());
		 */
		setUnApproved(true);
		loadTree();
		// getJvmSummary().setReceivedBy(userMB.getUser());
		// getReceiptSummary().setReceiptBy(userMB.getUser());
	}

	/**
	 * @return the drMinusCr
	 */
	public double getDrMinusCr() {
		return drMinusCr;
	}

	/**
	 * @param drMinusCr
	 *            the drMinusCr to set
	 */
	public void setDrMinusCr(double drMinusCr) {
		this.drMinusCr = drMinusCr;
	}

	/**
	 * @return the jvNo
	 */
	public int getJvNo() {
		return jvNo;
	}

	/**
	 * @param jvNo
	 *            the jvNo to set
	 */
	public void setJvNo(int jvNo) {
		this.jvNo = jvNo;
	}

	/**
	 * @return the unApproved
	 */
	public boolean isUnApproved() {
		return unApproved;
	}

	/**
	 * @param unApproved
	 *            the unApproved to set
	 */
	public void setUnApproved(boolean unApproved) {
		this.unApproved = unApproved;
	}

	/**
	 * @return the toDtNp
	 */
	public String getToDtNp() {
		return toDtNp;
	}

	/**
	 * @param toDtNp
	 *            the toDtNp to set
	 */
	public void setToDtNp(String toDtNp) {
		this.toDtNp = toDtNp;
	}

	/**
	 * @return the userMB
	 */
	public UserMB getUserMB() {
		return userMB;
	}

	public void updateDatesNP() {
		if (frmDt != null) {
			setFrmDtNp((new JCalendarFunctions()).getNepaliDate(getFrmDt()));
		}
		if (toDt != null) {
			setToDtNp((new JCalendarFunctions()).getNepaliDate(getToDt()));
		}
	}

	public void updateDatesAD() {
		if (frmDtNp != null
				&& frmDtNp.matches("([0-9]{4})-([0-9]{1,2})-([0-9]{1,2})")) {
			setFrmDt((new JCalendarFunctions()).GetEnglishDate(getFrmDtNp()));
		}
		if (toDtNp != null
				&& toDtNp.matches("([0-9]{4})-([0-9]{1,2})-([0-9]{1,2})")) {
			setToDt((new JCalendarFunctions()).GetEnglishDate(getToDtNp()));
		}
	}

	/**
	 * @return the jvPrint
	 */
	public JournalVoucherModel getJvPrint() {
		return jvPrint;
	}

	/**
	 * @param jvPrint
	 *            the jvPrint to set
	 */
	public void setJvPrint(JournalVoucherModel jvPrint) {
		this.jvPrint = jvPrint;
	}

	public void print() throws JRException, IOException {

		List<VoucherReport> vrList = new ArrayList<VoucherReport>();

		Map<String, Object> params = new HashMap<String, Object>();

		for (JournalVoucherDetailModel journalVoucherDetailModel : getJvPrint()
				.getJvdmList()) {
			VoucherReport vObj = new VoucherReport();
			vObj.setCr(journalVoucherDetailModel.getCrAmt());
			vObj.setDr(journalVoucherDetailModel.getDrAmt());
			vObj.setParticulars((vObj.getCr() > 0 ? "To " : "")
					+ journalVoucherDetailModel.getAccountHead().getAccName()
					+ (vObj.getDr() > 0 ? " Dr" : ""));

			if (!getJvPrint().getJvType().getCvLbl().equals("Receipt Voucher")
					&& !getJvPrint().getJvType().getCvLbl()
							.equals("Payment Voucher")) {
				vrList.add(vObj);
			}
			if (getJvPrint().getJvType().getCvLbl().equals("Receipt Voucher")
					|| getJvPrint().getJvType().getCvLbl()
							.equals("Payment Voucher")) {
				if (vObj.getCr() > 0) {
					vrList.add(vObj);
				}

			}

			if (getJvPrint().getJvType().getCvLbl().equals("Receipt Voucher")
					|| getJvPrint().getJvType().getCvLbl()
							.equals("Payment Voucher")) {

				if (vObj.getDr() > 0
						&& accHeadMapEJB
								.checkCashInHandAccountHead(journalVoucherDetailModel
										.getAccountHead().getAccHeadId()) != null) {

					params.put("cashInHand", journalVoucherDetailModel
							.getAccountHead().getAccName());
					params.put("cashInHandAmt", String
							.valueOf(journalVoucherDetailModel.getDrAmt()));

				}

				if (vObj.getDr() > 0
						&& accHeadMapEJB
								.checkBankAccountHead(journalVoucherDetailModel
										.getAccountHead().getAccHeadId()) != null) {
					params.put("bank", journalVoucherDetailModel
							.getAccountHead().getAccName());
					params.put("bankAmt", String
							.valueOf(journalVoucherDetailModel.getDrAmt()));
				}
			}

		}

		params.put("no", getJvPrint().getJvNo());
		params.put(
				"date",
				getJvPrint()
						.getJvDateBs()
						.replaceAll("-", ".")
						.concat("/ "
								+ (new SimpleDateFormat("dd-MMM-yyyy"))
										.format(getJvPrint().getJvDateAd())));
		params.put("voucherType", getJvPrint().getJvType().getCvLbl());
		params.put("inWords", JrUtils.toWordsMiracle((new DecimalFormat(
				"##,##0.00").format(getJvPrint().getReceiptAmt()))));
		params.put("onAccOf", getJvPrint().getNarration());
		params.put("fsyr", "(" + getJvPrint().getFiscalYrModel().getFiscalYr()
				+ ")");

		JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(
				vrList);
		String reportPath = null;
		if (getJvPrint().getJvType().getCvLbl().equals("Receipt Voucher")
				|| getJvPrint().getJvType().getCvLbl()
						.equals("Payment Voucher")) {
			reportPath = FacesContext.getCurrentInstance().getExternalContext()
					.getRealPath("/reports/Receipt Voucher.jasper");
		} else {
			reportPath = FacesContext.getCurrentInstance().getExternalContext()
					.getRealPath("/reports/Journal Voucher.jasper");
		}
		JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath,
				params, beanCollectionDataSource);

		HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext
				.getCurrentInstance().getExternalContext().getResponse();
		httpServletResponse.setContentType("application / pdf");
		httpServletResponse.addHeader("Content-Disposition",
				"inline; filename=Voucher.pdf");
		ServletOutputStream servletOutputStream = httpServletResponse
				.getOutputStream();

		servletOutputStream.write(JasperExportManager
				.exportReportToPdf(jasperPrint));
		servletOutputStream.flush();
		servletOutputStream.close();
		FacesContext.getCurrentInstance().renderResponse();
		FacesContext.getCurrentInstance().responseComplete();

	}
}
