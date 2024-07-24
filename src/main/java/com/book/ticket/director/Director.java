package com.book.ticket.director;

import com.book.ticket.builder.CompanyTicketBuilder;
import com.book.ticket.builder.PersonalTicketBuilder;
import org.springframework.stereotype.Component;

@Component
public class Director extends AbstractDirector {
    @Override
    public Object buildTicket(String type, String product, String content, String title, String bankInfo, String taxId) {
        if(type.equals("person")){
            PersonalTicketBuilder personalTicketBuilder = new PersonalTicketBuilder();
            personalTicketBuilder.setCommonInfo(title,product,content);
            return personalTicketBuilder.buildTicket();
        }
        else if (type.equals("company")){
            CompanyTicketBuilder companyTicketBuilder = new CompanyTicketBuilder();
            companyTicketBuilder.setCommonInfo(title,product,content);
            companyTicketBuilder.setTaxId(taxId);
            companyTicketBuilder.setBankInfo(bankInfo);
            return companyTicketBuilder.buildTicket();
        }
        throw new UnsupportedOperationException("不支持的发票类型！");
    }
}
