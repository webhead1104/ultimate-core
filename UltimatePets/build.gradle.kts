version = "5.2.8"

repositories {
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    implementation("com.github.cryptomorin:XSeries:11.2.1")
    implementation("de.tr7zw:item-nbt-api:2.13.2")
    implementation("com.zaxxer:HikariCP:5.0.1")

    compileOnly(project(":UltimateSkills"))

    compileOnly("org.spigotmc:spigot-api:1.13.2-R0.1-SNAPSHOT")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("me.clip:placeholderapi:2.11.6")
    compileOnly("com.github.Archy-X:AureliumSkills:Beta1.1.3")

    implementation(project(":UltimateHelper"))
}

tasks {
    named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
        relocate("de.tr7zw", "mc.ultimatecore.pets.depends")
    }
}