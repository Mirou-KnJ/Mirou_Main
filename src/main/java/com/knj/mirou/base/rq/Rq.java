package com.knj.mirou.base.rq;

import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.base.util.Ut;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Date;
import java.util.Locale;

@Component
@RequestScope
public class Rq {
    private final MemberService memberService;
    private final LocaleResolver localeResolver;
    private Locale locale;
    private final HttpServletRequest req;
    private final HttpServletResponse resp;
    private final User user;
    private Member member = null;

    public Rq(MemberService memberService, MessageSource messageSource, LocaleResolver localeResolver, HttpServletRequest req, HttpServletResponse resp, HttpSession session) {
        this.memberService = memberService;
        this.localeResolver = localeResolver;
        this.req = req;
        this.resp = resp;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof User) {
            this.user = (User) authentication.getPrincipal();
        } else {
            this.user = null;
        }
    }

    // 로그인 되어 있는지 체크
    public boolean isLogin() {
        return user != null;
    }

    // 로그아웃 되어 있는지 체크
    public boolean isLogout() {
        return !isLogin();
    }

    // 로그인 된 회원의 객체
    public Member getMember() {
        if (isLogout()) return null;

        if (member == null) {
            member = memberService.getByLoginId(user.getUsername()).orElseThrow();
        }
        return member;
    }

    // 뒤로가기 + 메세지
    public String historyBack(String msg) {
        String referer = req.getHeader("referer");
        String key = "historyBackErrorMsg___" + referer;
        req.setAttribute("localStorageKeyAboutHistoryBackErrorMsg", key);
        req.setAttribute("historyBackErrorMsg", msg); //400 응답코드 지정
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return "common/js/js";
    }

    // 뒤로가기 + 메세지
    public String historyBack(RsData rsData) {
        return historyBack(rsData.getMsg());
    }

    // 302 + 메세지
    public String redirectWithMsg(String url, RsData rsData) {
        return redirectWithMsg(url, rsData.getMsg());
    }

    // 302 + 메세지
    public String redirectWithMsg(String url, String msg) {
        return "redirect:" + urlWithMsg(url, msg);
    }

    private String urlWithMsg(String url, String msg) {
        return Ut.url.modifyQueryParam(url, "msg", msgWithTtl(msg));
    }

    private String msgWithTtl(String msg) {
        return Ut.url.encode(msg) + ";ttl=" + new Date().getTime();
    }

    private Locale getLocale() {
        if (locale == null) locale = localeResolver.resolveLocale(req);

        return locale;
    }
}
