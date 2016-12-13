package sasrestro.mb.account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.DualListModel;
import org.primefaces.model.TreeNode;

import sasrestro.misc.AbstractMB;
import sasrestro.model.account.AccHeadMap;
import sasrestro.model.account.AccHeadMcg;
import sasrestro.model.account.FormCodeModel;
import sasrestro.sessionejb.account.AccHeadEJB;
import sasrestro.sessionejb.account.AccHeadMapEJB;
import sasrestro.sessionejb.account.FormCodeEJB;

@ViewScoped
@ManagedBean(name = "accHeadMappingMB")
public class AccountHeadMappingMB extends AbstractMB implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB
	private AccHeadEJB accHeadEJB;
	@EJB
	AccHeadMapEJB accMapEJB;
	@EJB
	FormCodeEJB formCodeEJB;

	private TreeNode root, selectedNode;
	private FormCodeModel mapPurpose;
	private List<AccHeadMcg> lstAccHead;
	private List<AccHeadMcg> lstSelectedAccHead;
	private List<AccHeadMap> lstMappedAccount;
	private List<AccHeadMcg> tempList;
	private List<AccHeadMcg> saveLst;
	private List<AccHeadMap> originalDatabase;
	private List<AccHeadMap> saveLstMap;
	private List<AccHeadMap> deleteLstMap;

	public List<AccHeadMcg> getTempList() {
		if(tempList == null)
			tempList = new ArrayList<AccHeadMcg>();
		return tempList;
	}

	public void setTempList(List<AccHeadMcg> tempList) {
		this.tempList = tempList;
	}

	private DualListModel<AccHeadMcg> dlstAccounts;

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public TreeNode getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}

	public FormCodeModel getMapPurpose() {
		if (mapPurpose == null)
			mapPurpose = new FormCodeModel();
		return mapPurpose;
	}

	public void setMapPurpose(FormCodeModel mapPurpose) {
		this.mapPurpose = mapPurpose;
	}

	public List<AccHeadMcg> getLstAccHead() {
		if (lstAccHead == null)
			lstAccHead = new ArrayList<AccHeadMcg>();
		return lstAccHead;
	}

	public void setLstAccHead(List<AccHeadMcg> lstAccHead) {
		this.lstAccHead = lstAccHead;
	}

	public List<AccHeadMcg> getLstSelectedAccHead() {
		if (lstSelectedAccHead == null)
			lstSelectedAccHead = new ArrayList<AccHeadMcg>();
		return lstSelectedAccHead;
	}

	public void setLstSelectedAccHead(List<AccHeadMcg> lstSelectedAccHead) {
		this.lstSelectedAccHead = lstSelectedAccHead;
	}

	public List<AccHeadMap> getLstMappedAccount() {
		return lstMappedAccount;
	}

	public void setLstMappedAccount(List<AccHeadMap> lstMappedAccount) {
		this.lstMappedAccount = lstMappedAccount;
	}

	public DualListModel<AccHeadMcg> getDlstAccounts() {
		return dlstAccounts;
	}

	public void setDlstAccounts(DualListModel<AccHeadMcg> dlstAccounts) {
		this.dlstAccounts = dlstAccounts;
	}

	public List<AccHeadMap> getsaveLstMap() {
		if (saveLstMap==null) {
			saveLstMap = new ArrayList<AccHeadMap>();
		}
		return saveLstMap;
	}

	public void setsaveLstMap(List<AccHeadMap> saveLstMap) {
		this.saveLstMap = saveLstMap;
	}

	public List<AccHeadMcg> getSaveLst() {
		if(saveLst == null){
			saveLst = new ArrayList<AccHeadMcg>();
		}
		return saveLst;
	}

	public void setSaveLst(List<AccHeadMcg> saveLst) {
		this.saveLst = saveLst;
	}

	public List<AccHeadMap> getOriginalDatabase() {
		if(originalDatabase == null){
			originalDatabase = new ArrayList<AccHeadMap>();
		}
		return originalDatabase;
	}

	public void setOriginalDatabase(List<AccHeadMap> originalDatabase) {
		this.originalDatabase = originalDatabase;
	}

	public List<AccHeadMap> getDeleteLstMap() {
		if(deleteLstMap == null){
			deleteLstMap = new ArrayList<AccHeadMap>();
		}
		return deleteLstMap;
	}

	public void setDeleteLstMap(List<AccHeadMap> deleteLstMap) {
		this.deleteLstMap = deleteLstMap;
	}

	@PostConstruct
	public void init() {
		root = new DefaultTreeNode("Root", null);
		List<AccHeadMcg> lstRoots = new ArrayList<AccHeadMcg>();
		List<AccHeadMcg> lstNonRoots = new ArrayList<AccHeadMcg>();
		List<AccHeadMcg> lstParent = accHeadEJB.listParentNodeOnly();
		int nodeId = 0;
		TreeNode currRoot;
		for (AccHeadMcg obj : lstParent) {
			if (obj.getParent() == 0) {
				lstRoots.add(obj);
			} else {
				lstNonRoots.add(obj);
			}
		}
		for (AccHeadMcg obj : lstRoots) {
			nodeId = obj.getAccHeadId();
			currRoot = new DefaultTreeNode(obj, root);
			createNodes(nodeId, currRoot, lstNonRoots);
		}
		dlstAccounts = new DualListModel<AccHeadMcg>(getLstAccHead(),
				getLstSelectedAccHead());
	}

	private void createNodes(int accHeadId, TreeNode root,
			List<AccHeadMcg> dataList) {
		int nodeId = 0;
		for (AccHeadMcg obj : dataList) {
			nodeId = obj.getAccHeadId();
			if (obj.getParent() == accHeadId) {
				TreeNode node0 = new DefaultTreeNode(obj, root);
				createNodes(nodeId, node0, dataList);
			}
		}
	}

	public void onNodeSelect(NodeSelectEvent event) {
		AccHeadMcg obj = new AccHeadMcg();
		selectedNode = event.getTreeNode();
		getLstAccHead();
		getLstSelectedAccHead();
		lstAccHead.addAll(getTempList());
		if (selectedNode.getChildCount() <= 0) {
			obj = (AccHeadMcg) selectedNode.getData();
			lstAccHead = accHeadEJB.getChildrenList(obj.getAccHeadId());

		} else {
			obj = (AccHeadMcg) selectedNode.getData();
			lstAccHead = accHeadEJB.findAllNonRoot(obj.getAccCode());
		}
		arrangeLists();
		dlstAccounts = new DualListModel<AccHeadMcg>(lstAccHead,lstSelectedAccHead);
	}
	public void loadSelected(){
		getLstSelectedAccHead();
	}

	public void selectedData(){
		mapPurpose = formCodeEJB.find(mapPurpose.getFormCodeId());
		lstMappedAccount = accMapEJB.findCodevalByMappurpose(mapPurpose.getLabel());
		lstSelectedAccHead = new ArrayList<AccHeadMcg>();
		saveLst = new ArrayList<AccHeadMcg>();
		lstAccHead.addAll(getTempList());
		for (AccHeadMap SelectedItem : lstMappedAccount) {
			AccHeadMcg accHead = new AccHeadMcg();
			accHead = SelectedItem.getAccHeadModel();
			getOriginalDatabase().add(SelectedItem);
			lstSelectedAccHead.add(accHead);
		}
		getLstAccHead();
		getLstSelectedAccHead();
		arrangeLists();
		dlstAccounts = new DualListModel<AccHeadMcg>(lstAccHead,lstSelectedAccHead);
	}

	public void getAccountByMapPurpose() {
		lstMappedAccount = accMapEJB.findCodeByMappurpose(mapPurpose);
	}

	public void arrangeLists(){
		tempList = new ArrayList<AccHeadMcg>();
		for(int i=0; i<lstSelectedAccHead.size();i++){
			for(int j=0;j<lstAccHead.size();j++){
				if(lstAccHead.get(j).equals(lstSelectedAccHead.get(i))){
					tempList.add((lstSelectedAccHead.get(i)));
					lstAccHead.remove(j);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void updateList(TransferEvent event){
		if(getMapPurpose().getFormCodeId() == 0){
			displayErrorMessageToUser("Please select Map Purpose first.");
			return;
		}
		if(event.isAdd()){
			for(int i=0;i<event.getItems().size();i++){
				int index = contains((AccHeadMcg)event.getItems().get(i));
				if(index > 0){
					getDeleteLstMap().remove(originalDatabase.get(index-1));
				}
			}
			lstSelectedAccHead.addAll((Collection<? extends AccHeadMcg>) event.getItems());
			saveLst.addAll((Collection<? extends AccHeadMcg>) event.getItems());
		}else if(event.isRemove()){
			for(int i=0;i<event.getItems().size();i++){
				int index = contains((AccHeadMcg)event.getItems().get(i));
				if(index > 0){
					getDeleteLstMap().add(originalDatabase.get(index-1));
				}
			}
			lstAccHead.addAll((Collection<? extends AccHeadMcg>) event.getItems());
			saveLst.removeAll((Collection<? extends AccHeadMcg>) event.getItems());
		}
	}

	public int contains(AccHeadMcg event){
		List<AccHeadMcg> mcgList = new ArrayList<AccHeadMcg>();
		for(AccHeadMap acc : getOriginalDatabase()){
			AccHeadMcg amp = new AccHeadMcg();
			amp = acc.getAccHeadModel();
			mcgList.add(amp);
		}
		for(int i=0;i<mcgList.size();i++){
			if(mcgList.get(i).equals(event)){
				return i+1;
			}
		}
		return 0;
	}

	public void save(){
		if(getDeleteLstMap().size() > 0 ){
			try {
				for(int i=0; i<getDeleteLstMap().size();i++){
					System.out.println(":::::::::::::::::: accHead to be deleted "+getDeleteLstMap().get(i).getMappingPurpose());
					accMapEJB.delete(getDeleteLstMap().get(i).getAccHeadMapId(), AccHeadMap.class);
				}
				displayInfoMessageToUser("Account Map Deleted Successfully");
			} catch (Exception e) {
				e.printStackTrace();
				displayErrorMessageToUser("Error::  Could not Delete Account Map");
			}
			deleteLstMap = new ArrayList<AccHeadMap>();
		}
		if(getSaveLst().size() > 0 ){
			for (Iterator<AccHeadMcg> iterator = saveLst.iterator(); iterator.hasNext();) {
				AccHeadMcg accHeadMcg = (AccHeadMcg) iterator.next();
				AccHeadMap headMapObj = new AccHeadMap();
				headMapObj.setAccHeadModel(accHeadMcg);
				headMapObj.setMappingPurpose(mapPurpose.getLabel());
				getsaveLstMap().add(headMapObj);
			}
			try {
				accMapEJB.saveList(getsaveLstMap());
				displayInfoMessageToUser("Account Mapped Successfully");
			} catch (Exception e) {
				e.printStackTrace();
				displayErrorMessageToUser("Error::  Could not Map Account");
			}
		}
		resetMap();
	}

	public void resetMap(){
		saveLst = new ArrayList<AccHeadMcg>();
		tempList = new ArrayList<AccHeadMcg>();
		lstAccHead = new ArrayList<AccHeadMcg>();
		lstSelectedAccHead = new ArrayList<AccHeadMcg>();
		deleteLstMap = new ArrayList<AccHeadMap>();
		dlstAccounts = new DualListModel<AccHeadMcg>(lstAccHead,lstSelectedAccHead);
		mapPurpose = new FormCodeModel();
		init();
	}

	public List<FormCodeModel> getFormCodeByFee() {
		List<FormCodeModel> formCodebyRemarks = new ArrayList<FormCodeModel>();
		formCodebyRemarks=formCodeEJB.findCodeByRemarks("form_code");
		return formCodebyRemarks;
	}
}
