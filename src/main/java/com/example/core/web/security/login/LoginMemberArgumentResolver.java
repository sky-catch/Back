package com.example.core.web.security.login;

import com.example.api.member.MemberDTO;
import com.example.api.member.MemberMapper;
import com.example.api.member.UsersMapper;
import com.example.api.owner.OwnerMapper;
import com.example.api.owner.dto.Owner;
import com.example.core.exception.SystemException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

// filter 처리 후 진입
@Slf4j
@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final Map<Class<?>, UsersMapper<?>> usersMapperMap;

    public LoginMemberArgumentResolver(MemberMapper memberMapper, OwnerMapper ownerMapper) {
        this.usersMapperMap = new HashMap<>();
        usersMapperMap.put(MemberDTO.class, memberMapper);
        usersMapperMap.put(Owner.class, ownerMapper);
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory)
            throws Exception {

        if (isNotAuthenticated()) {
            throw new SystemException("토큰 인증에 실패하였습니다.");
        }

        LoginMember loginMemberAnnotation = methodParameter.getParameterAnnotation(LoginMember.class);
        if (!loginMemberAnnotation.required()) {
            return null;
        }

        Class<?> parameterType = methodParameter.getParameterType();

        log.info("parameterType = {}", parameterType);

        String email = getAuthenticatedUserEmail();
        return usersMapperMap.get(parameterType).findByEmail(email)
                .orElseThrow(() -> new SystemException("존재하지 않는 사용자입니다."));
    }

    private boolean isNotAuthenticated() {
        return !SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
    }

    private String getAuthenticatedUserEmail() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }
}
