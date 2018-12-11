package com.fic.service.service.impl;

import com.fic.service.entity.AdminLog;
import com.fic.service.entity.AdminUser;
import com.fic.service.mapper.AdminLogMapper;
import com.fic.service.mapper.AdminUserMapper;
import com.fic.service.service.AdminLogService;
import com.fic.service.utils.SecurityUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *   @Author Xie
 *   @Date 2018/12/7
 *   @Discription:
**/
@Service
public class AdminLogServiceImpl implements AdminLogService {

    private final Logger log = LoggerFactory.getLogger(AdminLogServiceImpl.class);

    @Autowired
    AdminUserMapper adminUserMapper;
    @Autowired
    AdminLogMapper adminLogMapper;


    @Override
    public void saveAdminLog(AdminLog adminLog) {
        String username = SecurityUtil.getCurrentUser();
        if(StringUtils.isEmpty(username)){
            log.error(" 获取当前用户失败 当前用户名 空");
            throw new RuntimeException();
        }
        AdminUser user = adminUserMapper.findByUserName(username);
        if(null == user){
            log.error(" 获取当前用户失败 当前用户不存在");
            throw new RuntimeException();
        }
        adminLog.setAdminId(user.getId());
        int saveLogResult = adminLogMapper.insertSelective(adminLog);
        if(saveLogResult <=0){
            log.error(" 保存失败：{}",adminLog.toString());
            throw new RuntimeException();
        }
    }
}
