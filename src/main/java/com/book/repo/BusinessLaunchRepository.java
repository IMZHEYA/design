package com.book.repo;

import com.book.pojo.BusinessLaunch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessLaunchRepository extends JpaRepository<BusinessLaunch,Integer> {

}
