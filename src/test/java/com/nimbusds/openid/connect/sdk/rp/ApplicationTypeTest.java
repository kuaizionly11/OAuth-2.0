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

package com.nimbusds.openid.connect.sdk.rp;


import com.nimbusds.openid.connect.sdk.rp.ApplicationType;
import junit.framework.TestCase;


/**
 * Tests the application type enumeration.
 */
public class ApplicationTypeTest extends TestCase {


	public void testIdentifiers() {

		assertEquals("web", ApplicationType.WEB.toString());
		assertEquals("native", ApplicationType.NATIVE.toString());
	}


	public void testDefault() {

		assertEquals(ApplicationType.WEB, ApplicationType.getDefault());
	}
}
