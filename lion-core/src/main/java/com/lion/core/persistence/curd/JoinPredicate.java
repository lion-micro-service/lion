package com.lion.core.persistence.curd;

import com.lion.core.persistence.JpqlParameter;
import com.lion.core.persistence.entity.BaseEntity;
import lombok.Data;

import java.util.List;

/**
 * @Author Mr.Liu
 * @Description //TODO
 * @Date 2021/5/12 上午11:13
 **/
@Data
public class JoinPredicate {

    public BaseEntity entity1;

    public BaseEntity entity2;

    private List<JpqlParameter> where;
}
