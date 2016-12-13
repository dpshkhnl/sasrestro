package sasrestro.mb.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import sasrestro.misc.AbstractMB;
import sasrestro.model.user.AccessControlList;
import sasrestro.model.user.UserRole;
import sasrestro.sessionejb.user.AclEJB;
import sasrestro.sessionejb.user.RoleEJB;


@ViewScoped
@ManagedBean(name="hq")
public class AuthMB extends AbstractMB implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = UserMB.INJECTION_NAME)
	private UserMB userMB;
	
	private UserRole role;
	@SuppressWarnings("unused")
	private List<AccessControlList> aclCol;
	private List<String> aclIds;
	@SuppressWarnings("unused")
	private List<UserRole> roleList;
	private boolean newCheck;
	@EJB
	private RoleEJB roleEjb;
	@EJB
	private AclEJB aclEJB;

	/**
	 * @return the role
	 */
	public UserRole getRole() {
		if(role==null)
		{
			role=new UserRole();
		}
		return role;
	}
	/**
	 * @param role the role to set
	 */
	public void setRole(UserRole role) {
		this.role = role;
	}
	/**
	 * @return the acl
	 */
	public List<AccessControlList> getAclCol() {
		return aclCol=aclEJB.findAll();
	}
	/**
	 * @param acl the acl to set
	 */
	public void setAclCol(List<AccessControlList> acl) {
		this.aclCol = acl;
	}

	/**
	 * @return the aclIds
	 */
	public List<String> getAclIds() {
		return aclIds;
	}
	/**
	 * @param aclIds the aclIds to set
	 */
	public void setAclIds(List<String> aclIds) {
		this.aclIds = aclIds;
	}
	/**
	 * @return the roleList
	 */
	public List<UserRole> getRoleList() {
		return roleList=roleEjb.findAll();
	}
	/**
	 * @param roleList the roleList to set
	 */
	public void setRoleList(List<UserRole> roleList) {
		this.roleList = roleList;
	}
	/**
	 * @return the newCheck
	 */
	public boolean isNewCheck() {
		return newCheck;
	}
	/**
	 * @param newCheck the newCheck to set
	 */
	public void setNewCheck(boolean newCheck) {
		this.newCheck = newCheck;
	}

	public void saveRole()
	{
		
		List<AccessControlList> aclTemp=new ArrayList<AccessControlList>();
		for(String id: aclIds )
		{
			aclTemp.add(aclEJB.find(Integer.valueOf(id)));
		}
		role.setAccessControlList(aclTemp);
		if(role.getRoleId()>0)
		{
			roleEjb.update(role);
			//resetBack newCheck flag to default
			setNewCheck(false);
			resetAll();
			displayInfoMessageToUser("The Role has been updated!");
		}
		else
		{
			if(role.getRoleName()==null || role.getRoleName().trim().isEmpty())
			{
				displayErrorMessageToUser("The Role Name is blank!");
			}
			else
			{
				
					
				try
				{
					roleEjb.save(role);
					displayInfoMessageToUser("The Role has beens saved!");
					resetAll();
					//resetBack newCheck flag to default
					setNewCheck(false);
				}
				catch(Exception ex)
				{
					displayErrorMessageToUser("Duplicate Role Name");
					
				}
				
			}
		}
		
		
				
	}
	public void updateRole()
	{
		if(userMB.isLoggedIn())
		{
		List<AccessControlList> aclTemp=new ArrayList<AccessControlList>();
		for(String id: aclIds )
		{
			aclTemp.add(aclEJB.find(Integer.valueOf(id)));
		}
		role.setAccessControlList(aclTemp);
		roleEjb.update(role);
		displayInfoMessageToUser("The Role has beens updated!");
		}
		else
		{
			//setNewCheck(false);
			System.out.println("zz... unauthorized user's update method call...");
			displayErrorMessageToUser("Insufficient Privilege to perform this operation!");
		}
	}
	public void newRole()
	{
		//set the newCheck flag
		setNewCheck(true);
		//reset role model if contains anything before
		role=null;
	}
	public void resetAll()
	{
		setNewCheck(false);
		role=null;
		if(aclIds!=null)
			aclIds.clear();
	}
	public void onRoleSelect()
	{
		//aclCol=roleEjb.find(role.getRoleId()).getAccessControlList();
		//role=roleEjb.find(role.getRoleId());
		role=roleEjb.find(role.getRoleId());
		List<AccessControlList> aclTemp=role.getAccessControlList();
		List<String> list = new ArrayList<String>();
		for(AccessControlList acl:aclTemp)
		{
			list.add(String.valueOf(acl.getAclId()));
		}
		setAclIds(list);
		/*if(!aclTemp.isEmpty())
		{
			//reset back aclIds list
			if(getAclIds()!=null)
			{
				aclIds.clear();
			}
		}*/

	}
	public void setUserMB(UserMB userMB) {
		this.userMB = userMB;
	}

}
