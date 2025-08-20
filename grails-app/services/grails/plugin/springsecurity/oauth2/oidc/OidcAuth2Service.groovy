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

import com.auth0.jwk.JwkProviderBuilder
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.interfaces.RSAKeyProvider
import com.github.scribejava.core.builder.ServiceBuilder
import com.github.scribejava.core.builder.api.DefaultApi20
import com.github.scribejava.core.model.OAuth2AccessToken
import com.github.scribejava.core.oauth.OAuth20Service
import grails.plugin.springsecurity.oauth2.SpringSecurityOauth2BaseService
import grails.plugin.springsecurity.oauth2.exception.OAuth2Exception
import grails.plugin.springsecurity.oauth2.service.OAuth2AbstractProviderService
import grails.plugin.springsecurity.oauth2.token.OAuth2SpringToken
import grails.plugin.springsecurity.oauth2.util.OAuth2ProviderConfiguration
import groovy.json.JsonSlurper
import groovy.transform.CompileStatic
import org.springframework.beans.factory.InitializingBean

import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.concurrent.TimeUnit

@CompileStatic
class OidcAuth2Service extends OAuth2AbstractProviderService implements InitializingBean{

    SpringSecurityOauth2BaseService springSecurityOauth2BaseService
    
    OidcOauth2Config OidcOauth2Config
    
    final String providerID = OidcOauth2Api.PROVIDER_ID
    final Class apiClass = OidcOauth2Api
    final String scopeSeparator = ' '
    private JWTVerifier jwtVerifier 
    
    String getProfileScope() {
        "${OidcOauth2Config.domain}/userinfo"
    }
    String getScopes() {
        OidcOauth2Config.scope
    }
    

    @Override
    OAuth2SpringToken createSpringAuthToken(OAuth2AccessToken accessToken) {
        DecodedJWT decoded = decodeJWT(accessToken)
        verifyJWT(decoded)
        return new OidcOauth2SpringToken(accessToken, decoded.claims)
    }

    DecodedJWT decodeJWT(OAuth2AccessToken accessToken) {
        def raw = new JsonSlurper().parseText(accessToken.rawResponse as String) as Map
        String idToken = raw?.id_token as String
        if (!idToken) throw new IllegalStateException("Missing id_token from OIDC response")

        DecodedJWT decoded = new JWT().decodeJwt(idToken)
        return decoded
    }

    @Override
    OAuth20Service buildScribeService(OAuth2ProviderConfiguration providerConfiguration) {
        DefaultApi20 api = new OidcOauth2Api(OidcOauth2Config.domain)

        return new ServiceBuilder(OidcOauth2Config.apiKey)
                .apiSecret(OidcOauth2Config.apiSecret)
                .defaultScope(OidcOauth2Config.scope)
                .callback(providerConfiguration.callbackUrl)
                .build(api)
    }

    
    @Override
    void afterPropertiesSet() throws Exception {
        def jwkProvider = new JwkProviderBuilder(OidcOauth2Config.domain)
                .cached(10, 24, TimeUnit.HOURS)
                .rateLimited(10, 1, TimeUnit.MINUTES)
                .build()
        
        RSAKeyProvider keyProvider = new RSAKeyProvider() {
            @Override
            RSAPublicKey getPublicKeyById(String kid) {
                return (RSAPublicKey) jwkProvider.get(kid).getPublicKey()
            }

            @Override
            RSAPrivateKey getPrivateKey() {
                // return the private key used 
                return null
            }

            @Override
            String getPrivateKeyId() {
                return null
            }
        }

        def algorithm = Algorithm.RSA256(keyProvider)
        String issuerDomain = OidcOauth2Config.domain.endsWith('/') ? OidcOauth2Config.domain : OidcOauth2Config.domain + '/'
        jwtVerifier = JWT.require(algorithm)
                .withIssuer(issuerDomain)
                .withAnyOfAudience(OidcOauth2Config.apiKey)
                .build()
    }


    private void verifyJWT(DecodedJWT decoded) {
        try {
            jwtVerifier.verify(decoded)
        } catch (JWTVerificationException e) {
            throw new OAuth2Exception("OIDC token is invalid", e)
        }
    }

}
