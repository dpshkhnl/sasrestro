package sasrestro.model.user;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
@Table(name = "user_login_info")
@NamedQueries({
	@NamedQuery(name = "UserLoginInfo.GET_ACTIVE_SESSION_BY_USER", query = "select u from UserLoginInfo u where u.userId.id =:userId AND u.logoutTime IS NULL"),
	@NamedQuery(name = "UserLoginInfo.GET_ACTIVE_USER", query = "select u from UserLoginInfo u where  u.logoutTime IS NULL")})

public class UserLoginInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	public final static String GET_ACTIVE_SESSION_BY_USER = "UserLoginInfo.GET_ACTIVE_SESSION_BY_USER";
	public final static String GET_ACTIVE_USER = "UserLoginInfo.GET_ACTIVE_USER";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "login_info_id")
	private int loginInfoId;
	
	@OneToOne
	@JoinColumn (name = "user_id")
	private User userId;
	
	@Column (name = "login_time")
	@Temporal(TemporalType.TIMESTAMP)
	private  Date loginTime;
	
	@Column (name = "logout_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date logoutTime;
	
	@Column (name = "remarks")
	private String remarks;
	
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


	public int getLoginInfoId() {
		return loginInfoId;
	}


	public void setLoginInfoId(int loginInfoId) {
		this.loginInfoId = loginInfoId;
	}


	public User getUserId() {
		return userId;
	}


	public void setUserId(User userId) {
		this.userId = userId;
	}


	public Date getLoginTime() {
		return loginTime;
	}


	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}


	public Date getLogoutTime() {
		return logoutTime;
	}


	public void setLogoutTime(Date logoutTime) {
		this.logoutTime = logoutTime;
	}


	public String getRemarks() {
		return remarks;
	}


	public void setRemarks(String remarks) {
		this.remarks = remarks;
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


	public Date getUpdatedDateAD() {
		return updatedDateAD;
	}


	public void setUpdatedDateAD(Date updatedDateAD) {
		this.updatedDateAD = updatedDateAD;
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
	
	
}
