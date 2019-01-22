package com.fic.service.service.impl;

import com.fic.service.Enum.BroadcastTypeEnum;
import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Enum.PriceTypeEnum;
import com.fic.service.Vo.BetBroadcastVo;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.entity.BetUser;
import com.fic.service.entity.LuckyRecord;
import com.fic.service.entity.LuckyTurntable;
import com.fic.service.mapper.BetUserMapper;
import com.fic.service.mapper.LuckyRecordMapper;
import com.fic.service.mapper.LuckyTurntableMapper;
import com.fic.service.mapper.UserMapper;
import com.fic.service.service.BroadcastService;
import com.fic.service.utils.RegexUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class BroadcastServiceImpl implements BroadcastService {

    @Autowired
    BetUserMapper betUserMapper;
    @Autowired
    LuckyRecordMapper luckyRecordMapper;
    @Autowired
    LuckyTurntableMapper luckyTurntableMapper;
    @Autowired
    UserMapper userMapper;

    @Override
    public ResponseVo getByType(int type) {
        List<BetBroadcastVo> broadcastVoList = new ArrayList<BetBroadcastVo>();
        if(type == BroadcastTypeEnum.BET.code()){
            /** 竞猜 */
            List<BetUser> lastWinner = betUserMapper.findLastWinner();
            for(BetUser betUser : lastWinner){
                String telephone = userMapper.findTelephoneById(betUser.getUserId());
                BetBroadcastVo broad = new BetBroadcastVo();
                broad.setTelephone(RegexUtil.replaceTelephone(telephone));
                broad.setPrice(betUser.getBingoPrice().setScale(0, BigDecimal.ROUND_DOWN).toString() + "TFC");
                broadcastVoList.add(broad);
            }
        }
        if(type == BroadcastTypeEnum.DRAW.code()){
            /** 抽奖 */
            List<LuckyRecord> luckyRecords = luckyRecordMapper.findLastTen();
            for(LuckyRecord record : luckyRecords){
                BetBroadcastVo broadcastVo = new BetBroadcastVo();
                LuckyTurntable luckyTurntable = luckyTurntableMapper.get(record.getBingoPrice());
                if(null == luckyTurntable){
                    continue;
                }
                if(null == luckyTurntable.getPriceType()){
                    continue;
                }
                if(luckyTurntable.getPriceType() == PriceTypeEnum.FIFTY.code() ||
                        luckyTurntable.getPriceType() == PriceTypeEnum.FIVE_THOUSAND.code() ||
                        luckyTurntable.getPriceType() == PriceTypeEnum.TWO_HUNDRED.code()){
                    broadcastVo.setPrice(luckyTurntable.getAmount().setScale(0,BigDecimal.ROUND_DOWN).toString() + "TFC");
                }else{
                    broadcastVo.setPrice(luckyTurntable.getPriceName());
                }
                String telephone = userMapper.findTelephoneById(record.getUserId());
                if(StringUtils.isEmpty(telephone)){
                    continue;
                }
                broadcastVo.setTelephone(RegexUtil.replaceTelephone(telephone));
                broadcastVoList.add(broadcastVo);
            }
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,broadcastVoList);
    }
}
