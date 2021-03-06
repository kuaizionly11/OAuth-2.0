/*
 * oauth2-oidc-sdk
 *
 * Copyright 2012-2016, Connect2id Ltd and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of the
 * License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.nimbusds.oauth2.sdk;


import java.util.Map;


/**
 * Authorisation grant. Extending classes should be immutable.
 *
 * <p>Supported authorisation grant types:
 *
 * <ul>
 *     <li>{@link GrantType#AUTHORIZATION_CODE Authorisation code}
 *     <li>{@link GrantType#PASSWORD Resource owner password credentials}
 *     <li>{@link GrantType#CLIENT_CREDENTIALS Client credentials}
 *     <li>{@link GrantType#REFRESH_TOKEN Refresh token}
 *     <li>{@link GrantType#JWT_BEARER}
 *     <li>{@link GrantType#SAML2_BEARER}
 * </ul>
 *
 * <p>Related specifications:
 *
 * <ul>
 *     <li>OAuth 2.0 (RFC 6749), sections 1.3.
 * </ul>
 */
public abstract class AuthorizationGrant {


	/**
	 * The authorisation grant type.
	 */
	private final GrantType type;


	/**
	 * Creates a new authorisation grant.
	 *
	 * @param type               The authorisation grant type. Must not be
	 *                           {@code null}.
	 */
	protected AuthorizationGrant(final GrantType type) {

		if (type == null)
			throw new IllegalArgumentException("The grant type must not be null");

		this.type = type;
	}


	/**
	 * Gets the authorisation grant type.
	 *
	 * @return The authorisation grant type.
	 */
	public GrantType getType() {

		return type;
	}


	/**
	 * Return the parameters for the authorisation grant.
	 *
	 * @return The parameters.
	 */
	public abstract Map<String,String> toParameters();


	/**
	 * Parses an authorisation grant from the specified parameters.
	 *
	 * @param params The parameters. Must not be {@code null}.
	 *
	 * @return The authorisation grant.
	 *
	 * @throws ParseException If parsing failed or the grant type is not
	 *                        supported.
	 */
	public static AuthorizationGrant parse(final Map<String,String> params)
		throws ParseException {

		// Parse grant type
		String grantTypeString = params.get("grant_type");

		if (grantTypeString == null)
			throw new ParseException("Missing \"grant_type\" parameter", OAuth2Error.INVALID_REQUEST);

		GrantType grantType = GrantType.parse(grantTypeString);

		if (grantType.equals(GrantType.AUTHORIZATION_CODE)) {

			return AuthorizationCodeGrant.parse(params);

		} else if (grantType.equals(GrantType.REFRESH_TOKEN)) {

			return RefreshTokenGrant.parse(params);
			
		} else if (grantType.equals(GrantType.PASSWORD)) {

			return ResourceOwnerPasswordCredentialsGrant.parse(params);

		} else if (grantType.equals(GrantType.CLIENT_CREDENTIALS)) {

			return ClientCredentialsGrant.parse(params);

		} else if (grantType.equals(GrantType.JWT_BEARER)) {

			return JWTBearerGrant.parse(params);

		} else if (grantType.equals(GrantType.SAML2_BEARER)) {

			return SAML2BearerGrant.parse(params);

		} else {

			throw new ParseException("Invalid or unsupported grant type: " + grantType, OAuth2Error.UNSUPPORTED_GRANT_TYPE);
		}
	}
}
