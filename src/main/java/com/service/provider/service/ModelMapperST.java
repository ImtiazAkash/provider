package com.service.provider.service;

import org.modelmapper.ModelMapper;

public class ModelMapperST {

	private static ModelMapper INSTANCE = new ModelMapper();

	private ModelMapperST() {}
	
	public static ModelMapper getInstance(){
		return INSTANCE;
	}	
}
