package sasrestro.model.account;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author nebula
 * 
 */

@Entity
@Table(name = "code_value_mcg")
@NamedQueries({
		@NamedQuery(name = "CodeValue.findByCVId", query = "SELECT cv FROM CodeValue cv where cv.cvId = :cvIdPassed"),
		@NamedQuery(name = "CodeValue.findByCVType", query = "SELECT cv FROM CodeValue cv where cv.cvType = :cvTypePassed ORDER BY cv.cvCode"),
		@NamedQuery(name = "CodeValue.FIND_BY_LABELS", query = "SELECT cv FROM CodeValue cv where cv.cvType = :type and cv.cvCode in (1,2,6)"),
		@NamedQuery(name = "CodeValue.FIND_BY_POSTALTYPE", query = "SELECT cv FROM CodeValue cv where cv.cvType = :type and cv.cvCode in (3,4,5)"),
		@NamedQuery(name = "CodeValue.FIND_BY_CV_TYPE_LABEL", query = "SELECT cv FROM CodeValue cv where cv.cvType = :type and cv.cvLbl=:label") })
public class CodeValue implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String FIND_BY_CV_ID = "CodeValue.findByCVId";
	public static final String FIND_BY_CV_TYPE = "CodeValue.findByCVType";
	public static final String FIND_BY_CV_TYPE_LABEL = "CodeValue.FIND_BY_CV_TYPE_LABEL";
	public static final String FIND_BY_LABELS = "CodeValue.FIND_BY_LABELS";
	public static final String FIND_BY_POSTALTYPE = "CodeValue.FIND_BY_POSTALTYPE";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cv_id")
	private int cvId;
	@Column(name = "cv_code")
	private int cvCode;
	@Column(name = "cv_lbl")
	private String cvLbl;
	@Column(name = "cv_type")
	private String cvType;

	public int getCvId() {
		return cvId;
	}

	public void setCvId(int cvId) {
		this.cvId = cvId;
	}

	public int getCvCode() {
		return cvCode;
	}

	public void setCvCode(int cvCode) {
		this.cvCode = cvCode;
	}

	public String getCvLbl() {
		return cvLbl;
	}

	public void setCvLbl(String cvLbl) {
		this.cvLbl = cvLbl;
	}

	public String getCvType() {
		return cvType;
	}

	public void setCvType(String cvType) {
		this.cvType = cvType;
	}

	public CodeValue() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int hashCode() {
		return getCvId();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CodeValue) {
			CodeValue codeValue = (CodeValue) obj;
			return codeValue.getCvId() == cvId;
		}

		return false;
	}

}
