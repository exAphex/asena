package com.asena.scimgateway.config;

import java.security.Principal;

import com.asena.scimgateway.model.User;
import com.asena.scimgateway.security.AUserPrincipal;
import com.asena.scimgateway.security.stereotypes.CurrentUser;

import org.springframework.security.core.Authentication;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class CurrentUserResolver implements
		HandlerMethodArgumentResolver {

	public boolean supportsParameter(MethodParameter methodParameter) {
		return methodParameter.getParameterAnnotation(CurrentUser.class) != null
				&& methodParameter.getParameterType().equals(User.class);
	}

	public Object resolveArgument(MethodParameter methodParameter,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {
		if (this.supportsParameter(methodParameter)) {
            Principal principal = (Principal) webRequest.getUserPrincipal();
            AUserPrincipal ap = (AUserPrincipal) ((Authentication) principal).getPrincipal();
			User u = ap.getUser();
			return u;
		} else {
			return WebArgumentResolver.UNRESOLVED;
		}
	}
}