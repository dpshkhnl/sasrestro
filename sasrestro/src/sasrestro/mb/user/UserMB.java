package sasrestro.mb.user;

import java.io.IOException;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.primefaces.event.SelectEvent;

import sasrestro.misc.SaltedPBKDFHash;
import sasrestro.model.user.AccessControlList;
import sasrestro.model.user.User;
import sasrestro.model.user.UserLoginInfo;
import sasrestro.model.user.UserRole;
import sasrestro.model.user.UserStatus;
import sasrestro.sessionejb.user.LoginInfoEJB;
import sasrestro.sessionejb.user.UserEJB;
import sasrestro.sessionejb.util.FiscalYrEJB;
 
/**
 * @author nebula
 *
 */
@SessionScoped
@ManagedBean(name="userMB")
public class UserMB implements Serializable{
    public static final String INJECTION_NAME = "#{userMB}";
    private static final long serialVersionUID = 1L;
 
    @EJB
    private UserEJB userEJB;
    
    @EJB
	private LoginInfoEJB loginInfoEJB;
    
    private User user;
    private User selectedUser;
    private User newUser;
    private boolean userSelection = false;
	public User getNewUser() {
		if (newUser == null)
			newUser = new User();
		
    	if (newUser.getFyModel() == null) {
    		newUser.setFyModel(fsYrEJB.getActiveFsYr());
		}
    	if (newUser.getRole() == null) {
    		newUser.setRole(new UserRole());
		}
    	
		return newUser;
	}
	public void setNewUser(User newUser) {
		this.newUser = newUser;
	}

	private String oldPassword;
    public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	
	//private LoginMB loginMB; 
	private List<User> users;
    private List<User> filteredUsers;
    private List<String> userAcl;
     boolean enableAdmin ,enableAccountModule, enableFeeModule, enableStudentModule,enableTrainingModule,enableLibraryModule,enableHRModule,
    		enableMemberModule,enableInventoryModule,enableExamModule,enableFinalExamModule;
    boolean enableAdminManagement,enableAdminInventory,enableAdminReports;
    boolean enableAccountHead,enableJournalVoucher,enableReceipt,enableFormCode,enableTrailBalance,enableBalanceSheet,enableProfitLoss,enableAccHeadMap;
    @SuppressWarnings("unused")
	private boolean isLoggedIn ,enableIndex,enableDepartment,enableRoleManagement,
	redirectToLogin,enableUserManagement,enableOfficeTimeInfo,
	enableCbcNameEntry,enableCbcEntry,enableCbcPositionEntry,enableCbcMemberEntry,enableCbcMeetAndAll,
	enableEmployeeDesignation,enableEmployeeLeaveInfo,enableEmployeeLeaveTypes,enableEmployeeManagement,enableEmployeeRegistration,
	enableEmployeeOvertimeInfo,enableEmploymentHistoryInfo,enableTrainingDeportationInfo,enableEmployeeAttendanceInfo,
	enableInstitutionRegistration,enableFeeScheme,enableFeePaymentList,
	enableLibrarySetting,enableAuthorManagement,enablePublisherManagement,enableBookCategory,enableBookManagementForm,
	enableMemberManagementForm,enableBookIssueListForm,
	enableStudentList,enableArticleShip;
    
    boolean isEnableBrandName, enableSupplier,isEnableSupplierInformation,isEnableHandlingCost, enablefixedAssest, enableInventoryStock;
	@EJB
    UserEJB userSessionEJB;
	
	@EJB
	private FiscalYrEJB fsYrEJB;
    
    private Map<String,String> roles=new HashMap<String, String>();
    
    
    public boolean isLoggedIn() {
		return isLoggedIn;
	}
	/**
	 * @param isLoggedIn the isLoggedIn to set
	 */
	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}
    /**
   	 * @return the filteredUsers
   	 */
   	public List<User> getFilteredUsers() {
   		return filteredUsers;
   	}

   	/**
   	 * @param filteredUsers the filteredUsers to set
   	 */
   	public void setFilteredUsers(List<User> filteredUsers) {
   		this.filteredUsers = filteredUsers;
   	}
   	
    public User getSelectedUser() {
    	if (selectedUser == null)
    		selectedUser = new User();
    	if(selectedUser.getRole()== null)
    	selectedUser.setRole( new UserRole());
    	
  		return selectedUser;
  	}
  	public void setSelectedUser(User selectedUser) {
  		this.selectedUser = selectedUser;
  	}
    
	public Map<String, String> getRoles() {
		return roles;
	}

	public void setRoles(Map<String, String> roles) {
		this.roles = roles;
	}

    
    public List<User> getAllUsers() {
        if (users == null) {
            loadUsers();
        }
 
        return users;
    }
 
	 
    
    private void loadUsers() {
        users = userSessionEJB.findAll();
    }
 
    public void resetUser() {
       newUser = null;
       selectedUser = null;
       userSelection = false;
       
    }
    
    public List<UserStatus> getStatusList()
	{
		return Arrays.asList(UserStatus.values());
		
	}
    /*public UserFacade getUserFacade() {
        if (userFacade == null) {
            userFacade = new UserFacade();
        }
 
        return userFacade;
    }*/
    public void onRowSelect(SelectEvent event){
		//FacesMessage msg = new FacesMessage("Employee Selected",((Employee) event.getObject()).getLastName());
		//FacesContext.getCurrentInstance().addMessage(null, msg);
    	selectedUser=null;
    	
		if(selectedUser==null)
		{
			selectedUser = (User)event.getObject();
			if (selectedUser.getStatus() == UserStatus.Deleted)
			{
				 resetUser();
				 return;
			}
			userSelection = true;
		}
		System.out.println("OnRowSelect has selectedUser name : "+ selectedUser.getName()+" and email : "+ selectedUser.getEmail()+" and role ="
		+ selectedUser.getRole().getRoleId() +"and Password : "+ selectedUser.getPassword());

	}
	
    public void createUser() {
        try {
        	System.out.println("Created by ---"+user.getName());
        	
        	if (!(userEJB.findUserByEmail(newUser.getEmail()) == null))
        	{
        		 FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
     					"Duplicate Email", "");
     			FacesContext.getCurrentInstance().addMessage(null, message);
     			return;
        	}
        	
        	newUser.setCreatedBy(user.getId());
        	newUser.setPassword(SaltedPBKDFHash.createHash(newUser.getPassword()));
        	userSessionEJB.save(newUser);
        	
            loadUsers();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Create Successful", "User Created Successfully.");
			FacesContext.getCurrentInstance().addMessage(null, message);
            resetUser();
        } catch (Exception e) {
        	 FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
 					"Error", "Error");
 			FacesContext.getCurrentInstance().addMessage(null, message);
            e.printStackTrace();
        }
    }
 
    public void updateUser() {
        try {
        	selectedUser.setUpdatedBy(user.getId());
        	
        //	userSessionEJB.update(selectedUser);

        	System.out.println("Updated by ---"+user.getName());
        	selectedUser.setUpdatedDate(new Date());
        	userSessionEJB.update(selectedUser);
        	loadUsers();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Update Successful", "User Updated Successfully.");
			FacesContext.getCurrentInstance().addMessage(null, message);
            resetUser();
            
        } catch (Exception e) {
        	 FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
  					"Error", "Error");
  			FacesContext.getCurrentInstance().addMessage(null, message);
             e.printStackTrace();
        }
    }
    
    public void resetPassword(){
    	try {
        	//selectedUser.setUpdatedBy(user.getId());
        	//selectedUser.setUpdatedDate(new Date());
        //	userSessionEJB.update(selectedUser);
    		selectedUser.setUpdatedBy(getUser().getId());
    		selectedUser.setUpdatedDate(new Date());
        	selectedUser.setPassword(SaltedPBKDFHash.createHash(selectedUser.getPassword()));
        	userSessionEJB.update(selectedUser);
        	loadUsers();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Update Successful", "User Updated Successfully.");
			FacesContext.getCurrentInstance().addMessage(null, message);
            resetUser();
            
        } catch (Exception e) {
        	 FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
  					"Error", "Error");
  			FacesContext.getCurrentInstance().addMessage(null, message);
             e.printStackTrace();
        }
    }
 
    public void deleteUser() {
    	 try {
         	selectedUser.setUpdatedBy(user.getId());
         	
         //	userSessionEJB.update(selectedUser);

         	System.out.println("Deleted by ---"+user.getName());
         	selectedUser.setStatus(UserStatus.Deleted);
         	selectedUser.setUpdatedDate(new Date());
         	userSessionEJB.update(selectedUser);
         	loadUsers();
             FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
 					"Delete Successful", "User Deleted Successfully.");
 			FacesContext.getCurrentInstance().addMessage(null, message);
             resetUser();
             
         } catch (Exception e) {
         	 FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
   					"Error", "Error");
   			FacesContext.getCurrentInstance().addMessage(null, message);
              e.printStackTrace();
         }
    }
 

    
    public boolean isAdmin() {
        return true;/*user.isAdmin();*/
    }
 
    public boolean isDefaultUser() {
        return true;
    }
 
    public String logOut() {
        
    	
    	FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		ServletContext servletContext = request.getSession().getServletContext();
		
		servletContext.removeAttribute("user-"+user.getId());
        
		UserLoginInfo loginInfo = new UserLoginInfo();
		loginInfo = loginInfoEJB.getActiveSessionByUser(getUser().getId());
		loginInfo.setLogoutTime(new Date());
		loginInfo.setUpdatedBy(user.getId());
		loginInfo.setUpdatedDateAD(new Date());
		loginInfo.setRemarks("Normal logout");
		loginInfoEJB.update(loginInfo);
		getRequest().getSession().invalidate();
        return "/pages/public/login.xhtml?faces-redirect=true";
    }
 
    private HttpServletRequest getRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }
 
    public User getUser() {
    	if (user == null) {
			user = new User();
    	}
    	
    	if (user.getFyModel() == null) {
			user.setFyModel(fsYrEJB.getActiveFsYr());
		}
    	if (user.getRole() == null) {
			user.setRole(new UserRole());
		}
    	
        return user;
    }
 
    public void setUser(User user) {
    	
    	this.user = user;
    	enableModules();
    }
    public void changePassword() throws NoSuchAlgorithmException, InvalidKeySpecException{
    	
    	User currentUser = userEJB.isValidLogin(user.getEmail(), oldPassword);
    	if(currentUser != null)
    	{
    		
    		userSessionEJB.updateUser(user);
    		
    		
    		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Update Successful", "Password Updated Successfully.");
			FacesContext.getCurrentInstance().addMessage(null, message);
			
    	
    	}
    	else
    	{
    		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Wrong credentials", "Check your old Password");
			FacesContext.getCurrentInstance().addMessage(null, message);
    	}
    }
   
    public void PDF(ActionEvent e) throws JRException, IOException {
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(getAllUsers());
        //String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("userdemo.jasper");
        String  reportPath=  FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/userReport.jasper");        
        JasperPrint jasperPrint=JasperFillManager.fillReport(reportPath, new HashMap<String,Object>(),beanCollectionDataSource);  
        
        HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        httpServletResponse.setContentType("application / pdf");
        httpServletResponse.addHeader("Content-disposition", "inline; filename=UserReport.pdf");
        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
        /*JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);*/
        /*FacesContext.getCurrentInstance().renderResponse();
        JasperViewer.viewReport(jasperPrint);*/
        servletOutputStream.write(JasperExportManager.exportReportToPdf(jasperPrint));
        servletOutputStream.flush();
        servletOutputStream.close();
        FacesContext.getCurrentInstance().renderResponse();
        FacesContext.getCurrentInstance().responseComplete();
        
       /* HttpServletResponse httpServletResponse=(HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();  
        httpServletResponse.addHeader("Content-disposition", "attachment; filename=ICANERP_USER_LIST.pdf");  
         ServletOutputStream servletOutputStream=httpServletResponse.getOutputStream();  
         JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);  
         FacesContext.getCurrentInstance().responseComplete();  */
        
        /*JasperRunManager.runReportToPdfFile("userdemo.jasper", "userdemo.pdf", new HashMap(), beanCollectionDataSource);*/
       /* FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletResponse response = (HttpServletResponse) externalContext
                .getResponse();

        byte[] report = JasperRunManager.runReportToPdf(jasperReport, new HashMap(), beanCollectionDataSource);

        response.setContentType("application/pdf");
        response.setContentLength(report.length);
        response.setHeader("Content-disposition", "inline; filename=report.pdf" );

        ServletOutputStream servletOutputStream = response.getOutputStream();
        servletOutputStream.write(report);
        facesContext.renderResponse();
        servletOutputStream.flush();
        servletOutputStream.close();
        facesContext.responseComplete();*/
        
        
        //return "next_page";
    }
    private void enableModules()
	{
		try
		{
			System.out.print("check roleid::" + getUser().getRole().getRoleId());
			List<AccessControlList> aclList=getUser().getRole().getAccessControlList();
			userAcl=new ArrayList<String>();
			for(AccessControlList acl:aclList)
			{
				
				userAcl.add(acl.getViewName());
			}
		}
		catch(Exception ex)
		{
			System.out.println("The error is :"+ex.getMessage());
			//setRedirectToLogin(true);			
		}
	}
    // for menu item based previlige
    public boolean isEnableAdmin(){
    	
		if (userAcl.contains("Adminprevilige"))
    		return enableAdmin = true;
    	else 
    	{
    		return enableAdmin = false;
    	}
    }
    public boolean  isEnableAccount(){
    	
		if (userAcl.contains("AccountPrevilige"))
    		return enableAccountModule = true;
    	else 
    		return enableAccountModule = false;
    }
    public boolean isEnablefeeModule(){
    	if (userAcl.contains("feePrevilige"))
    		return enableFeeModule = true;
    	else 
    		return enableFeeModule = false;
    }
    public boolean isEnableStudentModule(){
    	if (userAcl.contains("StudentPrevilige"))
    		return enableStudentModule = true;
    	else 
    		return enableStudentModule = false;
    }
    public boolean isEnableTrainingModule(){
    	if (userAcl.contains("TrainingPrevilige"))
    		return enableTrainingModule = true;
    	else 
    		return enableTrainingModule = false;
    }
    public boolean isEnableLibraryModule(){
    	if (userAcl.contains("LibraryPrevilige"))
    		return enableLibraryModule= true;
    	else 
    		return enableLibraryModule= false;
    }
    public boolean isEnableHRModule(){
    	if (userAcl.contains("HRModulePrevilige"))
    		return enableHRModule = true;
    	else 
    		return enableHRModule = false;
    }
    public boolean isEnableMemberModule(){
    	if (userAcl.contains("MemberPrevilige"))
    		return enableMemberModule = true;
    	else 
    		return enableMemberModule = false;
    }
    public boolean isEnableInventoryModule(){
    	if (userAcl.contains("InventoryPrevilige"))
    		return enableInventoryModule = true;
    	else 
    		return enableInventoryModule = false;
    }
    public boolean isEnableExamModule(){
    	if (userAcl.contains("ExamPrevilige"))
    		return enableExamModule = true;
    	else 
    		return enableExamModule = false;
    }
   
    public boolean isEnableFinalExamModule(){
    	if (userAcl.contains("finalExamModulePrevilige"))
    		return enableFinalExamModule = true;
    	else 
    		return enableFinalExamModule = false;
    }
    
    
    /*
     * Role management for Admin menu item
     */
    public boolean isEnableAdminManagement(){
    	
		if (userAcl.contains("AdminManagement"))
    		return enableAdminManagement = true;
    	else
    		return enableAdminManagement = false; 
    }
    
    public boolean isEnableAdminInventory(){
    	if(userAcl.contains("AdminInventory"))
    		return enableAdminInventory = true;
    	else
    		return enableAdminInventory = false;
    }
    
    public boolean isEnableAdminReports(){
    	if(userAcl.contains("AdminReports"))
    		return enableAdminReports = true;
    	else
    		return enableAdminReports = false;
    }
	/**
	 * @return the enableRoleManagement
	 */
	public boolean isEnableRoleManagement() {
		if(userAcl.contains("rolemanagement"))
			return enableRoleManagement=true;
		else
			return enableRoleManagement=false;
	}
	/**
	 * @return the enableUserManagement
	 */
	public boolean isEnableUserManagement() {
		if(userAcl.contains("usermanagement"))
			return enableUserManagement=true;
		else
			return enableUserManagement=false;
	}
    /*
     * Role Management for Account Menu Item
     */
    
    
    public boolean isEnableIndex() {
		if(userAcl.contains("index"))
			return enableIndex=true;
		else
			return enableIndex=false;
	}
    
	
	/**
	 * @return the enableDepartment
	 */
	public boolean isEnableDepartment() {
		if(userAcl.contains("department"))
			return enableDepartment=true;
		else
			return enableDepartment=false;
	}
	/**
	 * @return the enableAccountHead
	 */
	public boolean isEnableAccountHead() {
		if(userAcl.contains("accounthead"))
			return enableAccountHead=true;
		else
			return enableAccountHead=false;
	}

	/**
	 * @return the enableJournalVoucher
	 */
	public boolean isEnableJournalVoucher() {
		if(userAcl.contains("journalVoucherDetails"))
			return enableJournalVoucher=true;
		else
			return enableJournalVoucher=false;
	}
	/**
	 * @return the enableAccHeadMap
	 */
	public boolean isEnableAccHeadMap() {
		if(userAcl.contains("accHeadMap"))
			return enableAccHeadMap=true;
		else
			return enableAccHeadMap=false;
	}
	/**
	 * @return the enableFormCode
	 */
	public boolean isEnableFormCode() {
		if(userAcl.contains("formCode"))
			return enableFormCode=true;
		else
			return enableFormCode=false;
	}
	/**
	 * @return the enableOfficeTimeInfo
	 */
	public boolean isEnableOfficeTimeInfo() {
		if(userAcl.contains("officeTimeInfo"))
			return enableOfficeTimeInfo=true;
		else
			return enableOfficeTimeInfo=false;
	}
	/**
	 * @return the enableCbcNameEntry
	 */
	public boolean isEnableCbcNameEntry() {
		if(userAcl.contains("committeeNameEntry"))
			return enableCbcNameEntry=true;
		else
			return enableCbcNameEntry=false;
	}
	/**
	 * @return the enableCbcEntry
	 */
	public boolean isEnableCbcEntry() {
		if(userAcl.contains("committeeEntry"))
			return enableCbcEntry=true;
		else
			return enableCbcEntry=false;
	}
	/**
	 * @return the enableCbcPositionEntry
	 */
	public boolean isEnableCbcPositionEntry() {
		if(userAcl.contains("committeePositionEntry"))
			return enableCbcPositionEntry=true;
		else
			return enableCbcPositionEntry=false;
	}
	/**
	 * @return the enableCbcMemberEntry
	 */
	public boolean isEnableCbcMemberEntry() {
		if(userAcl.contains("committeeMemberEntry"))
			return enableCbcMemberEntry=true;
		else
			return enableCbcMemberEntry=false;
	}
	/**
	 * @return the enableCbcMeetAndAll
	 */
	public boolean isEnableCbcMeetAndAll() {
		if(userAcl.contains("committeeMeetingAndAllow\\Info"))
			return enableCbcMeetAndAll=true;
		else
			return enableCbcMeetAndAll=false;
	}
	/**
	 * @return the enableEmployeeDesignation
	 */
	public boolean isEnableEmployeeDesignation() {
		if(userAcl.contains("EmployeeDesignation"))
			return enableEmployeeDesignation=true;
		else
			return enableEmployeeDesignation=false;
	}
	/**
	 * @return the enableEmployeeLeaveInfo
	 */
	public boolean isEnableEmployeeLeaveInfo() {
		if(userAcl.contains("EmployeeLeaveInfo"))
			return enableEmployeeLeaveInfo=true;
		else
			return enableEmployeeLeaveInfo=false;
	}
	/**
	 * @return the enableEmployeLeaveTypes
	 */
	public boolean isEnableEmployeeLeaveTypes() {
		if(userAcl.contains("EmployeeLeaveTypes"))
			return enableEmployeeLeaveTypes=true;
		else
			return enableEmployeeLeaveTypes=false;
	}
	/**
	 * @return the enableEmployeeManagement
	 */
	public boolean isEnableEmployeeManagement() {
		if(userAcl.contains("EmployeeManagement"))
			return enableEmployeeManagement=true;
		else
			return enableEmployeeManagement=false;
	}
	/**
	 * @return the enableEmployeeRegistration
	 */
	public boolean isEnableEmployeeRegistration() {
		if(userAcl.contains("EmployeeRegistration"))
			return enableEmployeeRegistration=true;
		else
			return enableEmployeeRegistration=false;
	}
	/**
	 * @return the enableEmployeeOvertimeInfo
	 */
	public boolean isEnableEmployeeOvertimeInfo() {
		if(userAcl.contains("EmployeeOvertimeInfo"))
			return enableEmployeeOvertimeInfo=true;
		else
			return enableEmployeeOvertimeInfo=false;
	}
	/**
	 * @return the enableEmploymentHistoryInfo
	 */
	public boolean isEnableEmploymentHistoryInfo() {
		if(userAcl.contains("EmploymentHistoryInfo"))
			return enableEmploymentHistoryInfo=true;
		else
			return enableEmploymentHistoryInfo=false;
	}
	/**
	 * @return the enableTrainingDeportationInfo
	 */
	public boolean isEnableTrainingDeportationInfo() {
		if(userAcl.contains("TrainingDeportationInfo"))
			return enableTrainingDeportationInfo=true;
		else
			return enableTrainingDeportationInfo=false;
	}
	/**
	 * @return the enableEmployeeAttendanceInfo
	 */
	public boolean isEnableEmployeeAttendanceInfo() {
		if(userAcl.contains("EmployeeAttendanceInfo"))
			return enableEmployeeAttendanceInfo=true;
		else
			return enableEmployeeAttendanceInfo=false;
	}
	/**
	 * @return the enableInstitutionRegistration
	 */
	public boolean isEnableInstitutionRegistration() {
		if(userAcl.contains("InstitutionRegistration"))
			return enableInstitutionRegistration=true;
		else
			return enableInstitutionRegistration=false;
	}
	/**
	 * @return the enableFeeScheme
	 */
	public boolean isEnableFeeScheme() {
		if(userAcl.contains("fee_scheme"))
			return enableFeeScheme=true;
		else
			return enableFeeScheme=false;
	}
	/**
	 * @return the enableFeePaymentList
	 */
	public boolean isEnableFeePaymentList() {
		if(userAcl.contains("feePaymentList"))
			return enableFeePaymentList=true;
		else
			return enableFeePaymentList=false;
	}
	/**
	 * @return the enableLibrarySetting
	 */
	public boolean isEnableLibrarySetting() {
		if(userAcl.contains("LibrarySetting"))
			return enableLibrarySetting=true;
		else
			return enableLibrarySetting=false;
	}
	/**
	 * @return the enableAuthorManagement
	 */
	public boolean isEnableAuthorManagement() {
		if(userAcl.contains("AuthorManagement"))
			return enableAuthorManagement=true;
		else
			return enableAuthorManagement=false;
	}
	/**
	 * @return the enablePublisherManagement
	 */
	public boolean isEnablePublisherManagement() {
		if(userAcl.contains("PublisherManagement"))
			return enablePublisherManagement=true;
		else
			return enablePublisherManagement=false;
	}
	/**
	 * @return the enableBookCategory
	 */
	public boolean isEnableBookCategory() {
		if(userAcl.contains("BookCategory"))
			return enableBookCategory=true;
		else
			return enableBookCategory=false;
	}
	/**
	 * @return the enableBookManagementForm
	 */
	public boolean isEnableBookManagementForm() {
		if(userAcl.contains("BookManagementForm"))
			return enableBookManagementForm=true;
		else
			return enableBookManagementForm=false;
	}
	/**
	 * @return the enableMemberManagementForm
	 */
	public boolean isEnableMemberManagementForm() {
		if(userAcl.contains("MemberManagementForm"))
			return enableMemberManagementForm=true;
		else
			return enableMemberManagementForm=false;
	}
	/**
	 * @return the enableBookIssueListForm
	 */
	public boolean isEnableBookIssueListForm() {
		if(userAcl.contains("BookIssueListForm"))
			return enableBookIssueListForm=true;
		else
			return enableBookIssueListForm=false;
	}
	/**
	 * @return the enableStudentList
	 */
	public boolean isEnableStudentList() {
		if(userAcl.contains("studentlist"))
			return enableStudentList=true;
		else
			return enableStudentList=false;
	}
	/**
	 * @return the enableArticleShip
	 */
	public boolean isEnableArticleShip() {
		if(userAcl.contains("ArticleShip"))
			return enableArticleShip=true;
		else
			return enableArticleShip=false;
	}
	
	/*---------------------------Inventory------------------------*/
	/*
	 * hasnt added these name in database and dispatch url not defined
	 */
	public boolean isEnabledBrandName(){
		if(userAcl.contains("BrandName"))
			return isEnableBrandName = true;
		else 
			return isEnableBrandName = false;
	}
	public boolean isEnableSupplierInformation(){
		if(userAcl.contains("supplierInformation"))
			return isEnableSupplierInformation = true;
		else 
			return isEnableSupplierInformation= false;
	}
	public boolean isEnableHandlingCost(){
		if(userAcl.contains("handlingCost"))
			return isEnableHandlingCost = true;
		else
			return isEnableHandlingCost = false;
	}
	public boolean isEnableFixedAssestCost(){
		if(userAcl.contains("fixedAsset"))
			return enablefixedAssest = true;
		else
			return enablefixedAssest = false;
	}
	public boolean isEnableInventoryStock(){
		if (userAcl.contains("inventoryStock"))
			return enableInventoryStock = true;
		else
			return enableInventoryStock = false;
	}
	
	/*
	 * View dispatcher methods should be added below.
	 * For a view request either it dispatches the requested view
	 * or the insufficient privilege page (from where user can return back to welcome page)
	 * 
	 * These methods actually are not needed for simple cases (lower level of security practices)
	 * where view redirection is done in the *.xhtml view files. But for higher level of security
	 * purposes the view navigations must be accomplished via methods of some compiled classes.
	 * 
	 * 
	 */
	public String dispatchIndexView()
	{
		if(isEnableIndex())
			return "/pages/protected/index.xhtml?faces-redirect=true";
		else
			return redirectToPrivilegeErrorPage();
	}
	public String dispatchUserManagementView()
	{
		if(isEnableUserManagement())
			return "/pages/protected/admin/usermanagement.xhtml?faces-redirect=true";
		else
			return redirectToPrivilegeErrorPage();
	}
	public String dispatchDepartmentView()
	{
		if(isEnableDepartment())
			return "/pages/protected/admin/department.xhtml?faces-redirect=true";
		else
			return redirectToPrivilegeErrorPage();
	}
	public String dispatchRoleManagementView()
	{
		if(isEnableRoleManagement())
			return "/pages/protected/admin/rolemanagement.xhtml?faces-redirect=true";
		else
			return redirectToPrivilegeErrorPage();
	} 
	public String dispatchAccountHeadView()
	{
		if(isEnableAccountHead())
			return "/pages/protected/admin/accounthead.xhtml?faces-redirect=true";
		else
			return redirectToPrivilegeErrorPage();
	}

//================
	public String dispatchJournalVoucherView()
	{
		if(isEnableJournalVoucher())
			return "/pages/account/journalVoucherDetails.xhtml?faces-redirect=true";
		else
			return redirectToPrivilegeErrorPage();
	}
	public String dispatchAccHeadMapView()
	{
		if(isEnableUserManagement())
			return "/pages/account/accHeadMap.xhtml?faces-redirect=true";
		else
			return redirectToPrivilegeErrorPage();
	}
	public String dispatchFormCodeView()
	{
		if(isEnableFormCode())
			return "/pages/account/formCode.xhtml?faces-redirect=true";
		else
			return redirectToPrivilegeErrorPage();
	}
//=====CBC=========	
	public String dispatchCbcEntryView()
	{
		if(isEnableCbcEntry())
			return "/pages/hr/committee/committeeEntry.xhtml?faces-redirect=true";
		else
			return redirectToPrivilegeErrorPage();
	}

	public String dispatchCbcMemberEntryView()
	{
		if(isEnableCbcMemberEntry())
			return "/pages/hr/committee/committeeMemberEntry.xhtml?faces-redirect=true";
		else
			return redirectToPrivilegeErrorPage();
	}
	public String dispatchCbcNameEntryView()
	{
		if(isEnableUserManagement())
			return "/pages/protected/admin/usermanagement.xhtml?faces-redirect=true";
		else
			return redirectToPrivilegeErrorPage();
	}
	public String dispatchCbcPositionEntryView()
	{
		if(isEnableCbcPositionEntry())
			return "/pages/hr/committee/committeePositionEntry.xhtml?faces-redirect=true";
		else
			return redirectToPrivilegeErrorPage();
	}
	public String dispatchCbcMeetingAndAllowanceView()
	{
		if(isEnableCbcMeetAndAll())
			return "/pages/hr/committee/committeeMeetingAndAllowInfo.xhtml?faces-redirect=true";
		else
			return redirectToPrivilegeErrorPage();
	} 
//=====Employee==========	
	public String dispatchOfficeTimeView()
	{
		if(isEnableOfficeTimeInfo())
			return "/pages/hr/OfficeTimeInfo.xhtml?faces-redirect=true";
		else
			return redirectToPrivilegeErrorPage();
	} 

	public String dispatchEmployeeDesignationView()
	{
		if(isEnableEmployeeDesignation())
			return "/pages/hr/employee/EmployeeDesignation.xhtml?faces-redirect=true";
		else
			return redirectToPrivilegeErrorPage();
	}


	public String dispatchEmployeeLeaveInfoView()
	{
		if(isEnableEmployeeLeaveInfo())
			return "/pages/hr/employee/EmployeeLeaveInfo.xhtml?faces-redirect=true";
		else
			return redirectToPrivilegeErrorPage();
	}
	public String dispatchEmployeeLeaveTypesView()
	{
		if(isEnableEmployeeLeaveTypes())
			return "/pages/hr/employee/EmployeeLeaveTypes.xhtml?faces-redirect=true";
		else
			return redirectToPrivilegeErrorPage();
	}
	public String dispatchEmployeeManagementView()
	{
		if(isEnableEmployeeManagement())
			return "/pages/hr/employee/EmployeeManagement.xhtml?faces-redirect=true";
		else
			return redirectToPrivilegeErrorPage();
	}
	public String dispatchEmployeeRegistrationView()
	{
		if(isEnableEmployeeRegistration())
			return "/pages/hr/employee/dialog/EmployeeRegistration.xhtml?faces-redirect=true";
		else
			return redirectToPrivilegeErrorPage();
	}
	public String dispatchEmployeeOvertimeInfoView()
	{
		if(isEnableEmployeeOvertimeInfo())
			return "/pages/hr/employee/EmployeeOvertimeInfo.xhtml?faces-redirect=true";
		else
			return redirectToPrivilegeErrorPage();
	} 
	public String dispatchEmploymentHistoryInfoView()
	{
		if(isEnableEmploymentHistoryInfo())
			return "/pages/hr/employee/EmploymentHistoryInfo.xhtml?faces-redirect=true";
		else
			return redirectToPrivilegeErrorPage();
	}

	public String dispatchTrainingDeputationView()
	{
		if(isEnableTrainingDeportationInfo())
			return "/pages/hr/employee/TrainingDeportationInfo.xhtml?faces-redirect=true";
		else
			return redirectToPrivilegeErrorPage();
	}
	public String dispatchEmployeeAttendanceView()
	{
		if(isEnableEmployeeAttendanceInfo())
			return "/pages/hr/employee/EmployeeAttendanceInfo.xhtml?faces-redirect=true";
		else
			return redirectToPrivilegeErrorPage();
	}
	//=====Institution========
	public String dispatchInstitutionRegistrationView()
	{
		if(isEnableDepartment())
			return "/pages/protected/admin/department.xhtml?faces-redirect=true";
		else
			return redirectToPrivilegeErrorPage();
	}
	//=====Fee=======
	public String dispatchFeeSchemeView()
	{
		if(isEnableFeeScheme())
			return "/pages/fee/fee_scheme.xhtml?faces-redirect=true";
		else
			return redirectToPrivilegeErrorPage();
	} 
	public String dispatchFeePaymentListView()
	{
		if(isEnableFeePaymentList())
			return "/pages/fee/feePaymentList.xhtml?faces-redirect=true";
		else
			return redirectToPrivilegeErrorPage();
	}

//========Library======
	public String dispatchLibrarySettingView()
	{
		if(isEnableLibrarySetting())
			return "/pages/library/LibrarySetting.xhtml?faces-redirect=true";
		else
			return redirectToPrivilegeErrorPage();
	}
	public String dispatchAuthorManagementView()
	{
		if(isEnableAuthorManagement())
			return "/pages/library/AuthorManagement.xhtml?faces-redirect=true";
		else
			return redirectToPrivilegeErrorPage();
	}
	public String dispatchPublisherManagementView()
	{
		if(isEnablePublisherManagement())
			return "/pages/library/PublisherManagement.xhtml?faces-redirect=true";
		else
			return redirectToPrivilegeErrorPage();
	}
	public String dispatchBookCategoryView()
	{
		if(isEnableBookCategory())
			return "/pages/library/BookCategory.xhtml?faces-redirect=true";
		else
			return redirectToPrivilegeErrorPage();
	} 
	public String dispatchBookManagementFormView()
	{
		if(isEnableBookManagementForm())
			return "/pages/library/BookManagementForm.xhtml?faces-redirect=true";
		else
			return redirectToPrivilegeErrorPage();
	}

	public String dispatchMemberManagementFormView()
	{
		if(isEnableMemberManagementForm())
			return "/pages/library/MemberManagementForm.xhtml?faces-redirect=true";
		else
			return redirectToPrivilegeErrorPage();
	}
	public String dispatchBookIssueListFormView()
	{
		if(isEnableBookIssueListForm())
			return "/pages/library/BookIssueListForm.xhtml?faces-redirect=true";
		else
			return redirectToPrivilegeErrorPage();
	}
	//=====Student==========
	public String dispatchStudentListView()
	{
		if(isEnableStudentList())
			return "/pages/registration/studentlist.xhtml?faces-redirect=true";
		else
			return redirectToPrivilegeErrorPage();
	}
	public String dispatchArticleShipView()
	{
		if(isEnableArticleShip())
			return "/pages/registration/ArticleShip.xhtml?faces-redirect=true";
		else
			return redirectToPrivilegeErrorPage();
	} 
	
	private String redirectToPrivilegeErrorPage()
	{
		return "/pages/public/accessDenied.xhtml?faces-redirect=true";
	}
//=============
	public boolean isUserSelection() {
		return userSelection;
	}
	public void setUserSelection(boolean userSelection) {
		this.userSelection = userSelection;
	}
	
	
}
