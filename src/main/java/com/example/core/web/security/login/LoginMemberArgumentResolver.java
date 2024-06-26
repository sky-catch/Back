package com.example.core.web.security.login;

import com.example.api.member.MemberDTO;
import com.example.api.member.MemberExceptionType;
import com.example.api.member.MemberMapper;
import com.example.core.exception.SystemException;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberMapper memberMapper;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory)
            throws Exception {

        LoginMember loginMemberAnnotation = methodParameter.getParameterAnnotation(LoginMember.class);
        if (isNotLoginRequired(loginMemberAnnotation) && isNotLogined()) {
            return MemberDTO.empty();
        }

        String email = getAuthenticatedUserEmail();
        return memberMapper.findByEmail(email)
                .orElseThrow(() -> new SystemException(MemberExceptionType.NOT_FOUND.getMessage()));
    }

    private boolean isNotLoginRequired(LoginMember loginMemberAnnotation) {
        return !loginMemberAnnotation.required();
    }

    private boolean isNotLogined() {
        return !(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetails);
    }

    private String getAuthenticatedUserEmail() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }
}
