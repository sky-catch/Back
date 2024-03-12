package com.example.core.web.security.login;

import com.example.api.owner.OwnerMapper;
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
public class LoginOwnerArgumentResolver implements HandlerMethodArgumentResolver {

    private final OwnerMapper ownerMapper;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(LoginOwner.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory)
            throws Exception {

        LoginOwner loginOwnerAnnotation = methodParameter.getParameterAnnotation(LoginOwner.class);
        if (!loginOwnerAnnotation.required()) {
            return null;
        }

        String email = getAuthenticatedUserEmail();
        return ownerMapper.findByEmail(email)
                .orElseThrow(() -> new SystemException("존재하지 않는 사장입니다."));
    }

    private String getAuthenticatedUserEmail() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }
}
