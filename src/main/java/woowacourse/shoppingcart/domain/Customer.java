package woowacourse.shoppingcart.domain;

import woowacourse.global.exception.InvalidCustomerException;

import java.util.regex.Pattern;

public class Customer {

    private static final String EMAIL_REGEX = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
    private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,20}$";
    private static final String NICKNAME_REGEX = "^*[a-zA-Z0-9]{1,10}$";

    private Long id;
    private final String email;
    private final String password;
    private final String nickname;

    public Customer(String email, String password, String nickname) {
        this(null, email, password, nickname);
    }

    public Customer(Long id, String email, String password, String nickname) {
        validateCustomer(email, password, nickname);
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    private void validateCustomer(String email, String password, String nickname) {
        validateEmail(email);
        validatePassword(password);
        validateNickname(nickname);
    }

    private void validateEmail(String email) {
        if (email.isBlank() || !Pattern.matches(EMAIL_REGEX, email)) {
            throw new InvalidCustomerException("[ERROR] 이메일 기본 형식에 어긋납니다.");
        }
    }

    public void validatePassword(String password) {
        if (password.isBlank() || !Pattern.matches(PASSWORD_REGEX, password)) {
            throw new InvalidCustomerException("[ERROR] 패스워드 기본 형식에 어긋납니다.");
        }
    }

    public void validateNickname(String nickname) {
        if (nickname.isBlank() || !Pattern.matches(NICKNAME_REGEX, nickname)) {
            throw new InvalidCustomerException("[ERROR] 닉네임 기본 형식에 어긋납니다.");
        }
    }

    public void equalPrevPassword(String prePassword) {
        if (!password.equals(prePassword)) {
            throw new InvalidCustomerException("[ERROR] 이전 비밀번호와 일치하지 않습니다.");
        }
    }

    public void nonEqualNewPassword(String newPassword) {
        if (password.equals(newPassword)) {
            throw new InvalidCustomerException("[ERROR] 새로운 비밀번호가 이전 비밀번호와 동일합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
