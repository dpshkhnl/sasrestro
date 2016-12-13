package sasrestro.mb.account;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import sasrestro.model.account.JournalVoucherDetailModel;
import sasrestro.model.account.JournalVoucherModel;

@ManagedBean(name = "journalTreeBean")
public class JournalTreeBean implements Serializable {

	private static final long serialVersionUID = 1L;
	// private TreeNode root;

	@ManagedProperty(value = JournalVoucherDetailMB.INJECTION_NAME_JV)
	private JournalVoucherDetailMB objJDMB;

	private TreeNode root;

	public JournalTreeBean() {
	
	}

	public TreeNode getRoot() {
		loadTree();
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	@SuppressWarnings("unused")
	private void loadTree() {
		getObjJDMB();
		root = new DefaultTreeNode("Root", null);
		List<JournalVoucherModel> listJVM = objJDMB.getObjAllJVM();
		Iterator<JournalVoucherModel> itr = listJVM.iterator();
		while (itr.hasNext()) {
			JournalVoucherModel jvm = itr.next();
			TreeNode documents = new DefaultTreeNode(new JournalDocument(jvm),
					root);

			List<JournalVoucherDetailModel> listJVDM = jvm.getJvdmList();
			Iterator<JournalVoucherDetailModel> itrjvdm = listJVDM.iterator();
			while (itrjvdm.hasNext()) {
				JournalVoucherDetailModel jvdm = itrjvdm.next();
				TreeNode work = new DefaultTreeNode(new JournalDocument(jvdm),
						documents);
			}

		}

	}

	public JournalVoucherDetailMB getObjJDMB() {
		if (objJDMB == null) {
			objJDMB = new JournalVoucherDetailMB();
		}
		return objJDMB;
	}

	public void setObjJDMB(JournalVoucherDetailMB objJDMB) {
		this.objJDMB = objJDMB;
//		System.out.println("Hello ia ,a");
		// this.objJDMB.setObjJVM(new JournalVoucherModel());
		// loadTree();
	}
}
