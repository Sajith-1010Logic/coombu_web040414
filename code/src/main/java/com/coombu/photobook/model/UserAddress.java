package com.coombu.photobook.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
// Generated Nov 3, 2013 6:30:53 PM by Hibernate Tools 4.0.0
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 * UserAddress generated by hbm2java
 */
@XmlRootElement
@Entity
@Table(name = "user_address", uniqueConstraints = @UniqueConstraint(columnNames = {
		"USER_PROFILE_ID", "ADDRESS_TYPE_ID", "ADDRESS_ID" }))
public class UserAddress extends AuditTrail implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "USER_ADDRESS_ID", unique = true, nullable = false)
	private int userAddressId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_PROFILE_ID", nullable = false)
	private UserProfile userProfile;
	
	@Cascade({CascadeType.ALL})
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ADDRESS_ID", nullable = false)
	private Address address;
	
	@Column(name = "ADDRESS_TYPE_ID", nullable = false)
	private short addressTypeId;


	public UserAddress() {
	}

	public UserAddress(int userAddressId, UserProfile userProfile,
			Address address, short addressTypeId, Timestamp createTimestamp,
			long createdBy) {
		this.userAddressId = userAddressId;
		this.userProfile = userProfile;
		this.address = address;
		this.addressTypeId = addressTypeId;
		this.createTimestamp = createTimestamp;
		this.createdBy = createdBy;
	}

	public UserAddress(int userAddressId, UserProfile userProfile,
			Address address, short addressTypeId, Timestamp createTimestamp,
			long createdBy, Timestamp updateTimestamp, Long updatedBy) {
		this.userAddressId = userAddressId;
		this.userProfile = userProfile;
		this.address = address;
		this.addressTypeId = addressTypeId;
		this.createTimestamp = createTimestamp;
		this.createdBy = createdBy;
		this.updateTimestamp = updateTimestamp;
		this.updatedBy = updatedBy;
	}

	@XmlElement
	public int getUserAddressId() {
		return this.userAddressId;
	}

	public void setUserAddressId(int userAddressId) {
		this.userAddressId = userAddressId;
	}
	@XmlTransient
	public UserProfile getUserProfile() {
		return this.userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	@XmlElement
	public Address getAddress() {
		return this.address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	@XmlElement
	public short getAddressTypeId() {
		return this.addressTypeId;
	}

	public void setAddressTypeId(short addressTypeId) {
		this.addressTypeId = addressTypeId;
	}


}
