/* Copyright 2025-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package grails.plugin.springsecurity.oauth2.oidc

import com.auth0.jwt.interfaces.Claim
import com.github.scribejava.core.model.OAuth2AccessToken
import grails.plugin.springsecurity.oauth2.token.OAuth2SpringToken
import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors

@InheritConstructors
@CompileStatic
class OidcOauth2SpringToken extends OAuth2SpringToken {
    final String providerName = OidcOauth2Api.PROVIDER_ID
    Map<String, Claim> claims

    OidcOauth2SpringToken(OAuth2AccessToken accessToken, Map<String, Claim> claims) {
        super(accessToken)
        this.claims = claims
    }
    
    OAuth2AccessToken getAccessToken() {
        super.accessToken
    }

    @Override
    String getSocialId() {
        return claims.sub.asString() ?: ''
    }

    @Override
    String getScreenName() {
        return claims.email.asString() ?: ''
    }
}
