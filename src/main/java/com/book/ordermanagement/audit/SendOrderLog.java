package com.book.ordermanagement.audit;

import java.util.HashMap;

public class SendOrderLog extends AbstractAuditLogProcessor {
    @Override
    protected OrderAuditLog buildDetails(OrderAuditLog auditLog) {
        //增加快递公司信息和快递编号
        HashMap<String,String> extraLog = new HashMap<>();
        extraLog.put("快递公司","顺丰快递");
        extraLog.put("快递编号","100");
        auditLog.setDetails(extraLog);
        return auditLog;
    }
}
