mapOf(
    "composeVersion" to "1.0.0",
    "hiltVersion" to "2.38.1",
    "hiltVersionPlugin" to "2.38.1",
    "hiltNavigation" to "1.0.0-alpha03",
    "navigation" to "2.4.0-alpha05"
).forEach { (name, version) ->
    project.extra.set(name, version)
}