package sasrestro.mb.account;



import sasrestro.model.account.JournalVoucherDetailModel;
import sasrestro.model.account.JournalVoucherModel;

public class JournalDocument {

	private JournalVoucherModel jvm;
	private JournalVoucherDetailModel jvdm;
	

	public JournalDocument(Object obj) {
		if (obj instanceof JournalVoucherModel) {
			this.jvm = (JournalVoucherModel) obj;

		} else {
			this.jvdm = (JournalVoucherDetailModel) obj;
		}
	}

	public JournalVoucherModel getJvm() {
		return jvm;
	}

	public void setJvm(JournalVoucherModel jvm) {
		this.jvm = jvm;
	}

	public JournalVoucherDetailModel getJvdm() {
		return jvdm;
	}

	public void setJvdm(JournalVoucherDetailModel jvdm) {
		this.jvdm = jvdm;
	}
	

}
