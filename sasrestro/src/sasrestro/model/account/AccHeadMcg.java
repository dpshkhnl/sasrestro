package sasrestro.model.account;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

/**
 * @author nebula
 * 
 */

@Entity
@Table(name = "acc_head_mcg", uniqueConstraints = { @UniqueConstraint(columnNames = { "acc_code" }) })
@NamedQueries({
		@NamedQuery(name = "AccHeadMcg.findByAccHeadId", query = "SELECT a FROM AccHeadMcg a "
				+ "where a.accHeadId = :accHeadIdPassed"),
		@NamedQuery(name = "AccHeadMcg.findAllWithParentName", query = "SELECT a FROM AccHeadMcg a"),
		@NamedQuery(name = "AccHeadMcg.Get_Acc_Alias_On_Select", query = "SELECT a FROM AccHeadMcg a "
				+ "where a.accHeadId not in "
				+ "(SELECT DISTINCT(au.parent) FROM AccHeadMcg au) and a.accCode like :accCodeSelected"),
		@NamedQuery(name = "AccHeadMcg.GET_ALL_BY_ACC_CODE", query = "SELECT a FROM AccHeadMcg a "
				+ "where a.accCode=:accCode"),
		@NamedQuery(name = "AccHeadMcg.findBankAccounts", query = "SELECT DISTINCT a FROM AccHeadMap ah "
				+ "JOIN ah.accHeadModel a "
				+ "WHERE ah.mappingPurpose='Bank Account'"),
		@NamedQuery(name = "AccHeadMcg.findCheckDuplicateAccHeadName", query = "SELECT a FROM AccHeadMcg a "
				+ "WHERE a.accName=:accName"),
		@NamedQuery(name = "AccHeadMcg.listRoots", query = "SELECT a FROM AccHeadMcg a "
				+ "WHERE a.parent=0"),
		@NamedQuery(name = "AccHeadMcg.getChildren", query = "SELECT a FROM AccHeadMcg a WHERE a.parent=:parentPassed"),
		/*@NamedQuery(name = "AccHeadMcg.findAllNotRoot", query = "SELECT a FROM AccHeadMcg a WHERE trim(a.accCode) LIKE :acc_code_passed "
				+ "AND a.parent!=0 AND a.accHeadId NOT IN (SELECT aa.parent FROM AccHeadMcg aa WHERE aa.parent != 0)"),
		@NamedQuery(name = "AccHeadMcg.findAllNonRoot", query = "SELECT a FROM AccHeadMcg a "
				+ "WHERE a.parent!=0 AND a.accHeadId NOT IN (SELECT aa.parent FROM AccHeadMcg aa WHERE aa.parent != 0)"),*/
		@NamedQuery(name = "AccHeadMcg.findAllCashInHand", query = "SELECT DISTINCT a FROM AccHeadMap ah "
				+ "JOIN ah.accHeadModel a "
				+ "WHERE ah.mappingPurpose='Cash In Hand'"),
		@NamedQuery(name = "AccHeadMcg.listParentNodeOnly", query = "SELECT a FROM AccHeadMcg a "
				+ "WHERE a.accHeadId IN (SELECT DISTINCT(aa.parent) FROM AccHeadMcg aa) ORDER BY a.accCode"),
		@NamedQuery(name = "AccHeadMcg.listNonParentNodeOnly", query = "SELECT a FROM AccHeadMcg a WHERE a.parent = :passParent AND a.accHeadId "
				+ "NOT IN (SELECT DISTINCT(aa.parent) FROM AccHeadMcg aa)") })
@NamedNativeQueries({ @NamedNativeQuery(name = "UniqueAccCode.generateNewOnThisAccRoot", query = "select dbo.max_acc_code(:accRootCodePassed)") })
public class AccHeadMcg implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String FIND_BY_ACC_HEAD_ID = "AccHeadMcg.findByAccHeadId";
	public static final String GET_MAX_CODE = "UniqueAccCode.generateNewOnThisAccRoot";
	public static final String Get_Acc_Alias_On_Select = "AccHeadMcg.Get_Acc_Alias_On_Select";
	public static final String GET_ALL_BY_ACC_CODE = "AccHeadMcg.GET_ALL_BY_ACC_CODE";
	public static final String GET_ALL_BANK_ACCOUNTS = "AccHeadMcg.findBankAccounts";
	public static final String CHECK_DUPICATE_NAME = "AccHeadMcg.findCheckDuplicateAccHeadName";
	public static final String LIST_ROOT_NODES = "AccHeadMcg.listRoots";
	public static final String LIST_CHILDREN = "AccHeadMcg.getChildren";
	public static final String FIND_ALL_NON_ROOT = "AccHeadMcg.findAllNotRoot";
	public static final String FIND_ALL_NON_ROOT_LIST = "AccHeadMcg.findAllNonRoot";
	public static final String FIND_ALL_CASH_IN_HAND = "AccHeadMcg.findAllCashInHand";
	public static final String LIST_PARENT_NODE_ONLY = "AccHeadMcg.listParentNodeOnly";
	public static final String LIST_NON_PARENT_NODE_ONLY = "AccHeadMcg.listNonParentNodeOnly";
	/*
	 * When we allow the Hibernate to create the database table, the columns are
	 * created according to alphabetic order of column names defined in @Column
	 * in entity.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "acc_head_id")
	private int accHeadId;

	@Column(name = "br_id")
	private int brId;

	@Column(name = "acc_code", nullable = false)
	private String accCode;

	private String alias;

	@Column(name = "acc_name", nullable = false, unique = true)
	private String accName;

	@Column(name = "acc_type", nullable = false)
	private int accType;

	@Column(nullable = false)
	private int parent;

	@Column(name = "acc_level")
	private int accLevel;

	@Column(name = "min_bal", columnDefinition = "Decimal(28,2) default '0.00'")
	private double minBal;

	private String remarks;

	@Column(name = "created_by")
	private int createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_date")
	private java.util.Date createdDate;

	@Column(name = "updated_by")
	private int updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "update_count")
	private int updateCount;

	private String drcr;

	@Column(name = "jv_status", nullable = false)
	private int jvStatus;

	@Transient
	private String parentName;

	@OneToOne(mappedBy = "accountHead", cascade = CascadeType.ALL)
	private LedgerMcg ledgerMcg;

	public AccHeadMcg() {
	}

	public LedgerMcg getLedger() {
		return ledgerMcg;
	}

	public void setLedger(LedgerMcg ledger) {
		this.ledgerMcg = ledger;
	}

	public int getAccHeadId() {
		return accHeadId;
	}

	public void setAccHeadId(int accHeadId) {
		this.accHeadId = accHeadId;
	}

	public int getBrId() {
		return brId;
	}

	public void setBrId(int brId) {
		this.brId = brId;
	}

	public String getAccCode() {
		return accCode;
	}

	public void setAccCode(String accCode) {
		this.accCode = accCode;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getAccName() {
		return accName;
	}

	public void setAccName(String accName) {
		this.accName = accName;
	}

	public int getAccType() {
		return accType;
	}

	public void setAccType(int accType) {
		this.accType = accType;
	}

	public int getParent() {
		return parent;
	}

	public void setParent(int parent) {
		this.parent = parent;
	}

	public int getAccLevel() {
		return accLevel;
	}

	public void setAccLevel(int accLevel) {
		this.accLevel = accLevel;
	}

	public double getMinBal() {
		return minBal;
	}

	public void setMinBal(double minBal) {
		this.minBal = minBal;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public int getUpdateCount() {
		return updateCount;
	}

	public void setUpdateCount(int updateCount) {
		this.updateCount = updateCount;
	}

	public String getDrcr() {
		return drcr;
	}

	public void setDrcr(String drcr) {
		this.drcr = drcr;
	}

	public int getJvStatus() {
		return jvStatus = 1; // Since Summation is true for all account heads by
								// default.
	}

	public void setJvStatus(int jvStatus) {
		this.jvStatus = jvStatus;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentNamePassed) {
		this.parentName = parentNamePassed;
	}

	@Override
	public int hashCode() {
		return getAccHeadId();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AccHeadMcg) {
			AccHeadMcg accHeadMcg = (AccHeadMcg) obj;
			return accHeadMcg.getAccHeadId() == accHeadId;
		}

		return false;
	}

	@PrePersist
	protected void onCreate() {
		createdDate = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		updatedDate = new Date();
	}
}
