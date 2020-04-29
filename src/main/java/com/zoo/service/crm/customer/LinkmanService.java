package com.zoo.service.crm.customer;

import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.mapper.crm.customer.LinkmanMapper;
import com.zoo.model.crm.Linkman;

@Service
@Transactional
public class LinkmanService {
	@Autowired
	LinkmanMapper linkmanMapper;
	public Linkman addLinkman(Linkman linkman) {
		linkman.setId(UUID.randomUUID().toString());
		linkmanMapper.addLinkman(linkman);
		return linkman;
	}
	public int updateLinkman(Linkman linkman) {
		return linkmanMapper.updateLinkman(linkman);
	}
}
