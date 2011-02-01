package com.gisgraphy.addressparser;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;


public class AddressResultsDtoTest {
	@Test
	public void constructor(){
		Address address = new Address();
		address.setCity("city");
		List<Address> list = new ArrayList<Address>();
		list.add(address);
		Long qtime = 234L;
		AddressResultsDto dto = new AddressResultsDto(list,qtime );
		Assert.assertEquals(list, dto.getResult());
		Assert.assertEquals(qtime, dto.getQTime());
		Assert.assertEquals(list.size(), dto.getNumFound());
	}
	
	@Test
	public void constructorWithNullList(){
		Long qtime = 234L;
		AddressResultsDto dto = new AddressResultsDto(null,qtime );
		Assert.assertEquals(null, dto.getResult());
		Assert.assertEquals(qtime, dto.getQTime());
		Assert.assertEquals(0,dto.getNumFound());
	}
	
}
