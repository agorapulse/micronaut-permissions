/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2023 Agorapulse.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.agorapulse.permissions

import io.micronaut.test.annotation.MicronautTest
import spock.lang.Specification

import javax.inject.Inject

@MicronautTest
class AdministratorPostServiceSpec extends Specification {

    @Inject AdministratorPostService administratorPostService
    @Inject PostService postService

    void 'cannot archive without user present using the normal service'() {
        when:
            postService.archive(postService.create(1, 'Hello'))
        then:
            thrown(PermissionException)
    }

    void 'can archive using the administrator service'() {
        when:
            administratorPostService.archive(postService.create(1, 'Hello'))
        then:
            noExceptionThrown()
    }

    void 'cannot publish without user present using the normal service'() {
        when:
            postService.publish(postService.create(1, 'Hello'))
        then:
            thrown(PermissionException)
    }

    void 'can publish using the administrator service'() {
        when:
            administratorPostService.publish(postService.create(1, 'Hello'))
        then:
            noExceptionThrown()
    }

}
