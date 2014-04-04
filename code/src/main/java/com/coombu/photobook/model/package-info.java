@XmlJavaTypeAdapters(  
 @XmlJavaTypeAdapter(value=TimestampAdapter.class,type=Timestamp.class)
 )
package com.coombu.photobook.model;
import java.sql.Timestamp;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;

import com.coombu.photobook.util.TimestampAdapter;

