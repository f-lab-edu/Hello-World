package me.soo.helloworld.model.email

class Email(val to: String, val title: String, val body: String) {

    companion object {
        fun create(to: String, title: String, body: String) = Email(to, title, body)
    }
}

const val FIND_PASSWORD_TITLE = "임시 비밀번호 안내입니다."

const val FIND_PASSWORD_BODY = "회원님의 임시비밀번호는 다음과 같습니다. 로그인 하신 후 비밀번호를 바로 변경해주세요. \n \n"
