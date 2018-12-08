package com.hong.service;

import com.hong.entity.User;
import com.hong.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by John on 2018/12/8.
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User getUserById(Long id){
        return userMapper.selectById(id);
    }
}
