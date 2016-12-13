package sasrestro.mb.account;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.commons.lang.StringUtils;

import sasrestro.mb.user.UserMB;
import sasrestro.misc.AbstractMB;
import sasrestro.misc.JCalendarFunctions;
import sasrestro.model.account.AccHeadMcg;
import sasrestro.model.account.AccountReportModel;
import sasrestro.model.account.JournalVoucherDetailModel;
import sasrestro.model.account.LedgerMcg;
import sasrestro.sessionejb.account.AccHeadEJB;
import sasrestro.sessionejb.account.AccountReportEJB;
import sasrestro.sessionejb.account.LedgerEJB;


@ViewScoped
@ManagedBean(name = "accReportMB")
public class AccountReportMB extends AbstractMB implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = UserMB.INJECTION_NAME)
	private UserMB userMB;

	@EJB
	private AccountReportEJB accountReportEJB;

	@EJB
	AccHeadEJB accHeadEJB;

	@EJB
	LedgerEJB ledgerEJB;

	private int reportLevel;
	private Date toDate;
	private Date fromDate;
	private AccHeadMcg rootNode;
	private AccHeadMcg childNode;

	private List<LedgerMcg> ledgerObj;

	private List<LedgerReport> ledgerRepo;

	private LedgerReport ledgerRepoObj; 

	private List<JournalVoucherDetailModel> jvDetList;

	private DateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

	private String jvDt;

	private int jvNo;

	private String jvTpe;

	/**
	 * @return the jvDt
	 */
	public String getJvDt() {
		return jvDt;
	}
	/**
	 * @param jvDt the jvDt to set
	 */
	public void setJvDt(String jvDt) {
		this.jvDt = jvDt;
	}
	/**
	 * @return the jvNo
	 */
	public int getJvNo() {
		return jvNo;
	}
	/**
	 * @param jvNo the jvNo to set
	 */
	public void setJvNo(int jvNo) {
		this.jvNo = jvNo;
	}
	public void setUserMB(UserMB userMB) {
		this.userMB = userMB;
	}
	public List<AccountReportModel> getTrialBalance()
	{
		List<AccountReportModel> tbList=accountReportEJB.getTrialBalanceReportData(reportLevel, sdf.format(getToDate()), sdf.format(getFromDate()) ,userMB.getUser().getFyModel().getFyId());
		if(!tbList.isEmpty())
		{
			double drSum=0.00,crSum=0.00;
			for(AccountReportModel arm:tbList)
			{
				if(reportLevel==1)
				{
					if(!arm.getAccountCode().contains(".") && arm.getDrAmt()>0.00)
						drSum+=arm.getDrAmt();
					else if(!arm.getAccountCode().contains(".") && arm.getCrAmt()>0.00)
						crSum+=arm.getCrAmt();

					if (arm.getAccountCode().contains(".")) {
						//	int countDots = StringUtils.countMatches(arm.getAccountCode(), ".");


						//Inserts WHITE SPACES before the start of the String. No. of WHITE SPACES = No. of dots(.) in the String.

						/*arm.setAccountCode(String.format("%"+(StringUtils.countMatches(arm.getAccountCode(), ".")+arm.getAccountCode().length())+"s", arm.getAccountCode()).replace(' ', '*'));*/
						arm.setAccountCode(String.format("%"+(StringUtils.countMatches(arm.getAccountCode(), ".")+arm.getAccountCode().length())+"s", arm.getAccountCode()));
						//	Strings.padStart("string", 10, ' ');
					}
				}
				else
				{
					if( arm.getDrAmt()>0.00)
						drSum+=arm.getDrAmt();
					else if(arm.getCrAmt()>0.00)
						crSum+=arm.getCrAmt();
				}
			}
			AccountReportModel arm=new AccountReportModel();
			arm.setParticulars("Total");
			arm.setDrAmt(drSum);
			arm.setCrAmt(crSum);
			tbList.add(arm);
		}
		return tbList;
	}
	public List<AccountReportModel> getProfitLoss()
	{
		List<AccountReportModel> incomeList=accountReportEJB.getThisAccTypeReportData(reportLevel, sdf.format(getToDate()),sdf.format(getFromDate()),userMB.getUser().getFyModel().getFyId(),4);
		List<AccountReportModel> expenseList=accountReportEJB.getThisAccTypeReportData(reportLevel, sdf.format(getToDate()),sdf.format(getFromDate()),userMB.getUser().getFyModel().getFyId(),2);
		List<AccountReportModel> plList=new ArrayList<AccountReportModel>();

		double totalIncome=0.00;
		AccountReportModel armLabel=new AccountReportModel();
		armLabel.setParticulars("Incomes");
		plList.add(armLabel);
		for(AccountReportModel arm:incomeList)
		{
			if(reportLevel==1)
			{
				if(!arm.getAccountCode().contains(".") && arm.getDrAmt()>0.00)
				{
					totalIncome+=arm.getDrAmt();
					//re-locate the values for dislay purpose
					arm.setDrAmt(arm.getDrAmt());
				}
				else if(!arm.getAccountCode().contains(".") && arm.getCrAmt()>0.00)
				{
					totalIncome+=arm.getCrAmt();
					//re-locate the values for dislay purpose
					arm.setDrAmt(arm.getCrAmt());
				}
			}
			else
			{

				if(arm.getDrAmt()>0.00)
				{
					totalIncome+=arm.getDrAmt();
					//re-locate the values for dislay purpose
					arm.setDrAmt(arm.getDrAmt());
				}
				else if(arm.getCrAmt()>0.00)
				{
					totalIncome+=arm.getCrAmt();
					//re-locate the values for dislay purpose
					arm.setDrAmt(arm.getCrAmt());
				}
			}
			//re-locate the values for dislay purpose
			arm.setCrAmt(0.00);
			plList.add(arm);
		}
		armLabel=new AccountReportModel();
		armLabel.setParticulars("Total Income");
		armLabel.setCrAmt(totalIncome);

		plList.add(armLabel);


		armLabel=new AccountReportModel();
		armLabel.setParticulars("Expenses");

		plList.add(armLabel);
		double totalExpense=0.00;
		for(AccountReportModel arm:expenseList)
		{
			if(reportLevel==1)
			{
				if(!arm.getAccountCode().contains(".") && arm.getDrAmt()>0.00)
				{
					totalExpense+=arm.getDrAmt();
					//re-locate the values for dislay purpose
					arm.setDrAmt(arm.getDrAmt());
				}
				else if(!arm.getAccountCode().contains(".") && arm.getCrAmt()>0.00)
				{
					totalExpense+=arm.getCrAmt();
					//re-locate the values for dislay purpose
					arm.setDrAmt(arm.getCrAmt());
				}
			}
			else
			{

				if(arm.getDrAmt()>0.00)
				{
					totalExpense+=arm.getDrAmt();
					//re-locate the values for dislay purpose
					arm.setDrAmt(arm.getDrAmt());
				}
				else if(arm.getCrAmt()>0.00)
				{
					totalExpense+=arm.getCrAmt();
					//re-locate the values for dislay purpose
					arm.setDrAmt(arm.getCrAmt());
				}
			}
			//re-locate the values for dislay purpose
			arm.setCrAmt(0.00);
			plList.add(arm);
		}

		double currentProfit=accountReportEJB.findProfitLossAmount(1, sdf.format(getToDate()),sdf.format(getFromDate()),userMB.getUser().getFyModel().getFyId());//totalIncome-totalExpense;
		armLabel=new AccountReportModel();
		armLabel.setCrAmt(Math.abs(currentProfit));
		if(totalIncome-totalExpense>0.00)
		{
			armLabel.setParticulars("Current Profit");
			plList.add(incomeList.size()+expenseList.size()+3,armLabel);//at end of last expense item
		}
		else
		{
			armLabel.setParticulars("Current Loss");
			plList.add(incomeList.size()+1,armLabel);//at the end of income list			
		}

		armLabel=new AccountReportModel();
		armLabel.setParticulars("Total Expenses");

		armLabel.setCrAmt(currentProfit>0.00?(totalExpense+currentProfit):(totalIncome+Math.abs(currentProfit)));

		plList.add(armLabel);
		//return the decorated list
		return plList;
	}

	public List<AccountReportModel> getBalanceSheet()
	{
		List<AccountReportModel> assetList=accountReportEJB.getThisAccTypeReportData(reportLevel, sdf.format(getToDate()),sdf.format(getFromDate()),userMB.getUser().getFyModel().getFyId(),1);
		List<AccountReportModel> liabilityList/*= new ArrayList<AccountReportModel>();
		liabilityList*/ =	accountReportEJB.getThisAccTypeReportData(reportLevel, sdf.format(getToDate()),sdf.format(getFromDate()),userMB.getUser().getFyModel().getFyId(),3);
		List<AccountReportModel> bsList=new ArrayList<AccountReportModel>();
		double totalLiability=0.00;
		AccountReportModel armLabel=new AccountReportModel();
		armLabel.setParticulars("Liabilities");
		bsList.add(armLabel);
		for(AccountReportModel arm:liabilityList)
		{
			if(reportLevel==1)
			{
				if(!arm.getAccountCode().contains(".") && arm.getDrAmt()>0.00)
				{
					totalLiability+=arm.getDrAmt();
					//re-locate the values for dislay purpose
					arm.setDrAmt(arm.getDrAmt());
				}
				else if(!arm.getAccountCode().contains(".") && arm.getCrAmt()>0.00)
				{
					totalLiability+=arm.getCrAmt();
					//re-locate the values for dislay purpose
					arm.setDrAmt(arm.getCrAmt());
				}
			}
			else
			{

				if(arm.getDrAmt()>0.00)
				{
					totalLiability+=arm.getDrAmt();
					//re-locate the values for dislay purpose
					arm.setDrAmt(arm.getDrAmt());
				}
				else if(arm.getCrAmt()>0.00)
				{
					totalLiability+=arm.getCrAmt();
					//re-locate the values for dislay purpose
					arm.setDrAmt(arm.getCrAmt());
				}
			}
			//re-locate the values for dislay purpose
			arm.setCrAmt(0.00);
			bsList.add(arm);
		}
		/*armLabel=new AccountReportModel();
		armLabel.setParticulars("Total Liability");
		armLabel.setCrAmt(totalLiability);

		bsList.add(armLabel);
		 */
		armLabel=new AccountReportModel();
		armLabel.setParticulars("Assets");

		bsList.add(armLabel);
		double totalAssets=0.00;
		for(AccountReportModel arm:assetList)
		{
			if(reportLevel==1)
			{
				if(!arm.getAccountCode().contains(".") && arm.getDrAmt()>0.00)
				{
					totalAssets+=arm.getDrAmt();
					//re-locate the values for dislay purpose
					arm.setDrAmt(arm.getDrAmt());
				}
				else if(!arm.getAccountCode().contains(".") && arm.getCrAmt()>0.00)
				{
					totalAssets+=arm.getCrAmt();
					//re-locate the values for dislay purpose
					arm.setDrAmt(arm.getCrAmt());
				}
			}
			else
			{

				if(arm.getDrAmt()>0.00)
				{
					totalAssets+=arm.getDrAmt();
					//re-locate the values for dislay purpose
					arm.setDrAmt(arm.getDrAmt());
				}
				else if(arm.getCrAmt()>0.00)
				{
					totalAssets+=arm.getCrAmt();
					//re-locate the values for dislay purpose
					arm.setDrAmt(arm.getCrAmt());
				}
			}
			//re-locate the values for dislay purpose
			arm.setCrAmt(0.00);
			bsList.add(arm);
		}
		double currentProfit=accountReportEJB.findProfitLossAmount(1, sdf.format(getToDate()),sdf.format(getFromDate()),userMB.getUser().getFyModel().getFyId());//totalIncome-totalExpense;
		armLabel=new AccountReportModel();
		armLabel.setCrAmt(Math.abs(currentProfit));
		if(currentProfit>0.00)
		{
			armLabel.setParticulars("Current Profit");
			bsList.add(liabilityList.size()+1,armLabel);//at the end of income list		
		}
		else
		{
			armLabel.setParticulars("Current Loss");
			bsList.add(liabilityList.size()+assetList.size()+2,armLabel);//at end of last expense item
		}

		armLabel=new AccountReportModel();
		armLabel.setParticulars(currentProfit>0.00?"Total Liabilities":"Total Assets");
		armLabel.setCrAmt(currentProfit<0.00?(totalAssets+Math.abs(currentProfit)):(totalLiability+currentProfit));
		if(currentProfit>0.00)
		{
			bsList.add(liabilityList.size()+2,armLabel);
			armLabel=new AccountReportModel();
			armLabel.setParticulars("Total Assets");
			armLabel.setCrAmt(totalAssets);
			bsList.add(armLabel);
		}
		else
		{
			bsList.add(armLabel);
			armLabel=new AccountReportModel();
			armLabel.setParticulars("Total Liabilities");
			armLabel.setCrAmt(totalLiability);
			bsList.add(armLabel);
		}
		//return the decorated list
		return bsList;
	}

	public void viewTrialbalance()
	{
		getTrialBalance();
	}
	public void viewBalanceSheet()
	{
		getBalanceSheet();
	}
	public void viewProfitLoss()
	{
		getProfitLoss();
	}
	/**
	 * @return the reportLevel
	 */
	public int getReportLevel() {
		return reportLevel;
	}
	/**
	 * @param reportLevel the reportLevel to set
	 */
	public void setReportLevel(int reportLevel) {
		this.reportLevel = reportLevel;
	}
	/**
	 * @return the toDate
	 */
	public Date getToDate() {
		if(toDate==null)
		{
			setToDate(new Date());
		}
		return toDate;
	}
	/**
	 * @param toDate the toDate to set
	 */
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getNepaliToDate()
	{
		return new JCalendarFunctions().getNepaliDate(toDate);
	}
	/**
	 * @return the fromDate
	 */
	public Date getFromDate() {
		if (fromDate==null) {
			setFromDate(userMB.getUser().getFyModel().getStartDate());
		}
		return fromDate;
	}
	/**
	 * @param fromDate the fromDate to set
	 */
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * @return the rootNodes for listing
	 */
	public List<AccHeadMcg> getRootNodes() {

		return accHeadEJB.listRootNodes();
	}

	public List<AccHeadMcg> getChildNodes(){
		if (getRootNode().getAccHeadId()>0) {
			rootNode = accHeadEJB.find(rootNode.getAccHeadId());
			return accHeadEJB.findAllNonRoot(rootNode.getAccCode());
		}
		else {
			return null;
		}
	}

	/**
	 * @return the rootNode
	 */
	public AccHeadMcg getRootNode() {
		if (rootNode==null) {
			rootNode = new AccHeadMcg();
		}
		return rootNode;
	}

	/**
	 * @param rootNode the rootNode to set
	 */
	public void setRootNode(AccHeadMcg rootNode) {
		this.rootNode = rootNode;
	}
	/**
	 * @return the childNode
	 */
	public AccHeadMcg getChildNode() {
		if (childNode==null) {
			childNode = new AccHeadMcg();
		}
		return childNode;
	}
	/**
	 * @param childNode the childNode to set
	 */
	public void setChildNode(AccHeadMcg childNode) {
		this.childNode = childNode;
	}

	/*public List<LedgerMcg> getLedger(){
		if (childNode.getAccHeadId()>0) {
			setChildNode(accHeadEJB.find(childNode.getAccHeadId()));
			return accountReportEJB.getLedger(childNode.getAccHeadId(), fromDate, toDate);
		}
		return null;
	}*/
	/**
	 * @return the ledgerObj
	 */
	public List<LedgerMcg> getLedgerObj() {
		if (ledgerObj==null) {
			ledgerObj = new ArrayList<LedgerMcg>();
		}
		/*if (childNode.getAccHeadId()>0) {
			childNode = accHeadEJB.find(childNode.getAccHeadId());
			ledgerObj =  accountReportEJB.getLedger(childNode.getAccHeadId(), fromDate, toDate);
		}*/
		return ledgerObj;
	}
	/**
	 * @param ledgerObj the ledgerObj to set
	 */
	public void setLedgerObj(List<LedgerMcg> ledgerObj) {
		this.ledgerObj = ledgerObj;
	}

	public void loadLedger(){
		if (childNode.getAccHeadId()>0) {
			//	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			childNode = accHeadEJB.find(childNode.getAccHeadId());
			//		ledgerObj = null;
			ledgerObj =  ledgerEJB.getLedger(childNode.getAccHeadId(), fromDate, toDate);

			/*ledgerObj =	DirectSqlUtils.getListOfResultSets("SELECT l.* FROM ledger_mcg l \n" +
					"	INNER JOIN acc_head_mcg a ON l.acc_code_id=a.acc_head_id \n" +
					"		WHERE a.acc_head_id='"+childNode.getAccHeadId()+"'\n" +
					"				AND (l.posted_date BETWEEN '"+sdf.format(fromDate)+"' AND '"+sdf.format(toDate)+"')", LedgerMcg.class);*/
			ledgerRepo = null;
			double sum = 0.00;
			//	double bdTomorrow = 0.00;
			double totalDr = 0.00;
			double totalCr = 0.00;

			//	ledgerEJB.findBroughtDownAmount(sdf.format(toDate), childNode.getAccHeadId());

			if (childNode.getDrcr().equalsIgnoreCase("dr")) {

				LedgerReport bdAmtObj = new LedgerReport();

				bdAmtObj.getLedgerObj().getToAccountHead().setAccName("Balance C/F");
				bdAmtObj.getLedgerObj().setDrAmt(ledgerEJB.findBroughtDownAmount(sdf.format(fromDate), childNode.getAccHeadId()));

				sum+=bdAmtObj.getLedgerObj().getDrAmt();

				bdAmtObj.setSum(sum);

				getLedgerRepo().add(bdAmtObj);

				for (LedgerMcg ledgerMcg : ledgerObj) {
					LedgerReport obj = new LedgerReport();
					obj.setLedgerObj(ledgerMcg);
					sum=Math.abs(sum+ledgerMcg.getDrAmt()-ledgerMcg.getCrAmt());
					obj.setSum(sum);

					//			bdTomorrow+=ledgerMcg.getDrAmt();

					getLedgerRepo().add(obj);
				}


				//			int i =0;
				for (LedgerReport ledgerRepo : getLedgerRepo()) {

					//	if (i>0) {
					totalDr+=ledgerRepo.getLedgerObj().getDrAmt();
					totalCr+=ledgerRepo.getLedgerObj().getCrAmt();
					/*		}

					i++;*/
				}

				LedgerReport totObj = new LedgerReport();

				totObj.getLedgerObj().getToAccountHead().setAccName("Total: ");
				totObj.getLedgerObj().setDrAmt(totalDr);
				totObj.getLedgerObj().setCrAmt(totalCr);

				totObj.setSum(0.00);

				getLedgerRepo().add(totObj);



				LedgerReport bdAmtTomObj = new LedgerReport();

				bdAmtTomObj.getLedgerObj().getToAccountHead().setAccName("Balance B/F");
				bdAmtTomObj.getLedgerObj().setCrAmt(totalDr-totalCr);

				bdAmtTomObj.setSum(0.00);

				getLedgerRepo().add(bdAmtTomObj);

				/*int i =0;
				for (LedgerReport ledgerRepo : getLedgerRepo()) {

					if (i>0) {
						totalDr+=ledgerRepo.getLedgerObj().getDrAmt();
						totalCr+=ledgerRepo.getLedgerObj().getCrAmt();
					}

					i++;
				}

				LedgerReport totObj = new LedgerReport();

				totObj.getLedgerObj().getToAccountHead().setAccName("Total: ");
				totObj.getLedgerObj().setDrAmt(totalDr);
				totObj.getLedgerObj().setCrAmt(totalCr);

				totObj.setSum(0.00);

				getLedgerRepo().add(totObj);*/

			}
			if (childNode.getDrcr().equalsIgnoreCase("cr")) {

				LedgerReport bdAmtObj = new LedgerReport();

				bdAmtObj.getLedgerObj().getToAccountHead().setAccName("Balance C/F");
				bdAmtObj.getLedgerObj().setCrAmt(ledgerEJB.findBroughtDownAmount(sdf.format(fromDate), childNode.getAccHeadId()));

				sum+=bdAmtObj.getLedgerObj().getCrAmt();

				bdAmtObj.setSum(sum);

				getLedgerRepo().add(bdAmtObj);

				for (LedgerMcg ledgerMcg : ledgerObj) {
					LedgerReport obj = new LedgerReport();
					obj.setLedgerObj(ledgerMcg);
					sum=Math.abs(sum-ledgerMcg.getDrAmt()+ledgerMcg.getCrAmt());
					obj.setSum(sum);

					//	bdTomorrow+=ledgerMcg.getCrAmt();

					getLedgerRepo().add(obj);
				}


				//			int i =0;
				for (LedgerReport ledgerRepo : getLedgerRepo()) {

					//		if (i>0) {
					totalDr+=ledgerRepo.getLedgerObj().getDrAmt();
					totalCr+=ledgerRepo.getLedgerObj().getCrAmt();
					//		}

					//	i++;
				}

				LedgerReport totObj = new LedgerReport();

				totObj.getLedgerObj().getToAccountHead().setAccName("Total: ");
				totObj.getLedgerObj().setCrAmt(totalCr);
				totObj.getLedgerObj().setDrAmt(totalDr);

				totObj.setSum(0.00);

				getLedgerRepo().add(totObj);


				LedgerReport bdAmtTomObj = new LedgerReport();

				bdAmtTomObj.getLedgerObj().getToAccountHead().setAccName("Balance B/F");
				bdAmtTomObj.getLedgerObj().setDrAmt(totalCr-totalDr);

				bdAmtTomObj.setSum(0.00);

				getLedgerRepo().add(bdAmtTomObj);

				/*int i =0;
				for (LedgerReport ledgerRepo : getLedgerRepo()) {

					if (i>0) {
						totalDr+=ledgerRepo.getLedgerObj().getDrAmt();
						totalCr+=ledgerRepo.getLedgerObj().getCrAmt();
					}

					i++;
				}

				LedgerReport totObj = new LedgerReport();

				totObj.getLedgerObj().getToAccountHead().setAccName("Total: ");
				totObj.getLedgerObj().setCrAmt(totalCr);
				totObj.getLedgerObj().setDrAmt(totalDr);

				totObj.setSum(0.00);

				getLedgerRepo().add(totObj);*/

			}

		}
	}

	/**
	 * @return the ledgerRepo
	 */
	public List<LedgerReport> getLedgerRepo() {
		if (ledgerRepo==null) {
			ledgerRepo = new ArrayList<LedgerReport>();
		}
		return ledgerRepo;
	}
	/**
	 * @param ledgerRepo the ledgerRepo to set
	 */
	public void setLedgerRepo(List<LedgerReport> ledgerRepo) {
		this.ledgerRepo = ledgerRepo;
	}



	public class LedgerReport{
		LedgerMcg ledgerObj;
		double sum;

		/**
		 * @return the ledgerObj
		 */
		public LedgerMcg getLedgerObj() {
			if (ledgerObj==null) {
				ledgerObj = new LedgerMcg();
				if (ledgerObj.getToAccountHead()==null) {
					ledgerObj.setToAccountHead(new AccHeadMcg());
				}

			}
			return ledgerObj;
		}
		/**
		 * @param ledgerObj the ledgerObj to set
		 */
		public void setLedgerObj(LedgerMcg ledgerObj) {
			this.ledgerObj = ledgerObj;
		}
		/**
		 * @return the sum
		 */
		public double getSum() {
			return sum;
		}
		/**
		 * @param sum the sum to set
		 */
		public void setSum(double sum) {
			this.sum = sum;
		}

	}

	public void forLedgerDetail(){
		jvDetList = new ArrayList<JournalVoucherDetailModel>();

		setJvDetList(getLedgerRepoObj().getLedgerObj().getJournalVourcher().getJvdmList());


		JournalVoucherDetailModel journalVoucherDetailModel = new JournalVoucherDetailModel();

		journalVoucherDetailModel.setAccountHead(new AccHeadMcg());
		journalVoucherDetailModel.getAccountHead().setAccName("Total :");

		journalVoucherDetailModel.setCrAmt(getLedgerRepoObj().getLedgerObj().getJournalVourcher().getReceiptAmt());
		journalVoucherDetailModel.setDrAmt(getLedgerRepoObj().getLedgerObj().getJournalVourcher().getReceiptAmt());

		getJvDetList().add(journalVoucherDetailModel);


		setJvDt(getLedgerRepoObj().getLedgerObj().getJournalVourcher().getJvDateBs());
		setJvNo(getLedgerRepoObj().getLedgerObj().getJournalVourcher().getJvNo());
		setJvTpe(getLedgerRepoObj().getLedgerObj().getJvType().getCvLbl());
	}
	/**
	 * @return the jvDetList
	 */
	public List<JournalVoucherDetailModel> getJvDetList() {
		return jvDetList;
	}
	/**
	 * @param jvDetList the jvDetList to set
	 */
	public void setJvDetList(List<JournalVoucherDetailModel> jvDetList) {
		this.jvDetList = jvDetList;
	}
	/**
	 * @return the ledgerRepoObj
	 */
	public LedgerReport getLedgerRepoObj() {
		return ledgerRepoObj;
	}
	/**
	 * @param ledgerRepoObj the ledgerRepoObj to set
	 */
	public void setLedgerRepoObj(LedgerReport ledgerRepoObj) {
		this.ledgerRepoObj = ledgerRepoObj;
	}
	/**
	 * @return the jvTpe
	 */
	public String getJvTpe() {
		return jvTpe;
	}
	/**
	 * @param jvTpe the jvTpe to set
	 */
	public void setJvTpe(String jvTpe) {
		this.jvTpe = jvTpe;
	}
}
