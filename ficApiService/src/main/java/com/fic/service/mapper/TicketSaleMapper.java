package com.fic.service.mapper;

import com.fic.service.entity.TicketSale;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TicketSaleMapper {

    int deleteByPrimaryKey(Integer ticketSaleId);

    int insert(TicketSale record);

    int insertSelective(TicketSale record);

    TicketSale selectByPrimaryKey(Integer ticketSaleId);

    int updateByPrimaryKeySelective(TicketSale record);

    int updateByPrimaryKey(TicketSale record);
}