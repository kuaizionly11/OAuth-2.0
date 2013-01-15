package com.nimbusds.oauth2.sdk;


import net.jcip.annotations.Immutable;

import net.minidev.json.JSONObject;

import com.nimbusds.oauth2.sdk.http.CommonContentTypes;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;

import com.nimbusds.oauth2.sdk.util.JSONObjectUtils;


/**
 * Access token response. This class is immutable.
 *
 * <p>Example HTTP response:
 *
 * <pre>
 * HTTP/1.1 200 OK
 * Content-Type: application/json;charset=UTF-8
 * Cache-Control: no-store
 * Pragma: no-cache
 *
 * {
 *   "access_token"      : "2YotnFZFEjr1zCsicMWpAA",
 *   "token_type"        : "example",
 *   "expires_in"        : 3600,
 *   "refresh_token"     : "tGzv3JOkF0XG5Qx2TlKWIA",
 *   "example_parameter" : "example_value"
 * }
 * </pre>
 *
 * <p>Related specifications:
 *
 * <ul>
 *     <li>OAuth 2.0 (RFC 6749), sections 4.1.4, 4.3.3,  4.4.3 and 5.1.
 * </ul>
 *
 * @author Vladimir Dzhuvinov
 * @version $version$ (2013-01-15)
 */
@Immutable
public class AccessTokenResponse implements SuccessResponse {


	/**
	 * The access token.
	 */
	private final AccessToken accessToken;
	
	
	/**
	 * Optional refresh token.
	 */
	private final RefreshToken refreshToken;
	
	
	/**
	 * Creates a new access token response.
	 *
	 * @param accessToken  The access token. Must not be {@code null}.
	 * @param refreshToken Optional refresh token, {@code null} if none.
	 */
	public AccessTokenResponse(final AccessToken accessToken,
	                           final RefreshToken refreshToken) {
				   
		if (accessToken == null)
			throw new IllegalArgumentException("The access token must not be null");
		
		this.accessToken = accessToken;
		
		this.refreshToken = refreshToken;
	}
	
	
	/**
	 * Gets the access token.
	 *
	 * @return The access token.
	 */
	public AccessToken getAccessToken() {
	
		return accessToken;
	}
	
	
	/**
	 * Gets the optional refresh token.
	 *
	 * @return The refresh token, {@code null} if none.
	 */
	public RefreshToken getRefreshToken() {
	
		return refreshToken;
	}
	
	
	/**
	 * Returns the JSON object representing this access token response.
	 *
	 * <p>Example JSON object:
	 *
	 * <pre>
	 * {
	 *   "access_token" : "SlAV32hkKG",
	 *   "token_type"   : "Bearer",
	 *   "refresh_token": "8xLOxBtZp8",
	 *   "expires_in"   : 3600
	 * }
	 * </pre>
	 *
	 * @return The JSON object.
	 *
	 * @throws SerializeException If this access token response couldn't be
	 *                            serialised to a JSON object.
	 */
	public JSONObject toJSONObject()
		throws SerializeException {
	
		JSONObject o = new JSONObject();
		
		o.put("access_token", accessToken.getValue());
		o.put("token_type", AccessToken.TYPE);
		
		if (accessToken.getLifetime() > 0)
			o.put("expires_in", accessToken.getLifetime());
		
		if (refreshToken != null)
			o.put("refresh_token", refreshToken.getValue());
		
		return o;
	}
	
	
	@Override
	public HTTPResponse toHTTPResponse()
		throws SerializeException {
	
		HTTPResponse httpResponse = new HTTPResponse(HTTPResponse.SC_OK);
		
		httpResponse.setContentType(CommonContentTypes.APPLICATION_JSON);
		httpResponse.setCacheControl("no-store");
		httpResponse.setPragma("no-cache");
		
		httpResponse.setContent(toJSONObject().toString());
		
		return httpResponse;
	}
	
	
	/**
	 * Parses an access token response from the specified JSON object.
	 *
	 * @param jsonObject The JSON object to parse. Must not be {@code null}.
	 *
	 * @return The access token response.
	 *
	 * @throws ParseException If the JSON object couldn't be parsed to a
	 *                        valid access token response.
	 */
	public static AccessTokenResponse parse(final JSONObject jsonObject)
		throws ParseException {
		
		String tokenType = JSONObjectUtils.getString(jsonObject, "token_type");
		
		if (! tokenType.equals(AccessToken.TYPE))
			throw new ParseException("The access token type must be \"" + AccessToken.TYPE + "\"");
		
		String accessTokenValue = JSONObjectUtils.getString(jsonObject, "access_token");
		
		long exp = 0;
		
		if (jsonObject.containsKey("expires_in"))
			exp = JSONObjectUtils.getLong(jsonObject, "expires_in");
		
		
		AccessToken accessToken = new AccessToken(accessTokenValue, exp, null);
		
		
		RefreshToken refreshToken = null;
		
		if (jsonObject.containsKey("refresh_token"))
			refreshToken = new RefreshToken(JSONObjectUtils.getString(jsonObject, "refresh_token"));
		
		return new AccessTokenResponse(accessToken, refreshToken);
	}
	
	
	/**
	 * Parses an access token response from the specified HTTP response.
	 *
	 * @param httpResponse The HTTP response. Must not be {@code null}.
	 *
	 * @return The access token response.
	 *
	 * @throws ParseException If the HTTP response couldn't be parsed to a 
	 *                        valid access token response.
	 */
	public static AccessTokenResponse parse(final HTTPResponse httpResponse)
		throws ParseException {
		
		httpResponse.ensureStatusCode(HTTPResponse.SC_OK);
		
		JSONObject jsonObject = httpResponse.getContentAsJSONObject();
		
		return parse(jsonObject);
	}
}
