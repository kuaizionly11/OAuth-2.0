package com.nimbusds.openid.connect.claims;


/**
 * Not-before time claim. The value is number of seconds from 1970-01-01T0:0:0Z 
 * as measured in UTC until the desired date/time.
 *
 * <p>Related specifications:
 *
 * <ul>
 *     <li>draft-jones-oauth-jwt-bearer-04, section 3.
 * </ul>
 *
 * @author Vladimir Dzhuvinov
 * @version $version$ (2012-05-10)
 */
public class NotBeforeTime extends TimeClaim {


	/**
	 * @inheritDoc
	 *
	 * @return "nbf".
	 */
	public String getClaimName() {
	
		return "nbf";
	}
}
