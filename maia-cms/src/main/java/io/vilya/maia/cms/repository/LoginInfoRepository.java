package io.vilya.maia.cms.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import io.vilya.maia.cms.entity.LoginInfo;

/**
 * 
 * @author erkea <erkea@vilya.io>
 *
 */
@Mapper
public interface LoginInfoRepository {

    int deleteByPrimaryKey(Integer id);

    int insert(LoginInfo record);

    int insertSelective(LoginInfo record);

    // @Select("select * from login_info where id = #{id}")
    LoginInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LoginInfo record);

    int updateByPrimaryKey(LoginInfo record);
	
}
