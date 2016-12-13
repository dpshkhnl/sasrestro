package sasrestro.model.util;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "fiscal_yr")

@NamedQueries({
	@NamedQuery(name = "FiscalYrModel.findAll", query = "select f from FiscalYrModel f"),
	@NamedQuery(name = "FiscalYrModel.findActiveFsYr", query = "SELECT f FROM FiscalYrModel f WHERE f.status = '1'")
})

public class FiscalYrModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final static String FIND_ALL_FISCAL_YR = "FiscalYrModel.findAll";
	public final static String FIND_ACTIVE_FISCAL_YR = "FiscalYrModel.findActiveFsYr";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "fy_id")
	private int fyId;
	
	@Column(name = "fiscal_year")
	private String fiscalYr;
	
	@Column(name = "start_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;
	
	@Column(name = "end_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;
	
	@Column(name = "status")
	private String status;

	public int getFyId() {
		return fyId;
	}

	public void setFyId(int fyId) {
		this.fyId = fyId;
	}

	public String getFiscalYr() {
		return fiscalYr;
	}

	public void setFiscalYr(String fiscalYr) {
		this.fiscalYr = fiscalYr;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result
				+ ((fiscalYr == null) ? 0 : fiscalYr.hashCode());
		result = prime * result + fyId;
		result = prime * result
				+ ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FiscalYrModel other = (FiscalYrModel) obj;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (fiscalYr == null) {
			if (other.fiscalYr != null)
				return false;
		} else if (!fiscalYr.equals(other.fiscalYr))
			return false;
		if (fyId != other.fyId)
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}
	
	
	

}
