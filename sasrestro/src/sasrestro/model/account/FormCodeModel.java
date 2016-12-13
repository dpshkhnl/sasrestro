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

@Entity
@Table(name = "form_code_mcg")
@NamedQueries({
		@NamedQuery(name = "FormCode.findByFcRemarks", query = "SELECT fc FROM FormCodeModel fc where fc.Remarks = :fcRemarksPassed order by fc.label"),
		@NamedQuery(name = "FormCode.FIND_BY_FC_REMARKS_LABEL", query = "SELECT fc FROM FormCodeModel fc where fc.Remarks = :fcRemarksPassed AND fc.label =:labelPassed"),
		@NamedQuery(name = "FormCode.findByFc", query = "SELECT fc FROM FormCodeModel fc where fc.formCodeId = :fcformcodePassed") })
public class FormCodeModel implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String FIND_BY_FC_REMARKS = "FormCode.findByFcRemarks";
	public static final String FIND_BY_FC_REMARKS_LABEL = "FormCode.FIND_BY_FC_REMARKS_LABEL";
	public static final String FIND_BY_FC = "FormCode.findByFc";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "form_code_id")
	private int formCodeId;

	@Column(name = "label")
	private String label;
	@Column(name = "remarks")
	private String Remarks;

	public int getFormCodeId() {
		return formCodeId;
	}

	public void setFormCodeId(int formCodeId) {
		this.formCodeId = formCodeId;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getRemarks() {
		return Remarks;
	}

	public void setRemarks(String remarks) {
		Remarks = remarks;
	}

	public FormCodeModel() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int hashCode() {
		return getFormCodeId();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FormCodeModel) {
			FormCodeModel formCode = (FormCodeModel) obj;
			return formCode.getFormCodeId() == formCodeId;
		}

		return false;
	}
}