package sasrestro.mb.account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import sasrestro.misc.AbstractMB;
import sasrestro.misc.DirectSqlUtils;
import sasrestro.model.account.AccHeadMap;
import sasrestro.model.account.AccHeadMcg;
import sasrestro.model.account.FormCodeModel;
import sasrestro.sessionejb.account.AccHeadEJB;
import sasrestro.sessionejb.account.AccHeadMapEJB;
import sasrestro.sessionejb.account.FormCodeEJB;

@RequestScoped
@ManagedBean(name = "AccHeadMapMB")
public class AccHeadMapMB extends AbstractMB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String INJECTION_NAME = "#{AccHeadMapMB}";
	private AccHeadMap accheadmap;
	private FormCodeModel formCode;
	private List<AccHeadMap> listaccHeadMap;
	private List<AccHeadMap> accHeadbyMappurpose;
	private List<String> selectedItems;

	private AccHeadMcg headmcg;
 
	@EJB
	AccHeadMapEJB accmapEJB;

	@EJB
	private AccHeadEJB accHeadEjb;

	@EJB
	private FormCodeEJB formCodeEjb;

	
	public AccHeadMapMB() {  
		
      
    }  
	
		
	

	public List<String> getSelectedItems() {
		if(selectedItems==null){
			selectedItems=new ArrayList<String>();
		}
		return selectedItems;
	}

	public void setSelectedItems(List<String> selectedItems) {
		this.selectedItems = selectedItems;
	}

	public AccHeadMap getAccheadmap() {
		if (accheadmap == null)
			this.accheadmap = new AccHeadMap();
		return accheadmap;
	}

	public void setAccheadmap(AccHeadMap accheadmap) {
		this.accheadmap = accheadmap;
	}

	public void submit() {
				
		listaccHeadMap=accmapEJB.findCodevalByMappurpose(formCode.getLabel());
		List<String> dbList = new ArrayList<String>();
		for (AccHeadMap SelectedItem : listaccHeadMap) {
			dbList.add(SelectedItem.getAccHeadModel().getAccCode());
		}
		for (AccHeadMap accHeadMap : listaccHeadMap) {
			
			if (!selectedItems.contains(accHeadMap.getAccHeadModel().getAccCode()))
			{
				int accHeadId=Integer
						.valueOf(String.valueOf(DirectSqlUtils
								.getSingleValueFromTable("select acc_head_map_id from acc_head_map_mcg where acc_head_id='"
										+accHeadMap.getAccHeadModel().getAccHeadId()+ "' and map_purpose='"+formCode.getLabel()+"'")));
			//delete account head
			//	System.out.println("ACC HEAD ID"+accHeadId);
				if(checkformap_purpose(accHeadId)){
					displayInfoMessageToUser("Deletion unsuccessful.Account head is in use.");
					return;
				}
				else
			accmapEJB.deleteAccHeadMap(accHeadMap);
			}
			
		}
		
		for (String accCode : selectedItems) {
			
			if (!dbList.contains(accCode))
			{
				//insert new account head
				accheadmap=new AccHeadMap();
				accheadmap.setAccHeadModel(accHeadEjb
						.getSelectedAccByAccCode(accCode));
				accheadmap.setMappingPurpose(formCode.getLabel());
				accmapEJB.save(accheadmap);
			}
			
		}
		displayInfoMessageToUser("Inserted Sucessfully");
	}




	public FormCodeModel getFormCode() {
		if (formCode == null)
			formCode = new FormCodeModel();
		return formCode;
	}

	public void setFormCode(FormCodeModel formcode) {
		this.formCode = formcode;
	}

	public void Reset() {
		System.out.println("Reset Called");
		accheadmap = null;
		accheadmap = new AccHeadMap();
		formCode = null;
		formCode = new FormCodeModel();
	}

	public List<AccHeadMap> getAccHeadmap() {
		if (listaccHeadMap == null) {
			listaccHeadMap = new ArrayList<AccHeadMap>();
		}
		return listaccHeadMap;
	}

	public void setAccHeadmap(List<AccHeadMap> accHeadmap) {
		this.listaccHeadMap = accHeadmap;
	}

	public AccHeadMcg getHeadmcg() {
		if (headmcg == null) {
			headmcg = new AccHeadMcg();
		}
		return headmcg;
	}

	public void setHeadmcg(AccHeadMcg headmcg) {
		this.headmcg = headmcg;
	}

	public List<AccHeadMap> getaccHeadByMappurpose() {

		accHeadbyMappurpose = accmapEJB.findAll();
		
		//accHeadbyMappurpose = accmapEJB.findCodeByMappurpose(formCodeEjb.findCodeRemarks("form_code"));

		return accHeadbyMappurpose;
	}
		
	public void getSavedData(){
		System.out.println("the selected combo is:"+formCode.getLabel());
			listaccHeadMap=accmapEJB.findCodevalByMappurpose(formCode.getLabel());
			selectedItems = new ArrayList<String>();
			//selectedItems.clear();
			//getSelectedItems();
			
			for (AccHeadMap SelectedItem : listaccHeadMap) {
				selectedItems.add(SelectedItem.getAccHeadModel().getAccName());
				System.out.println("Try "+SelectedItem.getAccHeadModel());
				System.out.println("Naya Items are:"+selectedItems+" "+selectedItems.size());
			}
			System.out.println("My item "+selectedItems.toString());
			
//		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successful",  "Edit Successful.");  
 //       FacesContext.getCurrentInstance().addMessage(null, message); 
	
	}




	private boolean checkformap_purpose(int i) {
		Boolean Result;
		int Count = Integer
				.valueOf(String.valueOf(DirectSqlUtils
						.getSingleValueFromTable("select count(plan_id) from feescheme_detail_mcg where acc_head_map_id='"
								+ i + "'")));
		System.out.println("the count is::::"+Count);
		if(Count>=1){
			Result=true;
		}
		else{
			Result=false;
		}
		return Result;
	
	}
	
}
