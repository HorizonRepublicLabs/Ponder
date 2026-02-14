group = "net.createmod.ponder"

val buildNumber: String = providers.environmentVariable("BUILD_NUMBER").orElse("99999").get()
version = "1.0.$buildNumber+mc${rootProject.libs.versions.minecraft.get()}"

// compute once, read in subprojects
ext["git_hash"] = calculateGitHash() + (if (hasUnstaged()) "-modified" else "")

fun calculateGitHash(): String {
    try {
        val output = providers.exec {
            commandLine("git", "rev-parse", "HEAD")
        }
        return output.standardOutput.asText.get().trim()
    } catch (_: Throwable) {
        return "unknown"
    }
}

fun hasUnstaged(): Boolean {
    try {
        val output = providers.exec {
            commandLine("git", "status", "--porcelain")
        }

        val result = output.standardOutput.asText.get().replace("/M gradlew(\\.bat)?/", "").trim()
        return !result.isEmpty()
    } catch (_: Throwable) {
        return false
    }
}
