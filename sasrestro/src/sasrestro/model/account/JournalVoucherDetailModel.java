package sasrestro.model.account;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


@Entity
@Table(name = "journal_voucher_details_mcg",uniqueConstraints={@UniqueConstraint(name="unqJvDetConstraint",columnNames={"jv_det_id","fy_id","jv_no","jv_type"})})
public class JournalVoucherDetailModel implements Serializable {

	private static final long serialVersionUID = 1L;
	// public static final String Find_Pending_JV =
	// "JournalVoucherDetailModel.findPendingJV";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "jv_det_id")
	private long jvDetailId;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({@JoinColumn(name="jv_no", referencedColumnName="jv_no"), @JoinColumn(name="fy_id",referencedColumnName="fy_id"),@JoinColumn(name="jv_type",referencedColumnName="jv_type")})
	private JournalVoucherModel jvm;



	@Column(name = "br_id", nullable = false)
	private int br_id;

	@ManyToOne
	@JoinColumn(name="acc_code_id", referencedColumnName="acc_head_id")
	private AccHeadMcg accountHead;


	@Column(name = "dr_amt",columnDefinition="Decimal(28,2) default '0.00'", nullable = false)
	private double drAmt;
	@Column(name = "cr_amt",columnDefinition="Decimal(28,2) default '0.00'", nullable = false)
	private double crAmt;
	@Column(name = "narration", nullable = false, length = 500)
	private String narration;

	/**
	 * @return the jvDetailId
	 */
	public long getJvDetailId() {
		return jvDetailId;
	}

	/**
	 * @param jvDetailId the jvDetailId to set
	 */
	public void setJvDetailId(long jvDetailId) {
		this.jvDetailId = jvDetailId;
	}

	/**
	 * @return the jvm
	 */
	public JournalVoucherModel getJvm() {
		return jvm;
	}

	/**
	 * @param jvm the jvm to set
	 */
	public void setJvm(JournalVoucherModel jvm) {
		this.jvm = jvm;
	}

	/**
	 * @return the br_id
	 */
	public int getBr_id() {
		return br_id;
	}

	/**
	 * @param br_id the br_id to set
	 */
	public void setBr_id(int br_id) {
		this.br_id = br_id;
	}

	/**
	 * @return the accountHead
	 */
	public AccHeadMcg getAccountHead() {
		return accountHead;
	}

	/**
	 * @param accountHead the accountHead to set
	 */
	public void setAccountHead(AccHeadMcg accountHead) {
		this.accountHead = accountHead;
	}

	/**
	 * @return the drAmt
	 */
	public double getDrAmt() {
		return drAmt;
	}

	/**
	 * @param drAmt the drAmt to set
	 */
	public void setDrAmt(double drAmt) {
		this.drAmt = drAmt;
	}

	/**
	 * @return the crAmt
	 */
	public double getCrAmt() {
		return crAmt;
	}

	/**
	 * @param crAmt the crAmt to set
	 */
	public void setCrAmt(double crAmt) {
		this.crAmt = crAmt;
	}

	/**
	 * @return the narration
	 */
	public String getNarration() {
		return narration;
	}

	/**
	 * @param narration the narration to set
	 */
	public void setNarration(String narration) {
		this.narration = narration;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((accountHead == null) ? 0 : accountHead.hashCode());
		result = prime * result + br_id;
		long temp;
		temp = Double.doubleToLongBits(crAmt);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(drAmt);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (int) (jvDetailId ^ (jvDetailId >>> 32));
		result = prime * result + ((jvm == null) ? 0 : jvm.hashCode());
		result = prime * result
				+ ((narration == null) ? 0 : narration.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof JournalVoucherDetailModel)) {
			return false;
		}
		JournalVoucherDetailModel other = (JournalVoucherDetailModel) obj;
		if (accountHead == null) {
			if (other.accountHead != null) {
				return false;
			}
		} else if (!accountHead.equals(other.accountHead)) {
			return false;
		}
		if (br_id != other.br_id) {
			return false;
		}
		if (Double.doubleToLongBits(crAmt) != Double
				.doubleToLongBits(other.crAmt)) {
			return false;
		}
		if (Double.doubleToLongBits(drAmt) != Double
				.doubleToLongBits(other.drAmt)) {
			return false;
		}
		if (jvDetailId != other.jvDetailId) {
			return false;
		}
		if (jvm == null) {
			if (other.jvm != null) {
				return false;
			}
		} else if (!jvm.equals(other.jvm)) {
			return false;
		}
		if (narration == null) {
			if (other.narration != null) {
				return false;
			}
		} else if (!narration.equals(other.narration)) {
			return false;
		}
		return true;
	}

	/*@Override
	public int hashCode() {

		return (int)getJvDetailId();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof JournalVoucherDetailModel) {
			JournalVoucherDetailModel other = (JournalVoucherDetailModel) obj;
			return other.getJvDetailId() == jvDetailId;
		}
		return false;
	}*/

}
