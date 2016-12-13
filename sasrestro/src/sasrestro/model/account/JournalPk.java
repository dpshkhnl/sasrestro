package sasrestro.model.account;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class JournalPk implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Column(name="jv_no")
	private int jvNo;
	@Column(name="fy_id")
	private int fyId;
	@Column(name="jv_type")
	private int jvType;
	/**
	 * @return the jvNo
	 */
	public int getJvNo() {
		return jvNo;
	}
	/**
	 * @param jvNo the jvNo to set
	 */
	public void setJvNo(int jvNo) {
		this.jvNo = jvNo;
	}
	/**
	 * @return the fiscalYrModel
	 */
	public int getFyId() {
		return fyId;
	}
	/**
	 * @param fyIdPassed the fiscalYrModel to set
	 */
	public void setFyId(int fyIdPassed) {
		this.fyId = fyIdPassed;
	}
	/**
	 * @return the type
	 */
	public int getJvType() {
		return jvType;
	}
	/**
	 * @param type the type to set
	 */
	public void setJvType(int jvType) {
		this.jvType = jvType;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + fyId;
		result = prime * result + jvNo;
		return result;
	}
	 (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof JournalPk)) {
			return false;
		}
		JournalPk other = (JournalPk) obj;
		if (fyId != other.fyId) {
			return false;
		}
		if (jvNo != other.jvNo) {
			return false;
		}
		return true;
	}*/
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + fyId;
		result = prime * result + jvNo;
		result = prime * result + jvType;
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JournalPk other = (JournalPk) obj;
		if (fyId != other.fyId)
			return false;
		if (jvNo != other.jvNo)
			return false;
		if (jvType != other.jvType)
			return false;
		return true;
	}
	
	
	
}
