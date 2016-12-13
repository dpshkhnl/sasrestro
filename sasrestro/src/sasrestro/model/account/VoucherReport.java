package sasrestro.model.account;

import java.io.Serializable;

public class VoucherReport implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String particulars;
	double dr;
	double cr;
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
	 * @return the dr
	 */
	public double getDr() {
		return dr;
	}
	/**
	 * @param dr the dr to set
	 */
	public void setDr(double dr) {
		this.dr = dr;
	}
	/**
	 * @return the cr
	 */
	public double getCr() {
		return cr;
	}
	/**
	 * @param cr the cr to set
	 */
	public void setCr(double cr) {
		this.cr = cr;
	}
	
	public static java.util.Collection<VoucherReport> loadData() {
		java.util.List<VoucherReport> objList = new java.util.ArrayList<VoucherReport>();
		
		try {
			VoucherReport obj = new VoucherReport();
			obj.setParticulars("Hello World !!");
			obj.setDr(0.00);
			obj.setCr(0.00);
			objList.add(obj);
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return objList;
	}

}
