package com.knj.mirou.boundedContext.member.model.entity;

import com.knj.mirou.base.entity.BaseEntity;
import com.knj.mirou.boundedContext.coin.entity.Coin;
import com.knj.mirou.boundedContext.inventory.entity.Inventory;
import com.knj.mirou.boundedContext.member.model.enums.MemberRole;
import com.knj.mirou.boundedContext.member.model.enums.SocialCode;
import com.knj.mirou.boundedContext.point.entity.Point;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
public class Member extends BaseEntity {

    @Column(unique = true)
    private String loginId;

    @Column(unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private SocialCode socialCode;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    private String inviteCode;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    private Coin coin;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    private Point point;

    @OneToMany(mappedBy = "owner")
    private List<Inventory> inventory;


    public List<? extends GrantedAuthority> getGrantedAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        // 모든 회원에게 member 권한 부여
        grantedAuthorities.add(new SimpleGrantedAuthority("member"));

        // 관리자에게는 admin 권한 부여
        if (isAdmin()) {
            grantedAuthorities.add(new SimpleGrantedAuthority("admin"));
        }

        return grantedAuthorities;
    }

    public boolean isAdmin() {
        return "admin".equals(nickname);
    }

}
