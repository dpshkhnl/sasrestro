package sasrestro.model.user;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;



@Entity
@Table(name = "role")
public class UserRole implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="role_id")
	private int roleId;

	@Column(unique = true)
	private String roleName;
	
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="role_acl",    joinColumns=
    @JoinColumn(name="role_id", referencedColumnName="role_id"),
    inverseJoinColumns= @JoinColumn(name="acl_id", referencedColumnName="acl_id")
)
	private List<AccessControlList> accessControlList;
	
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

	/**
	 * @return the roleId
	 */
	public int getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	/**
	 * @return the roleName
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * @param roleName the roleName to set
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * @return the accessControlList
	 */
	public List<AccessControlList> getAccessControlList() {
		return accessControlList;
	}

	/**
	 * @param accessControlList the accessControlList to set
	 */
	public void setAccessControlList(List<AccessControlList> accessControlList) {
		this.accessControlList = accessControlList;
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
	
	@PrePersist
    protected void onCreate() {
      createdDate = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
      updatedDate = new Date();
    }
	
	
}
