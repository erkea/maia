package io.vilya.maia.cms.service;

import javax.inject.Inject;

import io.vilya.maia.cms.entity.LoginInfo;
import io.vilya.maia.cms.repository.LoginInfoRepository;
import io.vilya.maia.core.annotation.Service;

/**
 * @author erkea <erkea@vilya.io>
 */
@Service
public class LoginInfoServiceImpl implements LoginInfoService {

	@Inject
	private LoginInfoRepository loginInfoRepository;
	
	@Override
	public LoginInfo getById(Integer id) {
		return loginInfoRepository.selectByPrimaryKey(id);
	}
	
}
