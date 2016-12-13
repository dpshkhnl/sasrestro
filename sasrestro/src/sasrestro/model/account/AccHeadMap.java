package sasrestro.model.account;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "acc_head_map_mcg")
@NamedQueries({
		@NamedQuery(name = "Accheadmap.findByMapPurpose", query = "SELECT ahp FROM AccHeadMap ahp  "
				+ "where ahp.mappingPurpose = :mappurposePassed"),
		@NamedQuery(name = "AccHeadMap.findMapPurposeBank", query = "SELECT ahm FROM AccHeadMap ahm "
				+ "JOIN ahm.accHeadModel ah "
				+ "WHERE ah.accHeadId=:accHeadIdPassed "
				+ "AND ahm.mappingPurpose='Bank Account'"),
		@NamedQuery(name = "AccHeadMap.findMapPurposeCash", query = "SELECT ahm FROM AccHeadMap ahm "
				+ "JOIN ahm.accHeadModel ah "
				+ "WHERE ah.accHeadId=:accHeadIdPassed "
				+ "AND ahm.mappingPurpose='Main Cash In Hand'") })
public class AccHeadMap implements Serializable {
	private static final long serialVersionUID = 932395667757955783L;
	public static final String FIND_BY_MP = "Accheadmap.findByMapPurpose";
	public static final String CHECK_BANK_ACCOUNT_HEAD = "AccHeadMap.findMapPurposeBank";
	public static final String CHECK_CASH_IN_HAND = "AccHeadMap.findMapPurposeCash";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "acc_head_map_id")
	private int accHeadMapId;

	@ManyToOne
	@JoinColumn(name = "acc_head_id", referencedColumnName = "acc_head_id")
	private AccHeadMcg accHeadModel;

	// @Column(name="acc_head_id")
	// private int accHeadId;

	public AccHeadMcg getAccHeadModel() {
		return accHeadModel;
	}

	public void setAccHeadModel(AccHeadMcg accHeadModel) {
		this.accHeadModel = accHeadModel;
	}

	// @ManyToOne(fetch=FetchType.LAZY)
	// @JoinColumn(name="map_purpose",referencedColumnName="form_code_id")
	// private FormCodeModel mappingPurpose;
	@Column(name = "map_purpose", nullable = false)
	private String mappingPurpose;

	public String getMappingPurpose() {
		return mappingPurpose;
	}

	public void setMappingPurpose(String mappingPurpose) {
		this.mappingPurpose = mappingPurpose;
	}

	public int getAccHeadMapId() {
		return accHeadMapId;
	}

	public void setAccHeadMapId(int accHeadMapId) {
		this.accHeadMapId = accHeadMapId;

	}

	// public AccHeadMcg getAccHeadId() {
	// return accHeadModel;
	// }
	//
	// public void setAccHeadId(AccHeadMcg accHeadId) {
	// this.accHeadModel = accHeadId;
	// }

	// public FormCodeModel getMappingPurpose() {
	// if(mappingPurpose==null){
	// mappingPurpose=new FormCodeModel();
	// }
	// return mappingPurpose;
	// }

	// public void setMappingPurpose(FormCodeModel mappingPurpose) {
	// this.mappingPurpose = mappingPurpose;
	// }

	// public int getAccHeadId() {
	// return accHeadId;
	// }
	//
	// public void setAccHeadId(int accHeadId) {
	// this.accHeadId = accHeadId;
	// }

	// public String getMappingPurpose() {
	// return mappingPurpose;
	// }
	//
	// public void setMappingPurpose(String mappingPurpose) {
	// this.mappingPurpose = mappingPurpose;
	// }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + accHeadMapId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AccHeadMap other = (AccHeadMap) obj;
		if (accHeadMapId != other.accHeadMapId)
			return false;
		return true;
	}

	public AccHeadMap() {

	}

}
