dependencies {
    compileOnly("org.spigotmc:spigot:1.18.2-R0.1-SNAPSHOT")
    compileOnly(project(":UltimateDragon:UltimateDragonNMS"))
}

java {
    toolchain {
        toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    }
}