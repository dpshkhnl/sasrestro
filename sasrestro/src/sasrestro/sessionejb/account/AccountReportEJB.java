package sasrestro.sessionejb.account;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.Query;

import sasrestro.model.account.AccountReportModel;
import sasrestro.model.account.LedgerMcg;
import sasrestro.sessionejb.core.GenericDAO;

/**
 * @author Ganesh-Magnus
 * 
 */
@Stateless
@Local
public class AccountReportEJB extends GenericDAO<AccountReportModel> {

	public AccountReportEJB() {
		super(AccountReportModel.class);
		//	System.out.println("Hello i am ----Account Report EJB------------");
		// TODO Auto-generated constructor stub
	}

	/*@PostConstruct
	public void hello() {
		System.out
		.println("*Hello I am Account Report EJB called");
	}*/
	@SuppressWarnings("unchecked")
	public List<AccountReportModel> getTrialBalanceReportData(int level, String toDate, String fromDate, int fyId )
	{
		String levelCondtion=level==1?" (aa.acc_code like a.acc_code+'.%' and (a.PARENT=0 OR a.PARENT>0)) OR ":" ";
		/*String qry="with tbResult as"
				+ "( select a.acc_code,a.acc_name,a.acc_type,a.parent, "
				+ "	CASE WHEN ((a.acc_type='1' OR a.acc_type='2' OR (a.acc_type='5' AND a.drcr='Dr'))) then  "
				+ " isnull((select (SUM(dr_amt)-SUM(cr_amt)) 	FROM  ledger_mcg l inner join acc_head_mcg aa on l.acc_code_id=aa.acc_head_id  "
				+ " where ("+levelCondtion+" l.acc_code_id=a.acc_head_id) and l.posted_date<='"+toDate+"' and l.fy_id="+fyId+" ),0.00) "
				+ " else 0.00 end as drAmt, "
				+ " CASE WHEN ((a.acc_type='3' OR a.acc_type='4' OR (a.acc_type='5' AND a.drcr='Cr'))) then  "
				+ " isnull((select (SUM(cr_amt)-SUM(dr_amt)) 	FROM  ledger_mcg l inner join acc_head_mcg aa on l.acc_code_id=aa.acc_head_id "
				+ " where ("+levelCondtion+" l.acc_code_id=a.acc_head_id) and l.posted_date<='"+toDate+"' and l.fy_id="+fyId+" ),0.00) "
				+ " else 0.00 end as crAmt From acc_head_mcg a "
				+ " ) "
				+ " select acc_code accountCode,acc_name particulars,drAmt,crAmt from tbResult"
				+ " where (drAmt>0.00 or crAmt>0.00) "
				//+ " UNION ALL"
				//+ " select  'Total' as accountCode,'Total' as particulars,sum(drAmt) as drAmt,sum(crAmt) as drAmt from tbResult where PARENT=0 "
				+ " order by 1 ";*/
		String qry1 = /*"with tbResult as"
				+ "( select a.acc_code,a.acc_name,a.acc_type,a.parent, "
				+ "	CASE WHEN ((a.acc_type='1' OR a.acc_type='2' OR (a.acc_type='5' AND a.drcr='Dr'))) then  "
				+ " isnull((select (SUM(dr_amt)-SUM(cr_amt)) 	FROM  ledger_mcg l inner join acc_head_mcg aa on l.acc_code_id=aa.acc_head_id  "
				+ " where ("+levelCondtion+" l.acc_code_id=a.acc_head_id) and  l.posted_date between '"+fromDate+"' AND '"+toDate+"' and l.fy_id="+fyId+" ),0.00) "
				+ " else 0.00 end as drAmt, "
				+ " CASE WHEN ((a.acc_type='3' OR a.acc_type='4' OR (a.acc_type='5' AND a.drcr='Cr'))) then  "
				+ " isnull((select (SUM(cr_amt)-SUM(dr_amt)) 	FROM  ledger_mcg l inner join acc_head_mcg aa on l.acc_code_id=aa.acc_head_id "
				+ " where ("+levelCondtion+" l.acc_code_id=a.acc_head_id) and  l.posted_date between '"+fromDate+"' AND '"+toDate+"' and l.fy_id="+fyId+" ),0.00) "
				+ " else 0.00 end as crAmt From acc_head_mcg a "
				+ " ) "
				+ " select acc_code accountCode,acc_name particulars,drAmt,crAmt from tbResult"
				+ " where (drAmt>0.00 or crAmt>0.00) "
				//+ " UNION ALL"
				//+ " select  'Total' as accountCode,'Total' as particulars,sum(drAmt) as drAmt,sum(crAmt) as drAmt from tbResult where PARENT=0 "
				+ " order by 1 ";*/
				"with tbResult as"
				+ "( select a.acc_code,a.acc_name,a.acc_type,a.parent, "
				+ "	CASE WHEN ((a.acc_type='1' OR a.acc_type='2' OR (a.acc_type='5' AND a.drcr='Dr'))) then  "
				+ " isnull((select (SUM(dr_amt)-SUM(cr_amt)) 	FROM  ledger_mcg l inner join acc_head_mcg aa on l.acc_code_id=aa.acc_head_id  "
				+ " where ("+levelCondtion+" l.acc_code_id=a.acc_head_id) and  l.posted_date between '"+fromDate+"' AND '"+toDate+"' and l.fy_id="+fyId+" ),0.00) "
				+ " else 0.00 end as drAmt, "
				+ " CASE WHEN ((a.acc_type='3' OR a.acc_type='4' OR (a.acc_type='5' AND a.drcr='Cr'))) then  "
				+ " isnull((select (SUM(cr_amt)-SUM(dr_amt)) 	FROM  ledger_mcg l inner join acc_head_mcg aa on l.acc_code_id=aa.acc_head_id "
				+ " where ("+levelCondtion+" l.acc_code_id=a.acc_head_id) and  l.posted_date between '"+fromDate+"' AND '"+toDate+"' and l.fy_id="+fyId+" ),0.00) "
				+ " else 0.00 end as crAmt From acc_head_mcg a "
				+ " ) "
				+ " select acc_code accountCode,acc_name particulars,drAmt,crAmt from tbResult"
				+ " where (drAmt>0.00 or crAmt>0.00) "
				//+ " UNION ALL"
				//+ " select  'Total' as accountCode,'Total' as particulars,sum(drAmt) as drAmt,sum(crAmt) as drAmt from tbResult where PARENT=0 "
				+ " order by 1 ";
		/*String levelCondtion=level==1?" (aa.accCode like a.accCode+'.%' and a.PARENT=0) OR ":" ";
		String qry=
				" select a.accCode,a.accName,a.accType,a.parent, "
				+ "	CASE WHEN ((a.accType='1' OR a.accType='2' OR (a.accType='5' AND a.drcr='Dr'))) then  "
				+ " (select (SUM(drAmt)-SUM(crAmt)) 	FROM  LedgerMcg l inner join l.accountHead aa "
				+ " where ( l.accountHead=a.accHeadId) and l.postedDate<='"+toDate+"' ) "
				+ " else 0.00 end as drAmt, "
				+ " CASE WHEN ((a.accType='3' OR a.accType='4' OR (a.accType='5' AND a.drcr='Cr'))) then  "
				+ " (select (SUM(crAmt)-SUM(drAmt)) 	FROM  LedgerMcg l inner join l.accountHead aa "
				+ " where ( l.accountHead=a.accHeadId) and l.postedDate<='"+toDate+"' ) "
				+ " else 0.00 end as crAmt From AccHeadMcg a ";*/
		//	Query query = getEntityManager().createNativeQuery(qry,AccountReportModel.class);
		Query query = getEntityManager().createNativeQuery(qry1,AccountReportModel.class);

		List<AccountReportModel> result=query.getResultList();
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<AccountReportModel> getThisAccTypeReportData(int level, String toDate, String fromDate, int fyId,int accType)
	{
		String levelCondtion=level==1?" (aa.acc_code like a.acc_code+'.%' and a.PARENT=0) OR  ":" ";
		String calcStr=(accType==2 || accType==1)?" (SUM(dr_amt)-SUM(cr_amt)) ":" (SUM(cr_amt)-SUM(dr_amt)) ";
		String qry=/*" SELECT acc_code as accountCode,acc_name as particulars,drAmt,crAmt FROM (SELECT a.acc_code, a.acc_name,a.parent, "
				+ "  CASE when ("+accType+" in(1,2)) then (SELECT  "+calcStr+" FROM ledger_mcg l inner join acc_head_mcg aa on l.acc_code_id=aa.acc_head_id "
				+ "  WHERE ( "+levelCondtion+" l.acc_code_id=a.acc_head_id) "
				+ "  AND l.fy_id="+fyId+"   AND l.posted_date between '2014-01-01' AND '"+toDate+"'  ) else 0.00 end AS drAmt,"
				+ "  CASE when ("+accType+" in(3,4)) then (SELECT  "+calcStr+" FROM ledger_mcg l inner join acc_head_mcg aa on l.acc_code_id=aa.acc_head_id "
				+ "  WHERE ( "+levelCondtion+" l.acc_code_id=a.acc_head_id) "
				+ "  AND l.fy_id="+fyId+"   AND l.posted_date between '2014-01-01' AND '"+toDate+"'  ) else 0.00 end AS crAmt"
				+ "  FROM acc_head_mcg AS a WHERE (acc_type='"+accType+"'))  AS T1  where drAmt<>0 or crAmt<>0  ORDER BY acc_code ";*/
				" SELECT acc_code as accountCode,acc_name as particulars,drAmt,crAmt FROM (SELECT a.acc_code, a.acc_name,a.parent, "
				+ "  CASE when ("+accType+" in(1,2)) then (SELECT  "+calcStr+" FROM ledger_mcg l inner join acc_head_mcg aa on l.acc_code_id=aa.acc_head_id "
				+ "  WHERE ( "+levelCondtion+" l.acc_code_id=a.acc_head_id) "
				+ "  AND l.fy_id="+fyId+"   AND l.posted_date between '"+fromDate+"' AND '"+toDate+"'  ) else 0.00 end AS drAmt,"
				+ "  CASE when ("+accType+" in(3,4)) then (SELECT  "+calcStr+" FROM ledger_mcg l inner join acc_head_mcg aa on l.acc_code_id=aa.acc_head_id "
				+ "  WHERE ( "+levelCondtion+" l.acc_code_id=a.acc_head_id) "
				+ "  AND l.fy_id="+fyId+"   AND l.posted_date between '"+fromDate+"' AND '"+toDate+"'  ) else 0.00 end AS crAmt"
				+ "  FROM acc_head_mcg AS a WHERE (acc_type='"+accType+"'))  AS T1  where drAmt<>0 or crAmt<>0  ORDER BY acc_code ";
		Query query = getEntityManager().createNativeQuery(qry,AccountReportModel.class);

		List<AccountReportModel> result=query.getResultList();
		return result;
	}
	public double findProfitLossAmount(int level, String toDate,String fromDate, int fyId)
	{
		String levelCondtion=level==1?" (aa.acc_code like a.acc_code+'.%' and a.PARENT=0) OR  ":" ";
		String qry=/*" SELECT acc_code as accountCode,acc_name as particulars,drAmt,crAmt FROM (SELECT a.acc_code, a.acc_name,a.parent, "
				+ "  CASE when (a.acc_type=2) then (SELECT  (SUM(dr_amt)-SUM(cr_amt)) FROM ledger_mcg l inner join acc_head_mcg aa on l.acc_code_id=aa.acc_head_id "
				+ "  WHERE ( "+levelCondtion+" l.acc_code_id=a.acc_head_id) "
				+ "  AND l.fy_id="+fyId+"   AND l.posted_date between '2014-01-01' AND '"+toDate+"'  ) else 0.00 end AS drAmt,"
				+ "  CASE when (a.acc_type=4) then (SELECT  (SUM(cr_amt)-SUM(dr_amt)) FROM ledger_mcg l inner join acc_head_mcg aa on l.acc_code_id=aa.acc_head_id "
				+ "  WHERE ( "+levelCondtion+" l.acc_code_id=a.acc_head_id) "
				+ "  AND l.fy_id="+fyId+"   AND l.posted_date between '2014-01-01' AND '"+toDate+"'  ) else 0.00 end AS crAmt"
				+ "  FROM acc_head_mcg AS a  )  AS T1  where parent=0 and (drAmt<>0 or crAmt<>0)  ORDER BY acc_code ";*/
				" SELECT acc_code as accountCode,acc_name as particulars,drAmt,crAmt FROM (SELECT a.acc_code, a.acc_name,a.parent, "
				+ "  CASE when (a.acc_type=2) then (SELECT  (SUM(dr_amt)-SUM(cr_amt)) FROM ledger_mcg l inner join acc_head_mcg aa on l.acc_code_id=aa.acc_head_id "
				+ "  WHERE ( "+levelCondtion+" l.acc_code_id=a.acc_head_id) "
				+ "  AND l.fy_id="+fyId+"   AND l.posted_date between '"+fromDate+"' AND '"+toDate+"'  ) else 0.00 end AS drAmt,"
				+ "  CASE when (a.acc_type=4) then (SELECT  (SUM(cr_amt)-SUM(dr_amt)) FROM ledger_mcg l inner join acc_head_mcg aa on l.acc_code_id=aa.acc_head_id "
				+ "  WHERE ( "+levelCondtion+" l.acc_code_id=a.acc_head_id) "
				+ "  AND l.fy_id="+fyId+"   AND l.posted_date between '"+fromDate+"' AND '"+toDate+"'  ) else 0.00 end AS crAmt"
				+ "  FROM acc_head_mcg AS a  )  AS T1  where parent=0 and (drAmt<>0 or crAmt<>0)  ORDER BY acc_code ";
		Query query = getEntityManager().createNativeQuery(qry,AccountReportModel.class);

		@SuppressWarnings("unchecked")
		List<AccountReportModel> result=query.getResultList();
		double incomeSum=0.00,expenseSum=0.00;
		for(AccountReportModel arm:result)
		{
			if(arm.getCrAmt()>0.00)
				incomeSum+=arm.getCrAmt();
			else
				expenseSum+=arm.getDrAmt();
		}
		return incomeSum-expenseSum;

	}

	@SuppressWarnings("unchecked")
	public List<LedgerMcg> getLedger(int accHeadId,Date fromDate,Date toDate){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
		String qry = "SELECT l FROM LedgerMcg l "
				+ "JOIN l.accountHead a "
				+ "WHERE a.accHeadId='"+accHeadId+"' "
				+ "AND (l.postedDate BETWEEN '"+sdf.format(fromDate)+"' AND '"+sdf.format(toDate)+"') ";
		Query query = getEntityManager().createNativeQuery(qry,LedgerMcg.class);

		return (List<LedgerMcg>)query.getResultList();
	}

}
