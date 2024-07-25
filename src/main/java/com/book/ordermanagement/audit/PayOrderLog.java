package com.book.ordermanagement.audit;

import java.util.HashMap;

public class PayOrderLog extends AbstractAuditLogProcessor {
    @Override
    protected OrderAuditLog buildDetails(OrderAuditLog auditLog) {
        //新增支付类型和实际支付金额
        HashMap<String,String> extraLog = new HashMap<>();
        extraLog.put("patType","支付宝");
        extraLog.put("price","100");
        auditLog.setDetails(extraLog);
        return auditLog;
    }
}
