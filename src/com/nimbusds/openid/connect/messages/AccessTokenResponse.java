package com.nimbusds.openid.connect.messages;


import net.minidev.json.JSONObject;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;

import com.nimbusds.openid.connect.ParseException;
import com.nimbusds.openid.connect.SerializeException;

import com.nimbusds.openid.connect.http.CommonContentTypes;
import com.nimbusds.openid.connect.http.HTTPResponse;

import com.nimbusds.openid.connect.util.JSONObjectUtils;


/**
 * Access token response.
 *
 * <p>Example HTTP response:
 *
 * <pre>
 * HTTP/1.1 200 OK
 * Content-Type: application/json
 * Cache-Control: no-store
 * Pragma: no-cache
 * 
 * {
 *   "access_token"  : "SlAV32hkKG",
 *   "token_type"    : "Bearer",
 *   "refresh_token" : "8xLOxBtZp8",
 *   "expires_in"    : 3600,
 *   "id_token"      : "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOl
 * wvXC9zZXJ2ZXIuZXhhbXBsZS5jb20iLCJ1c2VyX2lkIjoiMjQ4Mjg5NzYxMDAxIiwiYXVkIj
 * oiaHR0cDpcL1wvY2xpZW50LmV4YW1wbGUuY29tIiwiZXhwIjoxMzExMjgxOTcwfQ.eDesUD0
 * vzDH3T1G3liaTNOrfaeWYjuRCEPNXVtaazNQ"
 * }
 * </pre>
 *
 * <p>Related specifications:
 *
 * <ul>
 *     <li>OpenID Connect Messages 1.0, section 2.2.3.
 *     <li>draft-ietf-oauth-v2-26, section 4.1.4 and 5.1.
 * </ul>
 *
 * @author Vladimir Dzhuvinov
 * @version $version$ (2012-10-21)
 */
public class AccessTokenResponse implements SuccessResponse {


	/**
	 * The access token.
	 */
	private AccessToken accessToken;
	
	
	/**
	 * Optional ID Token serialised to a JWT.
	 */
	private JWT idToken = null;
	
	
	/**
	 * Optional refresh token.
	 */
	private RefreshToken refreshToken = null;
	
	
	/**
	 * Creates a new access token response.
	 *
	 * @param accessToken  The access token. Must not be {@code null}.
	 * @param idToken      The ID token. Must be {@code null} if the
	 *                     request grant type was not 
	 *                     {@link GrantType#AUTHORIZATION_CODE}.
	 * @param refreshToken Optional refresh token, {@code null} if none.
	 */
	public AccessTokenResponse(final AccessToken accessToken,
	                           final JWT idToken,
	                           final RefreshToken refreshToken) {
				   
		if (accessToken == null)
			throw new IllegalArgumentException("The access token must not be null");
		
		this.accessToken = accessToken;
		
		this.idToken = idToken;
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
	 * Gets the ID token.
	 *
	 * @return The ID token, {@code null} if none.
	 */
	public JWT getIDToken() {
	
		return idToken;
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
		
		if (accessToken.getExpiration() > 0)
			o.put("expires_in", accessToken.getExpiration());
		
		if (idToken != null) {
			
			try {
				o.put("id_token", idToken.serialize());

			} catch (IllegalStateException e) {

				throw new SerializeException("Couldn't serialize ID token: " + e.getMessage(), e);
			}
		}
		
		if (refreshToken != null)
			o.put("refresh_token", refreshToken.getValue());
		
		return o;
	}
	
	
	/**
	 * @inheritDoc
	 */
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
		
		long exp = -1;
		
		if (jsonObject.containsKey("expires_in"))
			exp = JSONObjectUtils.getInt(jsonObject, "expires_in");
		
		
		AccessToken accessToken = new AccessToken(accessTokenValue, exp, null);
		
		
		JWT idToken = null;
		
		if (jsonObject.containsKey("id_token")) {
			
			try {
				idToken = JWTParser.parse(JSONObjectUtils.getString(jsonObject, "id_token"));
				
			} catch (java.text.ParseException e) {
			
				throw new ParseException("Couldn't parse ID token: " + e.getMessage(), e);
			}
		}
		
		
		RefreshToken refreshToken = null;
		
		if (jsonObject.containsKey("refresh_token"))
			refreshToken = new RefreshToken(JSONObjectUtils.getString(jsonObject, "refresh_token"));
		
		return new AccessTokenResponse(accessToken, idToken, refreshToken);
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
