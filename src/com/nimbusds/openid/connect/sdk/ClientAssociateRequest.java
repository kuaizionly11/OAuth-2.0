package com.nimbusds.openid.connect.sdk;


import java.net.URL;

import java.util.Set;

import com.nimbusds.oauth2.sdk.ParseException;

import com.nimbusds.oauth2.sdk.http.HTTPRequest;



/**
 * Client associate (new registration) request.
 *
 * <p>Example HTTP request:
 *
 * <pre>
 * POST /connect/register HTTP/1.1
 * Content-Type: application/x-www-form-urlencoded
 * Host: server.example.com
 * Authorization: Bearer eyJhbGciOiJSUzI1NiJ9.eyJ ... fQ.8Gj_-sj ... _X
 * 
 * type=client_associate
 * &amp;application_type=web
 * &amp;redirect_uris=https://client.example.org/callback
 *     %20https://client.example.org/callback2
 * &amp;application_name=My%20Example%20
 * &amp;application_name%23ja-Jpan-JP=ワタシ用の例
 * &amp;logo_url=https://client.example.org/logo.png
 * &amp;subject_type=pairwise
 * &amp;sector_identifier_url=
 *     https://othercompany.com/file_of_redirect_uris_for_our_sites.js
 * &amp;token_endpoint_auth_type=client_secret_basic
 * &amp;jwk_url=https://client.example.org/my_rsa_public_key.jwks
 * &amp;userinfo_encrypted_response_alg=RSA1_5
 * &amp;userinfo_encrypted_response_enc=A128CBC+HS256
 * </pre>
 *
 * <p>Related specifications:
 *
 * <ul>
 *     <li>OpenID Connect Dynamic Client Registration 1.0, section 2.1.
 * </ul>
 *
 * @author Vladimir Dzhuvinov
 * @version $version$ (2013-01-22)
 */
public class ClientAssociateRequest extends ClientDetailsRequest {


	/**
	 * Creates a new client associate (new registration) request.
	 *
	 * @param redirectURIs The client redirect URIs. The set must not be
	 *                     {@code null} and must include at least one URL.
	 */
	public ClientAssociateRequest(final Set<URL> redirectURIs) {

		super(ClientRegistrationType.CLIENT_ASSOCIATE, redirectURIs);
	}


	/**
	 * Creates a new client associate (new registration) request.
	 *
	 * @param redirectURI The client redirect URI. Must not be 
	 *                    {@code null}.
	 */
	public ClientAssociateRequest(final URL redirectURI) {

		super(ClientRegistrationType.CLIENT_ASSOCIATE, redirectURI);
	}


	/**
	 * Parses a client associate request from the specified HTTP POST
	 * request.
	 *
	 * <p>Example HTTP request (GET):
	 *
	 * <pre>
	 * POST /connect/register HTTP/1.1
	 * Content-Type: application/x-www-form-urlencoded
	 * Host: server.example.com
	 * Authorization: Bearer eyJhbGciOiJSUzI1NiJ9.eyJ ... fQ.8Gj_-sj ... _X
 	 * 
	 * type=client_associate
	 * &amp;application_type=web
	 * &amp;redirect_uris=https://client.example.org/callback
	 *     %20https://client.example.org/callback2
	 * &amp;application_name=My%20Example%20
	 * &amp;application_name%23ja-Jpan-JP=ワタシ用の例
	 * &amp;logo_url=https://client.example.org/logo.png
	 * &amp;subject_type=pairwise
	 * &amp;sector_identifier_url=
	 *     https://othercompany.com/file_of_redirect_uris_for_our_sites.js
	 * &amp;token_endpoint_auth_type=client_secret_basic
	 * &amp;jwk_url=https://client.example.org/my_rsa_public_key.jwks
	 * &amp;userinfo_encrypted_response_alg=RSA1_5
	 * &amp;userinfo_encrypted_response_enc=A128CBC+HS256
	 * </pre>
	 *
	 * @param httpRequest The HTTP request. Must not be {@code null}.
	 *
	 * @return The parsed client associate request.
	 *
	 * @throws ParseException If the HTTP request couldn't be parsed to a 
	 *                        client associate request.
	 */
	public static ClientAssociateRequest parse(final HTTPRequest httpRequest)
		throws ParseException {

		ClientDetailsRequest req = ClientDetailsRequest.parse(httpRequest);

		if (req instanceof ClientAssociateRequest)
			return (ClientAssociateRequest)req;

		else
			throw new ParseException("Invalid \"type\" parameter", OIDCError.INVALID_TYPE);
	}
}