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

package com.nimbusds.oauth2.sdk.util;


import java.net.URI;
import java.net.URISyntaxException;


/**
 * URI operations.
 */
public class URIUtils {


	/**
	 * Gets the base part (schema, host, port and path) of the specified
	 * URI.
	 *
	 * @param uri The URI. May be {@code null}.
	 *
	 * @return The base part of the URI, {@code null} if the original URI
	 *         is {@code null} or doesn't specify a protocol.
	 */
	public static URI getBaseURI(final URI uri) {

		if (uri == null)
			return null;

		try {
			return new URI(uri.getScheme(), null, uri.getHost(), uri.getPort(), uri.getPath(), null, null);

		} catch (URISyntaxException e) {

			return null;
		}
	}


	/**
	 * Prevents instantiation.
	 */
	private URIUtils() {

		// do nothing
	}
}
