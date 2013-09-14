package com.nimbusds.openid.connect.sdk.claims;


import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import com.nimbusds.jwt.JWTClaimsSet;

import com.nimbusds.oauth2.sdk.id.Audience;
import com.nimbusds.oauth2.sdk.id.Issuer;
import com.nimbusds.oauth2.sdk.id.Subject;
import com.nimbusds.oauth2.sdk.util.DateUtils;
import com.nimbusds.openid.connect.sdk.Nonce;


/**
 * Tests the ID token claims set.
 *
 * @author Vladimir Dzhuvinov
 */
public class IDTokenClaimsSetTest extends TestCase {


	public void testParseRoundTrip()
		throws Exception {

		// Example from messages spec

		String json = "{\n" +
			"   \"iss\"       : \"https://server.example.com\",\n" +
			"   \"sub\"       : \"24400320\",\n" +
			"   \"aud\"       : \"s6BhdRkqt3\",\n" +
			"   \"nonce\"     : \"n-0S6_WzA2Mj\",\n" +
			"   \"exp\"       : 1311281970,\n" +
			"   \"iat\"       : 1311280970,\n" +
			"   \"auth_time\" : 1311280969,\n" +
			"   \"acr\"       : \"urn:mace:incommon:iap:silver\",\n" +
			"   \"at_hash\"   : \"MTIzNDU2Nzg5MDEyMzQ1Ng\"\n" +
			" }";

		JWTClaimsSet jwtClaimsSet = JWTClaimsSet.parse(json);

		IDTokenClaimsSet idTokenClaimsSet = new IDTokenClaimsSet(jwtClaimsSet);

		assertEquals("https://server.example.com", idTokenClaimsSet.getIssuer().getValue());
		assertEquals("24400320", idTokenClaimsSet.getSubject().getValue());
		assertEquals("s6BhdRkqt3", idTokenClaimsSet.getAudience().get(0).getValue());
		assertEquals("n-0S6_WzA2Mj", idTokenClaimsSet.getNonce().getValue());
		assertEquals(1311281970l, DateUtils.toSecondsSinceEpoch(idTokenClaimsSet.getExpirationTime()));
		assertEquals(1311280970l, DateUtils.toSecondsSinceEpoch(idTokenClaimsSet.getIssueTime()));
		assertEquals(1311280969l, DateUtils.toSecondsSinceEpoch(idTokenClaimsSet.getAuthenticationTime()));
		assertEquals("urn:mace:incommon:iap:silver", idTokenClaimsSet.getACR().getValue());
		assertEquals("MTIzNDU2Nzg5MDEyMzQ1Ng", idTokenClaimsSet.getAccessTokenHash().getValue());

		json = idTokenClaimsSet.toJWTClaimsSet().toJSONObject().toJSONString();

		jwtClaimsSet = JWTClaimsSet.parse(json);

		idTokenClaimsSet = new IDTokenClaimsSet(jwtClaimsSet);

		assertEquals("https://server.example.com", idTokenClaimsSet.getIssuer().getValue());
		assertEquals("24400320", idTokenClaimsSet.getSubject().getValue());
		assertEquals("s6BhdRkqt3", idTokenClaimsSet.getAudience().get(0).getValue());
		assertEquals("n-0S6_WzA2Mj", idTokenClaimsSet.getNonce().getValue());
		assertEquals(1311281970l, DateUtils.toSecondsSinceEpoch(idTokenClaimsSet.getExpirationTime()));
		assertEquals(1311280970l, DateUtils.toSecondsSinceEpoch(idTokenClaimsSet.getIssueTime()));
		assertEquals(1311280969l, DateUtils.toSecondsSinceEpoch(idTokenClaimsSet.getAuthenticationTime()));
		assertEquals("urn:mace:incommon:iap:silver", idTokenClaimsSet.getACR().getValue());
		assertEquals("MTIzNDU2Nzg5MDEyMzQ1Ng", idTokenClaimsSet.getAccessTokenHash().getValue());
	}


	public void testGettersAndSetters()
		throws Exception {

		Issuer issuer = new Issuer("iss");
		Subject subject = new Subject("sub");

		List<Audience> audList = new LinkedList<Audience>();
		audList.add(new Audience("aud"));

		Date expirationTime = DateUtils.fromSecondsSinceEpoch(100000l);
		Date issueTime = DateUtils.fromSecondsSinceEpoch(200000l);

		IDTokenClaimsSet idTokenClaimsSet = new IDTokenClaimsSet(issuer, subject, audList, expirationTime, issueTime);

		Date authenticationTime = DateUtils.fromSecondsSinceEpoch(300000l);
		idTokenClaimsSet.setAuthenticationTime(authenticationTime);

		Nonce nonce = new Nonce();
		idTokenClaimsSet.setNonce(nonce);

		AccessTokenHash accessTokenHash = new AccessTokenHash("123");
		idTokenClaimsSet.setAccessTokenHash(accessTokenHash);

		CodeHash codeHash = new CodeHash("456");
		idTokenClaimsSet.setCodeHash(codeHash);

		ACR acr = new ACR("1");
		idTokenClaimsSet.setACR(acr);

		List<AMR> amrList = new LinkedList<AMR>();
		amrList.add(new AMR("A"));
		idTokenClaimsSet.setAMR(amrList);

		AuthorizedParty authorizedParty = new AuthorizedParty("azp");
		idTokenClaimsSet.setAuthorizedParty(authorizedParty);

		// Mandatory claims
		assertEquals("iss", idTokenClaimsSet.getIssuer().getValue());
		assertEquals("sub", idTokenClaimsSet.getSubject().getValue());
		assertEquals("aud", idTokenClaimsSet.getAudience().get(0).getValue());
		assertEquals(100000l, idTokenClaimsSet.getExpirationTime().getTime() / 1000);
		assertEquals(200000l, idTokenClaimsSet.getIssueTime().getTime() / 1000);

		// Optional claims
		assertEquals(300000l, idTokenClaimsSet.getAuthenticationTime().getTime() / 1000);
		assertEquals(nonce.getValue(), idTokenClaimsSet.getNonce().getValue());
		assertEquals(accessTokenHash.getValue(), idTokenClaimsSet.getAccessTokenHash().getValue());
		assertEquals(codeHash.getValue(), idTokenClaimsSet.getCodeHash().getValue());
		assertEquals(acr.getValue(), idTokenClaimsSet.getACR().getValue());
		assertEquals("A", idTokenClaimsSet.getAMR().get(0).getValue());
		assertEquals(authorizedParty.getValue(), idTokenClaimsSet.getAuthorizedParty().getValue());

		String json = idTokenClaimsSet.toJSONObject().toJSONString();

		idTokenClaimsSet = IDTokenClaimsSet.parse(json);

		// Mandatory claims
		assertEquals("iss", idTokenClaimsSet.getIssuer().getValue());
		assertEquals("sub", idTokenClaimsSet.getSubject().getValue());
		assertEquals("aud", idTokenClaimsSet.getAudience().get(0).getValue());
		assertEquals(100000l, idTokenClaimsSet.getExpirationTime().getTime() / 1000);
		assertEquals(200000l, idTokenClaimsSet.getIssueTime().getTime() / 1000);

		// Optional claims
		assertEquals(300000l, idTokenClaimsSet.getAuthenticationTime().getTime() / 1000);
		assertEquals(nonce.getValue(), idTokenClaimsSet.getNonce().getValue());
		assertEquals(accessTokenHash.getValue(), idTokenClaimsSet.getAccessTokenHash().getValue());
		assertEquals(codeHash.getValue(), idTokenClaimsSet.getCodeHash().getValue());
		assertEquals(acr.getValue(), idTokenClaimsSet.getACR().getValue());
		assertEquals("A", idTokenClaimsSet.getAMR().get(0).getValue());
		assertEquals(authorizedParty.getValue(), idTokenClaimsSet.getAuthorizedParty().getValue());
	}
}
