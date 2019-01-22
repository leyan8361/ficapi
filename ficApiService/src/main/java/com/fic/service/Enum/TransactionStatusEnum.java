package com.fic.service.Enum;

/**
 *   @Author Xie
 *   @Date 2019/1/9
 *   @Discription:
**/
public enum TransactionStatusEnum {

        APPLY(0,"转账申请"),
        REJECT(1,"转账申请被拒绝"),
        WAIT_CONFIRM(2,"转账中"),
        SUCCESS(3,"转账成功"),
        FAILED(4,"转账失败");

        private Integer code;
        private String remark;

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        TransactionStatusEnum(Integer code, String remark) {
            this.code = code;
            this.remark = remark;
        }

}
