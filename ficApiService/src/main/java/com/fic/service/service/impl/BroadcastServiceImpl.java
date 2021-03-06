package com.fic.service.service.impl;

import com.fic.service.Enum.BroadcastTypeEnum;
import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.Enum.PriceTypeEnum;
import com.fic.service.Vo.BetBroadcastVo;
import com.fic.service.Vo.ResponseVo;
import com.fic.service.constants.Constants;
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
            /** 前5个大礼品*/
            List<LuckyRecord> goodGift = luckyRecordMapper.findGiftCondition(1);
            /** 后5个差礼品*/
            List<LuckyRecord> betGift = luckyRecordMapper.findGiftCondition(2);

            for(LuckyRecord record : goodGift) {
                BetBroadcastVo broadcastVo = new BetBroadcastVo();
                LuckyTurntable luckyTurntable = luckyTurntableMapper.get(record.getBingoPrice());
                if(null == luckyTurntable){
                    continue;
                }
                if(null == luckyTurntable.getPriceType()){
                    continue;
                }
                String telephone = userMapper.findTelephoneById(record.getUserId());
                if(StringUtils.isEmpty(telephone)){
                    continue;
                }
                broadcastVo.setTelephone(RegexUtil.replaceTelephone(telephone));
                if(luckyTurntable.getPriceType() == PriceTypeEnum.FIVE_THOUSAND.code() ||
                                luckyTurntable.getPriceType() == PriceTypeEnum.FIFTY.code() ||
                                luckyTurntable.getPriceType() == PriceTypeEnum.TWO_HUNDRED.code()){
                    broadcastVo.setPrice(luckyTurntable.getAmount().setScale(0,BigDecimal.ROUND_DOWN).toString() + "TFC");
                }else{
                    broadcastVo.setPrice(luckyTurntable.getPriceName());
                }
                broadcastVoList.add(broadcastVo);
            }

            for(LuckyRecord record : betGift) {
                BetBroadcastVo broadcastVo = new BetBroadcastVo();
                LuckyTurntable luckyTurntable = luckyTurntableMapper.get(record.getBingoPrice());
                if(null == luckyTurntable){
                    continue;
                }
                if(null == luckyTurntable.getPriceType()){
                    continue;
                }
                String telephone = userMapper.findTelephoneById(record.getUserId());
                if(StringUtils.isEmpty(telephone)){
                    continue;
                }
                broadcastVo.setTelephone(RegexUtil.replaceTelephone(telephone));
                if(luckyTurntable.getPriceType() == PriceTypeEnum.TEN.code()){
                    broadcastVo.setPrice(luckyTurntable.getAmount().setScale(0,BigDecimal.ROUND_DOWN).toString() + "TFC");
                }
                if(luckyTurntable.getPriceType() == PriceTypeEnum.WORD.code()){
                    String words[] = luckyTurntable.getPriceName().split(Constants.WORDS_CUT);
                    if(words.length >= record.getTrace()){
                        String word = words[record.getTrace()];
                        broadcastVo.setPrice("淘影说：" +word);
                    }
                }
                if(StringUtils.isEmpty(broadcastVo.getPrice())){
                    broadcastVo.setPrice(luckyTurntable.getPriceName());
                }
                if(broadcastVoList.size() < 10){
                    broadcastVoList.add(broadcastVo);
                }else{
                    break;
                }
            }
        }
        return new ResponseVo(ErrorCodeEnum.SUCCESS,broadcastVoList);
    }
}
