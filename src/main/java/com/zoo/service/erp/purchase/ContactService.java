package com.zoo.service.erp.purchase;

import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.mapper.erp.purchase.ContactMapper;
import com.zoo.model.erp.purchase.Contact;

@Service
@Transactional
public class ContactService {

	@Autowired
	private ContactMapper contactMapper;

	public Contact addContact(Contact contact) {
		// TODO Auto-generated method stub
		contact.setId(UUID.randomUUID().toString());
		contactMapper.add(contact);
		return contact;
	}

	public int updateContact(Contact contact) {
		// TODO Auto-generated method stub
		return contactMapper.update(contact);
	}
	
}
