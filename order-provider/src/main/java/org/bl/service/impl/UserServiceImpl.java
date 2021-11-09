package org.bl.service.impl;

import org.bl.annotation.BlRemoteService;
import org.bl.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * @Description：create
 * @author: ext.liukai3
 * @date: 2021/11/9 15:26
 */
@BlRemoteService
public class UserServiceImpl implements IUserService {


    @Override
    public String getUserById(Long id) {
        return "i am bail";
    }
}
