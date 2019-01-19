package com.hong.mapper;

import com.hong.entity.DistributedMessage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * Created by John on 2019/1/19.
 */
public interface DistributedMessageMapper {

    int insertOne(DistributedMessage distributedMessage);

    @Update("UPDATE t_distributed_message t " +
            "SET t.`msg_status`=#{msgStatus} " +
            "WHERE t.`msg_id`=#{msgId}")
    int updateStatus(@Param("msgId") String msgId,@Param("msgStatus") Integer msgStatus);

}
