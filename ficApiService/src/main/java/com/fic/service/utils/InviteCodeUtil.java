package com.fic.service.utils;

import java.util.Random;

/**
 *   @Author Xie
 *   @Date 2018/11/26
 *   @Discription:邀请码
**/
public class InviteCodeUtil {

    private static final char[] r=new char[]{'q', 'w', 'e', '8', 'a', 's', '2', 'd', 'z', 'x', '9', 'c', '7', 'p', '5', 'i', 'k', '3', 'm', 'j', 'u', 'f', 'r', '4', 'v', 'y', 'l', 't', 'n', '6', 'b', 'g', 'h'};

    private static final char B='o';

    private static final int BINLEN=r.length;

    private static final int S=6;

//    public static void main(String[] args) {
//        System.out.println(InviteCodeUtil.toSerialCode(50l));
//    }

    public static String toSerialCode(long id) {
        char[] buf=new char[32];
        int charPos=32;

        while((id / BINLEN) > 0) {
            int ind=(int)(id % BINLEN);
            buf[--charPos]=r[ind];
            id /= BINLEN;
        }
        buf[--charPos]=r[(int)(id % BINLEN)];
        String str=new String(buf, charPos, (32 - charPos));
        if(str.length() < S) {
            StringBuilder sb=new StringBuilder();
            sb.append(B);
            Random rnd=new Random();
            for(int i=1; i < S - str.length(); i++) {
                sb.append(r[rnd.nextInt(BINLEN)]);
            }
            str+=sb.toString();
        }
        return str.toUpperCase();
    }

    public static long codeToId(String code) {
        char chs[]=code.toCharArray();
        long res=0L;
        for(int i=0; i < chs.length; i++) {
            int ind=0;
            for(int j=0; j < BINLEN; j++) {
                if(chs[i] == r[j]) {
                    ind=j;
                    break;
                }
            }
            if(chs[i] == B) {
                break;
            }
            if(i > 0) {
                res=res * BINLEN + ind;
            } else {
                res=ind;
            }
        }
        return res;
    }

}
