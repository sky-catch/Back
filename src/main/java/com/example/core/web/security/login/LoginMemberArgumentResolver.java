package com.example.core.web.security.login;

import com.example.api.member.MemberDTO;
import com.example.api.member.MemberMapper;
import com.example.api.member.UsersMapper;
import com.example.api.owner.OwnerMapper;
import com.example.api.owner.dto.Owner;
import com.example.core.exception.SystemException;
import com.example.core.web.security.jwt.JWTProvider;
import com.example.core.web.security.jwt.JWTUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final JWTProvider jwtProvider;
    private final Map<Class<?>, UsersMapper<?>> usersMapperMap;

    public LoginMemberArgumentResolver(JWTProvider jwtProvider, MemberMapper memberMapper, OwnerMapper ownerMapper) {
        this.jwtProvider = jwtProvider;
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

        LoginMember loginMemberAnnotation = methodParameter.getParameterAnnotation(LoginMember.class);
        if (!loginMemberAnnotation.required()) {
            return null;
        }

        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);

        Class<?> parameterType = methodParameter.getParameterType();

        return Optional.ofNullable(request.getHeader(JWTUtils.AUTHORIZATION_HEADER))
                .map(authorization -> authorization.split(JWTUtils.TOKEN_PREFIX)[1])
                .map(jwtProvider::getMemberEmail)
                .map(email -> usersMapperMap.get(parameterType).findByEmail(email)
                        .orElseThrow(() -> new SystemException("존재하지 않는 사용자입니다.")))
                .orElseThrow(() -> new SystemException("권한이 없습니다."));
    }
}
