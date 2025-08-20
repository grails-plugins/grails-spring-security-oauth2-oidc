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

import grails.plugin.springsecurity.ReflectionUtils
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.oauth2.SpringSecurityOauth2BaseService
import grails.plugin.springsecurity.oauth2.exception.OAuth2Exception
import grails.plugins.Plugin
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Component

@Slf4j
class GrailsSpringSecurityOauth2OidcGrailsPlugin extends Plugin {

    def grailsVersion = "7.0.0-SNAPSHOT > *"

    def title = "Grails Spring Security Oauth2 OIDC Client"
    def author = "Søren Berg Glasius"
    def authorEmail = "soeren@glasius.dk"
    def description = 'Provider to add OIDC support to a Grails application, using the Grails Spring Security Oauth2 plugin'
    def documentation = "https://github.com/grails-plugins/grails-spring-security-oauth2-oidc"
    def license = "APACHE"
    def developers = [[name: "Søren Berg Glasius", github: "sbglasius"]]
    def issueManagement = [system: "GitHub", url: "https://github.com/grails-plugins/grails-spring-security-oauth2-oidc/issues"]
    def scm = [url: 'https://github.com/grails-plugins/grails-spring-security-oauth2-oidc']

    Closure doWithSpring() {
        { ->
            ReflectionUtils.application = grailsApplication
            if (grailsApplication.warDeployed) {
                SpringSecurityUtils.resetSecurityConfig()
            }
            SpringSecurityUtils.application = grailsApplication

            // Check if there is an SpringSecurity configuration
            def coreConf = SpringSecurityUtils.securityConfig
            boolean printStatusMessages = (coreConf.printStatusMessages instanceof Boolean) ? coreConf.printStatusMessages : true
            if (!coreConf || !coreConf.active) {
                if (printStatusMessages) {
                    println('ERROR: There is no SpringSecurity configuration or SpringSecurity is disabled')
                    println('       Stopping configuration of SpringSecurity Oauth2')
                }
                return
            }
            xmlns context: 'http://www.springframework.org/schema/context'

            context.'component-scan'('base-package': 'grails.plugin.springsecurity.oauth2.oidc') {
                context.'include-filter'(
                        type: 'annotation',
                        expression: Component.canonicalName)
            }

            if (printStatusMessages) {
                println('Configuring Spring Security OAuth2 OIDC plugin...')
            }
            SpringSecurityUtils.loadSecondaryConfig('DefaultOAuth2OidcConfig')
            if (printStatusMessages) {
                println('... finished configuring Spring Security OAuth2 OIDC\n')
            }

        }
    }

    void doWithApplicationContext() {
        log.trace("doWithApplicationContext")
        SpringSecurityOauth2BaseService oAuth2BaseService = grailsApplication.mainContext.getBean(SpringSecurityOauth2BaseService)
        OidcAuth2Service oidcAuth2Service = grailsApplication.mainContext.getBean(OidcAuth2Service)
        try {
            oAuth2BaseService.registerProvider(oidcAuth2Service)
        } catch (OAuth2Exception exception) {
            log.error('OAuth2 OIDC not loaded', exception)
        }
    }

}
