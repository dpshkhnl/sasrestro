package sasrestro.mb.account;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;

import sasrestro.mb.user.UserMB;
import sasrestro.misc.AbstractMB;
import sasrestro.misc.DirectSqlUtils;
import sasrestro.misc.JCalendarFunctions;
import sasrestro.model.account.AccHeadMcg;
import sasrestro.model.account.LedgerMcg;
import sasrestro.sessionejb.account.AccHeadEJB;

/**
 * @author nebula
 * 
 */
@ViewScoped
@ManagedBean(name = "accHeadMB")
public class AccHeadMB extends AbstractMB implements Serializable {
	public static final String INJECTION_NAME = "#{accHeadMB}";
	private static final long serialVersionUID = 1L;

	private AccHeadMcg accHead, selectedAccHead;
	private List<AccHeadMcg> getSelected;

	@EJB
	AccHeadEJB accHeadEJB;

	private List<AccHeadMcg> allAccHeads;
	//private List<AccHeadMcg> nodeAccHeadsOnly;

	@ManagedProperty(value = UserMB.INJECTION_NAME)
	private UserMB userMB;

	@ManagedProperty(value = JournalVoucherDetailMB.INJECTION_NAME_JV)
	private JournalVoucherDetailMB journalMB;

	public AccHeadMB() {
		// TODO Auto-generated constructor stub
	}

	public AccHeadMcg getAccHead() {
		return accHead;
	}

	public void setAccHead(AccHeadMcg accHead) {
		this.accHead = accHead;
	}

	public AccHeadMcg getSelectedAccHead() {
		return selectedAccHead;
	}

	public void setSelectedAccHead(AccHeadMcg selectedAccHead) {
		this.selectedAccHead = selectedAccHead;
	}

	/*
	 * public AccHeadFacade getAccHeadFacade() { return accHeadFacade; }
	 */
	/*
	 * public void setAccHeadFacade(AccHeadFacade accHeadFacade) {
	 * this.accHeadFacade = accHeadFacade; }
	 */
	public List<AccHeadMcg> getUltimateAccountHeads()
	{
		return accHeadEJB.getUltimateAccountsHeads();
	}

	public List<AccHeadMcg> getAllAccHeads() {
		if (allAccHeads == null) {
			loadAllAccHeads();
		}
		return allAccHeads;
	}

	public void setAllAccHeads(List<AccHeadMcg> allAccHeads) {
		this.allAccHeads = allAccHeads;
	}

	private void loadAllAccHeads() {
		allAccHeads = accHeadEJB.findAll();
	}

	public void resetAccHeadMcg() {
		accHead = new AccHeadMcg();
	}

	public void addNewHead() {
		if (accHead.getMinBal()> 0.00) {
			displayErrorMessageToUser("The Selected Head '"
					+ accHead.getAccName()
					+ "' is a Child Node with Opening Balance.\nIt Can not have child nodes.");
		} else {
			String nextAccCode = (String) DirectSqlUtils
					.getSingleValueFromTable("select max_acc_code("
							+ accHead.getAccHeadId() + ")");
			// Query qry=
			// accHeadEJB.getEntityManager().createNativeQuery("select dbo.max_acc_code("+accHead.getAccHeadId()+")");
			int parent = accHead.getAccHeadId();
			String parentName = null;
			if (parent > 0) {
				parentName = accHeadEJB.find(parent).getAccName();
			}
			String drcr = accHead.getDrcr();
			int accType = accHead.getAccType();
			resetAccHeadMcg();
			accHead.setAccCode(nextAccCode);
			accHead.setParent(parent);
			accHead.setParentName(parentName);
			accHead.setDrcr(drcr);
			accHead.setAccType(accType);
			accHead.setCreatedBy(userMB.getUser().getId());
		}

	}

	/*
	 * public AccHeadFacade getAccHeadFacade() { if (accHeadFacade == null) {
	 * accHeadFacade = new AccHeadFacade(); }
	 * 
	 * return accHeadFacade; }
	 */
	public void onRowSelect(SelectEvent event) {
		// FacesMessage msg = new FacesMessage("Employee Selected",((Employee)
		// event.getObject()).getLastName());
		// FacesContext.getCurrentInstance().addMessage(null, msg);
		this.selectedAccHead = null;
		if (this.selectedAccHead == null) {
			this.selectedAccHead = (AccHeadMcg) event.getObject();
		}
		/*System.out.println("OnRowSelect has AccHead name : "
				+ selectedAccHead.getAccName() + " and acc_code : "
				+ selectedAccHead.getAccCode() + " and accHeadId="
				+ selectedAccHead.getAccHeadId());*/

	}

	public void createAccHead() {
		if (userMB.getUser().isAdmin()) {
			try {
				if (accHead != null) {
					accHead.setUpdatedBy(userMB.getUser().getId());
					/*System.out.println("THe new account head will have code :"
							+ accHead.getAccCode());*/
					if (accHead.getMinBal() > 0.00) {
						LedgerMcg ledgerMcg = new LedgerMcg();
						// ledgerMcg.setAccCode(accHead.getAccCode());
						ledgerMcg.setDrAmt(accHead.getDrcr().equalsIgnoreCase(
								"dr") ? accHead.getMinBal() : 0.00);
						ledgerMcg.setCrAmt(accHead.getDrcr().equalsIgnoreCase(
								"cr") ? accHead.getMinBal() : 0.00);
						//ledgerMcg.setJvNo(-1);
						ledgerMcg.setRemarks(accHead.getRemarks());
						ledgerMcg.setPostedBy(userMB.getUser());
						ledgerMcg.setIsOpening(1);
						ledgerMcg.setBrId(1);//hard coded

						ledgerMcg.setAccountHead(accHead);
						ledgerMcg.setPostedDate(new Date());
						ledgerMcg.setCreatedDateNp(new JCalendarFunctions().getNepaliDate(ledgerMcg.getPostedDate()));
						ledgerMcg.setFiscalYear(userMB.getUser().getFyModel());
						accHead.setLedger(ledgerMcg);
					}
					if (!accHeadEJB.checkDuplicateName(accHead.getAccName())) {
						displayWarningMessageToUser("Please enter unique Account Head Name.");
						return;
					}

					accHeadEJB.createAccountHead(accHead);
					closeDialog();
					displayInfoMessageToUser("AccHead Created With Sucess");
					loadAllAccHeads();
					resetAccHeadMcg();
				}
			} catch (Exception e) {
				keepDialogOpen();
				displayErrorMessageToUser("Ops, we could not create the AccHead now. Try again later");
			}
		} else {
			displayErrorMessageToUser("Sorry, You are not ADMIN user. CREATE operation is allowed only to ADMIN Users.");
		}
	}

	public void updateAccHead() {
		if (userMB.getUser().isAdmin()) {
			/*int childCount = Integer
					.valueOf(String.valueOf(DirectSqlUtils
							.getSingleValueFromTable("select count(*) from acc_head_mcg where parent='"
									+ accHead.getAccHeadId() + "'")));*/
			/*int childCount = Integer
					.valueOf(String.valueOf(accHeadEJB.getChildrenCount(accHead.getAccHeadId())));
			if (childCount > 0) {
				displayErrorMessageToUser("The account head you want to update contains other child account heads.So, You can not update it now.");
				loadAllAccHeads();
				resetAccHeadMcg();
			} else {*/
				try {
					accHead.setUpdatedBy(userMB.getUser().getId());
				/*	System.out.println("THe new account head will have code :"
							+ accHead.getAccCode());*/
					if (accHead.getMinBal()> 0.00) {
						LedgerMcg ledgerMcg = accHead.getLedger();
						if (ledgerMcg == null) {
							ledgerMcg = new LedgerMcg();
						}
						// ledgerMcg.setAccCode(accHead.getAccCode());
						ledgerMcg.setDrAmt(accHead.getDrcr().equalsIgnoreCase(
								"dr") ? accHead.getMinBal() : 0.00);
						ledgerMcg.setCrAmt(accHead.getDrcr().equalsIgnoreCase(
								"cr") ? accHead.getMinBal() :0.00);
						//ledgerMcg.setJvNo(-1);
						ledgerMcg.setRemarks(accHead.getRemarks());
						ledgerMcg.setPostedBy(userMB.getUser());
						ledgerMcg.setIsOpening(1);
						ledgerMcg.setBrId(1);//hard coded

						ledgerMcg.setAccountHead(accHead);
						ledgerMcg.setPostedDate(ledgerMcg.getPostedDate()==null?new Date():ledgerMcg.getPostedDate());
						ledgerMcg.setCreatedDateNp(new JCalendarFunctions().getNepaliDate(ledgerMcg.getPostedDate()));
						ledgerMcg.setFiscalYear(userMB.getUser().getFyModel());
						ledgerMcg.setUpdatedBy(userMB.getUser());
						accHead.setLedger(ledgerMcg);

					}
					accHeadEJB.updateAccHead(accHead);
					closeDialog();
					displayInfoMessageToUser("AccHead Updated With Sucess");
					loadAllAccHeads();
					resetAccHeadMcg();
				} catch (Exception e) {
					keepDialogOpen();
					displayErrorMessageToUser("Ops, we could not update accHead now. Try again later");
					e.printStackTrace();
				}
	//		}// else ends
		} else {
			displayErrorMessageToUser("Sorry, You are not ADMIN user. UPDATE operation is allowed only to ADMIN Users.");
		}
	}

	public void deleteAccHead() {
		if (userMB.getUser().isAdmin()) {
			try {
				/*int childCount = Integer
						.valueOf(String.valueOf(DirectSqlUtils
								.getSingleValueFromTable("select count(*) from acc_head_mcg where parent='"
										+ accHead.getAccHeadId() + "'")));*/
				int childCount = Integer
						.valueOf(String.valueOf(accHeadEJB.getChildrenCount(accHead.getAccHeadId())));
				if (childCount > 0) {
					displayErrorMessageToUser("The account head you want to delete contains other account heads. You can not delete it now.");
					loadAllAccHeads();
					resetAccHeadMcg();
				} else {
					accHeadEJB.deleteAccHead(accHead);
					closeDialog();
					displayInfoMessageToUser("Acc head Deleted With Sucess");
					loadAllAccHeads();
					resetAccHeadMcg();
				}
			} catch (Exception e) {
				keepDialogOpen();
				displayErrorMessageToUser("Ops, we could not Delete the acc head. Try again later");
				e.printStackTrace();
			}
		} else {
			displayErrorMessageToUser("Sorry, You are not ADMIN user. DELETE operation is allowed only to ADMIN Users.");
		}
	}

	public void setUserMB(UserMB userMB) {
		this.userMB = userMB;
	}

	public List<AccHeadMcg> getGetSelected() {
		if (getSelected == null) {

		}

		return getSelected;
	}

	public void setGetSelected(List<AccHeadMcg> getSelected) {
		this.getSelected = getSelected;
	}

	public JournalVoucherDetailMB getJournalMB() {
		return journalMB;
	}

	public void setJournalMB(JournalVoucherDetailMB journalMB) {
		this.journalMB = journalMB;
	}
	
	public List<AccHeadMcg> getParentNodeOnly() {
		
		
		return null;
	}

	/**
	 * @param accCode
	 *            Setting the accCode and Name to the journalMb AccCode and
	 *            AccName if it has no child
	 * @author Ganesh-Magnus
	 * @return
	 */
	public void getAccAliasOnSelect(String accCode, String accName,
			int accHeadID, int accType) {
		/*int childCount = Integer
				.valueOf(String.valueOf(DirectSqlUtils
						.getSingleValueFromTable("select count(*) from acc_head_mcg where parent='"
								+ accHeadID + "'")));*/
		/*int childCount = Integer
				.valueOf(String.valueOf(accHeadEJB.getChildrenCount(accHeadID)));*/
		/*if (childCount > 0) {
			journalMB.setAccCode("");
			journalMB.setAccHead("");
			journalMB.setDisplyAMount(0.0);
			journalMB.setAccType(0);
		} else {
			journalMB.setAccCode(accCode);
			journalMB.setAccHead(accName);
			journalMB.setAccType(accType);
			journalMB
					.setDisplyAMount(Double.parseDouble(String.valueOf(DirectSqlUtils
							.getSingleValueFromTable("select isnull("
									+ ((accType == 1) || (accType == 2) ? "sum(dr_amt)-sum(cr_amt) "
											: "SUM(cr_amt)-SUM(dr_amt)  ")
									+ " ,0) as currbal From ledger_mcg where acc_code='"+accCode+"'"))));


		}*/
	}

	
}// end of class