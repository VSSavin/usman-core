package com.github.vssavin.usmancore.config;

/**
 * Provides available roles.
 *
 * @author vssavin on 28.11.2023
 */
public enum Role {

	ROLE_USER, ROLE_ADMIN;

	public static Role getRole(String role) {
		if (role == null) {
			return Role.ROLE_USER;
		}
		return role.toLowerCase().contains("admin") ? Role.ROLE_ADMIN : Role.ROLE_USER;
	}

	public static String getStringRole(Role role) {
		String stringRole = role.toString();
		String[] splitted = stringRole.split("_");
		if (splitted.length > 1) {
			stringRole = splitted[1];
		}
		return stringRole;
	}

}
