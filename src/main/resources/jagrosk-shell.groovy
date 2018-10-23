name = "JaGroSK"
mask = 0

prompt {
    left = "${_.bg(_.CYAN, _.fg(_.BLACK, '[' + _.console.terminal.name + ']'))} ${_.bgfg(_.RED, _.WHITE, _.user)}@${_.cwd}> "
    right = "[${-> _.datetime} ${_.FOO}]"
}
