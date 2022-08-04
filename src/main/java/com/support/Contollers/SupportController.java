package com.support.Contollers;

import java.security.Principal;
import java.util.Optional;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.support.Entitis.Resever;
import com.support.Entitis.Ticket;
import com.support.Entitis.User;

import com.support.Enums.Status;
import com.support.Services.BreakDownService;
import com.support.Services.MachineService;
import com.support.Services.TicketService;
import com.support.Services.UserService;
import com.support.Utils.Utils;




@Controller
public class SupportController {
    //Servises Injection
    @Autowired
    TicketService TicketService;
    @Autowired 
    BreakDownService BreakDownService;
    @Autowired
    MachineService MachineService;
    @Autowired
    UserService UserService;

    @GetMapping("/Support/OpenSupport")
    public String OpenSupport(Ticket ticket,Model model) {
        model.addAttribute("BreakDowns",BreakDownService.FindAll() );
        return "OpenSupport";
    }

    @GetMapping("/Support/CloseSupport/{id}")
    public String CloseSupportPage(@PathVariable("id") String id,Model model) {
        model.addAttribute("Tid", id);
        return "CloseSupport";
    }


 
    @PostMapping("/Support/TakeSupport/{id}")
    public String TakeSupport(@PathVariable("id") String id,Principal principal, Model model) {
        Optional<Ticket> t= TicketService.FindById(id);
        if(t.isEmpty()){return "index";}
        t.get().setTecnesstion((Resever) UserService.findByUsername(principal.getName()));        
        t.get().setStatus(Status.InProgress);
        TicketService.CloseTicket(t.get());

        return "redirect:/";
    }



    @PostMapping("/Support/DropeSupport/{id}")
    public String DropeSupport(@PathVariable("id") String id,Principal principal, Model model) {
        Optional<Ticket> t= TicketService.FindById(id);
        if(t.isEmpty()){System.out.println("not found"); return "index";}
        if((t.get().getTecnesstion().getUsername()==principal.getName())){ System.out.println("Not Yours");;return "index";};

       
        TicketService.DropTicket(t.get());
        

        return "redirect:/";
    }



    @PostMapping("/Support/CloseSupport")
    public String CloseSupport(@RequestParam(name = "DESCREPTION") String DESCREPTION,@RequestParam(name = "ID") String id ,@RequestParam(name = "OBSERVATION") String OBSERVATION, Model model) {
        Optional<Ticket> t= TicketService.FindById(id);
        System.out.println(id);
        if(t.isEmpty()){ return "index";}
        
        t.get().setObservetion(OBSERVATION);
        t.get().setDescrption(DESCREPTION);
        t.get().setCloseDate(Utils.CurrentDate());
        t.get().setStatus(Status.Closed);
        TicketService.CloseTicket(t.get());
        return "index";
    }

    @GetMapping("/Support/ListSupport")
    public String ListSupport(Model model,Principal principal) {
        model.addAttribute("ticketList",TicketService.FindAll(PageRequest.of(0,7)) );
        model.addAttribute("username",principal.getName() );
        return "ListSupport";
    }

    @GetMapping
    public String Index(Principal principal,Model model){
        return "Index";
    }

    @PostMapping("/Support/OpenSupport")
    String  OpenTicket( @Valid Ticket ticket,BindingResult result,Principal principal ,Errors errors, Model model){
        model.addAttribute("BreakDowns",BreakDownService.FindAll() );

        System.out.println(errors);
        System.out.println(errors.getErrorCount());

        if (null != errors && errors.getErrorCount() > 0) { return "OpenSupport";}
   


        ticket.setStatus(Status.Open);
        ticket.setIssueDate(Utils.CurrentDate());
        ticket.setUser(UserService.findByUsername(principal.getName()));       
        TicketService.CloseTicket(ticket);
        return "index";
    }

 
}
