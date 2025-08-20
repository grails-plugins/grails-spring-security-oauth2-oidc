Spring Security OAuth2 OIDC Plugin
====================================
[ ![Download](https://api.bintray.com/packages/grails/plugins/spring-security-oauth2-google/images/download.svg) ](https://bintray.com/grails/plugins/spring-security-oauth2-google/_latestVersion)

Add aa OIDC OAuth2 provider to the [Spring Security OAuth2 Plugin](https://github.com/apache/grails-/grails-spring-security-oauth2).

Installation
------------
Add the following dependencies in `build.gradle`
```
dependencies {
...
    compile 'org.apache.grails:grails-spring-security-oauth2:7.0.0-M5'
    compile 'org.grails.plugins:spring-security-oauth2-oidc:1.0.+'
...
}
```

Usage
-----
Add this to your application.yml
```
grails:
    plugin:
        springsecurity:
            oauth2:
                providers:
                    oidc:
                        api_key: 'oidc-api-key'               #needed
                        api_secret: 'oidc-api-secret'         #needed
                        successUri: "/oauth2/google/success"    #optional
                        failureUri: "/oauth2/google/failure"    #optional
                        callback: "/oauth2/google/callback"     #optional
                        scopes: "some_scope"                    #optional, see https://developers.google.com/identity/protocols/googlescopes#monitoringv3
```
You can replace the URIs with your own controller implementation.

In your view you can use the taglib exposed from this plugin and from OAuth plugin to create links and to know if the user is authenticated with a given provider:
```xml
<oauth2:connect provider="google" id="google-connect-link">Google</oauth2:connect>

Logged with google?
<oauth2:ifLoggedInWith provider="google">yes</oauth2:ifLoggedInWith>
<oauth2:ifNotLoggedInWith provider="google">no</oauth2:ifNotLoggedInWith>
```
License
-------
Apache 2