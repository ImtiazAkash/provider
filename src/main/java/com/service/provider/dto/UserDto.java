package com.service.provider.dto;

import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserDto {
private int id;


	private String name;


	private String email;
	

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY) 
	private String password;


	private String about;
	
	
	private Set<RoleDto> roles = new HashSet<RoleDto>();
}
