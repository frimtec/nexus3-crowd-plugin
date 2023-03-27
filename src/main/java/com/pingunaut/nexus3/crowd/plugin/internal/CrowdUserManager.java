/*
 * Copyright (c) 2010 Sonatype, Inc. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */
package com.pingunaut.nexus3.crowd.plugin.internal;

import com.google.inject.Inject;
import com.pingunaut.nexus3.crowd.plugin.CrowdAuthenticatingRealm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.nexus.security.role.RoleIdentifier;
import org.sonatype.nexus.security.user.*;

import javax.enterprise.inject.Typed;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author justin
 * @author Issa Gorissen
 */
@Singleton
@Typed(UserManager.class)
@Named("Crowd")
public class CrowdUserManager extends AbstractReadOnlyUserManager {

	public static final String SOURCE = "Crowd";
	private static final Logger LOGGER = LoggerFactory.getLogger(CrowdUserManager.class);

	private CachingNexusCrowdClient client;

	@Inject
	public CrowdUserManager(CachingNexusCrowdClient client) {
		LOGGER.info("CrowdUserManager is starting...");
		this.client = client;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAuthenticationRealmName() {
		return CrowdAuthenticatingRealm.NAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getSource() {
		return SOURCE;
	}

	private User completeUserRolesAndSource(User user) {
		return completeUserRolesAndSource(user, client.findRolesByUser(user.getUserId()));
	}

	private User completeUserRolesAndSource(User user, Set<String> roleIds) {
		user.setSource(SOURCE);
		user.setRoles(roleIds.stream().map(r -> new RoleIdentifier(SOURCE, r)).collect(Collectors.toSet()));
		return user;
	}

	private User getUserInternal(String userId) throws UserNotFoundException {
		User u = client.findUserByUsername(userId);
		if(u == null){
			throw new UserNotFoundException(userId);
		}
		return u;
	}

	@Override
	public Set<User> listUsers() {
		return client.findUsers().stream().map(this::completeUserRolesAndSource).collect(Collectors.toSet());
	}

	@Override
	public Set<String> listUserIds() {
		return client.findAllUsernames();
	}

	@Override
	public Set<User> searchUsers(UserSearchCriteria criteria) {
		return client.findUserByCriteria(criteria).stream().map(this::completeUserRolesAndSource).collect(Collectors.toSet());
	}

	@Override
	public User getUser(String userId) throws UserNotFoundException {
		User u = getUserInternal(userId);
		return completeUserRolesAndSource(u);
	}

	@Override
	public User getUser(String userId, Set<String> roleIds) throws UserNotFoundException {
		log.debug("getUser(%d, %d)", userId, roleIds);
		User u = getUserInternal(userId);
		log.debug("getUser({}, {}): {}", userId, roleIds, u);
		if(roleIds == null) {
			return completeUserRolesAndSource(u);
		}
		return completeUserRolesAndSource(u, roleIds);
	}
}
