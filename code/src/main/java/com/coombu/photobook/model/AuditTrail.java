package com.coombu.photobook.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;

@MappedSuperclass
public class AuditTrail implements IAuditable, Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Column(name = "CREATE_TMSTMP",  length = 19)
	protected Timestamp createTimestamp;
	
	@Column(name = "CREATED_BY", nullable = false)
	protected Long createdBy;
	
	@Column(name = "UPDATE_TMSTMP", length = 19)
	protected Timestamp updateTimestamp;
	
	@Column(name = "UPDATED_BY")
	protected Long updatedBy;

	/* (non-Javadoc)
	 * @see com.coombu.photobook.model.Auditable#getCreateTimestamp()
	 */
	@XmlTransient
	@Override
	public Timestamp getCreateTimestamp() {
		return this.createTimestamp;
	}

        /* (non-Javadoc)
		 * @see com.coombu.photobook.model.Auditable#setCreateTimestamp(java.sql.Timestamp)
		 */
	@Override
	public void setCreateTimestamp(Timestamp createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.model.Auditable#getCreatedBy()
	 */
	@XmlTransient
	@Override
	public Long getCreatedBy() {
		return this.createdBy;
	}

        /* (non-Javadoc)
		 * @see com.coombu.photobook.model.Auditable#setCreatedBy(int)
		 */
    @Override
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.model.Auditable#getUpdateTimestamp()
	 */
    @XmlTransient
    @Override
	public Timestamp getUpdateTimestamp() {
		return this.updateTimestamp;
	}

        /* (non-Javadoc)
		 * @see com.coombu.photobook.model.Auditable#setUpdateTimestamp(java.sql.Timestamp)
		 */
    @Override
	public void setUpdateTimestamp(Timestamp updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.model.Auditable#getUpdatedBy()
	 */
    @XmlTransient
    @Override
	public Long getUpdatedBy() {
		return this.updatedBy;
	}

        /* (non-Javadoc)
		 * @see com.coombu.photobook.model.Auditable#setUpdatedBy(java.lang.Integer)
		 */
    @Override
	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	/* (non-Javadoc)
	 * @see com.coombu.photobook.model.Auditable#getAuditTrailString()
	 */
    @XmlTransient
    @Transient
	public String getAuditTrailString()
	{
		StringBuffer strbuf = new StringBuffer("\ncreatedBy: ");
		strbuf.append(createdBy);
		strbuf.append("\nupdatedBy: ");
		strbuf.append(updatedBy);
		strbuf.append("\ncreatedTimestamp: ");
		strbuf.append(createTimestamp);
		strbuf.append("\nupdateTimestamp: ");
		strbuf.append(updateTimestamp);
		
		return strbuf.toString();
	}
	
	/* (non-Javadoc)
	 * @see com.coombu.photobook.model.Auditable#clearAuditInfo()
	 */
	public void clearAuditInfo()
	{
		this.createdBy         = null;		
		this.updatedBy        = null;
		this.createTimestamp  = null;
		this.updateTimestamp = null;		
	}
}
