plugins {
    kotlin("jvm")
}

group = "com.example.librarymanagementsystem"
version = "1.0.0"

dependencies {
    implementation(project(":common"))
    testImplementation(libs.junit)
    testImplementation(kotlin("test"))
}
