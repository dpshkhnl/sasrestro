package sasrestro.sessionejb.account;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import sasrestro.model.account.AccHeadMap;
import sasrestro.model.account.FormCodeModel;
import sasrestro.sessionejb.core.GenericDAO;
@Stateless
@LocalBean
public class AccHeadMapEJB extends GenericDAO<AccHeadMap>{

	public AccHeadMapEJB() {
		super(AccHeadMap.class);

	}

	public void  InsertData(List<AccHeadMap> accheadmap) {
		for(AccHeadMap acc: accheadmap){
			save(acc);
		}
	}

	public List<AccHeadMap> findCodeByMappurpose(FormCodeModel mapPurpose) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("mappurposePassed", mapPurpose);     

		return super.findAllWithGivenCondition(AccHeadMap.FIND_BY_MP, parameters);
	}
	
			// Acc Head mapping
	
	public List<AccHeadMap> findByMapPurpose(String mapPurpose){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("mappurposePassed", mapPurpose);
		
		return super.findAllWithGivenCondition(AccHeadMap.FIND_BY_MP, param);
	}
	
	public AccHeadMap checkBankAccountHead(int accHeadId){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("accHeadIdPassed", accHeadId);

		return super.findOneResult(AccHeadMap.CHECK_BANK_ACCOUNT_HEAD, parameters);
	}
	
	public AccHeadMap checkCashInHandAccountHead(int accHeadId){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("accHeadIdPassed", accHeadId);
		
		return super.findOneResult(AccHeadMap.CHECK_CASH_IN_HAND, parameters);
	}

	public List<AccHeadMap> listAll() {
		List<AccHeadMap> result = findAll();
		return result;
	}

	public List<AccHeadMap> findCodevalByMappurpose(String label) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("mappurposePassed", label);     

		return super.findAllWithGivenCondition(AccHeadMap.FIND_BY_MP, parameters);
	}

	public void deleteAccHeadMap(AccHeadMap accHeadmap) {
		delete(accHeadmap.getAccHeadMapId(), AccHeadMap.class);
	}


}