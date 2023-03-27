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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.nexus.security.authz.AbstractReadOnlyAuthorizationManager;
import org.sonatype.nexus.security.authz.AuthorizationManager;
import org.sonatype.nexus.security.privilege.NoSuchPrivilegeException;
import org.sonatype.nexus.security.privilege.Privilege;
import org.sonatype.nexus.security.role.NoSuchRoleException;
import org.sonatype.nexus.security.role.Role;

import javax.enterprise.inject.Typed;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author justin
 * @author Issa Gorissen
 */
@Singleton
@Typed(AuthorizationManager.class)
@Named("Crowd")
public class CrowdAuthorizationManager extends AbstractReadOnlyAuthorizationManager {

	private CachingNexusCrowdClient client;

	private static final Logger LOGGER = LoggerFactory.getLogger(CrowdAuthorizationManager.class);

	@Inject
	public CrowdAuthorizationManager(CachingNexusCrowdClient client) {
		LOGGER.info("CrowdAuthorizationManager is starting...");
		this.client = client;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Privilege getPrivilege(String privilegeId) throws NoSuchPrivilegeException {
		throw new NoSuchPrivilegeException("Crowd plugin doesn't support privileges");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Role getRole(String roleId) throws NoSuchRoleException {
		Role role = client.findRoleByRoleId(roleId);
		if (role == null) {
			throw new NoSuchRoleException("Failed to get role " + roleId + " from Crowd.");
		} else {
			return role;
		}
	}

	@Override
	public String getSource() {
		return CrowdUserManager.SOURCE;
	}

	@Override
	public Set<Privilege> listPrivileges() {
		return Collections.emptySet();
	}

	@Override
	public Set<Role> listRoles() {
		return client.findRoles();
	}

	@Override
	public Privilege getPrivilegeByName(String privilegeName) throws NoSuchPrivilegeException {
		throw new NoSuchPrivilegeException("Crowd plugin doesn't support privileges");
	}

	@Override
	public List<Privilege> getPrivileges(Set<String> privilegeIds) {
		throw new NoSuchPrivilegeException("Crowd plugin doesn't support privileges");
	}
}
