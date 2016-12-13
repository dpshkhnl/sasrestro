package sasrestro.model.restaurant;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

	
	@Entity
	@Table(name ="employee_arrtt")
	public class EmployeeAttModel implements Serializable {

		private static final long serialVersionUID = 1L; 
		
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name = "att_id")
		private int attId;
		
		@Column(name = "date")
		@Temporal(TemporalType.TIMESTAMP)
		private Date date;
		
		
		@OneToOne
		@JoinColumn(name = "employee")
		private EmployeeModel employee;
		
		@Column(name = "attendance")
		private Boolean attendance;

		public int getAttId() {
			return attId;
		}

		public void setAttId(int attId) {
			this.attId = attId;
		}

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}

		public EmployeeModel getEmployee() {
			return employee;
		}

		public void setEmployee(EmployeeModel employee) {
			this.employee = employee;
		}

		public Boolean getAttendance() {
			return attendance;
		}

		public void setAttendance(Boolean attendance) {
			this.attendance = attendance;
		}
		

}
