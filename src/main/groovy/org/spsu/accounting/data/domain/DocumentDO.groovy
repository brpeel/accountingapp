package org.spsu.accounting.data.domain

import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.joda.time.DateTime
import org.spsu.accounting.data.serial.DateTimeSerializer

public class DocumentDO extends BaseDO{

	@JsonProperty("trans_id")
	int transId


	@JsonProperty("file_name")
	String name

	@JsonProperty("content_type")
	String contentType

	int size

	String hash

	@JsonSerialize(using = DateTimeSerializer)
	DateTime uploaded

	@JsonGetter("url")
	public String getUrl(){
		return "api/document/${id}"
	}


	@Override
	public String toString() {
		return "DocumentDO : {transId=${transId}, name='${name}', contentType='${contentType}', size=${size}, hash='${hash}', approved='${uploaded}'}";
	}
}
