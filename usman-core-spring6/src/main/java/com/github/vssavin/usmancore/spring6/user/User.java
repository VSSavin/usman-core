package com.github.vssavin.usmancore.spring6.user;

import com.github.vssavin.usmancore.spring6.event.Event;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

/**
 * Base user management entity.
 *
 * @author vssavin on 06.12.2023.
 */
@Entity
@Table(name = "users")
public class User implements UserDetails {

    public static final int EXPIRATION_DAYS = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String login;

    private String name;

    private String password;

    private String email;

    private String authority;

    @Column(name = "expiration_date")
    private Date expirationDate;

    @Column(name = "verification_id")
    private String verificationId;

    @Column(name = "account_locked")
    private int accountLocked = 0;

    @Column(name = "credentials_expired")
    private int credentialsExpired = 0;

    private int enabled = 1;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final List<Event> events = new ArrayList<>();

    public User(String login, String name, String password, String email, String authority) {
        this.login = login;
        this.name = name;
        this.password = password;
        this.email = email;
        this.authority = authority;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, EXPIRATION_DAYS);
        expirationDate = calendar.getTime();
        verificationId = UUID.randomUUID().toString();
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(authority));
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return expirationDate.after(new Date());
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountLocked == 0;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsExpired == 0;
    }

    @Override
    public boolean isEnabled() {
        return enabled != 0;
    }

    public String getEmail() {
        return email;
    }

    public String getAuthority() {
        return authority;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public String getVerificationId() {
        return verificationId;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setVerificationId(String verificationId) {
        this.verificationId = verificationId;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked ? 1 : 0;
    }

    public void setCredentialsExpired(boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired ? 1 : 0;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled ? 1 : 0;
    }

    public void setEvents(List<Event> events) {
        this.events.retainAll(events);
        this.events.addAll(events);
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static final class UserBuilder {

        private Long id;

        private String login;

        private String name;

        private String password;

        private String email;

        private String authority;

        private Date expirationDate;

        private String verificationId;

        private boolean accountLocked = false;

        private boolean credentialsExpired = false;

        private boolean enabled = true;

        private UserBuilder() {
        }

        public UserBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserBuilder login(String login) {
            this.login = login;
            return this;
        }

        public UserBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder authority(String authority) {
            this.authority = authority;
            return this;
        }

        public UserBuilder expirationDate(Date expirationDate) {
            this.expirationDate = expirationDate;
            return this;
        }

        public UserBuilder verificationId(String verificationId) {
            this.verificationId = verificationId;
            return this;
        }

        public UserBuilder accountLocked(boolean accountLocked) {
            this.accountLocked = accountLocked;
            return this;
        }

        public UserBuilder credentialsExpired(boolean credentialsExpired) {
            this.credentialsExpired = credentialsExpired;
            return this;
        }

        public UserBuilder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public User build() {
            User user = new User();
            user.id = id;
            user.login = login;
            user.name = name;
            user.password = password;
            user.email = email;
            user.authority = authority;
            user.expirationDate = expirationDate;
            user.verificationId = verificationId;
            user.setAccountLocked(accountLocked);
            user.setCredentialsExpired(credentialsExpired);
            user.setEnabled(enabled);
            return user;
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return login.equals(user.login) && email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, email);
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", login='" + login + '\'' + ", name='" + name + '\'' + ", email='" + email + '\''
                + ", authority='" + authority + '\'' + ", expiration_date=" + expirationDate + ", verification_id='"
                + verificationId + '\'' + ", accountLocked=" + accountLocked + ", credentialsExpired="
                + credentialsExpired + ", enabled=" + enabled + '}';
    }

}
