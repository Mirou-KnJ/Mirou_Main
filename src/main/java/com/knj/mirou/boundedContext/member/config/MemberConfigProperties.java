package com.knj.mirou.boundedContext.member.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "custom.login")
public class MemberConfigProperties {

    private List<String> adminIdList;

    public boolean isAdmin(String loginId) {

        for(String admin : adminIdList) {

            if(admin.equals(loginId)) {
                return true;
            }
        }

        return false;
    }

}
