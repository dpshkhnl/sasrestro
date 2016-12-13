package sasrestro.sessionejb.account;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import sasrestro.misc.DirectSqlUtils;
import sasrestro.model.account.LedgerMcg;
import sasrestro.sessionejb.core.GenericDAO;

@Stateless
@LocalBean
public class LedgerEJB extends GenericDAO<LedgerMcg>{

	public LedgerEJB() {
		super(LedgerMcg.class);
		// TODO Auto-generated constructor stub
	}


	public List<LedgerMcg> getLedger(int accHeadId,Date fromDate,Date toDate){

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("fromDt", fromDate);
		parameters.put("toDt", toDate);
		parameters.put("accHeadIdPassed", accHeadId);

		return super.findAllWithGivenCondition(LedgerMcg.LIST_FOR_LEDGER_REPORT, parameters);
	}

	public double findBroughtDownAmount(String toDate,int accHeadId){
		/*String qry = "SELECT SUM(l.drAmt)-SUM(l.crAmt) total FROM LedgerMcg l "
				+ "JOIN l.accountHead a "
				+ "WHERE a.accHeadId='"+accHeadId+"' "
				+ "AND l.postedDate='"+toDate+"' "
				+ "AND l.ledId >= (SELECT MIN(ll.ledId) FROM LedgerMcg ll "
				+ "WHERE ll.postedDate = "
				+ "(SELECT lll.postedDate FROM LedgerMcg lll WHERE lll.ledId = "
				+ "(SELECT MAX(llll.ledId) FROM LedgerMcg llll "
				+ "WHERE llll.journalVourcher.journalPk.jvNo=-1 "
				+ "AND llll.postedDate ='"+toDate+"')) "
				+ "AND l.isOpening=1 )";

		Query query = getEntityManager().createNativeQuery(qry);*/

		return ((BigDecimal) DirectSqlUtils.getSingleResult("SELECT ABS(isnull((SUM(dr_amt)-SUM(cr_amt)),0.00)) AS PrevBalance \n" +
				"				 FROM ledger_mcg   \n" +
				"				 WHERE acc_code_id = '"+accHeadId+"'\n" +
				"				 AND posted_date < '"+toDate+"'\n" +
				"				 AND led_id >= (select ISNULL(min(led_id), 0)\n" +
				"				 from  ledger_mcg \n" +
				"				 where  posted_date = (select posted_date \n" +
				"					from  ledger_mcg \n" +
				"				 		where  led_id = (select ISNULL(max(led_id),0) from  ledger_mcg\n" +
				"				  			where posted_date < '"+toDate+"'	))\n" +
				"						and is_opening = 1	)")).doubleValue();


		//	return  ((BigDecimal) query.getSingleResult()).doubleValue();
	}

}
