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
import javax.persistence.Version;

@Entity
@Table (name= "day_in_out_status")

@NamedQueries({
	@NamedQuery(name = "DayInOutStatusModel.findAll", query = "select d from DayInOutStatusModel d"),
	@NamedQuery(name = "DayInOutStatusModel.findActiveWorkDay", query = "SELECT d FROM DayInOutStatusModel d WHERE d.status = 0"),
	@NamedQuery(name = "DayInOutStatusModel.findPreviousWorkDay", query = "SELECT di FROM DayInOutStatusModel di ORDER BY di.dayInOutId  DESC")
})
public class DayInOutStatusModel implements Serializable{

	private static final long serialVersionUID = 1L;
	public final static String FIND_ALL = "DayInOutStatusModel.findAll";
	public final static String FIND_ACTIVE_WORKING_DATE = "DayInOutStatusModel.findActiveWorkDay";
	public final static String FIND_PREVIOUS_WORKING_DATE = "DayInOutStatusModel.findPreviousWorkDay";
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "day_in_out_id")
	private int dayInOutId;
	
	@Column (name = "trans_date_ad")
	@Temporal(TemporalType.TIMESTAMP)
	private Date transDateEn;
	
	@Column (name = "trans_date_bs")
	private String transDateNp;
	
	@Column (name = "day_in_date_ad")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dayInDateEn;
	
	@Column (name = "day_in_date_bs")
	private String dayInDateNp;
	
	@Column (name = "day_out_date_ad")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dayOutDateEn;
	
	@Column (name = "day_out_date_bs")
	private String dayOutDateNp;
	
	
	@Column (name = "status")
	private int status ;
	
	@Column(name = "created_date_ad")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDateAD;
	
	@Column(name = "created_by")
	private int createdBy;
	

	@Column(name = "updated_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDateAD;
	
	@Column(name = "updated_by")
	private int updatedBy;
	
	
	@Column(name = "update_count")
	@Version
	private int updateCount;
	
	public int getDayInOutId() {
		return dayInOutId;
	}

	public void setDayInOutId(int dayInOutId) {
		this.dayInOutId = dayInOutId;
	}

	public Date getTransDateEn() {
		return transDateEn;
	}

	public void setTransDateEn(Date transDateEn) {
		this.transDateEn = transDateEn;
	}

	public String getTransDateNp() {
		return transDateNp;
	}

	public void setTransDateNp(String transDateNp) {
		this.transDateNp = transDateNp;
	}

	public Date getDayInDateEn() {
		return dayInDateEn;
	}

	public void setDayInDateEn(Date dayInDateEn) {
		this.dayInDateEn = dayInDateEn;
	}

	public String getDayInDateNp() {
		return dayInDateNp;
	}

	public void setDayInDateNp(String dayInDateNp) {
		this.dayInDateNp = dayInDateNp;
	}

	public Date getDayOutDateEn() {
		return dayOutDateEn;
	}

	public void setDayOutDateEn(Date dayOutDateEn) {
		this.dayOutDateEn = dayOutDateEn;
	}

	public String getDayOutDateNp() {
		return dayOutDateNp;
	}

	public void setDayOutDateNp(String dayOutDateNp) {
		this.dayOutDateNp = dayOutDateNp;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getCreatedDateAD() {
		return createdDateAD;
	}

	public void setCreatedDateAD(Date createdDateAD) {
		this.createdDateAD = createdDateAD;
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

	public Date getUpdatedDateAD() {
		return updatedDateAD;
	}

	public void setUpdatedDateAD(Date updatedDateAD) {
		this.updatedDateAD = updatedDateAD;
	}

	public int getUpdateCount() {
		return updateCount;
	}

	public void setUpdateCount(int updateCount) {
		this.updateCount = updateCount;
	}


	
	
}
