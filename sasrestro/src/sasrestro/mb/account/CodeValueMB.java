package sasrestro.mb.account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import sasrestro.misc.AbstractMB;
import sasrestro.model.account.CodeValue;
import sasrestro.sessionejb.account.CodeValueEJB;

/**
 * @author nebula
 * 
 */
@ViewScoped
@ManagedBean(name = "codeValueMB")
public class CodeValueMB extends AbstractMB implements Serializable {

	/**
	 * 
	 */
	public static final String INJECTION_NAME = "#{codeValueMB}";
	private static final long serialVersionUID = 1L;

	@EJB
	CodeValueEJB codeValueEJB;

	private List<CodeValue> allCodeValues;
	private List<CodeValue> codeValuesOfType;
	private List<CodeValue> codeValuesOfTest;
	private List<CodeValue> codeValuesOfDivision;
	private List<CodeValue> codeValueOfDesignationType;
	private List<CodeValue> codeValueOfLeaveStatus;
	private List<CodeValue> codeValueFaculty;
	private List<CodeValue> codeValueMemberDisciplinary;
	private List<CodeValue> codeValueRaClass;
	private List<CodeValue> codeValueInstitute;
	private List<CodeValue> codeValueMemberCancelReason;
	private List<CodeValue> firmCancellationReason;
	private List<CodeValue> codeValueModeOfUpgrade;
	private List<CodeValue> codeValueForLibrary;
	private List<CodeValue> codeValueForPos;
	private List<CodeValue> codeValueForInvoiceStatus;
	private List<CodeValue> codeValueForEngMonthType;
	private List<CodeValue> codeValueForSubjectGroup;
	private List<CodeValue> codeValueForAttendenceStatus;
	private List<CodeValue> codeRegions;

	public CodeValueMB() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * public CodeValueFacade getCodeValueFacade() { if(codeValueFacade==null) {
	 * codeValueFacade=new CodeValueFacade(); } return codeValueFacade; }
	 * 
	 * public void setCodeValueFacade(CodeValueFacade codeValueFacade) {
	 * this.codeValueFacade = codeValueFacade; }
	 */

	public List<CodeValue> getAllCodeValues() {
		if (allCodeValues == null) {
			loadAllCodeValues();
		}
		return allCodeValues;
	}

	public void setAllCodeValues(List<CodeValue> allCodeValues) {
		this.allCodeValues = allCodeValues;
	}

	private void loadAllCodeValues() {
		allCodeValues = codeValueEJB.listAllCodes();
		Iterator<CodeValue> iteraror = allCodeValues.iterator();
		while (iteraror.hasNext()) {
			CodeValue codeValue = iteraror.next();
			String labelName = codeValue.getCvLbl();
			int cvId = codeValue.getCvId();
			System.out.println("This Code value =" + labelName + " with id ="
					+ cvId);
		}

	}

	/*
	 * problem problem.if used same instance of codeValuesOfType two
	 * times.because condition checked is null if (codeValuesOfType == null)
	 */
	public List<CodeValue> getCodeValuesOfTypeAccount() {
		if (codeValuesOfType == null) {
			codeValuesOfType = new ArrayList<CodeValue>();
			codeValuesOfType = codeValueEJB
					.findCodeValueByCVType("Account Type");
		}
		return codeValuesOfType;
	}

	public List<CodeValue> getCodeValuesOfInvStockType() {
		if (codeValuesOfType == null) {
			codeValuesOfType = new ArrayList<CodeValue>();
			codeValuesOfType = codeValueEJB.findCodeValueByCVType("InvStkType");
		}
		return codeValuesOfType;
	}

	public List<CodeValue> getCodeValuesOfInvOutMethod() {
		if (codeValuesOfType == null) {
			codeValuesOfType = new ArrayList<CodeValue>();
			codeValuesOfType = codeValueEJB
					.findCodeValueByCVType("InvOutMethod");
		}
		return codeValuesOfType;
	}

	public void setCodeValuesOfType(List<CodeValue> codeValuesOfType) {
		this.codeValuesOfType = codeValuesOfType;
	}

	public List<CodeValue> getCodeValueGender() {
		// if (codeValuesOfType==null) {
		codeValuesOfType = new ArrayList<CodeValue>();
		codeValuesOfType = codeValueEJB.findCodeValueByCVType("Gender");
		// }
		return codeValuesOfType;
	}

	public List<CodeValue> getCodeValueMartialStatus() {
		// if (codeValuesOfType==null) {
		codeValuesOfType = new ArrayList<CodeValue>();
		codeValuesOfType = codeValueEJB.findCodeValueByCVType("MartialStatus");
		// }
		return codeValuesOfType;
	}

	public List<CodeValue> getCodeValueReligion() {
		// if (codeValuesOfType==null) {
		codeValuesOfType = new ArrayList<CodeValue>();
		codeValuesOfType = codeValueEJB.findCodeValueByCVType("Religion");
		// }
		return codeValuesOfType;
	}

	public List<CodeValue> getCodeValueByDivision() {
		// if (codeValuesOfType==null) {
		codeValuesOfType = new ArrayList<CodeValue>();
		codeValuesOfType = codeValueEJB.findCodeValueByCVType("Division");
		// }
		return codeValuesOfType;
	}

	public List<CodeValue> getCodeValueCountry() {
		// if (codeValuesOfType==null) {
		codeValuesOfType = new ArrayList<CodeValue>();
		codeValuesOfType = codeValueEJB.findCodeValueByCVType("Country");
		// }
		return codeValuesOfType;
	}

	public List<CodeValue> getCodeValueByAppoinmentType() {
		// if (codeValuesOfType==null) {
		codeValuesOfType = new ArrayList<CodeValue>();
		codeValuesOfType = codeValueEJB.findCodeValueByCVType("AppoinmentType");
		// }
		return codeValuesOfType;
	}

	public List<CodeValue> getcodeValueSchmeType() {
		if (codeValuesOfType == null) {
			codeValuesOfType = new ArrayList<CodeValue>();
			codeValuesOfType = codeValueEJB.findCodeValueByCVType("SchemeType");
		}
		return codeValuesOfType;
	}

	// library
	public List<CodeValue> getCodeValueLibSetting() {
		if (codeValuesOfType == null) {
			codeValuesOfType = new ArrayList<CodeValue>();
			codeValuesOfType = codeValueEJB
					.findCodeValueByCVType("LibrarySetting");
		}
		return codeValuesOfType;
	}

	public List<CodeValue> getcodeValueCategoryType() {
		// if(codeValuesOfType==null)
		// {
		codeValuesOfTest = new ArrayList<CodeValue>();
		codeValuesOfTest = codeValueEJB.findCodeValueByCVType("Category");
		// }
		return codeValuesOfTest;
	}

	public List<CodeValue> getcodeValueTrxnwithType() {

		codeValuesOfType = new ArrayList<CodeValue>();
		codeValuesOfType = codeValueEJB.findCodeValueByCVType("trxnWith");

		return codeValuesOfType;
	}

	public List<CodeValue> getcodeValuePaymentByType() {

		codeValuesOfType = new ArrayList<CodeValue>();
		codeValuesOfType = codeValueEJB.findCodeValueByCVType("PaymentBy");

		return codeValuesOfType;
	}

	public List<CodeValue> getCodeValuesOfTest() {
		return codeValuesOfTest;
	}

	public void setCodeValuesOfTest(List<CodeValue> codeValuesOfTest) {
		this.codeValuesOfTest = codeValuesOfTest;
	}

	public List<CodeValue> getBookStatus() {

		if (codeValuesOfType == null) {
			codeValuesOfType = new ArrayList<CodeValue>();
			codeValuesOfType = codeValueEJB.findCodeValueByCVType("BookStatus");
		}
		return codeValuesOfType;
	}

	public List<CodeValue> getCodeValuesOfDivision() {
		if (codeValuesOfDivision == null) {
			codeValuesOfDivision = codeValueEJB
					.findCodeValueByCVType("DivisionType");
		}
		return codeValuesOfDivision;
	}

	/**
	 * Generates list of Encashment Condition for Leave Type
	 * 
	 * @return
	 */
	public List<CodeValue> getEncashmentCondition() {

		// if (codeValuesOfType == null) {
		codeValuesOfType = new ArrayList<CodeValue>();
		codeValuesOfType = codeValueEJB
				.findCodeValueByCVType("Encashment Condition");
		// }
		return codeValuesOfType;
	}

	public void setCodeValuesOfDivision(List<CodeValue> codeValuesOfDivision) {
		this.codeValuesOfDivision = codeValuesOfDivision;
	}

	/**
	 * @return the codeValueOfDesignationType
	 */
	public List<CodeValue> getCodeValueOfDesignationType() {
		if (codeValueOfDesignationType == null) {
			codeValueOfDesignationType = codeValueEJB
					.findCodeValueByCVType("DesignationType");
		}
		return codeValueOfDesignationType;
	}

	/**
	 * Generates list of Leave Status for Leave Types
	 * 
	 * @return
	 */
	public List<CodeValue> getCodeValueOfLeaveStatus() {
		// if (codeValueOfLeaveStatus == null) {
		codeValueOfLeaveStatus = new ArrayList<CodeValue>();
		codeValueOfLeaveStatus = codeValueEJB
				.findCodeValueByCVType("LeaveStatus");
		// }
		return codeValueOfLeaveStatus;
	}

	public void setCodeValueFaculty(List<CodeValue> codeValueFaculty) {
		this.codeValueFaculty = codeValueFaculty;
	}

	public List<CodeValue> getCodeValueFaculty() {
		codeValueFaculty = new ArrayList<CodeValue>();
		codeValueFaculty = codeValueEJB.findCodeValueByCVType("Member Faculty");
		return codeValueFaculty;
	}

	public List<CodeValue> getCodeValueMemberDisciplinary() {
		codeValueMemberDisciplinary = new ArrayList<CodeValue>();
		codeValueMemberDisciplinary = codeValueEJB
				.findCodeValueByCVType("Disciplinary Action");
		return codeValueMemberDisciplinary;
	}

	public void setCodeValueMemberDisciplinary(
			List<CodeValue> codeValueMemberDisciplinary) {
		this.codeValueMemberDisciplinary = codeValueMemberDisciplinary;
	}

	public List<CodeValue> getCodeValueInstitute() {
		codeValueInstitute = new ArrayList<CodeValue>();
		codeValueInstitute = codeValueEJB.findCodeValueByCVType("Institute");
		return codeValueInstitute;
	}

	public void setCodeValueInstitute(List<CodeValue> codeValueInstitute) {
		this.codeValueInstitute = codeValueInstitute;
	}

	public List<CodeValue> getCodeValueRaClass() {
		codeValueRaClass = new ArrayList<CodeValue>();
		codeValueRaClass = codeValueEJB.findCodeValueByCVType("RA Class");
		return codeValueRaClass;
	}

	public void setCodeValueRaClass(List<CodeValue> codeValueRaClass) {
		this.codeValueRaClass = codeValueRaClass;
	}

	public List<CodeValue> getCodeValueMemberCancelReason() {
		codeValueMemberCancelReason = new ArrayList<CodeValue>();
		codeValueMemberCancelReason = codeValueEJB
				.findCodeValueByCVType("MemberCancelReason");
		return codeValueMemberCancelReason;
	}

	public void setCodeValueMemberCancelReason(
			List<CodeValue> codeValueMemberCancelReason) {
		this.codeValueMemberCancelReason = codeValueMemberCancelReason;
	}

	/**
	 * @param codeValueOfDesignationType
	 *            the codeValueOfDesignationType to set
	 */
	public void setCodeValueOfDesignationType(
			List<CodeValue> codeValueOfDesignationType) {
		this.codeValueOfDesignationType = codeValueOfDesignationType;
	}

	public List<CodeValue> getFirmCancellationReason() {
		firmCancellationReason = new ArrayList<CodeValue>();
		firmCancellationReason = codeValueEJB
				.findCodeValueByCVType("FirmCancellationReason");
		return firmCancellationReason;
	}

	public void setFirmCancellationReason(List<CodeValue> firmCancellationReason) {
		this.firmCancellationReason = firmCancellationReason;
	}

	/**
	 * Gets list for Source Type= Outsource For CPE Training Schedule Entry
	 * 
	 * @return
	 */
	/**
	 * @return
	 */
	public List<CodeValue> getOutsourceICAN() {

		// if (codeValuesOfType == null) {
		codeValuesOfType = new ArrayList<CodeValue>();
		codeValuesOfType = codeValueEJB
				.findCodeValueByCVType("CPEInsourceType");
		// }
		return codeValuesOfType;
	}
	public List<CodeValue> getCodeValuesOfJobStatus() {
		if (codeValuesOfDivision == null) {
			codeValuesOfDivision = codeValueEJB
					.findCodeValueByCVType("JobStatus");
		}
		return codeValuesOfDivision;
	}

	public List<CodeValue> getCodeValueModeOfUpgrade() {
		codeValueModeOfUpgrade = new ArrayList<CodeValue>();
		codeValueModeOfUpgrade = codeValueEJB.findCodeValueByCVType("Mode Of Upgrade");
		return codeValueModeOfUpgrade;
	}

	public void setCodeValueModeOfUpgrade(List<CodeValue> codeValueModeOfUpgrade) {
		this.codeValueModeOfUpgrade = codeValueModeOfUpgrade;
	}

	public List<CodeValue> getCodeValueForLibrary() {
		codeValueForLibrary = new ArrayList<CodeValue>();
		codeValueForLibrary = codeValueEJB.findCodeValueByCVType("LibrarySetting");
		return codeValueForLibrary;
	}

	public void setCodeValueForLibrary(List<CodeValue> codeValueForLibrary) {
		this.codeValueForLibrary = codeValueForLibrary;
	}

	/**
	 * @return the codeValueForPos
	 */
	public List<CodeValue> getCodeValueForPos() {
		codeValueForPos = new ArrayList<CodeValue>();
		codeValueForPos = codeValueEJB.findCodeValueByCVType("InventoryIssueStatus");
		return codeValueForPos;
	}

	/**
	 * @param codeValueForPos the codeValueForPos to set
	 */
	public void setCodeValueForPos(List<CodeValue> codeValueForPos) {
		this.codeValueForPos = codeValueForPos;
	}
	
	public List<CodeValue> getCodeValueForInvoiceStatus() {
		codeValueForInvoiceStatus = new ArrayList<CodeValue>();
		codeValueForInvoiceStatus = codeValueEJB.findCodeValueByCVType("InvoiceStatus");
		return codeValueForInvoiceStatus;
	}

	public void setCodeValueForInvoiceStatus(
			List<CodeValue> codeValueForInvoiceStatus) {
		this.codeValueForInvoiceStatus = codeValueForInvoiceStatus;
	}

	public List<CodeValue> getCodeValueForEngMonthType() {
		codeValueForEngMonthType = new ArrayList<CodeValue>();
		codeValueForEngMonthType = codeValueEJB.findCodeValueByCVType("EngMonthType");
		return codeValueForEngMonthType;
	}

	public void setCodeValueForEngMonthType(List<CodeValue> codeValueForEngMonthType) {
		this.codeValueForEngMonthType = codeValueForEngMonthType;
	}

	public List<CodeValue> getCodeValueForSubjectGroup() {
		
		codeValueForSubjectGroup = new ArrayList<CodeValue>();
		codeValueForSubjectGroup = codeValueEJB.findCodeValueByCVType("SubjectGroup");
		return codeValueForSubjectGroup;
	}
	
	public void setCodeValueForSubjectGroup(List<CodeValue> codeValueForSubjectGroup) {
		this.codeValueForSubjectGroup = codeValueForSubjectGroup;
	}

	public List<CodeValue> getCodeValueForPreTestPaper() {
		codeValueForLibrary = new ArrayList<CodeValue>();
		codeValueForLibrary = codeValueEJB.findCodeValueByCVType("PreTestPaperType");
		return codeValueForLibrary;
	}

	public List<CodeValue> getCodeValueForAttendenceStatus() {
		codeValueForAttendenceStatus = new ArrayList<CodeValue>();
		codeValueForAttendenceStatus = codeValueEJB.findByCVType("AttendenceStatus");
		return codeValueForAttendenceStatus;
	}

	public void setCodeValueForAttendenceStatus(
			List<CodeValue> codeValueForAttendenceStatus) {
		this.codeValueForAttendenceStatus = codeValueForAttendenceStatus;
	}

	public List<CodeValue> getCodeRegions() {
		codeRegions=new ArrayList<CodeValue>();
		codeRegions=codeValueEJB.findByCVType("Development Regions");
		return codeRegions;
	}

	public void setCodeRegions(List<CodeValue> codeRegions) {
		this.codeRegions = codeRegions;
	}
}
