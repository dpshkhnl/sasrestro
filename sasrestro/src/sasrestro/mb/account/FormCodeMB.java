package sasrestro.mb.account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import sasrestro.model.account.FormCodeModel;
import sasrestro.sessionejb.account.FormCodeEJB;

@ViewScoped
@ManagedBean(name="formCodeMB")
public class FormCodeMB implements Serializable{

	/**
	 * 
	 */
	public static final String INJECTION_NAME = "#{formCodeMB}";
	private static final long serialVersionUID = 1L;
	private FormCodeModel formModel;
	private List<FormCodeModel> codeDetail;
	
	
	@EJB
	FormCodeEJB formCodeEJB;
	
	private List<FormCodeModel> formCodebyRemarks;
	private List<FormCodeModel> formCodeAll;
	
	public void setFormCodebyRemarks(List<FormCodeModel> formCodebyRemarks) {
		this.formCodebyRemarks = formCodebyRemarks;
	}

	public FormCodeMB(){
	}

	

	public List<FormCodeModel> getFormCodeByRemarks() {
		if(formCodebyRemarks==null)
		{
			formCodebyRemarks=new ArrayList<FormCodeModel>();
			
		}
		formCodebyRemarks=formCodeEJB.findCodeByRemarks("Membership Status");
		return formCodebyRemarks;
	}
	
	public List<FormCodeModel> getFormCodeByFee() {
		
			formCodebyRemarks=new ArrayList<FormCodeModel>();
			formCodebyRemarks=formCodeEJB.findCodeByRemarks("form_code");
		
		return formCodebyRemarks;
	}

	public FormCodeModel getFormModel() {
		
		if(formModel==null){
			formModel=new FormCodeModel();
		}
		return formModel;
	}

	public void setFormModel(FormCodeModel formModel) {
		this.formModel = formModel;
	}
	
	public List<FormCodeModel> getAllData() {
		return setCodeDetail(formCodeEJB.listAll());
		
	}
	
	

	public void saveformCode() {
	
		if(!beforeSave())
		{
			return;
		}
		if(getFormModel().getFormCodeId()==0){
		formCodeEJB.createFormCode(getFormModel());
		
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successful",  "Saved Successfully.");  
        FacesContext.getCurrentInstance().addMessage(null, message); 
		}
		else{
			formCodeEJB.updateFormCode(getFormModel());
			
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Update Successful",  "FormCode Updated Successfully.");  
	        FacesContext.getCurrentInstance().addMessage(null, message); 
	
		}
		}
			
		
		public boolean beforeSave()
		{
			if(getFormModel().getLabel().equals("") || getFormModel().getRemarks().equals(""))
			{
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Some fields are empty.",  "Some fields are empty.");  
		        FacesContext.getCurrentInstance().addMessage(null, message);
		        return false;
			}else
				return true;
	}

	public List<FormCodeModel> getCodeDetail() {
		return codeDetail;
	}

	public List<FormCodeModel> setCodeDetail(List<FormCodeModel> codeDetail) {
		this.codeDetail = codeDetail;
		return codeDetail;
	}

	public void deleteformCode()
	{
		if (getFormModel() == null || getFormModel().getFormCodeId()==0)
			return;
		formCodeEJB.deleteFormCode(getFormModel());
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successful",  "Form Code Deleted Successfully.");  
		FacesContext.getCurrentInstance().addMessage(null, message); 
	}

	public List<FormCodeModel> getFormCodeAll() {
		if(formCodeAll==null){
			formCodeAll=new ArrayList<FormCodeModel>();
		}
		formCodeAll=formCodeEJB.findAll();
		return formCodeAll;
	}

	public void setFormCodeAll(List<FormCodeModel> formCodeAll) {
		this.formCodeAll = formCodeAll;
	}

	
}
