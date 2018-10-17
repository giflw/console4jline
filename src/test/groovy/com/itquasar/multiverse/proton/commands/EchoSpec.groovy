package com.itquasar.multiverse.proton.commands

import picocli.CommandLine
import spock.lang.Specification

class EchoSpec extends Specification {

    def "echo spec"() {

        given: "create command"
        def command = new Echo()
        def cmdLine = new CommandLine(command)
        cmdLine.parse(["foo bar", "baz"] as String[])
        def message = "foo bar baz"

        when: "invoke command"
        def result = command.invoke(
                cmdLine,
                null,
                Optional.empty()
        )

        then: "check commad result"
        result.get() == message
    }
}
