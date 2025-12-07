package com.ruralsync.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruralsync.sys.entity.Official;
import com.ruralsync.sys.mapper.OfficialMapper;
import com.ruralsync.sys.service.OfficialService;
import org.springframework.stereotype.Service;

@Service
public class OfficialServiceImpl extends ServiceImpl<OfficialMapper, Official> implements OfficialService {
}
