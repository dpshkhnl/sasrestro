package sasrestro.sessionejb.account;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import sasrestro.model.account.FormCodeModel;
import sasrestro.sessionejb.core.GenericDAO;

@Stateless
@LocalBean
public class FormCodeEJB extends GenericDAO<FormCodeModel>{
	public FormCodeEJB() {
		super(FormCodeModel.class);
		// TODO Auto-generated constructor stub
	}
	public List<FormCodeModel> findCodeByRemarks(String RemarksPassed){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("fcRemarksPassed", RemarksPassed);     

		return super.findAllWithGivenCondition(FormCodeModel.FIND_BY_FC_REMARKS, parameters);
	}
	
	public FormCodeModel findCodeByRemarksAndLabel(String RemarksPassed,String labelPassed){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("fcRemarksPassed", RemarksPassed);     
		parameters.put("labelPassed", labelPassed);     
		return super.findOneResult(FormCodeModel.FIND_BY_FC_REMARKS_LABEL, parameters);
	}
	
	public void createFormCode(FormCodeModel formCode) {
    	save(formCode);
    }
 
    public void updateFormCode(FormCodeModel formCode) {
    	FormCodeModel persistedFormCode = find(formCode.getFormCodeId());
    	persistedFormCode.setLabel(formCode.getLabel());
    	persistedFormCode.setRemarks(formCode.getRemarks());
    	update(persistedFormCode);
    }
    public void deleteFormCode(FormCodeModel formCode)
    {
    	delete(formCode.getFormCodeId(), FormCodeModel.class);
    }
    
    public FormCodeModel getSelectedCode(String formCode) {
    	FormCodeModel result = getFormCode(formCode);
		return result;
	}
	private FormCodeModel getFormCode(String formCode) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("fcformcodePassed", formCode);
		return super.findOneResult(FormCodeModel.FIND_BY_FC, parameter);
	}
	
	public List<FormCodeModel> listAll() {
		List<FormCodeModel> result= getFeeScheme();
		return result;
	}
	private List<FormCodeModel> getFeeScheme() {
			return super.findAll();
			
	}
}
