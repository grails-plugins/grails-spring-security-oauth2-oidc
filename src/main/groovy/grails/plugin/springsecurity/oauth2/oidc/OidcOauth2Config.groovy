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

import grails.plugin.springsecurity.oauth2.util.OAuth2ProviderConfiguration
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = 'grails.plugin.springsecurity.oauth2.providers.oidc')
class OidcOauth2Config extends OAuth2ProviderConfiguration {

    String domain
    String scope = 'openid profile email'
    String scopeSeparator = ' '
    String logoutUri = '/v2/logout'
}
