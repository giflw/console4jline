package com.itquasar.multiverse.proton.commands

import com.itquasar.multiverse.proton.InterCommunication
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
        def interComm = command.invoke(
                cmdLine,
                null,
                InterCommunication.ok()
        )

        then: "check commad result"
        interComm.result.get() == message
    }
}
