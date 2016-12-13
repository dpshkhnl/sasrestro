package sasrestro.mb.account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import sasrestro.mb.user.UserMB;
import sasrestro.misc.AbstractMB;
import sasrestro.misc.JCalendarFunctions;
import sasrestro.model.account.AccHeadMcg;
import sasrestro.model.account.LedgerMcg;
import sasrestro.sessionejb.account.AccHeadEJB;
import sasrestro.sessionejb.account.LedgerEJB;

@ManagedBean(name="openingBalMB")
@ViewScoped
public class OpeningBalanceMB extends AbstractMB implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = UserMB.INJECTION_NAME)
	private UserMB userMB;

	@EJB
	AccHeadEJB accHeadEJB;

	@EJB
	LedgerEJB ledgerEJB;

	private AccHeadMcg head;

	private List<OpenBalDummy> openBalDummyList;
	

	public List<AccHeadMcg> getRootHeads(){

		List<AccHeadMcg> objList = new ArrayList<AccHeadMcg>();
		for (Iterator<AccHeadMcg> iterator = accHeadEJB.listRootNodes().iterator(); iterator
				.hasNext();) {
			AccHeadMcg openBalDummy = (AccHeadMcg) iterator.next();
			
			if (openBalDummy.getAccName().equalsIgnoreCase("Liabilities") || openBalDummy.getAccName().equalsIgnoreCase("Assets")) {
				objList.add(openBalDummy);
			}
			
		}
		return objList;
	}


	/**
	 * @return the head
	 */
	public AccHeadMcg getHead() {
		if (head==null) {
			head = new AccHeadMcg();
		}
		return head;
	}


	/**
	 * @param head the head to set
	 */
	public void setHead(AccHeadMcg head) {
		this.head = head;
	}

	public void getChildHeads(){
		if (head!=null && head.getAccHeadId()>0) {

			openBalDummyList=null;

			for (AccHeadMcg accHeadObj : accHeadEJB.findAllNonRoot(head.getAccCode())) {

				OpenBalDummy dummy = new OpenBalDummy();

				dummy.setAccheadObj(accHeadObj);
				dummy.setOpeningBal(0.00);

				getOpenBalDummyList().add(dummy);
			}
		}

	}

	public void save(){

		if (head!=null && head.getAccHeadId()>0) {
			try {
				List<LedgerMcg> ledgerList = new ArrayList<LedgerMcg>();

				if (head.getDrcr().equalsIgnoreCase("dr")) {
					for (OpenBalDummy openBalDummy : openBalDummyList) {
						
						LedgerMcg ledgerObj = new LedgerMcg();

						ledgerObj.setAccountHead(openBalDummy.getAccheadObj());
						ledgerObj.setDrAmt(openBalDummy.getOpeningBal());
						ledgerObj.setFiscalYear(userMB.getUser().getFyModel());
						ledgerObj.setIsOpening(1);
						ledgerObj.setJournalVourcher(null);
						ledgerObj.setJvType(null);
						ledgerObj.setPostedBy(userMB.getUser());
						ledgerObj.setPostedDate(userMB.getUser().getDayInStatus().getTransDateEn());
						ledgerObj.setRemarks("Opening Balance");
						ledgerObj.setCreatedDateNp(new JCalendarFunctions().getNepaliDate(new Date()));

						ledgerList.add(ledgerObj);
					}
				}
				if (head.getDrcr().equalsIgnoreCase("cr")) {
					for (OpenBalDummy openBalDummy : openBalDummyList) {
						
						LedgerMcg ledgerObj = new LedgerMcg();

						ledgerObj.setAccountHead(openBalDummy.getAccheadObj());
						ledgerObj.setCrAmt(openBalDummy.getOpeningBal());
						ledgerObj.setFiscalYear(userMB.getUser().getFyModel());
						ledgerObj.setIsOpening(1);
						ledgerObj.setJournalVourcher(null);
						ledgerObj.setJvType(null);
						ledgerObj.setPostedBy(userMB.getUser());
						ledgerObj.setPostedDate(userMB.getUser().getDayInStatus().getTransDateEn());
						ledgerObj.setRemarks("Opening Balance");
						ledgerObj.setCreatedDateNp(new JCalendarFunctions().getNepaliDate(new Date()));

						ledgerList.add(ledgerObj);

					}
				}

				ledgerEJB.saveList(ledgerList);
				
				displayInfoMessageToUser("Successfully Saved.");
				
				openBalDummyList=null;
				head=null;
				
			} catch (Exception e) {
				displayErrorMessageToUser("Sorry.\n Error Occurred");// TODO: handle exception
			}

			
		}

	}

	/**
	 * @return the openBalDummyList
	 */
	public List<OpenBalDummy> getOpenBalDummyList() {
		if (openBalDummyList==null) {
			openBalDummyList = new ArrayList<OpeningBalanceMB.OpenBalDummy>();
		}
		return openBalDummyList;
	}


	/**
	 * @param openBalDummyList the openBalDummyList to set
	 */
	public void setOpenBalDummyList(List<OpenBalDummy> openBalDummyList) {
		this.openBalDummyList = openBalDummyList;
	}

	/**
	 * @return the userMB
	 */
	public UserMB getUserMB() {
		return userMB;
	}


	/**
	 * @param userMB the userMB to set
	 */
	public void setUserMB(UserMB userMB) {
		this.userMB = userMB;
	}

	public class OpenBalDummy{
		AccHeadMcg accheadObj;
		double openingBal;
		/**
		 * @return the accheadObj
		 */
		public AccHeadMcg getAccheadObj() {
			if (accheadObj==null) {
				accheadObj=new AccHeadMcg();
			}
			return accheadObj;
		}
		/**
		 * @param accheadObj the accheadObj to set
		 */
		public void setAccheadObj(AccHeadMcg accheadObj) {
			this.accheadObj = accheadObj;
		}
		/**
		 * @return the openingBal
		 */
		public double getOpeningBal() {
			return openingBal;
		}
		/**
		 * @param openingBal the openingBal to set
		 */
		public void setOpeningBal(double openingBal) {
			this.openingBal = openingBal;
		}
	}

}
