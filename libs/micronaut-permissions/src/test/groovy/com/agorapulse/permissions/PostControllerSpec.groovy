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

import com.agorapulse.gru.Gru
import io.micronaut.test.annotation.MicronautTest
import spock.lang.AutoCleanup
import spock.lang.Specification

import javax.inject.Inject

@MicronautTest
@SuppressWarnings([
    'BuilderMethodWithSideEffects',
    'FactoryMethodName',
])
class PostControllerSpec extends Specification {

    private static final String AUTH_ID_1 = '1'
    private static final String AUTH_ID_2 = '2'
    private static final String HELLO_MESSAGE = 'Hello'

    @Inject @AutoCleanup Gru gru
    @Inject PostRepository postRepository

    void cleanup() {
        postRepository.clean()
    }

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
        given:
            Post post = Post.createDraft(AUTH_ID_1.toLong(), HELLO_MESSAGE)
            postRepository.save(post)
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
        given:
            Post post = Post.createDraft(AUTH_ID_1.toLong(), HELLO_MESSAGE)
            postRepository.save(post)
        expect:
            gru.test {
                put '/post/1', {
                    headers 'X-User-Id': AUTH_ID_1
                }
                expect {
                    json 'publishedPost.json'
                }
            }
    }

    void 'publish post with wrong auth'() {
        given:
            Post post = Post.createDraft(AUTH_ID_1.toLong(), HELLO_MESSAGE)
            postRepository.save(post)
        expect:
            gru.test {
                put '/post/1', {
                    headers 'X-User-Id': AUTH_ID_2
                }
                expect {
                    status UNAUTHORIZED
                    json 'failedPublish.json'
                }
            }
    }

    void 'archive post without any auth'() {
        given:
            Post post = Post.createDraft(AUTH_ID_1.toLong(), HELLO_MESSAGE)
            postRepository.save(post)
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
        given:
            Post post = Post.createDraft(AUTH_ID_1.toLong(), HELLO_MESSAGE)
            postRepository.save(post)
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

    void 'merge posts with one not allowed'() {
        given:
            postRepository.save(Post.createDraft(AUTH_ID_1.toLong(), HELLO_MESSAGE))
            postRepository.save(Post.createDraft(AUTH_ID_2.toLong(), HELLO_MESSAGE))
        expect:
            gru.test {
                post '/post/merge', {
                    headers 'X-User-Id': '1'
                    json id1: '1', id2: '2'
                }
                expect {
                    status UNAUTHORIZED
                    json 'failedMerge.json'
                }
            }
    }

    void 'merge posts'() {
        given:
            postRepository.save(Post.createDraft(AUTH_ID_1.toLong(), HELLO_MESSAGE))
            postRepository.save(Post.createDraft(AUTH_ID_1.toLong(), HELLO_MESSAGE))
        expect:
            gru.test {
                post '/post/merge', {
                    headers 'X-User-Id': '1'
                    json id1: '1', id2: '2'
                }
                expect {
                    status CREATED
                    json 'mergedPost.json'
                }
            }
    }

    void 'handle null iterable container'() {
        expect:
            gru.test {
                post '/post/handle-iterable-container', {
                    headers 'X-User-Id': '1'
                    json ids: null
                }
                expect {
                    status OK
                }
            }
    }

    void 'handle empty iterable container'() {
        expect:
            gru.test {
                post '/post/handle-iterable-container', {
                    headers 'X-User-Id': '1'
                    json ids: []
                }
                expect {
                    status OK
                }
            }
    }

    void 'handle iterable containing one not allowed'() {
        given:
            postRepository.save(Post.createDraft(AUTH_ID_1.toLong(), HELLO_MESSAGE))
            postRepository.save(Post.createDraft(AUTH_ID_2.toLong(), HELLO_MESSAGE))
        expect:
            gru.test {
                post '/post/handle-iterable-container', {
                    headers 'X-User-Id': '1'
                    json ids: [1, 2]
                }
                expect {
                    status UNAUTHORIZED
                }
            }
    }

    void 'handle iterable container containing only allowed'() {
        given:
            postRepository.save(Post.createDraft(AUTH_ID_1.toLong(), HELLO_MESSAGE))
            postRepository.save(Post.createDraft(AUTH_ID_1.toLong(), HELLO_MESSAGE))
        expect:
            gru.test {
                post '/post/handle-iterable-container', {
                    headers 'X-User-Id': '1'
                    json ids: [1, 2]
                }
                expect {
                    status OK
                }
            }
    }

    void 'ignore non iterable container'() {
        expect:
            gru.test {
                get '/post/handle-non-iterable-container', {
                    headers 'X-User-Id': '1'
                }
                expect {
                    status OK
                }
            }
    }

    void 'view post without any auth'() {
        expect:
            gru.test {
                get '/post/1'
                expect {
                    status UNAUTHORIZED
                    json 'viewFailed.json'
                }
            }
    }

    void 'view post with auth'() {
        expect:
            gru.test {
                get '/post/1', {
                    headers 'X-User-Id': '1'
                }
                expect {
                    json 'existingPost.json'
                }
            }
    }

}
