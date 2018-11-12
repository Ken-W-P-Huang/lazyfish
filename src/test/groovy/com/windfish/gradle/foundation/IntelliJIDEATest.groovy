package com.windfish.gradle.foundation

import com.windfish.gradle.AbstractProjectTest

/**
 * Created by kenhuang on 2018/11/1.
 */
class IntelliJIDEATest extends AbstractProjectTest {
    def "InitVcs"() {
        given:
        when:

        new IntelliJIDEA(this.project).initVcs('git', [remote: 'ssh://git@phabricator.windfish.club:2222/diffusion/13/imassage.git'])
        then:
        this.project.file('.gitignore').exists()
    }

    def "SetPhases"() {

    }
}
