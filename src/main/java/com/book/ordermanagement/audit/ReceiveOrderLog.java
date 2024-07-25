package com.book.ordermanagement.audit;

public class ReceiveOrderLog extends AbstractAuditLogProcessor {
    @Override
    protected OrderAuditLog buildDetails(OrderAuditLog auditLog) {
        return auditLog;
    }
}
