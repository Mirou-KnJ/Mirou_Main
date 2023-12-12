package com.knj.mirou.boundedContext.notification.controller;

import com.knj.mirou.base.rq.Rq;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.notification.model.entity.Notification;
import com.knj.mirou.boundedContext.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/noti")
public class NotificationController {

    private final Rq rq;
    private final NotificationService notificationService;

    @GetMapping("/list")
    public String showList(Model model) {

        Member member = rq.getMember();

        List<Notification> notifications = notificationService.getMy20Notifications(member);
        notificationService.updateRead(notifications);

        model.addAttribute("notifications", notifications);

        return "view/notification/list";
    }

}
