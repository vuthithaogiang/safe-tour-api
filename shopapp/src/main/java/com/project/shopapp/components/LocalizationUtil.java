package com.project.shopapp.components;

import com.project.shopapp.utils.WebUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;


@RequiredArgsConstructor
@Component
public class LocalizationUtil {

    private final MessageSource messageSource;

    private final LocaleResolver localeResolver;

    public String getLocalizationMessage(String messageKey, Object ... params){ //spread operator
        HttpServletRequest request = WebUtil.getCurrentRequest();
        Locale locale = localeResolver.resolveLocale(request);
        return messageSource.getMessage(messageKey, params, locale);
    }
}
