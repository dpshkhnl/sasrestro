package sasrestro.model.restaurant;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

	
	@Entity
	@Table(name ="employee")
	public class EmployeeModel implements Serializable {

		private static final long serialVersionUID = 1L; 
		
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name = "employee_id")
		private int employeeId;
		
		@Column (name="name")
		private String name;
		
		@Column (name="address")
		private String address;
		
		@Column (name = "phone")
		private String phone;
		
		@Column (name = "email")
		private String email;
		
		@Column(name = "designation")
		private String designation;
		
		@Basic(fetch = FetchType.LAZY)
		@Lob
		@Column(name = "image")
		private byte[] image;
		
		@Column (name = "enroll_date")
		private String enrollDate;
		@Column (name = "gender")
		private String gender;
		public int getEmployeeId() {
			return employeeId;
		}
		public void setEmployeeId(int employeeId) {
			this.employeeId = employeeId;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getDesignation() {
			return designation;
		}
		public void setDesignation(String designation) {
			this.designation = designation;
		}
		public byte[] getImage() {
			return image;
		}
		public void setImage(byte[] image) {
			this.image = image;
		}
		public String getEnrollDate() {
			return enrollDate;
		}
		public void setEnrollDate(String enrollDate) {
			this.enrollDate = enrollDate;
		}
		public String getGender() {
			return gender;
		}
		public void setGender(String gender) {
			this.gender = gender;
		}
		
		

}
