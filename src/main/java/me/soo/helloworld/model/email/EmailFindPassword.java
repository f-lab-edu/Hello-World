package me.soo.helloworld.model.email;

public class EmailFindPassword extends EmailBase {

    public EmailFindPassword(String newPassword) {
        this.title = "<Hello World> 회원님의 임시비밀번호 안내입니다.";
        this.body = writeBody(newPassword);
    }

    String writeBody(String newPassword) {
        StringBuilder body = new StringBuilder();
        body.append("회원님의 임시 비밀번호는 ")
                .append(newPassword)
                .append("입니다. 로그인 후 비밀번호를 다시 변경해 주세요.");
        return String.valueOf(body);
    }
}
