package sasrestro.model.user;
 
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import sasrestro.model.util.DayInOutStatusModel;
import sasrestro.model.util.FiscalYrModel;
 
/**
 * @author nebula
 *
 */

@Entity
@Table(name = "USERS")
@NamedQueries(
{@NamedQuery(name = "User.findUserByEmail", query = "select u from User u where u.email = :email "),
@NamedQuery(name="User.findUserByName",query="select u from User u where u.name=:name ")})
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
 
    public static final String FIND_BY_EMAIL = "User.findUserByEmail";
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
 
    @Column(unique = true)
    private String email;
    private String password;
    private String name;
    @OneToOne
    @JoinColumn(name="role_id")
    private UserRole role;
    
    @Column(name = "created_by")
	private int createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private int updatedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Version
	@Column(name = "update_count")
	private int updateCount;
	
	@Enumerated
	@Column(name="status")
	private UserStatus status;
	/*@Transient
	private String statusValue;*/
	
    // get and set
	@Transient
    private FiscalYrModel fyModel;

	@Transient
	private DayInOutStatusModel dayInStatus;
    // get and set
 
    public DayInOutStatusModel getDayInStatus() {
		return dayInStatus;
	}

	public void setDayInStatus(DayInOutStatusModel dayInStatus) {
		this.dayInStatus = dayInStatus;
	}

	/**
	 * @return the fyModel
	 */
	public FiscalYrModel getFyModel() {
		if (fyModel==null) {
			fyModel = new FiscalYrModel();
		}
		return fyModel;
	}

	/**
	 * @param fyModel the fyModel to set
	 */
	public void setFyModel(FiscalYrModel fyModel) {
		this.fyModel = fyModel;
	}
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UserRole getRole() {
		/*if(role==null)
			role=new UserRole();*/
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}
	
	/**
	 * @return the createdBy
	 */
	public int getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(int createdBy) {
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
	public int getUpdatedBy() {
		return updatedBy;
	}

	/**
	 * @param updatedBy the updatedBy to set
	 */
	public void setUpdatedBy(int updatedBy) {
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
	 * @return the status
	 */
	public UserStatus getStatus() {
		return status;
	}

	/**
	 * @return the statusValue
	 */
	/*public String getStatusValue() {
		return statusValue=getStatus()==1?"Active":"Inactive";
	}

	*//**
	 * @param statusValue the statusValue to set
	 *//*
	public void setStatusValue(String statusValue) {
		this.statusValue = statusValue;
	}*/

	/**
	 * @param status the status to set
	 */
	public void setStatus(UserStatus status) {
		this.status = status;
	}


	@PrePersist
    protected void onCreate() {
      createdDate = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
      updatedDate = new Date();
    }

 
    @Override
    public int hashCode() {
        return getId();
    }
 
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            User user = (User) obj;
            return user.getId() == id;
        }
 
        return false;
    }

	public boolean isAdmin() {
        return true;
    }
 
    public boolean isUser() {
        return false;
    }
}