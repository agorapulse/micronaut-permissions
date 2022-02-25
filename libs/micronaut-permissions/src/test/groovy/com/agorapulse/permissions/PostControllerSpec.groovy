/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2022 Agorapulse.
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

import com.agorapulse.gru.Gru
import io.micronaut.test.annotation.MicronautTest
import spock.lang.AutoCleanup
import spock.lang.Specification
import spock.lang.Stepwise

import javax.inject.Inject

@Stepwise
@MicronautTest
@SuppressWarnings([
    'BuilderMethodWithSideEffects',
    'FactoryMethodName',
])
class PostControllerSpec extends Specification {

    @Inject @AutoCleanup Gru gru

    void 'create post without any auth'() {
        expect:
            gru.test {
                post '/post', {
                    json message: 'Hello'
                }
                expect {
                    status BAD_REQUEST
                    json 'creationFailed.json'
                }
            }
    }

    void 'create post with auth'() {
        expect:
            gru.test {
                post '/post', {
                    headers 'X-User-Id': '1'
                    json message: 'Hello'
                }
                expect {
                    status CREATED
                    json 'newPost.json'
                }
            }
    }

    void 'publish post without any auth'() {
        expect:
            gru.test {
                put '/post/1'
                expect {
                    status UNAUTHORIZED
                    json 'failedPublish.json'
                }
            }
    }

    void 'publish post'() {
        expect:
            gru.test {
                put '/post/1', {
                    headers 'X-User-Id': '1'
                }
                expect {
                    json 'publishedPost.json'
                }
            }
    }

    void 'archive post without any auth'() {
        expect:
            gru.test {
                delete '/post/1'
                expect {
                    status UNAUTHORIZED
                    json 'failedArchive.json'
                }
            }
    }

    void 'archive post'() {
        expect:
            gru.test {
                delete '/post/1', {
                    headers 'X-User-Id': '1'
                }
                expect {
                    json 'archivedPost.json'
                }
            }
    }

}
