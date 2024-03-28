package com.teknokote.cm.http;

import com.teknokote.cm.core.model.EnumActivityType;
import com.teknokote.cm.core.model.User;
import com.teknokote.cm.core.service.UserHistoryService;
import com.teknokote.cm.dto.UserHistoryDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;
import java.util.Objects;

@Component
public class InterceptLog implements HandlerInterceptor {

    @Autowired
    private UserHistoryService userHistoryService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Long userId=null;
        if(Objects.nonNull(request.getUserPrincipal())) {
            userId=((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        }

        UserHistoryDto userHistoryDto= UserHistoryDto.builder()
           .userId(userId)
           .ipAddress(request.getRemoteAddr())
           .requestURI(request.getRequestURI())
           .activityDate(LocalDateTime.now())
           .activityType(EnumActivityType.of(request.getMethod()))
           .requestHandler(handler.toString())
           .build();

        userHistoryService.create(userHistoryDto);

        return true;
    }
}
