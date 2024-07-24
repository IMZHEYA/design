package com.book.ticket.builder;

import com.book.ticket.product.PersonalTicket;

public class PersonalTicketBuilder extends TicketBuilder<PersonalTicket> {

    private PersonalTicket personalTicket = new PersonalTicket();
    @Override
    public void setCommonInfo(String title, String product, String content) {
        personalTicket.setTitle(title);
        personalTicket.setProduct(product);
        personalTicket.setContent(content);
    }

    @Override
    public PersonalTicket buildTicket() {
        return personalTicket;
    }
}
