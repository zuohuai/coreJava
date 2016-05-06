package com.edu.java8.optional;

import java.util.Optional;

import org.junit.Test;

public class OptionalTest {

	@Test
	public void test_optinal() throws Exception{
		
		Optional< String > fullName = Optional.ofNullable( null );
		System.out.println( "Full Name is set ? " + fullName.isPresent() );        
		System.out.println( "Full Name: " + fullName.orElseGet( () -> "[none]" ) ); 
		System.out.println( fullName.map( s -> "Hey " + s + "!" ).orElse( "Hey Stranger!" ) );
		
		
		Optional< String > firstName = Optional.of( "Tom" );
		System.out.println( "First Name is set? " + firstName.isPresent() );        
		System.out.println( "First Name: " + firstName.orElseGet( () -> "[none]" ) ); 
		System.out.println( firstName.map( s -> "Hey " + s + "!" ).orElse( "Hey Stranger!" ) );
		System.out.println();
		
	}
}
