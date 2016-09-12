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

package com.nimbusds.oauth2.sdk.pkce;


import junit.framework.TestCase;


/**
 * Code challenge test.
 */
public class CodeChallengeTest extends TestCase {
	

	public void testComputePlain() {

		CodeVerifier verifier = new CodeVerifier();

		CodeChallenge challenge = CodeChallenge.compute(CodeChallengeMethod.PLAIN, verifier);

		assertEquals(verifier.getValue(), challenge.getValue());
	}


	public void testS256() {
		// see https://tools.ietf.org/html/rfc7636#appendix-A

		CodeVerifier verifier = new CodeVerifier("dBjftJeZ4CVP-mB92K27uhbUJU1p1r_wW1gFWFOEjXk");

		CodeChallenge challenge = CodeChallenge.compute(CodeChallengeMethod.S256, verifier);

		assertEquals("E9Melhoa2OwvFrEMTJguCHaoeK1t8URWbuGJSstw-cM", challenge.getValue());
	}


	public void testUnsupportedMethod() {

		try {
			CodeChallenge.compute(new CodeChallengeMethod("S512"), new CodeVerifier());
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Unsupported code challenge method: S512", e.getMessage());
		}
	}
}
