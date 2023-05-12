package org.simulspeak.bridge.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import org.simulspeak.bridge.domain.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

	List<Customer> findByLastName(String lastName);

	Customer findById(long id);
}