package sasrestro.model.account;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import sasrestro.model.util.FiscalYrModel;

@Entity
@Table(name = "budget")
@NamedQueries({
		@NamedQuery(name = "BudgetModel.findByFsYr", query = "SELECT b FROM BudgetModel b "
				+ "WHERE b.fsYr = :fsIdPassed"),
		@NamedQuery(name = "BudgetModel.findByFsYrAndAccType", query = "SELECT b FROM BudgetModel b "
				+ "WHERE b.fsYr.fyId = :fyIdPassed AND b.accType.cvId = :accTypePassed "
				+ "ORDER BY b.accHead.accName") })
public class BudgetModel implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String FIND_BUDGET_BY_FY_ID = "BudgetModel.findByFsYr";
	public static final String FIND_BUDGET_BY_FY_ID_ACC_TYPE = "BudgetModel.findByFsYrAndAccType";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "budget_id")
	private int budgetId;

	@ManyToOne
	@JoinColumn(name = "fy_id", referencedColumnName = "fy_id")
	private FiscalYrModel fsYr;

	@ManyToOne
	@JoinColumn(name = "acc_type", referencedColumnName = "cv_id")
	private CodeValue accType;

	@ManyToOne
	@JoinColumn(name = "acc_head_id", referencedColumnName = "acc_head_id")
	private AccHeadMcg accHead;

	@Column(name = "budget_amt")
	private Double budgetAmt;

	@Column(name = "closing_amt")
	private Double closingAmt;

	/**
	 * @return the budgetId
	 */
	public int getBudgetId() {
		return budgetId;
	}

	/**
	 * @param budgetId
	 *            the budgetId to set
	 */
	public void setBudgetId(int budgetId) {
		this.budgetId = budgetId;
	}

	/**
	 * @return the fsYr
	 */
	public FiscalYrModel getFsYr() {
		return fsYr;
	}

	/**
	 * @param fsYr
	 *            the fsYr to set
	 */
	public void setFsYr(FiscalYrModel fsYr) {
		this.fsYr = fsYr;
	}

	public CodeValue getAccType() {
		return accType;
	}

	public void setAccType(CodeValue accType) {
		this.accType = accType;
	}

	/**
	 * @return the accHead
	 */
	public AccHeadMcg getAccHead() {
		return accHead;
	}

	/**
	 * @param accHead
	 *            the accHead to set
	 */
	public void setAccHead(AccHeadMcg accHead) {
		this.accHead = accHead;
	}

	/**
	 * @return the budgetAmt
	 */
	public Double getBudgetAmt() {
		return budgetAmt;
	}

	/**
	 * @param budgetAmt
	 *            the budgetAmt to set
	 */
	public void setBudgetAmt(Double budgetAmt) {
		this.budgetAmt = budgetAmt;
	}

	/**
	 * @return the closingAmt
	 */
	public Double getClosingAmt() {
		return closingAmt;
	}

	/**
	 * @param closingAmt
	 *            the closingAmt to set
	 */
	public void setClosingAmt(Double closingAmt) {
		this.closingAmt = closingAmt;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accHead == null) ? 0 : accHead.hashCode());
		result = prime * result
				+ ((budgetAmt == null) ? 0 : budgetAmt.hashCode());
		result = prime * result + budgetId;
		result = prime * result
				+ ((closingAmt == null) ? 0 : closingAmt.hashCode());
		result = prime * result + ((fsYr == null) ? 0 : fsYr.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BudgetModel other = (BudgetModel) obj;
		if (accHead == null) {
			if (other.accHead != null)
				return false;
		} else if (!accHead.equals(other.accHead))
			return false;
		if (budgetAmt == null) {
			if (other.budgetAmt != null)
				return false;
		} else if (!budgetAmt.equals(other.budgetAmt))
			return false;
		if (budgetId != other.budgetId)
			return false;
		if (closingAmt == null) {
			if (other.closingAmt != null)
				return false;
		} else if (!closingAmt.equals(other.closingAmt))
			return false;
		if (fsYr == null) {
			if (other.fsYr != null)
				return false;
		} else if (!fsYr.equals(other.fsYr))
			return false;
		return true;
	}

}
