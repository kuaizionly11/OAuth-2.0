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

package com.nimbusds.oauth2.sdk.auth.verifier;


import java.security.PublicKey;
import java.util.List;

import com.nimbusds.jose.JWSHeader;
import com.nimbusds.oauth2.sdk.auth.ClientAuthenticationMethod;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.oauth2.sdk.id.ClientID;


/**
 * Selector of client credential candidates for client authentication
 * verification. The select methods should typically return a single candidate,
 * but may also return multiple in case of client credentials key rotation.
 * Implementations should be tread-safe.
 *
 * <p>Selection of {@link com.nimbusds.oauth2.sdk.auth.ClientSecretBasic
 * client_secret_basic}, {@link com.nimbusds.oauth2.sdk.auth.ClientSecretPost
 * client_secret_post} and {@link com.nimbusds.oauth2.sdk.auth.ClientSecretJWT
 * client_secret_jwt} secrets is handled by the {@link #selectClientSecrets}
 * method.
 *
 * <p>Selection of {@link com.nimbusds.oauth2.sdk.auth.PrivateKeyJWT
 * private_key_jwt} keys is handled by the {@link #selectPublicKeys} method.
 *
 * <p>The generic {@link Context context object} may be used to return
 * {@link com.nimbusds.oauth2.sdk.client.ClientMetadata client metadata} or
 * other information to the caller.
 */
public interface ClientCredentialsSelector<T> {


	/**
	 * Selects one or more client secret candidates for
	 * {@link com.nimbusds.oauth2.sdk.auth.ClientSecretBasic client_secret_basic},
	 * {@link com.nimbusds.oauth2.sdk.auth.ClientSecretPost client_secret_post} and
	 * {@link com.nimbusds.oauth2.sdk.auth.ClientSecretJWT client_secret_jwt}
	 * authentication.
	 *
	 * @param claimedClientID The client identifier (to be verified). Not
	 *                        {@code null}.
	 * @param authMethod      The client authentication method. Not
	 *                        {@code null}.
	 * @param context         Additional context. May be {@code null}.
	 *
	 * @return The selected client secret candidates, empty list if none.
	 *
	 * @throws InvalidClientException If the client is invalid.
	 */
	List<Secret> selectClientSecrets(final ClientID claimedClientID,
					 final ClientAuthenticationMethod authMethod,
					 final Context<T> context)
		throws InvalidClientException;


	/**
	 * Selects one or more public key candidates (e.g. RSA or EC) for
	 * {@link com.nimbusds.oauth2.sdk.auth.PrivateKeyJWT private_key_jwt}
	 * authentication.
	 *
	 * @param claimedClientID The client identifier (to be verified). Not
	 *                        {@code null}.
	 * @param authMethod      The client authentication method. Not
	 *                        {@code null}.
	 * @param jwsHeader       The JWS header, which may contain parameters
	 *                        such as key ID to facilitate the key
	 *                        selection. Not {@code null}.
	 * @param forceRefresh    {@code true} to force refresh of the JWK set
	 *                        (for a remote JWK set referenced by URL).
	 * @param context         Additional context. May be {@code null}.
	 *
	 * @return The selected public key candidates, empty list if none.
	 *
	 * @throws InvalidClientException If the client is invalid.
	 */
	List<? extends PublicKey> selectPublicKeys(final ClientID claimedClientID,
						   final ClientAuthenticationMethod authMethod,
						   final JWSHeader jwsHeader,
						   final boolean forceRefresh,
						   final Context<T> context)
		throws InvalidClientException;
}
