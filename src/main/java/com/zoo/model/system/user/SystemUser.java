package com.zoo.model.system.user;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zoo.model.system.position.Position;

import lombok.Data;

@Data
public class SystemUser {
	private String id;
	private String realName;
	private String userName;
	@JsonIgnore
	private String password;
	@JsonIgnore
	private String salt;
	private String companyId;
	private List<Position> positions;
}
