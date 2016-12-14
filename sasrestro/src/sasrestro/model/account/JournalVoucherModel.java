package sasrestro.model.account;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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

@Entity
@Table(name = "journal_voucher_mcg", uniqueConstraints={@UniqueConstraint(columnNames={"fy_id","jv_no","jv_type"})})
//@SequenceGenerator(name="seqJvNo", initialValue=1,allocationSize=2)
@NamedQueries({
		@NamedQuery(name = "JournalVoucherModel.findPendingJV", 
					query = "SELECT u from JournalVoucherModel u "
							+ "where u.status=:status"),
		@NamedQuery(name = "JournalVoucherModel.FIND_BY_JV_NO", 
					query = "select u from JournalVoucherModel u  "
							+ "where u.journalPk.jvNo=:jv_no "),
		@NamedQuery(name = "JournalVoucherModel.findUnApproved", 
					query = "SELECT j FROM JournalVoucherModel j WHERE j.status = 0"),
		@NamedQuery(name="JournalVoucherModel.findByFsYr",
					query="SELECT j FROM JournalVoucherModel j "
							+ "JOIN j.fiscalYrModel f "
							+ "WHERE f.fyId=:fsYrPassed"),
		@NamedQuery(name="JournalVoucherModel.findByJvType",
					query="SELECT j FROM JournalVoucherModel j "
							+ "JOIN j.jvType jvt "
							+ "WHERE jvt.cvId=:jvTypePassed"),
		@NamedQuery(name="JournalVoucherModel.findByJvDt",
					query="SELECT j FROM JournalVoucherModel j "
							+ "WHERE j.jvDateAd BETWEEN :frDt AND :toDt"),
		@NamedQuery(name="JournalVoucherModel.findByAccHead",
					query="SELECT j FROM JournalVoucherModel j "
							+ "JOIN j.jvdmList jvd "
							+ "WHERE jvd.accountHead.accHeadId=:accHeadPassed"),
		@NamedQuery(name="JournalVoucherModel.findByFsYrJvType",
					query="SELECT j FROM JournalVoucherModel j "
							+ "JOIN j.fiscalYrModel f "
							+ "JOIN j.jvType jvt "
							+ "WHERE f.fyId=:fsYrIdPassed "
							+ "AND jvt.cvId=:jvTypePassed"),
		@NamedQuery(name="JournalVoucherModel.findByFsYrJvDate",
					query="SELECT j FROM JournalVoucherModel j "
							+ "JOIN j.fiscalYrModel f "
							+ "WHERE f.fyId=:fsYrIdPassed "
							+ "AND j.jvDateAd BETWEEN :frDt AND :toDt"),
		@NamedQuery(name="JournalVoucherModel.findByFsYrJvNo",
					query="SELECT j FROM JournalVoucherModel j "
							+ "JOIN j.fiscalYrModel f "
							+ "WHERE f.fyId=:fsYrIdPassed "
							+ "AND j.journalPk.jvNo=:jv_no"),
		@NamedQuery(name="JournalVoucherModel.findByFsYrAccHeadId",
					query="SELECT j FROM JournalVoucherModel j "
							+ "JOIN j.fiscalYrModel f "
							+ "JOIN j.jvdmList jvd "
							+ "WHERE f.fyId=:fsYrIdPassed "
							+ "AND jvd.accountHead.accHeadId=:accHeadPassed"),
		@NamedQuery(name="JournalVoucherModel.findByJvTypeJvDt",
					query="SELECT j FROM JournalVoucherModel j "
							+ "JOIN j.jvType jvt "
							+ "WHERE jvt.cvId=:jvTypePassed AND "
							+ "j.jvDateAd BETWEEN :frDt AND :toDt"),
		@NamedQuery(name="JournalVoucherModel.findByJvTypeJvNo",
					query="SELECT j FROM JournalVoucherModel j "
							+ "JOIN j.jvType jvt "
							+ "WHERE jvt.cvId=:jvTypePassed "
							+ "AND j.journalPk.jvNo=:jv_no"),
		@NamedQuery(name="JournalVoucherModel.findByJvTypeAccHead",
					query="SELECT j FROM JournalVoucherModel j "
							+ "JOIN j.jvType jvt "
							+ "JOIN j.jvdmList jvd "
							+ "WHERE jvt.cvId=:jvTypePassed "
							+ "AND jvd.accountHead.accHeadId=:accHeadPassed"),
		@NamedQuery(name="JournalVoucherModel.findByJvDtJvNo",
					query="SELECT j FROM JournalVoucherModel j "
							+ "WHERE j.jvDateAd BETWEEN :frDt AND :toDt "
							+ "AND j.journalPk.jvNo=:jv_no"),
		@NamedQuery(name="JournalVoucherModel.findByJvDtAccHead",
					query="SELECT j FROM JournalVoucherModel j "
							+ "JOIN j.jvdmList jvd "
							+ "WHERE j.jvDateAd BETWEEN :frDt AND :toDt "
							+ "AND jvd.accountHead.accHeadId=:accHeadPassed"),
	    @NamedQuery(name="JournalVoucherModel.findByJvNoAccHead",
	    			query="SELECT j FROM JournalVoucherModel j "
	    					+ "JOIN j.jvdmList jvd "
	    					+ "WHERE j.journalPk.jvNo=:jv_no "
	    					+ "AND jvd.accountHead.accHeadId=:accHeadPassed"),
	    @NamedQuery(name="JournalVoucherModel.findByFsYrJvTypeJvDate",
	    			query="SELECT j FROM JournalVoucherModel j "
	    					+ "JOIN j.fiscalYrModel f "
	    					+ "JOIN j.jvType jvt "
	    					+ "WHERE f.fyId=:fsYrIdPassed "
	    					+ "AND jvt.cvId=:jvTypePassed "
	    					+ "AND j.jvDateAd BETWEEN :frDt AND :toDt"),
	    @NamedQuery(name="JournalVoucherModel.findByJvTypeJvDtJvNo",
	    			query="SELECT j FROM JournalVoucherModel j "
	    					+ "JOIN j.jvType jvt "
	    					+ "WHERE jvt.cvId=:jvTypePassed "
	    					+ "AND j.jvDateAd BETWEEN :frDt AND :toDt "
	    					+ "AND j.journalPk.jvNo=:jv_no"),
	    @NamedQuery(name="JournalVoucherModel.findByJvDtJvNoAcchead",
	    			query="SELECT j FROM JournalVoucherModel j "
	    					+ "JOIN j.jvdmList jvd "
	    					+ "WHERE j.jvDateAd BETWEEN :frDt AND :toDt "
	    					+ "AND j.journalPk.jvNo=:jv_no "
	    					+ "AND jvd.accountHead.accHeadId=:accHeadPassed"),
	    @NamedQuery(name="JournalVoucherModel.findByFsYrJvTypeJvDtJvNo",
	    			query="SELECT j FROM JournalVoucherModel j "
	    					+ "JOIN j.fiscalYrModel f "
	    					+ "JOIN j.jvType jvt "
	    					+ "WHERE f.fyId=:fsYrIdPassed "
	    					+ "AND jvt.cvId=:jvTypePassed "
	    					+ "AND j.jvDateAd BETWEEN :frDt AND :toDt "
	    					+ "AND j.journalPk.jvNo=:jv_no")					
})
public class JournalVoucherModel implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String Find_Pending_JV = "JournalVoucherModel.findPendingJV";
	public static final String FIND_BY_JV_NO = "JournalVoucherModel.FIND_BY_JV_NO";
	public static final String FIND_UNAPPROVED_JV = "JournalVoucherModel.findUnApproved";
	public static final String FIND_BY_FS_YR = "JournalVoucherModel.findByFsYr";
	public static final String FIND_BY_JV_TYPE = "JournalVoucherModel.findByJvType";
	public static final String FIND_BY_JV_DATE="JournalVoucherModel.findByJvDt";
	public static final String FIND_BY_ACC_HEAD_ID = "JournalVoucherModel.findByAccHead";
	public static final String FIND_BY_FS_YR_JV_TYPE = "JournalVoucherModel.findByFsYrJvType";
	public static final String FIND_BY_FS_YR_JV_DATE = "JournalVoucherModel.findByFsYrJvDate";
	public static final String FIND_BY_FS_YR_JV_NO = "JournalVoucherModel.findByFsYrJvNo";
	public static final String FIND_BY_FS_YR_ACC_HEAD_ID="JournalVoucherModel.findByFsYrAccHeadId";
	public static final String FIND_BY_JV_TYPE_JV_DT="JournalVoucherModel.findByJvTypeJvDt";
	public static final String FIND_BY_JV_TYPE_JV_NO ="JournalVoucherModel.findByJvTypeJvNo";
	public static final String FIND_BY_JV_TYPE_ACC_HEAD = "JournalVoucherModel.findByJvTypeAccHead";
	public static final String FIND_BY_JV_DT_JV_NO="JournalVoucherModel.findByJvDtJvNo";
	public static final String FIND_BY_JV_DT_ACC_HEAD="JournalVoucherModel.findByJvDtAccHead";
	public static final String FIND_BY_JV_NO_ACC_HEAD ="JournalVoucherModel.findByJvNoAccHead";
	public static final String FIND_BY_FS_YR_JV_TYPE_JV_DT = "JournalVoucherModel.findByFsYrJvTypeJvDate";
	public static final String FIND_BY_JV_TYPE_JV_DT_JV_NO="JournalVoucherModel.findByJvTypeJvDtJvNo";
	public static final String FIND_BY_JV_DT_JV_NO_ACC_HEAD ="JournalVoucherModel.findByJvDtJvNoAcchead";
	public static final String FIND_BY_FS_YR_JV_TYPE_JV_DT_JV_NO = "JournalVoucherModel.findByFsYrJvTypeJvDtJvNo";
	
	/*@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="jvid",unique=true)
	private long jvId;*/
	@EmbeddedId 
	private JournalPk journalPk;
	
	@OneToOne
	@MapsId("fyId")
	@JoinColumn(name="fy_id", referencedColumnName="fy_id")
	private FiscalYrModel fiscalYrModel;
	
	@OneToOne
	@MapsId("jvType")
	@JoinColumn(name="jv_type",referencedColumnName="cv_id")
	private CodeValue jvType;
	

	@OneToMany(mappedBy = "jvm", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval=true)
	private List<JournalVoucherDetailModel> jvdmList;
	
	@OneToMany(mappedBy="journalVourcher", fetch=FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval=true)
	private List<LedgerMcg> ledgerList;
	
	@Column(name = "br_id")
	private int brId;

	@Column(name = "to_from_type",columnDefinition="Integer default '1'")
	private int toFromType;
	
	@Column(name = "jv_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date jvDateAd;

	@Column(name = "nepali_jv_date")
	private String jvDateBs;

	@Column(name = "narration", nullable = false, length = 500)
	private String narration;
	
	@Column(name = "receipt_no")
	private String receiptNo;
	
	@Column(name = "receipt_amt",columnDefinition="Decimal(28,2) default '0.00'")
	private double receiptAmt;
	
	@Column(name = "cheque_no")
	private String chequeNo;
	
	@ManyToOne
	@JoinColumn(name = "submitted_by", referencedColumnName="id")
	private User submittedBy;
	
	@ManyToOne
	@JoinColumn(name = "received_by", referencedColumnName="id")
	private User receivedBy;
	
	@ManyToOne
	@JoinColumn(name = "audited_by", referencedColumnName="id")
	private User auditedBy;
	
	@ManyToOne
	@JoinColumn(name = "approved_by", referencedColumnName="id")
	private User approvedBy;
	
	
	@ManyToOne
	@JoinColumn(name = "created_by", referencedColumnName="id")
	private User createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_date")
	private Date createdDate;
	
	@ManyToOne
	@JoinColumn(name = "updated_by", referencedColumnName="id")
	private User updatedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updatedDate")
	private Date updatedDate;

	@Column(name = "update_count")
	@Version
	private int updateCount;

	@ManyToOne
	@JoinColumn(name = "posted_by", referencedColumnName="id")
	private User postedBy;

	/*
	 * the status contain the value 1 for pending voucher 2 for approved voucher
	 * and 3 reject voucher
	 */
	@Column(name = "status")
	private int status;
	@Column(name = "rv_flag")
	private int rvFlag;
	/**
	 * @return the journalPk
	 */
	public JournalPk getJournalPk() {
		if(journalPk==null)
			journalPk=new JournalPk();
		return journalPk;
	}
	/**
	 * @param journalPk the journalPk to set
	 */
	public void setJournalPk(JournalPk journalPk) {
		this.journalPk = journalPk;
	}
	/**
	 * @return the jvId
	 */
	/*public long getJvId() {
		return jvId;
	}
	*//**
	 * @param jvId the jvId to set
	 *//*
	public void setJvId(long jvId) {
		this.jvId = jvId;
	}*/
	/**
	 * @return the jvNo
	 */
	public int getJvNo() {
		return getJournalPk().getJvNo();
	}
	/**
	 * @param jvNo the jvNo to set
	 */
	public void setJvNo(int jvNo) {
		this.getJournalPk().setJvNo(jvNo);
	}
	/**
	 * @return the fiscalYrModel
	 */
	public FiscalYrModel getFiscalYrModel() {
		return fiscalYrModel;
	}
	/**
	 * @param fiscalYrModel the fiscalYrModel to set
	 */
	public void setFiscalYrModel(FiscalYrModel fiscalYrModel) {
		this.fiscalYrModel = fiscalYrModel;
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
	/**
	 * @return the objJVDM
	 */
	public List<JournalVoucherDetailModel> getJvdmList() {
		return jvdmList;
	}
	/**
	 * @param objJvdmList the objJVDM to set
	 */
	public void setJvdmList(List<JournalVoucherDetailModel> objJvdmList) {
		this.jvdmList = objJvdmList;
	}
	/**
	 * @return the ledgerList
	 */
	public List<LedgerMcg> getLedgerList() {
		return ledgerList;
	}
	/**
	 * @param ledgerList the ledgerList to set
	 */
	public void setLedgerList(List<LedgerMcg> ledgerList) {
		this.ledgerList = ledgerList;
	}
	/**
	 * @return the brId
	 */
	public int getBrId() {
		return brId;
	}
	/**
	 * @param brId the brId to set
	 */
	public void setBrId(int brId) {
		this.brId = brId;
	}
	/**
	 * @return the toFromType
	 */
	public int getToFromType() {
		return toFromType;
	}
	/**
	 * @param toFromType the toFromType to set
	 */
	public void setToFromType(int toFromType) {
		this.toFromType = toFromType;
	}
	/**
	 * @return the jvDateAd
	 */
	public Date getJvDateAd() {
		return jvDateAd;
	}
	/**
	 * @param jvDateAd the jvDateAd to set
	 */
	public void setJvDateAd(Date jvDateAd) {
		this.jvDateAd = jvDateAd;
	}
	/**
	 * @return the jvDateBs
	 */
	public String getJvDateBs() {
		return jvDateBs;
	}
	/**
	 * @param jvDateBs the jvDateBs to set
	 */
	public void setJvDateBs(String jvDateBs) {
		this.jvDateBs = jvDateBs;
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
	/**
	 * @return the receiptNo
	 */
	public String getReceiptNo() {
		return receiptNo;
	}
	/**
	 * @param receiptNo the receiptNo to set
	 */
	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}
	/**
	 * @return the receiptAmt
	 */
	public double getReceiptAmt() {
		return receiptAmt;
	}
	/**
	 * @param receiptAmt the receiptAmt to set
	 */
	public void setReceiptAmt(double receiptAmt) {
		this.receiptAmt = receiptAmt;
	}
	/**
	 * @return the chequeNo
	 */
	public String getChequeNo() {
		return chequeNo;
	}
	/**
	 * @param chequeNo the chequeNo to set
	 */
	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}
	/**
	 * @return the submittedBy
	 */
	public User getSubmittedBy() {
		return submittedBy;
	}
	/**
	 * @param submittedBy the submittedBy to set
	 */
	public void setSubmittedBy(User submittedBy) {
		this.submittedBy = submittedBy;
	}
	/**
	 * @return the receivedBy
	 */
	public User getReceivedBy() {
		return receivedBy;
	}
	/**
	 * @param receivedBy the receivedBy to set
	 */
	public void setReceivedBy(User receivedBy) {
		this.receivedBy = receivedBy;
	}
	/**
	 * @return the auditedBy
	 */
	public User getAuditedBy() {
		return auditedBy;
	}
	/**
	 * @param auditedBy the auditedBy to set
	 */
	public void setAuditedBy(User auditedBy) {
		this.auditedBy = auditedBy;
	}
	/**
	 * @return the approvedBy
	 */
	public User getApprovedBy() {
		return approvedBy;
	}
	/**
	 * @param approvedBy the approvedBy to set
	 */
	public void setApprovedBy(User approvedBy) {
		this.approvedBy = approvedBy;
	}
	/**
	 * @return the createdBy
	 */
	public User getCreatedBy() {
		return createdBy;
	}
	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}
	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}
	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	/**
	 * @return the updatedBy
	 */
	public User getUpdatedBy() {
		return updatedBy;
	}
	/**
	 * @param updatedBy the updatedBy to set
	 */
	public void setUpdatedBy(User updatedBy) {
		this.updatedBy = updatedBy;
	}
	/**
	 * @return the updatedDate
	 */
	public Date getUpdatedDate() {
		return updatedDate;
	}
	/**
	 * @param updatedDate the updatedDate to set
	 */
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	/**
	 * @return the updateCount
	 */
	public int getUpdateCount() {
		return updateCount;
	}
	/**
	 * @param updateCount the updateCount to set
	 */
	public void setUpdateCount(int updateCount) {
		this.updateCount = updateCount;
	}
	/**
	 * @return the postedBy
	 */
	public User getPostedBy() {
		return postedBy;
	}
	/**
	 * @param postedBy the postedBy to set
	 */
	public void setPostedBy(User postedBy) {
		this.postedBy = postedBy;
	}
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	/**
	 * @return the rvFlag
	 */
	public int getRvFlag() {
		return rvFlag;
	}
	/**
	 * @param rvFlag the rvFlag to set
	 */
	public void setRvFlag(int rvFlag) {
		this.rvFlag = rvFlag;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
    public int hashCode() {
        return (int)getJvNo();
    }
	
 
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof JournalVoucherModel) {
        	JournalVoucherModel jvModel = (JournalVoucherModel) obj;
            return jvModel.getJvNo() == journalPk.getJvNo();
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
