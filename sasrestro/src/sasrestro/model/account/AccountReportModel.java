package sasrestro.model.account;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AccountReportModel {

	@Id
	private String accountCode;
	private String particulars;
	private double drAmt;
	private double crAmt;
	
	public AccountReportModel() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the accountCode
	 */
	public String getAccountCode() {
		return accountCode;
	}
	/**
	 * @param accountCode the accountCode to set
	 */
	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}
	/**
	 * @return the particulars
	 */
	public String getParticulars() {
		return particulars;
	}
	/**
	 * @param particulars the particulars to set
	 */
	public void setParticulars(String particulars) {
		this.particulars = particulars;
	}
	/**
	 * @return the drAmt
	 */
	public double getDrAmt() {
		return drAmt;
	}
	/**
	 * @param drAmt the drAmt to set
	 */
	public void setDrAmt(double drAmt) {
		this.drAmt = drAmt;
	}
	/**
	 * @return the crAmt
	 */
	public double getCrAmt() {
		return crAmt;
	}
	/**
	 * @param crAmt the crAmt to set
	 */
	public void setCrAmt(double crAmt) {
		this.crAmt = crAmt;
	}

}
