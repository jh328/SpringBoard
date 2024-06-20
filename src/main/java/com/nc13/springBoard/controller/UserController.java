package com.nc13.springBoard.controller;

import com.nc13.springBoard.model.UserDTO;
import com.nc13.springBoard.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller // 크롬에서 주소를 입력을 할 때 http://loco호스트 유저/~~~ 모든 유저로 시작 하는 애들은
// 유저 컨트롤러 로 오게 만들고 싶은거임 그러면, 물론
// 겟 맵핑, / 슬러시 맵핑 주소 이걸 다 붙쳐줘도 되지만 편하게 사용을 하고 싶으면
// 이렇게 적어주기 --> 앳 리퀘스트맵핑 슬러시 유저 슬러시
@RequestMapping("/user/")
@RequiredArgsConstructor
public class UserController {
    // 실제 SQL 통신을 담당할  Service 객체
    @Autowired
    private UserService userService;


    @PostMapping("auth")
    // 사용자가 로그인을 할 시 실행할 auth 메소드를 만들어줄꺼다.
    // (여기서 질문 auth 메소드는 어떤 맵핑을 만들어주어야 할까?
    // post 맵핑 jsp 보면 메소드에 post 라고 했기 때문에 그렇다.
    // Post 혹은 GET 방식으로 웹페이지의 값을 받아올 때에는
    // 파라미터에 해당 form 의 name 어트리뷰트와 같은 이름을 가진
    // 파라미터를 적어주면 된다.
    // 또한, 해당 name 어트리뷰트를 필드를 가진 클래스 객체를 파라미터롤 잡아주면
    // 자동으로 데이터가 바인딩 된다.

    public String auth(UserDTO userDTO, HttpSession session) {
        UserDTO result = userService.auth(userDTO);
        if (result != null) { // 이게 로그인 성공, 이전에는 세션에 저장을 했지.
            session.setAttribute("logIn", result);
            return "redirect:/board/showAll";
        }// 에러가 나오는 이유는 jsp 에서 세션은 알아서 기본적으로 포함을 시켜놓음. 근데 애는 자바잖아. 임포트를 해야한다.
        // 아무거나 임포트 하면 될까? 당연히 안된다. 우리 스프링 프로젝트 내장 톰캣을 불러와야 한다.


        // 만약 우리가 해당 메소드를 실행 시키고 나서 특정 URL 로 이동시킬때에는 다음과 같이 적어준다.
        return "redirect:/"; //이 슬러시는 이동할 URL 이 된다. 슬러시만 적으면 로컬호스트 슬러시 끝 이렇게 되는거다.
        // 리 다이렉트는 유알엘을 강제로 보낼 때 사용하는거다. 이해 됨?
    }


    @GetMapping("register")
    public String showRegister() {
        return "user/register";
    }

    @PostMapping("register")
    public String register(UserDTO userDTO, RedirectAttributes redirectAttributes) {
        if (userService.validateUsername(userDTO)) {
            userService.register(userDTO);
        } else {
            // 회원가입 실패 메시지 전송
            // 회원가입 실패시, 우리가 URL을 / error 라는 곳으로 전송을 해주되
            // 해당 페이지에 무슨 에러인지 알 수 있도록
            // 메시지 내용을 여기서 담아서 보낸다.
            // 만약 다른 URL로 이동을 할 때 어떠한 값을 보내주어야 하는 경우
            // RedirectAttributes 라는 것을 사용한다.
            redirectAttributes.addFlashAttribute("message", "중복된 아이디로는 가입하실 수 없습니다.");

            return "redirect:/showMessage";
        }

        return "redirect:/";
    }

}
