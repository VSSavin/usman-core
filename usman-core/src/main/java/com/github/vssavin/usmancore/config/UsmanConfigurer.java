package com.github.vssavin.usmancore.config;

import com.github.vssavin.usmancore.security.SecureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Configures user management params.
 *
 * @author vssavin on 05.12.2023.
 */
public class UsmanConfigurer {

    private static final Logger log = LoggerFactory.getLogger(UsmanConfigurer.class);

    private static final int TWO_WEEKS_S = 1209600;

    private String loginPageTitle = "";

    private String applicationUrl = "http://127.0.0.1:8085";

    private SecureService secureService;

    private Pattern passwordPattern;

    private UsmanAuthPasswordConfig passwordConfig;

    private String passwordPatternErrorMessage = "Wrong password!";

    private List<AuthorizedUrlPermission> permissions = new ArrayList<>();

    private final Map<String, String[]> resourceHandlers = new HashMap<>();

    private final UsmanUrlsConfigurer urlsConfigurer;

    private final OAuth2Config oAuth2Config;

    private final List<PermissionPathsContainer> permissionPathsContainerList;

    private boolean csrfEnabled = true;

    private boolean configured = false;

    private boolean registrationAllowed = true;

    private int maxAuthFailureCount = 3;

    private int authFailureBlockTimeMinutes = 60;

    private int rememberMeTokenValiditySeconds = TWO_WEEKS_S;

    private int csrfTokenValiditySeconds = TWO_WEEKS_S;

    public UsmanConfigurer() {
        this.urlsConfigurer = new UsmanUrlsConfigurer();
        this.oAuth2Config = new OAuth2Config();
        this.permissionPathsContainerList = Collections.emptyList();
    }

    public UsmanConfigurer(UsmanUrlsConfigurer urlsConfigurer, OAuth2Config oAuth2Config,
            List<PermissionPathsContainer> permissionPathsContainerList) {
        this.urlsConfigurer = urlsConfigurer;
        this.oAuth2Config = oAuth2Config;
        this.permissionPathsContainerList = permissionPathsContainerList;
    }

    public UsmanConfigurer loginPageTitle(String loginPageTitle) {
        checkAccess();
        this.loginPageTitle = loginPageTitle;
        return this;
    }

    public UsmanConfigurer applicationUrl(String applicationUrl) {
        checkAccess();
        this.applicationUrl = applicationUrl;
        return this;
    }

    public UsmanConfigurer secureService(SecureService secureService) {
        checkAccess();
        this.secureService = secureService;
        return this;
    }

    public UsmanConfigurer passwordPatternErrorMessage(String passwordPatternErrorMessage) {
        checkAccess();
        this.passwordPatternErrorMessage = passwordPatternErrorMessage;
        return this;
    }

    public UsmanConfigurer permissions(List<AuthorizedUrlPermission> permissions) {
        checkAccess();
        this.permissions = permissions;
        return this;
    }

    public UsmanConfigurer permission(AuthorizedUrlPermission permission) {
        checkAccess();
        this.permissions.add(permission);
        return this;
    }

    public UsmanConfigurer csrf(boolean enabled) {
        checkAccess();
        this.csrfEnabled = enabled;
        return this;
    }

    public UsmanConfigurer registrationAllowed(boolean registrationAllowed) {
        checkAccess();
        this.registrationAllowed = registrationAllowed;
        return this;
    }

    public UsmanConfigurer maxAuthFailureCount(int maxAuthFailureCount) {
        checkAccess();
        this.maxAuthFailureCount = maxAuthFailureCount;
        return this;
    }

    public UsmanConfigurer authFailureBlockTimeMinutes(int authFailureBlockTimeMinutes) {
        checkAccess();
        this.authFailureBlockTimeMinutes = authFailureBlockTimeMinutes;
        return this;
    }

    public UsmanConfigurer rememberMeTokenValiditySeconds(int rememberMeTokenValiditySeconds) {
        checkAccess();
        this.rememberMeTokenValiditySeconds = rememberMeTokenValiditySeconds;
        return this;
    }

    public UsmanConfigurer csrfTokenValiditySeconds(int csrfTokenValiditySeconds) {
        checkAccess();
        this.csrfTokenValiditySeconds = csrfTokenValiditySeconds;
        return this;
    }

    public UsmanConfigurer resourceHandlers(Map<String, String[]> resourceHandlers) {
        checkAccess();
        resourceHandlers.forEach((handler, locations) -> {
            String[] existsLocations = this.resourceHandlers.get(handler);
            if (existsLocations != null) {
                String[] newLocations = Arrays.copyOf(existsLocations, existsLocations.length + locations.length);
                System.arraycopy(locations, 0, newLocations, existsLocations.length, locations.length);
                this.resourceHandlers.put(handler, newLocations);
            }
            else {
                this.resourceHandlers.put(handler, locations);
            }
        });
        return this;
    }

    public UsmanConfigurer configure() {
        checkAccess();
        initPermissions();
        configurePermissions();
        this.configured = true;
        return this;
    }

    public String getLoginPageTitle() {
        return loginPageTitle;
    }

    public String getApplicationUrl() {
        return applicationUrl;
    }

    public SecureService getSecureService() {
        log.trace("SecureService implementation can be changed after application arguments have been processed!"
                + " You can implement ArgumentsProcessedNotifier interface to receive notification when this happens.");
        return secureService;
    }

    public Pattern getPasswordPattern() {
        if (passwordPattern == null) {
            if (passwordConfig == null) {
                passwordConfig = new UsmanAuthPasswordConfig();
            }
            passwordPattern = initPasswordPattern(passwordConfig);
        }
        return passwordPattern;
    }

    public String getPasswordPatternErrorMessage() {
        return passwordPatternErrorMessage;
    }

    public List<AuthorizedUrlPermission> getPermissions() {
        return permissions;
    }

    public boolean isGoogleAuthAllowed() {
        String clientId = oAuth2Config.getGoogleClientId();
        return clientId != null && !clientId.isEmpty();
    }

    public boolean isRegistrationAllowed() {
        return registrationAllowed;
    }

    public int getMaxAuthFailureCount() {
        return maxAuthFailureCount;
    }

    public int getAuthFailureBlockTimeMinutes() {
        return authFailureBlockTimeMinutes;
    }

    public int getRememberMeTokenValiditySeconds() {
        return rememberMeTokenValiditySeconds;
    }

    public int getCsrfTokenValiditySeconds() {
        return csrfTokenValiditySeconds;
    }

    public UsmanAuthPasswordConfig passwordConfig() {
        this.passwordConfig = new UsmanAuthPasswordConfig();
        return this.passwordConfig;
    }

    public Map<String, String[]> getResourceHandlers() {
        return resourceHandlers;
    }

    public boolean isCsrfEnabled() {
        return csrfEnabled;
    }

    @Override
    public String toString() {
        return "UsmanConfigurer{" + "loginPageTitle='" + loginPageTitle + '\'' + ", applicationUrl='" + applicationUrl
                + '\'' + ", secureService=" + secureService + ", passwordPattern=" + passwordPattern
                + ", passwordConfig=" + passwordConfig + ", passwordPatternErrorMessage='" + passwordPatternErrorMessage
                + '\'' + ", permissions=" + permissions + ", resourceHandlers=" + resourceHandlers + ", csrfEnabled="
                + csrfEnabled + ", configured=" + configured + ", registrationAllowed=" + registrationAllowed
                + ", maxAuthFailureCount=" + maxAuthFailureCount + ", authFailureBlockTimeMinutes="
                + authFailureBlockTimeMinutes + '}';
    }

    void changeSecureService(SecureService secureService) {
        this.secureService = secureService;
    }

    private void checkAccess() {
        if (configured) {
            throw new IllegalStateException("UsmanConfigurer is already configured!");
        }
    }

    private void initPermissions() {
        permissionPathsContainerList.forEach(container -> {
            List<AuthorizedUrlPermission> paths = container.getPermissionPaths(Permission.ANY_USER);
            permissions.addAll(paths);
            paths = container.getPermissionPaths(Permission.ADMIN_ONLY);
            permissions.addAll(paths);
            paths = container.getPermissionPaths(Permission.USER_ADMIN);
            permissions.addAll(paths);
        });
    }

    private void configurePermissions() {
        if (!isRegistrationAllowed()) {
            updatePermission(urlsConfigurer.getRegistrationUrl(), Permission.ADMIN_ONLY);
            updatePermission(urlsConfigurer.getPerformRegisterUrl(), Permission.ADMIN_ONLY);
            updatePermission(urlsConfigurer.getPerformRegisterUrl(), HttpMethod.POST.name(), Permission.ADMIN_ONLY);
        }
    }

    private void updatePermission(String url, Permission permission) {
        int index = getPermissionIndex(url);
        String httpMethod = getPermissionHttpMethod(url);
        if (index != -1) {
            this.permissions.set(index, new AuthorizedUrlPermission(url, httpMethod, permission));
        }
    }

    private void updatePermission(String url, String httpMethod, Permission permission) {
        int index = getPermissionIndex(url);
        if (index != -1) {
            AuthorizedUrlPermission urlPermission = this.permissions.get(index);
            if (urlPermission != null && urlPermission.getHttpMethod().equals(httpMethod)) {
                this.permissions.set(index, new AuthorizedUrlPermission(url, httpMethod, permission));
            }
            else {
                this.permissions.add(new AuthorizedUrlPermission(url, httpMethod, permission));
            }
        }
    }

    private int getPermissionIndex(String url) {
        for (int i = 0; i < this.permissions.size(); i++) {
            AuthorizedUrlPermission authorizedUrlPermission = this.permissions.get(i);
            if (authorizedUrlPermission.getUrl().equals(url)) {
                return i;
            }
        }
        return -1;
    }

    private String getPermissionHttpMethod(String url) {
        for (AuthorizedUrlPermission authorizedUrlPermission : this.permissions) {
            if (authorizedUrlPermission.getUrl().equals(url)) {
                return authorizedUrlPermission.getHttpMethod();
            }
        }
        return AuthorizedUrlPermission.getDefaultHttpMethod();
    }

    private Pattern initPasswordPattern(UsmanAuthPasswordConfig passwordConfig) {
        StringBuilder stringPatternBuilder = new StringBuilder("^");
        if (passwordConfig.isAtLeastOneDigit()) {
            stringPatternBuilder.append("(?=.*[0-9])");
        }

        if (passwordConfig.isAtLeastOneLowerCaseLatin()) {
            stringPatternBuilder.append("(?=.*[a-z])");
        }

        if (passwordConfig.isAtLeastOneUpperCaseLatin()) {
            stringPatternBuilder.append("(?=.*[A-Z])");
        }

        if (passwordConfig.isAtLeastOneSpecialCharacter()) {
            stringPatternBuilder.append("(?=.*[!@#&()–[{}]:;',?/*~$^+=<>])");
        }

        stringPatternBuilder.append(".").append("{").append(passwordConfig.getMinLength()).append(",");

        if (passwordConfig.getMaxLength() != 0) {
            stringPatternBuilder.append(passwordConfig.getMaxLength());
        }

        stringPatternBuilder.append("}$");

        return Pattern.compile(stringPatternBuilder.toString());
    }

}
