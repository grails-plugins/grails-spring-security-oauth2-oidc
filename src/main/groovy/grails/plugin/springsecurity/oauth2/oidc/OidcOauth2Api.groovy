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

import com.github.scribejava.core.builder.api.DefaultApi20
import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor
import com.github.scribejava.core.extractors.TokenExtractor
import com.github.scribejava.core.model.OAuth2AccessToken

class OidcOauth2Api extends DefaultApi20 {
    
    final static String PROVIDER_ID = 'oidc'
    
    final String domain

    OidcOauth2Api(String domain) {
        this.domain = domain.endsWith('/') ? domain[0..-2] : domain
    }

    @Override
    String getAccessTokenEndpoint() {
        return "$domain/oauth/token"
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "$domain/authorize"
    }

    @Override
    TokenExtractor<OAuth2AccessToken> getAccessTokenExtractor() {
        return OAuth2AccessTokenJsonExtractor.instance()
    }
}
