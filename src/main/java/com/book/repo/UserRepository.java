package com.book.repo;

import com.book.pojo.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserInfo,Integer> {
    //根据用户名查询用户信息
    UserInfo findByUserName(String userName);

    //根据用户和密码查询用户信息
    UserInfo findByUserNameAndUserPassword(String account,String password);
}
