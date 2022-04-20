package com.example.backend.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@ControllerAdvice("com.example.backend")
public class GlobalControllerAdvice {

    private final HashMap<ReturnCode, String> redirectMap = new HashMap<>();

    // NOTE : @PostConstruct는 객체의 초기화 부분.
    //  객체가 생성된 후 별도의 초기화 작업을 위해 실행하는 메소드
    //  @PostConstruct 를 설정해놓은 init 메소드는 WAS가 띄워질 때 실행
    @PostConstruct
    private void postContruct() {
        redirectMap.put(ReturnCode.USER_EXIST_USING_THIS_EMAIL, "/"); //TODO: 리다이렉트 URL 다시 정하기
    }

    // FIXME: 프론트 리액트니까 MVC 쓸 수 없음..
    @ExceptionHandler({com.example.backend.exception.BackendException.class})
    public ModelAndView handleBackendException(com.example.backend.exception.BackendException e){
        ModelAndView mav = new ModelAndView();
        String redirectURL = redirectMap.get(e.getReturnCode());
        mav.setViewName("/errors/error");
        mav.addObject("errorCode", e.getReturnCode().getMessage());
        mav.addObject("redirectURL", redirectURL);
        return mav;
    }
}
