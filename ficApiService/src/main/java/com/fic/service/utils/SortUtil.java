package com.fic.service.utils;

import com.fic.service.Vo.BetRankingVo;

import java.util.List;

/**
 *   @Author Xie
 *   @Date 2019/1/22
 *   @Discription:
**/
public class SortUtil {

    /**
     * 用于竞猜排行排序
     * @param rankingVos  win rate
     * @param low
     * @param high
     */
    public static void quickSortForRanking(List<BetRankingVo> rankingVos, int low, int high){
        if(low < high) {
            int middle = getMiddleForRanking(rankingVos,low,high);
            quickSortForRanking(rankingVos, low, middle-1);
            quickSortForRanking(rankingVos, middle+1, high);
        }
    }
    public static int getMiddleForRanking(List<BetRankingVo> rankingVos, int low,int high){
        BetRankingVo temp = rankingVos.get(low);
        while(low < high)
        {
            while(low < high && rankingVos.get(high).getWinRate().compareTo(temp.getWinRate()) < 0)
            {
                high--;
            }
            rankingVos.set(low,rankingVos.get(high));
            while(low < high && rankingVos.get(low).getWinRate().compareTo(temp.getWinRate()) >= 0)
            {
                low++;
            }
            rankingVos.set(high,rankingVos.get(low));
        }
        rankingVos.set(low,temp);
        return low ;
    }

}
