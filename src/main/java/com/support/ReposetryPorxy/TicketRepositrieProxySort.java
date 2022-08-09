package com.support.ReposetryPorxy;

import com.support.Entitis.Ticket;
import com.support.Enums.Status;
import com.support.Repositories.TicketRepositrie;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



@Service
public class TicketRepositrieProxySort {

    @Autowired 
    TicketRepositrie TicketRepositrie;


    public List<Ticket> getList(String Sort,  String Key){
    List<Ticket> Unsorted = TicketRepositrie.findByMachineIdStartingWith(Key);
    if(Sort =="Date"){  
        Collections.sort(Unsorted,new TicketComparatorDate());
    }
    if(Sort=="Status"){
        Collections.sort(Unsorted,new TicketComparatorStatus());
    }if(Sort=="Level"){
        Collections.sort(Unsorted,new TicketComparatorLevel());

    }
    return Unsorted;



    };

}
