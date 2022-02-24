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
