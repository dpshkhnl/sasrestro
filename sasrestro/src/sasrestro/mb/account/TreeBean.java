package sasrestro.mb.account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import sasrestro.misc.AbstractMB;
import sasrestro.misc.DirectSqlUtils;
import sasrestro.model.account.AccHeadMcg;

@ViewScoped
@ManagedBean(name = "treeBean")
public class TreeBean extends AbstractMB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TreeNode root, selectedNode;

	@ManagedProperty(value = AccHeadMB.INJECTION_NAME)
	private AccHeadMB accHeadMB;
	List<AccHeadMcg> accHeadList;

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public TreeBean() {

	}

	private void loadTree() {
		root = new DefaultTreeNode("Root", null);
		List<AccHeadMcg> lList = accHeadMB.getAllAccHeads();

		// iterator loop
		System.out.println("#1 iterator");
		Iterator<AccHeadMcg> iterator = lList.iterator();
		List<AccHeadMcg> listRoots = new ArrayList<AccHeadMcg>();
		List<AccHeadMcg> listOtherNodes = new ArrayList<AccHeadMcg>();
		while (iterator.hasNext()) {
			AccHeadMcg accHeadMcg = iterator.next();
			if (accHeadMcg.getParent() == 0) {
				// add the root node into list
				listRoots.add(accHeadMcg);
			} else {
				listOtherNodes.add(accHeadMcg);
			}
		}
		Iterator<AccHeadMcg> iteratorRoots = listRoots.iterator();
		while (iteratorRoots.hasNext()) {
			AccHeadMcg accHeadMcgRoot = iteratorRoots.next();
			String labelName = accHeadMcgRoot.getAccName();
			int nodeId = accHeadMcgRoot.getAccHeadId();
			System.out.println("The Value on Node =" + labelName);
			TreeNode currRoot = new DefaultTreeNode(accHeadMcgRoot, root);
			findChildAndCreateNodes(nodeId, currRoot, listOtherNodes);
		}
	}

	private void findChildAndCreateNodes(int accHeadId, TreeNode root,
			List<AccHeadMcg> dataList) {
		// iterator loop
		Iterator<AccHeadMcg> iterator = dataList.iterator();
		while (iterator.hasNext()) {
			// List<AccHeadMcg> childList= new ArrayList<AccHeadMcg>();
			AccHeadMcg accHeadMcg = iterator.next();
			int nodeId = accHeadMcg.getAccHeadId();
			// String labelName = accHeadMcg.getAccName();
			if (accHeadMcg.getParent() == accHeadId) {
				TreeNode node0 = new DefaultTreeNode(accHeadMcg, root);
				findChildAndCreateNodes(nodeId, node0, dataList);
			}

		}

	}

	public void setAccHeadMB(AccHeadMB accHeadMB) {
		this.accHeadMB = accHeadMB;
		/**
		 * This explicit object initialization of cascaded objects should be
		 * done here because JSF-EL won't do this itself and throws folllowing
		 * error while execution :
		 * 
		 * javax.el.PropertyNotFoundException: /pages/account/accounthead.xhtml
		 * 
		 * @43,26 value="#{treeBean.accHeadMB.accHead.accHeadId}": Target
		 *        Unreachable, 'accHead' returned null
		 */
		this.accHeadMB.setAccHead(new AccHeadMcg());
		loadTree();
	}

	public AccHeadMB getAccHeadMB() {
		return accHeadMB;
	}

	public TreeNode getRoot() {
		return root;
	}

	public TreeNode getSelectedNode() {
		System.out.println("KKKK... from getSelectedNode() Tree Node is ="
				+ selectedNode);
		return selectedNode;
	}

	public void setSelectedNode(TreeNode selectedNode) {
		System.out.println("KKKK... from setSelected() Tree Node is ="
				+ selectedNode);
		this.selectedNode = selectedNode;
	}

	public void saveNewAccHead() {
		accHeadMB.createAccHead();
		loadTree();
	}

	public void removeAccHead() {
		accHeadMB.deleteAccHead();
		loadTree();
	}

	public void updateAccHead() {
		accHeadMB.updateAccHead();
		loadTree();
	}

	public void onNodeExpand(NodeExpandEvent event) {
		TreeNode treeNodeSelected = event.getTreeNode();
		AccHeadMcg selectedAccHeadObj = ((AccHeadMcg) treeNodeSelected
				.getData());

		displayInfoMessageToUser("Expanded Head is : "
				+ selectedAccHeadObj.getAccName());
	}

	public void onNodeCollapse(NodeCollapseEvent event) {
		TreeNode treeNodeSelected = event.getTreeNode();
		AccHeadMcg selectedAccHeadObj = ((AccHeadMcg) treeNodeSelected
				.getData());

		/*
		 * FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
		 * "Collapsed", selectedAccHeadObj.getAccName());
		 * FacesContext.getCurrentInstance().addMessage(null, message);
		 */
		displayInfoMessageToUser("Collapsed Head is : "
				+ selectedAccHeadObj.getAccName());
	}

	public void onNodeSelect(NodeSelectEvent event) {

		/*
		 * ExternalContext ec =
		 * FacesContext.getCurrentInstance().getExternalContext();
		 * 
		 * Object requestObject = ec.getRequest(); HttpServletRequest req =
		 * (HttpServletRequest) requestObject; HttpSession session =
		 * req.getSession(); if(session==null) {
		 * displayErrorMessageToUser("He's DEAD Jim! Go Home now."); }
		 */

		TreeNode treeNodeSelected = event.getTreeNode();
		AccHeadMcg selectedAccHeadObj = ((AccHeadMcg) treeNodeSelected
				.getData());
		/*
		 * FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
		 * "Selected", selectedAccHeadObj.getAccName());
		 * FacesContext.getCurrentInstance().addMessage(null, message);
		 */

		displayInfoMessageToUser("Selected Head is : "
				+ selectedAccHeadObj.getAccName());
		System.out.println("The node is=" + selectedAccHeadObj.getAccName());
		this.selectedNode = treeNodeSelected;
		if (selectedAccHeadObj.getParent() > 0) {
			selectedAccHeadObj
					.setParentName(selectedAccHeadObj.getParent() > 0 ? accHeadMB.accHeadEJB
							.find(selectedAccHeadObj.getParent()).getAccName()
							: null);
			// selectedAccHeadObj.setParentName(selectedAccHeadObj.getParent());
		}
		this.accHeadMB.setAccHead(selectedAccHeadObj);

	}

	public void onNodeUnselect(NodeUnselectEvent event) {
		TreeNode treeNodeSelected = event.getTreeNode();
		AccHeadMcg selectedAccHeadObj = ((AccHeadMcg) treeNodeSelected
				.getData());

		displayInfoMessageToUser("UnSelected Head is : "
				+ selectedAccHeadObj.getAccName());
		/*
		 * FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
		 * "Unselected", selectedAccHeadObj.getAccName());
		 * FacesContext.getCurrentInstance().addMessage(null, message);
		 */
	}

	public void onAccTypeSelect() {
		setParentalSettingsOnChild();

	}

	private void setParentalSettingsOnChild() {
		/*
		 * whenever this event is fired from UI, at least the accountHead object
		 * has accountType value set. For particular, we intend this to use on a
		 * child node so, the accuntHead object has its fields set using the
		 * values in UI tree and the UI input fields. ie. we already have an
		 * accountHead instance by here. Thus, we use this method to check and
		 * balance the accountType and drCr type of selected child node.
		 */
		int parentAccType = 0;
		/*
		 * Case of a child node; we prevent changing accType and drcr types
		 */
		if (this.accHeadMB.getAccHead().getParent() > 0) {
			parentAccType = Integer.valueOf(DirectSqlUtils
					.getSingleValueFromTable(
							"select acc_type from acc_head_mcg where acc_head_id="
									+ this.accHeadMB.getAccHead().getParent()
									+ "").toString());
			System.out.println("The selected item selected is"
					+ this.accHeadMB.getAccHead().getAccType()
					+ " and acc Name="
					+ this.accHeadMB.getAccHead().getAccName());
			/*
			 * Now, we reset the accType and drcr type of this selected child
			 * node to match to its parent and prevent from being updated by UI
			 * changed values what so ever.
			 */
			this.accHeadMB.getAccHead().setAccType(parentAccType);
		}
		/*
		 * Case of a basic root node with children; we prevent changing accType,
		 * drcr type and reset opening balance back to 0.00
		 */
		else if (this.accHeadMB.getAccHead().getParent() == 0
				&& getChildrenCountOfThisHead() > 0) {
			parentAccType = Integer.valueOf(DirectSqlUtils
					.getSingleValueFromTable(
							"select acc_type from acc_head_mcg where acc_head_id="
									+ this.accHeadMB.getAccHead()
											.getAccHeadId() + "").toString());
			this.accHeadMB.getAccHead().setMinBal(0.00);
			/*
			 * Now, reset back the values of accountHead object being refrenced
			 * here
			 */
			this.accHeadMB.getAccHead().setAccType(parentAccType);

		} else if (this.accHeadMB.getAccHead().getParent() == 0
				&& getChildrenCountOfThisHead() == 0) {
			this.accHeadMB.getAccHead().setAccType(
					accHeadMB.getAccHead().getAccType());
		}
		setDrCrValueAsOfParent(this.accHeadMB.getAccHead().getAccType());
	}

	private int getChildrenCountOfThisHead() {
		int validParentId = this.accHeadMB.getAccHead().getAccHeadId();
		int retValue = 0;
		if (validParentId > 0) {
			retValue = Integer
					.valueOf(String.valueOf(DirectSqlUtils
							.getSingleValueFromTable("select count(*) from acc_head_mcg where parent='"
									+ this.accHeadMB.getAccHead()
											.getAccHeadId() + "'")));
		}
		return retValue;
	}

	private void setDrCrValueAsOfParent(int parentType) {
		if (parentType == 1 || parentType == 2) {
			this.accHeadMB.getAccHead().setDrcr("dr");
		} else if (parentType == 3 || parentType == 4) {
			this.accHeadMB.getAccHead().setDrcr("cr");
		}
	}

	/**
	 * @param event
	 *            For Journal Voucher Entry View set the accCode and Label to
	 *            the accCode and accLabel on input textfield
	 * @author Ganesh-Magnus
	 */
	public void onNodeSelectForJournal(NodeSelectEvent event) {

		TreeNode nodeSelected = event.getTreeNode();
		System.out.println("Node is selected");
		AccHeadMcg selectedAccHeadObj = (AccHeadMcg) nodeSelected.getData();
		System.out.println("Acc Code--" + selectedAccHeadObj.getAccCode());
		this.accHeadMB.getAccAliasOnSelect(selectedAccHeadObj.getAccCode(),
				selectedAccHeadObj.getAccName(),
				selectedAccHeadObj.getAccHeadId(),
				selectedAccHeadObj.getAccType());

		this.selectedNode = nodeSelected;

	}
}