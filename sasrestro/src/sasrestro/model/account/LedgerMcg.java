package sasrestro.model.account;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import sasrestro.model.user.User;
import sasrestro.model.util.FiscalYrModel;

/**
 * @author nebula
 *
 */

@Entity
@Table(name="ledger_mcg",uniqueConstraints={@UniqueConstraint(name="unqLedgerConstraint",columnNames={"led_id","fy_id","jv_no","jv_type"})})
@NamedQueries({
	
	@NamedQuery(name="LedgerMcg.forLedgerReport",
			query="SELECT l FROM LedgerMcg l "
					+ "JOIN l.accountHead a "
					+ "WHERE (l.postedDate >= :fromDt AND l.postedDate <= :toDt) "
					+ "AND a.accHeadId=:accHeadIdPassed "
					+ "ORDER BY l.postedDate"),
					
	@NamedQuery(name="LedgerMcg.broughtDownAmt",
			query="SELECT SUM(l.drAmt)-SUM(l.crAmt) FROM LedgerMcg l "
			+ "JOIN l.accountHead a "
			+ "WHERE a.accHeadId=:accHeadIdPassed "
			+ "AND l.postedDate=:toDate "
			+ "AND l.ledId >= (SELECT MIN(ll.ledId) FROM LedgerMcg ll "
			+ "WHERE ll.postedDate = "
			+ "(SELECT lll.postedDate FROM LedgerMcg lll WHERE lll.ledId = "
			+ "(SELECT MAX(llll.ledId) FROM LedgerMcg llll "
			+ "WHERE llll.journalVourcher.journalPk.jvNo=-1 "
			+ "AND llll.postedDate =:toDate)) "
			+ "AND l.isOpening=1 )")
})
public class LedgerMcg implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String LIST_FOR_LEDGER_REPORT = "LedgerMcg.forLedgerReport";
	public static final String FIND_BROUGHT_DOWN_AMOUNT = "LedgerMcg.broughtDownAmt";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="led_id")
	private long ledId;

	/*@ManyToOne(fetch=FetchType.LAZY)
	//@JoinColumn(name="jv_no", referencedColumnName="jv_no")
	@JoinColumns({@JoinColumn(name="jv_no", referencedColumnName="jv_no"), @JoinColumn(name="fy_id",referencedColumnName="fy_id")})*/
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({@JoinColumn(name="jv_no", referencedColumnName="jv_no"), @JoinColumn(name="fy_id",referencedColumnName="fy_id",insertable=false,updatable=false),@JoinColumn(name="jv_type",referencedColumnName="jv_type",insertable=false,updatable=false)})
	private JournalVoucherModel journalVourcher;

	@OneToOne
	@JoinColumn(name="acc_code_id", referencedColumnName="acc_head_id")
	private AccHeadMcg accountHead;
	
	@ManyToOne
	@JoinColumn(name="to_acc_code_id", referencedColumnName="acc_head_id")
	private AccHeadMcg toAccountHead;

	@Column(name="dr_amt",columnDefinition="Decimal(28,2) default '0.00'")
	private double drAmt;

	@Column(name="cr_amt",columnDefinition="Decimal(28,2) default '0.00'")
	private double crAmt;

	@Temporal(TemporalType.DATE)
	@Column(name="posted_date")
	private Date postedDate;

	@Column(name="created_date_np")
	private String createdDateNp;

	@Column(name="remarks")
	private String remarks;

	@Column(name="br_id")
	private int brId;

	@ManyToOne
	@JoinColumn(name="fy_id", referencedColumnName="fy_id")
	private FiscalYrModel fiscalYear;
	
	@ManyToOne
	@JoinColumn(name="jv_type",referencedColumnName="cv_id")
	private CodeValue jvType;
	
	@Column(name="is_opening")
	private int isOpening;

	@ManyToOne
	@JoinColumn(name = "posted_by", referencedColumnName="id")
	private User postedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_date")
	private Date createdDate;

	@ManyToOne
	@JoinColumn(name = "updated_by", referencedColumnName="id")
	private User updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_date")
	private Date updatedDate;

	@Version
	@Column(name="update_count")
	private int updateCount;

	public LedgerMcg() {
		// TODO Auto-generated constructor stub
	}

	public long getLedId() {
		return ledId;
	}

	public void setLedId(int ledId) {
		this.ledId = ledId;
	}



	public double getDrAmt() {
		return drAmt;
	}

	public void setDrAmt(double drAmt) {
		this.drAmt = drAmt;
	}

	public double getCrAmt() {
		return crAmt;
	}

	public void setCrAmt(double crAmt) {
		this.crAmt = crAmt;
	}

	public Date getPostedDate() {
		return postedDate;
	}

	public void setPostedDate(Date postedDate) {
		this.postedDate = postedDate;
	}

	public String getCreatedDateNp() {
		return createdDateNp;
	}

	public void setCreatedDateNp(String createdDateNp) {
		this.createdDateNp = createdDateNp;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public AccHeadMcg getAccountHead() {
		return accountHead;
	}

	public void setAccountHead(AccHeadMcg accountHead) {
		this.accountHead = accountHead;
	}

	public AccHeadMcg getToAccountHead() {
		return toAccountHead;
	}

	public void setToAccountHead(AccHeadMcg toAccountHead) {
		this.toAccountHead = toAccountHead;
	}

	public int getBrId() {
		return brId;
	}

	public void setBrId(int brId) {
		this.brId = brId;
	}

	public FiscalYrModel getFiscalYear() {
		return fiscalYear;
	}

	public void setFiscalYear(FiscalYrModel fiscalYear) {
		this.fiscalYear = fiscalYear;
	}

	public int getIsOpening() {
		return isOpening;
	}

	public void setIsOpening(int isOpening) {
		this.isOpening = isOpening;
	}

	public User getPostedBy() {
		return postedBy;
	}

	public void setPostedBy(User postedBy) {
		this.postedBy = postedBy;
	}

	public User getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(User updatedBy) {
		this.updatedBy = updatedBy;
	}

	public JournalVoucherModel getJournalVourcher() {
		return journalVourcher;
	}

	public void setJournalVourcher(JournalVoucherModel journalVourcher) {
		this.journalVourcher = journalVourcher;
	}

	/**
	 * @return the jvType
	 */
	public CodeValue getJvType() {
		return jvType;
	}

	/**
	 * @param jvType the jvType to set
	 */
	public void setJvType(CodeValue jvType) {
		this.jvType = jvType;
	}

	@Override
	public int hashCode() {
		return (int)getLedId();
	}


	@Override
	public boolean equals(Object obj) {
		if (obj instanceof LedgerMcg) {
			LedgerMcg ledgerMcg = (LedgerMcg) obj;
			return ledgerMcg.getLedId() == ledId;
		}

		return false;
	}
	@PrePersist
	protected void onCreate() {
		//postedDate = new Date();
		createdDate = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		updatedDate = new Date();
	}

}
