package com.knj.mirou.boundedContext.member.controller;

import com.knj.mirou.base.rq.Rq;
import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challenge.service.ChallengeService;
import com.knj.mirou.boundedContext.challengemember.model.enums.Progress;
import com.knj.mirou.boundedContext.challengemember.service.ChallengeMemberService;
import com.knj.mirou.boundedContext.inventory.entity.Inventory;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.member.service.MemberService;
import com.knj.mirou.boundedContext.product.model.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final Rq rq;
    private final MemberService memberService;
    private final ChallengeMemberService challengeMemberService;
    private final ChallengeService challengeService;

    @GetMapping("/login")
    public String showLoginPage(){

        return "view/member/login";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myPage")
    public String showMyPage(Principal principal, Model model){

        String loginId = principal.getName();
        Optional<Member> ObyLoginId = memberService.getByLoginId(loginId);

        if(ObyLoginId.isPresent()) {
            Member member = ObyLoginId.get();
            int inProgressNum = challengeMemberService
                    .getCountByLinkedMemberAndProgress(member, Progress.IN_PROGRESS);
            int endProgressNum = challengeMemberService
                    .getCountByLinkedMemberAndProgress(member, Progress.PROGRESS_END);
            model.addAttribute("member", member);
            model.addAttribute("inProgressNum", inProgressNum);
            model.addAttribute("endProgressNum", endProgressNum);
        } else {
            //FIXME 에러페이지 렌더링?
            return null;
        }

        return "view/member/mypage";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin")
    public String adminPage(Model model) {

        List<Challenge> challengeList = challengeService.getAllList();
        model.addAttribute("challengeList", challengeList);

        return "view/admin/adminPage";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/inventory")
    public String showInventory(Model model) {

        Member member = rq.getMember();

        List<Inventory> inventories = member.getInventory();

        List<Product> products = new ArrayList<>();

        for(Inventory inventory : inventories) {
            products.add(inventory.getProduct());
        }

        model.addAttribute("products", products);


        return "view/member/inventory";
    }

}
