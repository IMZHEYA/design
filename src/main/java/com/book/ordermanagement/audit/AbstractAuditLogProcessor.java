package com.book.ordermanagement.audit;

import java.util.Date;

public abstract class AbstractAuditLogProcessor {
    //基本方法
    private final OrderAuditLog basicAuditLog(String account,String action,String orderId){
        OrderAuditLog auditLog = new OrderAuditLog();
        auditLog.setAccount(account);
        auditLog.setAction(action);
        auditLog.setDate(new Date());
        auditLog.setOrderId(orderId);
        return auditLog;
    }
    //抽象模版方法
    protected abstract OrderAuditLog buildDetails(OrderAuditLog auditLog);


    //订单审计日志的创建步骤
    public final OrderAuditLog createAuditLog(String account,String action,String orderId){
        OrderAuditLog orderAuditLog = basicAuditLog(account,action,orderId);
        return buildDetails(orderAuditLog);
    }
}
