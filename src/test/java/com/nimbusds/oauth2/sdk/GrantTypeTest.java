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


import junit.framework.TestCase;


/**
 * Tests the grant type class.
 */
public class GrantTypeTest extends TestCase {


	public void testConstants() {

		assertEquals("authorization_code", GrantType.AUTHORIZATION_CODE.toString());
		assertEquals("implicit", GrantType.IMPLICIT.toString());
		assertEquals("refresh_token", GrantType.REFRESH_TOKEN.toString());
		assertEquals("password", GrantType.PASSWORD.toString());
		assertEquals("client_credentials", GrantType.CLIENT_CREDENTIALS.toString());
		assertEquals("urn:ietf:params:oauth:grant-type:jwt-bearer", GrantType.JWT_BEARER.toString());
		assertEquals("urn:ietf:params:oauth:grant-type:saml2-bearer", GrantType.SAML2_BEARER.toString());
	}


	public void testClientAuthRequirement() {

		assertFalse(GrantType.AUTHORIZATION_CODE.requiresClientAuthentication());
		assertFalse(GrantType.IMPLICIT.requiresClientAuthentication());
		assertFalse(GrantType.REFRESH_TOKEN.requiresClientAuthentication());
		assertFalse(GrantType.PASSWORD.requiresClientAuthentication());
		assertTrue(GrantType.CLIENT_CREDENTIALS.requiresClientAuthentication());
		assertFalse(GrantType.JWT_BEARER.requiresClientAuthentication());
		assertFalse(GrantType.SAML2_BEARER.requiresClientAuthentication());
	}


	public void testClientIDRequirement() {

		assertTrue(GrantType.AUTHORIZATION_CODE.requiresClientID());
		assertTrue(GrantType.IMPLICIT.requiresClientID());
		assertFalse(GrantType.REFRESH_TOKEN.requiresClientID());
		assertFalse(GrantType.PASSWORD.requiresClientID());
		assertTrue(GrantType.CLIENT_CREDENTIALS.requiresClientID());
		assertFalse(GrantType.JWT_BEARER.requiresClientID());
		assertFalse(GrantType.SAML2_BEARER.requiresClientID());
	}


	public void testRequestParameters() {

		assertTrue(GrantType.AUTHORIZATION_CODE.getRequestParameterNames().contains("code"));
		assertTrue(GrantType.AUTHORIZATION_CODE.getRequestParameterNames().contains("redirect_uri"));
		assertTrue(GrantType.AUTHORIZATION_CODE.getRequestParameterNames().contains("code_verifier"));
		assertEquals(3, GrantType.AUTHORIZATION_CODE.getRequestParameterNames().size());

		assertTrue(GrantType.IMPLICIT.getRequestParameterNames().isEmpty());

		assertTrue(GrantType.PASSWORD.getRequestParameterNames().contains("username"));
		assertTrue(GrantType.PASSWORD.getRequestParameterNames().contains("password"));
		assertEquals(2, GrantType.PASSWORD.getRequestParameterNames().size());

		assertTrue(GrantType.CLIENT_CREDENTIALS.getRequestParameterNames().isEmpty());

		assertTrue(GrantType.JWT_BEARER.getRequestParameterNames().contains("assertion"));
		assertEquals(1, GrantType.JWT_BEARER.getRequestParameterNames().size());

		assertTrue(GrantType.SAML2_BEARER.getRequestParameterNames().contains("assertion"));
		assertEquals(1, GrantType.SAML2_BEARER.getRequestParameterNames().size());
	}


	public void testDefaultConstructor() {

		GrantType grantType = new GrantType("custom");
		assertEquals("custom", grantType.getValue());
		assertFalse(grantType.requiresClientAuthentication());
		assertFalse(grantType.requiresClientID());
	}


	public void testParseStandard()
		throws ParseException {

		assertEquals(GrantType.AUTHORIZATION_CODE, GrantType.parse(GrantType.AUTHORIZATION_CODE.getValue()));
		assertEquals(GrantType.IMPLICIT, GrantType.parse(GrantType.IMPLICIT.getValue()));
		assertEquals(GrantType.REFRESH_TOKEN, GrantType.parse(GrantType.REFRESH_TOKEN.getValue()));
		assertEquals(GrantType.PASSWORD, GrantType.parse(GrantType.PASSWORD.getValue()));
		assertEquals(GrantType.CLIENT_CREDENTIALS, GrantType.parse(GrantType.CLIENT_CREDENTIALS.getValue()));
		assertEquals(GrantType.JWT_BEARER, GrantType.parse(GrantType.JWT_BEARER.getValue()));
		assertEquals(GrantType.SAML2_BEARER, GrantType.parse(GrantType.SAML2_BEARER.getValue()));
	}


	public void testParseCustomGrant()
		throws ParseException {

		GrantType grantType = GrantType.parse("custom");

		assertEquals("custom", grantType.getValue());
		assertFalse(grantType.requiresClientAuthentication());
		assertFalse(grantType.requiresClientID());
		assertTrue(grantType.getRequestParameterNames().isEmpty());
	}


	public void testParseNull() {

		try {
			GrantType.parse(null);
			fail();

		} catch (ParseException e) {
			// ok
		}
	}


	public void testParseEmpty() {

		try {
			GrantType.parse("");
			fail();

		} catch (ParseException e) {
			// ok
		}
	}


	public void testParseBlank() {

		try {
			GrantType.parse(" ");
			fail();

		} catch (ParseException e) {
			// ok
		}
	}
}
