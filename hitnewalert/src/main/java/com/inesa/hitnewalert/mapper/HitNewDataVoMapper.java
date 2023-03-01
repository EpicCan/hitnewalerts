package com.inesa.hitnewalert.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inesa.hitnewalert.entity.HitNewDataVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper//表明这是一个Mapper，也可以在启动类上加上包扫描
public interface HitNewDataVoMapper extends BaseMapper<HitNewDataVo> {
}
//继承BaseMapper可以省略xml的编写
