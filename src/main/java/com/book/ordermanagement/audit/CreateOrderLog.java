package com.book.ordermanagement.audit;

public class CreateOrderLog extends AbstractAuditLogProcessor {
    @Override
    protected OrderAuditLog buildDetails(OrderAuditLog auditLog) {
        return auditLog;
    }
}
