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

security {
    oauth2 {
        providers {
            oidc {
                domain = 'https://some.oidc.provider.domain' // Mandatory
                api_key = "changeme_apikey" // Mandatory 
                api_secret = "changeme_apisecret" // Mandatory
                successUri = "/oauth2/oidc/success" // Optional
                failureUri = "/oauth2/oidc/failure" // Optional
                callback = "/oauth2/oidc/callback" // Optional
            }
        }
    }
}