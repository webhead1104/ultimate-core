dependencies {
    compileOnly("org.spigotmc:spigot:1.20.6-R0.1-SNAPSHOT")
    compileOnly(project(":UltimateFarm:UltimateFarmNMS"))
}

java {
    toolchain {
        toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    }
}