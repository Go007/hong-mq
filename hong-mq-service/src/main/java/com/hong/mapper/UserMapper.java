package com.hong.mapper;

import com.hong.entity.User;

public interface UserMapper{
   User selectById(Long id);
}