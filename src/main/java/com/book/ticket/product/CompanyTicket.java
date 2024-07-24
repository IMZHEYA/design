package com.book.ticket.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyTicket implements Cloneable {
    private String finalInfo;
    private String title;
    private String taxId;
    private String bankInfo;
    private String product;
    private String content;

    @Override
    protected CompanyTicket clone(){
        CompanyTicket companyTicket = null;
        try {
            companyTicket = (CompanyTicket) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return companyTicket;
     }
}
